package org.marker.mushroom.controller;

import org.marker.mushroom.beans.*;
import org.marker.mushroom.dao.LinkDao;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * 历程管理
 * 
 * @author marker
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/history")
public class HistoryController extends SupportController {

	// 权限
	@Autowired private LinkDao linkDao;


	public HistoryController() {
		this.viewPath = "/admin/history/";
	}

	
	
	/** 添加 */
	@RequestMapping("/add")
	public ModelAndView add() {
		ModelAndView view = new ModelAndView(this.viewPath + "add");
		return view;
	}
	
	
	
	/** 保存组 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(History entity){
		if(commonDao.save(entity)){
			return new ResultMessage(true, "添加成功!");
		}else{
			return new ResultMessage(false,"保存失败!"); 
		}
	}
	
	/** 删除组 */
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
        boolean status = linkDao.deleteByIds(History.class, rid);
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}


	

	/** 保存 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(History link,@RequestParam("id") int id){
        link.setId(id);// 不能注入

		if(commonDao.update(link)){
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(false,"更新失败!"); 
		}
	}


	
	/** 编辑组 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id){
		ModelAndView view = new ModelAndView(this.viewPath+"edit");
		view.addObject("entity", commonDao.findById(History.class, id));
		return view;
	}

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value = "currentPageNo", defaultValue = "1") int currentPageNo) {
        ModelAndView view = new ModelAndView(this.viewPath+"list");
        StringBuilder sql = new StringBuilder();
        sql.append("select u.* from ").append(getPrefix()).append("history u");

        Page page = commonDao.findByPage(currentPageNo, 10, sql.toString());
        view.addObject("page", page);
		return view;
	}
	


}
