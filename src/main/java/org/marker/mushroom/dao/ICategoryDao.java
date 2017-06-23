package org.marker.mushroom.dao;

import java.util.List;

import org.marker.mushroom.beans.Category;
 

public interface ICategoryDao extends ISupportDao{

	List<Category> list();

    List<Category> findAll();

    Object findByGroupId(int userGroupId);

}
