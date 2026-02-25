package com.rev.app.service.Interface;

import com.rev.app.entity.Order;
import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId, Long shippingAddressId, Long billingAddressId, String paymentMethod);

    List<Order> getOrdersByUser(Long userId);

    Order getOrderById(Long orderId);

    void updateOrderStatus(Long orderId, String status);
}
