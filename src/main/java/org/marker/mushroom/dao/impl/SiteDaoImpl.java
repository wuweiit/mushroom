package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.ISiteDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperCategory;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 分类Dao
 * 
 * @author marker
 * @version 1.0
 */
@Repository(DAO.SiteDao)
public class SiteDaoImpl extends DaoEngine implements ISiteDao {

	





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
