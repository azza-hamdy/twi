package com.thirdwayv.westpharma.service.impl;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.model.Block;
import com.thirdwayv.westpharma.model.Transaction;
import com.thirdwayv.westpharma.service.api.BlockChainService;
import com.thirdwayv.westpharma.service.api.BlockService;
import com.thirdwayv.westpharma.service.api.TransactionService;
import com.thirdwayv.westpharma.util.SystemConfig;
import com.thirdwayv.westpharma.util.Utils;

@Service
public class BlockChainServiceImpl implements BlockChainService {

	@Autowired
	private BlockService blockService;

	@Autowired
	private TransactionService txService;

	@Autowired
	private SystemConfig sysConfig;

	// TODO: blocking issue
	@Transactional
	public synchronized TransactionDTO saveTransaction(TransactionDTO transactionDTO) throws BlockChainException {
		Block latestBlock = blockService.getLatestBlock();

		Transaction txEntity = buildTransactionEntity(transactionDTO, latestBlock);
		txEntity = txService.save(txEntity);

		latestBlock.setTransactionsNumber(latestBlock.getTransactionsNumber() + 1);
		latestBlock = blockService.save(latestBlock);

		transactionDTO.setBlockId(latestBlock.getId());

		if (isBlockClosed(latestBlock)) {
//			formBlock();
//			openNewBlock();
//			updateBlockchain();
		}
		return transactionDTO;
	}

	private boolean isBlockClosed(Block block) {
		return block.getTransactionsNumber() == sysConfig.getTransctionNumInBlock();
	}

	private Transaction buildTransactionEntity(TransactionDTO transactionDTO, Block block) throws BlockChainException {
		Transaction tx = new Transaction();
		tx.setBlock(block);
		tx.setWriterId(transactionDTO.getWriterId());
		tx.setTagId(transactionDTO.getTagId());
		tx.setCreationTime(new Timestamp(transactionDTO.getTime()));
		tx.setLength(transactionDTO.getTransactionJson().length());
		try {
			tx.setTransaction(new PGobject());
			tx.getTransaction().setValue(transactionDTO.getTransactionJson());
			tx.setHash(Utils.getHashBySHA256(transactionDTO.getTransactionJson()));
			tx.setSignature(Utils.getHashBySHA256(tx.getWriterId(), tx.getTagId(),
					String.valueOf(tx.getCreationTime().getTime()), String.valueOf(tx.getLength()), tx.getHash()));
		} catch (SQLException e) {
			throw new BlockChainException("Can't convert transaction json to PGObject", e);
		} catch (NoSuchAlgorithmException e) {
			throw new BlockChainException("Can't Hash transaction json", e);
		}

		return tx;
	}
}
