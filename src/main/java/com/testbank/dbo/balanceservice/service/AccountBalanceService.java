package com.testbank.dbo.balanceservice.service;

import com.testbank.dbo.balanceservice.entity.AccountEntity;
import com.testbank.dbo.balanceservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
@Service
public class AccountBalanceService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RedisTemplate<String, BigDecimal> redisTemplate;

    public BigDecimal getAccountBalance(Long accountId) {
        // 1. Пробуем получить из кэша
        BigDecimal cachedBalance = getCachedBalance(accountId);
        if (cachedBalance != null) {
            System.out.println("✅ Баланс из кэша для accountId: {} - {}"+accountId+"баланс:" +cachedBalance);
            return cachedBalance;
        }

        // 2. Если нет в кэше - ищем в БД
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Счет не найден"));

        // 3. Сохраняем в кэш
        cacheBalance(accountId, account.getBalance());
        System.out.println("💾 Баланс из БД и сохранен в кэш: {} - {}"+accountId+"баланс:" +account.getBalance() );

        return account.getBalance();
    }

    private BigDecimal getCachedBalance(Long accountId) {
        try {
            return redisTemplate.opsForValue().get("balance:account:" + accountId);
        } catch (Exception e) {
            System.out.println("❌ Ошибка чтения из кэша: {}"+e.getMessage());
            return null;
        }
    }

    private void cacheBalance(Long accountId, BigDecimal balance) {
        try {
            redisTemplate.opsForValue().set(
                    "balance:account:" + accountId,
                    balance,
                    Duration.ofMinutes(5)
            );
        } catch (Exception e) {
            System.out.println("❌ Ошибка записи в кэш: {}"+e.getMessage());
        }
    }

}
