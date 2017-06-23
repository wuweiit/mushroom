package org.marker.mushroom.ext.tag.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.UNIT;
import org.marker.mushroom.core.SystemStatic;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.ObjectDataSourceImpl;
import org.marker.mushroom.template.tags.res.SqlDataSourceImpl;


/**
 * 遍历集合标签
 * 说明：采用解析结构生成SQL语句进行进一步的查询，最后使用MVC模式传递数据
 * 格式：
 * <code>
 *  <!-- {c:channel} -->
 *		<a href="${c.url}">${c.name}</a>
 *	<!-- {/list} -->
 * </code>
 * 2013-01-18: 调整了输出HTML代码的格式，增加可读性
 * 2013-01-20 根据item参数来存取SQLDataEngqin
 * 
 * @author marker
 * @date 2013-01-17
 */
public class ChildChannelTagImpl extends Taglib {

	/** 默认构造 */
	public ChildChannelTagImpl(){
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "子栏目输出");
		config.put("author", "marker");
		config.put("doc", "doc/14.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\s*<!--\\s*\\{(\\w+):channel\\s*\\}\\s*-->[\\x20]*\\n?",
				"<#list $2 as $1>\n",1);
		this.put("\\s*<!--\\s*?\\{/channel\\}\\s*?-->[\\x20]*\\n?",
				"</#list>\n", 0);
	}
  
	/**
	 * 处理拥有数据的标签
	 * @throws SystemException 
	 */
	@Override
	public void doDataReplace(MatchRule mr) throws SystemException {
		MyCMSTemplate cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE);
		Matcher m = mr.getRegex().matcher(content);
		while(m.find()){
			//处理提取数据.进行持久化操作
			String text = m.group(); 
    		//解析数据库相关字段表名等。
			int sql_start = text.indexOf(":")+1;

			int left_start = text.indexOf("{")+1;
			//创建数据引擎
			SqlDataSourceImpl data = new SqlDataSourceImpl();
			
			String var = text.substring(left_start, sql_start-1);
			 
			data.setVar(var);
            WebParam param = WebParam.get();
			String pageName = param.pageName;
			int cid = param.channel.getId();
            DataBaseConfig config = DataBaseConfig.getInstance();

			long pid = param.channel.getPid();

			String sql = "select *,id = "+cid + " as active, concat('p=', url) url from "+config.getPrefix()
					+"channel where (hide=1 and pid = (select distinct id from "+config.getPrefix()+"channel where id = "+pid+" limit 1)) "
					+" order by sort desc";
			data.setSql(sql);

			data.setItems("mrcms_"+UUID.randomUUID().toString().replaceAll("-",""));
			String re = "<#list "+data.getItems()+" as "+data.getVar()+">";
			
			content = content.replace(text, re);//替换采用UUID生成必须的

			cmstemplate.put(data);  
		}
	}
	
}
