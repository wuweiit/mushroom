package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.CategoryDataSourceImpl;




/**
 * 
 * <!--{category } -->
 *
 *
 *     分类数据查询，单级别
 * 
 * @author marker
 * @version 1.0
 */
public class SingleCategoryTagImpl extends Taglib  {

	public SingleCategoryTagImpl() { 
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "分类单级数据查询标签");
		config.put("author", "marker");
		config.put("doc", "doc/50.md");
		config.put("description", "系统内置"); 
		this.configure(config); 

		this.put("\\s*<!--\\s*\\{category\\s*[\\w+\\=\\('?\\w+\\x20?\\d*\\w+'?\\)\\s*]*\\}\\s*-->[\\x20]*\\n?",
				"<#list $2 as $1>\n",Taglib.MODE_DATA);
		this.put("\\s*<!--\\s*\\{/category\\}\\s*-->[\\x20]*\\n?", 
				"</#list>\n", Taglib.MODE_NO_DATA);
	}
	
	

	
	/**
	 * 处理拥有数据的标签
	 * @throws SystemException 
	 */ 
	public void doDataReplace(MatchRule mr) throws SystemException {
		MyCMSTemplate cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE);
		Matcher m = mr.getRegex().matcher(content);
		while(m.find()){
			//处理提取数据.进行持久化操作
			String text = m.group(); 
    		// 解析数据库相关字段表名等。
			int sql_start = text.indexOf(":")+1;
			int sql_end   = text.lastIndexOf("}");
			String text2 = text.substring(sql_start, sql_end); 
			Pattern p_a = Pattern.compile("\\w+\\=\\('?\\w*\\x20?\\d*\\w*'?"); // 将给定的正则表达式编译到模式中
			Matcher m_a = p_a.matcher(text2);
			
			// 创建数据引擎
			CategoryDataSourceImpl data = new CategoryDataSourceImpl();
			
			
			String whereTemp = "";//必须初始化""
			while(m_a.find()){  
				String[] field_kv = m_a.group().split("\\=\\(");//拆分数据格式 ："var=(xxxx"
				if("alias".equals(field_kv[0])) {//遍历内部对象变量名称，参考forEach标签
					data.setVar(field_kv[1]);
					continue;
				}else if("limit".equals(field_kv[0])) {//数据量限制
					data.setLimit(field_kv[1]);
					continue;
				}else if("order".equals(field_kv[0])){//排序支持
					data.setOrder(field_kv[1]);
				}else { 
					 if("id".equals(field_kv[0]))
							data.setId(Integer.parseInt(field_kv[1]));
					 
					whereTemp += field_kv[0]+"="+field_kv[1]+",";
				}
			}
			data.setItems("mrcms_"+UUID.randomUUID().toString().replaceAll("-",""));
			String re = "<#list "+data.getItems()+" as "+data.getVar()+">";
			
			content = content.replace(text, re);//替换采用UUID生成必须的
			data.setWhere(whereTemp);//设置条件
			
			
			
			
			cmstemplate.put(data);
		}
	}
	
	
	
}
