package cn.touchmedia.sean.mongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.touchmedia.sean.mongo.flow.FlowMonitorBean;
import cn.touchmedia.sean.mongo.flow.IFlowDao;
import cn.touchmedia.sean.mongo.flow.IFlowService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class MyController {

	private static final String COLLECTION_NAME = "cellset";

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MyController.class);
	
	@Autowired
	MongoOperations mongoOps;
	
	@Autowired
	IFlowDao flowDao;
	
	@Autowired
	IFlowService flowService;
	
	
	@RequestMapping( value = "jsondata", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public String handleCellsGsonData(HttpServletRequest request) {
		try {
			GZIPInputStream gzis = new GZIPInputStream( request.getInputStream() );
			BufferedReader br = new BufferedReader( new InputStreamReader(gzis) );
			String json = IOUtils.toString( br );
			Gson gson = new Gson();
			Cell[] cells = gson.fromJson( json, Cell[].class);
			long startTime = System.nanoTime();
			for( Cell cell : cells ) {
				Query query = Query.query(Criteria.where("cellid").is(cell.getCellid()).and("lac").is(cell.getLac()));
				CellSet cellset = mongoOps.findOne(query, CellSet.class, COLLECTION_NAME + cell.getMnc());
				if( cellset == null ) {
					Update update = new Update().inc("totalLatitude", cell.getLatitude())
							.inc("totalLongitude", cell.getLongitude())
							.inc("counts", 1)
							.set("maxLatitude", cell.getLatitude())
							.set("minLatitude", cell.getLatitude())
							.set("maxLongitude", cell.getLongitude())
							.set("minLongitude", cell.getLongitude());
					FindAndModifyOptions options = new FindAndModifyOptions().upsert(true);
					mongoOps.findAndModify(query, update, options, CellSet.class, COLLECTION_NAME + cell.getMnc());
				} else {
					Update update = new Update().inc("totalLatitude", cell.getLatitude())
							.inc("totalLongitude", cell.getLongitude())
							.inc("counts", 1)
							.set("maxLatitude", Math.max(cellset.maxLatitude, cell.getLatitude()))
							.set("minLatitude", Math.min(cellset.minLatitude, cell.getLatitude()))
							.set("maxLongitude", Math.max(cellset.maxLongitude, cell.getLongitude()))
							.set("minLongitude", Math.min(cellset.minLongitude, cell.getLongitude()));
					FindAndModifyOptions options = new FindAndModifyOptions().upsert(true);
					mongoOps.findAndModify(query, update, options, CellSet.class, COLLECTION_NAME + cell.getMnc());
				}
			}
			long duration = (System.nanoTime() - startTime) / 1000000;
			logger.debug("update " + cells.length + " records used " + duration + " ms");
			return "success";
		} catch (IOException e) {
			throw new RuntimeException("fail");
		}
	}
	
//	@RequestMapping( value = "jsondata", method = RequestMethod.POST, consumes="application/json")
//	@ResponseBody
//	public String handleCellsData(HttpServletRequest request) {
//		try {
//			GZIPInputStream gzis = new GZIPInputStream( request.getInputStream() );
//			BufferedReader br = new BufferedReader( new InputStreamReader(gzis) );
//			String json = IOUtils.toString( br );
//			Gson gson = new Gson();
//			Cell[] cells = gson.fromJson( json, Cell[].class);
//			long startTime = System.nanoTime();
//			for( Cell cell : cells ) {
//				Query query = Query.query(Criteria.where("cellid").is(cell.getCellid()).and("lac").is(cell.getLac()));
//				Update update = new Update().inc("totalLatitude", cell.getLatitude())
//						.inc("totalLongitude", cell.getLongitude())
//						.inc("counts", 1);
//				FindAndModifyOptions options = new FindAndModifyOptions().upsert(true);
//				mongoOps.findAndModify(query, update, options, CellSet.class, "cellset" + cell.getMnc());
//			}
//			long duration = (System.nanoTime() - startTime) / 1000000;
//			System.out.println("update " + cells.length + " records used " + duration + " ms" );
//			
//			return "success";
//		} catch (IOException e) {
//			throw new RuntimeException("fail");
//		}
//		
//	}
	
//	@RequestMapping( value = "cellset", method = RequestMethod.GET ) 
//	public void getCellData( @RequestParam("mnc") int mnc, OutputStream os) {
//		System.out.println("output result for mnc: " + mnc);
//		try {
//			List<CellSet> list = mongoOps.findAll(CellSet.class, "cellset" + mnc);
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			String json = gson.toJson(list);
//			OutputStream out = new GZIPOutputStream( os );
//			out.write( json.getBytes() );
//			out.flush();
//			out.close();
//			System.out.println("success");
//		} catch ( Exception e ) {
//			throw new RuntimeException("fail");
//		}
//	}
	
	
	@RequestMapping( value = "getcells", method = RequestMethod.GET )
	@ResponseBody
	public String getCells(  @RequestParam("mnc") int mnc, OutputStream os) {
		//System.out.println("getCells");
		long startTime = System.nanoTime();
		OutputStream out = null;
		try {
			List<CellSet> list;
			int step = 1000;
			int skip = 0;

			
			Gson gson = new Gson();
			out = new GZIPOutputStream( os );
			out.write("[".getBytes());
			boolean isFirst = true;
			
			do {
				Aggregation agg = Aggregation.newAggregation(
						
						Aggregation.skip( skip ),
						Aggregation.limit( step )
						);
				skip += step;
				AggregationResults<CellSet> results = mongoOps.aggregate(agg, COLLECTION_NAME + mnc, CellSet.class);
				list = results.getMappedResults();
				
				for( CellSet cell : list ) {
					if( isFirst ) {
						isFirst = false;
					} else {
						out.write( ",\n".getBytes());
					}
					String json = gson.toJson(cell);
					out.write(json.getBytes());
				}
			}while( list.size() == step );
			out.write("]".getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("fail");
		} finally {
			IOUtils.closeQuietly(out);
		}
		long duration = (System.nanoTime() - startTime) / 1000000;
		//System.out.println("duration: " + duration + " ms");
		return "OK";
	}
	
	/*
	@RequestMapping( value = "/auth", method = RequestMethod.GET)
	@ResponseBody
	public String auth() {
		
		int cellid = 1;
		int lac = 1;
		double latitude = 1.0;
		double longitude = 2.0;
		CellInfo cell = new CellInfo();
		cell.setCellid(cellid);
		cell.setLac(lac);
		cell.setLatitude(latitude);
		cell.setLongitude(longitude);
		mongoOps.insert(cell,"mycells");
		return "auth ok";
	}
	
	@RequestMapping( value = "/insert", method = RequestMethod.GET)
	@ResponseBody
	public String insert(@RequestParam("count") int count ) {
		Random random = new Random(10);
		int cellid = 1;
		int lac = 1;
		double latitude = 1.0 - 0.5;
		double longitude = 2.0 - 0.5;
		CellInfo[] cells = new CellInfo[6];
		for( int i=0; i<count; i++) {
			cellid = random.nextInt();
			lac = random.nextInt();
			latitude = 0.5;
			longitude = 1.5 ;
			for( int j=0; j < cells.length; j++) {
				cells[j] = new CellInfo();
				cells[j].setCellid(cellid);
				cells[j].setLac(lac);
				cells[j].setLatitude(latitude + Math.random());
				cells[j].setLongitude(longitude + Math.random());
			}
			mongoOps.insert( Arrays.asList(cells), "mycells" );
		}
		return "insert " + count + " cells";
	}
	
	@RequestMapping( value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String test() {
//		TypedAggregation<CellInfo> agg = newAggregation(CellInfo.class, 
//				group("cellid").count().as("count"),
//				project("count").and("cellid").previousOperation()
//		);
//		
//		AggregationResults<CellCounts> results = mongoOps.aggregate(agg, "cells", CellCounts.class);
//		List<CellCounts> cellCounts = results.getMappedResults();
//		for( CellCounts cs : cellCounts ) {
//			System.out.println("cell " + cs.cellid + " = " + cs.count);
//		}
		
		mongoOps.mapReduce("mycells", 
				"classpath:/js/map.js", 
				"classpath:/js/reduce.js", 
				MapReduceOptions.options().outputCollection("cellData")
									      .outputTypeReduce()
									      .finalizeFunction("classpath:/js/finalize.js"),
				CellData.class);
		return "OK";
	}
	*/
	
//	@RequestMapping( value = "/test", method = RequestMethod.GET)
//	@ResponseBody
//	public String test() {
//		flowDao.createTable("tbl_flow_201506");
//		logger.info("create table tbl_flow_201506");
//		return "ok2";
//	}
	
	@RequestMapping( value = "/flow", method = RequestMethod.POST)
	@ResponseBody
	public String receiveFlowData( HttpServletRequest request ) {
		logger.debug("receiveFlowData: ");
		try {
			String content = IOUtils.toString(request.getReader());
			logger.debug(content);
			Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
			FlowMonitorBean b = gson.fromJson(content, FlowMonitorBean.class);
			logger.info("receive flow data from " + b.getTbid() + " - " + b.getLiscense() + " at " + new Date());
			flowService.saveFlowRecords(b.getTbid(), b.getLiscense(), b.getData());
		} catch( IOException e ) {
			logger.error("exception happen when handle flow data");
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
}
