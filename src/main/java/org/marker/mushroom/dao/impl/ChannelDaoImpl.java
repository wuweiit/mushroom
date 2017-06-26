package org.marker.mushroom.dao.impl;
 
import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperChannel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;


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
//	@CachePut( "channelCache")
	public boolean update(Object entity) {
		return super.update(entity);
	}
	
	
	
	/**
	 * 通过url查询出对应的栏目信息，并缓存数据
	 * */
//	@Cacheable("channelCache")
	public Channel queryByUrl(String url) { 
		Channel channel = null;
		
		StringBuilder sql = new StringBuilder();
		sql.append("select concat('/cms?p=',c.url) url, c.*, d.content from ")
				.append(getPreFix()).append("channel c left join ")
				.append(getPreFix()).append("content d on d.id = c.contentId where c.url=? limit 1");
		try{
			channel = jdbcTemplate.queryForObject(sql.toString(), new Object[] { url },  new ObjectRowMapper.RowMapperChannelNew());
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
		StringBuilder sql = new StringBuilder("select * from ").append(getPreFix()).append("channel").append(" where hide=? order by sort asc");
		return jdbcTemplate.query(sql.toString(), new RowMapperChannel(),1);
	}

	@Override
	public Channel findChildMaxSortMenuByPId(long pid) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("channel where pid=? order by sort desc limit 1");
		try{
			return this.jdbcTemplate.queryForObject(sql.toString(), new RowMapperChannel(), pid);
		}catch(Exception e){
			logger.error("", e);
		}
		return null;
	}

	@Override
	public void updateEnd0(long pid) {
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(getPreFix()).append("channel set end=0 where pid=?");
		try{
		 	this.jdbcTemplate.update(sql.toString(), pid);
		}catch(Exception e){
			logger.error("", e);
		}
	}

    @Override
    public List<Channel> findByPid(int deptPid) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(getPreFix()).append("channel where pid=? order by sort desc");
        try{
            List<Channel> list =  this.jdbcTemplate.query(sql.toString(), new RowMapperChannel(), deptPid);

            list.add(findObjectId(deptPid));
            return list;
        }catch(Exception e){
            logger.error("", e);
        }
        return null;
    }

	@Override
	public List<Channel> list() {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.* from ").append(getPreFix())
				.append("channel c ").append(" order by c.sort asc") ;
		return this.jdbcTemplate.query(sql.toString(), new RowMapperChannel());
	}

	@Override
	public List<Channel> findByGroupId(int userGroupId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.* from ").append(getPreFix()).append("channel c  ")
				.append(" where c.id in (select distinct cid from "+getPreFix()+"user_group_channel where gid = ?) ")
				.append(" order by c.sort asc") ;
		return this.jdbcTemplate.query(sql.toString(), new RowMapperChannel(), userGroupId);
	}

	@Override
	public Channel findByUrl(String pageName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("channel where url=? order by sort desc limit 1");
		try{
			return this.jdbcTemplate.queryForObject(sql.toString(), new RowMapperChannel(), pageName);
		}catch(Exception e){
			logger.error("", e);
		}
		return null;
	}


	public Channel findObjectId(int deptPid){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(getPreFix()).append("channel where id=? order by sort desc limit 1");
        return this.jdbcTemplate.queryForObject(sql.toString(), new RowMapperChannel(), deptPid);
    }

}
