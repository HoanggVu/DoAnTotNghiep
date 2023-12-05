package com.example.bookingcalender.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class DichVuRecyclerAdapter extends RecyclerView.Adapter<DichVuRecyclerAdapter.DichVuHolder>{

    Context context;
    ArrayList<DichVu> dichVuArrayList;

    public DichVuRecyclerAdapter(ArrayList<DichVu> dichVuArrayList, Context context) {
        this.dichVuArrayList = dichVuArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DichVuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dich_vu_recycler_view, parent, false);
        return new DichVuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DichVuHolder holder, int position) {
        holder.khoa.setText(dichVuArrayList.get(position).getKhoa());
        holder.dichVuKham.setText(dichVuArrayList.get(position).getDichVu());
    }

    @Override
    public int getItemCount() {
        return dichVuArrayList.size();
    }

    static class DichVuHolder extends RecyclerView.ViewHolder{
        TextView khoa;
        TextView dichVuKham;

        public DichVuHolder(@NonNull View itemView) {
            super(itemView);
            khoa = itemView.findViewById(R.id.tv_dich_vu_khoa);
            dichVuKham = itemView.findViewById(R.id.tv_dich_vu_dich_vu_kham);
        }
    }
}
