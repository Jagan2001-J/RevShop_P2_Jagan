package com.rev.app.mapper;

import com.rev.app.dto.OrderResponseDTO;
import com.rev.app.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    @Autowired
    private AddressMapper addressMapper;

    public OrderResponseDTO toDto(Order order) {
        if (order == null) {
            return null;
        }

        String paymentMethod = order.getPayment() != null && order.getPayment().getPaymentMethod() != null
                ? order.getPayment().getPaymentMethod().name()
                : "PENDING";

        String paymentStatus = order.getPayment() != null && order.getPayment().getStatus() != null
                ? order.getPayment().getStatus().name()
                : "PENDING";

        return OrderResponseDTO.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .paymentMethod(paymentMethod)
                .paymentStatus(paymentStatus)
                .shippingAddress(addressMapper.toDto(order.getShippingAddress()))
                .createdAt(order.getCreatedAt())
                .build();
    }
}
