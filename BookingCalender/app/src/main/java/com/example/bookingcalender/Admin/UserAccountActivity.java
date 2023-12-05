package com.example.bookingcalender.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookingcalender.Adapter.AccountUserRecyclerAdapter;
import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends AppCompatActivity {

    ImageView btnBack, btnAddDoctor;
    RecyclerView recyclerView;
    ArrayList<Account> accountUserArrayList;
    AccountUserRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        initUi();
        initListener();
        setAccountUser();
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
                Intent intent = new Intent(UserAccountActivity.this, HomeAdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAccountUser() {
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountUserArrayList = new ArrayList<>();
        adapter = new AccountUserRecyclerAdapter(accountUserArrayList, this);
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            Account account = documentSnapshot.toObject(Account.class);
                            accountUserArrayList.add(account);
                        }
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}