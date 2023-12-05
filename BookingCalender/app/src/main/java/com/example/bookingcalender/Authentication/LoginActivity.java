package com.example.bookingcalender.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingcalender.Admin.HomeAdminActivity;
import com.example.bookingcalender.Doctor.DoctorActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.BenhAnUtil;
import com.example.bookingcalender.Util.TokenUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView signUp, forgetPassword;
    Button btn_signIn;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    TextInputEditText edtAccount, edtPassword;

    String email, password;
    String UId = " ";
    String accountDoctor = "1";
    String accountUser = "0";
    String token = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();
        initListener();
        anhienpassword();

    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        token = task.getResult();
                        TokenUtil.token = token.toString();
                    }
                });
    }

    private void anhienpassword() {
        edtPassword.setTypeface(Typeface.DEFAULT);
        edtPassword.setTransformationMethod(new PasswordTransformationMethod());
    }

//    private void setTokenUser() {
//        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                    String dcId = documentSnapshot.getId();
//                    Map<String, Object> mToken = new HashMap<>();
//                    mToken.put("tokenUser", token);
//                    if (accountDoctor.equals(documentSnapshot.getString("account")) && Objects.equals(UId, documentSnapshot.getString("accountUi"))){
//                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId);
//                        documentReference.update(mToken);
//                        }
//                    }
//                }
//            });
//    }
//    private void setTokenDoctor(){
//        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
//                    String dcId1 = documentSnapshot1.getId();
//                    Map<String, Object> mToken1 = new HashMap<>();
//                    mToken1.put("tokenDoctor", token);
//                    if (accountUser.equals(documentSnapshot1.getString("account")) && Objects.equals(UId, documentSnapshot1.getString("accountUi"))){
//                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId1);
//                        documentReference.update(mToken1);
//                    }
//                }
//            }
//        });
//    }

    private void initUi() {
        btn_signIn = findViewById(R.id.btn_sign_in);
        signUp = findViewById(R.id.sign_up);
        edtAccount = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        /*forgetPassword = findViewById(R.id.quen_mat_khau);*/
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        /*forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });*/
    }


    private void onClickLogin() {
        if (Objects.requireNonNull(edtAccount.getText()).toString().equals("Admin") || Objects.requireNonNull(edtPassword.getText()).toString().equals("1")){
            Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
            startActivity(intent);
        }
        else {

            email = String.valueOf(edtAccount.getText());
            password = String.valueOf(edtPassword.getText());

            progressDialog.show();
            if (TextUtils.isEmpty(email)) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Nhập Email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Nhập Mật Khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Mật Khẩu Phải Trên 6 Ký Tự!", Toast.LENGTH_SHORT).show();
                return;
            }

            getToken();

            loginAccount();

        }
    }

    private void loginAccount() {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            UId = FirebaseAuth.getInstance().getUid();
                            firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        String dcId = documentSnapshot.getId();
                                        Map<String, Object> mToken1 = new HashMap<>();
                                        mToken1.put("tokenDoctor", token);
                                        if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                                            DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId);
                                            documentReference.update(mToken1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                }
                                            });
                                        }else {
                                            firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                                                        String dcId1 = documentSnapshot1.getId();
                                                        Map<String, Object> mToken = new HashMap<>();
                                                        mToken.put("tokenUser", token);
                                                        if (Objects.equals(UId, documentSnapshot1.getString("accountId"))){
                                                            DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId1);
                                                            documentReference.update(mToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    progressDialog.dismiss();
//                                                                    Toast.makeText(LoginActivity.this, TokenUtil.token, Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finishAffinity();
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
    }

}