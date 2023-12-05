package com.example.bookingcalender.User;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingcalender.Adapter.DatLichRecyclerAdapter;
import com.example.bookingcalender.Adapter.UserDatLichRecyclerAdapter;
import com.example.bookingcalender.Admin.DatLichActivity;
import com.example.bookingcalender.Admin.InfoDatLichActivity;
import com.example.bookingcalender.Interface.IClickItemDatLichListener;
import com.example.bookingcalender.Model.DatLich;
import com.example.bookingcalender.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatLichFragment extends Fragment {
    View mView;
    TextView tvChonNgay;
    RecyclerView recyclerView;
    ArrayList<DatLich> datLichArrayList;
    UserDatLichRecyclerAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String accountId = " ";
    String dateTime = " ";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dat_lich, container, false);

        initUi();
        initListener();
        setDatLich();

        return mView;
    }

    private void initListener() {
        tvChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }
    private void setDate(){
        final Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                tvChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
                dateTime = String.valueOf(simpleDateFormat.format(calendar.getTime()));
                setDatLichTheoNgay();
            }
        }, year, month, date);
        datePickerDialog.show();
    }
    private void setDatLichTheoNgay() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        accountId = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        datLichArrayList = new ArrayList<>();
        adapter = new UserDatLichRecyclerAdapter(datLichArrayList, getContext(), new IClickItemDatLichListener() {
            @Override
            public void onClickItemDatLich(DatLich datLich) {
                onClickGoToDetail(datLich);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DatLich").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountId.equals(documentSnapshot.getString("accountIdUser"))){
                                if (dateTime.equals(documentSnapshot.getString("ngayDatKham"))){
                                    DatLich datLich = documentSnapshot.toObject(DatLich.class);
                                    datLichArrayList.add(datLich);
                                    progressDialog.dismiss();
                                }
                                progressDialog.dismiss();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void setDatLich() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        progressDialog.show();
        accountId = FirebaseAuth.getInstance().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        datLichArrayList = new ArrayList<>();
        adapter = new UserDatLichRecyclerAdapter(datLichArrayList, getContext(), new IClickItemDatLichListener() {
            @Override
            public void onClickItemDatLich(DatLich datLich) {
                onClickGoToDetail(datLich);
            }
        });
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("DatLich").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot documentSnapshot : list){
                            if (accountId.equals(documentSnapshot.getString("accountIdUser"))){
                                DatLich datLich = documentSnapshot.toObject(DatLich.class);
                                datLichArrayList.add(datLich);
                                progressDialog.dismiss();
                            }
                            progressDialog.dismiss();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void onClickGoToDetail(DatLich datLich){
        Intent intent = new Intent(getActivity(), InfoUserDatLichActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info_userdatlich", datLich);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void initUi() {
        recyclerView = mView.findViewById(R.id.user_dat_lich_recycler_view);
        tvChonNgay = mView.findViewById(R.id.tv_ngay_dat_lich);
        progressDialog = new ProgressDialog(getContext());
    }

}