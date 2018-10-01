package com.thirdwayv.westpharma.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.model.Block;
import com.thirdwayv.westpharma.repo.BlockRepo;
import com.thirdwayv.westpharma.service.api.BlockService;

@Service
public class BlockServiceImpl implements BlockService {

	@Autowired
	private BlockRepo blockRepo;

	@Override
	public Block getLatestBlock() throws BlockChainException {
		Block block = blockRepo.findBySignatureIsNull();
		if (block == null) {
			throw new BlockChainException("Block Must not be Null");
		}
		return block;
	}

	@Override
	public Block save(Block block) {
		return blockRepo.save(block);
	}

}
