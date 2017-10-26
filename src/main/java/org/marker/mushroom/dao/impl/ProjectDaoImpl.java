package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Project;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IArticleDao;
import org.marker.mushroom.dao.ProjectDao;
import org.springframework.stereotype.Repository;


/**
 * 项目Dao
 *
 */
@Repository("projectDao")
public class ProjectDaoImpl extends DaoEngine implements ProjectDao {
	
	
	
	
	@Override
	public boolean update(Project a) {
		String prefix = getPreFix();//获取数据库表前缀

		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(prefix).append("project")
		.append(" set cid=?,did=?, title=?,keywords=?,description=?,author=?, content=?,status=?,source=?" +
                ",icon=?,location=?,customer=?,scale=?,startTime=?,extJson=?,stick=? where id=?");

		
		int status = jdbcTemplate.update(sql.toString(),a.getCid(),a.getDid(), a.getTitle(),a.getKeywords(),a.getDescription()
				,a.getAuthor(),a.getContent(),a.getStatus(),a.getSource()
                ,a.getIcon(),a.getLocation(),a.getCustomer(),a.getScale(),a.getStartTime(),a.getExtJson(),a.getStick(), a.getId());
		return status>0? true : false; 
	}

	@Override
	public boolean updateStatus(Integer id, Integer status) {
		String prefix = getPreFix();//获取数据库表前缀
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(prefix).append("article")
				.append(" set status=?,updateTime=sysdate() where id=?");
		int a = jdbcTemplate.update(sql.toString(), status , id );
		return a>0? true : false;
	}


}
