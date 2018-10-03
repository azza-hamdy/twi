package com.thirdwayv.westpharma.exception;

public class TransactionIsNotExistException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransactionIsNotExistException(String msg) {
		super(msg);
	}

}
