package com.techgear.orderservice.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String customerName, String orderDetails) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);


        String emailContent = generateEmailContent(customerName, orderDetails);

        helper.setText(emailContent, true);
        emailSender.send(message);
    }

    private String generateEmailContent(String customerName, String orderDetails) {
        return "<html>"
                + "<head><style>"
                + "body {font-family: Arial, sans-serif; color: #333; background-color: #f4f4f4; padding: 20px;}"
                + "h1 {color: #333; text-align: center;}"
                + ".order-details {background-color: #fff; padding: 15px; border-radius: 5px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);}"
                + ".order-details p {font-size: 14px; line-height: 1.6;}"
                + ".footer {font-size: 12px; color: #888; text-align: center; margin-top: 30px;}"
                + "</style></head>"
                + "<body>"
                + "<h1>Order Confirmation</h1>"
                + "<p>Dear " + customerName + ",</p>"
                + "<p>Thank you for your recent order with us! Below are your order details:</p>"
                + "<div class='order-details'>"
                + "<h2>Order Details</h2>"
                + "<p>" + orderDetails + "</p>"
                + "</div>"
                + "<p>We will notify you once your order has been processed and shipped. If you have any questions, feel free to contact us.</p>"
                + "<div class='footer'>"
                + "<p>Thank you for choosing us!<br>Your Company Name</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}