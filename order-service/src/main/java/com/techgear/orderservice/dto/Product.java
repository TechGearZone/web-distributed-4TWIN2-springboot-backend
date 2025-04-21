package com.techgear.orderservice.dto;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Product implements Serializable {


    private Long id;

    private String name;
    private String description;
    private double price;
    private int stock;

    private String category;

    @ElementCollection
    private List<String> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}