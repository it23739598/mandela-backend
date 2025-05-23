package com.example.management_system.controller;

import com.example.management_system.dto.CreateOrderRequest;
import com.example.management_system.dto.CreateOrderResponse;
import com.example.management_system.dto.OrderSummary;
import com.example.management_system.dto.OrderInvoice;
import com.example.management_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create order
    @PostMapping
    public CreateOrderResponse create(@RequestBody CreateOrderRequest req) {
        return orderService.createOrder(req);
    }

    // Get all orders for history page
    @GetMapping
    public List<OrderSummary> getAllOrders() {
        return orderService.getAllOrderSummaries();
    }

    // Get invoice details by order ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable Long id) {
        try {
            OrderInvoice invoice = orderService.getInvoiceByOrderId(id);
            return ResponseEntity.ok(invoice);
        } catch (RuntimeException e) {
            e.printStackTrace(); // <-- ADD THIS
            if (e.getMessage().contains("Order not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load invoice");
        }
    }

}
