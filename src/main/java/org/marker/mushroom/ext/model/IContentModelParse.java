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
package org.marker.mushroom.ext.model;
 
 
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.template.tags.res.WebDataSource;
import org.marker.mushroom.template.tags.res.WebDataSource;

/**
 * 模型驱动引擎
 * 
 * @author marker
 * @date 2013-9-18 上午11:37:02
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public interface IContentModelParse{
	
	public static final int INSTALL_FAILD = 00;
	// 已经存在
	public static final int INSTALL_EXIST = 01;
	
	public static final int INSTALL_SUCCESS = 02;
	
	
	/** 解析错误 */
	public static final int STATUS_ERROR = 0;
	
	/** 重定向 */
	public static final int STATUS_REDIRECT = 1;
	
	/** 内容模型 */
	public static final int STATUS_MODULE = 2;
	/** 内容模型未找到 */
	public static final int STATUS_NOT_FOUND_MODULE = 3;
	
	
	
	/**
	 * 通过解析对象找到对应的模型
	 * @param resv
	 * @return
	 * @throws SystemException 
	 */
	public int parse(WebParam param) throws SystemException;
	
	
	
	public StringBuilder parse(String tableName, WebDataSource WebDataSource) throws SystemException;
	
	
	
	
	
}
