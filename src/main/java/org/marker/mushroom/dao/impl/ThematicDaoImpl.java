package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.dao.ThematicDao;
import org.springframework.stereotype.Repository;


@Repository(DAO.Thematic)
public class ThematicDaoImpl extends DaoEngine implements ThematicDao {
	
	
	
	
	@Override
	public boolean update(Article a) {
		String prefix = getPreFix();//获取数据库表前缀
		
		
		
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(prefix).append("thematic")
		.append(" set cid=?,did=?, title=?,keywords=?,description=?,author=?, content=?,status=?,source=?,icon=? where id=?");
		
		
		
		int status = jdbcTemplate.update(sql.toString(),a.getCid(),a.getDid(), a.getTitle(),a.getKeywords(),a.getDescription()
				,a.getAuthor(),a.getContent(),a.getStatus(),a.getSource(),a.getIcon(),a.getId()); 
		return status>0? true : false; 
	}

	@Override
	public boolean updateStatus(Integer id, Integer status) {
		String prefix = getPreFix();//获取数据库表前缀
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(prefix).append("thematic")
				.append(" set status=?,updateTime=sysdate() where id=?");
		int a = jdbcTemplate.update(sql.toString(), status , id );
		return a>0? true : false;
	}


}
