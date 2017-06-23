package org.marker.mushroom.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Module;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IModuleDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperModule;
import org.springframework.stereotype.Repository;
 
 
/**
 * 内容模型数据库操作类
 * @author marker 
 * */
@Repository(DAO.MODULE)
public class ModuleDaoImpl extends DaoEngine implements IModuleDao {

	
	
	
	
	
	//查询全部的内容模型
	@Override
	public List<Module> queryAll() {
		String prefix = getPreFix();//获取数据库表前缀
		
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(prefix).append("module");
		
		try{
			return jdbcTemplate.query(sql.toString(),  new RowMapperModule());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Module>(0);
	}

	

	//检测是否安装当前要安装的内容模型
	@Override
	public Module checkIsInstall(String moduleType) {
		String prefix = getPreFix();//获取数据库表前缀
		
		StringBuilder sql = new StringBuilder();
		//暂时不优化字段
		sql.append("select * from ").append(prefix).append("module").append(" where type=?");
		
		try{
			return jdbcTemplate.queryForObject(sql.toString(),new Object[]{moduleType}, new RowMapperModule());
		}catch (Exception e) {
			return null;//如果没有找到数据就会抛出异常
		}
	}


	//安装模型
	@Override
	public boolean install(Module module) {	
		String prefix = getPreFix();//获取数据库表前缀
	
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(prefix).append("module")
		.append("(name,type,uri,template,author,version) values(?,?,?,?,?,?)"); 
		int status = this.jdbcTemplate.update(sql.toString(), new Object[]{module.getName()
			,module.getType(),module.getUri(),module.getTemplate(),module.getAuthor(),module.getVersion()});
		return status > 0 ?true : false;
	}


	/*  
	 */
	@Override
	public boolean deleteByType(String moduleType) {
		String prefix = getPreFix();//获取数据库表前缀 
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(prefix).append("module")
		.append(" where type=?"); 
		int status = this.jdbcTemplate.update(sql.toString(), moduleType);
		return status > 0 ?true : false;
	}


	/* (non-Javadoc)
	 * @see org.marker.mushroom.dao.IModuleDao#findByType(java.lang.String)
	 */
	@Override
	public Module findByType(String moduleType) {
		String prefix = getPreFix();//获取数据库表前缀
		
		StringBuilder sql = new StringBuilder();
		//暂时不优化字段
		sql.append("select * from ").append(prefix).append("module").append(" where type=?");
		
		try{
			return jdbcTemplate.queryForObject(sql.toString(),new Object[]{moduleType},  new RowMapperModule());
		}catch (Exception e) {
			return null;//如果没有找到数据就会抛出异常
		}
	}
	
	
	
	
	
}
