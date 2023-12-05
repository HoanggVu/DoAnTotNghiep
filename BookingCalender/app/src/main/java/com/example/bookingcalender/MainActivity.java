package com.example.bookingcalender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookingcalender.Authentication.LoginActivity;
import com.example.bookingcalender.Doctor.ChangePasswordDoctorFragment;
import com.example.bookingcalender.Doctor.DoctorActivity;
import com.example.bookingcalender.FirebaseCloudMessaging.MyFirebaseMessagingService;
import com.example.bookingcalender.User.BenhAnFragment;
import com.example.bookingcalender.User.DatLichFragment;
import com.example.bookingcalender.User.HomeUserFragment;
import com.example.bookingcalender.User.InfoUserDatLichActivity;
import com.example.bookingcalender.User.PhieuThanhToanFragment;
import com.example.bookingcalender.User.ProfileUserFragment;
import com.example.bookingcalender.User.UserSearchActivity;
import com.example.bookingcalender.Util.TokenUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int FRAGMENT_HOME_USER = 0;
    private static final int FRAGMENT_PROFILE_USER = 1;
    private static final int FRAGMENT_CHANGE_PASSWORD_USER = 2;
    private static final int FRAGMENT_DAT_LICH = 3;
    private static final int FRAGMENT_BENH_AN = 4;
    private static final int FRAGMENT_PHIEU_THANH_TOAN = 5;

    private int mCurrentFragment = FRAGMENT_HOME_USER;
    private final ProfileUserFragment mProfileUserFragment = new ProfileUserFragment();
    private final HomeUserFragment mHomeUserFragment = new HomeUserFragment();
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ImageView imgAvatar;
    ImageButton imgBtnSearch;
    TextView tvName, tvEmail;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static final String TAG = MyFirebaseMessagingService.class.getName();
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        initListener();
        Toolbar toolbar = findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(mHomeUserFragment);
        navigationView.getMenu().findItem(R.id.nav_home_user).setChecked(true);

        showUserInfo();

        //getToken();


    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.e(TAG, token);
                    }
                });
    }

    private void initListener(){
        imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserSearchActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String email = user.getEmail();

            tvEmail.setText(email);
            progressDialog.show();
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
                                    Glide.with(MainActivity.this).load(img).into(imgAvatar);
                                    tvName.setText(documentSnapshot.getString("hoTen"));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Chưa Cập Nhật Thông Tin!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    progressDialog.dismiss();
                }
            });
        }else {
            return;
        }
    }

    private void initUi(){
        mDrawerLayout = findViewById(R.id.drawer_layout_user);
        navigationView = findViewById(R.id.nav_view_user);
        imgBtnSearch = findViewById(R.id.img_searchBtn);
        progressDialog= new ProgressDialog(this);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avt);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toolbar toolbar = findViewById(R.id.toolbar_user);
        int id = item.getItemId();
        if (id == R.id.nav_home_user) {
            if (mCurrentFragment != FRAGMENT_HOME_USER){
                replaceFragment(mHomeUserFragment);
                mCurrentFragment = FRAGMENT_HOME_USER;
                toolbar.setTitle("Phòng Khám Đa Khoa");
            }
        } else if (id == R.id.nav_profile_user) {
            if (mCurrentFragment != FRAGMENT_PROFILE_USER){
                replaceFragment(mProfileUserFragment);
                mCurrentFragment = FRAGMENT_PROFILE_USER;
                toolbar.setTitle("Thông Tin Cá Nhân");
            }
        } else if (id == R.id.nav_dat_lich_user) {
            if (mCurrentFragment != FRAGMENT_DAT_LICH){
                replaceFragment(new DatLichFragment());
                mCurrentFragment = FRAGMENT_DAT_LICH;
                toolbar.setTitle("Lịch Khám");
            }
        } else if (id == R.id.nav_benh_an_user) {
            if (mCurrentFragment != FRAGMENT_BENH_AN){
                replaceFragment(new BenhAnFragment());
                mCurrentFragment = FRAGMENT_BENH_AN;
                toolbar.setTitle("Bệnh Án");
            }
        } else if (id == R.id.nav_phieu_thanh_toan_user) {
            if (mCurrentFragment != FRAGMENT_PHIEU_THANH_TOAN){
                replaceFragment(new PhieuThanhToanFragment());
                mCurrentFragment = FRAGMENT_PHIEU_THANH_TOAN;
                toolbar.setTitle("Thanh Toán");
            }
        } else if (id == R.id.nav_change_password_user) {
            if (mCurrentFragment != FRAGMENT_CHANGE_PASSWORD_USER){
                replaceFragment(new ChangePasswordDoctorFragment());
                mCurrentFragment = FRAGMENT_CHANGE_PASSWORD_USER;
                toolbar.setTitle("Đổi Mật Khẩu");
            }
        }else if (id == R.id.nav_logout_user) {
            deleteToken();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void deleteToken() {
        progressDialog.show();
        firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                    String dcId1 = documentSnapshot1.getId();
                    String accountId = FirebaseAuth.getInstance().getUid();
                    if (Objects.equals(accountId, documentSnapshot1.getString("accountId"))){
                        DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId1);
                        Map<String, Object> deletes = new HashMap<>();
                        deletes.put("tokenUser", FieldValue.delete());
                        documentReference.update(deletes).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_user, fragment);
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