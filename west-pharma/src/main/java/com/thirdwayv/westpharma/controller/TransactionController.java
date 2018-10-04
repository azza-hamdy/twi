package com.thirdwayv.westpharma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.service.api.BlockChainService;
import com.thirdwayv.westpharma.service.api.TransactionService;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {

	@Autowired
	private BlockChainService blockChainService;

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/save")
	public TransactionDTO saveTransaction(@RequestBody TransactionDTO tx) throws Exception {
		tx = blockChainService.saveTransaction(tx);
		return tx;
	}

	@GetMapping("/readbyhash")
	public List<TransactionDTO> getTransactionByHash(@RequestParam("hash") String hash) throws Exception {
		return transactionService.getTransactionByHash(hash);
	}

	@GetMapping("/readbytag")
	public List<TransactionDTO> getTransactionByTagId(@RequestParam("tag") String tagId) throws Exception {
		return transactionService.getTransactionByTagId(tagId);
	}

	@GetMapping("/readbywriter")
	public List<TransactionDTO> getTransactionByWriterd(@RequestParam("writer") String writerId) throws Exception {
		return transactionService.getTransactionByWriterId(writerId);
	}

	@GetMapping("/readwithintime")
	public List<TransactionDTO> getTransactionByCreationTimeBetween(@RequestParam("from") String from,
			@RequestParam("to") String to) {
		return transactionService.getTransactionWithinSpecificPeriod(from, to);
	}

	@GetMapping("/readtransaction")
	public TransactionDTO getTransactionDetails(@RequestParam("blocknumber") Long blockNumber,
			@RequestParam("transactionindex") Integer transactionIndex) throws Exception {
		return transactionService.getTransactionDetails(blockNumber, transactionIndex);

	}
	
	@GetMapping("/readhashes")
	public List<TransactionDTO> readBlockHashes(@RequestParam("blocknumber") Long blockNumber) throws Exception {
		return transactionService.getBlockTransactionSignatures(blockNumber);
	}

}
