package com.example.management_system.dto;

import java.math.BigDecimal;

public class CreateOrderResponse {
    private String transactionId;
    private BigDecimal subtotal;
    private int discountPercent;
    private BigDecimal total;

    public CreateOrderResponse(
            String transactionId,
            BigDecimal subtotal,
            int discountPercent,
            BigDecimal total
    ) {
        this.transactionId = transactionId;
        this.subtotal = subtotal;
        this.discountPercent = discountPercent;
        this.total = total;
    }

    // getters
    public String getTransactionId() { return transactionId; }
    public BigDecimal getSubtotal() { return subtotal; }
    public int getDiscountPercent() { return discountPercent; }
    public BigDecimal getTotal() { return total; }
}
