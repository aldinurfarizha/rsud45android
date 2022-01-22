package com.ali.rsud45.RegisterPoli;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.ali.rsud45.Model.AntrianDokterModel;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.R;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DaftarPoli extends AppCompatActivity {
    TextView nama_poli;
    Credential credential;
    String namapoli, poli_id;
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
    }
    public void getDokter(String poli_id){
        dialog.show();
        AndroidNetworking.post(ServerUrl.GET_DOKTER)
                .addBodyParameter("id_poli", poli_id)
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

}