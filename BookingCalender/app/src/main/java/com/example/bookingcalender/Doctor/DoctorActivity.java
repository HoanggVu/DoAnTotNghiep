package com.example.bookingcalender.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Authentication.LoginActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.TokenUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoctorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int FRAGMENT_HOME_DOCTOR = 0;
    private static final int FRAGMENT_PROFILE_DOCTOR = 1;
    private static final int FRAGMENT_CHANGE_PASSWORD_DOCTOR = 2;

    private int mCurrentFragment = FRAGMENT_HOME_DOCTOR;
    private final ProfileDoctorFragment mProfileDoctorFragment = new ProfileDoctorFragment();
    private final HomeDoctorFragment mHomeDoctorFragment = new HomeDoctorFragment();

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ImageView imgAvatar;
    TextView tvName, tvEmail;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        progressDialog = new ProgressDialog(this);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avt);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);

        Toolbar toolbar = findViewById(R.id.toolbar_doctor);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(mHomeDoctorFragment);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        showDoctorInfo();

    }
    public void showDoctorInfo(){
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor != null){
            String email = doctor.getEmail();
            tvEmail.setText(email);
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
                                        Glide.with(DoctorActivity.this).load(img).into(imgAvatar);
                                        tvName.setText(documentSnapshot.getString("hoTen"));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(DoctorActivity.this, "Chưa Cập Nhật Thông Tin!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DoctorActivity.this, "Không Tìm Thấy Dữ Liệu!", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            return;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toolbar toolbar = findViewById(R.id.toolbar_doctor);
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (mCurrentFragment != FRAGMENT_HOME_DOCTOR){
                replaceFragment(mHomeDoctorFragment);
                mCurrentFragment = FRAGMENT_HOME_DOCTOR;
                toolbar.setTitle("Phòng Khám Đa Khoa");
            }
        } else if (id == R.id.nav_profile) {
            if (mCurrentFragment != FRAGMENT_PROFILE_DOCTOR){
                replaceFragment(mProfileDoctorFragment);
                mCurrentFragment = FRAGMENT_PROFILE_DOCTOR;
                toolbar.setTitle("Thông Tin Cá Nhân");
            }
        } else if (id == R.id.nav_change_password) {
            if (mCurrentFragment != FRAGMENT_CHANGE_PASSWORD_DOCTOR){
                replaceFragment(new ChangePasswordDoctorFragment());
                mCurrentFragment = FRAGMENT_CHANGE_PASSWORD_DOCTOR;
                toolbar.setTitle("Đổi Mật Khẩu");
            }
        }else if (id == R.id.nav_logout) {
            deleteToken();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void deleteToken() {
        firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                    String dcId1 = documentSnapshot1.getId();
                    String accountId = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(accountId, documentSnapshot1.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId1);
                        Map<String, Object> deletes = new HashMap<>();
                        deletes.put("tokenDoctor", FieldValue.delete());
                        documentReference.update(deletes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(DoctorActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FirebaseUser doctor = FirebaseAuth.getInstance().getCurrentUser();
        if (doctor == null){
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}