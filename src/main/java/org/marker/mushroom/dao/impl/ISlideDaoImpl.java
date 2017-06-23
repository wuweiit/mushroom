package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Article;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.ISlideDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 幻灯片管理 Dao'
 *
 * @author marker
 *
 * Created by marker on 17/6/8.
 */
@Repository(DAO.SLIDE)
public class ISlideDaoImpl extends DaoEngine implements ISlideDao {
    @Override
    public boolean update(Article entity) {
        return false;
    }

    @Override
    public List<Map<String,Object>> findAll(Class<?> clzz) {
        String sql = "select * from "+getPreFix()+"slide";
        return super.queryForList(sql);
    }
}
