package com.techgear.orderservice.api;



import com.techgear.orderservice.dto.ChatRequestDTO;
import com.techgear.orderservice.entities.Order;

import com.techgear.orderservice.services.AIChatService;
import com.techgear.orderservice.services.IOrderService;
import com.techgear.orderservice.services.PDFService;
import com.techgear.orderservice.services.ReportSchedulerService;
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


import java.io.ByteArrayInputStream;
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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        try {
            ByteArrayInputStream pdfStream = reportSchedulerService.generateCustomDateRangeReport(startDate, endDate);

            if (pdfStream == null) {
                return ResponseEntity.badRequest().build();
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
}