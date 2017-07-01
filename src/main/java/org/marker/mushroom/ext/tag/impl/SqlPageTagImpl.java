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
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.template.MyCMSTemplate;
import org.marker.mushroom.template.tags.res.SQLDataSourceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * SQL处理
 *
 *  占时针对四院官网写死
 *
 * @author marker
 * @date 2013-9-14 下午1:13:28
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class SqlPageTagImpl extends Taglib {

	/** 默认构造 */
	public SqlPageTagImpl() {
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "执行分页SQL语句");
		config.put("author", "marker");
		config.put("doc", "doc/12.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\s*<!--\\s*\\{(\\w+):page[\\x20]+sql\\=\\((.+)\\)\\s*\\}\\s*-->[\\x20]*\\n?",
			"<#list $2 as $1>\n",1);
		this.put("\\s*<!--\\s*?\\{/page\\}\\s*?-->[\\x20]*\\n?",
			"</#list>\n", 0);
	}
	
	 
	public void doDataReplace(MatchRule mr) throws SystemException {
        MyCMSTemplate cmstemplate = SpringContextHolder.getBean(Core.ENGINE_TEMPLATE);
		Matcher m = mr.getRegex().matcher(content);
		while(m.find()){
			//处理提取数据.进行持久化操作
			String text = m.group();

			
    		//解析数据库相关字段表名等。
			int sql_start = text.indexOf("sql=(")+5;
			int sql_end   = text.lastIndexOf(")");
			String sql = text.substring(sql_start, sql_end);

			SQLDataSourceImpl datasource = new SQLDataSourceImpl();
			
			//创建数据引擎 
			int left_start = text.indexOf("{")+1;
			int var_dot = text.indexOf(":");
			String var = text.substring(left_start, var_dot);

			String items = "page";
			datasource.setType("page");
			datasource.setVarAndItems(var, items);

            String contentId = WebParam.get().contentId;


            // TODO 占时写死的代码，需要调整
            if(sql.indexOf("thematic_article") !=-1){

                datasource.setSql(sql +" and b.tid = "+contentId);

            }else{
                datasource.setSql(sql);
            }



		 
			String re = "<#list ".concat(items).concat(".data as ").concat(var).concat(">");
			
			content = content.replace(text, re);//替换采用UUID生成必须的


            cmstemplate.put(datasource);
		}
	}
	
	public static void main(String[] args) {
		
		SqlPageTagImpl sql = new SqlPageTagImpl();
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
