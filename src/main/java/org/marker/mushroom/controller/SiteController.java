package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Category;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.dao.IModelDao;
import org.marker.mushroom.service.impl.SiteService;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.validator.CategoryValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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
		view.addObject("data", siteService.list());
		return view;
	}
	
	/**
	 * 编辑分类
	 * */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") int id){
		ModelAndView view = new ModelAndView(this.viewPath + "edit");
		view.addObject("category", commonDao.findById(Category.class, id));
		view.addObject("data", siteService.list());
		return view;
	}
	
	/**
	 * 保存分类
	 * */
	@RequestMapping("/save")
	@ResponseBody
	public Object save(Category category, Errors errors){
        String nameStr = category.getName();

        new CategoryValidator().validate(category, errors);


        String[] names = nameStr.split(",");
        for(String name : names){
            Category entity = new Category();
            entity.setName(name);
            entity.setSort(category.getSort());
            entity.setDescription(category.getDescription());
            entity.setPid(category.getPid());
            commonDao.save(entity);
        }
	    return new ResultMessage(true, "批量添加成功!");
	}
	
	// 保存或者更新
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Category category){
        Integer id = category.getId();
	    if(id != null && id > 0){
            if(commonDao.update(category)){
                return new ResultMessage(true, "更新成功!");
            }
        }else{
            if(commonDao.save(category)){
                return new ResultMessage(true, "添加成功!");
            }
        }

        return new ResultMessage(false,"更新失败!");

    }
	
	// 删除
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("id") Integer id){
		 
		boolean isyou = siteService.hasChild(id);
		if(isyou){
			return new ResultMessage(false,"该节点下有子节点，不能删除!");
		}
		
		boolean status = commonDao.deleteByIds(Category.class, id+"");
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
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
