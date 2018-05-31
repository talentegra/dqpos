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
    @POST("get_printers")
    Call<PrinterObject> getPrinters(@Field("auth_token") String authToken);

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

    @FormUrlEncoded
    @POST("post_sales")
    Call<SaleObject> addSalesData(@Field("auth_token") String authToken,
                                  @Field("order_sale_id") String orderID,
                                  @Field("data") JSONArray data);

    @FormUrlEncoded
    @POST("get_order_by_table")
    Call<TableObject> getOrderedTable(@Field("auth_token") String authToken);

    @FormUrlEncoded
    @POST("get_order_table_items")
    Call<OrderItemsObject> getOrderedTableItems(@Field("auth_token") String authToken,
                                                @Field("table_id") String tableID);

    @FormUrlEncoded
    @POST("post_order_completed")
    Call<PostOrderItemsObject> postOrderCompletedData(@Field("auth_token") String authToken,
                                                  @Field("table_id") String tableID,
                                                  @Field("data") JSONArray data);

}