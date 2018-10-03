package com.thirdwayv.westpharma.service.api;

import java.util.List;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.TransactionIsNotExistException;

public interface TransactionService {

	List<TransactionDTO> getTransactionByHash(String hash) throws TransactionIsNotExistException;

	List<TransactionDTO> getTransactionByTagId(String tagId) throws TransactionIsNotExistException;

	List<TransactionDTO> getTransactionByWriterId(String writerId) throws TransactionIsNotExistException;

	List<TransactionDTO> getTransactionWithinSpecificPeriod(String from, String to);
}
