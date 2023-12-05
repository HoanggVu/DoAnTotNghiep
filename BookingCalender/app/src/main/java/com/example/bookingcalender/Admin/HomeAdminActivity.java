package com.example.bookingcalender.Admin;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookingcalender.Authentication.LoginActivity;
import com.example.bookingcalender.FirebaseCloudMessaging.MyFirebaseMessagingService;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.TokenUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class HomeAdminActivity extends AppCompatActivity {

    ImageView img_doctor, img_user, img_logoutBtn, imgDichVu, imgDatLich, imgCheck;
MyFirebaseMessagingService myFirebaseMessagingService;
FirebaseFirestore firebaseFirestore;

    String accountUser = "0";
    String UId = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        initUi();
        initListener();
    }
    private void initUi() {
        img_user = findViewById(R.id.img_user);
        img_doctor = findViewById(R.id.img_doctor);
        img_logoutBtn = findViewById(R.id.img_logoutBtn);
        imgDichVu = findViewById(R.id.img_dich_vu);
        imgDatLich = findViewById(R.id.img_dat_lich);
    }
    private void initListener() {
        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, UserAccountActivity.class);
                startActivity(intent);
            }
        });
        img_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, DoctorAccountActivity.class);
                startActivity(intent);
            }
        });

        img_logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        imgDichVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, DichVuActivity.class);
                startActivity(intent);
            }
        });
        imgDatLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, DatLichActivity.class);
                startActivity(intent);
            }
        });
    }

}