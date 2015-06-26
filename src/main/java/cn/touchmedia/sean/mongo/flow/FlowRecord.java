package cn.touchmedia.sean.mongo.flow;

import java.util.Date;


public class FlowRecord {
	private long id;

	private long Rx;

	private long Tx;

	private Date f_date;

	private String insertTime;

	private String updateTime;

	public FlowRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRx() {
		return Rx;
	}

	public void setRx(long rx) {
		Rx = rx;
	}

	public long getTx() {
		return Tx;
	}

	public void setTx(long tx) {
		Tx = tx;
	}

	public Date getF_date() {
		return f_date;
	}

	public void setF_date(Date f_date) {
		this.f_date = f_date;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
