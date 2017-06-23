package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.dao.IArticleTaoluDao;
import org.springframework.stereotype.Repository;


@Repository(DAO.ARTICLE_Taolu)
public class ArticleTaoluDaoImpl extends DaoEngine implements IArticleTaoluDao {
	
	
	
	
	@Override
	public boolean update(Article a) {
		String prefix = getPreFix();//获取数据库表前缀
		
		
		
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(prefix).append("taolu")
		.append(" set cid=?, title=?,keywords=?,description=?,author=?, content=?,status=?,source=?,icon=? where id=?");
		
		
		
		int status = jdbcTemplate.update(sql.toString(),a.getCid(),a.getTitle(),a.getKeywords(),a.getDescription()
				,a.getAuthor(),a.getContent(),a.getStatus(),a.getSource(),a.getIcon(),a.getId()); 
		return status>0? true : false; 
	}
	

}
