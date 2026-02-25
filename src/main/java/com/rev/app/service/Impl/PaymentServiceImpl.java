package com.rev.app.service.Impl;

import com.rev.app.entity.Order;
import com.rev.app.entity.Payment;
import com.rev.app.repository.IOrderRepository;
import com.rev.app.repository.IPaymentRepository;
import com.rev.app.service.Interface.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private IPaymentRepository paymentRepo;
    @Autowired
    private IOrderRepository orderRepo;

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return paymentRepo.findByOrder(order).orElseThrow(() -> new RuntimeException("Payment not found for order"));
    }

    @Override
    public Payment processPayment(Long orderId, String paymentMethod) {
        // Payment processing logic is primarily handled during placeOrder in
        // OrderServiceImpl for this mock,
        // but can be extended here for distinct payment gateways.
        return getPaymentByOrderId(orderId);
    }
}
