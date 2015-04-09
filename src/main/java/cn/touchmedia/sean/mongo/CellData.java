package cn.touchmedia.sean.mongo;

public class CellData {
	CellKey id;
	CellValue value;
	public CellKey getId() {
		return id;
	}
	public void setId(CellKey id) {
		this.id = id;
	}
	public CellValue getValue() {
		return value;
	}
	public void setValue(CellValue value) {
		this.value = value;
	}
	
}
