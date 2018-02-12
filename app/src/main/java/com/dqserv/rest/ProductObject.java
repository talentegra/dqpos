package com.dqserv.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2/12/2018.
 */

public class ProductObject {

    @SerializedName("products")
    private List<Products> products;

    public static class Products {

        public Products() {

        }

        @SerializedName("product_id")
        private String productId;

        @SerializedName("product_code")
        private String productCode;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("product_cost")
        private String productCost;

        public String getProductId() {
            return productId;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getProductCost() {
            return productCost;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setProductCost(String productCost) {
            this.productCost = productCost;
        }
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
