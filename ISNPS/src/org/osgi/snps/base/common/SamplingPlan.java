package org.osgi.snps.base.common;

import java.util.List;

public class SamplingPlan {

	String splan_identifier;
	List<String> nodesId;
	double threshold_min;
	double threshold_max;
	int interval;
	private String startDate="";
	private String endDate="";
	
	public SamplingPlan(){}
	
//	public SamplingPlan(String splanId,List<String> nID, double th_min, double th_max, int interval){
//		nodesId = nID;
//		threshold_max = th_max;
//		threshold_min = th_min;
//		this.interval = interval;
//		splan_identifier = splanId;
//	}
	
	public SamplingPlan(String splanId, List<String> nID, String startDate, String endDate, int interval){
		nodesId = nID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.interval = interval;
		splan_identifier = splanId;
	}

	public String getSplan_identifier() {
		return splan_identifier;
	}

	public void setSplan_identifier(String splan_identifier) {
		this.splan_identifier = splan_identifier;
	}
	
	public List<String> getNodesId() {
		return nodesId;
	}

	public void setNodesId(List<String> nodeId) {
		this.nodesId = nodeId;
	}

	public double getThreshold_min() {
		return threshold_min;
	}

	public void setThreshold_min(double th_min) {
		this.threshold_min = th_min;
	}

	public double getThreshold_max() {
		return threshold_max;
	}

	public void setThreshold_max(double th_max) {
		this.threshold_max = th_max;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Print the Sampling Plan
	 */
	@Override
	public String toString(){
		String str = this.getSplan_identifier()+","+this.getNodesId().toString()+","+this.getInterval()+","+this.getStartDate()+","+this.getEndDate();
		System.out.println("[Sampling Plan]: "+str);
		return str;
	}
}

	