package com.thirdwayv.persistence.entity;

import java.util.List;

public class LogData_CGM {
	private long ctrId;
	private long nodeId;
	private long logTime;
	private String cgmTxId;
	private int flags;
	private float estimatedGlucoseValue; //Estimated Glucose Value
	private float bloodGlucose;//Blood Glucose
	private long bloodGlucoseTime;
	private long seqNo;

	private short backfillStreamCount;
	private List<BackfillStream> streams;
	
	
	public long getCtrId() {
		return ctrId;
	}
	public void setCtrId(long ctrId) {
		this.ctrId = ctrId;
	}
	public long getNodeId() {
		return nodeId;
	}
	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
	public long getLogTime() {
		return logTime;
	}
	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}
	public String getCgmTxId() {
		return cgmTxId;
	}
	public void setCgmTxId(String cgmTxId) {
		this.cgmTxId = cgmTxId;
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
	public short getBackfillStreamCount() {
		return backfillStreamCount;
	}
	public void setBackfillStreamCount(short backfillStreamCount) {
		this.backfillStreamCount = backfillStreamCount;
	}
	
	public List<BackfillStream> getStreams() {
		return streams;
	}
	public void setStreams(List<BackfillStream> streams) {
		this.streams = streams;
	}
	@Override
	public String toString() {
		return new StringBuilder().append("ctrId:").append(ctrId).append("\t")
							.append("nodeId:").append(nodeId).append("\t")
							.append("logTime:").append(logTime).append("\t")
							.append("cgmTxId:").append(cgmTxId).append("\t")
							.append("flags:").append(flags).append("\t")
							.append("EGV:").append(estimatedGlucoseValue).append("\t")
							.append("BG:").append(bloodGlucose).append("\t")
							.append("BGTime:").append(bloodGlucoseTime).append("\t")
							.append("seqNo:").append(seqNo).append("\t")
							.append("BackfillStreamCount:").append(backfillStreamCount).toString();
	}
	
	
	
}
