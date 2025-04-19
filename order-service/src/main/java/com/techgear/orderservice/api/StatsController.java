package com.techgear.orderservice.api;

import com.techgear.orderservice.entities.OrderStatus;
import com.techgear.orderservice.repositories.OrderRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final OrderRepository orderRepository;

    public StatsController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping(value = "/orders/status-distribution", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getOrderStatusDistributionChart() throws Exception {
        // Sample statuses — adjust as needed
        long pending = orderRepository.countByStatus(OrderStatus.PROCESSING);
        long completed = orderRepository.countByStatus(OrderStatus.DELIVERED);
        long cancelled = orderRepository.countByStatus(OrderStatus.CANCELLED);

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("PROCESSING", pending);
        dataset.setValue("DELIVERED", completed);
        dataset.setValue("CANCELLED", cancelled);

        JFreeChart chart = ChartFactory.createPieChart(
                "Order Status Distribution",
                dataset,
                true,
                true,
                false
        );

        BufferedImage chartImage = chart.createBufferedImage(600, 400);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeBufferedImageAsPNG(baos, chartImage);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(baos.toByteArray());
    }
}
