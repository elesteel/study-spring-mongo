package cn.touchmedia.sean.mongo;

public class CellValue {
	int cellid;
	int lac;
	double avgLatitude;
	double avgLongitude;
	double totalLatitude;
	double totalLongitude;
	int counts;
	
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
	public double getAvgLatitude() {
		return avgLatitude;
	}
	public void setAvgLatitude(double avgLatitude) {
		this.avgLatitude = avgLatitude;
	}
	public double getAvgLongitude() {
		return avgLongitude;
	}
	public void setAvgLongitude(double avgLongitude) {
		this.avgLongitude = avgLongitude;
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
	
	
}
