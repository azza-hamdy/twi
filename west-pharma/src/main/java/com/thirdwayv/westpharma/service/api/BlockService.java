package com.thirdwayv.westpharma.service.api;

import com.thirdwayv.westpharma.dto.BlockHeaderDTO;
import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.exception.BlockNotFoundException;
import com.thirdwayv.westpharma.model.Block;

public interface BlockService {

	Block getLatestBlock() throws BlockChainException;

	Block save(Block block);

	void updateBlockchain(Block latestBlock) throws BlockChainException;

	BlockHeaderDTO getBlockHeaderByBlockNumber(Long blockNumber) throws BlockNotFoundException;

}
