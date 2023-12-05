package com.example.bookingcalender.Model;

import java.io.Serializable;

public class Account implements Serializable {
    String email;
    String password;
    String account;
    String accountId;

    public Account(){

    }

    public Account(String email, String password, String account, String accountId) {
        this.email = email;
        this.password = password;
        this.account = account;
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
