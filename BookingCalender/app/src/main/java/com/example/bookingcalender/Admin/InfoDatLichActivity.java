package com.example.bookingcalender.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import com.example.bookingcalender.FirebaseCloudMessaging.MyFirebaseMessagingService;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.Model.Notification;
import com.example.bookingcalender.Model.NotificationModel;
import com.example.bookingcalender.R;
import com.example.bookingcalender.User.DatLichThoiGianActivity;
import com.example.bookingcalender.Util.DatLichUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoDatLichActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen, tvTrangThai, tvThoiGianKham, tvNgayKham;
    Button btnXacNhan;
    String accountDoctor = "1";
    String accountIdDoctor = "";
    String accountUser = "0";
    String accountIdUser = "";
    String tokenUser = " ";
    String tokenDoctor = " ";
    String tGKham = "";
    String dichVuKham = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dat_lich);

        initUi();
        setInfoDatLich();
        initListener();
        getTokenUser();
        getTokenDoctor();
    }

    private void sendNotification() {

        NotificationModel notificationModel = new NotificationModel("Đặt Lịch", "Đặt Lịch Thành Công!");
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
    private void sendNotificationTokenDoctor() {

        NotificationModel notificationModel = new NotificationModel("Lịch Khám", "Bạn Có Một Lịch Khám Mới!");
        Notification notificationDoctor = new Notification();
        notificationDoctor.setNotification(notificationModel);

        notificationDoctor.setToken(tokenDoctor);

        ApiService apiService =  ApiClient.getClient().create(ApiService.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(notificationDoctor);

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

    private void setInfoDatLich() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        DatLich datLich = (DatLich) bundle.get("info_datlich");
        Glide.with(InfoDatLichActivity.this).load(datLich.getAvatarUser()).into(imgAvtUserBooking);
        tvNameUserBooking.setText(datLich.getNameUser());
        tvUserSDTBooking.setText(datLich.getDienThoaiUser());
        tvTrangThai.setText(datLich.getTrangThai());
        tvBookingDichVu.setText(datLich.getDichVuKham());
        tvBookingTrieuChung.setText(datLich.getTrieuChung());
        Glide.with(InfoDatLichActivity.this).load(datLich.getAvatarBacSi()).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(datLich.getKhoaBacSi());
        tvNameDoctor.setText(datLich.getNameBacSi());
        tvNgayLvDoctor.setText(datLich.getNgayLV());
        tvDoctorTGLVTu.setText(datLich.gettGLVTu());
        tvDoctorTGLVDen.setText(datLich.gettGLVDen());
        tvThoiGianKham.setText(datLich.gettGDatKham());
        tvNgayKham.setText(datLich.getNgayDatKham());

        accountIdUser = datLich.getAccountIdUser();
        accountIdDoctor = datLich.getAccountIdDoctor();
        tGKham = datLich.gettGDatKham();
        dichVuKham = datLich.getDichVuKham();
    }
    private void initUi() {
        imgBack = findViewById(R.id.img_info_dat_lich_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_info_dat_lich);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_info_dat_lich);
        tvNameUserBooking = findViewById(R.id.tv_name_user_info_dat_lich);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_info_dat_lich);
        tvTrangThai = findViewById(R.id.tv_info_dat_lich_trang_thai);
        tvBookingDichVu = findViewById(R.id.tv_info_dat_lich_dich_vu);
        tvBookingTrieuChung = findViewById(R.id.tv_info_dat_lich_trieu_chung);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_info_dat_lich);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_info_dat_lich);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_info_dat_lich);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_info_dat_lich);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_info_dat_lich);
        btnXacNhan = findViewById(R.id.btn_info_dat_lich);
        tvThoiGianKham = findViewById(R.id.tv_info_dat_lich_thoi_gian_kham);
        tvNgayKham = findViewById(R.id.tv_info_dat_lich_ngay_kham);
    }
    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptInfoDatLich();
            }
        });
    }
    private void sendNotificationUser(){
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
    private void sendNotificationDoctor() {
        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshotSend : queryDocumentSnapshots){
                    String dcIdSendDoctor = documentSnapshotSend.getId();
                    String tokenIdDoctor = tokenDoctor;
                    if (accountDoctor.equals(documentSnapshotSend.getString("account")) && Objects.equals(accountIdDoctor, documentSnapshotSend.getString("accountId"))
                            && tokenIdDoctor.equals(documentSnapshotSend.getString("tokenDoctor"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcIdSendDoctor);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                sendNotificationTokenDoctor();
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
    private void getTokenDoctor(){
        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshotToken : queryDocumentSnapshots){
                    String dcIdTokenDoctor = documentSnapshotToken.getId();
                    if (Objects.equals(accountIdDoctor, documentSnapshotToken.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcIdTokenDoctor);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshotTokenDoctor) {
                                tokenDoctor = documentSnapshotTokenDoctor.getString("tokenDoctor");
                            }
                        });
                    }
                }
            }
        });
    }
    private void AcceptInfoDatLich() {

        String mTrangthai = "Chờ Khám";

        Map<String, Object> datlich = new HashMap<>();
        datlich.put("trangThai", mTrangthai);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        /*firebaseFirestore.collection("DatLich")
                .whereEqualTo("trangThai", "Chờ")
                .whereEqualTo("accountIdUser", accountIdUser)
                .whereEqualTo("accountIdDoctor", accountIdDoctor)
                .whereEqualTo("tGDatKham", tGKham)
                .whereEqualTo("dichVuKham", dichVuKham)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(InfoDatLichActivity.this, "Lịch Khám Này Đã Được Duyệt Rồi", Toast.LENGTH_SHORT).show();
                    } else {
                        String dcId = documentSnapshot.getId();
                        DocumentReference documentReference = firebaseFirestore.collection("DatLich").document(dcId);
                        documentReference.update(datlich).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                sendNotificationUser();
                                sendNotificationDoctor();
                                progressDialog.dismiss();
                                Toast.makeText(InfoDatLichActivity.this, "Duyệt Lịch Khám Thành Công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InfoDatLichActivity.this, HomeAdminActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(InfoDatLichActivity.this, "Lỗi khi kiểm tra lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });*/

        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor")) && accountIdUser.equals(documentSnapshot.getString("accountIdUser")) && tGKham.equals(documentSnapshot.getString("tGDatKham")) && dichVuKham.equals(documentSnapshot.getString("dichVuKham"))) {
                        DocumentReference documentReference = firebaseFirestore.collection("DatLich").document(dcId);
                        documentReference.update(datlich).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                sendNotificationUser();
                                sendNotificationDoctor();
                                progressDialog.dismiss();
                                Toast.makeText(InfoDatLichActivity.this, "Duyệt Lịch Khám Thành Công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InfoDatLichActivity.this, HomeAdminActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(InfoDatLichActivity.this, "Duyệt Lịch Khám Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

}