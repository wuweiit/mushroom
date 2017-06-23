package org.marker.mushroom.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IStatisticsDao;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;


@Repository(DAO.STATISTICS)
public class StatisticsDaoImpl extends DaoEngine implements IStatisticsDao{

	

	private final  SimpleDateFormat yyyyMMdd_format = new SimpleDateFormat("yyyyMMdd");
	
	
	

	
	
	@Override
	public Map<String, Object> today() {
		Date as = new Date();
		String time = yyyyMMdd_format.format(as);
		return query(time);
	}



	@Override
	public Map<String, Object> yesterday() {
		Date as = new Date(new Date().getTime()-24*60*60*1000);
		String time = yyyyMMdd_format.format(as); 
		return query(time );
	}

	
	
	
	// 查询某天的访问情况，没有缓存
	public Map<String, Object> query(String time){
		final Map<String, Object> data = new HashMap<String, Object>(24);
		
		// 查询今天访问每小时详细情
		String sql = "select A.* from (select DATE_FORMAT(time,'%Y%m%d-%H') gdate, DATE_FORMAT(time,'%H') hours, DATE_FORMAT(time,'%Y%m%d') date,count(DISTINCT ip) ip,COUNT(DISTINCT visitor) uv, COUNT(id)  pv  from "+getPreFix()+"visited_his GROUP BY gdate) A where date = ?";
		final Map<Integer,Data> list = new HashMap<Integer,Data>(12); 
		this.jdbcTemplate.query(sql,new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {
				Data d = new Data();
				d.hours = rs.getInt("hours");
				d.ip = rs.getInt("ip");
				d.uv = rs.getInt("uv");
				d.pv = rs.getInt("pv");
				list.put(d.hours,d);
			}
		},time);
		
		// 查询今天访问概况,并写入data
		sql = "select A.* from (select DATE_FORMAT(time,'%Y%m%d') date,count(DISTINCT ip) ip,COUNT(DISTINCT visitor) uv, COUNT(id)  pv  from "+getPreFix()+"visited_his GROUP BY date) A where A.date = ?";
		this.jdbcTemplate.query(sql,new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {
				data.put("date", rs.getInt("date"));
				data.put("ipcount", rs.getInt("ip"));
				data.put("uvcount", rs.getInt("uv"));
				data.put("pvcount", rs.getInt("pv"));
			}
		},time);
		
		
		
		// 校正为24小时数据，可能有的时间没有访问记录
		StringBuilder ip = new StringBuilder("");
		StringBuilder pv = new StringBuilder("");
		StringBuilder uv = new StringBuilder(""); 
		for(int i=0;i<24;i++){
			if(list.containsKey(i)){
				Data c = list.get(i);
				ip.append(c.ip); pv.append(c.pv); uv.append(c.uv);
			}else{
				ip.append(0); pv.append(0); uv.append(0);
			} 
			if(i < 23){
				ip.append(",");
				pv.append(",");
				uv.append(",");
			}
		}
		data.put("ip", ip.toString());
		data.put("pv", pv.toString());
		data.put("uv", uv.toString()); 
		return data; 
	}
	
	
	
	
}

class Data{
	int hours;
	int ip;
	int uv;
	int pv; 
}