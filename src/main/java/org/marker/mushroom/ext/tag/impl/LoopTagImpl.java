package org.marker.mushroom.ext.tag.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.alias.Core;
import org.marker.mushroom.alias.DAO;
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.channel.TreeUtils;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.dao.IChannelDao;
import org.marker.mushroom.dao.impl.ChannelDaoImpl;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.ObjectDataSourceImpl;
import org.marker.mushroom.template.tags.res.PageDataSourceImpl;


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
			String var = m.group(1);// loop内部局部变量对象名称

            int sql_start = text.indexOf(":")+1;
            int sql_end   = text.lastIndexOf("}");
            String text2 = text.substring(sql_start, sql_end);

            if("".equals(var)){// 默认: it
				var = "it";
			}

            Pattern p_a = Pattern.compile("\\w+\\=\\('?\\w*\\x20?\\d*\\w*'?"); // 将给定的正则表达式编译到模式中
            Matcher m_a = p_a.matcher(text2);


            //创建数据引擎
            PageDataSourceImpl data = new PageDataSourceImpl();


            data.setItems("page");// 默认的输出对象

            String whereTemp = "";//必须初始化""
            while(m_a.find()){
                String[] field_kv = m_a.group().split("\\=\\(");//拆分数据格式 ："var=(xxxx"
                String key   = field_kv[0];
                String val = field_kv[1];
                if("table".equals(key)) {
                    data.setTableName(val);
                    continue;
                } else if("items".equals(key)) { // 数据量限制
                    data.setItems(val);
                    continue;
                }  else if("order".equals(key)){ // 排序支持
                    data.setOrder(val);
                    continue;
                } else if("prefix".equals(key)) { // 表前缀处理
                    data.setPrefix(val);
                    continue;
                }else {// Where条件
                    whereTemp += key + "=" + val + ",";
                }
            }
            data.setType("page");
            data.setVar(var);
            data.setWhere(whereTemp);

            // 限制在当前栏目下的数据
            WebParam param = WebParam.get();

            int cid = param.channel.getId();

            IChannelDao channelDao = SpringContextHolder.getBean(DAO.CHANNEL);


            // 计算子节点
            List<Channel> channelList = channelDao.findAll();

            List<Integer> cidList  = new ArrayList<>();
            cidList.add(cid);
            TreeUtils.buildChildIdList(channelList , cidList, cid);

            String cids = StringUtils.join(cidList,",");
            data.setWhereIn("cid in ("+cids+") ");








			// page.data为内容模型中使用的查询
			String re = "<#list page.data as " + var + ">";
			
			content = content.replace(text, re);// 替换采用UUID生成必须的

			MyCMSTemplate cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE);

			cmstemplate.put(data);


		}
	}
	
}
