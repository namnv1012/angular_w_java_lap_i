package com.laptop.shopping.repository.iRepository;

import com.laptop.shopping.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ITransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByTransactionBillIgnoreCase(String transactionBill);

    Optional<TransactionEntity> findByTransactionCodeIgnoreCase(String transactionCode);
}
