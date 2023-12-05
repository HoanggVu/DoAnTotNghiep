package com.example.bookingcalender.Model;

import java.io.Serializable;

public class DichVu implements Serializable {

    String khoa;
    String dichVu;

    public  DichVu(){}

    public DichVu(String khoa, String dichVu) {
        this.khoa = khoa;
        this.dichVu = dichVu;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getDichVu() {
        return dichVu;
    }

    public void setDichVu(String dichVu) {
        this.dichVu = dichVu;
    }
}
