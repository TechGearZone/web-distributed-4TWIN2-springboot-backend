package com.techgear.orderservice.api;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.techgear.orderservice.dto.CheckoutRequest;
import com.techgear.orderservice.services.EmailService;
import com.techgear.orderservice.services.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody CheckoutRequest request) throws StripeException {
        System.out.println("Request received: " + request);
        System.out.println("Products count: " + request.getProducts().size());

        Session session = stripeService.createCheckoutSession(request);
        System.out.println("Session URL: " + session.getUrl());
        return session.getUrl();
    }
    @GetMapping("/success")
    public String paymentSuccess() {

        return "Payment successful!";
    }

    @GetMapping("/cancel")
    public String paymentCancelled() {
        return "Payment cancelled.";
    }
}
