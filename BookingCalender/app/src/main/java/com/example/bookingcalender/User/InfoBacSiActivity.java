package com.example.bookingcalender.User;

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
import com.example.bookingcalender.Doctor.InfoBenhAnActivity;
import com.example.bookingcalender.Doctor.NewProfileDoctorActivity;
import com.example.bookingcalender.Doctor.UpdateProfileDoctorActivity;
import com.example.bookingcalender.Model.BacSi;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class InfoBacSiActivity extends AppCompatActivity {
    ImageView imgAvtProfileDoctor, btnBack;
    TextView tvDoctorHoVaTen, tvDoctorNgaySinh, tvDoctorGioiTinh, tvDoctorSDT, tvDoctorChuyenKhoa, tvDoctorChucVu, tvDoctorBangCap, tvDoctorChuyenMon;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_bac_si);
        initUi();
        setDoctorInfo();
        initListener();
    }

    private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initUi(){
        btnBack = findViewById(R.id.img_back_info_doctor);
        imgAvtProfileDoctor = findViewById(R.id.img_view_user_profile_doctor);
        tvDoctorHoVaTen = findViewById(R.id.tv_user_profile_doctor_ho_ten);
        tvDoctorNgaySinh = findViewById(R.id.tv_user_profile_doctor_ngay_sinh);
        tvDoctorGioiTinh = findViewById(R.id.tv_user_profile_doctor_gioi_tinh);
        tvDoctorSDT = findViewById(R.id.tv_user_profile_doctor_sdt);
        tvDoctorChuyenKhoa = findViewById(R.id.tv_user_profile_doctor_chuyen_khoa);
        tvDoctorChucVu = findViewById(R.id.tv_user_profile_doctor_chuc_vu);
        tvDoctorBangCap = findViewById(R.id.tv_user_profile_doctor_bang_cap);
        tvDoctorChuyenMon = findViewById(R.id.tv_user_profile_doctor_chuyen_mon);
        progressDialog = new ProgressDialog(this);
    }
    private void setDoctorInfo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        progressDialog.show();
        BacSi bacSi = (BacSi) bundle.get("info_bacSi");
        Glide.with(InfoBacSiActivity.this).load(bacSi.getAvatar()).into(imgAvtProfileDoctor);
        tvDoctorHoVaTen.setText(bacSi.getHoTen());
        tvDoctorNgaySinh.setText(bacSi.getNgaySinh());
        tvDoctorGioiTinh.setText(bacSi.getGioiTinh());
        tvDoctorSDT.setText(bacSi.getDienThoai());
        tvDoctorChuyenKhoa.setText(bacSi.getChuyenKhoa());
        tvDoctorChucVu.setText(bacSi.getChucVu());
        tvDoctorBangCap.setText(bacSi.getBangCap());
        tvDoctorChuyenMon.setText(bacSi.getChuyenMon());
        progressDialog.dismiss();
    }
}