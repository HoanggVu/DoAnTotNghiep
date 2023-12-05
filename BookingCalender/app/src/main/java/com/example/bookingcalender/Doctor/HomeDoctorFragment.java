package com.example.bookingcalender.Doctor;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class HomeDoctorFragment extends Fragment {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    View view;
    ImageView imgAvtDoctorHome, imgDocTorLichLamViec, imgDoctorLichKham, imgDoctorBenhAn, imgDoctorPhieuThanhToan;
    TextView tvNameDoctorHome, tvDutyDoctorHome, tvProfessionDoctorHome, tvChuaKham, tvDaKham, tvTongTien;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_doctor, container, false);

        initUi();
        setInfoDoctorHome();
        initListener();
        setInfoLichKham();
        return view;
    }

    private void setInfoLichKham() {
        firebaseFirestore.collection("DatLich").whereEqualTo("trangThai", "Chờ Khám").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String UId = FirebaseAuth.getInstance().getUid();
                int count = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (Objects.equals(UId, documentSnapshot.getString("accountIdDoctor"))){
                        count++;
                    }
                }
                String rs = String.valueOf(count);
                tvChuaKham.setText(rs);
            }
        });
        firebaseFirestore.collection("BenhAn").whereEqualTo("trangThai", "Đã Khám").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String UId = FirebaseAuth.getInstance().getUid();
                int count1 = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (Objects.equals(UId, documentSnapshot.getString("accountIdDoctor"))){
                        count1++;
                    }
                    String rs1 = String.valueOf(count1);
                    tvDaKham.setText(rs1);
                }
            }
        });
        firebaseFirestore.collection("PhieuThanhToan").whereEqualTo("trangThai", "Đã Thanh Toán").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String UId = FirebaseAuth.getInstance().getUid();
                int count2 = 0;
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    int tien = Integer.parseInt(Objects.requireNonNull(documentSnapshot.getString("thanhTien")));
                    if (Objects.equals(UId, documentSnapshot.getString("accountIdDoctor"))){
                        count2 += tien;
                    }
                }
                String rs2 = String.valueOf(count2);
                tvTongTien.setText(rs2);
            }
        });
    }

    private void initListener() {
        imgDocTorLichLamViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LichLamViecActivity.class);
                startActivity(intent);
            }
        });
        imgDoctorLichKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LichKhamActivity.class);
                startActivity(intent);
            }
        });
        imgDoctorBenhAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BenhAnActivity.class);
                startActivity(intent);
            }
        });
        imgDoctorPhieuThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhieuThanhToanActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setInfoDoctorHome() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        Glide.with(requireActivity()).load(doctor.getPhotoUrl()).error(R.drawable.avt_default).into(imgAvtDoctorHome);
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
                                String img = documentSnapshot.getString("avatar");
                                Glide.with(requireActivity()).load(img).into(imgAvtDoctorHome);
                                tvNameDoctorHome.setText(documentSnapshot.getString("hoTen"));
                                tvDutyDoctorHome.setText(documentSnapshot.getString("chuyenKhoa"));
                                tvProfessionDoctorHome.setText(documentSnapshot.getString("chucVu"));
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        });
    }
    private void initUi() {
        imgAvtDoctorHome = view.findViewById(R.id.img_avt_doctor_home);
        tvNameDoctorHome = view.findViewById(R.id.tv_name_doctor_home);
        tvDutyDoctorHome = view.findViewById(R.id.tv_duty_doctor_home);
        imgDocTorLichLamViec = view.findViewById(R.id.img_doctor_lich_lam_viec);
        imgDoctorLichKham = view.findViewById(R.id.img_doctor_lich_kham);
        imgDoctorBenhAn = view.findViewById(R.id.img_doctor_benh_an);
        imgDoctorPhieuThanhToan = view.findViewById(R.id.img_doctor_phieu_thanh_toan);
        tvProfessionDoctorHome = view.findViewById(R.id.tv_profession_doctor_home);
        tvChuaKham = view.findViewById(R.id.tv_lich_chua_kham);
        tvDaKham = view.findViewById(R.id.tv_lich_da_kham);
        tvTongTien = view.findViewById(R.id.tv_tong_tien);
        progressDialog = new ProgressDialog(getActivity());
    }
}
