package org.marker.mushroom.template.tags.res;

import org.marker.mushroom.alias.Services;
import org.marker.mushroom.alias.SQL;
import org.marker.mushroom.core.config.impl.DataBaseConfig;
import org.marker.mushroom.core.exception.SystemException;
import org.marker.mushroom.holder.SpringContextHolder;
import org.marker.mushroom.service.impl.CategoryService;
import org.springframework.util.StringUtils;

public class CategoryDataSourceImpl extends WebDataSource {

	
	private int id;
	// 条件
	private String where = ""; 
	// 限制条数
	private String limit = "10";
	// 排序
	private String order; //例如: "id desc"
	
	
	
	
	
	
	
	@Override
	public void generateSql() throws SystemException {
		String prefix = DataBaseConfig.getInstance().getPrefix();//表前缀，如："yl_"
		
		
		
		CategoryService service = SpringContextHolder.getBean(Services.CATEGORY);
		
		// 通过分类ID查询出内容模型，为表名称
		String model = service.findModelById(this.id);

		StringBuilder queryString = new StringBuilder();
		queryString.append("select ")
			.append(SQL.ALIAS_MODEL).append(".*,C.name cname, concat('/cms?','type="+model+"','&id=',CAST(M.id as char),'&time=',DATE_FORMAT(M.time,'%Y%m%d')) 'url'")
			.append(" from ").append(prefix).append("category").append(SQL.ALIAS_CATEGORY)
			.append(SQL.RIGHT_JOIN).append(prefix).append(model).append(SQL.ALIAS_MODEL).append(SQL.ON_MCID_E_CID);

		
		
		// 如果是模型中的表，那么就到模型工厂里面取得前部分sql语句
//		StringBuilder temp = contentModelContext.parse(model, this);
//		if(temp != null){ 
//			queryString = temp;
//		} 
//  
		
		// 追加条件语句
		String where = this.where; 
		if (where != null && !"".equals(where)) {// 如果条件语句存在
			queryString.append(SQL.QUERY_FOR_WHERE);
			String[] ws = where.split(",");
			for (int i = 0; i < ws.length; i++) {
				queryString.append(SQL.ALIAS_CATEGORY +"."+ ws[i]);
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







	public int getId() {
		return id;
	}







	public void setId(int id) {
		this.id = id;
	}







	public String getWhere() {
		return where;
	}







	public void setWhere(String where) {
		this.where = where;
	}







	public String getLimit() {
		return limit;
	}







	public void setLimit(String limit) {
		this.limit = limit;
	}







	public String getOrder() {
		return order;
	}







	public void setOrder(String order) {
		this.order = order;
	}
	
	

}
