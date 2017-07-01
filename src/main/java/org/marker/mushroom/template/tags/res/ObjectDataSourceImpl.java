package org.marker.mushroom.template.tags.res;

import org.marker.mushroom.alias.SQL;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.springframework.util.StringUtils;


/**
 * 通过模板引擎解析得到的数据对象
 * @author marker
 * */
public final class ObjectDataSourceImpl extends WebDataSource{

	//数据库表名称
	private String tableName;
	//条件
	private String where = "";
	//查询第几页数据
	private int page = 1;
	//限制条数
	private String limit = "10";
	//排序
	private String order; //例如: "id desc"

	 
	
	/**
	 * 生成Sql语句
	 * @throws SystemException 
	 * */
	@Override
	public void generateSql() throws SystemException{
		String prefix = DataBaseConfig.getInstance().getPrefix();//表前缀，如："yl_"
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(SQL.QUERY_FOR_FORM).append(prefix)
				.append(this.tableName).append(SQL.ALIAS_MODEL);


		
		// 如果是模型中的表，那么就到模型工厂里面取得前部分sql语句
		StringBuilder temp = contentModelContext.parse(this.tableName, this);
		if(temp != null){ 
			queryString = temp;
		} 
  
		
		// 追加条件语句
		String where = this.where; 
		if (where != null && !"".equals(where)) {// 如果条件语句存在
			queryString.append(SQL.QUERY_FOR_WHERE);
			String[] ws = where.split(",");
			for (int i = 0; i < ws.length; i++) {
				queryString.append(SQL.QUERY_FOR_ALIAS_DOT + ws[i]);
				if (i != (ws.length-1)) {
					queryString.append(SQL.QUERY_FOR_AND);
				}
			}

		}
		
		
		
		//追加排序语句
		if (this.order != null && !"".equals(this.order)) {
			queryString.append(SQL.QUERY_FOR_ORDERBY)
					.append(SQL.QUERY_FOR_ALIAS_DOT).append(this.order);
		}

		// limit 限制输出
		if(!StringUtils.isEmpty(this.limit)){
			queryString.append(" limit ").append(this.limit);
		}

		this.queryString = queryString.toString();
	}
	
	
 




	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
  
	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
 
	
	
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
 
	@Override
	public String toString(){
		String a = "Table: "+this.tableName;
		 a += "\n items:"+this.items;
		 a += "\n var:"+this.var;
		 a += "\n limit:"+this.limit; 
		 a += "\n where:"+this.where;
		 a += "\n order:"+this.order;
		 a += "\n page:"+page;
		return a;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

 
	
	
	
	
}
