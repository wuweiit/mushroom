/**
 *  
 *  吴伟 版权所有
 */
package org.marker.qqwryip;


import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
/**
 * @author marker
 * @date 2013-11-16 下午4:10:11
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */ 
public class IPSeekerSimpleCache extends IPSeeker {
	
	// 缓存
	private static final Map<String, IPLocation> cache = new Hashtable<String, IPLocation>();
	
	public IPSeekerSimpleCache(String path) throws IOException {
		super(path);
	}
	
	@Override
	public IPLocation getLocation(byte ip1, byte ip2, byte ip3, byte ip4) {
		final byte[] ip = { ip1, ip2, ip3, ip4 };
		String ipStr = new String(ip);
		if (cache.containsKey(ipStr)) {
			return cache.get(ipStr);
		} else {
			return cache.put(ipStr, this.getLocation(ip));
		}
	}
}
