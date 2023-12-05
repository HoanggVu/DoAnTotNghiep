package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.API.ApiClient;
import com.example.bookingcalender.API.ApiService;
import com.example.bookingcalender.Model.Notification;
import com.example.bookingcalender.Model.NotificationModel;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.BenhAnUtil;
import com.example.bookingcalender.Util.PhieuThanhToanUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class ThanhTienActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUser;
    TextView tvNameUser, tvUserSDT, tvDichVu, tvTrangThai, tvKetLuanTrieuChung, tvChanDoan, tvLieuPhap, tvDonThuoc, tvThoiGianKham, tvNgayKham;

    Map<String, Object> thanhtoan = new HashMap<>();
    Button btnXacNhan;
    String mTrangthai = "Đã Thanh Toán";
    String accountIdUser = PhieuThanhToanUtil.accountIdUser;
    String accountIdDoctor = PhieuThanhToanUtil.accountIdDoctor;
    String thanhTien, tgThanhToan, ngayThanhToan;
    EditText edtThanhTien, edtThoiGianThanhToan, edtNgayThanhToan;
    ProgressDialog progressDialog;

    String tokenUser = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_tien);

        initUi();
        setInfoPhieuThanhToan();
        initListener();
        setDateTimeThanhToan();
        getTokenUser();

    }

    private void setDateTimeThanhToan() {
        final Calendar calendar = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        String time = DateFormat.getTimeInstance().format(currentTime);
        edtThoiGianThanhToan.setText(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        edtNgayThanhToan.setText(simpleDateFormat.format(calendar.getTime()));
        /*String date = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        edtNgayThanhToan.setText(date);*/
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_thanh_tien_back);
        imgAvtUser = findViewById(R.id.img_avt_user_thanh_tien);
        tvNameUser = findViewById(R.id.tv_name_user_thanh_tien);
        tvUserSDT = findViewById(R.id.tv_phone_user_thanh_tien);
        tvDichVu = findViewById(R.id.tv_thanh_tien_dich_vu);
        tvTrangThai = findViewById(R.id.tv_thanh_tien_trang_thai);
        tvKetLuanTrieuChung = findViewById(R.id.tv_thanh_tien_ket_luan_trieu_chung);
        tvChanDoan = findViewById(R.id.tv_thanh_tien_chan_doan);
        tvLieuPhap = findViewById(R.id.tv_thanh_tien_lieu_phap);
        tvDonThuoc = findViewById(R.id.tv_thanh_tien_don_thuoc);
        tvThoiGianKham = findViewById(R.id.tv_thanh_tien_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_thanh_tien_ngay_kham);
        edtThanhTien = findViewById(R.id.edt_thanh_tien);
        edtThoiGianThanhToan = findViewById(R.id.edt_thanh_tien_thoi_gian_thanh_toan);
        edtNgayThanhToan = findViewById(R.id.edt_thanh_tien_ngay_thanh_toan);
        btnXacNhan = findViewById(R.id.btn_thanh_tien);
        progressDialog = new ProgressDialog(this);
    }

    private void setInfoPhieuThanhToan() {
        Glide.with(ThanhTienActivity.this).load(PhieuThanhToanUtil.avtUser).into(imgAvtUser);
        tvNameUser.setText(PhieuThanhToanUtil.nameUser);
        tvUserSDT.setText(PhieuThanhToanUtil.sdtUser);
        tvTrangThai.setText(PhieuThanhToanUtil.trangThai);
        tvDichVu.setText(PhieuThanhToanUtil.dichVuKham);
        tvKetLuanTrieuChung.setText(PhieuThanhToanUtil.ketLuanTrieuChung);
        tvChanDoan.setText(PhieuThanhToanUtil.chanDoan);
        tvLieuPhap.setText(PhieuThanhToanUtil.lieuPhap);
        tvDonThuoc.setText(PhieuThanhToanUtil.donThuoc);
        tvThoiGianKham.setText(PhieuThanhToanUtil.thoiGianKham);
        tvNgayKham.setText(PhieuThanhToanUtil.ngayKham);
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
                onBackPressed();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClichAccept();
            }
        });
    }
    private void getIdUser() {
        Intent intent = new Intent(ThanhTienActivity.this, InfoUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idUser", accountIdUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void onClichAccept() {

        thanhTien = edtThanhTien.getText().toString().trim();
        tgThanhToan = edtThoiGianThanhToan.getText().toString().trim();
        ngayThanhToan = edtNgayThanhToan.getText().toString().trim();

        thanhtoan.put("trangThai", mTrangthai);
        thanhtoan.put("thanhTien", thanhTien);
        thanhtoan.put("tGThanhToan", tgThanhToan);
        thanhtoan.put("ngayThanhToan", ngayThanhToan);


        progressDialog.show();

        firebaseFirestore.collection("PhieuThanhToan").whereEqualTo("trangThai", "Chưa Thanh Toán").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    String time = PhieuThanhToanUtil.thoiGianKham;
                    String dichvu = PhieuThanhToanUtil.dichVuKham;
                    if (dichvu.equals(documentSnapshot.getString("dichVuKham")) && time.equals(documentSnapshot.getString("tGKham"))
                            && accountIdUser.equals(documentSnapshot.getString("accountIdUser")) && accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor"))){
                        DocumentReference documentReference = firebaseFirestore.collection("PhieuThanhToan").document(dcId);
                        documentReference.update(thanhtoan).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                sendNotificationUser();
                                Toast.makeText(ThanhTienActivity.this, "Thanh Toán Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ThanhTienActivity.this, PhieuThanhToanActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(ThanhTienActivity.this, "Thanh Toán Thất Bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
    private void sendNotificationUser() {
        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshotSend : queryDocumentSnapshots){
                    String dcIdSend = documentSnapshotSend.getId();
                    String token = tokenUser;
                    if (Objects.equals(accountIdUser, documentSnapshotSend.getString("accountId"))
                            && token.equals(documentSnapshotSend.getString("tokenUser"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcIdSend);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                sendNotification();
                            }
                        });
                    }
                }
            }
        });
    }
    private void getTokenUser(){
        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshotToken : queryDocumentSnapshots){
                    String dcIdToken = documentSnapshotToken.getId();
                    if (Objects.equals(accountIdUser, documentSnapshotToken.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcIdToken);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshotTokenUser) {
                                tokenUser = documentSnapshotTokenUser.getString("tokenUser");
                            }
                        });
                    }
                }
            }
        });
    }

    private void sendNotification() {
        NotificationModel notificationModel = new NotificationModel("Thanh Toán", "Bạn Thanh Toán Thành Công!");
        Notification notificationUser = new Notification();
        notificationUser.setNotification(notificationModel);

        notificationUser.setToken(tokenUser);

        ApiService apiService =  ApiClient.getClient().create(ApiService.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(notificationUser);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("check","done");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}