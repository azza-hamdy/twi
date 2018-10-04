package com.thirdwayv.westpharma.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.westpharma.converter.TransactionConverter;
import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.BlockNotFoundException;
import com.thirdwayv.westpharma.exception.InvalidInputException;
import com.thirdwayv.westpharma.exception.TransactionNotFoundException;
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
	public List<TransactionDTO> getTransactionByHash(String hash) throws TransactionNotFoundException {
		List<Transaction> transactions = repo.findByHash(hash);
		if (transactions == null || transactions.isEmpty()) {
			throw new TransactionNotFoundException("There is no transactions with this hash: " + hash);
		}
		return convertTransactionsToDTOs(transactions);
	}

	@Override
	public List<TransactionDTO> getTransactionByTagId(String tagId) throws TransactionNotFoundException {
		List<Transaction> transactions = repo.findByTagId(tagId);
		if (transactions == null || transactions.isEmpty()) {
			throw new TransactionNotFoundException("There is no transactions with this tag ID: " + tagId);
		}

		return convertTransactionsToDTOs(transactions);
	}

	@Override
	public List<TransactionDTO> getTransactionByWriterId(String writerId) throws TransactionNotFoundException {
		List<Transaction> transactions = repo.findByWriterId(writerId);
		if (transactions == null || transactions.isEmpty()) {
			throw new TransactionNotFoundException("There is no transactions with this writer ID: " + writerId);
		}

		return convertTransactionsToDTOs(transactions);
	}

	@Override
	public List<TransactionDTO> getTransactionWithinSpecificPeriod(Long from, Long to) {
		Timestamp startDate = new Timestamp(from);
		Timestamp endDate = new Timestamp(to);
		return convertTransactionsToDTOs(repo.findByCreationTimeBetween(startDate, endDate));
	}

	@Override
	public TransactionDTO getTransactionDetails(Long blockNumber, Integer transactionIndex)
			throws TransactionNotFoundException {
		Optional<Transaction> optionalTx = repo.findByBlockBlockNumberAndIndex(blockNumber, transactionIndex);
		if (!optionalTx.isPresent()) {
			throw new TransactionNotFoundException(
					"There is no transactions in the block number: " + blockNumber + " at index: " + transactionIndex);
		}
		return converter.toDTO(optionalTx.get());
	}

	@Override
	public List<TransactionDTO> getBlockTransactionSignatures(Long blockNumber) throws BlockNotFoundException {
		List<Transaction> transactions = repo.findByBlockBlockNumber(blockNumber);
		if (transactions == null || transactions.isEmpty()) {
			throw new BlockNotFoundException("There is no Block with this number: " + blockNumber);
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
