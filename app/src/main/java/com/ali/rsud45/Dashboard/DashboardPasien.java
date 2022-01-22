package com.ali.rsud45.Dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.rsud45.Adapter.AntrianDokterAdapter;
import com.ali.rsud45.Adapter.PoliAdapter;
import com.ali.rsud45.Auth.LoginPasien;
import com.ali.rsud45.Config.ServerUrl;
import com.ali.rsud45.Model.Credential;
import com.ali.rsud45.Model.PoliModel;
import com.ali.rsud45.R;
import com.ali.rsud45.RegisterPoli.StatusAntrian;
import com.ali.rsud45.Util.SharedPrefManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardPasien extends AppCompatActivity {
ImageView logout;
TextView nama, dob, alamat, norm;
Credential credential;
ProgressDialog dialog;
PoliAdapter poliAdapter;
RecyclerView recyclerView;
ArrayList<PoliModel> poliModels;
RelativeLayout status_antrian;
String id_periksa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pasien);
        logout=(ImageView)findViewById(R.id.logout);
        nama=(TextView)findViewById(R.id.nama);
        dob=(TextView)findViewById(R.id.dob);
        alamat=(TextView)findViewById(R.id.alamat);
        norm = (TextView)findViewById(R.id.rm);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        status_antrian = (RelativeLayout)findViewById(R.id.status_antrian);
        poliModels = new ArrayList<>();
        credential = SharedPrefManager.getInstance(this).get();
        nama.setText(credential.getNama());
        dob.setText(credential.getUsername());
        alamat.setText(credential.getOff_name());
        norm.setText("No RM:"+credential.getId());
        AndroidNetworking.initialize(getApplicationContext());
        dialog=new ProgressDialog(DashboardPasien.this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
            }
        });
        getData();
    }
    public void getData(){
        dialog.show();
        AndroidNetworking.post(ServerUrl.DASHBOARD_PASIEN)
                .addBodyParameter("no_rm", ""+credential.getId())
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray array = jsonObject.getJSONArray("poli");
                            id_periksa=response.getString("id_periksa");
                            if(response.getBoolean("is_antri")){
                                status_antrian.setVisibility(View.VISIBLE);
                                status_antrian.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(DashboardPasien.this, StatusAntrian.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("id_periksa", id_periksa);
                                        startActivity(intent);
                                    }
                                });
                            }
                            for (int i=0; i<array.length(); i++){
                                JSONObject ob=array.getJSONObject(i);
                                PoliModel poliModel=new PoliModel(ob.getString("poli_id"),ob.getString("nama_poli"));
                                poliModels.add(poliModel);
                            }
                            poliAdapter = new PoliAdapter(poliModels);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DashboardPasien.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(poliAdapter);
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