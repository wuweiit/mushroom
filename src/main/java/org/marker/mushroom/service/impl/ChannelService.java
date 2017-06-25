package org.marker.mushroom.service.impl;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 分类业务层处理
 * @author marker
 * @version 1.0
 */
@Service(Services.CHANNEL)
public class ChannelService extends BaseService {

	@Autowired private ISupportDao commonDao;
	
	@Autowired private IChannelDao channelDao;



	/**
	 * 查询所有分类信息
	 * @return List<Map<String, Object>>
	 */
	public List<Channel> list() {
		return channelDao.list();
	}


    /**
     * 获取用户组的栏目信息
     * @param userGroupId 用户组Id
     * @return
     */
	public List<Channel> getUserGroupChannel(int userGroupId) {
		if(Core.ADMINI_GROUP_ID == userGroupId){
			return channelDao.findAll();
		}else{
			return channelDao.findByGroupId(userGroupId);
		}
	}

	public List<Channel> getAll() {
		return channelDao.findAll();
	}

    public Channel getByUrl(String pageName) {
        return channelDao.findByUrl(pageName);
    }

    public Channel getChannel(String modelType, String contentId) {
        String prefix = channelDao.getPreFix();
        JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
        String sql = "select a.* from "+prefix+"channel a join "+prefix + modelType+" b on b.cid = a.id WHERE b.id = "+ contentId+" limit 1";
        return jdbcTemplate.queryForObject(sql, new ObjectRowMapper.RowMapperChannel());
    }
}
