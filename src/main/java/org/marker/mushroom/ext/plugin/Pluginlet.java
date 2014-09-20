package org.marker.mushroom.ext.plugin;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.holder.SpringContextHolder;


/**
 * 分发器
 * 
 * @author marker
 * @version 1.0
 */
public abstract class Pluginlet {
	
	public static final String CONFIG_TYPE = "type";
	
	/** 数据库模型引擎 */
	public ISupportDao commonDao;
	 
	
	// 定义路由
    protected Map<String,String> routers;
    
    /** 配置信息 */
    protected Map<String,Object> _config;
	
    
    
	/**
	 * 初始化一些必要工具
	 */
	public Pluginlet() {
		this.commonDao = SpringContextHolder.getBean(DAO.COMMON);
	}
	
    
    
	/**
	 * 获取HttpServletRequest 请求对象
	 * @return
	 */
	public HttpServletRequest getServletRequest(){
		return ActionContext.getReq();
	}
	
	
	/**
	 * 获取HttpServletResponse 响应对象
	 * @return
	 */
	public HttpServletResponse getServletResponse(){
		return ActionContext.getResp();
	}
	
	
	/**
	 * 获取当前会话(Session)对象
	 * @return
	 */
	public HttpSession getSession(){
		return ActionContext.getReq().getSession();
	}
	
	
	/**
	 * 获取当前会话(Session)对象
	 * @param boolean 是否创建新的Session
	 * @return
	 */
	public HttpSession getSession(boolean is){
		return ActionContext.getReq().getSession(is);
	}
	
	
	/**
	 * 获取ServletContext
	 * @return
	 */
	public ServletContext getServletContext(){
		return ActionContext.getApplication(); 
	}
	
}
