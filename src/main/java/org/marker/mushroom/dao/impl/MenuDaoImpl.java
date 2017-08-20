/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.dao.DaoEngine;
import org.marker.mushroom.dao.IMenuDao;
import org.marker.mushroom.dao.mapper.ObjectRowMapper.RowMapperMenu;
import org.springframework.stereotype.Repository;

/**
 * @author marker
 * @date 2013-10-6 上午11:36:12
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
@Repository(DAO.MENU)
public class MenuDaoImpl extends DaoEngine implements IMenuDao {

	

	// 判断是否有子节点
	public boolean hasChildMenu(int menuId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(id) from ").append(getPreFix()).append("user_menu where pid = ?");
		Integer rows = this.jdbcTemplate.queryForObject(sql.toString(),new Object[]{menuId}, Integer.class); 
		return rows>0?true: false;
	}

	// 查询一级菜单
	public List<Menu> findTopMenu() { 
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_menu where pid = 0 order by sort");
		List<Menu> list = this.jdbcTemplate.query(sql.toString(), new RowMapperMenu()); 
		return list;
	}

	// 根据ID查询子菜单
	public List<Menu> findChildMenuById(int id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_menu where pid = ?  order by sort");
		List<Menu> list = this.jdbcTemplate.query(sql.toString(),new Object[]{id},new RowMapperMenu()); 
		return list;
	}

	// 根据ID查询菜单
	public Menu findMenuById(int id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_menu where id = ?");
		return this.jdbcTemplate.queryForObject(sql.toString(), new Object[]{id}, new RowMapperMenu() );  
	}

	
	// 根据分组ID查询菜单
	public List<Menu> findTopMenuByGroupId(Serializable groupId) { 
		StringBuilder sql = new StringBuilder();
		sql.append("select m.* from ").append(getPreFix()).append("user_menu m JOIN ").
		append(getPreFix()).append("user_group_menu gm on gm.mid =m.id where gm.gid = ? and m.pid = 0 order by m.sort");
		List<Menu> list = this.jdbcTemplate.query(sql.toString(),new Object[]{groupId},new RowMapperMenu()); 
		return list; 
	}
 

	// 根据父级ID和分组ID查询子菜单
	public List<Menu> findChildMenuByGroupAndParentId(Serializable groupId,
			Serializable parentId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select m.* from ").append(getPreFix()).append("user_menu m JOIN ").
		append(getPreFix()).append("user_group_menu gm on gm.mid =m.id where gm.gid = ? and m.pid = ? order by m.sort");
		List<Menu> list = this.jdbcTemplate.query(sql.toString(),new Object[]{groupId, parentId},new RowMapperMenu()); 
		return list; 
	}

	
	
	// 根据类型名称查询菜单ID
	public int findMenuIdByType(String type) { 
		StringBuilder sql = new StringBuilder();
		sql.append("select id from ").append(getPreFix()).append("user_menu where type=?"); 
		return super.queryForObject(sql.toString(), Integer.class, type);
	}

	
	
	@Override
	public void saveMenuToAdminGroup(Serializable menuId) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into ").append(getPreFix()).append("user_group_menu(gid,mid) values(1,?)"); 
		super.update(sql.toString(), menuId);
	}

	@Override
	public void deleteGroupMenu(Serializable menuId) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ").append(getPreFix()).append("user_group_menu where mid=?"); 
		super.update(sql.toString(), menuId);
	}
	
	
	@Override
	public Menu findByName(String name) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_menu where name=?");
		try{
			return this.jdbcTemplate.queryForObject(sql.toString(), new RowMapperMenu(), name);
		}catch(Exception e){}
		return null;
	}

	
	// 检查菜单是否存在
	public boolean checkType(String type) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(id) from ").append(getPreFix()).append("user_menu where type=?");
		try{
			return this.jdbcTemplate.queryForObject(sql.toString(), Boolean.class, type);
		}catch(Exception e){}
		return false;
	}

	@Override
	public Menu findChildMaxSortMenuByPId(int parentId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_menu where pid=? order by sort desc limit 1");
		try{
			return this.jdbcTemplate.queryForObject(sql.toString(), new RowMapperMenu(), parentId);
		}catch(Exception e){
			logger.error("", e);
		}
		return null;
	}

	@Override
	public Menu findByModuleId(String moduleId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPreFix()).append("user_menu where moduleId=?");
		try{
			return this.jdbcTemplate.queryForObject(sql.toString(), new RowMapperMenu(), moduleId);
		}catch(Exception e){}
		return null;
	}


}
