package com.example.bookingcalender.User;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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

public class UpdateProfileUserActivity extends AppCompatActivity {
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
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_user);

        initUi();
        initListener();
        setInfoDoctor();
    }

    private void initUi() {
        imgUpdateProfileUser = findViewById(R.id.img_view_update_profile_user);
        imgBtnFindImage = findViewById(R.id.img_search_image_btn);
        edtUpdateUserHoTen = findViewById(R.id.edt_profile_user_ho_ten);
        edtUpdateUserNgaySinh = findViewById(R.id.edt_profile_user_ngay_sinh);
        edtUpdateUserGioiTinh = findViewById(R.id.edt_profile_user_gioi_tinh);
        edtUpdateUserSDT = findViewById(R.id.edt_profile_user_sdt);
        edtUpdateUserDiaChi = findViewById(R.id.edt_profile_user_dia_chi);
        btnXacNhan = findViewById(R.id.btn_accept_update_profile_user);
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
                            String imageUri = uri.toString();

                            Map<String, Object> user = new HashMap<>();
                            user.put("avatar", imageUri);
                            user.put("hoTen", String.valueOf(edtUpdateUserHoTen.getText()));
                            user.put("ngaySinh", String.valueOf(edtUpdateUserNgaySinh.getText()));
                            user.put("gioiTinh", String.valueOf(edtUpdateUserGioiTinh.getText()));
                            user.put("dienThoai", String.valueOf(edtUpdateUserSDT.getText()));
                            user.put("diaChi", String.valueOf(edtUpdateUserDiaChi.getText()));

                            firebaseFirestore.collection("BenhNhan").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        String dcId = documentSnapshot.getId();
                                        String UId = FirebaseAuth.getInstance().getUid();
                                        if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                                            DocumentReference documentReference = firebaseFirestore.collection("BenhNhan").document(dcId);
                                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UpdateProfileUserActivity.this, "Cập Nhập Thông Tin Thành Công", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(UpdateProfileUserActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateProfileUserActivity.this, "Cập Nhập Thông Tin Thất Bại", Toast.LENGTH_SHORT).show();
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

    private void setInfoDoctor(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null){
            return;
        }
        progressDialog.show();
        Uri picUrl = mUser.getPhotoUrl();
        Glide.with(this).load(picUrl).error(R.drawable.avt_default).into(imgUpdateProfileUser);
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
                                Glide.with(UpdateProfileUserActivity.this).load(img).into(imgUpdateProfileUser);
                                edtUpdateUserHoTen.setText(documentSnapshot.getString("hoTen"));
                                edtUpdateUserNgaySinh.setText(documentSnapshot.getString("ngaySinh"));
                                edtUpdateUserGioiTinh.setText(documentSnapshot.getString("gioiTinh"));
                                edtUpdateUserSDT.setText(documentSnapshot.getString("dienThoai"));
                                edtUpdateUserDiaChi.setText(documentSnapshot.getString("diaChi"));
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        });

    }
}