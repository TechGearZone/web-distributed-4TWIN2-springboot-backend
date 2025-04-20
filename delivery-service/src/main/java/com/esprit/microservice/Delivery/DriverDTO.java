package com.esprit.microservice.Delivery;

public class DriverDTO {
    private Long id;
    private String name;
    private String phone;

    // Constructeurs
    public DriverDTO() {}
    public DriverDTO(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}