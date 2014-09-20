package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Page;
import org.marker.mushroom.dao.IStatisticsDao;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 统计控制器
 * @author marker
 * */
@Controller
@RequestMapping("/admin/statistics")
public class StatisticsController extends SupportController{

	@Autowired
	private IStatisticsDao statisticsDao;
	
	public StatisticsController() {
		this.viewPath = "/admin/statistics/";
	}
	
	
	@RequestMapping("/today")
	public ModelAndView today(){
		ModelAndView view = new ModelAndView(this.viewPath+"today");
		return view;
	}
	
	@RequestMapping("/yesterday")
	public ModelAndView yesterday(){
		ModelAndView view = new ModelAndView(this.viewPath+"yesterday");
		return view;
	}
	
	@RequestMapping("/month")
	public ModelAndView month(){
		ModelAndView view = new ModelAndView(this.viewPath+"month");
		return view;
	}
	
	
	@RequestMapping("/todayRest")
	@ResponseBody
	public Object todayRest(){
		return statisticsDao.today();
	}
	
	@RequestMapping("/yesterdayRest")
	@ResponseBody
	public Object yesterdayRest(){
		return statisticsDao.yesterday();
	}
	
	@RequestMapping("/plugin_list")
	public ModelAndView plugin_list(Page page){
		ModelAndView view = new ModelAndView(this.viewPath+"pluginlist");
		return view;
	}
}