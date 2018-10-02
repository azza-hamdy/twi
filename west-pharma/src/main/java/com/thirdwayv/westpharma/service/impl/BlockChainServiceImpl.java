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
import com.thirdwayv.westpharma.util.HashingUtils;

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
		transactionDTO.setHash(txEntity.getSignature());

		if (isBlockClosed(latestBlock)) {
			blockService.updateBlockchain(latestBlock);
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
		tx.setCreationTime(transactionDTO.getTime() != null ? new Timestamp(transactionDTO.getTime()) : null);
		tx.setLength(transactionDTO.getTransactionJson() != null ? transactionDTO.getTransactionJson().length() : null);
		try {
			if (transactionDTO.getHash() == null) {
				tx.setTransaction(new PGobject());
				tx.getTransaction().setValue(transactionDTO.getTransactionJson());

				tx.setHash(HashingUtils.getHashBySHA256(transactionDTO.getTransactionJson()));
				tx.setSignature(HashingUtils.getHashBySHA256(tx.getWriterId(), tx.getTagId(),
						tx.getCreationTime().getTime(), tx.getLength(), tx.getHash()));
			} else {
				tx.setHash(transactionDTO.getHash());
				tx.setSignature(HashingUtils.getHashBySHA256(tx.getHash()));
			}

		} catch (SQLException e) {
			throw new BlockChainException("Can't convert transaction json to PGObject", e);
		} catch (NoSuchAlgorithmException e) {
			throw new BlockChainException("Can't Hash transaction json", e);
		}

		return tx;
	}
}
