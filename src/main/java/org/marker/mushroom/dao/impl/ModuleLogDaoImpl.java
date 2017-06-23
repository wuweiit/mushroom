/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.ModuleLog;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IModuleLogDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 模型安装日志
 * （之所以使用安装日志，是为卸载数据做为删除文件依据）
 * @author marker
 * @date 2013-9-18 上午10:03:40
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@Repository(DAO.MODULE_LOG)
public class ModuleLogDaoImpl extends DaoEngine implements IModuleLogDao{

	
	
	


	//查询时候要对相同文件路径做一个统计，如果个数为1，证明只有一个插件引用，可以放心删除，如果有多个引用，就不删除包
	@Override
	public List<ModuleLog> findByModuleType(String moduleType) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(prefix).append("module_install_log")
		.append(" where type=?");
		
		return super.jdbcTemplate.query(sql.toString(), new Object[]{moduleType}, new RowMapper<ModuleLog>(){

			@Override
			public ModuleLog mapRow(ResultSet rs, int n)
					throws SQLException {
				ModuleLog log = new ModuleLog();
				log.setType(rs.getString("type"));
				log.setFile(rs.getString("file"));
				log.setTime(rs.getTimestamp("time"));
				return log;
			}
			
		}); 
	}

 
	//批量提交日志
	@Override
	public int[] batchModuleLog(List<ModuleLog> logs) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(prefix).append("module_install_log(type,file,time) values(?,?,?)");
		
		
		List<Object[]> list = new ArrayList<Object[]>();
		for(ModuleLog log: logs){
			Object[] args = new Object[]{log.getType(), log.getFile(), new Date()};
			list.add(args);
		}
		
		return this.jdbcTemplate.batchUpdate(sql.toString(), list);
	}


	/* 
	 * 删除日志
	 */
	@Override
	public boolean deleteModuleLog(String moduleType) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(prefix).append("module_install_log where type=?");
		
		int status = this.jdbcTemplate.update(sql.toString(), moduleType);
		return status>0?true:false; 
	}

	
	
	
}
