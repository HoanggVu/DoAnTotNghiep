package com.example.bookingcalender.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddDichVuActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    Button btnAccept, btnBack;
    TextInputEditText edtKhoa, edtDichVu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dich_vu);

        initUi();
        initListener();
    }
    private void initUi() {
        btnAccept = findViewById(R.id.btn_accept_add_dich_vu);
        btnBack = findViewById(R.id.btn_back_dich_vu);
        edtKhoa = findViewById(R.id.add_dich_vu_khoa);
        edtDichVu = findViewById(R.id.add_dich_vu_dich_vu);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDichVuActivity.this, DichVuActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDichVuAccount();
            }
        });
    }
    private void AddDichVuAccount() {
        String khoa, dichVu;
        khoa = String.valueOf(edtKhoa.getText());
        dichVu = String.valueOf(edtDichVu.getText());

        DichVu mDichVu = new DichVu(khoa, dichVu);

        progressDialog.show();
        firebaseFirestore.collection("DichVu").add(mDichVu).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(AddDichVuActivity.this, "Thêm Dịch Vụ Thành Công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddDichVuActivity.this, "Thêm Dịch Vụ Thất Bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}