package com.thirdwayv.westpharma.converter;

import org.springframework.stereotype.Component;

import com.thirdwayv.westpharma.dto.BlockHeaderDTO;
import com.thirdwayv.westpharma.model.Block;

@Component
public class BlockConverter {

	public BlockHeaderDTO toBlockHeaderDTO(Block entity) {
		BlockHeaderDTO blockHeader = new BlockHeaderDTO();

		blockHeader.setVersion(entity.getVersion());
		blockHeader.setPreviousBlockHash(entity.getPreviousBlockHash());

		return blockHeader;
	}

}
