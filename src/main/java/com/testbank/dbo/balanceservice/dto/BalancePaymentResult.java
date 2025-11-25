package com.testbank.dbo.balanceservice.dto;

import com.testbank.dbo.balanceservice.service.PaymentErrorType;

import java.math.BigDecimal;

public class BalancePaymentResult {
    private String status;
    private Long paymentId;
    private String message;
    private PaymentErrorType errorType;
    private boolean success;
    private BigDecimal amount;
    private String fromAccount;
    private BigDecimal fromBalance;
    private String toAccount;
    private BigDecimal toBalance;

    public BalancePaymentResult(String status, Long paymentId, String message) {
        this.status = status;
        this.paymentId = paymentId;
        this.message = message;

    }

    // ✅ Фабричный метод для УСПЕХА
    public static BalancePaymentResult success(Long paymentId,String fromAccount, BigDecimal fromBalance,
                                               String toAccount, BigDecimal toBalance, BigDecimal amount) {
        BalancePaymentResult result = new BalancePaymentResult();
        result.paymentId=paymentId;
        result.success = true;
        result.status="SUCCESS";
        result.errorType = null;
        result.message = "Платеж успешно обработан";
        result.fromAccount = fromAccount;
        result.fromBalance = fromBalance;
        result.toAccount = toAccount;
        result.toBalance = toBalance;
        result.amount =amount;
        return result;
    }

    // ❌ Фабричный метод для ОШИБКИ
    public static BalancePaymentResult error(Long paymentId,PaymentErrorType errorType, String message, BigDecimal amount) {
        BalancePaymentResult result = new BalancePaymentResult();
        result.paymentId=paymentId;
        result.success = false;
        result.status="FAILED";
        result.errorType = errorType;
        result.message = message;
        result.fromAccount = null;
        result.fromBalance = null;
        result.toAccount = null;
        result.toBalance = null;
        result.amount =amount;
        return result;
    }


    public static BalancePaymentResult error(Long paymentId,PaymentErrorType errorType, String message, String fromAccount, BigDecimal fromBalance) {
        BalancePaymentResult result = new BalancePaymentResult();
        result.paymentId=paymentId;
        result.success = false;
        result.status="FAILED";
        result.errorType = errorType;
        result.message = message;
        result.fromAccount = fromAccount;
        result.fromBalance = fromBalance;
        result.toAccount = null;
        result.toBalance = null;
        return result;
    }

    public BalancePaymentResult() {
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public BigDecimal getFromBalance() {
        return fromBalance;
    }

    public void setFromBalance(BigDecimal fromBalance) {
        this.fromBalance = fromBalance;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getToBalance() {
        return toBalance;
    }

    public void setToBalance(BigDecimal toBalance) {
        this.toBalance = toBalance;
    }

    public PaymentErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(PaymentErrorType errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
