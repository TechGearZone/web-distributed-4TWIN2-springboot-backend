package tn.esprit.microservice.productservice;

public class ComparisonProduct {
    private String name;
    private String description;
    private double price;
    private String currency;
    private String imageUrl;
    private String source;
    private String link;

    public ComparisonProduct() {}

    public ComparisonProduct(String name, double price, String currency, String imageUrl, String source, String link) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.imageUrl = imageUrl;
        this.source = source;
        this.link = link;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

