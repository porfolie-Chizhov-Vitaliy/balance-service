package com.testbank.dbo.balanceservice.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Balance Service is UP and running!";
    }

    @GetMapping("/api/balances/hello")
    public String hello() {
        return "Hello from Balance Service!";
    }
}