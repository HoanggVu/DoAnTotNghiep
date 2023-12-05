package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LichLamViecActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtDoctorLlv, imgDelete;
    TextView tvNameDoctorLlv, tvKhoaDoctorLlv, tvDoctorPhongLlv, tvDoctorThoiGianLlvTu, tvDoctorThoiGianLlvDen, tvDoctorNgayLlv;
    Button btnDoctorTaoLlv, btnDoctorUpdateLlv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_lam_viec);

        initUi();
        setInfoDoctor();
        initListener();
    }

    private void setInfoDoctor() {
        FirebaseUser mDoctor = FirebaseAuth.getInstance().getCurrentUser();
        if (mDoctor == null){
            return;
        }
        Uri picUrl = mDoctor.getPhotoUrl();
        Glide.with(this).load(picUrl).error(R.drawable.avt_default).into(imgAvtDoctorLlv);

        progressDialog.show();
        firebaseFirestore.collection("BacSi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    String UId = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("BacSi").document(dcId);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String img = documentSnapshot.getString("avatar").toString();
                                Glide.with(LichLamViecActivity.this).load(img).into(imgAvtDoctorLlv);
                                tvNameDoctorLlv.setText(documentSnapshot.getString("hoTen"));
                                tvKhoaDoctorLlv.setText(documentSnapshot.getString("chuyenKhoa"));
                                progressDialog.dismiss();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
            }
        });
        progressDialog.show();
        firebaseFirestore.collection("LichLamViec").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcIdLLV = documentSnapshot.getId();
                    String UIdLLV = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(UIdLLV, documentSnapshot.getString("accountIdDoctor"))){
                        DocumentReference documentReference = firebaseFirestore.collection("LichLamViec").document(dcIdLLV);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                tvDoctorPhongLlv.setText(documentSnapshot.getString("phongLV"));
                                tvDoctorThoiGianLlvTu.setText(documentSnapshot.getString("tGLVTu"));
                                tvDoctorThoiGianLlvDen.setText(documentSnapshot.getString("tGLVDen"));
                                tvDoctorNgayLlv.setText(documentSnapshot.getString("ngayLV"));
                                progressDialog.dismiss();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void initUi() {
        imgAvtDoctorLlv = findViewById(R.id.img_avt_doctor_lich_lam_viec);
        imgBack = findViewById(R.id.img_doctor_lich_lam_viec_back);
        imgDelete = findViewById(R.id.img_doctor_lich_lam_viec_delete);
        tvNameDoctorLlv = findViewById(R.id.tv_name_doctor_lich_lam_viec);
        tvKhoaDoctorLlv = findViewById(R.id.tv_khoa_doctor_lich_lam_viec);
        tvDoctorPhongLlv = findViewById(R.id.tv_lich_lam_viec_phong);
        tvDoctorThoiGianLlvTu = findViewById(R.id.tv_lich_lam_viec_thoi_gian_bat_dau);
        tvDoctorThoiGianLlvDen = findViewById(R.id.tv_lich_lam_viec_thoi_gian_ket_thuc);
        tvDoctorNgayLlv = findViewById(R.id.tv_lich_lam_viec_ngay);
        btnDoctorUpdateLlv = findViewById(R.id.btn_doctor_update_lich_lam_viec);
        btnDoctorTaoLlv = findViewById(R.id.btn_doctor_tao_lich_lam_viec);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        btnDoctorUpdateLlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LichLamViecActivity.this, UpdateLichLamViecActivity.class);
                startActivity(intent);
            }
        });
        btnDoctorTaoLlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = tvDoctorNgayLlv.getText().toString();
                if (TextUtils.isEmpty(check)){
                    Intent intent = new Intent(LichLamViecActivity.this, TaoLichLamViecActivity.class);
                    startActivity(intent);
                    return;
                }
                Toast.makeText(LichLamViecActivity.this, "Bạn Đã Có Lịch Làm Việc, Nếu Cần Sửa Thông Tin Vui Lòng Cập Nhật!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LichLamViecActivity.this, DoctorActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLLV();
            }
        });
    }

    private void deleteLLV() {
        progressDialog.show();
        firebaseFirestore.collection("LichLamViec").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcIdDelete = documentSnapshot.getId();
                    String accountIdDoctor = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(accountIdDoctor, documentSnapshot.getString("accountIdDoctor"))) {
                        DocumentReference documentReference = firebaseFirestore.collection("LichLamViec").document(dcIdDelete);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(LichLamViecActivity.this, "Xóa Lịch Làm Việc Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LichLamViecActivity.this, LichLamViecActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LichLamViecActivity.this, "Không Tìm Thấy Lịch Làm Việc!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

}