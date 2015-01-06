package org.marker.mushroom.ext.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Model;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.IModelDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.tags.res.SqlDataSource;


/**
 * 内容模型作用域
 * 
 * @author marker
 * @version 1.0
 */
public class ContentModelContext implements IContentModelParse {

	
	 
	/** 系统配置信息 */
	public static final SystemConfig sysConfig = SystemConfig.getInstance();
	 
	
	/** 存放模型的集合(key:类型 value:模型对象) */
	private final Map<String,ContentModel> contentModels = new ConcurrentHashMap<String,ContentModel>();
	
	private IChannelDao channelDao ;
	
	private IModelDao modelDao;
	
	
	 
	
	private ContentModelContext(){ 
		channelDao = SpringContextHolder.getBean(DAO.CHANNEL); 
		modelDao = SpringContextHolder.getBean(DAO.MODEL);
		
	}
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static ContentModelContext getInstance(){ 
		return SingletonHolder.instance;
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
	    
	    List<?> models = modelDao.queryAll(); 
		for(Object obj : models){
			Model m = (Model)obj;
			if(!(type != null && !type.equals(m.getType()))){// 如果数据库中存在 
				return;
			}
		}
		
		
		// 数据库同步
		modelDao.save(model.getConfig());
		
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
		
		// 如果内容id不等于0，内容查询
		if(param.contentId != null && !"0".equals(param.contentId)){
			// 获取内容模型对象
			ContentModel cm = contentModels.get(param.modelType); 
			 
			Integer cid = Integer.valueOf(param.contentId);
			try{ 
				cm.fetchContent(cid);// 通过内容id抓取内容数据 
			}catch(Exception e){ return 0; }
			
			
			param.template = cm.get("template").toString();// 模型的模板
		  
			return 2;
			
		}else{ //查询当前访问的栏目信息，栏目信息里面包含模型调用对应的模型库
			Channel currentChannel = channelDao.queryByUrl(param.pageName);
			if(currentChannel != null){ 
				String keywords    = currentChannel.getKeywords();
				String description = currentChannel.getDescription(); 
				if("".equals(description)){
					description = sysConfig.get(SystemConfig.Names.DESCRIPTION);
					currentChannel.setDescription(description);
				}
				if("".equals(keywords)){
					keywords = sysConfig.get(SystemConfig.Names.KEYWORDS); 
					currentChannel.setKeywords(keywords);
				}
				
				
				
				request.setAttribute(AppStatic.WEB_CURRENT_CHANNEL, currentChannel);
				param.template   = currentChannel.getTemplate();//模板
				param.modelType = "article";//内容模型
				param.redirect   = currentChannel.getRedirect();//重定向地址
				if(param.redirect != null && !"".equals(param.redirect)){
					return 1;//如果重定向地址不为null
				}
				
				// 查到栏目对应的模型，然后进行相应操作
				ContentModel cm = contentModels.get(param.modelType);// 获取内容模型对象 
				
				if(cm != null){ 
//					if(param.page != null && !"".equals(param.page)){//内容
//						cm.doPage(currentChannel,param); 
//					}
					return 2;
				} 
				return 2;
				
			} 
		} 
		return 0;
	}


	
	/**
	 * 这里有点问题，不能是tableName
	 */
	@Override
	public StringBuilder parse(String tableName, SqlDataSource sqldatasource) throws SystemException {
	 
		//当type=null的时候应该获取栏目的模型，然后进行处理
		ContentModel mod = contentModels.get(tableName);//获取模型
		if(mod != null){
			return mod.doWebFront(tableName, sqldatasource);
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
		modelDao.deleteByType(modelType); 
	}


	
	
	public ContentModel get(String modelType) {
		return this.contentModels.get(modelType);
	}
	
	
 
	
	
}
