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

        @SerializedName("product_id")
        private String productId;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("unit_price")
        private String unitPrice;

        @SerializedName("quantity")
        private String quantity;

        @SerializedName("subtotal")
        private String subtotal;

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Orders> getOrders() {
        return orders;
    }
}
