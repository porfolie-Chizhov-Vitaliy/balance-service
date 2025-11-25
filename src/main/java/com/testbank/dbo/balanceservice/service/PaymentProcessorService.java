package com.testbank.dbo.balanceservice.service;

import com.testbank.dbo.balanceservice.dto.BalancePaymentResult;
import com.testbank.dbo.balanceservice.dto.PaymentEvent;
import com.testbank.dbo.balanceservice.entity.AccountEntity;
import com.testbank.dbo.balanceservice.entity.BalanceHistoryEntity;
import com.testbank.dbo.balanceservice.repository.AccountRepository;
import com.testbank.dbo.balanceservice.repository.BalanceHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PaymentProcessorService {
    private final AccountRepository accountRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    public PaymentProcessorService(AccountRepository accountRepository, BalanceHistoryRepository balanceHistoryRepository) {
        this.accountRepository = accountRepository;
        this.balanceHistoryRepository = balanceHistoryRepository;
    }

    public BalancePaymentResult processPayment(PaymentEvent request) {
        // Проверка счетов
        Optional<AccountEntity> fromAccountOpt = accountRepository.findByAccountNumber(request.getFromAccount());
        if (fromAccountOpt.isEmpty())
            return BalancePaymentResult.error(request.getPaymentId(),PaymentErrorType.ACCOUNT_NOT_FOUND, "Счет отправителя не найден:",request.getAmount());


        //"ERROR:" + PaymentErrorType.ACCOUNT_NOT_FOUND + ": Счет отправителя не найден: " + request.getFromAccount();

        Optional<AccountEntity> toAccountOpt = accountRepository.findByAccountNumber(request.getToAccount());
        if (toAccountOpt.isEmpty())
            return BalancePaymentResult.error(request.getPaymentId(),PaymentErrorType.ACCOUNT_NOT_FOUND, "Счет получателя не найден: ",request.getAmount());

        //"ERROR: " + PaymentErrorType.ACCOUNT_NOT_FOUND + ": Счет получателя не найден: " + request.getToAccount();

        AccountEntity fromAccount = fromAccountOpt.get();
        AccountEntity toAccount = toAccountOpt.get();

        // Проверка баланса
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            return BalancePaymentResult.error(request.getPaymentId(),PaymentErrorType.INSUFFICIENT_FUNDS, " Недостаточно средств", fromAccount.getAccountNumber(), fromAccount.getBalance());
            //"ERROR:" + PaymentErrorType.INSUFFICIENT_FUNDS + " Недостаточно средств. Доступно: " + fromAccount.getBalance();
        }

        // Списание и зачисление
        BigDecimal fromOldBalance = fromAccount.getBalance();
        BigDecimal fromNewBalance = fromOldBalance.subtract(request.getAmount());
        fromAccount.setBalance(fromNewBalance);

        BigDecimal toOldBalance = toAccount.getBalance();
        BigDecimal toNewBalance = toOldBalance.add(request.getAmount());
        toAccount.setBalance(toNewBalance);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // История
        saveBalanceHistory(fromAccount, fromOldBalance, fromNewBalance, request.getAmount().negate(), "DEBIT", request.getPaymentId());
        saveBalanceHistory(toAccount, toOldBalance, toNewBalance, request.getAmount(), "CREDIT", request.getPaymentId());

        return BalancePaymentResult.success(request.getPaymentId(),fromAccount.getAccountNumber(),fromNewBalance,toAccount.getAccountNumber(),toNewBalance,request.getAmount());
                //"SUCCESS:" + fromAccount.getAccountNumber() + ":" + fromNewBalance + ":" + toAccount.getAccountNumber() + ":" + toNewBalance;
    }


    private void saveBalanceHistory(AccountEntity account, BigDecimal oldBalance, BigDecimal newBalance,
                                    BigDecimal changeAmount, String changeType, Long paymentId) {
        BalanceHistoryEntity history = new BalanceHistoryEntity();
        history.setAccount(account);
        history.setOldBalance(oldBalance);
        history.setNewBalance(newBalance);
        history.setChangeAmount(changeAmount);
        history.setChangeType(changeType);
        history.setPaymentId(paymentId);
        history.setCreatedAt(LocalDateTime.now());
        balanceHistoryRepository.save(history);
    }
}
