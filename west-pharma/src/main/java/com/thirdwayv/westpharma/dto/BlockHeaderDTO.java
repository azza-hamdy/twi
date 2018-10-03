package com.thirdwayv.westpharma.dto;

public class BlockHeaderDTO {

	private Integer version;
	private String previousBlockHash;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}

	public void setPreviousBlockHash(String previousBlockHash) {
		this.previousBlockHash = previousBlockHash;
	}

}
