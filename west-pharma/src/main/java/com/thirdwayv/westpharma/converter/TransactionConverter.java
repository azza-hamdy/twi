package com.thirdwayv.westpharma.converter;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.BlockChainException;
import com.thirdwayv.westpharma.model.Transaction;
import com.thirdwayv.westpharma.util.HashingUtils;

@Component
public class TransactionConverter {

	public TransactionDTO toDTO(Transaction entity) {
		TransactionDTO dto = new TransactionDTO();

		dto.setBlockNumber(entity.getBlock().getBlockNumber());
		dto.setWriterId(entity.getWriterId());
		dto.setTagId(entity.getTagId());
		dto.setTime(entity.getCreationTime().getTime());
		dto.setHash(entity.getHash());
		dto.setTransactionJson(entity.getTransaction());
		dto.setSystemId(entity.getSystemId());
		return dto;
	}

	public Transaction toEntity(TransactionDTO transactionDTO) throws BlockChainException {
		Transaction tx = new Transaction();
		try {
			tx.setCreationTime(new Timestamp(transactionDTO.getTime()));
			tx.setSystemId(transactionDTO.getSystemId());

			if (transactionDTO.getHash() == null) {
				tx.setWriterId(transactionDTO.getWriterId());
				tx.setTagId(transactionDTO.getTagId());
				tx.setLength(transactionDTO.getTransactionJson().length());
				tx.setTransaction(transactionDTO.getTransactionJson());

				tx.setHash(HashingUtils.generateHashBySHA256(transactionDTO.getTransactionJson()));
				tx.setSignature(HashingUtils.generateHashBySHA256(tx.getSystemId(),tx.getWriterId(), tx.getTagId(),
				tx.getCreationTime().getTime(), tx.getLength(), tx.getHash()));
			} else {
				tx.setHash(transactionDTO.getHash());
				tx.setSignature(HashingUtils.generateHashBySHA256(tx.getHash()));
			}
		} catch (NoSuchAlgorithmException e) {
			throw new BlockChainException("Can't Hash transaction json", e);
		}
		return tx;
	}

}
