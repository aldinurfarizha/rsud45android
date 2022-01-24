package com.ali.rsud45.RegisterPoli;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.rsud45.Adapter.PoliAdapter;
import com.ali.rsud45.Config.ServerUrl;
import com.ali.rsud45.Dashboard.DashboardPasien;
import com.ali.rsud45.Model.PoliModel;
import com.ali.rsud45.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusAntrian extends AppCompatActivity {
TextView no_registrasi, nama_poli, tanggal_periksa, no_antrian, nama, tipe_pelayanan, antrian, keterangan_antrian, perbaharui;
String registrasi_id;
ProgressDialog dialog;
Button batal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_antrian);
        no_registrasi=(TextView)findViewById(R.id.no_registrasi);
        nama_poli=(TextView)findViewById(R.id.nama_poli);
        tanggal_periksa=(TextView)findViewById(R.id.tanggal_periksa);
        no_antrian=(TextView)findViewById(R.id.no_antrian);
        nama=(TextView)findViewById(R.id.nama);
        tipe_pelayanan=(TextView)findViewById(R.id.tipe_pelayanan);
        antrian=(TextView)findViewById(R.id.antrian);
        keterangan_antrian=(TextView)findViewById(R.id.keterangan_antrian);
        perbaharui=(TextView)findViewById(R.id.perbaharui);
        batal=(Button)findViewById(R.id.batal);

        Intent intent = getIntent();
        no_registrasi.setText("#"+intent.getExtras().getString("registrasi_id"));
        nama.setText(intent.getExtras().getString("nama_pasien"));
        tanggal_periksa.setText(intent.getExtras().getString("tanggal_periksa"));
        no_antrian.setText(intent.getExtras().getString("antrian_no"));
        nama_poli.setText(intent.getExtras().getString("nama_poli"));
        tipe_pelayanan.setText(intent.getExtras().getString("tipe_pelayanan"));
        registrasi_id=intent.getExtras().getString("registrasi_id");
        AndroidNetworking.initialize(getApplicationContext());
        dialog=new ProgressDialog(StatusAntrian.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        getData();
        perbaharui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StatusAntrian.this);
                builder.setTitle("Anda Yakin Membatalkan Antrian");
                builder.setPositiveButton("Ya Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        batalkan();
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    public void getData(){
        dialog.show();
        AndroidNetworking.post(ServerUrl.STATUS_ANTRIAN)
                .addBodyParameter("registrasi_id", registrasi_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        try {
                            if(response.getBoolean("success")){
                                antrian.setText(response.getString("antrian"));
                                keterangan_antrian.setVisibility(View.GONE);
                            }else{
                                antrian.setText("-");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error.toString());
                    }
                });
    }
    public void batalkan(){
        dialog.show();
        AndroidNetworking.post(ServerUrl.BATAL)
                .addBodyParameter("registrasi_id", registrasi_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        try {
                            if(response.getBoolean("success")){
                                finish();
                                Intent intent = new Intent(StatusAntrian.this, DashboardPasien.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                Toast.makeText(getApplicationContext(), "Registrasi Berhasil di batalkan", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }else{
                                antrian.setText("-");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("error:", error.toString());
                    }
                });
    }

}