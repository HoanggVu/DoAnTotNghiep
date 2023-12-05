package com.example.bookingcalender.Splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.bookingcalender.Authentication.LoginActivity;
import com.example.bookingcalender.Doctor.DoctorActivity;
import com.example.bookingcalender.MainActivity;
import com.example.bookingcalender.R;
import com.example.bookingcalender.Util.TokenUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String UId = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckLogin();
            }
        }, 2000);
    }

    private void CheckLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            firebaseFirestore.collection("Account").whereEqualTo("account", "1").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String dcId = documentSnapshot.getId();
                        if (Objects.equals(UId, documentSnapshot.getString("accountId"))){
                            DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId);
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Intent intent = new Intent(SplashActivity.this, DoctorActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });
                        }else {
                            firebaseFirestore.collection("Account").whereEqualTo("account", "0").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                                        String dcId1 = documentSnapshot1.getId();
                                        if (Objects.equals(UId, documentSnapshot1.getString("accountId"))){
                                            DocumentReference documentReference = firebaseFirestore.collection("Account").document(dcId1);
                                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });

        }else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

}