package org.marker.mushroom.ext.menu;

import java.io.Serializable;
import java.util.Map;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Menu;
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

	/** 首页 */
	public static final String L1_HOME    = "b8db3088786a43e2916b810bbab8425b";
	/** 系统 */
	public static final String L1_SYSTEM  = "a1d6ac255acf4feab32ee70795b7b265";
	/** 栏目 */
	public static final String L1_CHANNEL = "91ea1c25538b4c4e90401289cbe981fb";
	/** 内容 */
	public static final String L1_CONTENT = "bb2bc5fb802544a2a4634013d2c19936";
	/** 用户 */
	public static final String L1_USER    = "c18166b148444928a4bab628da4190c8"; 
	/** 扩展 */
	public static final String L1_EXTEND  = "bf41a349eec94e039648cc6de833440a";
	/** 商店 */
	public static final String L1_STORE   = "ed3fc978ab86454ca88166a7e40a46a5";
	
	
	
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
	
	
	
	
	/**
	 * 
	 * @param data
	 */
	public int build(String PType, Map<String,String> data){
		Menu menu = new Menu();
		menu.setName(data.get("name"));
		menu.setUrl(data.get("url"));
		menu.setDescription(data.get("desc"));
		menu.setIcon(data.get("icon"));
		menu.setType(data.get("type"));

		if(menuDao.checkType(menu.getType())){// 检查是否存在
			// 更新菜单数据
			
			
			return 0;
		}else{
			int pid = menuDao.findMenuIdByType(PType);// 查询父级
			menu.setPid(pid);// 设置父级ID
			
			
			// 保存操作
			menuDao.save(menu); 
			
			// 将菜单添加给内置管理员组
			menuDao.saveMenuToAdminGroup(menu.getId());
			

			return menu.getId();
		} 
	}
	
	
	public Menu builder(String type , Menu menu){
		try{
			int pid = menuDao.findMenuIdByType(type);
			menu.setPid(pid);// 设置父级ID
			
			Menu m = menuDao.findByModuleId(menu.getModuleId());
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
		menuDao.deleteByIds(Menu.class, menuId + ""); 
		menuDao.deleteGroupMenu(menuId); 
	}
	
	
	
}
