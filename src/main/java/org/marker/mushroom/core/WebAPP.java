package org.marker.mushroom.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.alias.LOG;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.BreadNav;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.marker.mushroom.ext.model.IContentModelParse;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.service.impl.ArticleService;
import org.marker.mushroom.service.impl.ChannelService;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.SendDataToView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 前台处理核心对象
 * 
 * 应用控制类，完成网址解析，单一入口控制，静态页面缓存功能
 * 
 * @author marker
 * */
public final class WebAPP {

	/** 日志记录器 */ 
	protected Logger logger =  LoggerFactory.getLogger(LOG.TEMPLATE_ENGINE); 
	
	
	/** 请求响应相关的对象 */
	private HttpServletRequest request;
	private HttpServletResponse response; 


	/** 模版引擎 */
	private static MyCMSTemplate cmstemplate;
	/** 内容模型工厂 */
	private static ContentModelContext cmc;
	
	/** 发送数据到视图对象 */
	private static SendDataToView dataToView;
	
	/**
	 * 安装状态（注意：容器初始化赋值）
	 * @see org.marker.mushroom.holder.InitBuilderHolder
	 */
	public static boolean install = false;
	
	// 成员变量是否初始化
	public static boolean initialization = false;
	
	
	
	/**
	 * 创建APP实例 
	 * @return WebAPP
	 */
	public static WebAPP newInstance() {
		return new WebAPP();
	} 
	
	
	
	/**
	 * 私有构造方法 ，避免外部被实例化
	 */
	private WebAPP() {
		this.request     = ActionContext.getReq();
		this.response    = ActionContext.getResp();
//		this.application = ActionContext.getApplication();
		 
		/*
		 * 项目刚初始化成功，能处理大批量的并发请求
		 * 
		 * */
		if(!initialization){
			synchronized (WebAPP.class) {
				
				//初始化模板引擎
				if(WebAPP.cmstemplate == null)
					WebAPP.cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE); 
				
				// 数据发送对象
				dataToView = new SendDataToView(cmstemplate);
				
				//初始化模型工厂
			    if(cmc == null){
					cmc = ContentModelContext.getInstance(); 
				}
			    initialization = true;
		    }
		}
	}



	
	/**
	 * 启动App
	 * */
	public synchronized void start(){
		
		/* ====================================================
		 *                 检查系统是否安装
		 * ====================================================
		 */
		if ( !install ) { 
			try {
				logger.error("mrcms not install");
				response.sendRedirect("install/index.jsp");// 没有安装则进入安装页面
				return; // 处理完毕直接返回。
			} catch (IOException e) {
				logger.error("",e);
			}
		}
	
		
		/* ====================================================
		 *                        採集碎片
		 * ====================================================
		 */
		IChip chipData = SpringContextHolder.getBean(SystemStatic.SYSTEM_CMS_CHIP);
		request.setAttribute(AppStatic.WEB_APP_CHIP, chipData.getVector());

        ChannelService channelService = SpringContextHolder.getBean(Services.CHANNEL);
		
		WebParam param = WebParam.get(); // 解析地址

		if("page".equals(param.action)){ // 页面
			// 获取当前栏目信息
			if(param.contentId != null){
				param.prefix = cmc.getPrefix(param.modelType);
				String tableName = param.prefix + param.modelType;
				param.channel = channelService.getChannel(tableName, param.contentId);
				param.pageName = param.channel.getUrl();
			}else{
				param.channel = channelService.getByUrl(param.pageName);
			}

		}else if("search".equals(param.action)){// 搜索
            param.template = "search.html";
            ArticleService articleService = SpringContextHolder.getBean(Services.ARTICLE);


            Page page = articleService.search(param);
            request.setAttribute("page", page);
            request.setAttribute("keywrods", param.keywords);
		}





		// 查询当前栏目和 面包屑数
		List<Channel> channelList = channelService.getAll();
        List<BreadNav> breadNavs = new ArrayList<>(4);
        getBreadNav(channelList, param.channel , breadNavs);
        // 移除首页
        if(breadNavs.size()>1 && "p=index".equals(breadNavs.get(breadNavs.size()-1).getUrl()) ){
            breadNavs.remove(breadNavs.size()-1);
        }
        Collections.reverse(breadNavs);
        request.setAttribute("breadNav", breadNavs);




		try {
			// 这里将模式分为：重定向模型|内容模型
			int statusCode = cmc.parse(param);
			switch (statusCode) {
			case IContentModelParse.STATUS_REDIRECT: //如果是重定向，不是依赖内容模型
				response.sendRedirect(param.redirect);
				return;
			case IContentModelParse.STATUS_MODULE: // 内容模型
				cmstemplate.proxyCompile(param.template);// 代理编译 
				break;
			default:
				response.sendError(404); return;//页面肯定不存在
			}
			
			
			dataToView.process(param.template);
			
			
			
		}catch(SystemException syse){ logger.error("", syse);
			handleErrorMessage(syse); 
		}catch (FileNotFoundException e) { logger.error("", e);
			handleErrorMessage(new SystemException(e.getMessage() + " 模板未找到"));
		} catch (IOException e) {
			logger.error("", e); 
		}finally{
			this.destory();
		}
	}


    /**
     * 迭代出面包屑导航
     * @param channelList
     * @param current 栏目对象
     * @param breadNavs
     */
    private void getBreadNav(List<Channel> channelList, Channel current, List<BreadNav> breadNavs) {
    	if(current == null) return;
	    for(Channel channel : channelList){
	        Integer channelPageName = channel.getId();
	        if(channelPageName == current.getId() ){
                BreadNav nav = new BreadNav();
                nav.setName(channel.getName());
                nav.setUrl("p="+ current.getUrl());
	            breadNavs.add(nav);
	            getBreadNav4Pid(channelList, channel.getPid(), breadNavs);
            }
        }
    }

    private void getBreadNav4Pid(List<Channel> channelList, long pid, List<BreadNav> breadNavs) {
        for(Channel channel : channelList){
            if(channel.getId() == pid){
                BreadNav nav = new BreadNav();
                nav.setName(channel.getName());
                nav.setUrl("p="+ channel.getUrl());
                breadNavs.add(nav);
                getBreadNav4Pid(channelList, channel.getPid(), breadNavs);
            }
        }
    }

    /**
	 * 发送错误消息
	 * @throws SystemException
	 */
	public void handleErrorMessage(SystemException e) {
        SystemConfig syscfg = SystemConfig.getInstance();

		String errorTemplate = syscfg.get("error_page"); 
//		String errorMessage = ErrorConfig.getInstance().get(errorCode);
		 
		
		
		this.request.setAttribute("err_code","MR-".concat("0000"));
		this.request.setAttribute("error",e.getErrMessage());
		
		File errorFile = new File(WebRealPathHolder.REAL_PATH + syscfg.getThemeActive()+ errorTemplate);
		if(!errorFile.exists()){ 
			try {
				Writer out = response.getWriter();
				out.write(e.renderHTML());
				out.flush(); out.close();
			} catch (IOException e2) { 
				logger.error("",e2); 
			}
		} else {
			try {
				cmstemplate.proxyCompile(errorTemplate);
				dataToView.process(errorTemplate); 
			} catch (Exception e3) { 
				logger.error("",e3); 
			}
		}
		
		this.destory();
	}
	
	
	
	
	/**
	 *  销毁App
	 * */
	public void destory() {
		//清除当前请求对象和响应对象
		ActionContext.remove();
	}

}
