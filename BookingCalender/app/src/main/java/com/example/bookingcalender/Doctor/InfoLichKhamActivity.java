package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.API.ApiClient;
import com.example.bookingcalender.API.ApiService;
import com.example.bookingcalender.Admin.HomeAdminActivity;
import com.example.bookingcalender.Admin.InfoDatLichActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.Model.Notification;
import com.example.bookingcalender.Model.NotificationModel;
import com.example.bookingcalender.R;
import com.example.bookingcalender.User.InfoUserDatLichActivity;
import com.example.bookingcalender.Util.BenhAnUtil;
import com.example.bookingcalender.Util.DatLichUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class InfoLichKhamActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen, tvTrangThai, tvThoiGianKham, tvNgayKham;
    Button btnXacNhan, btnHuy, btnUpdate;
    String avtuser, avtdoctor, tGDatKham, accountIdUser, accountIdDoctor, dichVuKham;

    String accountUser = "0";
    String tokenUser = " ";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_lich_kham);

        initUi();
        setInfoLichKham();
        initListener();
        getTokenUser();
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_info_lich_kham_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_info_lich_kham);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_info_lich_kham);
        tvNameUserBooking = findViewById(R.id.tv_name_user_info_lich_kham);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_info_lich_kham);
        tvTrangThai = findViewById(R.id.tv_info_lich_kham_trang_thai);
        tvBookingDichVu = findViewById(R.id.tv_info_lich_kham_dich_vu);
        tvBookingTrieuChung = findViewById(R.id.tv_info_lich_kham_trieu_chung);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_info_lich_kham);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_info_lich_kham);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_info_lich_kham);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_info_lich_kham);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_info_lich_kham);
        btnXacNhan = findViewById(R.id.btn_info_lich_kham);
        btnHuy = findViewById(R.id.btn_huy_lich_kham);
        btnUpdate = findViewById(R.id.btn_update_lich_kham);
        tvThoiGianKham = findViewById(R.id.tv_info_lich_kham_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_info_lich_kham_ngay_kham);
        progressDialog = new ProgressDialog(this);
    }

    private void setInfoLichKham() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        DatLich datLich = (DatLich) bundle.get("info_lichkham");
        Glide.with(InfoLichKhamActivity.this).load(datLich.getAvatarUser()).into(imgAvtUserBooking);
        tvNameUserBooking.setText(datLich.getNameUser());
        tvUserSDTBooking.setText(datLich.getDienThoaiUser());
        tvTrangThai.setText(datLich.getTrangThai());
        tvBookingDichVu.setText(datLich.getDichVuKham());
        tvBookingTrieuChung.setText(datLich.getTrieuChung());
        Glide.with(InfoLichKhamActivity.this).load(datLich.getAvatarBacSi()).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(datLich.getKhoaBacSi());
        tvNameDoctor.setText(datLich.getNameBacSi());
        tvNgayLvDoctor.setText(datLich.getNgayLV());
        tvDoctorTGLVTu.setText(datLich.gettGLVTu());
        tvDoctorTGLVDen.setText(datLich.gettGLVDen());
        tvThoiGianKham.setText(datLich.gettGDatKham());
        tvNgayKham.setText(datLich.getNgayDatKham());

        accountIdUser = datLich.getAccountIdUser();
        avtuser = datLich.getAvatarUser();
        avtdoctor = datLich.getAvatarBacSi();
        tGDatKham = datLich.gettGDatKham();
        accountIdDoctor = datLich.getAccountIdDoctor();
        dichVuKham = datLich.getDichVuKham();

        DatLichUtil.accountIdUser = datLich.getAccountIdUser();
        DatLichUtil.avt = datLich.getAvatarUser();
        DatLichUtil.ten = datLich.getNameUser();
        DatLichUtil.sdt = datLich.getDienThoaiUser();
        DatLichUtil.dichvu = datLich.getDichVuKham();
        DatLichUtil.trieuchung = datLich.getTrieuChung();
        DatLichUtil.avtbacsi = datLich.getAvatarBacSi();
        DatLichUtil.accountIdDoctor = datLich.getAccountIdDoctor();
        DatLichUtil.khoa = datLich.getKhoaBacSi();
        DatLichUtil.tenbacsi = datLich.getNameBacSi();
        DatLichUtil.ngaylamviec = datLich.getNgayLV();
        DatLichUtil.thoigianbatdau = datLich.gettGLVTu();
        DatLichUtil.thoigianketthuc = datLich.gettGLVDen();
        DatLichUtil.tGDatKham = datLich.gettGDatKham();
        DatLichUtil.ngayDatKham = datLich.getNgayDatKham();

    }

    private void initListener() {
        imgAvtUserBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdUser();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoLichKhamActivity.this, LichKhamActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInfo();
                Intent intent = new Intent(InfoLichKhamActivity.this, InfoKhamBenhActivity.class);
                startActivity(intent);
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLichKham();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoLichKhamActivity.this, UpdateLichKhamActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getIdUser() {
        Intent intent = new Intent(InfoLichKhamActivity.this, InfoUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("idUser", accountIdUser);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void deleteLichKham(){
        progressDialog.show();
        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ Khám").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                sendNotificationUser();
                                progressDialog.dismiss();
                                Toast.makeText(InfoLichKhamActivity.this, "Xóa Lịch Khám Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InfoLichKhamActivity.this, DoctorActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(InfoLichKhamActivity.this, "Không Tìm Thấy Lịch Khám!", Toast.LENGTH_SHORT).show();
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
                    if (accountUser.equals(documentSnapshotSend.getString("account")) && Objects.equals(accountIdUser, documentSnapshotSend.getString("accountId"))
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
                    if (accountUser.equals(documentSnapshotToken.getString("account")) && Objects.equals(accountIdUser, documentSnapshotToken.getString("accountId"))){
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
        NotificationModel notificationModel = new NotificationModel("Lịch Khám", "Lịch Khám Của Bạn Đã Bị Hủy Vì Đã Quá Thời Gian Đặt Khám!");
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
    private void putInfo(){
        BenhAnUtil.tGDatKham = tGDatKham;
        BenhAnUtil.accountId = accountIdUser;
        BenhAnUtil.accountIdDoctor = accountIdDoctor;
        BenhAnUtil.avtuser = avtuser;
        BenhAnUtil.nameuser = tvNameUserBooking.getText().toString();
        BenhAnUtil.sdt = tvUserSDTBooking.getText().toString();
        BenhAnUtil.trangthai = tvTrangThai.getText().toString();
        BenhAnUtil.dichvu = tvBookingDichVu.getText().toString();
        BenhAnUtil.trieuchung = tvBookingTrieuChung.getText().toString();
        BenhAnUtil.avtbacsi = avtdoctor;
        BenhAnUtil.khoa = tvKhoaDoctor.getText().toString();
        BenhAnUtil.tenbacsi = tvNameDoctor.getText().toString();
        BenhAnUtil.ngaylamviec = tvNgayLvDoctor.getText().toString();
        BenhAnUtil.thoigianbatdau = tvDoctorTGLVTu.getText().toString();
        BenhAnUtil.thoigianketthuc = tvDoctorTGLVDen.getText().toString();

    }

}