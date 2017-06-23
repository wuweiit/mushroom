package org.marker.mushroom.support;

import javax.servlet.ServletContext;

import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ICommonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;



/**
 * 控制器支撑类
 * @author marker
 * */
public class SupportController {

	/** 日志记录器 */
	public static Logger log = LoggerFactory.getLogger(SupportController.class);
	
	@Autowired protected JdbcTemplate dao;
	 
	
	/* 自动注入通用Dao */
	@Autowired protected ICommonDao commonDao;
	
	/*  */
	@Autowired protected ServletContext application;
	
	
	/**
	 * viewPath为视图的目录
	 * */
	protected String viewPath;

//	protected static final DataBaseConfig dbConfig = DataBaseConfig.getInstance();


	
	/**
	 * 获取数据库表前缀
	 * @return
	 */
	public String getPrefix(){
		return commonDao.getPreFix();//表前缀，如："yl_"
	}
	
	
	
 
}
