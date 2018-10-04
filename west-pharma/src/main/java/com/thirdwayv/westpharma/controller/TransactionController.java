package com.thirdwayv.westpharma.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.InvalidInputException;
import com.thirdwayv.westpharma.service.api.BlockChainService;
import com.thirdwayv.westpharma.service.api.TransactionService;
import com.thirdwayv.westpharma.service.validator.TransactionValidator;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {

	@Autowired
	private BlockChainService blockChainService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private TransactionValidator validator;

	
	@PostMapping("/save")
	public TransactionDTO saveTransaction(@RequestBody TransactionDTO tx) throws Exception {
		tx = blockChainService.saveTransaction(tx);
		return tx;
	}

	@GetMapping("/readbyhash")
	public List<TransactionDTO> getTransactionByHash(@RequestParam("hash") String hash) throws Exception {
		validator.validateInputString(hash);
		return transactionService.getTransactionByHash(hash);
	}

	@GetMapping("/readbytag")
	public List<TransactionDTO> getTransactionByTagId(@RequestParam("tag") String tagId) throws Exception {
		validator.validateInputString(tagId);
		return transactionService.getTransactionByTagId(tagId);
	}

	@GetMapping("/readbywriter")
	public List<TransactionDTO> getTransactionByWriterd(@RequestParam("writer") String writerId) throws Exception {
		validator.validateInputString(writerId);
		return transactionService.getTransactionByWriterId(writerId);
	}

	@GetMapping("/readwithintime")
	public List<TransactionDTO> getTransactionByCreationTimeBetween(@RequestParam("from") Long from,
			@RequestParam("to") Long to) throws Exception {
		validator.validateDates(from, to);
		return transactionService.getTransactionWithinSpecificPeriod(from, to);
	}

	@GetMapping("/readtransaction")
	public TransactionDTO getTransactionDetails(@RequestParam("blocknumber") Long blockNumber,
			@RequestParam("transactionindex") Integer transactionIndex) throws Exception {
		validator.validateInputNumber(blockNumber);
		return transactionService.getTransactionDetails(blockNumber, transactionIndex);

	}
	
	@GetMapping("/readhashes")
	public List<TransactionDTO> readBlockHashes(@RequestParam("blocknumber") Long blockNumber) throws Exception {
		validator.validateInputNumber(blockNumber);
		return transactionService.getBlockTransactionSignatures(blockNumber);
	}

}
