package com.example.bookingcalender.Model;

import java.io.Serializable;

public class LichLamViec implements Serializable {
    String accountIdDoctor;
    String avatarDoctor;
    String nameDoctor;
    String khoaDoctor;
    String phongLV;
    String ngayLV;
    String tGLVTu;
    String tGLVDen;

    public LichLamViec(){}

    public LichLamViec(String accountIdDoctor, String avatarDoctor, String nameDoctor, String khoaDoctor, String phongLV, String ngayLV, String tGLVTu, String tGLVDen) {
        this.accountIdDoctor = accountIdDoctor;
        this.avatarDoctor = avatarDoctor;
        this.nameDoctor = nameDoctor;
        this.khoaDoctor = khoaDoctor;
        this.phongLV = phongLV;
        this.ngayLV = ngayLV;
        this.tGLVTu = tGLVTu;
        this.tGLVDen = tGLVDen;
    }

    public String getAccountIdDoctor() {
        return accountIdDoctor;
    }

    public void setAccountIdDoctor(String accountIdDoctor) {
        this.accountIdDoctor = accountIdDoctor;
    }

    public String getAvatarDoctor() {
        return avatarDoctor;
    }

    public void setAvatarDoctor(String avatarDoctor) {
        this.avatarDoctor = avatarDoctor;
    }

    public String getNameDoctor() {
        return nameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        this.nameDoctor = nameDoctor;
    }

    public String getKhoaDoctor() {
        return khoaDoctor;
    }

    public void setKhoaDoctor(String khoaDoctor) {
        this.khoaDoctor = khoaDoctor;
    }

    public String getPhongLV() {
        return phongLV;
    }

    public void setPhongLV(String phongLV) {
        this.phongLV = phongLV;
    }

    public String getNgayLV() {
        return ngayLV;
    }

    public void setNgayLV(String ngayLV) {
        this.ngayLV = ngayLV;
    }

    public String gettGLVTu() {
        return tGLVTu;
    }

    public void settGLVTu(String tGLVTu) {
        this.tGLVTu = tGLVTu;
    }

    public String gettGLVDen() {
        return tGLVDen;
    }

    public void settGLVDen(String tGLVDen) {
        this.tGLVDen = tGLVDen;
    }
}
