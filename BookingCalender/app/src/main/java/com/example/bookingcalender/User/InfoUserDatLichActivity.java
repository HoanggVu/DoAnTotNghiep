package com.example.bookingcalender.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Admin.HomeAdminActivity;
import com.example.bookingcalender.Admin.InfoDatLichActivity;
import com.example.bookingcalender.Doctor.InfoUserActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class InfoUserDatLichActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen, tvTrangThai, tvThoiGianKham, tvNgayKham;
    Button btnHuy;
    String accountIdDoctor = " ";
    String accountIdUser = " ";
    String tGDatKham = " ";
    String dichVuKham = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user_dat_lich);

        initUi();
        setInfoDatLich();
        initListener();

    }
    private void getIdUser() {
        Intent intent = new Intent(InfoUserDatLichActivity.this, InfoUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("info_bacSi", accountIdDoctor);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void initUi() {
        imgBack = findViewById(R.id.img_info_user_dat_lich_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_info_user_dat_lich);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_info_user_dat_lich);
        tvNameUserBooking = findViewById(R.id.tv_name_user_info_user_dat_lich);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_info_user_dat_lich);
        tvTrangThai = findViewById(R.id.tv_info_user_dat_lich_trang_thai);
        tvBookingDichVu = findViewById(R.id.tv_info_user_dat_lich_dich_vu);
        tvBookingTrieuChung = findViewById(R.id.tv_info_user_dat_lich_trieu_chung);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_info_user_dat_lich);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_info_user_dat_lich);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_info_user_dat_lich);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_info_user_dat_lich);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_info_user_dat_lich);
        btnHuy = findViewById(R.id.btn_info_user_dat_lich);
        tvThoiGianKham = findViewById(R.id.tv_info_user_dat_lich_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_info_user_dat_lich_ngay_kham);
    }

    private void setInfoDatLich() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        DatLich datLich = (DatLich) bundle.get("info_userdatlich");
        Glide.with(InfoUserDatLichActivity.this).load(datLich.getAvatarUser()).into(imgAvtUserBooking);
        tvNameUserBooking.setText(datLich.getNameUser());
        tvUserSDTBooking.setText(datLich.getDienThoaiUser());
        tvTrangThai.setText(datLich.getTrangThai());
        tvBookingDichVu.setText(datLich.getDichVuKham());
        tvBookingTrieuChung.setText(datLich.getTrieuChung());
        Glide.with(InfoUserDatLichActivity.this).load(datLich.getAvatarBacSi()).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(datLich.getKhoaBacSi());
        tvNameDoctor.setText(datLich.getNameBacSi());
        tvNgayLvDoctor.setText(datLich.getNgayLV());
        tvDoctorTGLVTu.setText(datLich.gettGLVTu());
        tvDoctorTGLVDen.setText(datLich.gettGLVDen());
        tvThoiGianKham.setText(datLich.gettGDatKham());
        tvNgayKham.setText(datLich.getNgayDatKham());

        accountIdUser = datLich.getAccountIdUser();
        accountIdDoctor = datLich.getAccountIdDoctor();
        dichVuKham = datLich.getDichVuKham();
        tGDatKham = datLich.gettGDatKham();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLichKham();
            }
        });
        imgAvtDoctorBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdUser();
            }
        });
    }

    private void deleteLichKham() {
        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor")) && accountIdUser.equals(documentSnapshot.getString("accountIdUser"))
                            && tGDatKham.equals(documentSnapshot.getString("tGDatKham")) && dichVuKham.equals(documentSnapshot.getString("dichVuKham"))) {
                        DocumentReference documentReference = firebaseFirestore.collection("DatLich").document(dcId);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(InfoUserDatLichActivity.this, "Xóa Lịch Khám Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InfoUserDatLichActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InfoUserDatLichActivity.this, "Lịch Khám Đã Được Duyêt!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}