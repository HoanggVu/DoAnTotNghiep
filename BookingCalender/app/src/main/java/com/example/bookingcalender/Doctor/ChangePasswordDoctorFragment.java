package com.example.bookingcalender.Doctor;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Authentication.LoginActivity;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChangePasswordDoctorFragment extends Fragment {

    View mView;
    TextInputEditText tipedtMatKhauCu, tipedtMatKhauMoi, tipedtXacNhanMatKhau;
    Button btnXacNhan;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String oldPass = " ";
    Map<String, Object> account = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password_doctor, container, false);

        initUi();
        initListener();
        anhienpassword();
        xacthucngdung();

        return mView;
    }
    private void anhienpassword() {
        tipedtMatKhauCu.setTypeface(Typeface.DEFAULT);
        tipedtMatKhauCu.setTransformationMethod(new PasswordTransformationMethod());

        tipedtMatKhauMoi.setTypeface(Typeface.DEFAULT);
        tipedtMatKhauMoi.setTransformationMethod(new PasswordTransformationMethod());

        tipedtXacNhanMatKhau.setTypeface(Typeface.DEFAULT);
        tipedtXacNhanMatKhau.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void xacthucngdung() {
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                String email = documentSnapshot.getString("email");
                                String password = documentSnapshot.getString("password");
                                assert email != null;
                                assert password != null;
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(email, password);
                                doctor.reauthenticate(credential);
                            }
                        });
                    }
                }
            }
        });
    }

    private void initUi() {
        tipedtMatKhauCu = mView.findViewById(R.id.mat_khau_cu);
        tipedtMatKhauMoi = mView.findViewById(R.id.mat_khau_moi);
        tipedtXacNhanMatKhau = mView.findViewById(R.id.xac_nhan_mat_khau_moi);
        btnXacNhan = mView.findViewById(R.id.btn_xac_nhan_doi_mat_khau);
    }

    private void initListener() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPass();
            }
        });
    }

    private void checkPass() {


        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        oldPass = String.valueOf(tipedtMatKhauCu.getText());
        String newPass = String.valueOf(tipedtMatKhauMoi.getText());
        String rpNewPass = String.valueOf(tipedtXacNhanMatKhau.getText());


        account.put("password", newPass);

        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                if (oldPass.equals(documentSnapshot.getString("password"))) {
                                    if (newPass.length() >= 6) {
                                        if (newPass.equals(rpNewPass)) {
                                            doctor.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        updatePassword();
                                                        Toast.makeText(getContext(), "Đổi Mật Khẩu Thành Công", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Đổi Mật Khẩu Thất Bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getContext(), "Mật Khẩu Mới Không Trùng Khớp!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Mật Khẩu Mới Phải Trên 6 Ký Tự!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Mật Khẩu Cũ Không Đúng!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void updatePassword() {
        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcId = documentSnapshot.getId();
                    String UId = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId);
                        documentReference.update(account);
                    }
                }
            }
        });
    }
}
