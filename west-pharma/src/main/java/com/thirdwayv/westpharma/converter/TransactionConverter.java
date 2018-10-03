package com.thirdwayv.westpharma.converter;

import org.springframework.stereotype.Component;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.model.Transaction;

@Component
public class TransactionConverter {

//	public Transaction toEntity(TransactionDTO dto) {
//		Transaction entity = new Transaction();
//
//		return entity;
//	}

	public TransactionDTO toDTO(Transaction entity) {
		TransactionDTO dto = new TransactionDTO();

		dto.setBlockNumber(entity.getBlock().getBlockNumber());
		dto.setWriterId(entity.getWriterId());
		dto.setTagId(entity.getTagId());
		dto.setTime(entity.getCreationTime().getTime());
		dto.setHash(entity.getHash());
		dto.setTransactionJson(entity.getTransaction());

		return dto;
	}

}
