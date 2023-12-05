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

import com.example.bookingcalender.Adapter.SearchDichVuRecyclerAdapter;
import com.example.bookingcalender.Adapter.SearchDoctorRecyclerAdapter;
import com.example.bookingcalender.Interface.IClickItemDichVuListener;
import com.example.bookingcalender.Interface.IClickItemDoctorListener;
import com.example.bookingcalender.Model.DichVu;
import com.example.bookingcalender.Model.Doctor;
import com.example.bookingcalender.Model.LichLamViec;
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

public class SearchDoctorActivity extends AppCompatActivity {
    ImageView imgBtnBack;
    RecyclerView recyclerView;
    SearchDoctorRecyclerAdapter adapter;
    ArrayList<LichLamViec> lichLamViecArrayList;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("LichLamViec");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        initUi();
        initListener();
        setDichVu();
    }
    private void initUi() {
        imgBtnBack = findViewById(R.id.img_search_doctor_back_user);
        recyclerView = findViewById(R.id.search_doctor_recycler_view);
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
        lichLamViecArrayList = new ArrayList<>();
        adapter = new SearchDoctorRecyclerAdapter(this, lichLamViecArrayList, new IClickItemDoctorListener() {
            @Override
            public void onClickItemDoctor(LichLamViec lichLamViec) {
                onClickGoToDetail(lichLamViec);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("LichLamViec").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            LichLamViec lichLamViec = documentSnapshot.toObject(LichLamViec.class);
                            lichLamViecArrayList.add(lichLamViec);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void onClickGoToDetail(LichLamViec lichLamViec) {
        Intent intent = new Intent(this, DatLichBacSiActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("doctor", lichLamViec);
        intent.putExtras(bundle1);
        startActivity(intent);
    }

    /*private void setupSearchRecyclerView(String searchTerm) {
        Query query = collectionReference.whereGreaterThanOrEqualTo("khoa", searchTerm);

        FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
                .setQuery(query, Doctor.class).build();
        adapter = new SearchDoctorRecyclerAdapter(options, this, new IClickItemDoctorListener() {
            @Override
            public void onClickItemDoctor(Doctor doctor) {
                onClickGoToBookingCalender(doctor);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }
    private void onClickGoToBookingCalender(Doctor doctor){
        Intent intent = new Intent(this, DatLichBacSiActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("doctor", doctor);
        intent.putExtras(bundle1);
        startActivity(intent);
    }*/
}