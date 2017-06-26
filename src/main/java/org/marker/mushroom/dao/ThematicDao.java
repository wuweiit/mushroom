package org.marker.mushroom.dao;

import org.marker.mushroom.beans.Article;

public interface ThematicDao extends ISupportDao{
	
	public boolean update(Article entity);


    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean updateStatus(Integer id, Integer status);
}
