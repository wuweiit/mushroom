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
package org.marker.develop.freemarker;

import java.util.Properties;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;



/**
 * 消息包装模型
 * @author marker
 * @version 1.0
 */
public class MessageWrapperModel implements TemplateHashModel {

	 private final Properties pro;
	 private final ObjectWrapper wrapper; 
	 
	 
	 
	 
	
	public MessageWrapperModel(Properties pro, ObjectWrapper wrapper) { 
		this.pro = pro;
		this.wrapper = wrapper; 
	}

	
	
	
	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if(key == null){
			return wrapper.wrap(null);
		} 
		if(pro != null){
			String val = pro.getProperty(key);
			return wrapper.wrap(val);
		}
		return wrapper.wrap(null);
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		return pro == null || pro.isEmpty();
	}
	
	
	
	
}
