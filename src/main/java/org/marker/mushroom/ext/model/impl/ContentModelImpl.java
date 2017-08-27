package org.marker.mushroom.ext.model.impl;

/**
 * Created by marker on 17/5/19.
 */
import org.marker.mushroom.alias.SQL;
import org.marker.mushroom.alias.DAO;
import java.lang.StringBuilder;
import java.lang.Exception;
import java.util.HashMap;
import java.util.Map;

import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.dao.ISupportDao;
import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.template.tags.res.WebDataSource;




/**
 * 单页模型处理
 *
 * @author marker
 * */
public class ContentModelImpl extends ContentModel {


    public ContentModelImpl(){
        // 配置模型（必须调用）
        Map<String,Object> config = new HashMap<String,Object>();
        config.put("icon", "images/demo.jpg");
        config.put("name", "栏目模型");
        config.put("author", "marker");
        config.put("version", "0.1");
        config.put("type", "channel");// 模型标识
        config.put("template", "");
        config.put("description", "系统内置模型");
        configure(config);
    }



    /**
     * 前台标签生成SQL遇到该模型则调用模型内算法
     * @param tableName 表名称
     * */
    public StringBuilder doWebFront(String tableName, WebDataSource WebDataSource) {
        String prefix = getPrefix();// 表前缀，如："yl_"
        StringBuilder sql = new StringBuilder();
        sql.append("select M.*, concat('/cms?','p=',M.url) 'url' from ");
        sql.append(prefix).append("channel ").append(SQL.ALIAS_MODEL);
        return sql;
    }



    public void doPage(Channel current, WebParam param) {
        String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
        HttpServletRequest request = ActionContext.getReq();

        long cid  = current.getId();//当前栏目ID
        int limit = current.getRows();//每页内容条数


        int pageNo = 1;
        if(param.page != null && !"".equals(param.page)){
            try{
                pageNo = Integer.parseInt(param.page);
            }catch (Exception e) {e.printStackTrace(); }
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select A.id,A.title,C.name as cname ,A.time ,concat('/cms?p=',C.url,'&type=article&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url from ");
        sql.append(prefix).append("article").append(SQL.QUERY_FOR_ALIAS)
                .append(" join ").append(prefix).append("channel").append(" C on A.cid=C.id");
        sql.append(" where A.cid=").append(cid);


        ISupportDao commonDao = SpringContextHolder.getBean(DAO.COMMON);

        Page currentPage = commonDao.findByPage(pageNo, limit, sql.toString());

        request.setAttribute(AppStatic.WEB_APP_PAGE, currentPage);

        //传递分页信息
        String nextPage = "/cms?p="+param.pageName+"&page="+currentPage.getNextPageNo();
        String prevPage = "/cms?p="+param.pageName+"&page="+currentPage.getPrevPageNo();

        request.setAttribute("nextpage", urlrewrite.encoder(nextPage));
        request.setAttribute("prevpage", urlrewrite.encoder(prevPage));
    }


}
