package com.thirdwayv.westpharma.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Block implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(initialValue = 0, name = "blockIdGen", sequenceName = "block_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blockIdGen")
	private Long id;

	private Integer version;

	@Column(name = "blocknumber")
	private Long blockNumber;

	@Column(name = "previousblockhash")
	private String previousBlockHash;

	@Column(name = "tophash")
	private String topHash;

	@Column(name = "creationtime")
	private Timestamp creationTime;

	@Column(name = "transactionsnumber")
	private Integer transactionsNumber;

	private String signature;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "block", cascade = CascadeType.ALL)
	private List<Transaction> transactions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getPreviousBlockHash() {
		return previousBlockHash;
	}

	public void setPreviousBlockHash(String previousBlockHash) {
		this.previousBlockHash = previousBlockHash;
	}

	public String getTopHash() {
		return topHash;
	}

	public void setTopHash(String topHash) {
		this.topHash = topHash;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getTransactionsNumber() {
		return transactionsNumber;
	}

	public void setTransactionsNumber(Integer transactionsNumber) {
		this.transactionsNumber = transactionsNumber;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
