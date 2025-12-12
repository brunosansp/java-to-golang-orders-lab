package br.com.brunosansp.order.model;

public class OrderItem {
    private Long id;
    private String productId;
    private Integer quantity;
    
    public OrderItem() {
    }
    
    public OrderItem(Long id, String productId, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
