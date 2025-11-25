package com.testbank.dbo.balanceservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testbank.dbo.balanceservice.dto.BalanceCheckRequest;
import com.testbank.dbo.balanceservice.dto.BalancePaymentResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



@Service
public class BalanceResultProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private BalancePaymentResult result;
    private final ObjectMapper objectMapper;
    public BalanceResultProducer(KafkaTemplate<String, Object> kafkaTemplate,ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper=objectMapper;
    }

    public void sendSuccess(Long paymentId) {
        kafkaTemplate.send("payment-results", "SUCCESS: payment_id-" + paymentId);
        System.out.println("✅ Баланс OK, отправлен успех для платежа: " + paymentId);
    }


    public void sendSuccessWithNotification(BalancePaymentResult result) {


        try {
            String resultJson = objectMapper.writeValueAsString(result);

            // 1. Отправляем успех в payment-results
            kafkaTemplate.send("payment-results", result.getPaymentId().toString(), resultJson);

            // 2. Отправляем уведомление об успешной операции
            String notificationMessage = String.format(
                    "Платеж %d успешно обработан. Счет %s: баланс %s RUB. Счет %s: баланс %s RUB",
                    result.getPaymentId(), result.getFromAccount(), result.getFromBalance(), result.getToAccount(), result.getToBalance()
            );
            kafkaTemplate.send("notify-events", result.getPaymentId().toString(), resultJson);

            System.out.println("✅ Успех + уведомление отправлено: " + notificationMessage);
        } catch (Exception e) {
            System.out.println(" ERROR " + e);
        }

    }

    public void sendErrorBalance(BalancePaymentResult result) {
        sendResult(result, "❌ Недостаточно средств");
    }

    public void sendErrorAccount(BalancePaymentResult result) {
        sendResult(result, "❌ Неверные реквизиты");
    }

    public void sendError(BalancePaymentResult result) {

        if (result.getErrorType()==PaymentErrorType.ACCOUNT_NOT_FOUND) {
            this.sendErrorAccount(result);
        }
         else if (result.getErrorType()==PaymentErrorType.INSUFFICIENT_FUNDS) {
            this.sendErrorBalance(result);
        }
         else {
            sendResult(result, "❌ Ошибка обработки");
        }
    }

    private void sendResult(BalancePaymentResult result, String logMessage) {
        try {
            String resultJson = objectMapper.writeValueAsString(result);
            kafkaTemplate.send("payment-results", result.getPaymentId().toString(), resultJson);
            kafkaTemplate.send("notify-events", result.getPaymentId().toString(), resultJson);
            System.out.println( "payment_id:"+result.getPaymentId()+logMessage);


        } catch (JsonProcessingException e) {
            System.out.println( "payment_id:"+e);
        }
    }



    public void sendError(Long paymentId, String error) {
        kafkaTemplate.send("notify-events", "ERROR: payment_id-" + paymentId + ":" + error);
        System.out.println(error + " для платежа paymentId: " + paymentId);
    }

}
