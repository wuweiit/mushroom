package org.marker.mushroom.core.component;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.marker.mushroom.alias.CacheO;
import org.marker.mushroom.beans.Site;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.service.impl.SiteService;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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


	/**
	 * 初始化缓存
	 */
	public synchronized void refreshCache() {
		org.springframework.cache.Cache siteInfoCache = cacheManager.getCache(CacheO.SITE_INFO_CACHE);
		List<Site> list = siteService.getList();
		for (Site site : list) {
			siteInfoCache.put(site.getHost(), site);
		}
		log.info("syn cache init ");
	}


	/**
	 * 获取站点信息缓存
	 * @param host 域名
	 * @return
	 */
	public Site getByHost(String host) {
		org.springframework.cache.Cache siteInfoCache = cacheManager.getCache(CacheO.SITE_INFO_CACHE);
		Site site = siteInfoCache.get(host, Site.class);
		if (site == null) {
			refreshCache();
			site = siteInfoCache.get(host, Site.class);
		}
		return site;
	}
}


