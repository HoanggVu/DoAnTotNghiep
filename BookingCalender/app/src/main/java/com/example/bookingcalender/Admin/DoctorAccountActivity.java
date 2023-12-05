package com.example.bookingcalender.Admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bookingcalender.Adapter.AccountDoctorRecyclerAdapter;
import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorAccountActivity extends AppCompatActivity {

    ImageView btnBack, btnAddDoctor;
    RecyclerView recyclerView;
    ArrayList<Account> accountDoctorArrayList;
    AccountDoctorRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_account);

        initUi();
        initListener();
        setAccountDoctor();

    }

    private void setAccountDoctor() {
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountDoctorArrayList = new ArrayList<>();
        adapter = new AccountDoctorRecyclerAdapter(accountDoctorArrayList, this);
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            Account account = documentSnapshot.toObject(Account.class);
                            accountDoctorArrayList.add(account);
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initUi() {
        btnBack = findViewById(R.id.img_backBtn);
        btnAddDoctor = findViewById(R.id.img_addDoctor);
        recyclerView = findViewById(R.id.account_doctor_recycler_view);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorAccountActivity.this, HomeAdminActivity.class);
                startActivity(intent);
            }
        });

        btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorAccountActivity.this, RegisterDoctorActivity.class);
                startActivity(intent);
            }
        });
    }
}