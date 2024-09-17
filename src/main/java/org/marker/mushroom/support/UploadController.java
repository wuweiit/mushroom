package org.marker.mushroom.support;

import org.marker.mushroom.core.config.impl.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *
 * 系统配置管理控制器
 *
 * @author marker
 *
 * @date 2016-10-22 14:39:20
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 *
 * */
@Controller
@RequestMapping("/admin")
public class UploadController extends SupportController {

	/** 日志记录对象 */
	protected Logger logger =  LoggerFactory.getLogger(UploadController.class);

	/** 系统配置对象 */
	@Resource
	private SystemConfig config;




	// 网站基本信息
	@RequestMapping("/upload")
	public void upload(DefaultMultipartHttpServletRequest multipartHttpServletRequest, HttpServletResponse response) throws IOException {


	}





}
