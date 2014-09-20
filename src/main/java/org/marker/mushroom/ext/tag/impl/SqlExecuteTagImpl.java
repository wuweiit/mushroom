/**
 * 
 */
package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;

import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.template.tags.res.SqlDataSourceImpl;

/**
 * SQL处理
 * @author marker
 * @date 2013-9-14 下午1:13:28
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class SqlExecuteTagImpl extends Taglib {
	
	/** 默认构造 */
	public SqlExecuteTagImpl() {	
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "执行SQL语句");
		config.put("author", "marker");
		config.put("doc", "doc/9.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\s*<!--\\s*\\{(\\w+):list[\\x20]+sql\\=\\(([\\w+[\\x20]*\\.*\\=*]+)\\)\\s*\\}\\s*-->[\\x20]*\\n?",
			"<#list $2 as $1>\n",1);
		this.put("\\s*<!--\\s*?\\{/list\\}\\s*?-->[\\x20]*\\n?", 
			"</#list>\n", 0);
	}
	
	 
	public void doDataReplace(MatchRule mr) {
		Matcher m = mr.getRegex().matcher(content);
		while(m.find()){
			//处理提取数据.进行持久化操作
			String text = m.group();
			
			System.out.println(text);
			
    		//解析数据库相关字段表名等。
			int sql_start = text.indexOf("sql=(")+5;
			int sql_end   = text.lastIndexOf(")");
			String sql = text.substring(sql_start, sql_end); 
			
			SqlDataSourceImpl datasource = new SqlDataSourceImpl(); 
			
			//创建数据引擎 
			int left_start = text.indexOf("{")+1;
			int var_dot = text.indexOf(":");
			String var = text.substring(left_start, var_dot);
			String items = "mrcms_"+UUID.randomUUID().toString().replaceAll("-","");
			datasource.setVarAndItems(var, items);
		 
			String re = "<#list ".concat(items).concat(" as ").concat(var).concat(">");
			
			content = content.replace(text, re);//替换采用UUID生成必须的
			
			
		}
	}
	
	public static void main(String[] args) {
		
		SqlExecuteTagImpl sql = new SqlExecuteTagImpl();
		sql.iniContent("<!--{a:list sql=(select * from channel join t_asdasd on dsad.dasdsa=dsads)}--> \n" +
				" ");
		
		try {
			sql.doReplace();
		} catch (SystemException e) { 
			e.printStackTrace();
		}
		System.out.println(sql.getContent());
		
	}
}
