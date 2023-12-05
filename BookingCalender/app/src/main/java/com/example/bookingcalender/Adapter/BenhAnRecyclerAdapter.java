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
import com.example.bookingcalender.Interface.IClickItemBenhAnListener;
import com.example.bookingcalender.Interface.IClickItemDatLichListener;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class BenhAnRecyclerAdapter extends RecyclerView.Adapter<BenhAnRecyclerAdapter.BenhAnHolder>{

    Context context;
    ArrayList<BenhAn> benhAnArrayList;
    IClickItemBenhAnListener iClickItemBenhAnListener;

    public BenhAnRecyclerAdapter(ArrayList<BenhAn> benhAnArrayList, Context context, IClickItemBenhAnListener iClickItemBenhAnListener) {
        this.benhAnArrayList = benhAnArrayList;
        this.context = context;
        this.iClickItemBenhAnListener = iClickItemBenhAnListener;
    }

    @NonNull
    @Override
    public BenhAnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.benh_an_recycler_view, parent, false);
        return new BenhAnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BenhAnHolder holder, int position) {
        final BenhAn benhAn = benhAnArrayList.get(position);
        if (benhAn == null){
            return;
        }

        holder.trangthai.setText(benhAn.getTrangThai());
        holder.ngaykham.setText(benhAn.getNgayKham());
        holder.giokham.setText(benhAn.gettGKham());
        Glide.with(context).load(benhAn.getAvatarBacSi()).into(holder.avtdoctor);
        holder.tendoctor.setText(benhAn.getNameBacSi());
        holder.khoa.setText(benhAn.getKhoaBacSi());
        holder.ngaylamviec.setText(benhAn.getNgayLV());
        holder.tglvtu.setText(benhAn.gettGLVTu());
        holder.tglvden.setText(benhAn.gettGLVDen());
        holder.dichvukham.setText(benhAn.getDichVuKham());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemBenhAnListener.onClickItemBenhAn(benhAn);
            }
        });
    }
    @Override
    public int getItemCount() {
        return benhAnArrayList.size();
    }

    static class BenhAnHolder extends RecyclerView.ViewHolder{
        ImageView avtdoctor;
        TextView trangthai;
        TextView ngaykham;
        TextView giokham;
        TextView tendoctor;
        TextView khoa;
        TextView ngaylamviec;
        TextView tglvtu;
        TextView tglvden;
        TextView dichvukham;
        LinearLayout linearLayout;

        public BenhAnHolder(@NonNull View itemView) {
            super(itemView);
            avtdoctor = itemView.findViewById(R.id.img_doctor_benh_an);
            trangthai = itemView.findViewById(R.id.tv_benh_an_trang_thai);
            ngaykham = itemView.findViewById(R.id.tv_benh_an_ngay_kham);
            giokham = itemView.findViewById(R.id.tv_benh_an_thoi_gian_kham);
            tendoctor = itemView.findViewById(R.id.tv_doctor_name_benh_an);
            khoa = itemView.findViewById(R.id.tv_doctor_khoa_benh_an);
            ngaylamviec = itemView.findViewById(R.id.tv_doctor_ngay_lam_viec_benh_an);
            tglvtu = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_benh_an);
            tglvden = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_benh_an);
            dichvukham = itemView.findViewById(R.id.tv_benh_an_user_dich_vu_kham);
            linearLayout = itemView.findViewById(R.id.item_benh_an);
        }
    }
}
