package com.example.bookingcalender.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Model.PhieuThanhToan;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.PhieuThanhToanUtil;
public class InfoPhieuThanhToanActivity extends AppCompatActivity {
    ImageView imgBack, imgAvtUser;
    TextView tvNameUser, tvUserSDT, tvDichVu, tvTrangThai, tvKetLuanTrieuChung, tvChanDoan, tvLieuPhap, tvDonThuoc, tvThoiGianKham, tvNgayKham;
    Button btnXacNhan;
    String avtUser;
    String accountIdUser;
    String accountIdDoctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_phieu_thanh_toan);

        initUi();
        setInfoThanhToan();
        initListener();

    }

    private void setInfoThanhToan() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        PhieuThanhToan phieuThanhToan = (PhieuThanhToan) bundle.get("info_phieuThanhToan");
        Glide.with(InfoPhieuThanhToanActivity.this).load(phieuThanhToan.getAvatarUser()).into(imgAvtUser);
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

        avtUser = phieuThanhToan.getAvatarUser();
        accountIdUser = phieuThanhToan.getAccountIdUser();
        accountIdDoctor = phieuThanhToan.getAccountIdDoctor();
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_info_phieu_thanh_toan_back);
        imgAvtUser = findViewById(R.id.img_avt_user_info_phieu_thanh_toan);
        tvNameUser = findViewById(R.id.tv_name_user_info_phieu_thanh_toan);
        tvUserSDT = findViewById(R.id.tv_phone_user_info_phieu_thanh_toan);
        tvDichVu = findViewById(R.id.tv_info_phieu_thanh_toan_dich_vu);
        tvTrangThai = findViewById(R.id.tv_info_phieu_thanh_toan_trang_thai);
        tvKetLuanTrieuChung = findViewById(R.id.tv_info_phieu_thanh_toan_ket_luan_trieu_chung);
        tvChanDoan = findViewById(R.id.tv_info_phieu_thanh_toan_chan_doan);
        tvLieuPhap = findViewById(R.id.tv_info_phieu_thanh_toan_lieu_phap);
        tvDonThuoc = findViewById(R.id.tv_info_phieu_thanh_toan_don_thuoc);
        tvThoiGianKham = findViewById(R.id.tv_info_phieu_thanh_toan_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_info_phieu_thanh_toan_ngay_kham);
        btnXacNhan = findViewById(R.id.btn_info_phieu_thanh_toan);
    }

    private void initListener() {
        imgAvtUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdUser();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoPhieuThanhToanActivity.this, PhieuThanhToanActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trangThai = "Đã Thanh Toán";
                if (trangThai.equals(tvTrangThai.getText().toString())){
                    Toast.makeText(InfoPhieuThanhToanActivity.this, "Đã Thanh Toán!", Toast.LENGTH_SHORT).show();
                    return;
                }
                PhieuThanhToanUtil.accountIdUser = accountIdUser;
                PhieuThanhToanUtil.accountIdDoctor = accountIdDoctor;
                PhieuThanhToanUtil.avtUser = avtUser;
                PhieuThanhToanUtil.nameUser = tvNameUser.getText().toString().trim();
                PhieuThanhToanUtil.sdtUser = tvUserSDT.getText().toString().trim();
                PhieuThanhToanUtil.trangThai = tvTrangThai.getText().toString().trim();
                PhieuThanhToanUtil.dichVuKham = tvDichVu.getText().toString().trim();
                PhieuThanhToanUtil.ketLuanTrieuChung = tvKetLuanTrieuChung.getText().toString().trim();
                PhieuThanhToanUtil.chanDoan = tvChanDoan.getText().toString().trim();
                PhieuThanhToanUtil.lieuPhap = tvLieuPhap.getText().toString().trim();
                PhieuThanhToanUtil.donThuoc = tvDonThuoc.getText().toString().trim();
                PhieuThanhToanUtil.thoiGianKham = tvThoiGianKham.getText().toString().trim();
                PhieuThanhToanUtil.ngayKham = tvNgayKham.getText().toString().trim();

                Intent intent = new Intent(InfoPhieuThanhToanActivity.this, ThanhTienActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getIdUser() {
        Intent intent = new Intent(InfoPhieuThanhToanActivity.this, InfoUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idUser", accountIdUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}