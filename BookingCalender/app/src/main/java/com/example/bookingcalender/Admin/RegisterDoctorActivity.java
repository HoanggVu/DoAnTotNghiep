package com.example.bookingcalender.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookingcalender.Authentication.RegisterActivity;
import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterDoctorActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    Button btnAccept;
    TextInputEditText edtAccount, edtPassword;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        initUi();
        initListener();
    }
    private void initUi() {
        btnAccept = findViewById(R.id.btn_accept);
        edtAccount = findViewById(R.id.account);
        edtPassword = findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterDoctorAccount();
            }
        });
    }
    private void RegisterDoctorAccount() {

        email = String.valueOf(edtAccount.getText());
        password = String.valueOf(edtPassword.getText());

        if (TextUtils.isEmpty(email)){
            Toast.makeText(RegisterDoctorActivity.this, "Nhập Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(RegisterDoctorActivity.this, "Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendEmail();
                    /*Account account = new Account(email, password, "1", mAuth.getUid());
                    firebaseFirestore.collection("Account").add(account).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterDoctorActivity.this, "Đăng Ký Tài Khoản Thành Công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterDoctorActivity.this, DoctorAccountActivity.class);
                            startActivity(intent);
                            finishAffinity();;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterDoctorActivity.this, "Đăng Ký Tài Khoản Thất Bại!", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException){
                    progressDialog.dismiss();
                    btnAccept.setEnabled(false);
                    Toast.makeText(RegisterDoctorActivity.this, "Email Đã Đăng Ký", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                btnAccept.setEnabled(true);
                Toast.makeText(RegisterDoctorActivity.this, "Có Lỗi Xãy Ra!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendEmail() {
        if (mAuth.getCurrentUser() != null){
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        btnAccept.setEnabled(false);
                        Toast.makeText(RegisterDoctorActivity.this, "Vui Lòng Kiểm Tra Email Để Xác Thực Tài Khoản!", Toast.LENGTH_SHORT).show();
                        Account account = new Account(email, password, "1", mAuth.getUid());
                        firebaseFirestore.collection("Account").add(account);
                    } else {
                        progressDialog.dismiss();
                        btnAccept.setEnabled(true);
                        Toast.makeText(RegisterDoctorActivity.this, "Email Không Tồn Tại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}