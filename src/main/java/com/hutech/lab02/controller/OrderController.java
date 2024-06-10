package com.hutech.lab02.controller;

import com.hutech.lab02.model.CartItem;
import com.hutech.lab02.model.Order;
import com.hutech.lab02.model.Product;
import com.hutech.lab02.model.User;
import com.hutech.lab02.service.CartService;
import com.hutech.lab02.service.OrderService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }
    @PostMapping("/submit")
    public String submitOrder(String customerName, String customerPhone, String customerAddress, String customerMethod) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        orderService.createOrder(customerName, customerPhone, customerAddress, customerMethod, cartItems);
        return "redirect:/order/confirmation";
    }
    @GetMapping("/confirmation")
    public String orderConfirmation(Model model ) {
        model.addAttribute("message", "Bạn đã đặt hàng thành công  .");
        return "cart/order-confirmation";
    }
}
