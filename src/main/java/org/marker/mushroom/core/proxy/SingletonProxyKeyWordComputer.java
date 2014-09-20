package org.marker.mushroom.core.proxy;

import java.io.File;

import love.cq.domain.Forest;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.library.UserDefineLibrary;


/**
 * 关键字提取代理
 * @author marker
 * @version 1.0
 */
public class SingletonProxyKeyWordComputer {
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static KeyWordComputer instance = new KeyWordComputer(5);// 5个关键字
	}
	
	
	
	/**
	 * 获取KeyWordComputer实例
	 * @return KeyWordComputer
	 */
	public static KeyWordComputer getInstance(){
		return SingletonHolder.instance;
	}
	
	
	/**
	 * 加载字典
	 * */
	public static void init(String webRootPath){
		String dic = webRootPath+"data"+File.separator+"dic"+File.separator+"keywords.dic";
		Forest forest = new Forest(); 
        UserDefineLibrary.loadFile(forest, new File(dic)); 
	}
	
}
