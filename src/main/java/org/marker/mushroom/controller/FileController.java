package org.marker.mushroom.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.marker.mushroom.beans.FileObject;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.holder.WebRealPathHolder;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.FileTools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



/**
 * 文件管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/file")
public class FileController extends SupportController {

	
	public FileController() {
		this.viewPath = "/admin/file/";
	}
	
	
	
	//新建文件
	@RequestMapping("/newfolder")
	public ModelAndView newfolder(@RequestParam("path") String path){
		ModelAndView view = new ModelAndView(this.viewPath + "newfolder");
		view.addObject("path", encoding(path));
		return view;
	}
	
	@ResponseBody
	@RequestMapping("/savefolder")
	public Object savefolder(@RequestParam("path") String path, @RequestParam("name") String name){
		File file = new File(WebRealPathHolder.REAL_PATH + encoding(path + File.separator + name));
		if(file.mkdirs()){
			return new ResultMessage(true, "新建文件夹成功!");
		}else{
			return new ResultMessage(false, "新建文件夹失败!");
		}
	}
	
	//新建文件夹
	@RequestMapping("/newfile")
	public ModelAndView newfile(@RequestParam("path") String path){
		ModelAndView view = new ModelAndView(this.viewPath + "edit");
		view.addObject("path", encoding(path));
		return view;
	}
	
	//上传文件
	@RequestMapping("/upload")
	public ModelAndView upload(@RequestParam("path") String path){
		ModelAndView view = new ModelAndView(this.viewPath + "upload");
		view.addObject("path", encoding(path));
		return view;
	}
	
	@ResponseBody
	@RequestMapping("/savefile")
	public Object savefile(@RequestParam("path") String path, @RequestParam("file") MultipartFile  file){
	  
		String filePath = WebRealPathHolder.REAL_PATH+File.separator+path+File.separator+file.getOriginalFilename();
		File new_file = new File(filePath);
		 
		if(new_file.exists()){
			return new ResultMessage(false, "文件已存在!"); 
		}
		
		String fname = FileTools.getSuffix(filePath);
		
		// 过滤
		if(fname.toLowerCase().equals("jsp")){
			return new ResultMessage(false, "禁止上传jsp文件!");
		}
		
		try {
			if (!file.isEmpty()) {  
	            byte[] bytes = file.getBytes();  
			
	            FileOutputStream fos = new FileOutputStream(new_file);
				fos.write(bytes);
				 
				fos.flush();
				fos.close();
				return new ResultMessage(true, "上传成功!");
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return new ResultMessage(false, "上传失败!");
	}
	
	
	/**
	 * 编辑文本文件
	 * @param path
	 * @param name
	 * @return
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam("path") String path, @RequestParam("name") String name){
		ModelAndView view = new ModelAndView(this.viewPath + "edit");
		File file = new File(WebRealPathHolder.REAL_PATH + encoding(path + File.separator + name));
		try {
			view.addObject("data", FileTools.getFileContet(file, FileTools.FILE_CHARACTER_UTF8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		view.addObject("path", encoding(path));
		view.addObject("name", encoding(name));
		return view;
	}
	
	@RequestMapping("/rename")
	public ModelAndView rename(@RequestParam("path") String path, @RequestParam("name") String name){
		ModelAndView view = new ModelAndView(this.viewPath + "rename");
		view.addObject("path", encoding(path));
		view.addObject("name", encoding(name));
		return view;
	}
	
	
	@ResponseBody
	@RequestMapping("/savename")
	public Object savename(@RequestParam("path") String path, @RequestParam("name") String name, @RequestParam("rename")String rename){
		File oldFile = new File(WebRealPathHolder.REAL_PATH + path + File.separator + name);
		
		String fname = FileTools.getSuffix(name);
		
		// 过滤
		if(fname.toLowerCase().equals("jsp")){
			return new ResultMessage(false, "禁止jsp文件!");
		}
		
		
		if (oldFile.exists()) {
			String rootPath = oldFile.getParent();
			File newFile = new File(rootPath + File.separator + rename);
			if (!oldFile.renameTo(newFile)) {
				return new ResultMessage(true, "重命名失败!");
			}
		}
		return new ResultMessage(true, "重命名成功!");
	}
	
	
	/**
	 * 保存（其实完全可以绑定按键保存而不必提交，在编辑前，保留一个副本，作为还原文件）
	 * post请求了
	 * */
	@ResponseBody
	@RequestMapping("/save")
	public Object save(@RequestParam("path") String path, @RequestParam("name") String name, @RequestParam("data") String data){
		File file = new File(WebRealPathHolder.REAL_PATH + path + File.separator + name);
		
		String fname = FileTools.getSuffix(name);
		
		// 过滤
		if(fname.toLowerCase().equals("jsp")){
			return new ResultMessage(false, "禁止jsp文件!");
		}
		
		try {
			FileTools.setFileContet(file, data, FileTools.FILE_CHARACTER_UTF8);
		} catch (IOException e) {
			return new ResultMessage(false, "保存失败!");
		}
		return new ResultMessage(true, "保存成功!");
	}
	
	//单个文件删除
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@RequestParam("path") String path, @RequestParam("name") String name){
		File file = new File(WebRealPathHolder.REAL_PATH + encoding(path + File.separator + name));
		if(file.delete()){
			return new ResultMessage(true, "删除成功!");
		}else{
			return new ResultMessage(false, "删除失败!");
		}
	}
	
	
	
	
	//显示列表
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam("path") String path){
		ModelAndView view = new ModelAndView(this.viewPath+"list");
		String root = WebRealPathHolder.REAL_PATH;
		List<FileObject> list = new ArrayList<FileObject>(); 
		File file = new File(root + encoding(path));
		if(file.isDirectory()){//如果是文件夹
			File[] files = file.listFiles();
			for(File tmp : files){
				list.add(new FileObject(tmp));
			}
		} 
		view.addObject("data", list);
		view.addObject("pathURL", encoding(path));
		return view;
	}
	
	
	/**
	 * 编码转换(ISO-8859-1 -> utf-8)
	 * */
	private String encoding(String str){
		try {
			return new String(str.getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
		}
		return str;
	}
}
