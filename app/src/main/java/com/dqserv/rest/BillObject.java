package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class BillObject {

    @SerializedName("orders")
    private List<Orders> orders;

    public static class Orders {

        public Orders() {

        }

        @SerializedName("order_sale_id")
        private String orderSaleId;

        @SerializedName("date")
        private String date;

        @SerializedName("table_name")
        private String tableName;

        @SerializedName("table_id")
        private String tableId;

        @SerializedName("grand_total")
        private String grandTotal;

        @SerializedName("total_items")
        private String totalItems;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(String grandTotal) {
            this.grandTotal = grandTotal;
        }

        public String getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(String totalItems) {
            this.totalItems = totalItems;
        }

        public void setOrderSaleId(String orderSaleId) {
            this.orderSaleId = orderSaleId;
        }

        public String getOrderSaleId() {
            return orderSaleId;
        }

        public void setTableId(String tableId) {
            this.tableId = tableId;
        }

        public String getTableId() {
            return tableId;
        }
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Orders> getOrders() {
        return orders;
    }
}
