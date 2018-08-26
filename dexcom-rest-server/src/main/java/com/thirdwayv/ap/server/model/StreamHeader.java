package com.thirdwayv.ap.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="stream_header")
public class StreamHeader extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="starting_log_time")
	private long startingLogTime;
	
	@Column(name="egv_count")
	private int egvCount;
	
	@Column(name="period")
	private int period;
	
	@Column(name = "date_created")
//	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateCreated;
	
	@ManyToOne()
	@JoinColumn(name = "log_id", nullable = false)
	@JsonBackReference
	private Log log;
   
	public StreamHeader(){}

	public long getStartingLogTime() {
		return startingLogTime;
	}

	public void setStartingLogTime(long startingLogTime) {
		this.startingLogTime = startingLogTime;
	}

	public int getEgvCount() {
		return egvCount;
	}

	public void setEgvCount(int egvCount) {
		this.egvCount = egvCount;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	
}
