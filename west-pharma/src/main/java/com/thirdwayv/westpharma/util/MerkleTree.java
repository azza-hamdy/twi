package com.thirdwayv.westpharma.util;

import java.security.NoSuchAlgorithmException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class MerkleTree {

	private Deque<String> deque;

	MerkleTree(List<String> transactions) {
		if (transactions == null || transactions.isEmpty()) {
			throw new IllegalArgumentException("Transaction List must not be null");
		}
		initDeque(transactions);
	}

	private void initDeque(List<String> transactions) {
		deque = new LinkedList<>();
		for (String tx : transactions) {
			deque.addLast(tx);
		}

	}

	String build() throws NoSuchAlgorithmException {
		Deque<String> tmp = null;
		while (deque.size() != 1) {
			tmp = new LinkedList<>();
			prepareTransactions();
			while (!deque.isEmpty()) {
				tmp.addLast(HashingUtils.generateHashBySHA256(deque.pop(), deque.pop()));
			}
			deque = tmp;
		}
		return deque.pop();
	}

	private void prepareTransactions() {
		if (isTransactionsSizeOdd()) {
			duplicateLastElement();
		}
	}

	private void duplicateLastElement() {
		deque.addLast(deque.getLast());
	}

	private boolean isTransactionsSizeOdd() {
		return deque.size() % 2 != 0;
	}

}
