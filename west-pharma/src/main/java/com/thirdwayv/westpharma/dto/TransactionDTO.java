package com.thirdwayv.westpharma.dto;

public class TransactionDTO {

	private Long blockNumber;
	private String writerId;
	private String tagId;
	private Long time;
	private String hash;
	private String transactionJson;
	private Integer systemId;
	
	public Long getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
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

	public Integer getSystemId() {
		return systemId;
	}

	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}

}
