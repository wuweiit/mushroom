package org.marker.mushroom.dao.impl;

import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.ICategoryDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperCategory;
import org.springframework.stereotype.Repository;



/**
 * 分类Dao
 * 
 * @author marker
 * @version 1.0
 */
@Repository(DAO.CATEGORY)
public class CategoryDaoImpl extends DaoEngine implements ICategoryDao {

	
	
	
	public List<Category> list(){
		StringBuilder sql = new StringBuilder();
		sql.append("select c.*, m.name modelName from ").append(getPreFix())
		.append("category c ").append(" left join ").append(getPreFix())
		.append("model m on c.model=m.type order by c.sort asc") ;
		return this.jdbcTemplate.query(sql.toString(), new RowMapperCategory()); 
	}




	@Override
	public List<Category> findAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.* from ").append(getPreFix())
				.append("category c order by c.sort asc") ;
		return this.jdbcTemplate.query(sql.toString(), new ObjectRowMapper.RowMapperCategoryNew());
	}

    @Override
    public List<Category> findByGroupId(int userGroupId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.* from ").append(getPreFix()).append("category c  ")
                .append(" where c.id in (select distinct cid from "+getPreFix()+"user_group_category where gid = ?) ")
                .append(" order by c.sort asc") ;
        return this.jdbcTemplate.query(sql.toString(), new RowMapperCategory(), userGroupId);
    }


}
