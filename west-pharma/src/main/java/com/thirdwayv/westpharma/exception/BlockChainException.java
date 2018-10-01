package com.thirdwayv.westpharma.exception;

public class BlockChainException extends Exception {

	private static final long serialVersionUID = 1L;

	public BlockChainException() {
	}

	public BlockChainException(String msg) {
		super(msg);
	}

	public BlockChainException(String msg, Exception e) {
		super(msg, e);
	}

}
