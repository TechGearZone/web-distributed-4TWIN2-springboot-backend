package tn.esprit.microservice.productservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AppService {

    @Autowired
    private TokenService tokenService;

    @PostConstruct
    public void initialize() {
        String token = tokenService.getAccessToken();
        System.out.println("Access Token: " + token);
    }
}

