package com.example.bookingcalender.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookingcalender.Adapter.DoctorBenhAnRecyclerAdapter;
import com.example.bookingcalender.Adapter.PhieuThanhToanRecyclerAdapter;
import com.example.bookingcalender.Interface.IClickItemBenhAnListener;
import com.example.bookingcalender.Interface.IClickItemPhieuThanhToanListener;
import com.example.bookingcalender.Model.BenhAn;
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

public class PhieuThanhToanActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView tvChonNgay;
    RecyclerView recyclerView;
    ArrayList<PhieuThanhToan> phieuThanhToanArrayList;
    PhieuThanhToanRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String accountIdDoctor = " ";
    String dateTime = " ";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phieu_thanh_toan);
        initUi();
        initListener();
        setPhieuThanhToan();
    }
    private void setDate(){
        final Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        progressDialog.show();
        accountIdDoctor = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        phieuThanhToanArrayList = new ArrayList<>();
        adapter = new PhieuThanhToanRecyclerAdapter(phieuThanhToanArrayList, this, new IClickItemPhieuThanhToanListener() {
            @Override
            public void onClickItemPhieuThanhToan(PhieuThanhToan phieuThanhToan) {
                onClickGoToDetail(phieuThanhToan);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("PhieuThanhToan").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor"))){
                                if (dateTime.equals(documentSnapshot.getString("ngayKham"))){
                                    PhieuThanhToan phieuThanhToan = documentSnapshot.toObject(PhieuThanhToan.class);
                                    phieuThanhToanArrayList.add(phieuThanhToan);
                                }
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void setPhieuThanhToan() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        progressDialog.show();
        accountIdDoctor = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        phieuThanhToanArrayList = new ArrayList<>();
        adapter = new PhieuThanhToanRecyclerAdapter(phieuThanhToanArrayList, this, new IClickItemPhieuThanhToanListener() {
            @Override
            public void onClickItemPhieuThanhToan(PhieuThanhToan phieuThanhToan) {
                onClickGoToDetail(phieuThanhToan);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("PhieuThanhToan").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor"))){
                                PhieuThanhToan phieuThanhToan = documentSnapshot.toObject(PhieuThanhToan.class);
                                phieuThanhToanArrayList.add(phieuThanhToan);
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onClickGoToDetail(PhieuThanhToan phieuThanhToan) {
        Intent intent = new Intent(PhieuThanhToanActivity.this, InfoPhieuThanhToanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_phieuThanhToan", phieuThanhToan);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initUi() {
        btnBack = findViewById(R.id.img_back_btn_phieu_thanh_toan);
        tvChonNgay = findViewById(R.id.tv_ngay_phieu_thanh_toan);
        recyclerView = findViewById(R.id.phieu_thanh_toan_recycler_view);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhieuThanhToanActivity.this, DoctorActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        tvChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }
}