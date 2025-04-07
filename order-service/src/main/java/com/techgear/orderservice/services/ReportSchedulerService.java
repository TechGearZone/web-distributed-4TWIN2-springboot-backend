package com.techgear.orderservice.services;


import com.techgear.orderservice.entities.Order;
import com.techgear.orderservice.entities.OrderStatus;
import com.techgear.orderservice.repositories.OrderRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportSchedulerService {

    @Autowired
    private PDFService pdfService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Value("${report.output.directory:./reports}")
    private String reportOutputDirectory;

    @Value("${report.email.recipients:admin@techgear.com}")
    private String reportEmailRecipients;

    @Value("${spring.mail.username:no-reply@techgear.com}")
    private String emailFrom;

    // Run daily at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailyOrderReports() {
        try {
            // Ensure directory exists
            Path reportDir = Paths.get(reportOutputDirectory);
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
            }

            // Generate report PDF
            ByteArrayInputStream pdfStream = pdfService.generateDailyOrderReport();

            // Save to file system
            LocalDate today = LocalDate.now();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
            String fileName = "daily-order-report-" + today.format(dateFormat) + ".pdf";
            String filePath = reportOutputDirectory + "/" + fileName;

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            // Email the report to administrators
            sendDailyReportEmail(filePath, fileName, today);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Run on the first day of each month at 1:00 AM
    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateMonthlyOrderReports() {
        try {
            // Implementation for monthly reports
            // You could extend PDFService with a generateMonthlyOrderReport method
            // Similar to the daily report but with data from the previous month

            // For now, we'll just log that it ran
            System.out.println("Monthly report generation triggered at " + LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ad-hoc report generation for a specific date range
    public ByteArrayInputStream generateCustomDateRangeReport(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // This would be implemented in PDFService
            // For now, we'll use the daily report method
            return pdfService.generateDailyOrderReport();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendDailyReportEmail(String filePath, String fileName, LocalDate reportDate) {
        try {
            // Get summary data for email body
            LocalDateTime startOfDay = reportDate.atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

            List<Order> todaysOrders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);
            BigDecimal totalRevenue = todaysOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Count orders by status
            long pendingCount = todaysOrders.stream().filter(o -> OrderStatus.PROCESSING.equals(o.getStatus())).count();
            long deliveredCount = todaysOrders.stream().filter(o -> OrderStatus.DELIVERED.equals(o.getStatus())).count();

            // Create email
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailFrom);
            helper.setTo(reportEmailRecipients.split(","));
            helper.setSubject("Daily Orders Report - " + reportDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

            // Email body with summary
            String emailContent =
                    "<html><body>" +
                            "<h2>Daily Orders Report - " + reportDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + "</h2>" +
                            "<p>Please find attached the daily orders report.</p>" +
                            "<h3>Summary:</h3>" +
                            "<ul>" +
                            "<li>Total Orders: " + todaysOrders.size() + "</li>" +
                            "<li>Total Revenue: $" + String.format("%.2f", totalRevenue) + "</li>" +
                            "<li>Pending Orders: " + pendingCount + "</li>" +
                            "<li>Delivered Orders: " + deliveredCount + "</li>" +
                            "</ul>" +
                            "<p>For full details, please see the attached PDF report.</p>" +
                            "<p>Regards,<br>TechGear Order Management System</p>" +
                            "</body></html>";

            helper.setText(emailContent, true); // true indicates HTML content

            // Add PDF attachment
            helper.addAttachment(fileName, new java.io.File(filePath));

            // Send email
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}