package com.example.bookingcalender.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookingcalender.R;

public class HomeUserFragment extends Fragment {
    View view;
    Button btnDatLich;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_user, container, false);

        initUi();
        initListener();
        return view;
    }

    private void initUi() {
        btnDatLich = view.findViewById(R.id.btn_phu_khoa_dat_lich);
    }
    private void initListener(){
        btnDatLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DatLichDichVuActivity.class);
                startActivity(intent);
            }
        });
    }

}
