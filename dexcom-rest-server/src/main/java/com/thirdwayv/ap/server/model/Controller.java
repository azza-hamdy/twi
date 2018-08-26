package com.thirdwayv.ap.server.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="controller")
public class Controller implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(initialValue = 1, name = "ctrDeviceIdGen", sequenceName = "controller_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ctrDeviceIdGen")
	@Column(name = "id")
	private Long id;
	
	@Column(name = "device_number")
	private int deviceNumber;
	
	@JsonIgnore
	@Column(name = "date_created")
//	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;
    
	@JsonIgnore
	@Column(name = "date_updated")
//	@Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateUpdated;
    
	@JsonIgnore
	@Column(name = "cloud_long_term_key")
    private String ltk;
    
	@JsonIgnore
	@Column(name = "session_sequence_number")
    private long sequenceNumber;
    
	@Column(name = "last_activity", updatable=true)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastActivity;
    
	@Column(name = "lat")
	private double lat;
    
	@Column(name = "lng")
	private double lng;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "controller",cascade = {CascadeType.ALL},orphanRemoval = true)
	@OrderBy("logTime ASC")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Set<Log> logs;

	public Controller() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(int deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getLtk() {
		return ltk;
	}

	public void setLtk(String ltk) {
		this.ltk = ltk;
	}

	public long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public LocalDateTime getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Set<Log> getLogs() {
		return logs;
	}

	public void setLogs(Set<Log> logs) {
		this.logs = logs;
	}
}
