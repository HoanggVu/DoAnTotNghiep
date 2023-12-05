package com.example.bookingcalender.Model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("to") //  "to" changed to token
    private String token;

    @SerializedName("data")
    private NotificationModel notification;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NotificationModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationModel notification) {
        this.notification = notification;
    }
}
