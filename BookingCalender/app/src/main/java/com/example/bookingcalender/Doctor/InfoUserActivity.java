package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.R;
import com.example.bookingcalender.User.NewProfileUserActivity;
import com.example.bookingcalender.User.UpdateProfileUserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class InfoUserActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgAvtProfileUser, btnBack;
    TextView tvUserHoTen, tvUserNgaySinh, tvUserGioiTinh, tvUserSDT, tvUserEmail, tvUserDiaChi;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        initUi();
        setUserInfo();
        initListener();
    }
    private void initUi() {
        btnBack = findViewById(R.id.img_back_info_user);
        imgAvtProfileUser = findViewById(R.id.img_view_doctor_profile_user);
        tvUserHoTen = findViewById(R.id.tv_doctor_profile_user_ho_ten);
        tvUserNgaySinh = findViewById(R.id.tv_doctor_profile_user_ngay_sinh);
        tvUserGioiTinh = findViewById(R.id.tv_doctor_profile_user_gioi_tinh);
        tvUserSDT = findViewById(R.id.tv_doctor_profile_user_sdt);
        tvUserDiaChi = findViewById(R.id.tv_doctor_profile_user_dia_chi);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setUserInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        String accountIdUser = bundle.getString("idUser");
        progressDialog.show();
        firebaseFirestore.collection("BenhNhan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    if (Objects.equals(accountIdUser, documentSnapshot.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("BenhNhan").document(dcId);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String img = documentSnapshot.getString("avatar").toString();
                                Glide.with(InfoUserActivity.this).load(img).into(imgAvtProfileUser);
                                tvUserHoTen.setText(documentSnapshot.getString("hoTen"));
                                tvUserNgaySinh.setText(documentSnapshot.getString("ngaySinh"));
                                tvUserGioiTinh.setText(documentSnapshot.getString("gioiTinh"));
                                tvUserSDT.setText(documentSnapshot.getString("dienThoai"));
                                tvUserDiaChi.setText(documentSnapshot.getString("diaChi"));
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(InfoUserActivity.this, "Chưa Cập Nhật Thông Tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}