package org.marker.mushroom.core.component;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import org.marker.mushroom.alias.CacheO;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.service.impl.SiteService;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 碎片
 * */
@Service(SystemStatic.SYSTEM_CMS_SITE)
public class SiteContext {

	private static final Log log = LogFactory.getLog(SiteContext.class);
	
	@Resource
	private SiteService siteService;

	// 缓存管理器
	@Resource
	private EhCacheCacheManager cacheManager;


	public synchronized void init() {
		org.springframework.cache.Cache siteInfoCache = cacheManager.getCache(CacheO.SITE_INFO_CACHE);
		List<Map<String, Object>> list = siteService.list();
		for (Map<String, Object> o : list) {
			siteInfoCache.put(o.get("host"), new JSONObject(o));
		}
		log.info("syn cache init ");
	}

}


