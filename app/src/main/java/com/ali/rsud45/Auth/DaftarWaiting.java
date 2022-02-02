package com.ali.rsud45.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.Config.ExpiredSession;
import com.ali.rsud45.Config.ServerUrl;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DaftarWaiting extends AppCompatActivity {
Button cekstatus, daftarulang;
Credential credential;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_waiting);
        cekstatus=(Button)findViewById(R.id.cekstatus);
        daftarulang=(Button)findViewById(R.id.daftar_ulang);
        daftarulang.setVisibility(View.GONE);
        daftarulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
        credential=SharedPrefManager.getInstance(this).get();
        cekstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
        dialog=new ProgressDialog(DaftarWaiting.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
    }
    public void sendData(){
        final String id =""+credential.getId();
        dialog.show();
        AndroidNetworking.post(ServerUrl.STATUS_PASIEN)
                .addBodyParameter("id", id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            if(response.getBoolean("success")) {
                                switch (response.getString("status")) {
                                    case "0":
                                        Toast.makeText(getApplicationContext(), "Mohon Bersabar Akun Anda Sedang tahap Verifikasi", Toast.LENGTH_LONG).show();
                                        break;
                                    case "2":
                                        Toast.makeText(getApplicationContext(), "Akun Anda di tolak dengan Alasan:" + response.getString("alasan_penolakan"), Toast.LENGTH_LONG).show();
                                        daftarulang.setVisibility(View.VISIBLE);
                                        break;
                                    default:
                                        Date date = new Date();
                                        long timeMilli = date.getTime();
                                        long exp_until = timeMilli + ExpiredSession.DURATION;
                                        Credential credentials = new Credential(
                                                response.getString("no_rm"),
                                                0,
                                                0,
                                                exp_until,
                                                credential.getNama(),
                                                credential.getUsername(),
                                                credential.getOff_name()
                                        );
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(credentials);
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), DashboardPasien.class));
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Koneksi Error", Toast.LENGTH_LONG).show();
                        Log.e("error:", error.toString());
                    }
                });
    }

}