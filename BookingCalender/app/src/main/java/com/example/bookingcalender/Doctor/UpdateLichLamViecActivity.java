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

public class UpdateLichLamViecActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageView imgBack, imgAvtDoctorUpdateLlv;
    TextView tvNameDoctorUpdateLlv, tvKhoaDoctorUpdateLlv;
    EditText edtDoctorUpdatePhongLlv, edtDoctorUpdateThoiGianLlvTu, edtDoctorUpdateThoiGianLlvDen, edtDoctorUpdateNgayLlv;
    Button btnXacNhan;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lich_lam_viec);

        initUi();
        setInfoDoctor();
        initListener();
    }

    private void setInfoDoctor() {
        FirebaseUser mDoctor = FirebaseAuth.getInstance().getCurrentUser();
        if (mDoctor == null){
            return;
        }
        progressDialog.show();
        Uri picUrl = mDoctor.getPhotoUrl();
        Glide.with(this).load(picUrl).error(R.drawable.avt_default).into(imgAvtDoctorUpdateLlv);

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
                                String img = documentSnapshot.getString("avatarDoctor");
                                Glide.with(UpdateLichLamViecActivity.this).load(img).into(imgAvtDoctorUpdateLlv);
                                tvNameDoctorUpdateLlv.setText(documentSnapshot.getString("nameDoctor"));
                                tvKhoaDoctorUpdateLlv.setText(documentSnapshot.getString("khoaDoctor"));
                                edtDoctorUpdatePhongLlv.setText(documentSnapshot.getString("phongLV"));
                                edtDoctorUpdateThoiGianLlvTu.setText(documentSnapshot.getString("tGLVTu"));
                                edtDoctorUpdateThoiGianLlvDen.setText(documentSnapshot.getString("tGLVDen"));
                                edtDoctorUpdateNgayLlv.setText(documentSnapshot.getString("ngayLV"));
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
        imgBack = findViewById(R.id.img_doctor_update_lich_lam_viec_back);
        imgAvtDoctorUpdateLlv = findViewById(R.id.img_avt_doctor_update_lich_lam_viec);
        tvNameDoctorUpdateLlv = findViewById(R.id.tv_name_doctor_update_lich_lam_viec);
        tvKhoaDoctorUpdateLlv = findViewById(R.id.tv_khoa_doctor_update_lich_lam_viec);
        edtDoctorUpdatePhongLlv = findViewById(R.id.edt_update_lich_lam_viec_phong);
        edtDoctorUpdateThoiGianLlvTu = findViewById(R.id.edt_update_lich_lam_viec_thoi_gian_bat_dau);
        edtDoctorUpdateThoiGianLlvDen = findViewById(R.id.edt_update_lich_lam_viec_thoi_gian_ket_thuc);
        edtDoctorUpdateNgayLlv = findViewById(R.id.edt_update_lich_lam_viec_ngay);
        btnXacNhan = findViewById(R.id.btn_doctor_accept_update_lich_lam_viec);
        progressDialog = new ProgressDialog(this);
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
                setDoctorUpdateInfoLlv();
            }
        });

    }

    private void setDoctorUpdateInfoLlv() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        progressDialog.show();

        firebaseFirestore.collection("LichLamViec").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    String UId = FirebaseAuth.getInstance().getUid();

                    String phong, timeStart, timeEnd, ngay;

                    phong = String.valueOf(edtDoctorUpdatePhongLlv.getText());
                    timeStart = String.valueOf(edtDoctorUpdateThoiGianLlvTu.getText());
                    timeEnd = String.valueOf(edtDoctorUpdateThoiGianLlvDen.getText());
                    ngay = String.valueOf(edtDoctorUpdateNgayLlv.getText());

                    Map<String, Object> lichlamviec = new HashMap<>();
                    lichlamviec.put("phongLV", phong);
                    lichlamviec.put("tGLVTu", timeStart);
                    lichlamviec.put("tGLVDen", timeEnd);
                    lichlamviec.put("ngayLV", ngay);

                    if (Objects.equals(UId, documentSnapshot.getString("accountIdDoctor"))){
                        DocumentReference documentReference = firebaseFirestore.collection("LichLamViec").document(dcId);
                        documentReference.update(lichlamviec).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateLichLamViecActivity.this, "Cập Nhật Lịch Làm Việc Thành Công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateLichLamViecActivity.this, LichLamViecActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateLichLamViecActivity.this, "Cập Nhật Lịch Làm Việc Thất Bại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}