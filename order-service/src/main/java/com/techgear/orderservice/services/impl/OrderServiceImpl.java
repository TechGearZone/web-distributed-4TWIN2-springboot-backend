package com.techgear.orderservice.services.impl;

import com.techgear.orderservice.clients.ProductClient;
import com.techgear.orderservice.dto.Product;
import com.techgear.orderservice.dto.User;
import com.techgear.orderservice.entities.Order;

import com.techgear.orderservice.entities.OrderItems;
import com.techgear.orderservice.entities.OrderStatus;
import com.techgear.orderservice.repositories.OrderRepository;
import com.techgear.orderservice.repositories.UserRepository;
import com.techgear.orderservice.services.EmailService;
import com.techgear.orderservice.services.IOrderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private EmailService emailService;
    private final OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductClient productClient;

    @Override
    public Order createOrder(Order order) {
        // Set basic order info
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);

        // To hold total
        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;

        // Set back-reference and populate each item
        for (OrderItems item : order.getOrderItemsList()) {
            item.setOrder(order); // back-reference

            // 🔥 Fetch product info from ProductService
            Product product = productClient.getProductById(item.getProductId());

            // ✅ Check stock
            if (product.getStock() < item.getQuantity().intValue()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName() +
                        ". Available: " + product.getStock() +
                        ", Requested: " + item.getQuantity());
            }

            // ✅ Set product info
            item.setProductName(product.getName());
            item.setPrice((float) product.getPrice());

            // ✅ Calculate total
            java.math.BigDecimal itemTotal = java.math.BigDecimal.valueOf(product.getPrice())
                    .multiply(item.getQuantity());
            totalAmount = totalAmount.add(itemTotal);
        }

        // ✅ Set total amount and save
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // ✅ Send email
        sendOrderEmail(savedOrder, "New Order Assigned: Order #" + savedOrder.getId(), "Please process this order as soon as possible.");

        return savedOrder;
    }

@Override
    public List<Order> getUnpaidOrders() {
        return orderRepository.findByStatus(OrderStatus.PROCESSING);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order updateOrder(Long id, Order updatedOrder) {
        // Fetch the existing order
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Update the fields conditionally, only if the field in updatedOrder is not null
        if (updatedOrder.getOrderNumber() != null) {
            existingOrder.setOrderNumber(updatedOrder.getOrderNumber());
        }
        if (updatedOrder.getShippingAddress() != null) {
            existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
        }
        if (updatedOrder.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
        }
        if (updatedOrder.getTotalAmount() != null) {
            existingOrder.setTotalAmount(updatedOrder.getTotalAmount());
        }
        if (updatedOrder.getOrderItemsList() != null && !updatedOrder.getOrderItemsList().isEmpty()) {
            existingOrder.setOrderItemsList(updatedOrder.getOrderItemsList());
        }

        // Save the updated order
        Order savedOrder = orderRepository.save(existingOrder);

        // Send email (optional)
        sendOrderEmail(savedOrder, "Order Updated: Order #" + savedOrder.getId(), "Your order has been updated.");

        return savedOrder;
    }


    private void sendOrderEmail(Order order, String subject, String message) {
     // Get user information from the repository
     Optional<User> userOptional = userRepository.findById(order.getUser().getId());

     if (!userOptional.isPresent()) {
         log.error("User with ID {} not found, cannot send email notification", order.getUser().getId());
         return;
     }

     User user = userOptional.get();
     String recipientEmail = user.getEmail();

     // If the user's email is not available, fall back to a default email
     if (recipientEmail == null || recipientEmail.isEmpty()) {
         recipientEmail = "ayari.hamza1@esprit.tn"; // Fallback email
         log.warn("User email not found, using fallback email");
     }


     String employeeMessage = generateEmployeeEmailContent(order, user, message);


     try {
         log.info("Attempting to send email to: {}", recipientEmail);
         emailService.sendEmail(recipientEmail, subject, user.getName(), employeeMessage);
         log.info("Email sent successfully to {}", recipientEmail);
     } catch (MessagingException e) {
         log.error("Failed to send email to {}: {}", recipientEmail, e.getMessage(), e);
     }
 }
    private String generateEmployeeEmailContent(Order order, User user, String customMessage) {
        StringBuilder emailContent = new StringBuilder();

        // Start the email body
        emailContent.append("<html>")
                .append("<head><style>")
                .append("body {font-family: Arial, sans-serif; color: #333; background-color: #f4f4f4; padding: 20px;}")
                .append("h1 {color: #333; text-align: center;}")
                .append(".order-details {background-color: #fff; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}")
                .append(".footer {font-size: 12px; color: #888; text-align: center; margin-top: 30px;}")
                .append("</style></head>")
                .append("<body>")
                .append("<h1>New Order Assigned</h1>")
                .append("<p>Dear Client,</p>")
                .append("<p>You have been assigned a new order. Below are the order details:</p>")
                .append("<div class='order-details'>")
                .append("<h2>Order Details</h2>")
                .append("<p><strong>Customer:</strong> ").append(user.getName()).append("<br>")
                .append("<strong>Order Number:</strong> ").append(order.getOrderNumber()).append("<br>")
                .append("<strong>Order Date:</strong> ").append(order.getOrderDate()).append("<br>")
                .append("<strong>Shipping Address:</strong> ").append(order.getShippingAddress()).append("<br>")
                .append("<strong>Payment Method:</strong> ").append(order.getPaymentMethod()).append("<br>")
                .append("<strong>Order Items:</strong></p>")
                .append("<ul>");

        // Loop through the items in the order
        for (OrderItems item : order.getOrderItemsList()) {
            emailContent.append("<li>")
                    .append(item.getQuantity()).append(" x ").append(item.getProductName())
                    .append(" - $").append(item.getPrice())
                    .append("</li>");
        }
        emailContent.append("</ul>")
                .append("<p><strong>Total Amount:</strong> $").append(order.getTotalAmount()).append("</p>")
                .append("<p>").append(customMessage).append("</p>")
                .append("</div>")
                .append("<div class='footer'>")
                .append("<p>Thank you for your prompt action.<br>Your Company Name</p>")
                .append("</div>")
                .append("</body>")
                .append("</html>");

        return emailContent.toString();
    }
    /////product

    @Override
    public Product fetchProductDetails(Long productId) {
        return productClient.getProductById(productId);
    }


}


