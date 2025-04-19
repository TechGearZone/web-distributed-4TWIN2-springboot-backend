package tn.esprit.microservice.productservice;


public class StockUpdateMessage {
    private Long productId;
    private int quantity;

    // Constructors
    public StockUpdateMessage() {
    }

    public StockUpdateMessage(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
