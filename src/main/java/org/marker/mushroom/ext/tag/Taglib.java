package org.marker.mushroom.ext.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.marker.mushroom.core.exception.SystemException;

/**
 * 标签抽象类
 * 标签在编译模版文件的时候才会使用到
 * @author marker
 * @date 2012-12-04
 * */
public abstract class Taglib {
	
	/** 数据模式 */
	public static final int MODE_DATA    = 1;
	/** 无数据模式 */
	public static final int MODE_NO_DATA = 0;
	
	
	public static final String CONFIG_MODULE = "module";

	//匹配规则数组
	protected List<MatchRule> rules = new ArrayList<MatchRule>(1);
 
	//要替换的字符串
	protected String content;

	// 标签配置信息
	private Map<String, Object> config;
 
	
	
	/** 默认构造 */
	public Taglib(){ }
	
	 
	public void iniContent(String content) {
		this.content = content;
	}
	
	// 设置标签的模型UUID
	public Object setModule(String uuid){
		return config.put(CONFIG_MODULE, uuid);
	}
	
	/**
	 * 添加匹配规则
	 * @param regex   正则表达式
	 * @param replace 替换内容
	 * @param type 类型：1.有数据  | 0.没数据 
	 * */
	public void put(String regex, String replace, int type){
		MatchRule mr = new MatchRule(regex, replace, type);
		rules.add(mr);
	}
	
	
	
	// 默认替换支持 
	public void doReplace() throws SystemException {
		for(MatchRule rule : rules){
			int type = rule.getType();
			switch (type) {
				case MODE_DATA:
					this.doDataReplace(rule);
					break;
	
				default:
					Matcher m = rule.getRegex().matcher(this.content);
					this.content = m.replaceAll(rule.getReplace());
					break;
			}
		} 
	}
	
	
	/**
	 * 
	 * 替换构造MVC的视图（挖坑填充对象）
	 * 
	 * @throws SystemException 
	 */ 
	public void doDataReplace(MatchRule mr) throws SystemException{}
	
	 
	public String getContent() {
		return content;
	}
 
	protected void configure(Map<String,Object> config){
		this.config = config;
	}
	
	
	public Object getConfig(){
		return config;
	}
}
