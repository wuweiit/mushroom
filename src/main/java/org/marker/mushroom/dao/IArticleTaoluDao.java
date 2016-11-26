package org.marker.mushroom.dao;

import org.marker.mushroom.beans.Article;

public interface IArticleTaoluDao extends ISupportDao{
	
	public boolean update(Article entity);
	
}
