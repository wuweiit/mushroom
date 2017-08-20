package org.marker.mushroom.dao;

 
import java.io.Serializable;
import java.util.List;

import org.marker.mushroom.beans.Menu;

/**
 * 菜单管理Dao
 * @author marker
 */
public interface IMenuDao  extends ISupportDao{

	boolean hasChildMenu(int menuId);

	/**
	 * 查询最高级菜单
	 */
	List<Menu> findTopMenu();
	
	/**
	 * 通过分组ID查询最顶级菜单
	 */
	List<Menu> findTopMenuByGroupId(Serializable groupId);
	
	/**
	 * 通过分组ID和父级ID查询子菜单
	 */
	List<Menu> findChildMenuByGroupAndParentId(Serializable groupId, Serializable parentId);
	
	
	
	/**
	 * 通过ID查询子菜单
	 * @param id
	 * @return
	 */
	List<Menu> findChildMenuById(int id);
	
	
	
	
	
	Menu findMenuById(int id);
	
	/**
	 * 根据类型查询菜单ID
	 * @param type
	 * @return
	 */
	int findMenuIdByType(String type);
	
	void saveMenuToAdminGroup(Serializable menuId);

	void deleteGroupMenu(Serializable menuId);
 

	Menu findByName(String name);
	
	
	
	/**
	 * 检查类型菜单是否存在
	 * @param name
	 * @return
	 */
	boolean checkType(String type);

	/**
	 * 查询最大排序的
	 * @param parentId
	 * @return
	 */
	Menu findChildMaxSortMenuByPId(int parentId);


	/**
	 * 根据模块Id查询
	 * @param moduleId 模块Id
	 * @return
	 */
    Menu findByModuleId(String moduleId);
}
