package com.thirdwayv.westpharma.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.InvalidInputException;
import com.thirdwayv.westpharma.model.Transaction;
import com.thirdwayv.westpharma.repo.TransactionRepo;
import com.thirdwayv.westpharma.service.api.TransactionService;
import com.thirdwayv.westpharma.service.validator.TransactionValidator;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepo repo;

	@Autowired
	private TransactionValidator validator;

	@Override
	public Transaction save(Transaction tx) {
		return repo.save(tx);
	}

	@Override
	public void validate(TransactionDTO txDTO) throws InvalidInputException {
		validator.validate(txDTO);
	}

}
