package com.thirdwayv.westpharma.service.api;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.InvalidInputException;
import com.thirdwayv.westpharma.model.Transaction;

public interface TransactionService {

	Transaction save(Transaction tx);

	void validate(TransactionDTO txDTO) throws InvalidInputException;

}
