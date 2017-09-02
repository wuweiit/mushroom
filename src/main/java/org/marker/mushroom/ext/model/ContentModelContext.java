package org.marker.mushroom.ext.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.urlrewrite.URLRewriteEngine;


/**
 * 内容模型作用域
 *
 *
 * 
 * @author marker
 * @version 1.0
 */
public class ContentModelContext implements IContentModelParse {

	

	 
	
	/** 存放模型的集合(key:类型 value:模型对象) */
	private final Map<String,ContentModel> contentModels = new ConcurrentHashMap<String,ContentModel>();
	
	private IChannelDao channelDao ;

	
	 
	
	private ContentModelContext(){ 
		channelDao = SpringContextHolder.getBean(DAO.CHANNEL);

	}
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static ContentModelContext getInstance(){
		return SingletonHolder.instance;
	}


    /**
     * 获取自定义的表前缀
     * @param modelType
     * @return
     */
    public String getPrefix(String modelType) {
        ContentModel mod = contentModels.get(modelType); // 获取模型
        if(mod != null){
            String prefix = mod.getModelPrefix();
            if(!org.springframework.util.StringUtils.isEmpty(prefix)){
                return prefix;
            }
        }
        return "mr_";
    }


    /**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static ContentModelContext instance = new ContentModelContext();     
	}
	
	
	/**
	 * 添加模型
	 */
	public void put(ContentModel model){ 
		String type = model.get(ContentModel.CONFIG_TYPE).toString();  
		// 上下文同步
	    contentModels.put(type, model); 

		
	}
	
	
	

	
	


	/**
	 * 
	 * WebParam{
	 * 		pageName=about
	 *		moduleType=channel
	 *		contentId=null
	 *		page=null
	 * 
	 * }
	 * 
	 * { // 子栏目
	 * 		pageName = document/module
	 *		moduleType = channel
	 *		contentId = null
	 *		page=null
	 * }
	 * {// 内容
	 * 		pageName=index
			moduleType=article
			contentId=327
			page=null
		}
	 */
	public int parse(WebParam param) throws SystemException {
		HttpServletRequest request = ActionContext.getReq();//获取请求对象

		SystemConfig syscfg = SystemConfig.getInstance();
		
		// 如果内容id不等于0，内容查询
		if(param.contentId != null && !"0".equals(param.contentId)){

            ContentModel modle = contentModels.get(param.modelType);// 获取内容模型对象

			Integer cid = Integer.valueOf(param.contentId);


			// 计算面包屑导航





			// 计算当前栏目数据




			try{
				modle.fetchContent(cid);// 通过内容id抓取内容数据
			}catch(Exception e){
			    e.printStackTrace();
			    return 0; }
			
			
			param.template = modle.get("template").toString();// 模型的模板
		  
			return 2;
			
		}else{ // 查询当前访问的栏目信息，栏目信息里面包含模型调用对应的模型库
			if("page".equals(param.action) ){
				Channel currentChannel = channelDao.queryByUrl(param.pageName);
				param.channel = currentChannel;// 设置栏目参数
				if(currentChannel != null){

					String keywords    = currentChannel.getKeywords();
					String description = currentChannel.getDescription();
					if("".equals(description)){
						description = syscfg.get(SystemConfig.Names.DESCRIPTION);
						currentChannel.setDescription(description);
					}
					if("".equals(keywords)){
						keywords = syscfg.get(SystemConfig.Names.KEYWORDS);
						currentChannel.setKeywords(keywords);
					}


					request.setAttribute(AppStatic.WEB_CURRENT_CHANNEL, currentChannel);
					param.template   = currentChannel.getTemplate();//模板
					param.modelType = "article";//内容模型
					if(142 == currentChannel.getId()){// 专题内容模型
						param.modelType = "thematic";
					}

					/*
					 * 重定向重新定义
					 */
					String redirect = currentChannel.getRedirect();
					if(!StringUtils.isEmpty(redirect)){
						if(redirect.indexOf("http") != -1){
							param.redirect = redirect;
						}else{

							URLRewriteConfig urlConfig = URLRewriteConfig.getInstance();
							param.redirect = redirect.indexOf(".") != -1?redirect:(redirect + urlConfig.getPageSuffix());
						}
					}


					if(currentChannel.getRows() != 0){
						param.pageSize = currentChannel.getRows();
					}

					if(param.redirect != null && !"".equals(param.redirect)){
						return 1;//如果重定向地址不为null
					}


                    ContentModel modle = contentModels.get(param.modelType);// 获取内容模型对象

					if(modle != null){
						Page page = modle.doPage( param);

						request.setAttribute(AppStatic.WEB_APP_PAGE, page);

						URLRewriteEngine urlRewrite = SingletonProxyFrontURLRewrite.getInstance();

				//                    传递分页信息
						String nextPage = "p="+param.pageName+"&page=" + page.getNextPageNo();
						String prevPage = "p="+param.pageName+"&page=" + page.getPrevPageNo();
						request.setAttribute("nextpage", urlRewrite.encoder(nextPage));
						request.setAttribute("prevpage", urlRewrite.encoder(prevPage));


						return 2;
					}
					return 2;

				}
			}else{
                return IContentModelParse.STATUS_MODULE;
            }
		} 
		return 0;
	}


	
	/**
	 * 这里有点问题，不能是tableName
	 */
	@Override
	public StringBuilder parse(String tableName, WebDataSource WebDataSource) throws SystemException {
	 
		//当type=null的时候应该获取栏目的模型，然后进行处理
		ContentModel mod = contentModels.get(tableName);//获取模型
		if(mod != null){
			return mod.doWebFront(tableName, WebDataSource);
		}
		return null; 
	}


	
	public Object getCurrentList() {
		List<Object> list = new ArrayList<Object>();
		Set<String> sets = contentModels.keySet();
		Iterator<String> it =sets.iterator(); 
		while(it.hasNext()){
			String  a = it.next(); 
			ContentModel cm = contentModels.get(a); 
			list.add(cm.getConfig());
		}
		return list;
	}



	/**
	 * 删除模型
	 * @param modelType
	 */
	public void remove(String modelType) { 
		contentModels.remove(modelType);
	}


	
	
	public ContentModel get(String modelType) {
		return this.contentModels.get(modelType);
	}
	
	
 
	
	
}
