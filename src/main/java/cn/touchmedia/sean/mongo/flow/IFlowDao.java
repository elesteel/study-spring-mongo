package cn.touchmedia.sean.mongo.flow;

import java.util.List;

public interface IFlowDao {

	List<String> getTableNames();
	void createTable( String tableName );
	void saveFlowRecord( String tableName, String taxiFleet, String taxiLicense, FlowRecord record );
	void batchSaveFlowRecords( String tableName, String taxiFleet, String taxiLicense, final List<FlowRecord> records );
}
