package com.ali.rsud45.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.Config.ServerUrl;
import com.ali.rsud45.Dashboard.DashboardDokter;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.R;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class LoginDokter extends AppCompatActivity {
    EditText username, password;
    Button masuk;
    ProgressDialog dialog;
    String message="", usernamess;
    Boolean loginstatus;
    long oneday=86400000; //satu hari dalam ms
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dokter);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        masuk = (Button)findViewById(R.id.masuk);
        dialog=new ProgressDialog(LoginDokter.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        AndroidNetworking.initialize(getApplicationContext());
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernames = username.getText().toString();
                final String passwords =  password.getText().toString();
                if (TextUtils.isEmpty(usernames)) {
                    username.setError("Kolom ini tidak boleh kosong");
                    username.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(passwords)) {
                    password.setError("Kolom ini tidak boleh kosong");
                    password.requestFocus();
                    return;
                }
                dialog.show();

                AndroidNetworking.post(ServerUrl.LOGIN_DOKTER)
                        .addBodyParameter("username", username.getText().toString())
                        .addBodyParameter("password", password.getText().toString())
                        .setTag("Login Prosses")
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                dialog.dismiss();
                                try {
                                    message = response.getString("message");
                                    loginstatus=response.getBoolean("login");
                                    if(loginstatus){
                                        Date date = new Date();
                                        long timeMilli = date.getTime();
                                        long exp_until=timeMilli+oneday;
                                        JSONObject arr =response.getJSONObject("data");
                                        usernamess= arr.getString("username");
                                        Credential credential= new Credential(
                                                arr.getString("user_id"),
                                                arr.getInt("role_id"),
                                                arr.getInt("poli_id"),
                                                exp_until,
                                                arr.getString("nama"),
                                                "DOKTER",
                                                arr.getString("nama_poli")
                                        );
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(credential);
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), DashboardDokter.class));
                                    }else{
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), message+error.toString(), Toast.LENGTH_LONG).show();
                                Log.e("error:", error.toString());
                            }
                        });
            }
        });
    }
}