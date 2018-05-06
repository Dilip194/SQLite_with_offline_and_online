package com.example.dilip.uidemo.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/api/get-specialist")
    Call<List<ItemModel>> getItems(@Header("x-requested-with") String headerFirst, @Header("x-api-key") String header,@Header("authorization") String hearderThird);
}
