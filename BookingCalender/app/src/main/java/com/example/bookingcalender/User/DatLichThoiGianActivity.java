package com.example.bookingcalender.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Authentication.RegisterActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.DatLichUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DatLichThoiGianActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor, tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen;
    TextView tvThoiGianKham, tvNgayKham;
    Button btnDatLich;
    String accountIdUser = " ";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich_thoi_gian);

        initUi();
        setInfoDatLich();
        initListener();
        getInfoAccount();
    }
    private  void getInfoAccount(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null){
            return;
        }

        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    String UId = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                accountIdUser = documentSnapshot.getString("accountId");
                            }
                        });
                    }
                }
            }
        });
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
        Glide.with(DatLichThoiGianActivity.this).load(DatLichUtil.avt).into(imgAvtUserBooking);
        tvNameUserBooking.setText(DatLichUtil.ten);
        tvUserSDTBooking.setText(DatLichUtil.sdt);
        tvBookingDichVu.setText(DatLichUtil.dichvu);
        tvBookingTrieuChung.setText(DatLichUtil.trieuchung);
        Glide.with(DatLichThoiGianActivity.this).load(DatLichUtil.avtbacsi).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(DatLichUtil.khoa);
        tvNameDoctor.setText(DatLichUtil.tenbacsi);
        tvNgayLvDoctor.setText(DatLichUtil.ngaylamviec);
        tvDoctorTGLVTu.setText(DatLichUtil.thoigianbatdau);
        tvDoctorTGLVDen.setText(DatLichUtil.thoigianketthuc);
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_user_booking_back_thoi_gian);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_booking_thoi_gian);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_dat_lich_thoi_gian);
        tvNameUserBooking = findViewById(R.id.tv_name_user_booking_thoi_gian);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_booking_thoi_gian);
        tvBookingDichVu = findViewById(R.id.tv_booking_dich_vu_thoi_gian);
        tvBookingTrieuChung = findViewById(R.id.tv_booking_trieu_chung_thoi_gian);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_dat_lich_thoi_gian);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_dat_lich_thoi_gian);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_thoi_gian);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_thoi_gian);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_dat_lich_thoi_gian);
        btnDatLich = findViewById(R.id.btn_user_dat_lich);
        tvThoiGianKham = findViewById(R.id.tv_info_booking_thoi_gian_thoi_gian);
        tvNgayKham = findViewById(R.id.tv_info_booking_date_thoi_gian);
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

    private void datLich() {

        String thoigiankham, ngaykham;
        thoigiankham = tvThoiGianKham.getText().toString().trim();
        ngaykham = tvNgayKham.getText().toString().trim();

        DatLich datLich = new DatLich(accountIdUser, DatLichUtil.accountIdDoctor, DatLichUtil.avt, DatLichUtil.ten,
                DatLichUtil.sdt, DatLichUtil.dichvu, DatLichUtil.trieuchung, DatLichUtil.avtbacsi, DatLichUtil.khoa,
                DatLichUtil.tenbacsi, DatLichUtil.ngaylamviec, DatLichUtil.thoigianbatdau, DatLichUtil.thoigianketthuc,
                thoigiankham, ngaykham, "Chờ");
        progressDialog.show();
        firebaseFirestore.collection("DatLich").add(datLich).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(DatLichThoiGianActivity.this, "Đặt Lịch Thành Công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DatLichThoiGianActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(DatLichThoiGianActivity.this, "Đặt Lịch Thất Bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkCollection(){

        firebaseFirestore.collection("DatLich").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                checkDatLich();
                            } else {
                                datLich();
                            }
                        } else {
                            Toast.makeText(DatLichThoiGianActivity.this, "Đặt Lịch Thất Bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void checkDatLich(){
        String thoigiankham, ngaykham;
        thoigiankham = tvThoiGianKham.getText().toString().trim();
        ngaykham = tvNgayKham.getText().toString().trim();
        DatLich datLich = new DatLich(accountIdUser,
                DatLichUtil.accountIdDoctor,
                DatLichUtil.avt,
                DatLichUtil.ten,
                DatLichUtil.sdt,
                DatLichUtil.dichvu,
                DatLichUtil.trieuchung,
                DatLichUtil.avtbacsi,
                DatLichUtil.khoa,
                DatLichUtil.tenbacsi,
                DatLichUtil.ngaylamviec,
                DatLichUtil.thoigianbatdau,
                DatLichUtil.thoigianketthuc,
                thoigiankham,
                ngaykham,
                "Chờ");
        progressDialog.show();
        String UIdCheck = FirebaseAuth.getInstance().getUid();
        firebaseFirestore.collection("DatLich")
                .whereEqualTo("trangThai", "Chờ Khám")
                .whereEqualTo("accountIdUser", UIdCheck)
                .whereEqualTo("tGDatKham", thoigiankham)
                .whereEqualTo("dichVuKham", DatLichUtil.dichvu)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(DatLichThoiGianActivity.this, "Đã Đặt Lịch Với Thời Gian Và Dịch Vụ Khám Này Rồi!", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseFirestore.collection("DatLich").add(datLich).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                progressDialog.dismiss();
                                Toast.makeText(DatLichThoiGianActivity.this, "Đặt Lịch Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DatLichThoiGianActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(DatLichThoiGianActivity.this, "Đặt Lịch Thất Bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(DatLichThoiGianActivity.this, "Lỗi khi kiểm tra lịch: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}