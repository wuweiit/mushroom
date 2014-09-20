package org.marker.mushroom.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service(Services.ARTICLE)
public class ArticleService extends BaseService{


	@Autowired private ISupportDao commonDao;
	
	
	
	
	
	public Page find(int currentPageNo, int pageSize, Map<String,Object> condition) { 
		String keyword = (String) condition.get("keyword");
		String status = (String) condition.get("status");
		int cid = (Integer)condition.get("cid");
		try {
			keyword = new String(keyword.getBytes("iSO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
		}
		
		
		if(cid == 0){
			String sql = "select a.*, concat('/cms?type=',c.model,'&id=',''+a.id)  url, c.name as cname ,c.model from "+config.getPrefix()+"article as a "
					+ "left join "+config.getPrefix()+"category c on c.id = a.cid " 
					+ "where a.status in ("+status+") and a.title like ? order by a.id desc";
			return commonDao.findByPage(currentPageNo, pageSize,  sql,'%'+keyword+'%'); 
		}
		
		String sql = "select a.*, concat('/cms?type=',c.model,'&id=',''+a.id)  url, c.name  as cname ,c.model from "+config.getPrefix()+"article as a "
				+ "left join "+config.getPrefix()+"category c on c.id = a.cid " 
				+ "where a.status in ("+status+") and a.cid=? and a.title like ? order by a.id desc";
		
		
		return commonDao.findByPage(currentPageNo, pageSize,  sql, cid,'%'+keyword+'%'); 
	}
	
}
