package cn.touchmedia.sean.mongo.flow;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;


@Repository("flowDao")
public class FlowDaoImpl implements IFlowDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public List<String> getTableNames() {
		return jdbcTemplate.queryForList("show tables", String.class);
	}

	@Override
	public void createTable(String tableName) {
		String sql = "CREATE TABLE IF NOT EXISTS `" + tableName +"` ("
				+ " `FLOW_ID` int(11) NOT NULL AUTO_INCREMENT,"
				+ " `FLOW` bigint(20) DEFAULT NULL,"
				+ " `FLOW_DATE` date DEFAULT NULL,"
				+ " `RX` bigint(20) DEFAULT NULL,"
				+ " `TAXI_FLEET` varchar(4) NOT NULL,"
				+ " `TAXI_ID` varchar(6) NOT NULL,"
				+ " `TX` bigint(20) DEFAULT NULL,"
				+ " PRIMARY KEY (`FLOW_ID`)"
				+ " )";
				
		jdbcTemplate.execute(sql);
	}

	@Override
	public void saveFlowRecord(String tableName, final String taxiFleet, final String taxiLicense, final FlowRecord record) {
		String sql = "insert into " + tableName + " ( FLOW, FLOW_DATE, RX, TAXI_FLEET, TAXI_ID, TX ) "
				+ " values ( ?, ?, ?, ?, ?, ? ) ";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				long rx = record.getRx();
				long tx = record.getTx();
				java.sql.Date date = new java.sql.Date( record.getF_date().getTime() );
				ps.setLong(1, rx + tx);
				ps.setDate(2, date);
				ps.setLong(3, rx);
				ps.setString(4, taxiFleet);
				ps.setString(5, taxiLicense);
				ps.setLong(6, tx);
			}
		});
	}

	@Override
	public void batchSaveFlowRecords(String tableName, final String taxiFleet, final String taxiLicense, final List<FlowRecord> records) {
		String sql = "insert into " + tableName + " ( FLOW, FLOW_DATE, RX, TAXI_FLEET, TAXI_ID, TX ) "
				+ " values ( ?, ?, ?, ?, ?, ? ) ";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				long rx = records.get(i).getRx();
				long tx = records.get(i).getTx();
				java.sql.Date date = new java.sql.Date( records.get(i).getF_date().getTime());
				ps.setLong(1, rx + tx);
				ps.setDate(2, date);
				ps.setLong(3, rx);
				ps.setString(4, taxiFleet);
				ps.setString(5, taxiLicense);
				ps.setLong(6, tx);
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return records.size();
			}
		});
	}

}
