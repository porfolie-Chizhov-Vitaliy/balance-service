package com.testbank.dbo.balanceservice.repository;


import com.testbank.dbo.balanceservice.entity.AccountEntity;
import com.testbank.dbo.balanceservice.entity.BalanceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistoryEntity, Long> {
    List<BalanceHistoryEntity> findByAccountId(Long accountId);

    List<BalanceHistoryEntity> findByPaymentId(Long paymentId);
    List<BalanceHistoryEntity> findByChangeType(String changeType);

}
