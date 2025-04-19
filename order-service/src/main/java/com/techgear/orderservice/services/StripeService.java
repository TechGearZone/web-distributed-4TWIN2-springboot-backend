package com.techgear.orderservice.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.techgear.orderservice.dto.CheckoutRequest;
import com.techgear.orderservice.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StripeService {

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    public Session createCheckoutSession(CheckoutRequest request) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (Product product : request.getProducts()) {
            System.out.println("Product ID: " + product.getId() + ", Price: " + product.getPrice() + ", Name: " + product.getName() + ", Quantity: " + product.getStock());

            // Check if price is set properly
            if (product.getPrice() <= 0) {
                throw new IllegalArgumentException("Price for product " + product.getName() + " is invalid (zero or negative).");
            }

            // Prepare product data
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(Optional.ofNullable(product.getName()).orElse("Unnamed Product"))
                            .setDescription(Optional.ofNullable(product.getDescription()).orElse("No description available"))
                            .build();

            // Convert price to cents and ensure it is positive
            long priceInCents = (long) (product.getPrice() * 100);

            // Prepare price data
            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(priceInCents)
                            .setProductData(productData)
                            .build();

            // Create line item with the requested quantity, not the total stock
            SessionCreateParams.LineItem item =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity(Long.valueOf(product.getStock())) // product.getStock() represents requested quantity here
                            .build();

            lineItems.add(item);
        }

        // Create checkout session with session ID in success and cancel URLs
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .addAllLineItem(lineItems)
                .build();

        return Session.create(params);
    }
}