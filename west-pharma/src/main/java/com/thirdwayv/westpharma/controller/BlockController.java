package com.thirdwayv.westpharma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thirdwayv.westpharma.dto.BlockHeaderDTO;
import com.thirdwayv.westpharma.service.api.BlockService;

@RestController
@RequestMapping(value = "/api/block")
public class BlockController {

	@Autowired
	private BlockService blockService;

	@GetMapping("/readbynumber")
	public BlockHeaderDTO readByBlockHeaderNumber(@RequestParam("number") Long blockNumber) throws Exception {
		return blockService.getBlockHeaderByBlockNumber(blockNumber);
	}

}
