package org.marker.mushroom.controller;

import java.util.List;

import org.marker.mushroom.beans.Permission;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.beans.UserGroup;
import org.marker.mushroom.dao.IPermissionDao;
import org.marker.mushroom.dao.IUserDao;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**
 * 用户组管理
 * 
 * @author marker
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/group")
public class UserGroupController extends SupportController {
	
	// 权限
	@Autowired private IPermissionDao permissionDao;
	
	@Autowired private IUserDao userDao;
	
	
	public UserGroupController() {
		this.viewPath = "/admin/group/";
	}

	
	
	/** 添加用户 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView view = new ModelAndView(this.viewPath + "add");
		return view;
	}
	
	
	
	/** 保存组 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(UserGroup group){
		if(commonDao.save(group)){
			return new ResultMessage(true, "添加成功!");
		}else{
			return new ResultMessage(false,"保存失败!"); 
		}
	}
	
	/** 删除组 */
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("id") Integer id){
		if(id == 1){// 如果找到管理员组
			return new ResultMessage(false,"管理员组为内置，不能删除!"); 
		}
		
		// 检查用户是否有使用此组，如果有，不能删除。
		int count = userDao.countUserByGroupId(id);
		if(count > 0){
			return new ResultMessage(false,"用户组有用户存在，不能删除!"); 
		}
		
		dao.update("delete from mr_user_group_menu where gid =" + id);
		
		
		boolean status = commonDao.deleteByIds(UserGroup.class, id + "");
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
	
	

	/** 保存用户 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(UserGroup group,@RequestParam("id") int id){
		group.setId(id);// 不能注入 
		if(commonDao.update(group)){
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(false,"更新失败!"); 
		}
	}
	
	
	/** 编辑组 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") long id){
		ModelAndView view = new ModelAndView(this.viewPath+"edit");
		view.addObject("group", commonDao.findById(UserGroup.class, id));
		return view;
	}

	/**
	 * 用户组列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public ModelAndView list( ) {
		ModelAndView view = new ModelAndView(this.viewPath + "list");
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPrefix()).append("user_group");
		view.addObject("data", commonDao.queryForList(sql.toString()));
		return view;
	}
	
	
	/**
	 * 赋权操作
	 */
	@RequestMapping("/grant")
	public ModelAndView grant(@RequestParam("id") int groupId){
		ModelAndView view = new ModelAndView(this.viewPath + "grant");
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ").append(getPrefix()).append("user_menu order by sort"); 
		view.addObject("menus", commonDao.queryForList(sql.toString()));
		view.addObject("gid", groupId);
		 // 这里要把原来的权限查询出来
		List<Permission> permissions = permissionDao.findPermissionByGroupId(groupId);
		view.addObject("permissions", permissions);
		return view;
	}
	
	/**
	 * 赋权
	 */
	@RequestMapping("/granting")
	@ResponseBody
	public Object granting(@RequestParam("mid") String mid, @RequestParam("gid") int gid){
		// 在更新前，先清空以前权限，再更新最新权限。
		dao.update("delete from mr_user_group_menu where gid="+gid);
		
		
		String a = "insert into "+dbConfig.getPrefix()+"user_group_menu(gid,mid) values(?,?)";
		String[] s = mid.split(",");
		for(String id : s){
			int mi = Integer.parseInt(id);

			dao.update(a, new Object[]{gid, mi});
		} 
		
	    return new ResultMessage(true, "更新权限成功!");
	}
}
