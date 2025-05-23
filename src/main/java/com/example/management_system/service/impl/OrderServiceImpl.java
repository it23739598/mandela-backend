package com.example.management_system.service.impl;

import com.example.management_system.dto.CreateOrderRequest;
import com.example.management_system.dto.CreateOrderResponse;
import com.example.management_system.dto.OrderInvoice;
import com.example.management_system.dto.OrderSummary;
import com.example.management_system.model.*;
import com.example.management_system.repository.*;
import com.example.management_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private CustomerOrderRepository orderRepo;
    @Autowired private CustomerOrderItemRepository itemRepo;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest req) {
        Customer customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        var itemsDto = req.getItems();

        // 1) Subtotal
        BigDecimal subtotal = itemsDto.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2) Loyalty points redemption (Rs.1 per point)
        int loyaltyPoints = req.getLoyaltyPointsToRedeem();
        BigDecimal loyaltyDiscount = BigDecimal.valueOf(loyaltyPoints);

        // Apply the loyalty discount to the subtotal
        BigDecimal subtotalAfterLoyalty = subtotal.subtract(loyaltyDiscount);
        if (subtotalAfterLoyalty.compareTo(BigDecimal.ZERO) < 0) {
            subtotalAfterLoyalty = BigDecimal.ZERO;
        }

        // 3) Birthday-month discount
        int discount = 0;
        if (customer.getBirthDate() != null) {
            int birthMonth = customer.getBirthDate().getMonthValue();
            int nowMonth   = LocalDate.now().getMonthValue();
            if (birthMonth == nowMonth) discount = 20;
        }

        // 4) Total after both discounts
        BigDecimal total = subtotalAfterLoyalty
                .multiply(BigDecimal.valueOf(100 - discount))
                .divide(BigDecimal.valueOf(100));

        // 5) Persist order
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setTransactionId(UUID.randomUUID().toString());
        order.setSubtotal(subtotal);
        order.setDiscountPercent(discount);
        order.setTotal(total);
        order.setLoyaltyDiscount(loyaltyDiscount);
        CustomerOrder saved = orderRepo.save(order);

        String txId = String.format("T#%03d", saved.getId());
        saved.setTransactionId(txId);
        saved = orderRepo.save(saved);

        // 6) Persist items
        for (var dto : itemsDto) {
            Product prod = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            int newQty = prod.getQuantity() - dto.getQuantity();
            prod.setQuantity(newQty);
            productRepo.save(prod);

            CustomerOrderItem item = new CustomerOrderItem();
            item.setOrder(saved);
            item.setProduct(prod);
            item.setUnitPrice(dto.getUnitPrice());
            item.setQuantity(dto.getQuantity());
            item.setLineTotal(dto.getUnitPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
            itemRepo.save(item);
        }

        return new CreateOrderResponse(txId, subtotal, discount, total);
    }

    // 🆕 Get all orders for Order History page
    @Override
    public List<OrderSummary> getAllOrderSummaries() {
        return orderRepo.findAll().stream()
                .map(order -> new OrderSummary(
                        order.getId(),
                        order.getTransactionId(),
                        order.getCustomer().getPhoneNumber(),
                        order.getTotal(),
                        order.getDiscountPercent(),
                        order.getOrderDate()
                ))
                .collect(Collectors.toList());
    }

    // 🆕 Get full invoice for a given order
    @Override
    @Transactional
    public OrderInvoice getInvoiceByOrderId(Long id) {
        CustomerOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderInvoice.Item> items = order.getItems().stream()
                .map(i -> new OrderInvoice.Item(
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getLineTotal()
                ))
                .collect(Collectors.toList());

        return new OrderInvoice(
                order.getTransactionId(),
                order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName(),
                order.getCustomer().getPhoneNumber(),
                order.getOrderDate(),
                order.getSubtotal(),
                order.getDiscountPercent(),
                order.getTotal(),
                items
        );
    }
}
