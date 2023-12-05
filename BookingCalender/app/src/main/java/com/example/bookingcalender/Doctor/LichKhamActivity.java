package com.example.bookingcalender.Doctor;

import static com.example.bookingcalender.Util.BenhAnUtil.accountId;

import androidx.annotation.NonNull;
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

import com.example.bookingcalender.Adapter.DatLichRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserDatLichRecyclerAdapter;
import com.example.bookingcalender.Admin.DatLichActivity;
import com.example.bookingcalender.Admin.InfoDatLichActivity;
import com.example.bookingcalender.Interface.IClickItemDatLichListener;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Objects;

public class LichKhamActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView tvChonNgay;
    RecyclerView recyclerView;
    ArrayList<DatLich> datLichArrayList;
    DatLichRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String accountIdDoctor = " ";
    String dateTime = " ";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_kham);

        initUi();
        initListener();
        setDatLich();
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
                setLichKhamTheoNgay();
            }
        }, year, month, date);
        datePickerDialog.show();
    }
    private void setLichKhamTheoNgay() {

        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        progressDialog.show();
        accountIdDoctor = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datLichArrayList = new ArrayList<>();
        adapter = new DatLichRecyclerAdapter(datLichArrayList, this, new IClickItemDatLichListener() {
            @Override
            public void onClickItemDatLich(DatLich datLich) {
                onClickGoToDetail(datLich);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ Khám").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor"))){
                                if (dateTime.equals(documentSnapshot.getString("ngayDatKham"))){
                                    DatLich datLich = documentSnapshot.toObject(DatLich.class);
                                    datLichArrayList.add(datLich);
                                }
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void setDatLich() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        progressDialog.show();
        accountIdDoctor = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datLichArrayList = new ArrayList<>();
        adapter = new DatLichRecyclerAdapter(datLichArrayList, this, new IClickItemDatLichListener() {
            @Override
            public void onClickItemDatLich(DatLich datLich) {
                onClickGoToDetail(datLich);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ Khám").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor"))){
                                DatLich datLich = documentSnapshot.toObject(DatLich.class);
                                datLichArrayList.add(datLich);
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onClickGoToDetail(DatLich datLich){
        Intent intent = new Intent(LichKhamActivity.this, InfoLichKhamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_lichkham", datLich);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initUi() {
        btnBack = findViewById(R.id.img_back_btn_lich_kham);
        tvChonNgay = findViewById(R.id.tv_ngay_lich_kham);
        recyclerView = findViewById(R.id.lich_kham_recycler_view);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LichKhamActivity.this, DoctorActivity.class);
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