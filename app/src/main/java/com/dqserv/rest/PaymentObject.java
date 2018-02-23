package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class PaymentObject {

    @SerializedName("orders")
    private List<Orders> orders;

    public static class Orders {

        public Orders() {

        }

        @SerializedName("product_id")
        private String productId;

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductId() {
            return productId;
        }
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Orders> getOrders() {
        return orders;
    }
}
