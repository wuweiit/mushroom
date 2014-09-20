package org.marker.mushroom.template.tags.res;

/**
 * Sql语句数据源
 * @author marker
 * @date 2013-9-14 下午6:08:27
 * @version 1.0
 * @blog www.yl-blog.com
 * @weibo http://t.qq.com/wuweiit
 */
public class SqlDataSourceImpl extends SqlDataSource {

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
