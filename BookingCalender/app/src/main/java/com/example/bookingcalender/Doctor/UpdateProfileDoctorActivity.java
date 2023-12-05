package com.example.bookingcalender.Doctor;

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
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.Model.BacSi;
import com.example.bookingcalender.R;
import com.example.bookingcalender.User.UpdateProfileUserActivity;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UpdateProfileDoctorActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Uri image;
    Bitmap bitmap;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ImageView imgUpdateProfileDoctor;
    ImageButton imageBtnFindImage;
    Button btnXacNhan;
    EditText edtUpdateHoTen, edtUpdateNgaySinh, edtUpdateGioiTinh, edtUpdateSDT, edtUpdateChuyenKhoa, edtUpdateChucVu, edtUpdateBangCap, edtUpdateChuyenMon;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_doctor);

        initUi();
        initListener();
        setInfoDoctor();
    }

    private void initUi(){
        imgUpdateProfileDoctor = findViewById(R.id.img_view_update_profile_doctor);
        imageBtnFindImage = findViewById(R.id.img_search_image_doctor_btn);
        edtUpdateHoTen = findViewById(R.id.edt_profile_doctor_ho_ten);
        edtUpdateNgaySinh = findViewById(R.id.edt_profile_doctor_ngay_sinh);
        edtUpdateGioiTinh = findViewById(R.id.edt_profile_doctor_gioi_tinh);
        edtUpdateSDT = findViewById(R.id.edt_profile_doctor_sdt);
        edtUpdateChuyenKhoa = findViewById(R.id.edt_profile_doctor_chuyen_khoa);
        edtUpdateChucVu = findViewById(R.id.edt_profile_doctor_chuc_vu);
        edtUpdateBangCap = findViewById(R.id.edt_profile_doctor_bang_cap);
        edtUpdateChuyenMon = findViewById(R.id.edt_profile_doctor_chuyen_mon);
        btnXacNhan = findViewById(R.id.btn_accept_update_profile_doctor);
        progressDialog = new ProgressDialog(this);
    }
    private void initListener(){
        imageBtnFindImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFile();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewDoctorInfo();
            }
        });
    }

    private void OpenFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Tìm Kiếm Hình Ảnh"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                imgUpdateProfileDoctor.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setNewDoctorInfo(){
        FirebaseUser mDoctor = FirebaseAuth.getInstance().getCurrentUser();
        if (mDoctor == null){
            return;
        }
        progressDialog.show();
        if (imgUpdateProfileDoctor != null){
            final String randomKey = UUID.randomUUID().toString();
            StorageReference reference = storageRef.child("Images/" + randomKey);
            reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            Map<String, Object> mBacSi = new HashMap<>();
                            mBacSi.put("avatar", imageUri);
                            mBacSi.put("hoTen", String.valueOf(edtUpdateHoTen.getText()));
                            mBacSi.put("ngaySinh", String.valueOf(edtUpdateNgaySinh.getText()));
                            mBacSi.put("gioiTinh", String.valueOf(edtUpdateGioiTinh.getText()));
                            mBacSi.put("dienThoai", String.valueOf(edtUpdateSDT.getText()));
                            mBacSi.put("chuyenKhoa", String.valueOf(edtUpdateChuyenKhoa.getText()));
                            mBacSi.put("chucVu", String.valueOf(edtUpdateChucVu.getText()));
                            mBacSi.put("bangCap", String.valueOf(edtUpdateBangCap.getText()));
                            mBacSi.put("chuyenMon", String.valueOf(edtUpdateChuyenMon.getText()));

                            firebaseFirestore.collection("BacSi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        String dcId = documentSnapshot.getId();
                                        String UId = FirebaseAuth.getInstance().getUid();
                                        if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                                            DocumentReference documentReference = firebaseFirestore.collection("BacSi").document(dcId);
                                            documentReference.update(mBacSi).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(UpdateProfileDoctorActivity.this, "Cập Nhật Thông Tin Thành Công!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(UpdateProfileDoctorActivity.this, DoctorActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                }
                                            });
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(UpdateProfileDoctorActivity.this, "Cập Nhật Thông Tin Thất Bại!", Toast.LENGTH_SHORT).show();
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
        FirebaseUser mDoctor = FirebaseAuth.getInstance().getCurrentUser();
        if (mDoctor == null){
            return;
        }
        progressDialog.show();
        Uri picUrl = mDoctor.getPhotoUrl();
        Glide.with(this).load(picUrl).error(R.drawable.avt_default).into(imgUpdateProfileDoctor);
        firebaseFirestore.collection("BacSi").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String dcIdBacSi = documentSnapshot.getId();
                    String UIdBacSi = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(UIdBacSi, documentSnapshot.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("BacSi").document(dcIdBacSi);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String img = documentSnapshot.getString("avatar").toString();
                                Glide.with(UpdateProfileDoctorActivity.this).load(img).into(imgUpdateProfileDoctor);
                                edtUpdateHoTen.setText(documentSnapshot.getString("hoTen"));
                                edtUpdateNgaySinh.setText(documentSnapshot.getString("ngaySinh"));
                                edtUpdateGioiTinh.setText(documentSnapshot.getString("gioiTinh"));
                                edtUpdateSDT.setText(documentSnapshot.getString("dienThoai"));
                                edtUpdateChuyenKhoa.setText(documentSnapshot.getString("chuyenKhoa"));
                                edtUpdateChucVu.setText(documentSnapshot.getString("chucVu"));
                                edtUpdateBangCap.setText(documentSnapshot.getString("bangCap"));
                                edtUpdateChuyenMon.setText(documentSnapshot.getString("chuyenMon"));
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        });

    }
}