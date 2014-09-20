package org.marker.mushroom.service;

import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * 基础业务层
 * 
 * 
 * 提供：日志处理接口
 * 
 * @author marker
 * @version 1.0
 */
public class BaseService {

	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.SERVICE_LAYER);
	
	/** 数据库配置 */
	protected DataBaseConfig config = DataBaseConfig.getInstance(); 
	
}
