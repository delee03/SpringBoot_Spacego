package com.hutech.lab02.service;

import com.hutech.lab02.model.CartItem;
import com.hutech.lab02.model.Order;
import com.hutech.lab02.model.OrderDetail;
import com.hutech.lab02.repository.OrderDetailRepository;
import com.hutech.lab02.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService
    @Transactional
    public Order createOrder(String customerName,  String customerPhone, String customerAddress, String customerMethod,List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerPhone(customerPhone);
        order.setCustomerAddress(customerAddress);
        order.setCustomerMethod(customerMethod);
        order.setCustomerName(customerName);
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
        cartService.clearCart();
        return order;
    }
}