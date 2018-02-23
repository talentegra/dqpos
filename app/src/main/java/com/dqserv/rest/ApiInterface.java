package com.dqserv.rest;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {

    @FormUrlEncoded
    @POST("get_products")
    Call<ProductObject> getProducts(@Field("auth_token") String authToken);

    @FormUrlEncoded
    @POST("get_tables")
    Call<TableObject> getTables(@Field("auth_token") String authToken);

    @FormUrlEncoded
    @POST("get_categories")
    Call<CategoryObject> getCategories(@Field("auth_token") String authToken);

    @FormUrlEncoded
    @POST("post_orders")
    Call<ResponseOrderObject> addOrders(@Field("auth_token") String authToken,
                                @Field("table_id") String sTableId,
                                @Field("data") JSONArray data);

    @FormUrlEncoded
    @POST("get_orders")
    Call<BillObject> getOrders(@Field("auth_token") String authToken);

    @FormUrlEncoded
    @POST("get_order_by_id")
    Call<PaymentObject> getOrderById(@Field("auth_token") String authToken,
                               @Field("order_sale_id") String orderID);
}