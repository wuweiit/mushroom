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
package org.marker.mushroom.template.tags.res;

/**
 * Sql语句数据源
 * @author marker
 * @date 2013-9-14 下午6:08:27
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class SQLDataSourceImpl extends WebDataSource {

	/** sql语句 */
	private String sql;


	
	@Override
	public void generateSql() {
		// 这里可以对模板读取的SQL语句进行一个处理。
		
		
		
		
		
		//暂时不做复杂处理
		this.queryString = sql;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

}
