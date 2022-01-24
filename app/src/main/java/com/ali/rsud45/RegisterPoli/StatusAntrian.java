package com.ali.rsud45.RegisterPoli;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.rsud45.R;

public class StatusAntrian extends AppCompatActivity {
TextView no_registrasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_antrian);
        no_registrasi=(TextView)findViewById(R.id.no_registrasi);
        Intent intent = getIntent();
        no_registrasi.setText("#"+intent.getExtras().getString("registrasi_id"));
    }
}