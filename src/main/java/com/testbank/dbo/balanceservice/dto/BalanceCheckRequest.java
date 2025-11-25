package com.testbank.dbo.balanceservice.dto;

import java.math.BigDecimal;

public class BalanceCheckRequest {
    private Long paymentId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String currency;
    private  PaymentEvent paymentEvent;

    public BalanceCheckRequest(Long paymentId, String fromAccount, String toAccount, BigDecimal amount, String currency) {
        this.paymentId = paymentId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
    }

    public BalanceCheckRequest(PaymentEvent paymentEvent) {
        this.paymentEvent = paymentEvent;

    }

    public BalanceCheckRequest() {
    }


    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
