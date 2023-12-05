package com.example.bookingcalender.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Model.Doctor;
import com.example.bookingcalender.Model.LichLamViec;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.DatLichUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class DatLichBacSiActivity extends AppCompatActivity {
    ImageView imgBack, imgAvtUserBooking, imgBookingSearchDocTor, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen;
    Button btnNext;
    String imgDoctor = "";
    String accountIdDoctor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich_bac_si);

        initUi();
        showDichVuInfo();
        initListener();
        setDoctor();
    }

    private void setDoctor() {
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 == null){
            return;
        }else {
            LichLamViec lichLamViec = (LichLamViec) bundle1.get("doctor");
            Glide.with(DatLichBacSiActivity.this).load(lichLamViec.getAvatarDoctor()).into(imgAvtDoctorBooking);
            tvKhoaDoctor.setText(lichLamViec.getKhoaDoctor());
            tvNameDoctor.setText(lichLamViec.getNameDoctor());
            tvNgayLvDoctor.setText(lichLamViec.getNgayLV());
            tvDoctorTGLVTu.setText(lichLamViec.gettGLVTu());
            tvDoctorTGLVDen.setText(lichLamViec.gettGLVDen());
            imgDoctor = lichLamViec.getAvatarDoctor();

            accountIdDoctor = lichLamViec.getAccountIdDoctor();
        }
    }

    private void showDichVuInfo() {
        Glide.with(DatLichBacSiActivity.this).load(DatLichUtil.avt).into(imgAvtUserBooking);
        tvNameUserBooking.setText(DatLichUtil.ten);
        tvUserSDTBooking.setText(DatLichUtil.sdt);
        tvBookingDichVu.setText(DatLichUtil.dichvu);
        tvBookingTrieuChung.setText(DatLichUtil.trieuchung);
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_user_booking_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_booking_bac_si);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_dat_lich);
        tvNameUserBooking = findViewById(R.id.tv_name_user_booking_bac_si);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_booking_bac_si);
        tvBookingDichVu = findViewById(R.id.tv_booking_dich_vu_bac_si);
        tvBookingTrieuChung = findViewById(R.id.tv_booking_trieu_chung_bac_si);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_dat_lich);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_dat_lich);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_dat_lich);
        imgBookingSearchDocTor = findViewById(R.id.img_booking_search_doctor);
        btnNext = findViewById(R.id.btn_user_dat_lich_bac_si_tiep_tuc);
//        edtInfoBookingTime = findViewById(R.id.tv_info_booking_thoi_gian);
//        edtInfoBookingDate = findViewById(R.id.tv_info_booking_date);
    }
    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tvNameDoctor.getText().toString())){
                    Toast.makeText(DatLichBacSiActivity.this, "Hãy Chọn Bác Sĩ Khám Bệnh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatLichUtil.avtbacsi = imgDoctor;
                DatLichUtil.accountIdDoctor = accountIdDoctor;
                DatLichUtil.khoa = tvKhoaDoctor.getText().toString();
                DatLichUtil.tenbacsi = tvNameDoctor.getText().toString();
                DatLichUtil.ngaylamviec = tvNgayLvDoctor.getText().toString();
                DatLichUtil.thoigianbatdau = tvDoctorTGLVTu.getText().toString();
                DatLichUtil.thoigianketthuc = tvDoctorTGLVDen.getText().toString();
                Intent intent = new Intent(DatLichBacSiActivity.this, DatLichThoiGianActivity.class);
                startActivity(intent);
            }
        });
        imgBookingSearchDocTor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatLichBacSiActivity.this, SearchDoctorActivity.class);
                startActivity(intent);
            }
        });
    }
}