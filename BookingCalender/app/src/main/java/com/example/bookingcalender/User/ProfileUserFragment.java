package com.example.bookingcalender.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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

import java.util.Objects;

public class ProfileUserFragment extends Fragment {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    View view;
    ImageView imgAvtProfileUser;
    TextView tvUserHoTen, tvUserNgaySinh, tvUserGioiTinh, tvUserSDT, tvUserEmail, tvUserDiaChi;
    Button btnUpdateProfileUser;
    Button btnNewProfileUser;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        initUi();
        setUserInfo();
        initListener();

        return view;
    }

    private void initUi() {
        imgAvtProfileUser = view.findViewById(R.id.img_view_profile_user);
        tvUserEmail = view.findViewById(R.id.tv_profile_user_email);
        tvUserHoTen = view.findViewById(R.id.tv_profile_user_ho_ten);
        tvUserNgaySinh = view.findViewById(R.id.tv_profile_user_ngay_sinh);
        tvUserGioiTinh = view.findViewById(R.id.tv_profile_user_gioi_tinh);
        tvUserSDT = view.findViewById(R.id.tv_profile_user_sdt);
        tvUserDiaChi = view.findViewById(R.id.tv_profile_user_dia_chi);
        btnUpdateProfileUser = view.findViewById(R.id.btn_update_profile_user);
        btnNewProfileUser = view.findViewById(R.id.btn_new_profile_user);
        progressDialog = new ProgressDialog(getContext());
    }
    private void initListener(){
        btnUpdateProfileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfileUserActivity.class);
                startActivity(intent);
            }
        });
        btnNewProfileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = tvUserHoTen.getText().toString();
                if (TextUtils.isEmpty(check)){
                    Intent intent = new Intent(getActivity(), NewProfileUserActivity.class);
                    startActivity(intent);
                    return;
                }
                Toast.makeText(getActivity(), "Bạn Đã Có Thông Tin, Nếu Có Sửa Đổi Vui Lòng Cập Nhật!", Toast.LENGTH_SHORT).show();
                return;

            }
        });
    }
    private void setUserInfo() {
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            tvUserEmail.setText(user.getEmail());
            firebaseFirestore.collection("BenhNhan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String dcId = documentSnapshot.getId();
                        String UId = FirebaseAuth.getInstance().getUid();
                        if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                            DocumentReference documentReference = firebaseFirestore.collection("BenhNhan").document(dcId);
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String img = documentSnapshot.getString("avatar").toString();
                                    Glide.with(getActivity()).load(img).into(imgAvtProfileUser);
                                    tvUserHoTen.setText(documentSnapshot.getString("hoTen"));
                                    tvUserNgaySinh.setText(documentSnapshot.getString("ngaySinh"));
                                    tvUserGioiTinh.setText(documentSnapshot.getString("gioiTinh"));
                                    tvUserSDT.setText(documentSnapshot.getString("dienThoai"));
                                    tvUserDiaChi.setText(documentSnapshot.getString("diaChi"));
                                    progressDialog.dismiss();
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

}
