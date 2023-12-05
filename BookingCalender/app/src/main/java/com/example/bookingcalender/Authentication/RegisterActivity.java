package com.example.bookingcalender.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    TextInputEditText edtAccount, edtPassword;
    Button btnSignUp;
    TextView tvSignIn;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUi();
        initListener();
    }
    private void initUi() {
        edtAccount = findViewById(R.id.account);
        edtPassword = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btn_user_sign_up);
        tvSignIn = findViewById(R.id.sign_in);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUserAccount();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();;
            }
        });
    }
    private void RegisterUserAccount() {

        email = String.valueOf(edtAccount.getText());
        password = String.valueOf(edtPassword.getText());

        if (TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this, "Nhập Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "Nhập Mật Khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendEmail();
                    /*Account account = new Account(email, password, "0", mAuth.getUid());
                    firebaseFirestore.collection("Account").add(account).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Đăng Ký Tài Khoản Thành Công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finishAffinity();;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Đăng Ký Tài Khoản Thất Bại!", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException){
                    progressDialog.dismiss();
                    btnSignUp.setEnabled(false);
                    Toast.makeText(RegisterActivity.this, "Email Đã Đăng Ký", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                btnSignUp.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Có Lỗi Xãy Ra!", Toast.LENGTH_SHORT).show();
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
                        btnSignUp.setEnabled(false);
                        Toast.makeText(RegisterActivity.this, "Vui Lòng Kiểm Tra Email Để Xác Thực Tài Khoản!", Toast.LENGTH_SHORT).show();
                        Account account = new Account(email, password, "0", mAuth.getUid());
                        firebaseFirestore.collection("Account").add(account);
                    } else {
                        progressDialog.dismiss();
                        btnSignUp.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "Email Không Tồn Tại!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}