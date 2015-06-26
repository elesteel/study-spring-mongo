package cn.touchmedia.sean.mongo.flow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("flowService")
public class FlowServiceImpl implements IFlowService {

	private static final Logger logger = LoggerFactory.getLogger(FlowServiceImpl.class);
	
	@Autowired
	IFlowDao flowDao;
	
	private Set<String> tables = new HashSet<String>();
	
	public void setFlowDao( IFlowDao flowDao ) {
		logger.debug("setFlowDao");
		this.flowDao = flowDao;
		List<String> list = flowDao.getTableNames();
		for( String name : list ) {
			logger.debug("table: " + name);
			tables.add(name);
		}
	}

	@Override
	public void saveFlowRecords(String taxiFleet, String taxiLicense, List<FlowRecord> records) {
		Map<String, List<FlowRecord>> map = new HashMap<String, List<FlowRecord>>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		for( FlowRecord record : records ) {
			String strDate = sdf.format( record.getF_date());
			logger.debug("strDate: " + strDate);
			if( map.containsKey(strDate) ) {
				map.get(strDate).add(record);
			} else {
				List<FlowRecord> list = new ArrayList<FlowRecord>();
				list.add(record);
				map.put(strDate, list);
			}
		}
		
		for( String key : map.keySet() ) {
			String tableName = "tbl_flow_" + key;
			saveFlowRecordsToTable(tableName, taxiFleet, taxiLicense, map.get(key));
		}
	}
	
	// TODO: add exception handler, maybe create table is fail. it will tables.add(tableName). 
	// it will not create table next time
	private void saveFlowRecordsToTable( String tableName, String taxiFleet, String taxiLicense, List<FlowRecord> records) {
		if( !tables.contains(tableName) ) {
			flowDao.createTable(tableName);
			tables.add(tableName);
		}
		
		if( records.isEmpty() )
			return;
		
		if( records.size() == 1 ) {
			flowDao.saveFlowRecord( tableName, taxiFleet, taxiLicense, records.get(0));
		} else {
			flowDao.batchSaveFlowRecords( tableName, taxiFleet, taxiLicense, records );
		}
	}

}
