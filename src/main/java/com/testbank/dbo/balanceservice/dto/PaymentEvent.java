package com.testbank.dbo.balanceservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PaymentEvent {
    private Long paymentId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String currency;
    public PaymentEvent() {

    }

    @JsonCreator
    public PaymentEvent(@JsonProperty("paymentId") Long paymentId,
                        @JsonProperty("fromAccount") String fromAccount,
                        @JsonProperty("toAccount") String toAccount,
                        @JsonProperty("amount") BigDecimal amount,
                        @JsonProperty("currency") String currency) {
        this.paymentId = paymentId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "PaymentEvent{" +
                "paymentId=" + paymentId +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }



}
