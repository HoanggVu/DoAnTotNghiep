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
import com.example.bookingcalender.Interface.IClickItemDoctorListener;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.Model.Doctor;
import com.example.bookingcalender.Model.LichLamViec;
import com.example.bookingcalender.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class SearchDoctorRecyclerAdapter extends RecyclerView.Adapter<SearchDoctorRecyclerAdapter.SearchDoctorHolder> {
    Context context;
    ArrayList<LichLamViec> lichLamViecArrayList;
    IClickItemDoctorListener iClickItemDoctorListener;

    public SearchDoctorRecyclerAdapter(Context context, ArrayList<LichLamViec> lichLamViecArrayList, IClickItemDoctorListener iClickItemDoctorListener) {
        this.context = context;
        this.lichLamViecArrayList = lichLamViecArrayList;
        this.iClickItemDoctorListener = iClickItemDoctorListener;
    }
    @NonNull
    @Override
    public SearchDoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_recycler_view, parent, false);
        return new SearchDoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDoctorHolder holder, int position) {
        final LichLamViec lichLamViec = lichLamViecArrayList.get(position);
        if (lichLamViec == null){
            return;
        }
        Glide.with(context).load(lichLamViec.getAvatarDoctor()).into(holder.imgAvtDoctor);
        holder.name.setText(lichLamViec.getNameDoctor());
        holder.khoa.setText(lichLamViec.getKhoaDoctor());
        holder.ngayLamVien.setText(lichLamViec.getNgayLV());
        holder.thoiGianTu.setText(lichLamViec.gettGLVTu());
        holder.thoiGianDen.setText(lichLamViec.gettGLVDen());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemDoctorListener.onClickItemDoctor(lichLamViec);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lichLamViecArrayList.size();
    }

    class SearchDoctorHolder extends RecyclerView.ViewHolder{

        ImageView imgAvtDoctor;
        TextView khoa;
        TextView name;
        TextView ngayLamVien;
        TextView thoiGianTu;
        TextView thoiGianDen;
        LinearLayout linearLayout;
        public SearchDoctorHolder(@NonNull View itemView) {
            super(itemView);
            imgAvtDoctor = itemView.findViewById(R.id.img_doctor_recycler_view);
            khoa = itemView.findViewById(R.id.tv_doctor_khoa_recycler_view);
            name = itemView.findViewById(R.id.tv_doctor_name_recycler_view);
            ngayLamVien = itemView.findViewById(R.id.tv_doctor_ngay_lam_viec_recycler_view);
            thoiGianTu = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_recycler_view);
            thoiGianDen = itemView.findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_recycler_view);
            linearLayout = itemView.findViewById(R.id.doctor_recycler_view);
        }
    }
}
