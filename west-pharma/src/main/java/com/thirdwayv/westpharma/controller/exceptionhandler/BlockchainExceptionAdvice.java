package com.thirdwayv.westpharma.controller.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.exception.InvalidInputException;

@ControllerAdvice
public class BlockchainExceptionAdvice {

	private static Logger logger = LoggerFactory.getLogger(BlockchainExceptionAdvice.class);

	@ResponseBody
	@ExceptionHandler(BlockChainException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleBlockchainException(BlockChainException ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(InvalidInputException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleInvalidInputException(InvalidInputException ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return ex.getMessage();
	}

}
