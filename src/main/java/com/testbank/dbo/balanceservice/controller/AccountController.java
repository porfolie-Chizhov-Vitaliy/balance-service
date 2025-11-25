package com.testbank.dbo.balanceservice.controller;


import com.testbank.dbo.balanceservice.dto.AccountRequest;
import com.testbank.dbo.balanceservice.dto.AccountResponse;
import com.testbank.dbo.balanceservice.entity.AccountEntity;
import com.testbank.dbo.balanceservice.repository.AccountRepository;
import com.testbank.dbo.balanceservice.service.AccountBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountBalanceService accountBalanceService;
    @PostMapping
    public AccountResponse createAccount(@RequestBody AccountRequest request) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(request.getAccountNumber());

        if (request.getBalance() != null) {
            accountEntity.setBalance(BigDecimal.valueOf(request.getBalance()));
        }

        accountEntity.setCurrency(request.getCurrency());

        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return convertToResponse(savedAccount);
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/by-number/{id}")
    public AccountResponse getAccountByNumber(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(this::convertToResponse)
                .orElse(null);
    }

    @GetMapping("/{id}/balance")
    public BigDecimal getBalance(@PathVariable Long id) {
        return accountBalanceService.getAccountBalance(id);

//        return accountRepository.findById(id)
//                .map(AccountEntity::getBalance)
//                .orElse(null);
    }

    private AccountResponse convertToResponse(AccountEntity account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setCreatedAt(account.getCreatedAt());
        response.setLastUpdated(account.getLastUpdated());
        return response;
    }
}