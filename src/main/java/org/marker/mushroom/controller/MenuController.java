package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Menu;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.dao.IMenuDao;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


/**
 * 菜单管理
 * @author Administrator
 */
@Controller
@RequestMapping("/admin/menu")
public class MenuController extends SupportController {

	@Autowired private IMenuDao menuDao;



	public MenuController() {
		this.viewPath = "/admin/menu/";
	}
	
	/** 添加用户 */
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView(this.viewPath+"add");
		view.addObject("menus", commonDao.queryForList("select * from  "+getPrefix()+"user_menu"));
		return view;
	}

	/** 编辑用户 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") long id){
		ModelAndView view = new ModelAndView(this.viewPath+"edit");
		view.addObject("data", commonDao.findById(Menu.class, id)); 
		view.addObject("menus", commonDao.queryForList("select * from  "+getPrefix()+"user_menu"));
		return view;
	}
	
	/** 保存用户 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Menu menu,@RequestParam("id") int id){
		menu.setId(id);// 不能注入

		int parentId = menu.getPid();

		Menu maxMenu = menuDao.findChildMaxSortMenuByPId(parentId);
		if(maxMenu != null && (maxMenu.getSort() <= menu.getSort())){
			menu.setEnd(1);
		}

		if(commonDao.update(menu)){
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(false,"更新失败!"); 
		}
	}
	
	/** 保存用户 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(Menu menu){ 
		if(commonDao.save(menu)){
			return new ResultMessage(true, "添加成功!");
		}else{
			return new ResultMessage(false,"保存失败!"); 
		}
	}
	//删除文章
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		int id;
		try {
			id = Integer.parseInt(rid);
		} catch (Exception e) {
			return new ResultMessage(true,"删除失败!<br />说明：ID参数不是数字。");
		}
		if(!menuDao.hasChildMenu(id)){ // 如果没有子节点
			boolean status = commonDao.deleteByIds(Menu.class, rid);
			if(status){
				return new ResultMessage(true,"删除成功!");
			}else{
				return new ResultMessage(false,"删除失败!"); 
			}
		}else{
			return new ResultMessage(false,"删除失败!<br />说明：该菜单有子菜单哦。"); 
		}
	}
	

	
	
	/**
	 * 用户组列表
	 *
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list( ) {
		ModelAndView view = new ModelAndView(this.viewPath + "list");
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPrefix()).append("user_menu order by sort");
		List<Map<String,Object>> list = commonDao.queryForList(sql.toString());






		view.addObject("menus", list );
		return view;
	}
	
}
