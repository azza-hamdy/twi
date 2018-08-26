package com.thirdwayv.ap.server.model;

import java.time.LocalDateTime;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="log")
public class Log extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Column(name="log_time")
	private long logTime;
	
	@JsonIgnore
	@Column(name="flag")
	private int flags;
	
	@JsonIgnore
	@Column(name="egv")
	private float estimatedGlucoseValue;
	
	@Column(name="bg")
	private float bloodGlucose;
	
	@Column(name="bg_time")
	private long bloodGlucoseTime;
	
	@JsonIgnore
	@Column(name="seq_no")
	private long seqNo;
	
	@JsonIgnore
	@Column(name = "date_created")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateCreated;
	
	@JsonIgnore
	@Column(name = "stream_count")
	private int streamCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "controller_id", nullable = false)
	@JsonBackReference
	private Controller controller;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "node_id", nullable = false)
	@JsonBackReference
	private Node node;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY,mappedBy="log", cascade = {CascadeType.ALL},orphanRemoval = true)
	@OrderBy("logTime ASC")
	private Set<Stream> streams;
	
	@JsonIgnore
	@OneToMany(mappedBy="log", cascade = {CascadeType.ALL},orphanRemoval = true)
	private Set<StreamHeader> streamHeader;

	public Log() {
	}

	public long getLogTime() {
		return logTime;
	}

	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public float getEstimatedGlucoseValue() {
		return estimatedGlucoseValue;
	}

	public void setEstimatedGlucoseValue(float estimatedGlucoseValue) {
		this.estimatedGlucoseValue = estimatedGlucoseValue;
	}

	public float getBloodGlucose() {
		return bloodGlucose;
	}

	public void setBloodGlucose(float bloodGlucose) {
		this.bloodGlucose = bloodGlucose;
	}

	public long getBloodGlucoseTime() {
		return bloodGlucoseTime;
	}

	public void setBloodGlucoseTime(long bloodGlucoseTime) {
		this.bloodGlucoseTime = bloodGlucoseTime;
	}

	public long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getStreamCount() {
		return streamCount;
	}

	public void setStreamCount(int streamCount) {
		this.streamCount = streamCount;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Set<Stream> getStreams() {
		return streams;
	}

	public void setStreams(Set<Stream> streams) {
		this.streams = streams;
	}

	public Set<StreamHeader> getStreamHeader() {
		return streamHeader;
	}

	public void setStreamHeader(Set<StreamHeader> streamHeader) {
		this.streamHeader = streamHeader;
	}
	
	


	
}
