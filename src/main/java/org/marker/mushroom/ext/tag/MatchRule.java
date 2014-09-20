package org.marker.mushroom.ext.tag;

import java.util.regex.Pattern;

/**
 * 匹配规则
 * @author marker
 * @date 2012-12-04
 * */
public class MatchRule {

	/** 正则表达式 */
	private Pattern regex;
	/** 替换后的字符串 */
	private String replace;
	/** 类型：1.有数据  | 0.没数据 */
	private int type;
	
 
	
	public MatchRule(String regexStr, String replace,int type){
		this.regex = Pattern.compile(regexStr);
		this.replace = replace;
		this.type = type;
	}
	 
	
	public Pattern getRegex() {
		return regex;
	} 


	public void setRegex(Pattern regex) {
		this.regex = regex;
	}






	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	} 
}
