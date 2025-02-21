package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.Position;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.dao.LinkDao;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;


/**
 * 荣誉管理
 * 
 * @author marker
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/dangjian")
public class DangjianController extends SupportController {

	// 权限
	@Autowired private LinkDao linkDao;


	public DangjianController() {
		this.viewPath = "/admin/dangjian/";
	}


	@Resource
	private SystemConfig systemConfig;


	/** 保存 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(@RequestParam("json") String json){


		systemConfig.set("dangjian", json);
		systemConfig.store();
        return new ResultMessage(true, "更新成功!");

	}




	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public ModelAndView list() {
        ModelAndView view = new ModelAndView(this.viewPath + "list");

        String json = systemConfig.getDangjian();
        view.addObject("json", json);
		return view;
	}
	


}
