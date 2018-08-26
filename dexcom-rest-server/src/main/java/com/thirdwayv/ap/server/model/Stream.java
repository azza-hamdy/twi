package com.thirdwayv.ap.server.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="stream")
public class Stream extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="log_time")
	private long logTime;
	@Column(name="egv")
	private float estimatedGlucoseValue;

	@Column(name = "date_created")
//	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateCreated;
	
	@ManyToOne()
	@JoinColumn(name = "log_id", nullable = false)
	@JsonBackReference
	private Log log;
   
	public Stream(){}

	public long getLogTime() {
		return logTime;
	}

	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}

	public float getEstimatedGlucoseValue() {
		return estimatedGlucoseValue;
	}

	public void setEstimatedGlucoseValue(float estimatedGlucoseValue) {
		this.estimatedGlucoseValue = estimatedGlucoseValue;
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
