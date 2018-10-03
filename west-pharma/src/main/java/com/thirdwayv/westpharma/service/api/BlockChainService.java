package com.thirdwayv.westpharma.service.api;

import com.thirdwayv.westpharma.dto.TransactionDTO;

public interface BlockChainService {
	TransactionDTO saveTransaction(TransactionDTO tx) throws Exception;
}
