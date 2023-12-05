package com.example.bookingcalender.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Interface.IClickItemDichVuListener;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.R;

import java.util.ArrayList;

public class SearchDichVuRecyclerAdapter  extends RecyclerView.Adapter<SearchDichVuRecyclerAdapter.DichvuHolder> {
    Context context;
    ArrayList<DichVu> dichVuArrayList;
    IClickItemDichVuListener iClickItemDichVuListener;

    public SearchDichVuRecyclerAdapter(Context context, ArrayList<DichVu> dichVuArrayList, IClickItemDichVuListener iClickItemDichVuListener) {
        this.context = context;
        this.dichVuArrayList = dichVuArrayList;
        this.iClickItemDichVuListener = iClickItemDichVuListener;
    }

    @NonNull
    @Override
    public DichvuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dich_vu_recycler_view, parent, false);
        return new DichvuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DichvuHolder holder, int position) {
        final DichVu dichVu = dichVuArrayList.get(position);
        if (dichVu == null){
            return;
        }

        holder.khoa.setText(dichVu.getKhoa());
        holder.dichVu.setText(dichVu.getDichVu());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemDichVuListener.onClickItemDichVu(dichVu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dichVuArrayList.size();
    }

    static class DichvuHolder extends RecyclerView.ViewHolder {

        TextView dichVu;
        TextView khoa;
        RelativeLayout relativeLayout;
        public DichvuHolder(@NonNull View itemView) {
            super(itemView);
            dichVu = itemView.findViewById(R.id.tv_dich_vu_dich_vu_kham);
            khoa = itemView.findViewById(R.id.tv_dich_vu_khoa);
            relativeLayout = itemView.findViewById(R.id.item_dich_vu);

        }
    }
}
