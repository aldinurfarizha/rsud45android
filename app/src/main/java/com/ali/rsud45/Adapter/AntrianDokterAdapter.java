package com.ali.rsud45.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.rsud45.Model.AntrianDokterModel;
import com.ali.rsud45.R;

import java.util.ArrayList;

public class AntrianDokterAdapter extends RecyclerView.Adapter<AntrianDokterAdapter.ItemViewHolder> {

    private ArrayList<AntrianDokterModel> dataList;
    Context context;
    Boolean isfirst=true;

    public AntrianDokterAdapter(ArrayList<AntrianDokterModel> dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_antrian_dokter, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final Context context = holder.card.getContext();
        if(isfirst){
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.secondary));
            holder.sub_pasien.setTextColor(Color.WHITE);
            holder.date.setTextColor(Color.WHITE);
            holder.total_pasien.setTextColor(Color.WHITE);
            isfirst=false;
            holder.date.setText("Hari ini");
            holder.total_pasien.setText(dataList.get(position).getTotal_pasien());
            return;
        }
        holder.card.setCardBackgroundColor(Color.WHITE);
        holder.sub_pasien.setTextColor(Color.BLACK);
        holder.date.setTextColor(Color.BLACK);
        holder.total_pasien.setTextColor(Color.BLACK);
    holder.date.setText(dataList.get(position).getDate());
    holder.total_pasien.setText(dataList.get(position).getTotal_pasien());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView date, total_pasien, sub_pasien;
        private CardView card;
        public ItemViewHolder(View itemView) {
            super(itemView);
            date=(TextView) itemView.findViewById(R.id.date);
            total_pasien=(TextView) itemView.findViewById(R.id.pasien);
            card = (CardView)itemView.findViewById(R.id.card);
            sub_pasien = (TextView)itemView.findViewById(R.id.sub_pasien);
        }
    }
}