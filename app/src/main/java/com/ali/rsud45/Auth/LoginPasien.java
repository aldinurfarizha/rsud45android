package com.ali.rsud45.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.Dashboard.DashboardDokter;
import com.ali.rsud45.Dashboard.DashboardPasien;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.R;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;

public class LoginPasien extends AppCompatActivity {
    EditText nomor_rekam_medis;
    Button masuk;
    TextView dokter;
    RelativeLayout daftar;
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