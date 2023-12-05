package com.example.bookingcalender.Model;

import java.io.Serializable;

public class Doctor implements Serializable {

    String name;
    String khoa;
    String imageUri;
    String account;
    String AccountId;
    String phong;
    String thoiGianTu;
    String thoiGianDen;
    String ngay;

    public  Doctor(){

    }

    public Doctor(String name, String khoa, String imageUri, String account, String accountId, String phong, String thoiGianTu, String thoiGianDen, String ngay) {
        this.name = name;
        this.khoa = khoa;
        this.imageUri = imageUri;
        this.account = account;
        AccountId = accountId;
        this.phong = phong;
        this.thoiGianTu = thoiGianTu;
        this.thoiGianDen = thoiGianDen;
        this.ngay = ngay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getPhong() {
        return phong;
    }

    public void setPhong(String phong) {
        this.phong = phong;
    }

    public String getThoiGianTu() {
        return thoiGianTu;
    }

    public void setThoiGianTu(String thoiGianTu) {
        this.thoiGianTu = thoiGianTu;
    }

    public String getThoiGianDen() {
        return thoiGianDen;
    }

    public void setThoiGianDen(String thoiGianDen) {
        this.thoiGianDen = thoiGianDen;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}
