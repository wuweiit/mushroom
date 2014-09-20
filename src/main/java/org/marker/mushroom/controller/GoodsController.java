package org.marker.mushroom.controller;

import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * 后台管理主界面控制器
 * @author marker
 * 
 * */
@Controller
@RequestMapping("/admin/goods")
public class GoodsController extends SupportController {
	
	/** 构造方法初始化一些成员变量 */
	public GoodsController() {
		this.viewPath = "/admin/goods/";
	}
	
	
	

	
	/**
	 * 系统信息
	 * */
	@RequestMapping("/list")
	public ModelAndView list(){
		ModelAndView view = new ModelAndView(this.viewPath + "list");
	 
		return view;
	}
	
}
