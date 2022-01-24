package com.ali.rsud45.RegisterPoli;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.rsud45.Adapter.AntrianDokterAdapter;
import com.ali.rsud45.Config.ServerUrl;
import com.ali.rsud45.Dashboard.DashboardDokter;
import com.ali.rsud45.Dashboard.DashboardPasien;
import com.ali.rsud45.Model.AntrianDokterModel;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.R;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaftarPoli extends AppCompatActivity {
    TextView nama_poli;
    Credential credential;
    String namapoli, poli_id, dokter_id, message;
    ProgressDialog dialog;
    private SimpleDateFormat dateFormatter;
    Spinner dokter, cara_bayar, tipe_pelayanan, cara_kunjungan;
    Button antri;
    EditText tanggal;
    List<String> ArrayNamaDokter = new ArrayList<String>();
    List<String> ArrayIdDokter = new ArrayList<String>();
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_poli);
        credential = SharedPrefManager.getInstance(this).get();
        Intent intent = getIntent();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        poli_id=intent.getExtras().getString("id");
        namapoli=intent.getExtras().getString("nama_poli");
        nama_poli=(TextView)findViewById(R.id.nama_poli);
        cara_bayar=(Spinner)findViewById(R.id.cara_bayar);
        dokter=(Spinner)findViewById(R.id.dokter);
        tipe_pelayanan=(Spinner)findViewById(R.id.tipe_pelayanan);
        cara_kunjungan=(Spinner)findViewById(R.id.cara_kunjungan);
        antri=(Button)findViewById(R.id.antri);
        tanggal=(EditText)findViewById(R.id.tgl_periksa);
        nama_poli.setText("Daftar Ke Poli "+namapoli);
        dialog=new ProgressDialog(DaftarPoli.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        AndroidNetworking.initialize(getApplicationContext());
        dokter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dokter_id= ArrayIdDokter.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(DaftarPoli.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        tanggal.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
        getDokter(poli_id);
        antri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void getDokter(String poli_id){
        dialog.show();
        AndroidNetworking.post(ServerUrl.GET_DOKTER)
                .addBodyParameter("poli_id", poli_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray array = jsonObject.getJSONArray("data");
                            ArrayNamaDokter = new ArrayList<String>();
                            ArrayIdDokter = new ArrayList<String>();
                            ArrayNamaDokter.add("--Pilih Dokter--");
                            ArrayIdDokter.add("0");
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                               ArrayNamaDokter.add(ob.getString("nama"));
                               ArrayIdDokter.add(ob.getString("user_id"));
                            }
                          load_spinner();
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
    public void load_spinner(){
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ArrayNamaDokter);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dokter.setAdapter(adapters);
    }
    public void sendData() throws ParseException {
        final String dokter_ids = dokter_id;
        final String cara_bayars=cara_bayar.getSelectedItem().toString();
        final String tipe_pelayanans=tipe_pelayanan.getSelectedItem().toString();
        final String tgl_periksas=tanggal.getText().toString();
        final String cara_kunjungans=cara_kunjungan.getSelectedItem().toString();

        if(TextUtils.isEmpty(tgl_periksas)){
            openSnackbarMerah("Tanggal Periksa Tidak boleh kosong");
            return;
        }
        Date dateperiksa=new SimpleDateFormat("yyyy-MM-dd").parse(tgl_periksas);
        Date date= new Date();
        String datesekar = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Date datesekarang=new SimpleDateFormat("yyyy-MM-dd").parse(datesekar);

        if(dateperiksa.before(datesekarang)){
            openSnackbarMerah("Tanggal Periksa Tidak bisa di lakukan di masa lampau");
            return;
        }
        if(tgl_periksas.equals(datesekar)){
            Date date1= new Date();
            String jamsekar = new SimpleDateFormat("HH:mm:ss").format(date1);
            Date jamsekarang=new SimpleDateFormat("HH:mm:ss").parse(jamsekar);
            Date jammax=new SimpleDateFormat("HH:mm:ss").parse("15:00:00");
            if(jamsekarang.after(jammax)){
                openSnackbarMerah("Hari ini Poli Sudah Tutup");
                return;
            }
            openSnackbarbiru("Anda Melakukan Priksa hari ini");
        }
        if(dokter_ids.equals("0")){
            openSnackbarMerah("Pilih Dokter terlebih dahulu");
            return;
        }
        if(cara_bayars.equals("--Pilih Cara Bayar--")){
            openSnackbarMerah("Pilih Cara Bayar terlebih dahulu");
            return;
        }
        if(tipe_pelayanans.equals("--Pilih Tipe Pelayanan--")){
            openSnackbarMerah("Pilih Tipe Pelayanan terlebih dahulu");
            return;
        }
        if(cara_kunjungans.equals("--Pilih Cara Kunjungan--")){
            openSnackbarMerah("Pilih Cara Kunjungan terlebih dahulu");
            return;
        }
        dialog.show();
        AndroidNetworking.post(ServerUrl.ANTRI_POLI)
                .addBodyParameter("no_rm", ""+credential.getId())
                .addBodyParameter("dokter_id", dokter_ids)
                .addBodyParameter("cara_bayar", cara_bayars)
                .addBodyParameter("tipe_pelayanan", tipe_pelayanans)
                .addBodyParameter("tgl_periksa", tgl_periksas)
                .addBodyParameter("cara_kunjungan", cara_kunjungans)
                .addBodyParameter("poli_id", poli_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        try {
                            message = response.getString("message");
                            if(response.getBoolean("success")){
                                Intent intent = new Intent(DaftarPoli.this, StatusAntrian.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                final String id = response.getString("id");
                                intent.putExtra("id",id);
                                startActivity(intent);
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
    public void openSnackbarMerah(String messages) {
        Snackbar.make(findViewById(android.R.id.content), messages, Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show();
    }
    public void openSnackbarbiru(String messages) {
        Snackbar.make(findViewById(android.R.id.content), messages, Snackbar.LENGTH_LONG).setBackgroundTint(Color.BLUE).show();
    }
}