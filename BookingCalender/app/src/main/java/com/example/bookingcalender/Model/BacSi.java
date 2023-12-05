package com.example.bookingcalender.Model;

import java.io.Serializable;

public class BacSi implements Serializable {
    String accountId;
    String avatar;
    String HoTen;
    String ngaySinh;
    String gioiTinh;
    String dienThoai;
    String chuyenKhoa;
    String chucVu;
    String bangCap;
    String chuyenMon;

    public BacSi(){}

    public BacSi(String accountId, String avatar, String hoTen, String ngaySinh, String gioiTinh, String dienThoai, String chuyenKhoa, String chucVu, String bangCap, String chuyenMon) {
        this.accountId = accountId;
        this.avatar = avatar;
        this.HoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.dienThoai = dienThoai;
        this.chuyenKhoa = chuyenKhoa;
        this.chucVu = chucVu;
        this.bangCap = bangCap;
        this.chuyenMon = chuyenMon;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getChuyenKhoa() {
        return chuyenKhoa;
    }

    public void setChuyenKhoa(String chuyenKhoa) {
        this.chuyenKhoa = chuyenKhoa;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getBangCap() {
        return bangCap;
    }

    public void setBangCap(String bangCap) {
        this.bangCap = bangCap;
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }
}
