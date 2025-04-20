package com.esprit.microservice.Delivery;

public interface PhoneNumberResolver {
    String getPhoneNumberForOrder(Long orderId);
}