package org.marker.mushroom.dao.impl;
 
import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperChannel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;


/**
 * 栏目数据库操作对象
 * @author marker
 * @date 2013-11-15 下午5:31:58
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@Repository(DAO.CHANNEL)
public class ChannelDaoImpl extends DaoEngine implements IChannelDao{
	
	
	
	
	 //设定spring的ecache缓存策略,当编辑机构时候,把缓存全部清除掉,以达到缓存那数据同步;
	@Override
	@TriggersRemove(cacheName="channelCache",when=When.BEFORE_METHOD_INVOCATION, removeAll=true)
	public boolean update(Object entity) {
		return super.update(entity);
	}
	
	
	
	/**
	 * 通过url查询出对应的栏目信息，并缓存数据
	 * */
	@Cacheable(cacheName = "channelCache")
	public Channel queryByUrl(String url) { 
		Channel channel = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("select concat('/cms?p=',c.url) url, c.* from ").append(getPreFix()).append("channel c where c.url=?");
		try{
			channel = jdbcTemplate.queryForObject(sql.toString(), new Object[] { url },  new RowMapperChannel());
		}catch (EmptyResultDataAccessException e) { 
			logger.error("channel.url="+url+" not found! ");
		}
		return channel;
	}

	
	
	/**
	 * 查询所有栏目
	 */
	public List<Channel> findAll() {
		StringBuilder sql = new StringBuilder("select * from ").append(getPreFix()).append("channel");
		return jdbcTemplate.query(sql.toString(), new RowMapperChannel()); 
	}



	@Override
	public Channel queryByArticleId(Serializable aid) {
		Channel channel = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("select concat('/cms?p=',c.url) url, c.* from ").append(getPreFix()).append("channel c ")
		.append("join ").append(getPreFix()).append("article a on  a.pid=c.id where a.id=?");
				
		try{
			channel = jdbcTemplate.queryForObject(sql.toString(), new Object[] { aid },  new RowMapperChannel());
		}catch (EmptyResultDataAccessException e) { 
			logger.error("channel.aid="+aid+" not found! ");
		}
		return channel;
	}



	@Override
	public List<Channel> findValid() {
		StringBuilder sql = new StringBuilder("select * from ").append(getPreFix()).append("channel").append(" where hide=?");
		return jdbcTemplate.query(sql.toString(), new RowMapperChannel(),1);
	}
	
	
}
