package com.rev.app.service.Impl;

import com.rev.app.entity.Order;
import com.rev.app.entity.Payment;
import com.rev.app.repository.IOrderRepository;
import com.rev.app.repository.IPaymentRepository;
import com.rev.app.service.Interface.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepo;
    @Autowired
    private IOrderRepository orderRepo;

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        log.debug("Fetching payment details for order ID: {}", orderId);
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return paymentRepo.findByOrder(order).orElseThrow(() -> {
            log.warn("Payment not found for order ID: {}", orderId);
            return new RuntimeException("Payment not found for order");
        });
    }

    @Override
    public Payment processPayment(Long orderId, String paymentMethod) {
        log.info("Processing generic payment {} for order ID {}", paymentMethod, orderId);
        // Payment processing logic is primarily handled during placeOrder in
        // OrderServiceImpl for this mock,
        // but can be extended here for distinct payment gateways.
        return getPaymentByOrderId(orderId);
    }
}
