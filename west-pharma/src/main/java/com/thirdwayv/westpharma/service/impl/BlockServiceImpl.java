package com.thirdwayv.westpharma.service.impl;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thirdwayv.westpharma.converter.BlockConverter;
import com.thirdwayv.westpharma.dto.BlockHeaderDTO;
import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.exception.BlockNotFoundException;
import com.thirdwayv.westpharma.model.Block;
import com.thirdwayv.westpharma.model.Transaction;
import com.thirdwayv.westpharma.repo.BlockRepo;
import com.thirdwayv.westpharma.service.api.BlockService;
import com.thirdwayv.westpharma.util.BlockConcurrancyManager;
import com.thirdwayv.westpharma.util.HashingUtils;

@Service
public class BlockServiceImpl implements BlockService {

	private static final Logger logger = LoggerFactory.getLogger(BlockServiceImpl.class);

//	private static final int MERKAL_TREE_BLOCK_VERSION = 1;
	private static final int SEQUANCIAL_BLOCK_VERSION = 0;

	@Autowired
	private BlockRepo repo;

	@Autowired
	private BlockConverter converter;

	@Override
	public Block getLatestBlock() throws BlockChainException {
		Block block = repo.findBySignatureIsNull();
		if (block == null) {
			throw new BlockChainException("Block Must not be Null");
		}
		return block;
	}

	@Override
	public Block save(Block block) {
		return repo.save(block);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void updateBlockchainPeriodically() throws BlockChainException {
		logger.debug("try to form the latest block at: " + new Date().toString());
		while (true) {
			logger.debug("trying acquiring service lock...");
			if (BlockConcurrancyManager.lock()) {
				logger.debug("Acquireing service lock successfully");
				Block latestBlock = getLatestBlock();
				if (latestBlock.getTransactionsNumber() != 0) {
					updateBlockchain(latestBlock);
					logger.debug("successfully, form block#" + latestBlock.getBlockNumber());
				} else {
					logger.debug("Can't form the block#" + latestBlock.getBlockNumber() + " as it is empty");
				}
				BlockConcurrancyManager.unlock();
				logger.debug("Releasing service lock successfully");
				break;
			} else {
				logger.debug("Can't acquire service lock");
			}
		}
		BlockConcurrancyManager.unlock();
	}

	@Override
	public void updateBlockchain(Block latestBlock) throws BlockChainException {
		formLatestBlock(latestBlock);
		latestBlock = save(latestBlock);

		Block newBlock = initNewBlock(latestBlock);
		save(newBlock);
	}

	@Override
	public BlockHeaderDTO getBlockHeaderByBlockNumber(Long blockNumber) throws BlockNotFoundException {
		Optional<Block> optionalBlock = repo.findByBlockNumber(blockNumber);
		if (!optionalBlock.isPresent()) {
			throw new BlockNotFoundException("There is no Block with this number: " + blockNumber);
		}

		return converter.toBlockHeaderDTO(optionalBlock.get());
	}

	private void formLatestBlock(Block latestBlock) throws BlockChainException {
		latestBlock.setCreationTime(new Timestamp(new Date().getTime()));

		calcTopHash(latestBlock);
		calcSignature(latestBlock);
	}

	// TODO: generate top hash using Markel tree
	private void calcTopHash(Block latestBlock) throws BlockChainException {
		List<Transaction> transactions = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (Transaction tx : transactions) {
			sb.append(tx.getSignature());
		}

		try {
			latestBlock.setTopHash(HashingUtils.generateHashBySHA256(sb.toString()));
		} catch (NoSuchAlgorithmException e) {
			throw new BlockChainException("Can't Generate Block Top Hash", e);
		}

	}

	private void calcSignature(Block latestBlock) throws BlockChainException {
		try {
			String signature = HashingUtils.generateHashBySHA256(latestBlock.getVersion(), latestBlock.getBlockNumber(),
					latestBlock.getPreviousBlockHash(), latestBlock.getTopHash(), latestBlock.getCreationTime(),
					latestBlock.getTransactionsNumber());
			latestBlock.setSignature(signature);
		} catch (NoSuchAlgorithmException e) {
			throw new BlockChainException("Can't Generate Block Signature Hash", e);
		}

	}

	private Block initNewBlock(Block latestBlock) {
		Block block = new Block();

		block.setVersion(SEQUANCIAL_BLOCK_VERSION);
		block.setBlockNumber(repo.count());
		block.setTransactionsNumber(0);
		block.setPreviousBlockHash(latestBlock.getSignature());

		return block;
	}

}
