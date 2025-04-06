package com.esprit.microservice.Delivery;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    // Default constructor (Spring will inject @Value fields after this)
    public TwilioConfig() {
    }

    // Initialize Twilio after properties are injected
    @PostConstruct
    public void initTwilio() {
        if (accountSid == null || authToken == null || twilioPhoneNumber == null) {
            throw new IllegalStateException("Twilio configuration is incomplete. Check application.properties.");
        }
        Twilio.init(accountSid, authToken);
    }

    public String getTwilioPhoneNumber() {
        return twilioPhoneNumber;
    }
}