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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.ext.tag.MatchRule;
import org.marker.mushroom.ext.tag.Taglib;


/**
 * 遍历集合标签
 * 说明：采用解析结构生成SQL语句进行进一步的查询，最后使用MVC模式传递数据
 * 格式：
 * <code>
 *  <!-- {c:channel} -->
 *		<a href="${c.url}">${c.name}</a>
 *	<!-- {/list} -->
 * </code>
 * 
 * @author marker
 * @date 2013-01-17
 */
public class ChildChannelTagImpl extends Taglib {

	/** 默认构造 */
	public ChildChannelTagImpl(){
		Map<String,Object> config = new HashMap<String, Object>();
		config.put("name", "二级子栏目输出");
		config.put("author", "marker");
		config.put("doc", "doc/20.md");
		config.put("description", "系统内置"); 
		this.configure(config); 
		
		
		this.put("\\s*<!--\\x20*\\{\\x20*NavChild\\x20*(=>\\x20*\\w+)?\\x20*\\}\\x20*-->\\x20*\\n?",
				"<#list $2 as $1>\n",1);
		this.put("\\s*<!--\\s*?\\{/NavChild\\}\\s*?-->[\\x20]*\\n?",
				"</#list></@NavChild>\n", 0);
	}
  
	/**
	 * 处理拥有数据的标签
	 * @throws SystemException 
	 */
	@Override
	public void doDataReplace(MatchRule mr) throws SystemException {
		Matcher m = mr.getRegex().matcher(content);
		while(m.find()){
			//处理提取数据.进行持久化操作
			String text = m.group();
            String first = m.group(1);

            String var = "it";
            if(!StringUtils.isEmpty(first)){
                var = first.replace("=>","").trim();
            }
			//创建数据引擎
			String items = "navChildList";
			String re = "<@NavChild items=\"" + items + "\"><#list "+items+" as "+ var +">";
			content = content.replace(text, re);//替换采用UUID生成必须的

		}
	}
	
}
