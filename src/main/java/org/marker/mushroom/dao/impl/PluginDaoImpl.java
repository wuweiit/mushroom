package org.marker.mushroom.dao.impl;

import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Plugin;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IPluginDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperPlugin;
import org.springframework.stereotype.Repository;


/**
 * 插件数据库操作对象
 * @author marker
 * */
@Repository(DAO.PLUGIN)
public class PluginDaoImpl extends DaoEngine implements IPluginDao{

	
	

	
	
	/**
	 * 查询所有插件
	 */
	@Override
	public List<Plugin> queryAll() {
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(getPreFix()).append("plugin").append(" where status=1"); 
		return jdbcTemplate.query(sql.toString(), new RowMapperPlugin()); 
	}


	
	@Override
	public Plugin findByMark(String mark) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("select id,name,uri,mark,status from ");
		sql.append(prefix).append("plugin").append(" where mark=?");
		return jdbcTemplate.queryForObject(sql.toString(), new Object[]{mark}, new RowMapperPlugin()); 
	}



	@Override
	public boolean check(String uuid) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("select count(id) from ");
		sql.append(prefix).append("plugin").append(" where uuid=?");
		return jdbcTemplate.queryForObject(sql.toString(), Boolean.class, uuid);  
	}
	
	
	
	 
}
