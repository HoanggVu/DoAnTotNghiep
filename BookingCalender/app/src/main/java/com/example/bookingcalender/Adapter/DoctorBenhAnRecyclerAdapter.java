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
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class DoctorBenhAnRecyclerAdapter extends RecyclerView.Adapter<DoctorBenhAnRecyclerAdapter.BenhAnHolder>{

    Context context;
    ArrayList<BenhAn> benhAnArrayList;
    IClickItemBenhAnListener iClickItemBenhAnListener;

    public DoctorBenhAnRecyclerAdapter(ArrayList<BenhAn> benhAnArrayList, Context context, IClickItemBenhAnListener iClickItemBenhAnListener) {
        this.benhAnArrayList = benhAnArrayList;
        this.context = context;
        this.iClickItemBenhAnListener = iClickItemBenhAnListener;
    }

    @NonNull
    @Override
    public BenhAnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_benh_an_recycler_view, parent, false);
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
        Glide.with(context).load(benhAn.getAvatarUser()).into(holder.avtuser);
        holder.tenuser.setText(benhAn.getNameUser());
        holder.sdt.setText(benhAn.getDienThoaiUser());
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
        ImageView avtuser;
        TextView trangthai;
        TextView ngaykham;
        TextView giokham;
        TextView tenuser;
        TextView sdt;
        TextView dichvukham;
        LinearLayout linearLayout;

        public BenhAnHolder(@NonNull View itemView) {
            super(itemView);
            avtuser = itemView.findViewById(R.id.img_avt_user_doctor_benh_an);
            trangthai = itemView.findViewById(R.id.tv_doctor_benh_an_trang_thai);
            ngaykham = itemView.findViewById(R.id.tv_doctor_benh_an_ngay_kham);
            giokham = itemView.findViewById(R.id.tv_doctor_benh_an_thoi_gian_kham);
            tenuser = itemView.findViewById(R.id.tv_doctor_benh_an_user_name);
            sdt = itemView.findViewById(R.id.tv_doctor_benh_an_user_sdt);
            dichvukham = itemView.findViewById(R.id.tv_doctor_benh_an_user_dich_vu_kham);
            linearLayout = itemView.findViewById(R.id.item_doctor_benh_an);
        }
    }
}
