package org.marker.mushroom.support;

import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.dao.ICommonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;



/**
 * 控制器支撑类
 * @author marker
 * */
public abstract class SupportController implements InitializingBean {

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


	@Override
	public void afterPropertiesSet() {
		if(StringUtils.isNotBlank(this.viewPath)){
			return;
		}
		RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
		String url = requestMapping.value()[0];
		log.info("[{}] viewPath: {}", this.getClass().getSimpleName(), url);
		this.viewPath = url;
	}
}
