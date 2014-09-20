package org.marker.mushroom.ext.menu;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.dao.IMenuDao;
import org.marker.mushroom.holder.SpringContextHolder;


/**
 * 菜单工具类
 * 负责三方框架操作添加对应的菜单
 * 
 * 必须在Spring初始化完成调用！
 * 
 * 
 * @author marker
 * @version 1.0
 */ 
public class MenuUtils {

	// 菜单操作Dao
	private IMenuDao menuDao;
	
	
	/**
	 * 构造方法，初始化各种成员变量
	 */
	public MenuUtils() {
		menuDao = SpringContextHolder.getBean(DAO.MENU);
	}
	
	
	
	/**
	 * 
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * 
	 * */
	private static class SingletonHolder {
		public final static MenuUtils instance = new MenuUtils();     
	}
	
	
	
	/**
	 * 
	 * 获取菜单工具实例
	 * 
	 * */
	public static MenuUtils getInstance(){
		return SingletonHolder.instance;
	}
	
	
	
	
	
	
	public Menu builder(String type , Menu menu){
		try{
			int pid = menuDao.findMenuIdByType(type);
			menu.setPid(pid);// 设置父级ID
			
			Menu m = menuDao.findByName(menu.getName());
			if(null == m){
				// 保存操作
				menuDao.save(menu); 
				// 将菜单添加给内置管理员组
				menuDao.saveMenuToAdminGroup(menu.getId()); 
			}else{
				return m;
			} 
		}catch(Exception e){
			e.printStackTrace();
		}
		return menu;
	}
	
	
	/**
	 * 删除菜单
	 * @param id
	 */
	public void remove(Serializable menuId){
		menuDao.deleteByIds(menuId+"");
		
		menuDao.deleteGroupMenu(menuId);
		
		
	}
	
	
	
}
