package com.example.bookingcalender.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Doctor.InfoUserActivity;
import com.example.bookingcalender.Model.PhieuThanhToan;
import com.example.bookingcalender.R;

public class InfoPhieuThanhToanUserActivity extends AppCompatActivity {
    ImageView imgBack, imgAvtUser;
    TextView tvNameUser, tvUserSDT, tvDichVu, tvTrangThai, tvKetLuanTrieuChung, tvChanDoan, tvLieuPhap, tvDonThuoc, tvThoiGianKham, tvNgayKham, tvThanhTien, tvThoiGianThanhToan, tvNgayThanhToan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_phieu_thanh_toan_user);

        initUi();
        setInfoPhieuThanhToan();
        initListener();

    }

    private void initUi() {
        imgBack = findViewById(R.id.img_info_phieu_thanh_toan_user_back);
        imgAvtUser = findViewById(R.id.img_avt_user_info_phieu_thanh_toan_user);
        tvNameUser = findViewById(R.id.tv_name_user_info_phieu_thanh_toan_user);
        tvUserSDT = findViewById(R.id.tv_phone_user_info_phieu_thanh_toan_user);
        tvDichVu = findViewById(R.id.tv_info_phieu_thanh_toan_user_dich_vu);
        tvTrangThai = findViewById(R.id.tv_info_phieu_thanh_toan_user_trang_thai);
        tvKetLuanTrieuChung = findViewById(R.id.tv_info_phieu_thanh_toan_user_ket_luan_trieu_chung);
        tvChanDoan = findViewById(R.id.tv_info_phieu_thanh_toan_user_chan_doan);
        tvLieuPhap = findViewById(R.id.tv_info_phieu_thanh_toan_user_lieu_phap);
        tvDonThuoc = findViewById(R.id.tv_info_phieu_thanh_toan_user_don_thuoc);
        tvThoiGianKham = findViewById(R.id.tv_info_phieu_thanh_toan_user_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_info_phieu_thanh_toan_user_ngay_kham);
        tvThanhTien = findViewById(R.id.tv_info_phieu_thanh_toan_user_thanh_tien);
        tvThoiGianThanhToan = findViewById(R.id.tv_info_phieu_thanh_toan_user_thoi_gian_thanh_toan);
        tvNgayThanhToan = findViewById(R.id.tv_info_phieu_thanh_toan_user_ngay_thanh_toan);
    }

    private void setInfoPhieuThanhToan() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        PhieuThanhToan phieuThanhToan = (PhieuThanhToan) bundle.get("info_userPhieuThanhToan");
        Glide.with(InfoPhieuThanhToanUserActivity.this).load(phieuThanhToan.getAvatarUser()).into(imgAvtUser);
        tvNameUser.setText(phieuThanhToan.getNameUser());
        tvUserSDT.setText(phieuThanhToan.getDienThoaiUser());
        tvTrangThai.setText(phieuThanhToan.getTrangThai());
        tvDichVu.setText(phieuThanhToan.getDichVuKham());
        tvKetLuanTrieuChung.setText(phieuThanhToan.getkLTrieuChung());
        tvChanDoan.setText(phieuThanhToan.getChanDoan());
        tvLieuPhap.setText(phieuThanhToan.getLieuPhap());
        tvDonThuoc.setText(phieuThanhToan.getDonThuoc());
        tvThoiGianKham.setText(phieuThanhToan.gettGKham());
        tvNgayKham.setText(phieuThanhToan.getNgayKham());
        tvThanhTien.setText(phieuThanhToan.getThanhTien());
        tvThoiGianThanhToan.setText(phieuThanhToan.gettGThanhToan());
        tvNgayThanhToan.setText(phieuThanhToan.getNgayThanhToan());
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}