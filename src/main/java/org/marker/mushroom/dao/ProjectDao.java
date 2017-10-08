package org.marker.mushroom.dao;

import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Project;


/**
 * 项目管理
 *
 * @author marker
 */
public interface ProjectDao extends ISupportDao{
	
	public boolean update(Project entity);


    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(Integer id, Integer status);
}
