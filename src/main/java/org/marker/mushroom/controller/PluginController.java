package org.marker.mushroom.controller;

import org.marker.mushroom.ext.plugin.PluginContext;
import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;




/**
 * 插件控制器
 * @author marker
 * */
@Controller
@RequestMapping("/admin/plugin")
public class PluginController extends SupportController {

	
	
	public PluginController() {
		this.viewPath = "/admin/plugin/";
	}
	
	
	 
	// 显示插件接口列表和嵌入式指令列表
	@RequestMapping("/list")
	public ModelAndView list(){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		PluginContext pluginContext = PluginContext.getInstance();
		view.addObject("data",  pluginContext.getList());
		return view;
	}
	
	 
}
