package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2/12/2018.
 */

public class ResponseOrderObject {


    @SerializedName("table_id")
    private String tableId;

    @SerializedName("data")
    private String data;

    @SerializedName("status")
    private String status;

    @SerializedName("status_message")
    private String statusMessage;


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
