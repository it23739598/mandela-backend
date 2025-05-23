package com.example.management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummary{
    private Long id;
    private String transactionId;
    private String customerPhone;
    private BigDecimal total;
    private int discountPercent;
    private LocalDateTime orderDate;

    // Constructors
    public OrderSummary(Long id, String transactionId, String customerPhone,
                        BigDecimal total, int discountPercent, LocalDateTime orderDate) {
        this.id = id;
        this.transactionId = transactionId;
        this.customerPhone = customerPhone;
        this.total = total;
        this.discountPercent = discountPercent;
        this.orderDate = orderDate;
    }

    // Getters only
    public Long getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public String getCustomerPhone() { return customerPhone; }
    public BigDecimal getTotal() { return total; }
    public int getDiscountPercent() { return discountPercent; }
    public LocalDateTime getOrderDate() { return orderDate; }
}
