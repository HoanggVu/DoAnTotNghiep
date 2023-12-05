package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.API.ApiClient;
import com.example.bookingcalender.API.ApiService;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class UpdateLichKhamActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen;
    TextView tvThoiGianKham, tvNgayKham;
    Button btnDatLich;
    ProgressDialog progressDialog;
    String accountIdUser = " ";
    String accountIdDoctor = " ";
    String tGDatKham = " ";
    String dichVuKham = " ";
    String tokenUser = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lich_kham);
        initUi();
        setInfoDatLich();
        initListener();
        getTokenUser();
    }
    private void setTime(){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, hourOfDay, minute);
                tvThoiGianKham.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }
    private void setDate(){
        final Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvNgayKham.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, date);
        datePickerDialog.show();
    }
    private void setInfoDatLich() {
        Glide.with(UpdateLichKhamActivity.this).load(DatLichUtil.avt).into(imgAvtUserBooking);
        tvNameUserBooking.setText(DatLichUtil.ten);
        tvUserSDTBooking.setText(DatLichUtil.sdt);
        tvBookingDichVu.setText(DatLichUtil.dichvu);
        tvBookingTrieuChung.setText(DatLichUtil.trieuchung);
        Glide.with(UpdateLichKhamActivity.this).load(DatLichUtil.avtbacsi).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(DatLichUtil.khoa);
        tvNameDoctor.setText(DatLichUtil.tenbacsi);
        tvNgayLvDoctor.setText(DatLichUtil.ngaylamviec);
        tvDoctorTGLVTu.setText(DatLichUtil.thoigianbatdau);
        tvDoctorTGLVDen.setText(DatLichUtil.thoigianketthuc);
        tvNgayKham.setText(DatLichUtil.ngayDatKham);
        tvThoiGianKham.setText(DatLichUtil.tGDatKham);

        accountIdUser = DatLichUtil.accountIdUser;
        accountIdDoctor = DatLichUtil.accountIdDoctor;
        tGDatKham = DatLichUtil.tGDatKham;
        dichVuKham = DatLichUtil.dichvu;
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_doctor_back_update_lich_kham);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_update_lich_kham);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_update_lich_kham);
        tvNameUserBooking = findViewById(R.id.tv_name_user_update_lich_kham);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_update_lich_kham);
        tvBookingDichVu = findViewById(R.id.tv_dich_vu_update_lich_kham);
        tvBookingTrieuChung = findViewById(R.id.tv_trieu_chung_update_lich_kham);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_update_lich_kham);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_update_lich_kham);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_update_lich_kham);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_update_lich_kham);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_update_lich_kham);
        btnDatLich = findViewById(R.id.btn_update_lich_kham);
        tvThoiGianKham = findViewById(R.id.tv_info_booking_thoi_gian_update_lich_kham);
        tvNgayKham = findViewById(R.id.tv_info_booking_date_update_lich_kham);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnDatLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDatLich();
            }
        });
        tvNgayKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        tvThoiGianKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });
    }

    private void checkDatLich() {
        String tgDatKham1, ngayDatKham1;
        tgDatKham1 = tvThoiGianKham.getText().toString();
        ngayDatKham1 = tvNgayKham.getText().toString();

        Map<String, Object> updates = new HashMap<>();
        updates.put("tGDatKham", tgDatKham1);
        updates.put("ngayDatKham", ngayDatKham1);
        progressDialog.show();
        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ Khám").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    if (accountIdDoctor.equals(documentSnapshot.getString("accountIdDoctor")) && accountIdUser.equals(documentSnapshot.getString("accountIdUser"))
                            && tGDatKham.equals(documentSnapshot.getString("tGDatKham")) && dichVuKham.equals(documentSnapshot.getString("dichVuKham"))) {
                        DocumentReference documentReference = firebaseFirestore.collection("DatLich").document(dcId);
                        documentReference.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                sendNotificationUser();
                                progressDialog.dismiss();
                                Toast.makeText(UpdateLichKhamActivity.this, "Cập Nhật Lịch Khám Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateLichKhamActivity.this, DoctorActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateLichKhamActivity.this, "Không Tìm Thấy Lịch Khám!", Toast.LENGTH_SHORT).show();
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
        NotificationModel notificationModel = new NotificationModel("Lịch Khám", "Bác Sĩ Đã Cập Nhật Lịch Khám Của Bạn!");
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