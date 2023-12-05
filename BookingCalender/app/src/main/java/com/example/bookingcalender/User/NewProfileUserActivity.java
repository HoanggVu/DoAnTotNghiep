package com.example.bookingcalender.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Doctor.DoctorActivity;
import com.example.bookingcalender.Doctor.NewProfileDoctorActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.BenhNhan;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NewProfileUserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 100;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Uri image;
    Bitmap bitmap;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Button btnXacNhan;
    ImageView imgUpdateProfileUser;
    ImageButton imgBtnFindImage;
    EditText edtUpdateUserHoTen, edtUpdateUserNgaySinh, edtUpdateUserGioiTinh, edtUpdateUserSDT, edtUpdateUserDiaChi;
    String imageUri = " ";
    String accountIdUser = " ";
    String hoTen = " ";
    String ngaySinh = " ";
    String gioiTinh = " ";
    String dienThoai = " ";
    String diaChi = " ";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_user);

        initUi();
        initListener();
        getAccountId();
    }
    private void getAccountId() {
        FirebaseUser mDoctor = FirebaseAuth.getInstance().getCurrentUser();
        if (mDoctor == null){
            return;
        }
        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcIdGetId = documentSnapshot.getId();
                    String UIdGetId = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(UIdGetId, documentSnapshot.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcIdGetId);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                accountIdUser = documentSnapshot.getString("accountId");
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewProfileUserActivity.this, "Lấy Id Tài Khoản Thất Bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initUi() {
        imgUpdateProfileUser = findViewById(R.id.img_view_new_profile_user);
        imgBtnFindImage = findViewById(R.id.img_search_new_image_btn);
        edtUpdateUserHoTen = findViewById(R.id.edt_profile_user_new_ho_ten);
        edtUpdateUserNgaySinh = findViewById(R.id.edt_profile_user_new_ngay_sinh);
        edtUpdateUserGioiTinh = findViewById(R.id.edt_profile_user_new_gioi_tinh);
        edtUpdateUserSDT = findViewById(R.id.edt_profile_user_new_sdt);
        edtUpdateUserDiaChi = findViewById(R.id.edt_profile_user_new_dia_chi);
        btnXacNhan = findViewById(R.id.btn_accept_new_profile_user);
        progressDialog = new ProgressDialog(this);

    }
    private void initListener() {
        imgBtnFindImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFile();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewUserInfo();
            }
        });
    }

    private void OpenFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Tìm kiếm hình ảnh!"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                imgUpdateProfileUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setNewUserInfo(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null){
            return;
        }

        progressDialog.show();
        if (imgUpdateProfileUser != null){
            final String randomKey = UUID.randomUUID().toString();
            StorageReference reference = storageRef.child("Images/" + randomKey);
            reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri.toString();
                            hoTen = String.valueOf(edtUpdateUserHoTen.getText());
                            ngaySinh = String.valueOf(edtUpdateUserNgaySinh.getText());
                            gioiTinh = String.valueOf(edtUpdateUserGioiTinh.getText());
                            dienThoai = String.valueOf(edtUpdateUserSDT.getText());
                            diaChi = String.valueOf(edtUpdateUserDiaChi.getText());

                            BenhNhan benhNhan = new BenhNhan(accountIdUser, imageUri, hoTen, ngaySinh, gioiTinh, dienThoai, diaChi);

                            firebaseFirestore.collection("BenhNhan").add(benhNhan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressDialog.dismiss();
                                    Toast.makeText(NewProfileUserActivity.this, "Cập Nhập Thông Tin Thành Công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(NewProfileUserActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(NewProfileUserActivity.this, "Cập Nhập Thông Tin Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Loading: " + (int) progressPercent + "%");
                }
            });
        }
    }
}