package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.beans.Site;
import org.marker.mushroom.core.component.SiteContext;
import org.marker.mushroom.dao.IModelDao;
import org.marker.mushroom.service.impl.SiteService;
import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * 
 * 站点管理
 * 
 * @author marker
 * 
 * */
@Controller
@RequestMapping("/admin/site")
public class SiteController extends SupportController {

	/** 分类业务对象 */
	@Resource
	private SiteService siteService;

	@Resource
	private IModelDao modelDao;

	/** 构造方法初始化一些成员变量 */
	public SiteController() {
		this.viewPath = "/admin/site/";
	}




	
	/**
	 * 添加站点
	 * */
	@RequestMapping("/add")
	public ModelAndView add(HttpServletRequest req){
		ModelAndView view = new ModelAndView(this.viewPath + "add");

		return view;
	}
	
	/**
	 * 编辑分类
	 * */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id){
		ModelAndView view = new ModelAndView(this.viewPath + "edit");

		view.addObject("entity", siteService.get(id));
		return view;
	}
	
	/**
	 * 保存分类
	 * */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(Site site, Errors errors){
		commonDao.save(site);
		// 刷新站点缓存
		siteContext.init();
		return new ResultMessage(true, "批量添加成功!");
	}

	@Resource
	private SiteContext siteContext;


	// 保存或者更新
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Site site){
        Integer id = site.getId();
	    if (Objects.isNull(id)) {
			return new ResultMessage(false,"id不能空!");
		}
		if(commonDao.update(site)){
			// 刷新站点缓存
			siteContext.init();
			return new ResultMessage(true, "更新成功!");
		}
		return new ResultMessage(false,"更新失败!");
    }
	
	// 删除
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("id") Integer id){
		boolean status = commonDao.deleteByIds(Site.class, id + "");
		if (status) {
			return new ResultMessage(true, "删除成功!");
		} else {
			return new ResultMessage(false, "删除失败!");
		}
	}
		
	
	/**
	 * 分类管理
	 * */
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value = "currentPageNo",defaultValue = "1") int currentPageNo){
		ModelAndView view = new ModelAndView(this.viewPath + "list"); 
		view.addObject("data", siteService.list());


		StringBuilder sql = new StringBuilder();
		sql.append("select u.* from ").append(getPrefix()).append("site u  ");


		Page page = commonDao.findByPage(currentPageNo, 20, sql.toString());

		view.addObject("page", page);
		return view;
	}




	/**
	 * 获取分类信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/get")
	@ResponseBody
	public Object get(int id){
		return siteService.get(id);
	}
}
