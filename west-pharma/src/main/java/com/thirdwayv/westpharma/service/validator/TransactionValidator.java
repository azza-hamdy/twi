package com.thirdwayv.westpharma.service.validator;

import java.sql.Timestamp;

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

	public void validateDates(Long from, Long to) throws InvalidInputException {
		if (from == null) {
			throw new InvalidInputException("From Date is Required");
		}
		if (to == null) {
			throw new InvalidInputException("To Date is Requied");

		}
		if (from <= 0 || to <= 0) {
			throw new InvalidInputException("Dates values must be greater that zero");
		}
		Timestamp startDate = new Timestamp(from);
		Timestamp endDate = new Timestamp(to);

		if (!startDate.before(endDate)) {
			throw new InvalidInputException("From Date must be less than To Date");
		}
	}

	public void validateInputString(String input) throws InvalidInputException {
		// if(!input.matches("([a-zA-Z]|[0-9])+"))
		if (input != null && input.isEmpty()) {
			throw new InvalidInputException("Input must not be Empty");
		}

	}

	public void validateInputNumber(Long input) throws InvalidInputException {
		if (input == null) {
			throw new InvalidInputException("Input is Required");
		}

		if (input < 0) {
			throw new InvalidInputException("Input must be Positive");
		}

	}

}
