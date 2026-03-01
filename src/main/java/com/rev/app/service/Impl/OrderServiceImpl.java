package com.rev.app.service.Impl;

import com.rev.app.entity.*;
import com.rev.app.exception.BadRequestException;
import com.rev.app.exception.InsufficientStockException;
import com.rev.app.exception.ResourceNotFoundException;
import com.rev.app.repository.*;
import com.rev.app.service.Interface.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository orderRepo;
    @Autowired
    private IOrderItemRepository orderItemRepo;
    @Autowired
    private IProductRepository productRepo;
    @Autowired
    private ICartRepository cartRepo;
    @Autowired
    private ICartItemRepository cartItemRepo;
    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private IAddressRepository addressRepo;
    @Autowired
    private IPaymentRepository paymentRepo;
    @Autowired
    private com.rev.app.service.Interface.INotificationService notificationService;

    @Override
    @Transactional
    public Order placeOrder(Long userId, Long shippingAddressId, Long billingAddressId, String paymentMethodStr) {
        User u = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepo.findByUser(u).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        List<CartItem> cartItems = cartItemRepo.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Address shipping = addressRepo.findById(shippingAddressId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address not found"));
        Address billing = addressRepo.findById(billingAddressId)
                .orElseThrow(() -> new ResourceNotFoundException("Billing address not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal price = item.getProduct().getDiscountedPrice() != null ? item.getProduct().getDiscountedPrice()
                    : item.getProduct().getPrice();
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Order order = new Order();
        order.setUser(u);
        order.setShippingAddress(shipping);
        order.setBillingAddress(billing);
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.PENDING);

        order = orderRepo.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());

            BigDecimal price = item.getProduct().getDiscountedPrice() != null ? item.getProduct().getDiscountedPrice()
                    : item.getProduct().getPrice();
            orderItem.setPrice(price);

            orderItemRepo.save(orderItem);

            // Decrease Stock
            Product p = item.getProduct();
            if (p.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + p.getName());
            }
            p.setQuantity(p.getQuantity() - item.getQuantity());
            productRepo.save(p);
        }

        // Process Payment
        Payment.PaymentMethod pm = Payment.PaymentMethod.valueOf(paymentMethodStr.toUpperCase());
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(pm);
        payment.setStatus(
                pm == Payment.PaymentMethod.COD ? Payment.PaymentStatus.PENDING : Payment.PaymentStatus.COMPLETED);

        // Mock transaction ID for non-COD
        if (pm != Payment.PaymentMethod.COD) {
            payment.setTransactionId("TXN-" + System.currentTimeMillis());
        }

        paymentRepo.save(payment);

        // Clear Cart
        cartItemRepo.deleteAll(cartItems);

        // Send Notifications
        notificationService.sendNotification(u.getId(),
                "Your order #" + order.getId() + " has been placed successfully!");

        // Notify Sellers
        for (CartItem item : cartItems) {
            notificationService.sendNotification(item.getProduct().getSeller().getId(),
                    "New order received: #" + order.getId() + " for product " + item.getProduct().getName());
        }

        return order;
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        User u = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepo.findByUserOrderByCreatedAtDesc(u);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        orderRepo.save(order);

        // Notify Buyer
        notificationService.sendNotification(order.getUser().getId(),
                "Your order #" + order.getId() + " status has been updated to " + status.toUpperCase());
    }
}
