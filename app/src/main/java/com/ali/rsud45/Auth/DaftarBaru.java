package com.ali.rsud45.Auth;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.R;

public class DaftarBaru extends AppCompatActivity {
EditText nama, nik, tempat_lahir, tanggal_lahir, dusun, rt, rw, no_telp, nama_ibu_kandung;
Spinner  provinsi, kabupaten, kecamatan, jenis_kelamin, pekerjaan, agama, pendidikan, status_martial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_baru);
        nama=(EditText)findViewById(R.id.nama);
        nik=(EditText)findViewById(R.id.nik);
        tempat_lahir=(EditText)findViewById(R.id.tempat_lahir);
        tanggal_lahir=(EditText)findViewById(R.id.tanggal_lahir);
        dusun=(EditText)findViewById(R.id.dusun);
        rt=(EditText)findViewById(R.id.rt);
        rw=(EditText)findViewById(R.id.rw);
        no_telp=(EditText)findViewById(R.id.no_telepon);
        nama_ibu_kandung=(EditText)findViewById(R.id.nama_ibu_kandung);
        provinsi=(Spinner)findViewById(R.id.provinsi);
        kabupaten=(Spinner)findViewById(R.id.kabupaten);
        kecamatan=(Spinner)findViewById(R.id.kecamatan);
        jenis_kelamin=(Spinner)findViewById(R.id.jenis_kelamin);
        pekerjaan=(Spinner)findViewById(R.id.pekerjaan);
        agama=(Spinner)findViewById(R.id.agama);
        pendidikan=(Spinner)findViewById(R.id.pendidikan);
        status_martial=(Spinner)findViewById(R.id.status_martial);

    }
}