package com.thirdwayv.westpharma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thirdwayv.westpharma.converter.TransactionConverter;
import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.model.Block;
import com.thirdwayv.westpharma.model.Transaction;
import com.thirdwayv.westpharma.service.api.BlockChainService;
import com.thirdwayv.westpharma.service.api.BlockService;
import com.thirdwayv.westpharma.service.api.TransactionService;
import com.thirdwayv.westpharma.util.BlockConcurrancyManager;
import com.thirdwayv.westpharma.util.SystemConfig;

@Service
public class BlockChainServiceImpl implements BlockChainService {

	private static final Logger logger = LoggerFactory.getLogger(BlockChainServiceImpl.class);

	@Autowired
	private BlockService blockService;

	@Autowired
	private TransactionService treansactionService;

	@Autowired
	private SystemConfig sysConfig;

	@Autowired
	private TransactionConverter transactionConverter;

	// TODO: blocking issue
	@Transactional
	public TransactionDTO saveTransaction(TransactionDTO transactionDTO) throws Exception {
		getTransactionService().validate(transactionDTO);
		while (true) {
			logger.debug("trying acquiring service lock...");
			if (BlockConcurrancyManager.lock()) {
				logger.debug("Acquireing service lock successfully");
				
				Block latestBlock = blockService.getLatestBlock();
				saveTransaction(transactionDTO, latestBlock);
				updateBlock(latestBlock);
				
				BlockConcurrancyManager.unlock();
				logger.debug("Releasing service lock successfully");
				break;
			} else {
				logger.debug("Can't acquire service lock");
			}
		}
		return transactionDTO;
	}

	private void updateBlock(Block latestBlock) throws BlockChainException {
		latestBlock.setTransactionsNumber(latestBlock.getTransactionsNumber() + 1);
		latestBlock = blockService.save(latestBlock);

		if (isBlockClosed(latestBlock)) {
			blockService.updateBlockchain(latestBlock);
		}
	}

	private void saveTransaction(TransactionDTO transactionDTO, Block latestBlock) throws BlockChainException {
		Transaction txEntity = buildTransactionEntity(transactionDTO, latestBlock);
		txEntity = getTransactionService().save(txEntity);
		transactionDTO.setHash(txEntity.getSignature());
		transactionDTO.setBlockNumber(latestBlock.getBlockNumber());
	}

	private boolean isBlockClosed(Block block) {
		return block.getTransactionsNumber() == sysConfig.getTransctionNumInBlock();
	}

	private Transaction buildTransactionEntity(TransactionDTO transactionDTO, Block block) throws BlockChainException {
		Transaction tx = transactionConverter.toEntity(transactionDTO);
		tx.setBlock(block);
		tx.setIndex(block.getTransactionsNumber() + 1);
		return tx;
	}

	private TransactionServiceImpl getTransactionService() {
		return (TransactionServiceImpl) treansactionService;
	}
}
