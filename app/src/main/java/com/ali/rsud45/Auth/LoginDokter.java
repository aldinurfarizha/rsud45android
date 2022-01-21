package com.ali.rsud45.Auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.R;
import com.androidnetworking.AndroidNetworking;

public class LoginDokter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dokter);
        AndroidNetworking.initialize(getApplicationContext());
    }
}