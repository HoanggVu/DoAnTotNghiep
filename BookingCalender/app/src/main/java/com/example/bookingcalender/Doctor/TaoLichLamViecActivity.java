package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Model.Doctor;
import com.example.bookingcalender.Model.LichLamViec;
import com.example.bookingcalender.R;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TaoLichLamViecActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtDoctorTaoLlv;
    TextView tvNameDoctorTaoLlv, tvKhoaDoctorTaoLlv;
    EditText edtDoctorTaoPhongLlv, edtDoctorTaoThoiGianLlvTu, edtDoctorTaoThoiGianLlvDen, edtDoctorTaoNgayLlv;
    Button btnTao;
    String img, accountIdDoctor, nameDoctor, khoaDoctor;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_lich_lam_viec);

        initUi();
        setInfoDoctor();
        initListener();
    }

    private void initUi() {
        imgBack = findViewById(R.id.img_doctor_tao_lich_lam_viec_back);
        imgAvtDoctorTaoLlv = findViewById(R.id.img_avt_doctor_tao_lich_lam_viec);
        tvNameDoctorTaoLlv = findViewById(R.id.tv_name_doctor_tao_lich_lam_viec);
        tvKhoaDoctorTaoLlv = findViewById(R.id.tv_khoa_doctor_tao_lich_lam_viec);
        edtDoctorTaoPhongLlv = findViewById(R.id.edt_tao_lich_lam_viec_phong);
        edtDoctorTaoThoiGianLlvTu = findViewById(R.id.edt_tao_lich_lam_viec_thoi_gian_bat_dau);
        edtDoctorTaoThoiGianLlvDen = findViewById(R.id.edt_tao_lich_lam_viec_thoi_gian_ket_thuc);
        edtDoctorTaoNgayLlv = findViewById(R.id.edt_tao_lich_lam_viec_ngay);
        btnTao = findViewById(R.id.btn_doctor_accept_tao_lich_lam_viec);
        progressDialog = new ProgressDialog(this);
    }

    private void setInfoDoctor() {
        FirebaseUser mDoctor = FirebaseAuth.getInstance().getCurrentUser();
        if (mDoctor == null){
            return;
        }

        Uri picUrl = mDoctor.getPhotoUrl();
        Glide.with(this).load(picUrl).error(R.drawable.avt_default).into(imgAvtDoctorTaoLlv);

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
                                img = documentSnapshot.getString("avatar").toString();
                                Glide.with(TaoLichLamViecActivity.this).load(img).into(imgAvtDoctorTaoLlv);
                                tvNameDoctorTaoLlv.setText(documentSnapshot.getString("hoTen"));
                                tvKhoaDoctorTaoLlv.setText(documentSnapshot.getString("chuyenKhoa"));

                                accountIdDoctor = documentSnapshot.getString("accountId");
                                nameDoctor = documentSnapshot.getString("hoTen");
                                khoaDoctor = documentSnapshot.getString("chuyenKhoa");

                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDoctorTaoInfoLlv();
            }
        });
    }

    private void setDoctorTaoInfoLlv() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        String phongLv, tGLVTu, tGLVDen, ngayLV;

        phongLv = String.valueOf(edtDoctorTaoPhongLlv.getText());
        tGLVTu = String.valueOf(edtDoctorTaoThoiGianLlvTu.getText());
        tGLVDen = String.valueOf(edtDoctorTaoThoiGianLlvDen.getText());
        ngayLV = String.valueOf(edtDoctorTaoNgayLlv.getText());

        progressDialog.show();
        LichLamViec lichLamViec = new LichLamViec(accountIdDoctor, img, nameDoctor, khoaDoctor, phongLv, ngayLV, tGLVTu, tGLVDen);
        firebaseFirestore.collection("LichLamViec").add(lichLamViec).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(TaoLichLamViecActivity.this, "Tạo Mới Lịch Làm Việc Thành Công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TaoLichLamViecActivity.this, LichLamViecActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TaoLichLamViecActivity.this, "Tạo Mới Lịch Làm Việc Thất Bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}