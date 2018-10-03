package com.thirdwayv.westpharma.service.validator;

import org.springframework.stereotype.Component;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.InvalidInputException;

@Component
public class TransactionValidator {

	public void validate(TransactionDTO txDTO) throws InvalidInputException {

		if (txDTO.getHash() == null || txDTO.getHash().isEmpty()) {
			if (txDTO.getTransactionJson() == null || txDTO.getTransactionJson().isEmpty()) {
				throw new InvalidInputException("Send Transaction JSON OR Transactio Hash");
			}
		}

		if (txDTO.getTime() == null) {
			throw new InvalidInputException("Transaction Time is Mandatory");
		}
	}

}
