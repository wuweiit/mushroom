package org.marker.mushroom.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.marker.app.common.SessionAttr;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.beans.User;
import org.marker.mushroom.beans.UserLoginLog;
import org.marker.mushroom.core.AppStatic;
import org.marker.mushroom.core.WebAPP;
import org.marker.mushroom.core.config.impl.SystemConfig;
import org.marker.mushroom.dao.IMenuDao;
import org.marker.mushroom.dao.IUserDao;
import org.marker.mushroom.dao.IUserLoginLogDao;
import org.marker.mushroom.support.SupportController;
import org.marker.mushroom.utils.GeneratePass;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.qqwryip.IPLocation;
import org.marker.qqwryip.IPTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



/**
 * 后台管理主界面控制器
 * @author marker
 * 
 * */
@Controller
@RequestMapping("/admin")
public class AdminController extends SupportController {

	/** 日志记录器 */ 
	private Logger logger =  LoggerFactory.getLogger(AdminController.class);
	
	@Autowired IUserDao userDao;
	@Autowired IUserLoginLogDao userLoginLogDao;
	@Autowired IMenuDao menuDao;
	@Autowired ServletContext application;


	
	/** 构造方法初始化一些成员变量 */
	public AdminController() {
		this.viewPath = "/admin/";
	} 
	
	 
	
	/** 后台主界面 */ 
	@RequestMapping("/index")
	public String index(HttpServletRequest request){ 
		// 如果没有安装系统
		if(!WebAPP.install)
			return "redirect:../install/index.jsp";
		
		request.setAttribute("url", HttpUtils.getRequestURL(request)); 
		HttpSession session = request.getSession(false);
		if(session != null){
			try{
				int groupId = (Integer) session.getAttribute(AppStatic.WEB_APP_SESSSION_USER_GROUP_ID);
				request.setAttribute("topmenus", menuDao.findTopMenuByGroupId(groupId)); 
			}catch (Exception e) {
				log.error("因为没有登录，在主页就不能查询到分组ID");
				return "redirect:login.do";
			}
		}
		
		return this.viewPath+"index";
	}
	
	
	/**
	 * 子菜单接口
	 * @param id
	 * @return
	 */
	@RequestMapping("/childmenus")
	@ResponseBody
	public Object menu(HttpServletRequest request, @RequestParam("id") int id){
		ModelAndView view = new ModelAndView(this.viewPath+"childmenus");
		view.addObject("menu",menuDao.findMenuById(id));
		HttpSession session = request.getSession();
		if(session != null){
			try{
				int groupId = (Integer) session.getAttribute(AppStatic.WEB_APP_SESSSION_USER_GROUP_ID);
				view.addObject("childmenus",menuDao.findChildMenuByGroupAndParentId(groupId, id));
			}catch (Exception e) {
				log.error("因为没有登录，在主页就不能查询到分组ID");
				return "<script>window.location.href='login.do?status=timeout';</script>";
			} 
		} 
		return view;
	}
	
	/**
	 * 登录操作
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(
			@RequestParam(value = "safe", defaultValue = "") String safe,
			HttpServletRequest request,
            HttpServletResponse response) throws IOException {
		// 如果没有安装系统
		if(!WebAPP.install)
			return "redirect:../install/index.jsp";



        request.setAttribute("url", HttpUtils.getRequestURL(request));


		SystemConfig syscfg = SystemConfig.getInstance();
        String systemLoginSafe = syscfg.getLoginSafe();

		if(!systemLoginSafe.equals(safe)){// 验证登录路径
            return this.viewPath + "404";
		}

		
		HttpSession session = request.getSession(false);
		if(session != null){
			try{
				User user = (User) session.getAttribute(AppStatic.WEB_APP_SESSION_ADMIN);
				if(null != user){
					return "redirect:index.do";
				}
			}catch (Exception e) {}
		} 
		return this.viewPath + "login";
	}
	
	
	
	/**
	 * 登录系统
	 * 验证码不区分大小写
	 * @return json
	 * */
	@ResponseBody
	@RequestMapping(value="/loginSystem", method=RequestMethod.POST)
	public Object loginSystem(HttpServletRequest request){
		String randcode = request.getParameter("randcode").toLowerCase();//验证码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String device   = request.getParameter("device");// 设备
		HttpSession session = request.getSession();// 如果会话不存在也就创建
		Object authCode = session.getAttribute(AppStatic.WEB_APP_AUTH_CODE);
		
		int errorCode = 0;// 登录日志类型
		String scode = "";
		if(authCode != null){
			scode =((String)authCode).toLowerCase();
		}
		
		ResultMessage msg = null;
		if(scode != null && 	!scode.equals(randcode)){// 验证码不匹配
			msg = new ResultMessage(false,"验证码错误!");
			errorCode = 1;// 错误
		}else{
			String password2 = null;
			try {
				password2 = GeneratePass.encode(password);
				User user = userDao.queryByNameAndPass(username, password2);
				if(user != null){
					if(user.getStatus() == 1){//启用
						userDao.updateLoginTime(user.getId());// 更新登录时间
						session.setAttribute(AppStatic.WEB_APP_SESSION_ADMIN, user); 
						session.setAttribute(AppStatic.WEB_APP_SESSSION_LOGINNAME, user.getNickname());
						session.setAttribute(AppStatic.WEB_APP_SESSSION_USER_GROUP_ID, user.getGid());// 设置分组
						session.setAttribute(SessionAttr.USER_GROUP_ID, user.getGid());// 用户组
						session.removeAttribute(AppStatic.WEB_APP_AUTH_CODE); //移除验证码
						msg = new ResultMessage(true,"登录成功!");
					}else{
						errorCode = 1;
						msg = new ResultMessage(false,"用户已禁止登录!");
					}
				}else{
					errorCode = 1;
					msg = new ResultMessage(false,"用户名或者密码错误!");
				}
			} catch (Exception e) {
				errorCode = 1;
				msg = new ResultMessage(false,"系统加密算法异常!");
				log.error("系统加密算法异常!", e);
			}
			
		}
		// 获取真实IP地址
		String ip = HttpUtils.getRemoteHost(request);
		
		// IP归属地获取工具
		IPTool ipTool = IPTool.getInstance();
		
		
		
		// 记录日志信息
		UserLoginLog log = new UserLoginLog();
		log.setUsername(username);
		log.setTime(new Date());
		
		log.setDevice(device);
		log.setInfo(msg.getMessage());
		log.setIp(ip);
		log.setErrorcode(errorCode);
		if(ip != null){
			try{
				IPLocation location = ipTool.getLocation(ip);
				if(location != null){// 如果存在
					log.setArea(location.getCountry());
				}
			}catch(Exception e){
				logger.error("ip={} ",ip, e);
			}
		}
		 
		userLoginLogDao.save(log); 
		
		return msg;
	}
	
	/**
	 * 注销
	 * */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(false);
		if(session != null) session.invalidate();

		SystemConfig syscfg = SystemConfig.getInstance();
        String systemLoginSafe = syscfg.getLoginSafe();


        return "redirect:login.do?safe="+systemLoginSafe;
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
		
		view.addObject("serverinfo", application.getServerInfo());
		view.addObject("dauthor", "marker");
		view.addObject("email", "admin@wuweibi.com");
		view.addObject("version", "3.0");
		view.addObject("qqqun","331925386");
		view.addObject("uxqqqun","181150189");
		
		return view;
	}
	
}
