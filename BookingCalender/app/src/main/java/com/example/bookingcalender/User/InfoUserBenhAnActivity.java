package com.example.bookingcalender.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Doctor.InfoBenhAnActivity;
import com.example.bookingcalender.Doctor.InfoUserActivity;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.R;

public class InfoUserBenhAnActivity extends AppCompatActivity {

    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor,
            tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen, tvTrangThai, tvKetLuanTrieuChung,
            tvChanDoan, tvLieuPhap, tvDonThuoc, tvThoiGianKham, tvNgayKham;
    String accountIdDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user_benh_an);

        initUi();
        setInfoBenhAn();
        initListener();
    }
    private void getIdUser() {
        Intent intent = new Intent(InfoUserBenhAnActivity.this, InfoUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("info_bacSi", accountIdDoctor);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_info_user_benh_an_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_info_user_benh_an);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_info_user_benh_an);
        tvNameUserBooking = findViewById(R.id.tv_name_user_info_user_benh_an);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_info_user_benh_an);
        tvTrangThai = findViewById(R.id.tv_info_user_benh_an_trang_thai);
        tvBookingDichVu = findViewById(R.id.tv_info_user_benh_an_dich_vu);
        tvBookingTrieuChung = findViewById(R.id.tv_info_user_benh_an_trieu_chung);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_info_user_benh_an);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_info_user_benh_an);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_info_user_benh_an);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_info_user_benh_an);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_info_user_benh_an);
        tvKetLuanTrieuChung = findViewById(R.id.tv_info_user_benh_an_ket_luan_trieu_chung);
        tvChanDoan = findViewById(R.id.tv_info_user_benh_an_chan_doan);
        tvLieuPhap = findViewById(R.id.tv_info_user_benh_an_lieu_phap);
        tvDonThuoc = findViewById(R.id.tv_info_user_benh_an_don_thuoc);
        tvThoiGianKham = findViewById(R.id.tv_info_user_benh_an_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_info_user_benh_an_ngay_kham);
    }

    private void setInfoBenhAn() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        BenhAn benhAn = (BenhAn) bundle.get("info_userbenhAn");
        Glide.with(InfoUserBenhAnActivity.this).load(benhAn.getAvatarUser()).into(imgAvtUserBooking);
        tvNameUserBooking.setText(benhAn.getNameUser());
        tvUserSDTBooking.setText(benhAn.getDienThoaiUser());
        tvTrangThai.setText(benhAn.getTrangThai());
        tvBookingDichVu.setText(benhAn.getDichVuKham());
        tvBookingTrieuChung.setText(benhAn.getTrieuChung());
        Glide.with(InfoUserBenhAnActivity.this).load(benhAn.getAvatarBacSi()).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(benhAn.getKhoaBacSi());
        tvNameDoctor.setText(benhAn.getNameBacSi());
        tvNgayLvDoctor.setText(benhAn.getNgayLV());
        tvDoctorTGLVTu.setText(benhAn.gettGLVTu());
        tvDoctorTGLVDen.setText(benhAn.gettGLVDen());
        tvKetLuanTrieuChung.setText(benhAn.getkLTrieuChung());
        tvChanDoan.setText(benhAn.getChanDoan());
        tvLieuPhap.setText(benhAn.getLieuPhap());
        tvDonThuoc.setText(benhAn.getDonThuoc());
        tvThoiGianKham.setText(benhAn.gettGKham());
        tvNgayKham.setText(benhAn.getNgayKham());

        accountIdDoctor = benhAn.getAccountIdDoctor();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgAvtDoctorBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdUser();
            }
        });
    }
}