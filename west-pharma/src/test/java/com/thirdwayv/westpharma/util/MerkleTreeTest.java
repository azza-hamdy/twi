package com.thirdwayv.westpharma.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MerkleTreeTest {

	private static final String HASH_1 = "c2ee466b7ee5b9485d36dcfee5e202c922369b48cb502f3b64c7c17fa0465660";
	private static final String HASH_2 = "u3ge466b7ee5wk82kd36dcfee5e202c922369b48cb502f3b64c7c17fa0465660";
	private static final String HASH_3 = "7c6e0e1fe717a00211df76086356d081bc6fd2991c33b22ab006ba9555ee2731";
	private static final String HASH_4 = "f1006dce759460c3e7b10e2f66d6f26f7ed1f5b515a5111900d00709c9b444e7";
	private static final String HASH_5 = "2b0c1f37bb376b0cf61050317af97e1ea84fc89da0a6cf9bd953f0214a9af7b6";
	private List<String> transactions;

	@Test
	public void testNullList() throws NoSuchAlgorithmException {
		transactions = null;
		try {
			new MerkleTree(transactions).build();
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testEmptyList() throws NoSuchAlgorithmException {
		transactions = new ArrayList<>(Arrays.asList());
		try {
			new MerkleTree(transactions).build();
			fail();
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testListWithOneTx() throws NoSuchAlgorithmException {
		transactions = new ArrayList<>(Arrays.asList(HASH_1));
		String topHash = new MerkleTree(transactions).build();
		assertEquals(HASH_1, topHash);
	}

	@Test
	public void testListWithTwoTx() throws NoSuchAlgorithmException {
		transactions = new ArrayList<>(Arrays.asList(HASH_1, HASH_2));
		String topHash = new MerkleTree(transactions).build();
		String expected = HashingUtils.generateHashBySHA256(HASH_1, HASH_2);
		assertEquals(expected, topHash);
	}

	@Test
	public void testListWithThreeTx() throws NoSuchAlgorithmException {
		transactions = new ArrayList<>(Arrays.asList(HASH_1, HASH_2, HASH_3));
		String topHash = new MerkleTree(transactions).build();

		String h1 = HashingUtils.generateHashBySHA256(HASH_1, HASH_2);
		String h2 = HashingUtils.generateHashBySHA256(HASH_3, HASH_3);

		String expected = HashingUtils.generateHashBySHA256(h1, h2);

		assertEquals(expected, topHash);
	}

	@Test
	public void testListWithFiveTx() throws NoSuchAlgorithmException {
		transactions = new ArrayList<>(Arrays.asList(HASH_1, HASH_2, HASH_3, HASH_4, HASH_5));
		String topHash = new MerkleTree(transactions).build();

		String h1 = HashingUtils.generateHashBySHA256(HASH_1, HASH_2);
		String h2 = HashingUtils.generateHashBySHA256(HASH_3, HASH_4);
		String h3 = HashingUtils.generateHashBySHA256(HASH_5, HASH_5);

		String h12 = HashingUtils.generateHashBySHA256(h1, h2);
		String h33 = HashingUtils.generateHashBySHA256(h3, h3);

		String expected = HashingUtils.generateHashBySHA256(h12, h33);

		assertEquals(expected, topHash);
	}

}
