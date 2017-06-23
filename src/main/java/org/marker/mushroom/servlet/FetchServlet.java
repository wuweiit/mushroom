/**
 *  
 *  吴伟 版权所有
 */
package org.marker.mushroom.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.dao.ISupportDao;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 统计访问数据接口
 * 
 * @author marker
 * @date 2013-12-2 下午8:02:53
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class FetchServlet  extends HttpServlet{
	private  Logger logger = LoggerFactory.getLogger(FetchServlet.class);

	private static final long serialVersionUID = 2990324920926049103L;
	
	
	private static List<String> list = Collections.synchronizedList(new ArrayList<String>(30));
	

	private DataBaseConfig dbconfig = DataBaseConfig.getInstance();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
//		String baseURL = req.getAttribute(AppStatic.WEB_APP_URL).toString();
		
		String leave = req.getParameter("leave");// 是否离开
		
		if(!"1".equals(leave)){
			String url = req.getParameter("v0");// 当前url
			String referer = req.getParameter("v1");// 旧URL
			String system = req.getParameter("v2");// 操作系统
			String screen = req.getParameter("v3");// 屏幕分辨率
			String browser = req.getParameter("v4");// 浏览器
			String bVersion = req.getParameter("v5");// 浏览器
			String language = req.getParameter("v6");// 浏览器语言
			String isFlash  = req.getParameter("v7");// 是否安装Flash
			String ipv4 = HttpUtils.getRemoteHost(req);// 用户真实IP，处理了ngnix的代理IP
			String visitorId = HttpUtils.getCookie(req, "FETCHSESSIONID");// UV统计使用
			
			
			StringBuilder data = new StringBuilder();
			data.append(ipv4).append(",");
			data.append(language).append(",");
			data.append(browser).append(",");
			data.append(screen).append(",");
			data.append(url).append(",");
			data.append(referer).append(",");
			data.append(bVersion).append(",");
			data.append(system).append(",");
			data.append(visitorId).append(",");
			data.append(isFlash).append(",");
			
			
			
			String prefix = dbconfig.getPrefix(); 
			ISupportDao dao = SpringContextHolder.getBean(DAO.COMMON);
			
			if(list.size() < 5-1){
				list.add(data.toString());
			}else{// 满了，就批量推入数据库
				list.add(data.toString());
				List<Object[]> params = new ArrayList<Object[]>();
				Iterator<String> it = list.iterator();
				while(it.hasNext()){
					String f = it.next(); 
					params.add(f.split(",")); 
				} 
				
				StringBuilder sql = new StringBuilder();
				sql.append("insert into ").append(prefix).append("visited_his");
				sql.append("(`ip`,`language`,`browser`,`screen`,`url`,`referer`, `version`,`system`,`time`,`visitor`,`flash`)")
				.append(" values(?,?,?,?,?,?,?,?,sysdate(),?,?)");
				
				dao.batchUpdate(sql.toString(), params); 
				list.clear();
			}
		}else{// 离开
			logger.debug("用户已经离开!");
			
		}
		
	} 
}
