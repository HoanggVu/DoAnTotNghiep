package com.example.bookingcalender.User;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.bookingcalender.Adapter.BenhAnRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserDatLichRecyclerAdapter;
import com.example.bookingcalender.Doctor.BenhAnActivity;
import com.example.bookingcalender.Doctor.InfoBenhAnActivity;
import com.example.bookingcalender.Interface.IClickItemBenhAnListener;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BenhAnFragment extends Fragment {

    View mView;
    TextView tvChonNgay;
    RecyclerView recyclerView;
    ArrayList<BenhAn> benhAnArrayList;
    BenhAnRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String accountId = " ";
    String dateTime = " ";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_benh_an, container, false);

        initUi();
        initListener();
        setBenhAn();

        return mView;
    }

    private void initListener() {
        tvChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }

    private void initUi() {
        recyclerView = mView.findViewById(R.id.user_benh_an_recycler_view);
        tvChonNgay = mView.findViewById(R.id.tv_ngay_benh_an_user);
        progressDialog = new ProgressDialog(getContext());
    }
    private void setDate(){
        final Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
                dateTime = String.valueOf(simpleDateFormat.format(calendar.getTime()));
                setBenhAnTheoNgay();
            }
        }, year, month, date);
        datePickerDialog.show();
    }

    private void setBenhAnTheoNgay() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        accountId = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        benhAnArrayList = new ArrayList<>();
        adapter = new BenhAnRecyclerAdapter(benhAnArrayList, getContext(), new IClickItemBenhAnListener() {
            @Override
            public void onClickItemBenhAn(BenhAn benhAn) {
                onClickGoToDetail(benhAn);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("BenhAn").whereEqualTo("trangThai", "Đã Khám").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountId.equals(documentSnapshot.getString("accountIdUser"))){
                                if (dateTime.equals(documentSnapshot.getString("ngayKham"))){
                                    BenhAn benhAn = documentSnapshot.toObject(BenhAn.class);
                                    benhAnArrayList.add(benhAn);
                                }
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setBenhAn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        accountId = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        benhAnArrayList = new ArrayList<>();
        adapter = new BenhAnRecyclerAdapter(benhAnArrayList, getContext(), new IClickItemBenhAnListener() {
            @Override
            public void onClickItemBenhAn(BenhAn benhAn) {
                onClickGoToDetail(benhAn);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("BenhAn").whereEqualTo("trangThai", "Đã Khám").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountId.equals(documentSnapshot.getString("accountIdUser"))){
                                BenhAn benhAn = documentSnapshot.toObject(BenhAn.class);
                                benhAnArrayList.add(benhAn);
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void onClickGoToDetail(BenhAn benhAn){
        Intent intent = new Intent(getContext(), InfoUserBenhAnActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_userbenhAn", benhAn);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}