package com.thirdwayv.westpharma.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.westpharma.model.Transaction;
import com.thirdwayv.westpharma.repo.TransactionRepo;
import com.thirdwayv.westpharma.service.api.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepo repo;

	@Override
	public Transaction save(Transaction tx) {
		return repo.save(tx);
	}

}
