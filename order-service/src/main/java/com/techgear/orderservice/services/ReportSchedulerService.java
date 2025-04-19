package com.techgear.orderservice.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import com.techgear.orderservice.entities.Order;
import com.techgear.orderservice.entities.OrderStatus;
import com.techgear.orderservice.repositories.OrderRepository;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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

    @Value("${report.email.recipients:hamzosayari07@gmail.com}")
    private String reportEmailRecipients;

    @Value("${spring.mail.username:hamzosayari07@gmail.com}")
    private String emailFrom;

    @Scheduled(cron = "0 0/30 * * * ?") // Every 2 minutes
    public void generateDailyOrderReportAndSendToAdmin() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

            List<Order> todaysOrders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);

            // Generate report as PDF
            ByteArrayInputStream report = generateOrderReport(todaysOrders, "Daily Orders Report",
                    today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // Send report to admin via email
            sendReportToAdmin(report, "Daily_Orders_Report_" + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Run on the first day of each month at 1:00 AM
    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateMonthlyOrderReports() {
        try {

            System.out.println("Monthly report generation triggered at " + LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ByteArrayInputStream generateCustomDateRangeReport(LocalDate startDate, LocalDate endDate) throws Exception {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        System.out.println("Searching for orders between: " + startDateTime + " and " + endDateTime);

        List<Order> ordersInRange = orderRepository.findByOrderDateBetween(startDateTime, endDateTime);

        System.out.println("Found " + ordersInRange.size() + " orders");
        ordersInRange.forEach(order -> System.out.println("Order date: " + order.getOrderDate()));

        return generateOrderReport(ordersInRange, "Custom Date Range Report",
                startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " to " +
                        endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    private ByteArrayInputStream generateOrderReport(List<Order> orders, String reportTitle, String dateRange) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50); // Landscape for reports
        PdfWriter writer = PdfWriter.getInstance(document, out);

        // Add metadata
        document.addAuthor("TechGear");
        document.addCreator("Order Management System");
        document.addTitle(reportTitle + " - " + dateRange);

        document.open();
        try {
            Image logo = Image.getInstance(getClass().getClassLoader().getResource("logo.png"));
            logo.scaleToFit(120, 60);
            logo.setAlignment(Image.ALIGN_RIGHT);
            document.add(logo);
        } catch (Exception e) {
            System.err.println("Could not load logo: " + e.getMessage());
        }
        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        Paragraph title = new Paragraph(reportTitle + " - " + dateRange, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Summary section
        document.add(new Paragraph("Summary:", headerFont));
        document.add(new Paragraph("Total Orders: " + orders.size(), normalFont));

        // Calculate total revenue
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        document.add(new Paragraph("Total Revenue: $" + String.format("%.2f", totalRevenue), normalFont));
        document.add(new Paragraph("\n", normalFont));

        // Rest of your report generation code (table creation, etc.)
        // Orders table
        PdfPTable table = new PdfPTable(7); // 7 columns for the report
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {1f, 2f, 2f, 2f, 3f, 2f, 2f};
        table.setWidths(columnWidths);

        // Add table headers
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
        headerCell.setPadding(5);

        headerCell.setPhrase(new Phrase("Order ID", tableHeaderFont));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Order Number", tableHeaderFont));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Date/Time", tableHeaderFont));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Customer ID", tableHeaderFont));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Status", tableHeaderFont));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Payment Method", tableHeaderFont));
        table.addCell(headerCell);

        headerCell.setPhrase(new Phrase("Total Amount", tableHeaderFont));
        table.addCell(headerCell);

        // Add orders to table
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

        for (Order order : orders) {
            table.addCell(order.getId().toString());
            table.addCell(order.getOrderNumber());
            table.addCell(order.getOrderDate().format(dateTimeFormat));
            table.addCell(order.getUser().getId().toString());
            table.addCell(order.getStatus().toString());
            table.addCell(order.getPaymentMethod().toString());
            table.addCell("$" + String.format("%.2f", order.getTotalAmount()));
        }

        document.add(table);

        // Status breakdown and footer code
        // ... (rest of your report generation code)

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void sendDailyReportEmail(String filePath, String fileName, LocalDate reportDate) {
        try {

            LocalDateTime startOfDay = reportDate.atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

            List<Order> todaysOrders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);
            BigDecimal totalRevenue = todaysOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


            long pendingCount = todaysOrders.stream().filter(o -> OrderStatus.PROCESSING.equals(o.getStatus())).count();
            long deliveredCount = todaysOrders.stream().filter(o -> OrderStatus.DELIVERED.equals(o.getStatus())).count();


            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailFrom);
            helper.setTo(reportEmailRecipients.split(","));
            helper.setSubject("Daily Orders Report - " + reportDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

            // Email body
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

            helper.setText(emailContent, true);

            // Add PDF attachment
            helper.addAttachment(fileName, new java.io.File(filePath));

            // Send email
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendReportToAdmin(ByteArrayInputStream reportData, String reportName) {
        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailFrom);
            helper.setTo(emailFrom);
            helper.setSubject("Order Report: " + reportName);

            String emailContent = "<html><body>" +
                    "<h2>Order Report: " + reportName + "</h2>" +
                    "<p>Please find attached the requested order report.</p>" +
                    "<p>Regards,<br>TechGear Order Management System</p>" +
                    "</body></html>";

            helper.setText(emailContent, true);


            byte[] reportBytes = reportData.readAllBytes();
            helper.addAttachment(reportName + ".pdf", new ByteArrayResource(reportBytes));

            // Send email
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}