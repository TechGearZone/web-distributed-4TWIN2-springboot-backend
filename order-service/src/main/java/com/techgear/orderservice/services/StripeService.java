package com.techgear.orderservice.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.techgear.orderservice.dto.CheckoutRequest;
import com.techgear.orderservice.dto.ProductItem;
import com.techgear.orderservice.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    public Session createCheckoutSession(CheckoutRequest request) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (ProductItem product : request.getProducts()) {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(product.getProductName())
                            // You can add more product details if needed
                            .setDescription("Product description") // Optional
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(product.getPrice().multiply(new java.math.BigDecimal(100)).longValue())
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem item =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity(product.getQuantity())
                            .build();

             lineItems.add(item);
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
                .build();

        return Session.create(params);
    }



}