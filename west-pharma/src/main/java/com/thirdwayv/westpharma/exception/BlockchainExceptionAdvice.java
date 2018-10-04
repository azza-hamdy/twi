package com.thirdwayv.westpharma.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

	@ResponseBody
	@ExceptionHandler(TransactionNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleTransactionNotFoundException(TransactionNotFoundException ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(BlockNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleBlockNotFoundException(BlockNotFoundException ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleRestParamsIncorrectType(MethodArgumentTypeMismatchException ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return "NOT FOUND";
	}

	@ResponseBody
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleRestMissingParams(MissingServletRequestParameterException ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return "NOT FOUND";
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleUnhandledException(Exception ex) {
		logger.info(ex.getMessage(), ex.getCause());
		return "Internal Server Error";
	}

}
