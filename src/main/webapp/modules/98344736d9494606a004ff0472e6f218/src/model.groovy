import org.marker.mushroom.alias.DAOimport java.lang.StringBuilderimport java.lang.Exceptionimport org.marker.mushroom.context.ActionContextimport org.marker.mushroom.core.AppStaticimport org.marker.mushroom.holder.SpringContextHolderimport org.marker.mushroom.beans.Pageimport org.marker.mushroom.dao.ISupportDaoimport javax.servlet.http.HttpServletRequest 
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.ext.model.ContentModel;  
import org.marker.mushroom.template.tags.res.SqlDataSource;




/**
 * 商品模型处理
 * 
 * @author marker
 * */ 
public class ContentModelImpl extends ContentModel {
	
	
	public ContentModelImpl(){
		// 配置模型（必须调用）
		configure([ 
		    icon : "images/demo.jpg",
			name  : "商品模型",
			author: "marker",
			version : "0.1",
			type : "goods",// 模型标识
			template : "",
			description : "" 
	    ]);
	}
	
	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, SqlDataSource sqlDataSource) {
		String prefix = getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder();
		sql.append("select A.*, concat('p=',A.url) 'url' from ");
		sql.append(prefix).append("channel ").append(Sql.QUERY_FOR_ALIAS);
		return sql;
	}
	
	

	public void doPage(WebParam param) { 
		
	}
	
	
}
