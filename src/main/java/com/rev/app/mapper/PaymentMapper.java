package com.rev.app.mapper;

import com.rev.app.dto.PaymentResponseDTO;
import com.rev.app.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .paymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null)
                .amount(payment.getAmount())
                .build();
    }
}
