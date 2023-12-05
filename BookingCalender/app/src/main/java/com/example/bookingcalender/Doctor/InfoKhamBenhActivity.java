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
import com.example.bookingcalender.Admin.HomeAdminActivity;
import com.example.bookingcalender.Admin.InfoDatLichActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.Model.Notification;
import com.example.bookingcalender.Model.NotificationModel;
import com.example.bookingcalender.Model.PhieuThanhToan;
import com.example.bookingcalender.R;
import com.example.bookingcalender.User.DatLichThoiGianActivity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class InfoKhamBenhActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtUserBooking, imgAvtDoctorBooking;
    TextView tvNameUserBooking, tvUserSDTBooking, tvBookingDichVu, tvBookingTrieuChung, tvKhoaDoctor, tvNameDoctor,
            tvNgayLvDoctor, tvDoctorTGLVTu, tvDoctorTGLVDen, tvTrangThai;
    EditText edtKetLuanTrieuChung;
    EditText edtChanDoan;
    EditText edtLieuPhap;
    EditText edtDonThuoc;
    EditText edtThoiGianKham;
    EditText edtvNgayKham;
    Map<String, Object> datlich = new HashMap<>();
    Button btnXacNhan;
    String  accountId;
    String mTrangthai = "Đã Khám";
    String accountIdDoctor = BenhAnUtil.accountIdDoctor;
    String ketluantrieuchung, chandoan, lieuphap, donthuoc, thoigiankham, ngaykham;
    ProgressDialog progressDialog;
    String tokenUser = " ";
    String accountIdUser = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_kham_benh);

        initUi();
        setInfoKhamBenh();
        initListener();
        setDateTimeKham();
        getTokenUser();
    }

    private void setDateTimeKham() {
        final Calendar calendar = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        String time = DateFormat.getTimeInstance().format(currentTime);
        edtThoiGianKham.setText(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        edtvNgayKham.setText(simpleDateFormat.format(calendar.getTime()));
       /* String date = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
        edtvNgayKham.setText(date);*/
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_info_kham_benh_back);
        imgAvtUserBooking = findViewById(R.id.img_avt_user_info_kham_benh);
        imgAvtDoctorBooking = findViewById(R.id.img_avt_doctor_info_kham_benh);
        tvNameUserBooking = findViewById(R.id.tv_name_user_info_kham_benh);
        tvUserSDTBooking = findViewById(R.id.tv_phone_user_info_kham_benh);
        tvTrangThai = findViewById(R.id.tv_info_kham_benh_trang_thai);
        tvBookingDichVu = findViewById(R.id.tv_info_kham_benh_dich_vu);
        tvBookingTrieuChung = findViewById(R.id.tv_info_kham_benh_trieu_chung);
        tvKhoaDoctor = findViewById(R.id.tv_khoa_doctor_info_kham_benh);
        tvNameDoctor = findViewById(R.id.tv_name_doctor_info_kham_benh);
        tvDoctorTGLVTu = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_bat_dau_info_kham_benh);
        tvDoctorTGLVDen = findViewById(R.id.tv_doctor_lich_lam_viec_thoi_gian_ket_thuc_info_kham_benh);
        tvNgayLvDoctor = findViewById(R.id.tv_ngay_lam_viec_doctor_info_kham_benh);
        edtKetLuanTrieuChung = findViewById(R.id.edt_info_kham_benh_ket_luan_trieu_chung);
        edtChanDoan = findViewById(R.id.edt_info_kham_benh_chan_doan);
        edtLieuPhap = findViewById(R.id.edt_info_kham_benh_lieu_phap);
        edtDonThuoc = findViewById(R.id.edt_info_kham_benh_don_thuoc);
        btnXacNhan = findViewById(R.id.btn_info_kham_benh);
        edtThoiGianKham = findViewById(R.id.edt_info_kham_benh_thoi_gian_kham);
        edtvNgayKham = findViewById(R.id.edt_info_kham_benh_ngay_kham);
        progressDialog = new ProgressDialog(this);
    }

    private void setInfoKhamBenh() {
        accountId = BenhAnUtil.accountId;
        Glide.with(InfoKhamBenhActivity.this).load(BenhAnUtil.avtuser).into(imgAvtUserBooking);
        tvNameUserBooking.setText(BenhAnUtil.nameuser);
        tvUserSDTBooking.setText(BenhAnUtil.sdt);
        tvTrangThai.setText(BenhAnUtil.trangthai);
        tvBookingDichVu.setText(BenhAnUtil.dichvu);
        tvBookingTrieuChung.setText(BenhAnUtil.trieuchung);
        Glide.with(InfoKhamBenhActivity.this).load(BenhAnUtil.avtbacsi).into(imgAvtDoctorBooking);
        tvKhoaDoctor.setText(BenhAnUtil.khoa);
        tvNameDoctor.setText(BenhAnUtil.tenbacsi);
        tvNgayLvDoctor.setText(BenhAnUtil.ngaylamviec);
        tvDoctorTGLVTu.setText(BenhAnUtil.thoigianbatdau);
        tvDoctorTGLVDen.setText(BenhAnUtil.thoigianketthuc);
        accountIdUser = BenhAnUtil.accountId;
    }

    private void initListener() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoBenhAn();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void changeInfo(){

        datlich.put("trangThai", mTrangthai);

        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ Khám").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    String time = BenhAnUtil.tGDatKham;
                    String dichvu = BenhAnUtil.dichvu;
                    if (dichvu.equals(documentSnapshot.getString("dichVuKham")) && time.equals(documentSnapshot.getString("tGDatKham"))
                            && accountId.equals(documentSnapshot.getString("accountIdUser"))){
                        DocumentReference documentReference = firebaseFirestore.collection("DatLich").document(dcId);
                        documentReference.update(datlich);
                    }
                }
            }
        });
    }
    private void getInfoBenhAn() {

        ketluantrieuchung = edtKetLuanTrieuChung.getText().toString().trim();
        chandoan = edtChanDoan.getText().toString().trim();
        lieuphap = edtLieuPhap.getText().toString().trim();
        donthuoc = edtDonThuoc.getText().toString().trim();
        thoigiankham = edtThoiGianKham.getText().toString().trim();
        ngaykham = edtvNgayKham.getText().toString().trim();

        BenhAn benhAn = new BenhAn(accountId, accountIdDoctor, BenhAnUtil.avtuser, BenhAnUtil.nameuser, BenhAnUtil.sdt,mTrangthai,
                BenhAnUtil.dichvu, BenhAnUtil.trieuchung, BenhAnUtil.avtbacsi, BenhAnUtil.khoa,
                BenhAnUtil.tenbacsi, BenhAnUtil.ngaylamviec, BenhAnUtil.thoigianbatdau, BenhAnUtil.thoigianketthuc,
                ketluantrieuchung, chandoan, lieuphap, donthuoc, thoigiankham, ngaykham);


        progressDialog.show();

        firebaseFirestore.collection("BenhAn").add(benhAn).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(InfoKhamBenhActivity.this, "Xuất Bệnh Án Thành Công!", Toast.LENGTH_SHORT).show();
                changeInfo();
                createPhieuThanhToan();
                sendNotificationUser();
                Intent intent = new Intent(InfoKhamBenhActivity.this, DoctorActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(InfoKhamBenhActivity.this, "Xuất Bệnh Án Thất Bại!", Toast.LENGTH_SHORT).show();
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
        NotificationModel notificationModel = new NotificationModel("Phiếu Thanh Toán", "Bạn Có Một Phiếu Thanh Toán Chưa Thanh Toán!" + "\n" + "Hãy Thanh Toán Sớm Nhé");
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

    private void createPhieuThanhToan() {
        PhieuThanhToan phieuThanhToan = new PhieuThanhToan(accountId, accountIdDoctor, BenhAnUtil.avtuser, BenhAnUtil.nameuser,
                BenhAnUtil.sdt, BenhAnUtil.avtbacsi, BenhAnUtil.khoa, BenhAnUtil.tenbacsi, BenhAnUtil.ngaylamviec, BenhAnUtil.thoigianbatdau,
                BenhAnUtil.thoigianketthuc,"Chưa Thanh Toán", BenhAnUtil.dichvu, ketluantrieuchung, chandoan, lieuphap, donthuoc,
                thoigiankham, ngaykham, " ", " ", " ");
        firebaseFirestore.collection("PhieuThanhToan").add(phieuThanhToan);
    }
}