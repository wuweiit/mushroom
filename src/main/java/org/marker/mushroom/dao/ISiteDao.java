package org.marker.mushroom.dao;

import org.marker.mushroom.beans.Category;

import java.util.List;


public interface ISiteDao extends ISupportDao{

    List<Category> findAll();

    Object findByGroupId(int userGroupId);

}
