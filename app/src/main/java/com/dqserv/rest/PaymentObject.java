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

        @SerializedName("order_sale_id")
        private String orderSaleId;

        @SerializedName("order_sale_item_id")
        private String orderSaleItemId;

        @SerializedName("product_id")
        private String productId;

        @SerializedName("product_code")
        private String productCode;

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

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setOrderSaleId(String orderSaleId) {
            this.orderSaleId = orderSaleId;
        }

        public void setOrderSaleItemId(String orderSaleItemId) {
            this.orderSaleItemId = orderSaleItemId;
        }

        public String getOrderSaleId() {
            return orderSaleId;
        }

        public String getOrderSaleItemId() {
            return orderSaleItemId;
        }
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Orders> getOrders() {
        return orders;
    }
}
