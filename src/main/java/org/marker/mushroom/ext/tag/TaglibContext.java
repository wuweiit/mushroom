package org.marker.mushroom.ext.tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.marker.mushroom.core.exception.SystemException;


public class TaglibContext {
	
	/** 模版标签库 */
	private static final List<Taglib> tags = new ArrayList<Taglib>();
	
  
	
	private TaglibContext() {
		
	}
	
	
	
	/**
	 * 获取数据库配置实例
	 * */
	public static TaglibContext getInstance(){ 
		return SingletonHolder.instance;
	}

	
	
	/**
	 * 这种写法最大的美在于，完全使用了Java虚拟机的机制进行同步保证。
	 * */
	private static class SingletonHolder {
		public final static TaglibContext instance = new TaglibContext();     
	}


	public void put(Taglib tag) {
		tags.add(tag);
	}
	
	public String execute(String content) throws SystemException {
		for(Taglib tag : tags){//遍历编译
			tag.iniContent(content);
			tag.doReplace();
			content = tag.getContent();
		} 
		return content;
	}
	
	
	
	
	
	
	
	public List<Object> getList(){
		List<Object> list = new ArrayList<Object>(); 
		Iterator<Taglib> it = tags.iterator(); 
		while(it.hasNext()){
			Taglib tag = it.next();
			list.add(tag.getConfig());
		}
		return list;
	}
	
	
	
	
	
}
