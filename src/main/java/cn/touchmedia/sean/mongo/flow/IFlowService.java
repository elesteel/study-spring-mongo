package cn.touchmedia.sean.mongo.flow;

import java.util.List;


public interface IFlowService {

	public void saveFlowRecords( String taxiFleet, String taxiLicense, List<FlowRecord> records );
}
