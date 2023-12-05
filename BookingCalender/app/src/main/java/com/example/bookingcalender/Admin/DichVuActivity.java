package com.example.bookingcalender.Admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingcalender.Adapter.DichVuRecyclerAdapter;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DichVuActivity extends AppCompatActivity {

    ImageView btnBack, btnAddDichVu;
    RecyclerView recyclerView;
    ArrayList<DichVu> dichVuArrayList;
    DichVuRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dich_vu);

        initUi();
        initListener();
        setDichVu();

    }

    private void setDichVu() {
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dichVuArrayList = new ArrayList<>();
        adapter = new DichVuRecyclerAdapter(dichVuArrayList, this);
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DichVu").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            DichVu dichVu = documentSnapshot.toObject(DichVu.class);
                            dichVuArrayList.add(dichVu);
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initUi() {
        btnBack = findViewById(R.id.img_back_btn_dich_vu);
        btnAddDichVu = findViewById(R.id.img_add_dich_vu);
        recyclerView = findViewById(R.id.dich_vu_recycler_view);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DichVuActivity.this, HomeAdminActivity.class);
                startActivity(intent);
            }
        });

        btnAddDichVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DichVuActivity.this, AddDichVuActivity.class);
                startActivity(intent);
            }
        });
    }
}