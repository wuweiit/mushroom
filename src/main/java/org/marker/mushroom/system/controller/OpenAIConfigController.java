package org.marker.mushroom.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.marker.mushroom.beans.ResultMessage;
import org.marker.mushroom.core.config.impl.OpenAIConfig;
import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * openai 配置
 *
 * @author marker
 *
 * @date 2025-01-22 14:39:20
 * @version 1.0
 * @blog www.yl-blog.com
 *
 * */
@Slf4j
@Controller
@RequestMapping("/admin/system/openai")
public class OpenAIConfigController extends SupportController {

	/** 配置对象 */
	@Resource
	private OpenAIConfig config;


	@GetMapping("/info")
	public void getStorageInfo(HttpServletRequest request){
		request.setAttribute("config", config.getProperties());
	}
	
	
	//保存网站配置信息
	@ResponseBody
	@PostMapping("/info")
	public Object saveStorageInfo(OpenAIConfig config, HttpServletRequest request){
		try {
			/* 系统存储配置 */
			config.storeAsync();
			return new ResultMessage(true, "更新成功!");
		} catch (Exception e) {
			log.error("存储配置更新失败", e);
			return new ResultMessage(false, "更新失败!");
		} 
	}
	
	

}
