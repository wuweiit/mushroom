package org.marker.mushroom.ext.tag.impl;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.ObjectDataSourceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 遍历集合标签
 * 说明：采用解析结构生成SQL语句进行进一步的查询，最后使用MVC模式传递数据
 * 格式：
 * <code>
 *  <!-- {list:var=(channel) items=(channels) table=(channel) pid=(0) page=(1) limit=(10)} --> 
 *		<a href="${channel.url}">${channel.name}</a>
 *	<!-- {/list} -->
 * </code>
 * 2013-01-18: 调整了输出HTML代码的格式，增加可读性
 * 2013-01-20 根据item参数来存取SQLDataEngqin
 * 
 * @author marker
 * @date 2013-01-17
 */
public class ListCategoryTagImpl extends Taglib {

	/** 默认构造 */
	public ListCategoryTagImpl(){
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "遍历分类数据");
		config.put("author", "marker");
		config.put("doc", "doc/5.md");
		config.put("description", "系统内置"); 
		this.configure(config);

	
		this.put("\\s*<!--\\s*\\{(\\w+):listCateogry \\s*table\\=\\((\\w+)\\)\\s*[\\w+\\=\\('?\\w+\\x20?\\d*\\w+'?\\)\\s*]*\\}\\s*-->[\\x20]*\\n?",
				"<#list $2 as $1>\n",1);
		this.put("\\s*<!--\\s*?\\{/list\\}\\s*?-->[\\x20]*\\n?", 
				"</#list>\n", 0);
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
    		//解析数据库相关字段表名等。
			int sql_start = text.indexOf(":")+1;
			int sql_end   = text.lastIndexOf("}");
			String text2 = text.substring(sql_start, sql_end); 
			Pattern p_a = Pattern.compile("\\w+\\=\\('?\\w*\\x20?\\d*\\w*'?"); // 将给定的正则表达式编译到模式中
			Matcher m_a = p_a.matcher(text2);
			
			
			int left_start = text.indexOf("{")+1;
			//创建数据引擎
			ObjectDataSourceImpl data = new ObjectDataSourceImpl();
			
			String var = text.substring(left_start, sql_start-1);
			 
			data.setVar(var);

//			WebParam param = WebParam.get();
//			param.category;

			
			
			
			String whereTemp = "";//必须初始化""
			while(m_a.find()){  
				String[] field_kv = m_a.group().split("\\=\\(");//拆分数据格式 ："var=(xxxx"
				if("table".equals(field_kv[0])) {
					data.setTableName(field_kv[1]);
					continue;
				}else if("var".equals(field_kv[0])) {//遍历内部对象变量名称，参考forEach标签
					data.setVar(field_kv[1]);
					continue;
				}else if("limit".equals(field_kv[0])) {//数据量限制
					data.setLimit(field_kv[1]);
					continue;
				}else if("page".equals(field_kv[0])){//查询页码
					data.setPage(Integer.parseInt(field_kv[1]));
					continue;
				}else if("order".equals(field_kv[0])){//排序支持
					data.setOrder(field_kv[1]);
				}else {// Where条件
					whereTemp += field_kv[0]+"="+field_kv[1]+",";
				}

			}
            whereTemp += "did="+WebParam.get().contentId+",";

			data.setItems("mrcms_"+UUID.randomUUID().toString().replaceAll("-",""));
			String re = "<#list "+data.getItems()+" as "+data.getVar()+">";
			
			content = content.replace(text, re);//替换采用UUID生成必须的
			data.setWhere(whereTemp);//设置条件

			cmstemplate.put(data);

		}
	}
	
}
