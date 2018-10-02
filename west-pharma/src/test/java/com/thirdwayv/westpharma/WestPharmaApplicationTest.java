package com.thirdwayv.westpharma;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.thirdwayv.westpharma.dto.TransactionDTO;
import com.thirdwayv.westpharma.service.api.BlockChainService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WestPharmaApplicationTest {

	@Autowired
	private BlockChainService blockChainService;

	@Test
	public void saveTxTest() throws Exception {
		TransactionDTO tx = new TransactionDTO();
		tx.setTagId("12665");
		tx.setWriterId("localhost");
		tx.setTime(new Date().getTime());
		tx.setTransactionJson("{\"tagId\":\"1256\", \"WrtiterId\":\"localhost\"}");
		TransactionDTO savedTransaction = blockChainService.saveTransaction(tx);
		assertNotNull(savedTransaction.getBlockNumber());
	}
}