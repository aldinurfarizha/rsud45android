package com.ali.rsud45.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.rsud45.Model.PoliModel;
import com.ali.rsud45.R;
import com.ali.rsud45.RegisterPoli.DaftarPoli;

import java.util.ArrayList;

public class PoliAdapter extends RecyclerView.Adapter<PoliAdapter.ItemViewHolder> {

    private ArrayList<PoliModel> dataList;
    Context context;

    public PoliAdapter(ArrayList<PoliModel> dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_poli, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final Context context = holder.card.getContext();
    holder.nama_poli.setText(dataList.get(position).getPoli());
    holder.card.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context.getApplicationContext(), DaftarPoli.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            final String id = dataList.get(position).getPoli_id();
            final String nama_poli= dataList.get(position).getPoli();
            intent.putExtra("id",id);
            intent.putExtra("nama_poli",nama_poli);
            context.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView nama_poli;
        private CardView card;
        public ItemViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.card);
            nama_poli=(TextView) itemView.findViewById(R.id.nama_poli);
        }
    }
}