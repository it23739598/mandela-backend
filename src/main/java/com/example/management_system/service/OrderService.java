package com.example.management_system.service;

import com.example.management_system.dto.CreateOrderRequest;
import com.example.management_system.dto.CreateOrderResponse;
import com.example.management_system.dto.OrderSummary;
import com.example.management_system.dto.OrderInvoice;

import java.util.List;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);

    List<OrderSummary> getAllOrderSummaries();

    OrderInvoice getInvoiceByOrderId(Long id);
}
