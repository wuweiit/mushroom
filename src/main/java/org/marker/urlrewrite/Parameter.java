package org.marker.urlrewrite;



/**
 * 规则参数
 * @author marker
 * @date 2013-05-06
 * */
public final class Parameter {

	//参数中的键
	private String key;
	//标识(标识是规则里面的，如：{channel})
	private String express;
	//正则表示值(可能是数字也可能是字符,如：字符: ([a-zA-Z_0-9]+) ; 数字: ([0-9]+) )
	private String regex;
	
	
	/**
	 * 参数
	 * @param key get请求中的参数
	 * @param express 表达式(如：{channel})
	 * @param regex 正则表达式(用来替换匹配的)
	 * */
	public Parameter(String key, String express,String regex){
		this.key = key;
		this.express = express;
		this.regex = regex;
	}
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}
 

	public String getRegex() {
		return regex;
	}
 

	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	
	
	
}
