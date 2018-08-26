package com.thirdwayv.ap.server.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="node")
public class Node implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(initialValue = 1, name = "nodeidgen", sequenceName = "node_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nodeidgen")
	@Column(name = "id")
	private Long id;
	
	

	@Column(name = "serial_id")
	private String serialId;
	
	@Column(name = "date_created")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateCreated;
	
	@Column(name = "last_activity")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastActivity;
	
	@Column(name = "deleted")
	private boolean deleted;

	@Column(name = "state")
	private String state;
	
	@Column(name = "progress")
	private double progress;
	
	@Column(name = "estimated_egv")
	private double totalEgvs;

	@JsonIgnore
	@OneToMany( mappedBy = "node",cascade = {CascadeType.ALL},orphanRemoval = true)
	@OrderBy("logTime ASC")
	private Set<Log> logs;
	
	public Node(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	public Set<Log> getLogs() {
		return logs;
	}

	public void setLogs(Set<Log> logs) {
		this.logs = logs;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public double getTotalEgvs() {
		return totalEgvs;
	}

	public void setTotalEgvs(double totalEgvs) {
		this.totalEgvs = totalEgvs;
	}
	
	
	
}