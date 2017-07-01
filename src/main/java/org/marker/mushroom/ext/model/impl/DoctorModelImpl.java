package org.marker.mushroom.ext.model.impl;

import org.marker.mushroom.alias.SQL;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.ext.model.ContentModel;
import org.marker.mushroom.template.tags.res.WebDataSource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 医生数据模型实现
 *
 * @author marker
 * @version 1.0
 */
public class DoctorModelImpl extends ContentModel{


	public DoctorModelImpl() {
		Map<String,Object> config = new HashMap<String,Object>();
		config.put("icon", "images/demo.jpg");
		config.put("name", "医生模型");
		config.put("author", "marker");
		config.put("version", "0.1");
		config.put("type", "doctor");
		config.put("template", "doctor.html");
		config.put("description", "针对医疗行业");
		configure(config);
	}
	
	
	
	/**
	 * 抓取内容
	 */
	public void fetchContent(Serializable cid) {
		String prefix = getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();

		String sql = "select  M.*,C.name cname, concat('/cms?','type=doctor','&id=',CAST(M.id as char),'&time=',DATE_FORMAT(M.time,'%Y%m%d')) url from "+prefix+"category C "
				+ "right join "+prefix+"doctor M on M.did = C.id  where  M.id=?";
		Object article = commonDao.queryForMap(sql,cid); 
		commonDao.update("update " + prefix + "doctor set views = views+1 where id=?", cid);// 更新浏览量
		
		// 必须发送数据
		request.setAttribute("doctor", article);
	}



	public void doPage(Channel current, WebParam param) {




	}
	/**
	 * 处理分页
	 */
	public Page doPage(WebParam param) {
		String prefix = getPrefix();//表前缀，如："yl_" 



        long id = param.channel.getId();

        String categoryIds = ""+id;

		StringBuilder sql = new StringBuilder();
		sql.append("select A.*,C.name as cname,concat('type=doctor&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url from ")
		 .append(prefix).append("doctor").append(SQL.QUERY_FOR_ALIAS)
		 .append(" join ").append(prefix).append("category").append(" C on A.did=C.id ")
		.append("where 1=1 and ").append("A.cid in ("+categoryIds+") order by A.time desc").append(param.extendSql!= null?param.extendSql:"");

		return commonDao.findByPage(param.currentPageNo, param.pageSize, sql.toString());
	}
	
	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, WebDataSource WebDataSource) {
		String prefix = dbconfig.getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder("select  M.*,C.name cname, concat('/cms?','type=doctor','&id=',CAST(M.id as char),'&time=',DATE_FORMAT(M.time,'%Y%m%d')) url from" +
				" "+prefix+"category C "
				+ "right join "+prefix+"doctor M on M.did = C.id");
		 
		return sql;
	}
	
	 

	/**
	 * 备份数据
	 */
	public void backup(){
		
		
	}

	
	
	/**
	 * 恢复数据
	 */
	public void recover(){
		
	}
	
}
