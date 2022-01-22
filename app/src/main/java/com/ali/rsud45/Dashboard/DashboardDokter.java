package com.ali.rsud45.Dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.rsud45.Adapter.AntrianDokterAdapter;
import com.ali.rsud45.Auth.LoginDokter;
import com.ali.rsud45.Config.ServerUrl;
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

import java.util.ArrayList;

public class DashboardDokter extends AppCompatActivity {
TextView nama_dokter, nama_poli;
Credential credential;
RecyclerView recyclerView;
ProgressDialog dialog;
AntrianDokterAdapter antrianDokterAdapter;
ArrayList<AntrianDokterModel> antrianDokterModels;
ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_dokter);
        antrianDokterModels = new ArrayList<>();
        nama_dokter = (TextView)findViewById(R.id.nama_dokter);
        nama_poli = (TextView)findViewById(R.id.nama_poli);
        credential = SharedPrefManager.getInstance(this).get();
        nama_dokter.setText(credential.getNama());
        nama_poli.setText(credential.getOff_name());
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        logout = (ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
        dialog=new ProgressDialog(DashboardDokter.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        AndroidNetworking.initialize(getApplicationContext());
        getData();
    }
    public void getData(){
        dialog.show();
        AndroidNetworking.post(ServerUrl.DASHBOARD_DOKTER)
                .addBodyParameter("id_poli", ""+credential.getOff_id())
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
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                AntrianDokterModel antrianDokterModel=new AntrianDokterModel(ob.getString("tanggal"),ob.getString("total_pasien"));
                                antrianDokterModels.add(antrianDokterModel);
                            }
                            antrianDokterAdapter = new AntrianDokterAdapter(antrianDokterModels);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashboardDokter.this, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(antrianDokterAdapter);
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