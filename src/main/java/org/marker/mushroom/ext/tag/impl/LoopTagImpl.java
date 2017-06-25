package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.ObjectDataSourceImpl;


/**
 * 遍历当前栏目下的文章集合标签
 * 说明：采用解析结构生成SQL语句进行进一步的查询，最后使用MVC模式传递数据
 * 格式：
 * <code>
 *  <!-- {loop} --> 
 *		<a href="${it.url}">${it.name}</a>
 *	<!-- {/loop} -->
 * </code>
 * 2013-01-18: 调整了输出HTML代码的格式，增加可读性
 * 2013-01-20 根据item参数来存取SQLDataEngqin
 * 
 * @author marker
 * @date 2013-01-17
 */
public class LoopTagImpl extends Taglib{

	/** 默认构造 */
	public LoopTagImpl(){
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "遍历当前栏目内容");
		config.put("author", "marker");
		config.put("doc", "doc/6.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		// 正则植入
		this.put("\\s*<!--\\s*?\\{(\\w+):loop\\s*[\\w+\\=\\('?\\w+\\x20?\\d*\\w+'?\\)\\s*]*\\}\\s*?-->[\\x20]*\\n?", "<#list page.data as $1>\n", Taglib.MODE_DATA);
		this.put("\\s*<!--\\s*\\{/loop\\}\\s*-->[\\x20]*\\n?", "</#list>\n", Taglib.MODE_NO_DATA);
	}
	

	
	/**
	 * 处理拥有数据的标签
	 * @throws SystemException 
	 */ 
	public void doDataReplace(MatchRule mr) throws SystemException { 
		Matcher m = mr.getRegex().matcher(content);
		while(m.find()){
			//处理提取数据.进行持久化操作
			String text = m.group();
			String item = m.group(1);// loop内部局部变量对象名称

            int sql_start = text.indexOf(":")+1;
            int sql_end   = text.lastIndexOf("}");
            String text2 = text.substring(sql_start, sql_end);

            if("".equals(item)){// 默认: it
				item = "it";
			}

            Pattern p_a = Pattern.compile("\\w+\\=\\('?\\w*\\x20?\\d*\\w*'?"); // 将给定的正则表达式编译到模式中
            Matcher m_a = p_a.matcher(text2);


            //创建数据引擎
            ObjectDataSourceImpl data = new ObjectDataSourceImpl();



            String whereTemp = "";//必须初始化""
            while(m_a.find()){
                String[] field_kv = m_a.group().split("\\=\\(");//拆分数据格式 ："var=(xxxx"
                if("table".equals(field_kv[0])) {
                    data.setTableName(field_kv[1]);
                    continue;
                }else if("limit".equals(field_kv[0])) {//数据量限制
                    data.setLimit(field_kv[1]);
                    continue;
                } else if("order".equals(field_kv[0])){//排序支持
                    data.setOrder(field_kv[1]);
                }else {// Where条件
                    whereTemp += field_kv[0]+"="+field_kv[1]+",";
                }
            }
            data.setItems(item);
            data.setWhere(whereTemp);


			// page.data为内容模型中使用的查询
			String re = "<#list page.data as " + item + ">";
			
			content = content.replace(text, re);// 替换采用UUID生成必须的

			MyCMSTemplate cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE);

			cmstemplate.put(data);


		}
	}
	
}
