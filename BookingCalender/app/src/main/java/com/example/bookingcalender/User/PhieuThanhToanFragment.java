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

import com.example.bookingcalender.Adapter.PhieuThanhToanRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserDatLichRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserPhieuThanhToanRecyclerAdapter;
import com.example.bookingcalender.Interface.IClickItemDatLichListener;
import com.example.bookingcalender.Interface.IClickItemPhieuThanhToanListener;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.Model.PhieuThanhToan;
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

public class PhieuThanhToanFragment extends Fragment {
    View mView;
    TextView tvChonNgay;
    RecyclerView recyclerView;
    ArrayList<PhieuThanhToan> phieuThanhToanArrayList;
    UserPhieuThanhToanRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String accountUser = " ";
    String dateTime = " ";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_phieu_thanh_toan, container, false);

        initUi();
        initListener();
        setPhieuThanhToan();

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
        recyclerView = mView.findViewById(R.id.user_phieu_thanh_toan_recycler_view);
        tvChonNgay = mView.findViewById(R.id.tv_ngay_phieu_thanh_toan_user);
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
                setPhieuThanhToanTheoNgay();
            }
        }, year, month, date);
        datePickerDialog.show();
    }

    private void setPhieuThanhToanTheoNgay() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        accountUser = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phieuThanhToanArrayList = new ArrayList<>();
        adapter = new UserPhieuThanhToanRecyclerAdapter(phieuThanhToanArrayList, getContext(), new IClickItemPhieuThanhToanListener() {
            @Override
            public void onClickItemPhieuThanhToan(PhieuThanhToan phieuThanhToan) {
                onClickGoToDetail(phieuThanhToan);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("PhieuThanhToan").whereEqualTo("trangThai", "Đã Thanh Toán").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountUser.equals(documentSnapshot.getString("accountIdUser"))){
                                if (dateTime.equals(documentSnapshot.getString("ngayKham"))){
                                    PhieuThanhToan phieuThanhToan = documentSnapshot.toObject(PhieuThanhToan.class);
                                    phieuThanhToanArrayList.add(phieuThanhToan);
                                    progressDialog.dismiss();
                                }
                                progressDialog.dismiss();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        progressDialog.dismiss();
    }

    private void setPhieuThanhToan() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        accountUser = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        phieuThanhToanArrayList = new ArrayList<>();
        adapter = new UserPhieuThanhToanRecyclerAdapter(phieuThanhToanArrayList, getContext(), new IClickItemPhieuThanhToanListener() {
            @Override
            public void onClickItemPhieuThanhToan(PhieuThanhToan phieuThanhToan) {
                onClickGoToDetail(phieuThanhToan);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("PhieuThanhToan").whereEqualTo("trangThai", "Đã Thanh Toán").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountUser.equals(documentSnapshot.getString("accountIdUser"))){
                                PhieuThanhToan phieuThanhToan = documentSnapshot.toObject(PhieuThanhToan.class);
                                phieuThanhToanArrayList.add(phieuThanhToan);
                                progressDialog.dismiss();
                            }
                            progressDialog.dismiss();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        progressDialog.dismiss();
    }
    private void onClickGoToDetail(PhieuThanhToan phieuThanhToan) {
        Intent intent = new Intent(getActivity(), InfoPhieuThanhToanUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_userPhieuThanhToan", phieuThanhToan);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}