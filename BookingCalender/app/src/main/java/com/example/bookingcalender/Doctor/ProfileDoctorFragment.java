package com.example.bookingcalender.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileDoctorFragment extends Fragment {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    View view;
    ImageView imgAvtProfileDoctor;
    TextView tvDoctorEmail, tvDoctorHoVaTen, tvDoctorNgaySinh, tvDoctorGioiTinh, tvDoctorSDT, tvDoctorChuyenKhoa, tvDoctorChucVu, tvDoctorBangCap, tvDoctorChuyenMon;
    Button btnUpdateProfileDoctor;
    Button btnNewProfileDoctor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_doctor, container, false);

        initUi();
        setDoctorInfo();
        initListener();

        return view;
    }
    private void initUi(){
        imgAvtProfileDoctor = view.findViewById(R.id.img_view_profile_doctor);
        tvDoctorHoVaTen = view.findViewById(R.id.tv_profile_doctor_ho_ten);
        tvDoctorNgaySinh = view.findViewById(R.id.tv_profile_doctor_ngay_sinh);
        tvDoctorGioiTinh = view.findViewById(R.id.tv_profile_doctor_gioi_tinh);
        tvDoctorSDT = view.findViewById(R.id.tv_profile_doctor_sdt);
        tvDoctorEmail = view.findViewById(R.id.tv_profile_doctor_email);
        tvDoctorChuyenKhoa = view.findViewById(R.id.tv_profile_doctor_chuyen_khoa);
        tvDoctorChucVu = view.findViewById(R.id.tv_profile_doctor_chuc_vu);
        tvDoctorBangCap = view.findViewById(R.id.tv_profile_doctor_bang_cap);
        tvDoctorChuyenMon = view.findViewById(R.id.tv_profile_doctor_chuyen_mon);
        btnUpdateProfileDoctor = view.findViewById(R.id.btn_update_profile_doctor);
        btnNewProfileDoctor = view.findViewById(R.id.btn_new_profile_doctor);
        progressDialog = new ProgressDialog(getActivity());
    }
    private void setDoctorInfo() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor != null){
            tvDoctorEmail.setText(doctor.getEmail());
            Glide.with(getActivity()).load(doctor.getPhotoUrl()).error(R.drawable.avt_default).into(imgAvtProfileDoctor);

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
                                    Glide.with(getActivity()).load(img).into(imgAvtProfileDoctor);
                                    tvDoctorHoVaTen.setText(documentSnapshot.getString("hoTen"));
                                    tvDoctorNgaySinh.setText(documentSnapshot.getString("ngaySinh"));
                                    tvDoctorGioiTinh.setText(documentSnapshot.getString("gioiTinh"));
                                    tvDoctorSDT.setText(documentSnapshot.getString("dienThoai"));
                                    tvDoctorChuyenKhoa.setText(documentSnapshot.getString("chuyenKhoa"));
                                    tvDoctorChucVu.setText(documentSnapshot.getString("chucVu"));
                                    tvDoctorBangCap.setText(documentSnapshot.getString("bangCap"));
                                    tvDoctorChuyenMon.setText(documentSnapshot.getString("chuyenMon"));
                                }
                            });
                        }
                    }
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Chưa Cập Nhật Thông Tin!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            return;
        }
    }
    private void initListener(){
        btnUpdateProfileDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileDoctorActivity.class);
                startActivity(intent);
            }
        });
        btnNewProfileDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = tvDoctorHoVaTen.getText().toString();
                if (TextUtils.isEmpty(check)){
                    Intent intent = new Intent(getActivity(), NewProfileDoctorActivity.class);
                    startActivity(intent);
                    return;
                }
                Toast.makeText(getActivity(), "Bạn Đã Có Thông Tin, Nếu Có Sửa Đổi Vui Lòng Cập Nhật!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
