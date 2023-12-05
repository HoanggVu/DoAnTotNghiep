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

public class UserDatLichRecyclerAdapter extends RecyclerView.Adapter<UserDatLichRecyclerAdapter.DatLichHolder>{

    Context context;
    ArrayList<DatLich> datLichArrayList;
    IClickItemDatLichListener iClickItemDatLichListener;

    public UserDatLichRecyclerAdapter(ArrayList<DatLich> datLichArrayList, Context context, IClickItemDatLichListener iClickItemDatLichListener) {
        this.datLichArrayList = datLichArrayList;
        this.context = context;
        this.iClickItemDatLichListener = iClickItemDatLichListener;
    }

    @NonNull
    @Override
    public DatLichHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_dat_lich_recycler_view, parent, false);
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
        holder.tgkham.setText(datLich.gettGDatKham());
        Glide.with(context).load(datLich.getAvatarBacSi()).into(holder.avtdoctor);
        holder.tendoctor.setText(datLich.getNameBacSi());
        holder.khoa.setText(datLich.getKhoaBacSi());
        holder.ngaylamviec.setText(datLich.getNgayLV());
        holder.tglvtu.setText(datLich.gettGLVTu());
        holder.tglvden.setText(datLich.gettGLVDen());
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
        ImageView avtdoctor;
        TextView trangthai;
        TextView ngaykham;
        TextView tgkham;
        TextView ngaylamviec;
        TextView tglvtu;
        TextView tglvden;
        TextView khoa;
        TextView tendoctor;
        TextView dichvukham;
        LinearLayout linearLayout;

        public DatLichHolder(@NonNull View itemView) {
            super(itemView);
            avtdoctor = itemView.findViewById(R.id.img_doctor_user_dat_lich);
            trangthai = itemView.findViewById(R.id.tv_user_dat_lich_trang_thai);
            ngaykham = itemView.findViewById(R.id.tv_user_dat_lich_ngay_kham);
            tgkham = itemView.findViewById(R.id.tv_user_dat_lich_thoi_gian_kham);
            khoa = itemView.findViewById(R.id.tv_doctor_khoa_user_dat_lich);
            tendoctor = itemView.findViewById(R.id.tv_doctor_name_user_dat_lich);
            ngaylamviec = itemView.findViewById(R.id.tv_doctor_ngay_lam_viec_benh_an);
            tglvtu = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_user_dat_lich);
            tglvden = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_user_dat_lich);
            dichvukham = itemView.findViewById(R.id.tv_user_dat_lich_dich_vu_kham);
            linearLayout = itemView.findViewById(R.id.item_user_dat_lich);
        }
    }
}
