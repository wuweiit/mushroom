package org.marker.mushroom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.beans.User;
import org.marker.mushroom.beans.UserGroup;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IUserDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperUser;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperUserGroup;
import org.springframework.stereotype.Repository;


@Repository("userDao")
public class UserDaoImpl extends DaoEngine implements IUserDao{

	
	
	
	
	/**
	 * 通过用户名和密码查询用户对象
	 * */
	@Override
	public User queryByNameAndPass(String name, String pass) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(prefix).append("user").append(" where name=? and pass=?");
		User user = null; 
		try{
			user = this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{name, pass}, new RowMapperUser());
		}catch (Exception e) {
			logger.error("{}", e);
		}
		
		return user;
	}


	@Override
	public boolean updateLoginTime(Serializable id) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("update ");
		sql.append(prefix).append("user ").append("set logintime=sysdate() where id=?");
		
		int status = jdbcTemplate.update(sql.toString(), id);
		return status>0?true:false;
	}


	/* (non-Javadoc)
	 * @see org.marker.mushroom.dao.IUserDao#findUserByName(java.lang.String)
	 */
	@Override
	public User findUserByName(String userName) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(prefix).append("user").append(" where name=? or email = ?");
		User user = null; 
		try{
			user = this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{userName,userName}, new RowMapperUser());
		}catch (Exception e) {
			logger.error("通过name查询用户失败!", e);
		}
		
		return user; 
	}


	@Override
	public List<UserGroup> findGroup() {
		StringBuilder sql = new StringBuilder("select * from ");
		sql.append(getPreFix()).append("user_group"); 
		return this.jdbcTemplate.query(sql.toString(), new RowMapperUserGroup());
	}


	// 统计用户组的用户数量
	@Override
	public int countUserByGroupId(int groupId) {
		StringBuilder sql = new StringBuilder("select count(*) from ");
		sql.append(getPreFix()).append("user u where u.gid=? "); 
		
		return this.queryForObject(sql.toString(), Integer.class, new Object[]{groupId});
	}

	@Override
	public void updateToken(int userId, String token) {
		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("update ");
		sql.append(prefix).append("user ").append("set token=?, logintime=sysdate() where id=?");
		this.update(sql.toString(),token, userId);
	}

    @Override
    public boolean existsUserName(String username) {
        StringBuilder sql = new StringBuilder("select count(1) from ");
        sql.append(getPreFix()).append("user u where u.name = ? or u.email=?");
        return this.exists(sql.toString(), username,username);
    }

    @Override
    public void save(User user) {
        super.save(user);
    }

	@Override
	public boolean existEmail(String email) {
		StringBuilder sql = new StringBuilder("select count(1) from ");
		sql.append(getPreFix()).append("user u where u.email = ? ");
		return this.exists(sql.toString(), email);
	}

	@Override
	public void updateField(int userId, String field, String value) {

		String prefix = getPreFix();
		StringBuilder sql = new StringBuilder("update ");
		sql.append(prefix).append("user ").append("set "+field+"=? where id=?");
		this.update(sql.toString(),value, userId);

	}

}
