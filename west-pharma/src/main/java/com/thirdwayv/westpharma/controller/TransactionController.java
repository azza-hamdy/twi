package com.thirdwayv.westpharma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.service.api.BlockChainService;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionController {

	@Autowired
	private BlockChainService blockChainService;

	@PostMapping("/save")
	public TransactionDTO saveTransaction(@RequestBody TransactionDTO tx) throws Exception {
		tx = blockChainService.saveTransaction(tx);
		return tx;
	}
}
