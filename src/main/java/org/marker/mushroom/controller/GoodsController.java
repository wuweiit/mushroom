package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Goods;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.dao.ICommonDao;
import org.marker.mushroom.service.impl.CategoryService;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**
 * 后台管理主界面控制器
 * @author marker
 * 
 * */
@Controller
@RequestMapping("/admin/goods")
public class GoodsController extends SupportController {
	
	@Autowired ICommonDao commonDao;

	@Autowired CategoryService categoryService;
	
	
	/** 构造方法初始化一些成员变量 */
	public GoodsController() {
		this.viewPath = "/admin/goods/";
	}
	

	// 发布
	@RequestMapping("/add")
	public ModelAndView add(){
		ModelAndView view = new ModelAndView(this.viewPath+"add"); 
		view.addObject("categorys", categoryService.list("goods"));
		return view;
	}
	/**
	 * 持久化文章操作
	 * @param article
	 * @param pid
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(Goods goods, @RequestParam("cid") int cid){
//		goods.setTime(new Date());
//		goods.setCid(cid);// 这里是因为不能注入bean里
		
		
		
		if(commonDao.save(goods)){ 
			return new ResultMessage(true,     "成功!"); 
		}else{
			return new ResultMessage(false,   "失败!"); 
		}
	}
	
	
	//删除文章
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		boolean status = commonDao.deleteByIds(Goods.class, rid);
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
	
	
	// 
	@RequestMapping("/list")
	public ModelAndView listview(){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		view.addObject("categorys", categoryService.list("goods"));
		return view;
	}

	
	/**
	 * 系统信息
	 * */
	@RequestMapping(value="",method=RequestMethod.GET)
	public ModelAndView list(
			@RequestParam("currentPageNo") int currentPageNo,
			@RequestParam("cid") int cid,
			@RequestParam("status") String status,
			@RequestParam("keyword") String keyword,
			@RequestParam("pageSize") int pageSize){
		ModelAndView view = new ModelAndView(this.viewPath + "list");
	 
		Page page = commonDao.findByPage(currentPageNo, pageSize, "select * from "+getPrefix()
				+"goods ");
		
		view.addObject("page", page);
		return view;
	}
	
}
