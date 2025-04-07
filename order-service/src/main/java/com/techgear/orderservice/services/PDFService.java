package com.techgear.orderservice.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.techgear.orderservice.dto.User;
import com.techgear.orderservice.entities.Order;
import com.techgear.orderservice.entities.OrderItems;
import com.techgear.orderservice.repositories.OrderRepository;
import com.techgear.orderservice.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PDFService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public ByteArrayInputStream generateInvoicePDF(Long orderId) throws Exception {
        // Fetch order from database
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with id: " + orderId));

        // Get user information if available
        Optional<User> userOptional = userRepository.findById(order.getUserId());

        // Setup document
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, out);

        // Add metadata
        document.addAuthor("TechGear");
        document.addCreator("Order Management System");
        document.addTitle("Invoice #" + order.getOrderNumber());

        document.open();

        // Add company logo (if available)
        // Image logo = Image.getInstance("path/to/logo.png");
        // logo.setAlignment(Element.ALIGN_RIGHT);
        // document.add(logo);

        // Add header information
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        Paragraph title = new Paragraph("INVOICE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Add invoice details
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        document.add(new Paragraph("Invoice #: " + order.getOrderNumber(), headerFont));
        document.add(new Paragraph("Date: " + order.getOrderDate().format(dateFormat), normalFont));

        // Add user information if available
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            document.add(new Paragraph("Customer: " + user.getName(), normalFont));
            document.add(new Paragraph("Email: " + user.getEmail(), normalFont));
            document.add(new Paragraph("Phone: " + user.getPhoneNumber(), normalFont));
        } else {
            document.add(new Paragraph("Customer ID: " + order.getUserId(), normalFont));
        }

        document.add(new Paragraph("Shipping Address: " + order.getShippingAddress(), normalFont));
        document.add(new Paragraph("Billing Address: " + order.getBillingAddress(), normalFont));
        document.add(new Paragraph("\n", normalFont));

        // Create items table
        PdfPTable table = new PdfPTable(5); // 5 columns
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Set column widths
        float[] columnWidths = {1f, 5f, 1f, 1.5f, 1.5f};
        table.setWidths(columnWidths);

        // Add table headers
        addTableHeader(table);

        // Add order items
        int itemCount = 1;
        for (OrderItems item : order.getOrderItemsList()) {
            table.addCell(String.valueOf(itemCount++));
            table.addCell(item.getProductName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.format("$%.2f", item.getPrice()));

            // Calculate item total (quantity * price)
            BigDecimal itemTotal = item.getQuantity().multiply(BigDecimal.valueOf(item.getPrice()));
            table.addCell(String.format("$%.2f", itemTotal));
        }

        document.add(table);

        // Add totals
        document.add(new Paragraph("\n", normalFont));

        PdfPTable totalsTable = new PdfPTable(2);
        totalsTable.setWidthPercentage(40);
        totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        // Subtotal (assuming totalAmount is the final amount including any tax/shipping)
        totalsTable.addCell(createCell("Subtotal:", Element.ALIGN_LEFT, headerFont));
        totalsTable.addCell(createCell(String.format("$%.2f", order.getTotalAmount()), Element.ALIGN_RIGHT, normalFont));

        // You can add estimated tax calculation if needed
        BigDecimal estimatedTax = order.getTotalAmount().multiply(new BigDecimal("0.08")); // Example 8% tax
        totalsTable.addCell(createCell("Estimated Tax:", Element.ALIGN_LEFT, headerFont));
        totalsTable.addCell(createCell(String.format("$%.2f", estimatedTax), Element.ALIGN_RIGHT, normalFont));

        // Shipping (could be calculated or fixed)
        BigDecimal shippingFee = new BigDecimal("5.99"); // Example fixed shipping fee
        totalsTable.addCell(createCell("Shipping:", Element.ALIGN_LEFT, headerFont));
        totalsTable.addCell(createCell(String.format("$%.2f", shippingFee), Element.ALIGN_RIGHT, normalFont));

        // Final total
        BigDecimal grandTotal = order.getTotalAmount().add(estimatedTax).add(shippingFee);
        totalsTable.addCell(createCell("Total:", Element.ALIGN_LEFT, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        totalsTable.addCell(createCell(String.format("$%.2f", grandTotal), Element.ALIGN_RIGHT, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

        document.add(totalsTable);

        // Add footer with thank you message and payment information
        document.add(new Paragraph("\n\n", normalFont));
        Paragraph thankYou = new Paragraph("Thank you for your business!", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
        thankYou.setAlignment(Element.ALIGN_CENTER);
        document.add(thankYou);

        Paragraph paymentInfo = new Paragraph("Payment processed via " + order.getPaymentMethod(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
        paymentInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(paymentInfo);

        // Add order status
        Paragraph statusInfo = new Paragraph("Order Status: " + order.getStatus(), new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
        statusInfo.setAlignment(Element.ALIGN_CENTER);
        document.add(statusInfo);

        // Close document
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateDailyOrderReport() throws Exception {
        // Get today's orders (adjust as needed for your specific reporting needs)
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        List<Order> todaysOrders = orderRepository.findByOrderDateBetween(startOfDay, endOfDay);

        // Setup document
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50); // Landscape for reports
        PdfWriter writer = PdfWriter.getInstance(document, out);

        // Add metadata
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String reportDate = LocalDateTime.now().format(dateFormat);

        document.addAuthor("TechGear");
        document.addCreator("Order Management System");
        document.addTitle("Daily Orders Report - " + reportDate);

        document.open();

        // Add title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        Paragraph title = new Paragraph("Daily Orders Report - " + reportDate, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Summary section
        document.add(new Paragraph("Summary:", headerFont));
        document.add(new Paragraph("Total Orders: " + todaysOrders.size(), normalFont));

        // Calculate total revenue
        BigDecimal totalRevenue = todaysOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        document.add(new Paragraph("Total Revenue: $" + String.format("%.2f", totalRevenue), normalFont));
        document.add(new Paragraph("\n", normalFont));

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

        for (Order order : todaysOrders) {
            table.addCell(order.getId().toString());
            table.addCell(order.getOrderNumber());
            table.addCell(order.getOrderDate().format(dateTimeFormat));
            table.addCell(order.getUserId().toString());
            table.addCell(order.getStatus().toString());
            table.addCell(order.getPaymentMethod().toString());
            table.addCell("$" + String.format("%.2f", order.getTotalAmount()));
        }

        document.add(table);

        // Add order status breakdown
        document.add(new Paragraph("\nOrder Status Breakdown:", headerFont));

        // Count orders by status
        long pendingCount = todaysOrders.stream().filter(o -> "PENDING".equals(o.getStatus().toString())).count();
        long processingCount = todaysOrders.stream().filter(o -> "PROCESSING".equals(o.getStatus().toString())).count();
        long shippedCount = todaysOrders.stream().filter(o -> "SHIPPED".equals(o.getStatus().toString())).count();
        long deliveredCount = todaysOrders.stream().filter(o -> "DELIVERED".equals(o.getStatus().toString())).count();
        long cancelledCount = todaysOrders.stream().filter(o -> "CANCELLED".equals(o.getStatus().toString())).count();

        PdfPTable statusTable = new PdfPTable(2);
        statusTable.setWidthPercentage(50);

        statusTable.addCell(createCell("Pending:", Element.ALIGN_LEFT, normalFont));
        statusTable.addCell(createCell(String.valueOf(pendingCount), Element.ALIGN_RIGHT, normalFont));

        statusTable.addCell(createCell("Processing:", Element.ALIGN_LEFT, normalFont));
        statusTable.addCell(createCell(String.valueOf(processingCount), Element.ALIGN_RIGHT, normalFont));

        statusTable.addCell(createCell("Shipped:", Element.ALIGN_LEFT, normalFont));
        statusTable.addCell(createCell(String.valueOf(shippedCount), Element.ALIGN_RIGHT, normalFont));

        statusTable.addCell(createCell("Delivered:", Element.ALIGN_LEFT, normalFont));
        statusTable.addCell(createCell(String.valueOf(deliveredCount), Element.ALIGN_RIGHT, normalFont));

        statusTable.addCell(createCell("Cancelled:", Element.ALIGN_LEFT, normalFont));
        statusTable.addCell(createCell(String.valueOf(cancelledCount), Element.ALIGN_RIGHT, normalFont));

        document.add(statusTable);

        // Report footer
        document.add(new Paragraph("\n\nThis report was automatically generated on " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' HH:mm:ss")),
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)));

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        cell.setPhrase(new Phrase("#", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Product", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Qty", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Unit Price", headerFont));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", headerFont));
        table.addCell(cell);
    }

    private PdfPCell createCell(String content, int alignment, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }
}