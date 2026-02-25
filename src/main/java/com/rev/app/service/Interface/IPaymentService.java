package com.rev.app.service.Interface;

import com.rev.app.entity.Payment;

public interface IPaymentService {
    Payment getPaymentByOrderId(Long orderId);

    Payment processPayment(Long orderId, String paymentMethod);
}
