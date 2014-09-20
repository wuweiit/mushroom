package org.marker.mushroom.controller;

import org.marker.mushroom.beans.Chip;
import org.marker.mushroom.beans.Page;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.IChip;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.validator.ChipValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 碎片管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/chip")
public class ChipController extends SupportController {
	
	private IChip chipData = SpringContextHolder.getBean(SystemStatic.SYSTEM_CMS_CHIP);
	
	public ChipController() {
		this.viewPath = "/admin/chip/";
	}
	
	
	
	//添加碎片
	@RequestMapping("/add")
	public String add(){
		return this.viewPath + "add";
	}
	
	
	
	/** 编辑用户 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("id") long id){
		ModelAndView view = new ModelAndView(this.viewPath+"edit");
		view.addObject("chip", commonDao.findById(Chip.class, id));
		return view;
	}
	
	
	/** 保存 */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(@Validated(ChipValidator.class) Chip chip){
		if(commonDao.save(chip)){
			chipData.syn();//同步碎片集合内容
			return new ResultMessage(true, "添加成功!");
		}else{
			return new ResultMessage(true, "添加失败!");
		} 
	}
	
	
	
	/** 更新碎片 */
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Chip chip){
		if(commonDao.update(chip)){
			chipData.syn();//同步碎片集合内容
			return new ResultMessage(true, "更新成功!");
		}else{
			return new ResultMessage(true, "更新失败!");
		} 
	}
	
	
	//删除
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("rid") String rid){
		boolean status = commonDao.deleteByIds(Chip.class, rid);
		if(status){
			chipData.syn();//同步碎片集合内容
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
	
	
	
	//显示列表
	@RequestMapping("/list")
	public ModelAndView list(Page page){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		view.addObject("page", commonDao.findByPage(page.getCurrentPageNo(), 10, "select * from  "+getPrefix()+"chip order by id desc"));
		return view;
	}
	
 
}
