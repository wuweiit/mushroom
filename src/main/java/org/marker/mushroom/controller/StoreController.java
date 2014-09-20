package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Page;
import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * 在线商店控制器
 * @author marker
 * */
@Controller
@RequestMapping("/admin/store")
public class StoreController extends SupportController{

	public StoreController() {
		this.viewPath = "/admin/store/";
	}
	
	@RequestMapping("/template_list")
	public ModelAndView template_list(Page page){
		ModelAndView view = new ModelAndView(this.viewPath+"templatelist");
		return view;
	}
	
	@RequestMapping("/plugin_list")
	public ModelAndView plugin_list(Page page){
		ModelAndView view = new ModelAndView(this.viewPath+"pluginlist");
		return view;
	}
}
