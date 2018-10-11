package com.thirdwayv.westpharma;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.exception.InvalidInputException;
import com.thirdwayv.westpharma.model.Block;
import com.thirdwayv.westpharma.repo.BlockRepo;
import com.thirdwayv.westpharma.service.api.BlockChainService;
import com.thirdwayv.westpharma.service.api.BlockService;
import com.thirdwayv.westpharma.util.SystemConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WestPharmaApplicationTest {

	private static final int SHA256_HASH_LENGTH = 64;

	@Autowired
	private BlockChainService blockChainService;

	@Autowired
	private BlockService blockService;

	@Autowired
	private BlockRepo blockRepo;

	@Autowired
	private SystemConfig systemConfig;

	@Test
	public void testSaveTxWithJson() throws Exception {
		TransactionDTO tx = createTransactionDTO();
		TransactionDTO savedTransaction = blockChainService.saveTransaction(tx);
		assertNotNull(savedTransaction.getBlockNumber());
	}

	@Test
	public void testSaveTxWithHash() throws Exception {
//		for (int i = 0; i < 1000; i++) {
			TransactionDTO tx = new TransactionDTO();
			tx.setHash(generateRandomStringWithLength(SHA256_HASH_LENGTH));
			tx.setTime(new Date().getTime());
			tx.setSystemId(2010);
			TransactionDTO savedTransaction = blockChainService.saveTransaction(tx);
			assertNotNull(savedTransaction.getBlockNumber());
//		}
	}

	@Test
	public void testSaveInvalidTx() throws Exception {
		TransactionDTO tx = new TransactionDTO();
		try {
			blockChainService.saveTransaction(tx);
			fail();
		} catch (InvalidInputException e) {
		}
	}

	@Test
	public void testBlockFormationAfterCertainTransactionNumber() throws Exception {
		Block latestBlock = blockService.getLatestBlock();
		Integer blockSize = latestBlock.getTransactionsNumber();

		while (blockSize != systemConfig.getTransctionNumInBlock()) {
			blockChainService.saveTransaction(createTransactionDTO());
			latestBlock = blockRepo.findById(latestBlock.getId()).get();
			blockSize = latestBlock.getTransactionsNumber();
		}

		assertNotNull(latestBlock.getSignature());
	}

	@Test
	public void testIncreamentBlockSizeAfterSavingTxTest() throws Exception {
		Block latestBlock = blockService.getLatestBlock();
		Integer oldBlockSize = latestBlock.getTransactionsNumber();

		blockChainService.saveTransaction(createTransactionDTO());
		Block updatedBlock = blockRepo.findById(latestBlock.getId()).get();

		assertTrue(oldBlockSize + 1 == updatedBlock.getTransactionsNumber());
	}

	/********************************************************************************/
	/********************************************************************************/

	private TransactionDTO createTransactionDTO() {
		TransactionDTO tx = new TransactionDTO();
		tx.setTagId(String.valueOf(new Random().nextInt(100000)));
		byte[] strBytes = new byte[20];
		new Random().nextBytes(strBytes);
		tx.setSystemId(2020);
		tx.setWriterId(generateRandomStringWithLength(20));
		tx.setTime(new Date().getTime());
		tx.setTransactionJson("{\"tagId\":\"" + tx.getTagId() +"\", \"SystemId\":\""+tx.getSystemId() +"\", \"WrtiterId\":\"" + tx.getWriterId() + "\"}");
		return tx;
	}

	private String generateRandomStringWithLength(int length) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
}