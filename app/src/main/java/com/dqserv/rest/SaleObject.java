package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */
public class SaleObject {

    @SerializedName("sale_id")
    private String saleId;

    @SerializedName("delete_order")
    private String deleteOrder;

    @SerializedName("status")
    private String status;

    @SerializedName("status_message")
    private String statusMessage;


    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getDeleteOrder() {
        return deleteOrder;
    }

    public void setDeleteOrder(String deleteOrder) {
        this.deleteOrder = deleteOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
