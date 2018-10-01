package com.thirdwayv.westpharma.service.api;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.BlockChainException;

public interface BlockChainService {
	TransactionDTO saveTransaction(TransactionDTO tx) throws BlockChainException;
}
