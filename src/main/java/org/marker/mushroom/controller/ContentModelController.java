package org.marker.mushroom.controller;

import java.io.File;

import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.dao.IModelDao;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.ext.model.ContentModelContext;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.FileTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 内容模型管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/model")
public class ContentModelController extends SupportController {

	@Autowired private ISupportDao commonDao;

	@Autowired private IModelDao modelDao;
	
	public ContentModelController() {
		this.viewPath = "/admin/model/";
	}
	
	//添加模型
	@RequestMapping("/add")
	public String add(){
		return this.viewPath + "add";
	}
	
	
	
//	/** 编辑用户 */
//	@RequestMapping("/edit")
//	public ModelAndView edit(@RequestParam("id") long id){
//		ModelAndView view = new ModelAndView(this.viewPath+"edit");
//		view.addObject("chip", commonDao.findById(Chip.class, id));
//		return view;
//	}
//	
//	
	 
//	
//	
//	/** 保存 */
//	@ResponseBody
//	@RequestMapping("/save")
//	public Object save(@RequestParam("module") MultipartFile file){
//		
//		 
//		String filePath = WebRealPathHolder.REAL_PATH+File.separator+"upload"+File.separator+"modules"+File.separator+file.getOriginalFilename();
//		File new_file = new File(filePath);
//		 
//		if(new_file.exists()){
//			return new ResultMessage(true, "文件已存在!"); 
//		}
//		
//		try {
//			if (!file.isEmpty()) {  
//	            byte[] bytes = file.getBytes();
//	            FileOutputStream fos = new FileOutputStream(new_file);
//				fos.write(bytes);
//				fos.flush();
//				fos.close();
//			} 
//		} catch (Exception e) { 
//			return new ResultMessage(true, "上次模型失败!");
//		} 
//		int stauts = moduleManagement.install(filePath);
//		switch (stauts) {
//			case IModuleManagement.INSTALL_SUCCESS:
//				return new ResultMessage(true, "安装模型成功!");
//			case IModuleManagement.INSTALL_EXIST:
//				return new ResultMessage(true, "模型已经存在!");
//			default:
//				return new ResultMessage(true, "安装模型失败!"); 
//		}  
//	}
//	
//	
//	
//	/** 更新模型 */
//	@ResponseBody
//	@RequestMapping("/update")
//	public Object update(Module module){
//		if(commonDao.update(module)){
//			return new ResultMessage(true, "更新模型成功");
//		}else{
//			return new ResultMessage(false,"更新模型失败!"); 
//		}
//	}
//	
//	
//	//删除
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("type") String modelType){
		
		// 验证是否被引用
		
		
		
		boolean status = modelDao.deleteByType(modelType);
		
		ContentModelContext cmc = ContentModelContext.getInstance();
		
		cmc.remove(modelType);
		
		File file = new File(WebRealPathHolder.REAL_PATH+"models"+File.separator+modelType);
		
		FileTools.deleteDirectory(file);// 删除文件
		if(status){
			return new ResultMessage(true,"删除成功!");
		}else{
			return new ResultMessage(false,"删除失败!"); 
		}
	}
//	
//	
//	
	
	//显示列表
	@RequestMapping("/list")
	public ModelAndView list(){ 
		ModelAndView view = new ModelAndView(this.viewPath + "list"); 
		view.addObject("data",modelDao.queryAll());
		return view;
	}
	
 
}
