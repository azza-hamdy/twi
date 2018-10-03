package com.thirdwayv.westpharma.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thirdwayv.westpharma.model.Block;

public interface BlockRepo extends JpaRepository<Block, Long> {

	Block findBySignatureIsNull();

	Optional<Block> findByBlockNumber(Long blockNumber);
}
