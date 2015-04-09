package cn.touchmedia.sean.mongo;

public class CellSet {
	String id;
	int cellid;
	int lac;
	double totalLatitude;
	double totalLongitude;
	int counts;
	double maxLatitude;
	double minLatitude;
	double maxLongitude;
	double minLongitude;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getCellid() {
		return cellid;
	}
	public void setCellid(int cellid) {
		this.cellid = cellid;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
	}
	
	public double getTotalLatitude() {
		return totalLatitude;
	}
	public void setTotalLatitude(double totalLatitude) {
		this.totalLatitude = totalLatitude;
	}
	public double getTotalLongitude() {
		return totalLongitude;
	}
	public void setTotalLongitude(double totalLongitude) {
		this.totalLongitude = totalLongitude;
	}
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	public double getMaxLatitude() {
		return maxLatitude;
	}
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
	public double getMinLatitude() {
		return minLatitude;
	}
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}
	public double getMaxLongitude() {
		return maxLongitude;
	}
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
	public double getMinLongitude() {
		return minLongitude;
	}
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}
	
	
}
