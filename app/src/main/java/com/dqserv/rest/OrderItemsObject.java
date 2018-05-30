package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class OrderItemsObject {

    @SerializedName("orders")
    private List<Orders> orders;

    public static class Orders {

        public Orders() {

        }

        @SerializedName("product_code")
        private String productCode;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("quantity")
        private String quantity;

        @SerializedName("sale_price")
        private String salePrice;

        @SerializedName("subtotal")
        private String subTotal;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(String subTotal) {
            this.subTotal = subTotal;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductCode() {
            return productCode;
        }
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Orders> getOrders() {
        return orders;
    }
}
