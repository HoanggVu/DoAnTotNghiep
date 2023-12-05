package com.example.bookingcalender.Admin;

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
import com.example.bookingcalender.Adapter.DichVuRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserDatLichRecyclerAdapter;
import com.example.bookingcalender.Interface.IClickItemDatLichListener;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.Model.DichVu;
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

public class DatLichActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView tvChonNgay;
    RecyclerView recyclerView;
    ArrayList<DatLich> datLichArrayList;
    DatLichRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String dateTime = " ";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich);

        initUi();
        initListener();
        setDatLich();
    }

    private void setDatLich() {
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datLichArrayList = new ArrayList<>();
        adapter = new DatLichRecyclerAdapter(datLichArrayList, this, new IClickItemDatLichListener() {
            @Override
            public void onClickItemDatLich(DatLich datLich) {
                onClickGoToDetail(datLich);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DatLich").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            DatLich datLich = documentSnapshot.toObject(DatLich.class);
                            datLichArrayList.add(datLich);
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onClickGoToDetail(DatLich datLich){
        Intent intent = new Intent(DatLichActivity.this, InfoDatLichActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_datlich", datLich);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initUi() {
        btnBack = findViewById(R.id.img_back_btn_dat_lich);
        recyclerView = findViewById(R.id.dat_lich_recycler_view);
        tvChonNgay = findViewById(R.id.tv_ngay_dat_lich_quan_li);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

    }

    private void setDate() {
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
                setDatLichTheoNgay();
            }
        }, year, month, date);
        datePickerDialog.show();
    }

    private void setDatLichTheoNgay() {
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datLichArrayList = new ArrayList<>();
        adapter = new DatLichRecyclerAdapter(datLichArrayList, this, new IClickItemDatLichListener() {
            @Override
            public void onClickItemDatLich(DatLich datLich) {
                onClickGoToDetail(datLich);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DatLich").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (dateTime.equals(documentSnapshot.getString("ngayDatKham"))){
                                DatLich datLich = documentSnapshot.toObject(DatLich.class);
                                datLichArrayList.add(datLich);
                            }
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}