package com.example.bookingcalender.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Interface.IClickItemDichVuListener;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.Model.Doctor;
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

public class DatLichDichVuActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgBookingSearchDichVu;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu;
    EditText edtBookingTrieuChung;
    Button btnNext;
    String img = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich_dich_vu);

        initUi();
        showUserInfo();
        initListener();
        setDichVu();

    }
    private void setDichVu() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }else {
            DichVu dichVu = (DichVu) bundle.get("dichvukham");
            tvBookingDichVu.setText(dichVu.getDichVu());
        }
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_user_booking_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_booking);
        //imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_dat_lich);
        tvNameUserBooking = findViewById(R.id.tv_name_user_booking);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_booking);
        tvBookingDichVu = findViewById(R.id.tv_booking_dich_vu);
        edtBookingTrieuChung = findViewById(R.id.tv_booking_trieu_chung);
//        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_dat_lich);
//        tvNameDoctor = findViewById(R.id.tv_name_doctor_dat_lich);
//        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau);
//        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc);
//        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_dat_lich);
        imgBookingSearchDichVu = findViewById(R.id.img_booking_search_dich_vu);
        btnNext = findViewById(R.id.btn_user_dat_lich_tiep_tuc);
//        imgBookingSearchDoctor = findViewById(R.id.img_booking_search_doctor);
//        edtInfoBookingTime = findViewById(R.id.tv_info_booking_thoi_gian);
//        edtInfoBookingDate = findViewById(R.id.tv_info_booking_date);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgBookingSearchDichVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatLichDichVuActivity.this, SearchDichVuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tvBookingDichVu.getText().toString())){
                    Toast.makeText(DatLichDichVuActivity.this, "Hãy Chọn Dịch Vụ Khám!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBookingTrieuChung.getText().toString())){
                    Toast.makeText(DatLichDichVuActivity.this, "Hãy Nhập Triệu Chứng Bệnh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatLichUtil.avt = img;
                DatLichUtil.ten = tvNameUserBooking.getText().toString();
                DatLichUtil.sdt = tvUserSDTBooking.getText().toString();
                DatLichUtil.dichvu = tvBookingDichVu.getText().toString();
                DatLichUtil.trieuchung = edtBookingTrieuChung.getText().toString();
                Intent intent = new Intent(DatLichDichVuActivity.this, DatLichBacSiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showUserInfo() {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            firebaseFirestore.collection("BenhNhan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String dcId = documentSnapshot.getId();
                        String UId = FirebaseAuth.getInstance().getUid();
                        if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                            DocumentReference documentReference = firebaseFirestore.collection("BenhNhan").document(dcId);
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    img = documentSnapshot.getString("avatar").toString();
                                    Glide.with(DatLichDichVuActivity.this).load(img).into(imgAvtUserBooking);
                                    tvNameUserBooking.setText(documentSnapshot.getString("hoTen"));
                                    tvUserSDTBooking.setText(documentSnapshot.getString("dienThoai"));
                                    progressDialog.dismiss();
                                }
                            });
                        }
                        progressDialog.dismiss();
                    }
                }
            });
        }else {
            return;
        }
    }
}