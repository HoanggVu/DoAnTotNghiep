package com.example.bookingcalender.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Interface.IClickItemUserSearchListener;
import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.Model.BacSi;
import com.example.bookingcalender.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class UserSearchRecyclerAdapter extends RecyclerView.Adapter<UserSearchRecyclerAdapter.SearchDoctorHolder> {

    Context context;
    ArrayList<BacSi> bacSiArrayList;
    IClickItemUserSearchListener iClickItemUserSearchListener;

    public UserSearchRecyclerAdapter(ArrayList<BacSi> bacSiArrayList, Context context, IClickItemUserSearchListener iClickItemUserSearchListener) {
        this.bacSiArrayList = bacSiArrayList;
        this.context = context;
        this.iClickItemUserSearchListener = iClickItemUserSearchListener;
    }

    @NonNull
    @Override
    public SearchDoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_search_doctor_recycler_view, parent, false);
        return new SearchDoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDoctorHolder holder, int position) {
        final BacSi bacSi = bacSiArrayList.get(position);
        if (bacSi == null){
            return;
        }
        holder.name.setText(bacSi.getHoTen());
        holder.khoa.setText(bacSi.getChuyenKhoa());
        Glide.with(context).load(bacSi.getAvatar()).into(holder.imgAvtDoctor);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemUserSearchListener.onClickItemUserSearch(bacSi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bacSiArrayList.size();
    }

    class SearchDoctorHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView khoa;
        ImageView imgAvtDoctor;
        RelativeLayout relativeLayout;
        public SearchDoctorHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_user_search_name_doctor);
            khoa = itemView.findViewById(R.id.tv_user_search_khoa_doctor);
            imgAvtDoctor = itemView.findViewById(R.id.img_avt_doctor_user_search_recycler_view);
            relativeLayout = itemView.findViewById(R.id.item_user_search_doctor);
        }
    }
}
