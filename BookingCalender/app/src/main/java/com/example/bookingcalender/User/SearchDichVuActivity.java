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

import com.example.bookingcalender.Adapter.DichVuRecyclerAdapter;
import com.example.bookingcalender.Adapter.DoctorBenhAnRecyclerAdapter;
import com.example.bookingcalender.Adapter.SearchDichVuRecyclerAdapter;
import com.example.bookingcalender.Interface.IClickItemBenhAnListener;
import com.example.bookingcalender.Interface.IClickItemDichVuListener;
import com.example.bookingcalender.Model.BenhAn;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchDichVuActivity extends AppCompatActivity {
    EditText edtInputSearch;
    ImageView imgBtnSearch, imgBtnBack;
    RecyclerView recyclerView;
    ArrayList<DichVu> dichVuArrayList;
    SearchDichVuRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("DichVu");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dich_vu);

        initUi();
        initListener();
        setDichVu();
    }

    private void initUi() {
        imgBtnBack = findViewById(R.id.img_search_dich_vu_back_user);
        recyclerView = findViewById(R.id.search_dich_vu_recycler_view);
    }

    private void initListener() {
        /*imgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = edtInputSearch.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length() < 3){
                    edtInputSearch.setError("Invalid Search");
                    return;
                }
                setupSearchRecyclerView(searchTerm);
            }
        });*/
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setDichVu() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dichVuArrayList = new ArrayList<>();
        adapter = new SearchDichVuRecyclerAdapter(this, dichVuArrayList, new IClickItemDichVuListener() {
                    @Override
                    public void onClickItemDichVu(DichVu dichVu) {
                        onClickGoToDetail(dichVu);
                    }
                });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DichVu").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            DichVu dichVu = documentSnapshot.toObject(DichVu.class);
                            dichVuArrayList.add(dichVu);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onClickGoToDetail(DichVu dichVu) {
        Intent intent = new Intent(this, DatLichDichVuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dichvukham", dichVu);
        intent.putExtras(bundle);
        startActivity(intent);
    }
/*
    private void setupSearchRecyclerView(String searchTerm) {
        Query query = collectionReference.whereGreaterThanOrEqualTo("dichvu", searchTerm);

        FirestoreRecyclerOptions<DichVu> options = new FirestoreRecyclerOptions.Builder<DichVu>()
                .setQuery(query, DichVu.class).build();
        adapter = new SearchDichVuRecyclerAdapter(options, this, new IClickItemDichVuListener() {
            @Override
            public void onClickItemDichVu(DichVu dichVu) {
                onClickGoToBookingCalender(dichVu);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }
    private void onClickGoToBookingCalender(DichVu dichVu){

    }*/
}