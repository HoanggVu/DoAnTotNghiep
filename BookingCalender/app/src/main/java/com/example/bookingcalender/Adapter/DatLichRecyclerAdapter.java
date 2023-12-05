package com.example.bookingcalender.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Interface.IClickItemDatLichListener;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class DatLichRecyclerAdapter extends RecyclerView.Adapter<DatLichRecyclerAdapter.DatLichHolder>{

    Context context;
    ArrayList<DatLich> datLichArrayList;
    IClickItemDatLichListener iClickItemDatLichListener;

    public DatLichRecyclerAdapter(ArrayList<DatLich> datLichArrayList, Context context, IClickItemDatLichListener iClickItemDatLichListener) {
        this.datLichArrayList = datLichArrayList;
        this.context = context;
        this.iClickItemDatLichListener = iClickItemDatLichListener;
    }

    @NonNull
    @Override
    public DatLichHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dat_lich_recycler_view, parent, false);
        return new DatLichHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DatLichHolder holder, int position) {
        final DatLich datLich = datLichArrayList.get(position);
        if (datLich == null){
            return;
        }

        holder.trangthai.setText(datLich.getTrangThai());
        holder.ngaykham.setText(datLich.getNgayDatKham());
        holder.giokham.setText(datLich.gettGDatKham());
        Glide.with(context).load(datLich.getAvatarUser()).into(holder.avtuser);
        holder.tenuser.setText(datLich.getNameUser());
        holder.sdtuser.setText(datLich.getDienThoaiUser());
        holder.dichvukham.setText(datLich.getDichVuKham());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemDatLichListener.onClickItemDatLich(datLich);
            }
        });
    }
    @Override
    public int getItemCount() {
        return datLichArrayList.size();
    }

    static class DatLichHolder extends RecyclerView.ViewHolder{
        ImageView avtuser;
        TextView trangthai;
        TextView ngaykham;
        TextView giokham;
        TextView tenuser;
        TextView sdtuser;
        TextView dichvukham;
        LinearLayout linearLayout;

        public DatLichHolder(@NonNull View itemView) {
            super(itemView);
            avtuser = itemView.findViewById(R.id.img_avt_user_dat_lich);
            trangthai = itemView.findViewById(R.id.tv_dat_lich_trang_thai);
            ngaykham = itemView.findViewById(R.id.tv_dat_lich_ngay_kham);
            giokham = itemView.findViewById(R.id.tv_dat_lich_thoi_gian_kham);
            tenuser = itemView.findViewById(R.id.tv_dat_lich_user_name);
            sdtuser = itemView.findViewById(R.id.tv_dat_lich_user_chuyen_khoa);
            dichvukham = itemView.findViewById(R.id.tv_dat_lich_user_dich_vu_kham);
            linearLayout = itemView.findViewById(R.id.item_dat_lich);
        }
    }
}
