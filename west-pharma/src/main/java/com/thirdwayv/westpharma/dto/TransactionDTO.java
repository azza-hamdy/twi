package com.thirdwayv.westpharma.dto;

public class TransactionDTO {

	private Long blockId;
	private String writerId;
	private String tagId;
	private Long time;
	private String hash;
	private String transactionJson;

	public Long getBlockId() {
		return blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	public String getWriterId() {
		return writerId;
	}

	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getTransactionJson() {
		return transactionJson;
	}

	public void setTransactionJson(String transactionJson) {
		this.transactionJson = transactionJson;
	}

}
