package com.example.bookingcalender.Model;

import java.io.Serializable;

public class BenhAn implements Serializable {
    String accountIdUser;
    String accountIdDoctor;
    String avatarUser;
    String nameUser;
    String dienThoaiUser;
    String trangThai;
    String dichVuKham;
    String trieuChung;
    String avatarBacSi;
    String khoaBacSi;
    String nameBacSi;
    String ngayLV;
    String tGLVTu;
    String tGLVDen;
    String kLTrieuChung;
    String chanDoan;
    String lieuPhap;
    String donThuoc;
    String tGKham;
    String ngayKham;

    public BenhAn(){}

    public BenhAn(String accountIdUser, String accountIdDoctor, String avatarUser, String nameUser,
                  String dienThoaiUser, String trangThai, String dichVuKham, String trieuChung,
                  String avatarBacSi, String khoaBacSi, String nameBacSi, String ngayLV, String tGLVTu,
                  String tGLVDen, String kLTrieuChung, String chanDoan, String lieuPhap, String donThuoc,
                  String tGKham, String ngayKham) {
        this.accountIdUser = accountIdUser;
        this.accountIdDoctor = accountIdDoctor;
        this.avatarUser = avatarUser;
        this.nameUser = nameUser;
        this.dienThoaiUser = dienThoaiUser;
        this.trangThai = trangThai;
        this.dichVuKham = dichVuKham;
        this.trieuChung = trieuChung;
        this.avatarBacSi = avatarBacSi;
        this.khoaBacSi = khoaBacSi;
        this.nameBacSi = nameBacSi;
        this.ngayLV = ngayLV;
        this.tGLVTu = tGLVTu;
        this.tGLVDen = tGLVDen;
        this.kLTrieuChung = kLTrieuChung;
        this.chanDoan = chanDoan;
        this.lieuPhap = lieuPhap;
        this.donThuoc = donThuoc;
        this.tGKham = tGKham;
        this.ngayKham = ngayKham;
    }

    public String getAccountIdUser() {
        return accountIdUser;
    }

    public void setAccountIdUser(String accountIdUser) {
        this.accountIdUser = accountIdUser;
    }

    public String getAccountIdDoctor() {
        return accountIdDoctor;
    }

    public void setAccountIdDoctor(String accountIdDoctor) {
        this.accountIdDoctor = accountIdDoctor;
    }

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getDienThoaiUser() {
        return dienThoaiUser;
    }

    public void setDienThoaiUser(String dienThoaiUser) {
        this.dienThoaiUser = dienThoaiUser;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getDichVuKham() {
        return dichVuKham;
    }

    public void setDichVuKham(String dichVuKham) {
        this.dichVuKham = dichVuKham;
    }

    public String getTrieuChung() {
        return trieuChung;
    }

    public void setTrieuChung(String trieuChung) {
        this.trieuChung = trieuChung;
    }

    public String getAvatarBacSi() {
        return avatarBacSi;
    }

    public void setAvatarBacSi(String avatarBacSi) {
        this.avatarBacSi = avatarBacSi;
    }

    public String getKhoaBacSi() {
        return khoaBacSi;
    }

    public void setKhoaBacSi(String khoaBacSi) {
        this.khoaBacSi = khoaBacSi;
    }

    public String getNameBacSi() {
        return nameBacSi;
    }

    public void setNameBacSi(String nameBacSi) {
        this.nameBacSi = nameBacSi;
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

    public String getkLTrieuChung() {
        return kLTrieuChung;
    }

    public void setkLTrieuChung(String kLTrieuChung) {
        this.kLTrieuChung = kLTrieuChung;
    }

    public String getChanDoan() {
        return chanDoan;
    }

    public void setChanDoan(String chanDoan) {
        this.chanDoan = chanDoan;
    }

    public String getLieuPhap() {
        return lieuPhap;
    }

    public void setLieuPhap(String lieuPhap) {
        this.lieuPhap = lieuPhap;
    }

    public String getDonThuoc() {
        return donThuoc;
    }

    public void setDonThuoc(String donThuoc) {
        this.donThuoc = donThuoc;
    }

    public String gettGKham() {
        return tGKham;
    }

    public void settGKham(String tGKham) {
        this.tGKham = tGKham;
    }

    public String getNgayKham() {
        return ngayKham;
    }

    public void setNgayKham(String ngayKham) {
        this.ngayKham = ngayKham;
    }
}
