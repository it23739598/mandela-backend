package com.example.management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderInvoice {
    private String transactionId;
    private String customerName;
    private String customerPhone;
    private LocalDateTime orderDate;
    private BigDecimal subtotal;
    private int discountPercent;
    private BigDecimal total;
    private List<Item> items;

    public static class Item {
        public String productName;
        public int quantity;
        public BigDecimal unitPrice;
        public BigDecimal lineTotal;

        public Item(String productName, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.lineTotal = lineTotal;
        }
    }

    public OrderInvoice(String transactionId, String customerName, String customerPhone,
                        LocalDateTime orderDate, BigDecimal subtotal,
                        int discountPercent, BigDecimal total, List<Item> items) {
        this.transactionId = transactionId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.orderDate = orderDate;
        this.subtotal = subtotal;
        this.discountPercent = discountPercent;
        this.total = total;
        this.items = items;
    }

    // Getters here (omit setters for immutability)
    public String getTransactionId() { return transactionId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getSubtotal() { return subtotal; }
    public int getDiscountPercent() { return discountPercent; }
    public BigDecimal getTotal() { return total; }
    public List<Item> getItems() { return items; }
}
