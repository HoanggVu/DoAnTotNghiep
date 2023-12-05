package com.example.bookingcalender.API;

import com.example.bookingcalender.Model.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers({"Authorization: key=AAAAvL3BqI4:APA91bEvVyTWtFfFvceLwDApNrfh5OKl7s6FsN0iNo29ERE7fimYY6Hg_mQDQQeT3B_0mKJNIxZIW3KK9PE4Y_lzPHWHNayQgLq6-hnOCq_Uht2mIu84j1Zvd_C3mV79NUQZEHI1pmu-", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body Notification notification);

//    ApiService apiService = new Retrofit.Builder()
//            .baseUrl("https://fcm.googleapis.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build().create(ApiService.class);
//
//    @POST("/fcm/send")
//    Call<Notification> sendNotification(@Body Notification notification);
}
