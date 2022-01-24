package com.ali.rsud45.Auth;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.R;

public class DaftarBaru extends AppCompatActivity {
EditText nama, nik, tempat_lahir, tanggal_lahir, dusun, rt, rw, no_telp, nama_ibu_kandung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_baru);
    }
}