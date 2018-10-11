package com.thirdwayv.westpharma.service.api;

import java.util.List;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.BlockNotFoundException;
import com.thirdwayv.westpharma.exception.TransactionNotFoundException;

public interface TransactionService {

	List<TransactionDTO> getTransactionByHash(String hash) throws TransactionNotFoundException;

	List<TransactionDTO> getTransactionByTagId(String tagId) throws TransactionNotFoundException;

	List<TransactionDTO> getTransactionByWriterId(String writerId) throws TransactionNotFoundException;

	List<TransactionDTO> getTransactionBySystemId(Integer systemId) throws TransactionNotFoundException;
	
	List<TransactionDTO> getTransactionWithinSpecificPeriod(Long from, Long to);

	TransactionDTO getTransactionDetails(Long blockNumber, Integer transactionIndex)
			throws TransactionNotFoundException;

	List<TransactionDTO> getBlockTransactionSignatures(Long blockNumber) throws BlockNotFoundException;
}
