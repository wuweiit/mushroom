package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.ICategoryDao;
import org.springframework.stereotype.Repository;




@Repository(DAO.CATEGORY)
public class CategoryDaoImpl extends DaoEngine implements ICategoryDao {

	public CategoryDaoImpl( ) {
		super(null); 
	}

	
	
	
}
