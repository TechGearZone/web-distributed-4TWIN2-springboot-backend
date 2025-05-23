package com.techgear.orderservice.api;



import com.stripe.service.climate.OrderService;
import com.techgear.orderservice.dto.ChatRequestDTO;
import com.techgear.orderservice.dto.Product;
import com.techgear.orderservice.entities.Order;

import com.techgear.orderservice.services.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "OrderController", description = "Operations related to orders")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final IOrderService orderService;
    @Autowired
    private PDFService pdfService;
    @Autowired
    private ReportSchedulerService reportSchedulerService;
    @Autowired
    private AIChatService aiChatService;
    @Autowired
    private QrCodeService qrCodeService;

@Value("${welcome.message}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order order = orderService.updateOrder(id, updatedOrder);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<InputStreamResource> generateInvoice(@PathVariable Long orderId) {
        try {
            ByteArrayInputStream pdfStream = pdfService.generateInvoicePDF(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=invoice-" + orderId + ".pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/reports/daily")
    public ResponseEntity<InputStreamResource> generateDailyReport() {
        try {
            ByteArrayInputStream pdfStream = pdfService.generateDailyOrderReport();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=daily-orders-report.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/reports/custom")
    public ResponseEntity<InputStreamResource> generateCustomReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) boolean sendToAdmin) {

        try {
            ByteArrayInputStream pdfStream = reportSchedulerService.generateCustomDateRangeReport(startDate, endDate);

            if (pdfStream == null) {
                return ResponseEntity.badRequest().build();
            }

            if (sendToAdmin) {

                byte[] data = pdfStream.readAllBytes();
                ByteArrayInputStream adminCopy = new ByteArrayInputStream(data);
                pdfStream = new ByteArrayInputStream(data);

                reportSchedulerService.sendReportToAdmin(adminCopy,
                        "Custom Report " + startDate + " to " + endDate);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=custom-orders-report.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/ask-ai")
    public ResponseEntity<Map<String, String>> askAI(@RequestBody ChatRequestDTO chatRequest) {
        String question = chatRequest.getQuestion();
        String answer = aiChatService.getAIResponse(question);

        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/qr")
    public ResponseEntity<byte[]> generateQrCode(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        String qrCodeText = "Order ID: " + order.getId()
                + ", Customer: " + order.getUser().getName()
                + ", Total: " + order.getTotalAmount();
        try {

            byte[] image = qrCodeService.generateQrCode(qrCodeText, 250, 250);


            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    //////////prod
    @GetMapping("/product/{id}")
    public Product getProductDetails(@PathVariable Long id) {
        return orderService.fetchProductDetails(id);
    }

}
