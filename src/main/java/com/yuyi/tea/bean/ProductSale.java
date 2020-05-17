package com.yuyi.tea.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(value = { "handler" })
public class ProductSale implements Serializable {

    private int productId;
    private Product product;
    private BigDecimal sales=BigDecimal.valueOf(0);

    public ProductSale(int productId) {
        this.productId = productId;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getSales() {
        return sales;
    }

    public void setSales(BigDecimal sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "ProductSale{" +
                "product=" + product +
                ", sales=" + sales +
                '}';
    }
}
