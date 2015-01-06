package org.marker.mushroom.freemarker;

import java.io.IOException;
import java.util.Map;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ICommonDao;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.service.impl.CategoryService;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;



/**
 * 分页指令
 * @author marker
 * @version 1.0
 */
public class PageDirective implements TemplateDirectiveModel {
	
	private ContentModelContext cmc;
	private CategoryService categoryService;
	public PageDirective() { 
		 cmc = ContentModelContext.getInstance();
		 categoryService = SpringContextHolder.getBean(Services.CATEGORY); 
	}
	
	@Override
	public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		String modelType = params.get("model").toString();// 内容模型
		int cid = 0;// 内容Id
		try{
			cid = Integer.parseInt(params.get("cid").toString());
		}catch(Exception e){
			throw new TemplateException("@page cid is number e.g: cid=\"12\" ", env);
		}
		
		
		String cids = categoryService.findChildIds(cid);
		
		int pageSize = 10;
		try{
			pageSize = Integer.parseInt(params.get("size").toString());
		}catch(Exception e){}
		
		WebParam param = WebParam.get();
		param.pageSize = pageSize;// 页面大小 
		param.extendSql = "A.cid in(" + cids + ")";
		
		
		ContentModel cm = cmc.get(modelType);
		
		Page page = cm.doPage(param);
		
		
		
		
		env.setVariable("page", ObjectWrapper.DEFAULT_WRAPPER.wrap(page));
		body.render(env.getOut());
	}

}
