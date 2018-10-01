package com.thirdwayv.westpharma.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdwayv.westpharma.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long>{

}
