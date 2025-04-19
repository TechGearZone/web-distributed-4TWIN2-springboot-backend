package com.esprit.microservice.Delivery;

import org.springframework.stereotype.Component;

@Component
public class MockPhoneNumberResolver implements PhoneNumberResolver {
    @Override
    public String getPhoneNumberForOrder(Long orderId) {
        // Mock implementation: return a hardcoded phone number for testing
        return "+21627326154"; // Replace with real logic or microservice call later
    }
}