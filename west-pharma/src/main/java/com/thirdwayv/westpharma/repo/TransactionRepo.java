package com.thirdwayv.westpharma.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thirdwayv.westpharma.model.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long>{

}
