package com.techgear.orderservice.services.impl;

import com.techgear.orderservice.dto.User;
import com.techgear.orderservice.entities.Order;

import com.techgear.orderservice.entities.OrderItems;
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
    @Override
    public Order createOrder(Order order) {
        order.getOrderItemsList().forEach(item -> item.setOrder(order)); // set back-reference
        Order savedOrder = orderRepository.save(order);
        sendOrderEmail(savedOrder, "New Order Assigned: Order #" + savedOrder.getId(), "Please process this order as soon as possible.");

        return savedOrder;
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

}


