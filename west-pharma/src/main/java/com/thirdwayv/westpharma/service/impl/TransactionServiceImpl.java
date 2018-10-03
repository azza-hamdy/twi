package com.thirdwayv.westpharma.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.westpharma.converter.TransactionConverter;
import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.InvalidInputException;
import com.thirdwayv.westpharma.exception.TransactionIsNotExistException;
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

	@Autowired
	private TransactionConverter converter;

	public Transaction save(Transaction tx) {
		return repo.save(tx);
	}

	public void validate(TransactionDTO txDTO) throws InvalidInputException {
		validator.validate(txDTO);
	}

	@Override
	public List<TransactionDTO> getTransactionByHash(String hash) throws TransactionIsNotExistException {
		List<Transaction> transactions = repo.findByHash(hash);
		if (transactions == null || transactions.isEmpty()) {
			throw new TransactionIsNotExistException("There is no transactions with this hash: " + hash);
		}
		return convertTransactionsToDTOs(transactions);
	}

	@Override
	public List<TransactionDTO> getTransactionByTagId(String tagId) throws TransactionIsNotExistException {
		List<Transaction> transactions = repo.findByTagId(tagId);
		if (transactions == null || transactions.isEmpty()) {
			throw new TransactionIsNotExistException("There is no transactions with this tag ID: " + tagId);
		}

		return convertTransactionsToDTOs(transactions);
	}

	@Override
	public List<TransactionDTO> getTransactionByWriterId(String writerId) throws TransactionIsNotExistException {
		List<Transaction> transactions = repo.findByWriterId(writerId);
		if (transactions == null || transactions.isEmpty()) {
			throw new TransactionIsNotExistException("There is no transactions with this writer ID: " + writerId);
		}

		return convertTransactionsToDTOs(transactions);
	}

	private List<TransactionDTO> convertTransactionsToDTOs(List<Transaction> transactions) {
		List<TransactionDTO> txDTOs = new ArrayList<>();
		for (Transaction tx : transactions) {
			txDTOs.add(converter.toDTO(tx));
		}

		return txDTOs;
	}

}
