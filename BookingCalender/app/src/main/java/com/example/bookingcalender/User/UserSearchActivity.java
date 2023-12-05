package com.example.bookingcalender.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bookingcalender.Adapter.AccountDoctorRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserSearchRecyclerAdapter;
import com.example.bookingcalender.Doctor.BenhAnActivity;
import com.example.bookingcalender.Doctor.InfoBenhAnActivity;
import com.example.bookingcalender.Interface.IClickItemUserSearchListener;
import com.example.bookingcalender.Model.Account;
import com.example.bookingcalender.Model.BacSi;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends AppCompatActivity {
    EditText edtInputSearch;
    ImageView imgBtnSearch, imgBtnBack;
    RecyclerView recyclerView;
    ArrayList<BacSi> bacSiArrayList;
    UserSearchRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        imgBtnSearch = findViewById(R.id.img_searchBtn);
        imgBtnBack = findViewById(R.id.img_search_back_user);
        recyclerView = findViewById(R.id.user_search_recycler_view);

        setInfoBacSi();
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setInfoBacSi(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bacSiArrayList = new ArrayList<>();
        adapter = new UserSearchRecyclerAdapter(bacSiArrayList, this, new IClickItemUserSearchListener() {
            @Override
            public void onClickItemUserSearch(BacSi bacSi) {
                onClickGoToDetail(bacSi);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("BacSi").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            BacSi bacSi = documentSnapshot.toObject(BacSi.class);
                            bacSiArrayList.add(bacSi);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onClickGoToDetail(BacSi bacSi) {
        Intent intent = new Intent(UserSearchActivity.this, InfoBacSiActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_bacSi", bacSi);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}