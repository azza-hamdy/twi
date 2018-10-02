package com.thirdwayv.westpharma.service.api;

import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.model.Block;

public interface BlockService {

	Block getLatestBlock() throws BlockChainException;

	Block save(Block block);

	void updateBlockchain(Block latestBlock) throws BlockChainException;

}
