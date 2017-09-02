package org.marker.mushroom.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.Services;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


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

    public Channel getChannel(String tableName, String contentId) {
        String prefix = channelDao.getPreFix();
        JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
        String sql = "select a.* from "+prefix+"channel a join " + tableName+" b on b.cid = a.id WHERE b.id = "+ contentId+" limit 1";
        return jdbcTemplate.queryForObject(sql, new ObjectRowMapper.RowMapperChannel());
    }


	/**
	 * 获取全部栏目（以树形结构输出）
	 *
	 *
	 * @return List<JSONObject>
	 */
	public List<Map<String, Object>> getAllTree() {
        String prefix = commonDao.getPreFix();
        List<Map<String,Object>> list = commonDao.queryForList(
                "select c.* from  " +  prefix + "channel c order by c.sort asc");
        TreeUtils.processSimulateTree(list, 0, 0);

        return list;

    }




}
