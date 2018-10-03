package com.thirdwayv.westpharma.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdwayv.westpharma.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	List<Transaction> findByHash(String hash);

	List<Transaction> findByTagId(String tagId);

	List<Transaction> findByWriterId(String writerId);

}
