/**
 *  
 *  吴伟 版权所有
 */
package org.marker.qqwryip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP 地址转换物理地址工具
 *
 * @author marker
 * @date 2013-11-16 下午4:39:20
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class IPTool {

    private Logger logger = LoggerFactory.getLogger(IPTool.class);


    // 纯真数据库地址
	public static final String DAT_FILE_PATH = "qqwry.dat";
	
	// 缓存
	private static final Map<String, IPLocation> cache = new Hashtable<String, IPLocation>();
    /**
     * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
     * */
    private static IPTool instance;
	
	
	// IP地址正则模式
	private static final Pattern ipPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
	
	// IP查询器
	private static IPSeeker seeker ;
	
	
	// 单例模式
	private IPTool(){
		try {
            URL url = this.getClass().getClassLoader().getResource(DAT_FILE_PATH);
 			String datfile = url.getFile();
			String file = URLDecoder.decode(datfile, "utf-8");// 转码避免中文无法识别

			seeker = new IPSeeker(file);
		} catch (IOException e) {
            logger.error("初始化纯真IP数据库失败，请检查qqwry.dat", e);
		}
	}





	
	/**
	 * 获取数据库配置实例
	 * */
	public static IPTool getInstance(){
        if(instance == null){
            instance = new IPTool();
        }
		return  instance;
	}
	
	
	
	/**
	 * 获取指定IP地址的归属地
	 * @param ip IP地址
	 * @return 归属地
	 */
	public IPLocation getLocation(String ip){
		Matcher m = ipPattern.matcher(ip);
		if(m.find()){
			byte ip1 = (byte)Integer.parseInt(m.group(1));
			byte ip2 = (byte)Integer.parseInt(m.group(2));
			byte ip3 = (byte)Integer.parseInt(m.group(3));
			byte ip4 = (byte)Integer.parseInt(m.group(4)); 
			return seeker.getLocation(ip1, ip2, ip3, ip4);
		}else{
			return null;
		}
	}
	
	
	public IPLocation getLocationCache(String ip) {  
		if (cache.containsKey(ip)) {
			return cache.get(ip);
		} else {
			return cache.put(ip, this.getLocation(ip));
		}
	}
	
	
}
