package org.marker.mushroom.controller;

import javax.servlet.http.HttpServletRequest;

import org.marker.mushroom.ext.tag.TaglibContext;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 标签管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/taglib")
public class TaglibController extends SupportController {
	
	
	public TaglibController() {
		this.viewPath = "/admin/taglib/";
	}
	 
	
	//显示列表
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		view.addObject("taglibs", TaglibContext.getInstance().getList());
		view.addObject("url", HttpUtils.getRequestURL(request)); 
		return view;
	}
	
 
}
