package org.marker.mushroom.dao.impl;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.UserGroupChannel;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.UserGroupChannelDao;
import org.marker.mushroom.dao.annotation.Entity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marker
 * Created by marker on 2017/6/19.
 */
@Repository(DAO.UserGroupChannelDao)
public class UserGroupChannelDaoImpl extends DaoEngine implements UserGroupChannelDao {
    @Override
    public void batchSave(int gid, String[] cids) {
        String prefix = getPreFix();
        Entity tableInfo = UserGroupChannel.class.getAnnotation(Entity.class);
        String tableName = tableInfo.value();


        String sql = "insert into " + prefix+tableName + " values(?,?)";
        List<Object[]> args = new ArrayList<>();
        for(String cid: cids){
            int cidInt = Integer.parseInt(cid);
            args.add(new Object[]{gid, cidInt});
        }
        super.batchUpdate(sql,args);
    }

    @Override
    public void deleteAllByGid(int gid) {
        String prefix = getPreFix();
        Entity tableInfo = UserGroupChannel.class.getAnnotation(Entity.class);
        String tableName = tableInfo.value();
        String sql = "delete from "+prefix+tableName +" where gid = ?";
        jdbcTemplate.update(sql, gid);
    }
}
