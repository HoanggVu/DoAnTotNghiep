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
import com.example.bookingcalender.Interface.IClickItemPhieuThanhToanListener;
import com.example.bookingcalender.Model.PhieuThanhToan;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class UserPhieuThanhToanRecyclerAdapter extends RecyclerView.Adapter<UserPhieuThanhToanRecyclerAdapter.PhieuThanhToanHolder>{

    Context context;
    ArrayList<PhieuThanhToan> phieuThanhToanArrayList;
    IClickItemPhieuThanhToanListener iClickItemPhieuThanhToanListener;

    public UserPhieuThanhToanRecyclerAdapter(ArrayList<PhieuThanhToan> phieuThanhToanArrayList, Context context, IClickItemPhieuThanhToanListener iClickItemPhieuThanhToanListener) {
        this.phieuThanhToanArrayList = phieuThanhToanArrayList;
        this.context = context;
        this.iClickItemPhieuThanhToanListener = iClickItemPhieuThanhToanListener;
    }

    @NonNull
    @Override
    public PhieuThanhToanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_phieu_thanh_toan_recycler_view, parent, false);
        return new PhieuThanhToanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhieuThanhToanHolder holder, int position) {
        final PhieuThanhToan phieuThanhToan = phieuThanhToanArrayList.get(position);
        if (phieuThanhToan == null){
            return;
        }

        holder.trangthai.setText(phieuThanhToan.getTrangThai());
        holder.ngaykham.setText(phieuThanhToan.getNgayKham());
        holder.giokham.setText(phieuThanhToan.gettGKham());
        Glide.with(context).load(phieuThanhToan.getAvatarDoctor()).into(holder.avtDoctor);
        holder.tenDoctor.setText(phieuThanhToan.getNameDoctor());
        holder.khoaDoctor.setText(phieuThanhToan.getKhoaDoctor());
        holder.ngaylamViecDoctor.setText(phieuThanhToan.getNgayLV());
        holder.thoiGianBatDau.setText(phieuThanhToan.gettGLVTu());
        holder.thoiGianKetThuc.setText(phieuThanhToan.gettGLVDen());
        holder.ngaythanhtoan.setText(phieuThanhToan.getNgayThanhToan());
        holder.giothanhtoan.setText(phieuThanhToan.gettGThanhToan());
        holder.dichvukham.setText(phieuThanhToan.getDichVuKham());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemPhieuThanhToanListener.onClickItemPhieuThanhToan(phieuThanhToan);
            }
        });
    }
    @Override
    public int getItemCount() {
        return phieuThanhToanArrayList.size();
    }

    static class PhieuThanhToanHolder extends RecyclerView.ViewHolder{
        ImageView avtDoctor;
        TextView trangthai;
        TextView ngaykham;
        TextView giokham;
        TextView tenDoctor;
        TextView khoaDoctor;
        TextView ngaylamViecDoctor;
        TextView thoiGianBatDau;
        TextView thoiGianKetThuc;
        TextView dichvukham;
        TextView ngaythanhtoan;
        TextView giothanhtoan;
        LinearLayout linearLayout;

        public PhieuThanhToanHolder(@NonNull View itemView) {
            super(itemView);
            avtDoctor = itemView.findViewById(R.id.img_doctor_user_phieu_thanh_toan);
            trangthai = itemView.findViewById(R.id.tv_user_phieu_thanh_toan_trang_thai);
            ngaykham = itemView.findViewById(R.id.tv_user_phieu_thanh_toan_ngay_kham);
            giokham = itemView.findViewById(R.id.tv_user_phieu_thanh_toan_thoi_gian_kham);
            tenDoctor = itemView.findViewById(R.id.tv_doctor_name_user_phieu_thanh_toan);
            khoaDoctor = itemView.findViewById(R.id.tv_doctor_khoa_user_phieu_thanh_toan);
            ngaylamViecDoctor = itemView.findViewById(R.id.tv_doctor_ngay_lam_viec_user_phieu_thanh_toan);
            thoiGianBatDau = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_user_phieu_thanh_toan);
            thoiGianKetThuc = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_user_phieu_thanh_toan);
            dichvukham = itemView.findViewById(R.id.tv_doctor_user_phieu_thanh_toan_user_dich_vu_kham);
            ngaythanhtoan = itemView.findViewById(R.id.tv_user_phieu_thanh_toan_ngay_thanh_toan);
            giothanhtoan = itemView.findViewById(R.id.tv_user_phieu_thanh_toan_thoi_gian_thanh_toan);
            linearLayout = itemView.findViewById(R.id.item_user_phieu_thanh_toan);
        }
    }
}
