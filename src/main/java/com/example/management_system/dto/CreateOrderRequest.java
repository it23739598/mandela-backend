package com.example.management_system.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {
    private Long customerId;
    private List<OrderItem> items;
    private int loyaltyPointsToRedeem;

    public static class OrderItem {
        private Long productId;
        private BigDecimal unitPrice;
        private int quantity;

        // getters/setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }


    }

    // getters/setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public int getLoyaltyPointsToRedeem() {
        return loyaltyPointsToRedeem;
    }

    public void setLoyaltyPointsToRedeem(int loyaltyPointsToRedeem) {
        this.loyaltyPointsToRedeem = loyaltyPointsToRedeem;
    }

}
