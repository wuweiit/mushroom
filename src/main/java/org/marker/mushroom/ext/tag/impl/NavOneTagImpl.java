/**
 * The GNU GPL is the most widely used free software license and
 * has a strong copyleft requirement. When distributing derived works,
 * the source code of the work must be made available under the same
 * license. There are multiple variants of the GNU GPL, each with
 * different requirements.
 *
 *     https://choosealicense.com/licenses/gpl-2.0/#
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.marker.mushroom.ext.tag.impl;

import org.marker.mushroom.alias.Core;
import org.marker.mushroom.context.ActionContext;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.SQLDataSourceImpl;
import org.marker.mushroom.utils.HttpUtils;
import org.marker.mushroom.utils.WebUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;


/**
 * 遍历二级栏目集合标签
 * 说明：采用解析结构生成SQL语句进行进一步的查询，最后使用MVC模式传递数据
 * 格式：
 * <code>
 *  <!-- {c:NavOne} -->
 *		<a href="${c.url}">${c.name}</a>
 *	<!-- {/NavOne} -->
 * </code>
 * 2013-01-18: 调整了输出HTML代码的格式，增加可读性
 * 2013-01-20 根据item参数来存取SQLDataEngqin
 * 
 * @author marker
 * @date 2013-01-17
 */
public class NavOneTagImpl extends Taglib {

	/** 默认构造 */
	public NavOneTagImpl(){
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "一级栏目输出");
		config.put("author", "marker");
		config.put("doc", "doc/14.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("[\\x20]*<!--\\s*\\{(\\w+):NavOne\\s*\\}\\s*-->[\\x20]*\\n?",
				"<#list $2 as $1>\n",1);
		this.put("[\\x20]*<!--\\s*?\\{/NavOne\\}\\s*?-->[\\x20]*\\n?",
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
			SQLDataSourceImpl data = new SQLDataSourceImpl();
			
			String var = text.substring(left_start, sql_start-1);
			 
			data.setVar(var);
            WebParam param = WebParam.get();
			String pageName = param.pageName;
			int cid = param.channel.getId();
            DataBaseConfig config = DataBaseConfig.getInstance();

			long pid = param.channel.getPid();


			//INSERT INTO `db_mrcms_dev`.`mr_channel`(`id`, `pid`, `sort`, `rows`, `name`, `template`, `hide`, `keywords`, `description`, `url`, `icon`, `redirect`, `langkey`, `end`, `categoryIds`, `contentId`) VALUES (1, 0, 10, 10, '首页', 'index.html', 1, '华西医院 华西四院  医院 眼科 耳鼻喉科 生病', '华西公共卫生学院是中国最著名的公共卫生学院之一，历史可追溯到1914年在“华西协和大学医学院”建立的公共卫生学课程组', 'index', '', '', '', 0, '1,11,18,2,6', 23);

			HttpServletRequest request = ActionContext.getReq();
			String lang = HttpUtils.getLanguage(request);


			String sql = "select c.`id`, c.`pid`, c.`sort`, ifnull((select `value` from mr_sys_language WHERE `key` = c.langKey and lang='"+lang+"'), c.name) as name," +
					"  c.`keywords`, c.`description`, c.`icon`,  c.`langkey`,  c.id = "+cid + " as active, concat('p=', c.url) url from "+config.getPrefix()
					+"channel c where c.hide =1 ";
			if(pageName.indexOf("/") == -1){// 一级查询
				sql += " and c.pid = 0";
			}else{// 二级查询
				sql +=" and c.pid = " + pid;
			}
			sql += " order by c.sort asc";

			data.setSql(sql);

			data.setItems("mrcms_"+UUID.randomUUID().toString().replaceAll("-",""));
			String re = "<#list "+data.getItems()+" as "+data.getVar()+">";
			
			content = content.replace(text, re);//替换采用UUID生成必须的

			cmstemplate.put(data);  
		}
	}
	
}
