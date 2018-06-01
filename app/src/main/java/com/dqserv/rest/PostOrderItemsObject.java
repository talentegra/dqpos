package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class PostOrderItemsObject {


    @SerializedName("status")
    private String status;

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("del_status")
    private String delStatus;

    @SerializedName("order_sale_id")
    private String orderSaleId;

    @SerializedName("table_id")
    private String tableId;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getOrderSaleId() {
        return orderSaleId;
    }

    public void setOrderSaleId(String orderSaleId) {
        this.orderSaleId = orderSaleId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
}
