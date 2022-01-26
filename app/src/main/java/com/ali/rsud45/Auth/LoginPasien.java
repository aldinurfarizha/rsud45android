package com.ali.rsud45.Auth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ali.rsud45.Config.ServerUrl;
import com.ali.rsud45.Dashboard.DashboardDokter;
import com.ali.rsud45.Dashboard.DashboardPasien;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.R;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginPasien extends AppCompatActivity {
    EditText nomor_rekam_medis;
    Button masuk;
    TextView dokter;
    RelativeLayout daftar;
    ProgressDialog dialog;
    String message;
    long oneday=86400000;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pasien);
        SessionCheck();
        nomor_rekam_medis = (EditText)findViewById(R.id.nomor_rekam_medis);
        masuk = (Button)findViewById(R.id.masuk);
        dokter = (TextView)findViewById(R.id.dokter);
        daftar = (RelativeLayout)findViewById(R.id.daftar);
        AndroidNetworking.initialize(getApplicationContext());
        checkAndRequestPermissions();
        dialog=new ProgressDialog(LoginPasien.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String no_rm = nomor_rekam_medis.getText().toString();
                if(TextUtils.isEmpty(no_rm)){
                    nomor_rekam_medis.setError("Kolom ini tidak boleh kosong");
                    return;
                }
                dialog.show();
                AndroidNetworking.post(ServerUrl.LOGIN_PASIEN)
                        .addBodyParameter("no_rm", nomor_rekam_medis.getText().toString())
                        .setTag("Login Prosses")
                        .setPriority(Priority.IMMEDIATE)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                dialog.dismiss();
                                try {
                                    message = response.getString("message");
                                    if(response.getBoolean("login")){
                                        Date date = new Date();
                                        long timeMilli = date.getTime();
                                        long exp_until=timeMilli+oneday;
                                        JSONObject arr =response.getJSONObject("data");
                                        Credential credential= new Credential(
                                                arr.getInt("no_rm"),
                                                0,
                                                0,
                                                exp_until,
                                                arr.getString("nama"),
                                                arr.getString("tmplahir")+", "+arr.getString("tgllahir"),
                                                arr.getString("alamat")
                                        );
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(credential);
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), DashboardPasien.class));
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
        dokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginDokter.class));
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DaftarBaru.class));
            }
        });
    }
    private  boolean checkAndRequestPermissions() {
        int Camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int File = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (File != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (File != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (Camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void SessionCheck(){
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Credential credential;
            credential = SharedPrefManager.getInstance(this).get();
            switch (credential.getRole()){
                case 5:
                    startActivity(new Intent(getApplicationContext(), DashboardDokter.class));
                    break;
                case 0:
                    startActivity(new Intent(getApplicationContext(), DashboardPasien.class));
                    break;
                case 1:
                    startActivity(new Intent(getApplicationContext(), DaftarWaiting.class));
                    break;
            }
        }
    }
}