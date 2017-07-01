package org.marker.mushroom.ext.model.impl;

import com.alibaba.druid.util.StringUtils;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Model;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.core.config.impl.URLRewriteConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewrite;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.IModelDao;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.ext.model.IContentModelParse;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.urlrewrite.URLRewriteEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 内容模型作用域
 *
 *
 * 
 * @author marker
 * @version 1.0
 */
public class CategoryModelImpl extends ContentModel {



	public CategoryModelImpl() {
		Map<String,Object> config = new HashMap<String,Object>();
		config.put("icon", "images/demo.jpg");
		config.put("name", "科室模型");
		config.put("author", "marker");
		config.put("version", "0.1");
		config.put("type", "category");
		config.put("template", "content/dept.html");
		config.put("description", "针对医疗行业");
		configure(config);
	}

    /**
     * 抓取内容
     */
    public void fetchContent(Serializable cid) {
        String prefix = getPrefix();//表前缀，如："yl_"
        HttpServletRequest request = ActionContext.getReq();

        String sql = "select  M.*, concat('/cms?','type=category','&id=',CAST(M.id as char)) url from "+prefix+"category M "
                + " where  M.id=?";
        Object article = commonDao.queryForMap(sql,cid);
//        commonDao.update("update " + prefix + "doctor set views = views+1 where id=?", cid);// 更新浏览量

        // 必须发送数据
        request.setAttribute("category", article);
    }


	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, WebDataSource WebDataSource) {
		String prefix = dbconfig.getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder("select  M.*, concat('/cms?','type=category','&id=',CAST(M.id as char)) url from "+prefix+"category M ");

		return sql;
	}





}
