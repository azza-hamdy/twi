package com.thirdwayv.westpharma.repo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thirdwayv.westpharma.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	List<Transaction> findByHash(String hash);

	List<Transaction> findByTagId(String tagId);

	List<Transaction> findByWriterId(String writerId);

	List<Transaction> findByCreationTimeBetween(Timestamp from, Timestamp to);

	List<Transaction> findByBlockBlockNumber(Long blockNumber);
	
	List<Transaction> findBySystemId(Integer systemId);
	
	Optional<Transaction> findByBlockBlockNumberAndIndex(Long blockNumber, Integer transactionIndex);

}
