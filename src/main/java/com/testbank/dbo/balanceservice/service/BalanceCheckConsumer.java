package com.testbank.dbo.balanceservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbank.dbo.balanceservice.dto.BalanceCheckRequest;


import com.testbank.dbo.balanceservice.dto.BalancePaymentResult;
import com.testbank.dbo.balanceservice.dto.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Service
public class BalanceCheckConsumer {
    private final PaymentProcessorService paymentProcessorService;
    private final BalanceResultProducer balanceResultProducer;

    private final  ObjectMapper objectMapper;
    public BalanceCheckConsumer(PaymentProcessorService paymentProcessorService, BalanceResultProducer balanceResultProducer,
                                ObjectMapper objectMapper) {
        this.paymentProcessorService = paymentProcessorService;
        this.balanceResultProducer = balanceResultProducer;
        this.objectMapper = objectMapper;

    }

    @KafkaListener(topics = "balance-checks", groupId = "balance-service")
    public void checkBalance(String message) {


        try {
            PaymentEvent event = objectMapper.readValue(message, PaymentEvent.class);
            System.out.println("🔍 Получен платеж: payment_id" + event.getPaymentId());
            BalancePaymentResult result = paymentProcessorService.processPayment(event);


            if (result.isSuccess()) {

                balanceResultProducer.sendSuccessWithNotification(result);
            } else {
                balanceResultProducer.sendError(result);
            }
        } catch (Exception e) {
            balanceResultProducer.sendError(1L, "Ошибка: " + e.getMessage());
        }
    }
}
