package org.marker.mushroom.controller;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.marker.mushroom.dao.IUserDao;
import org.marker.mushroom.support.SupportController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * 后台管理主界面控制器
 * @author marker
 * 
 * */
@Controller
@RequestMapping("/admin/auth")
public class AuthController extends SupportController {

	private final Log log = LogFactory.getLog(AuthController.class);
	
	@Autowired IUserDao userDao;
	
	
	
	/** 构造方法初始化一些成员变量 */
	public AuthController() {
		this.viewPath = "/admin/auth/";
	}
	
	
	
 
 
	
	
	/**
	 * 系统信息
	 * */
	@RequestMapping("/systeminfo")
	public ModelAndView systeminfo(){
		ModelAndView view = new ModelAndView(this.viewPath + "systeminfo");
		String os = System.getProperty("os.name");//操作系统名称
		String osVer = System.getProperty("os.version"); //操作系统版本    
		String javaVer = System.getProperty("java.version"); //操作系统版本
		String javaVendor = System.getProperty("java.vendor"); //操作系统版本
		
		Runtime runTime = Runtime.getRuntime();
		
		long freeM = runTime.freeMemory() / 1024 / 1024;
        long maxM  = runTime.maxMemory() / 1024 / 1024;
        long tM    = runTime.totalMemory() / 1024 / 1024; 
        view.addObject("freememory", freeM);
        view.addObject("maxmemory", maxM);
        view.addObject("totalmemory", tM);
		view.addObject("os", os);
		view.addObject("osver", osVer);
		view.addObject("javaver", javaVer);
		view.addObject("javavendor", javaVendor);
		view.addObject("currenttime", new Date());
		view.addObject("serverinfo", "-");
		view.addObject("dauthor", "marker");
		view.addObject("email", "wuweiit@gmail.com");
		view.addObject("version", "20130928");
		view.addObject("qqqun","181150189");
		view.addObject("uxqqqun","181150189");
		
		return view;
	}
	
}
