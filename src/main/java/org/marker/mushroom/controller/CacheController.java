package org.marker.mushroom.controller;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.support.SupportController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 缓存管理
 * @author marker
 * */
@Controller
@RequestMapping("/admin/cache")
public class CacheController extends SupportController  {

	
	
	@RequestMapping("/list")
	public void list(){
		CacheManager CacheManager = SpringContextHolder.getBean("cacheManager");
		
		Cache channelCache = CacheManager.getCache("channelCache");
		
		 
		System.out.println("a"+channelCache);
	}
	
	
}
