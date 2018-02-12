package com.dqserv.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("get_products")
    Call<ProductObject> getProducts(@Field("auth_token") String authToken);
}