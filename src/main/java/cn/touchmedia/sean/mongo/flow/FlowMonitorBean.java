package cn.touchmedia.sean.mongo.flow;

import java.util.List;

public class FlowMonitorBean {
private String tbid;
	
	private String liscense;
	
	private List<FlowRecord> data;
	
	public FlowMonitorBean(String tbid, String liscense, List<FlowRecord> data) {
		super();
		this.tbid = tbid;
		this.liscense = liscense;
		this.data = data;
	}

	public String getTbid() {
		return tbid;
	}

	public void setTbid(String tbid) {
		this.tbid = tbid;
	}

	public String getLiscense() {
		return liscense;
	}

	public void setLiscense(String liscense) {
		this.liscense = liscense;
	}

	public List<FlowRecord> getData() {
		return data;
	}

	public void setData(List<FlowRecord> data) {
		this.data = data;
	}
}
