
package com.techgear.orderservice.dto;


import java.io.Serializable;
import java.util.List;

public class CheckoutRequest implements Serializable {
    private List<Product> products;
    private User user;
    private String shippingAddress;
    private String billingAddress;

    // Getters and setters
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}