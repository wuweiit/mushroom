import org.marker.mushroom.alias.SQLimport org.marker.mushroom.core.AppStaticimport org.marker.mushroom.dao.ISupportDaoimport org.marker.mushroom.alias.DAOimport org.marker.mushroom.holder.SpringContextHolderimport org.springframework.aop.config.SpringConfiguredBeanDefinitionParserimport org.marker.mushroom.context.ActionContextimport org.marker.mushroom.beans.Pageimport org.marker.mushroom.beans.Articleimport javax.servlet.http.HttpServletRequestimport java.lang.StringBuilderimport org.marker.mushroom.core.proxy.SingletonProxyFrontURLRewriteimport org.marker.urlrewrite.URLRewriteEngine 
import org.marker.mushroom.beans.Channel;
import org.marker.mushroom.core.WebParam;
import org.marker.mushroom.ext.model.ContentModel;  
import org.marker.mushroom.template.tags.res.SqlDataSource;




/**
 * 帮助模型处理
 * 
 * @author marker
 * */ 
public class ContentModelImpl extends ContentModel {
		
	
	public ContentModelImpl(){
		configure([ 
		    icon : "images/demo.jpg",
			name  : "帮助模型",
			author: "marker",
			version : "0.1",
			type : "help",// 模型标识
			template : "help.html",
			description : "" 
	    ]);
	}  
	
	
	

	 

	
	
	/**
	 * 前台标签生成SQL遇到该模型则调用模型内算法
	 * @param tableName 表名称
	 * */
	public StringBuilder doWebFront(String tableName, SqlDataSource sqlDataSource) {
//		ObjectDataSourceImpl obj = (ObjectDataSourceImpl) sqlDataSource;
		String prefix = getPrefix();// 表前缀，如："yl_"
		StringBuilder sql = new StringBuilder("select ");
		sql.append("A.id,A.title,C.name as 'cname' ,A.time ,concat('/cms?p=',C.url,'&type=',C.module,'&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as 'url' from ");
		sql.append(prefix).append("article").append(SQL.QUERY_FOR_ALIAS)
				.append(" join ").append(prefix).append("channel").append(" C ")
				.append("on A.pid=C.id");
		return sql;
	}



 

	/**	 * 抓取内容	 */
	public void fetchContent(Serializable cid) {
		String prefix = getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		 
		Object article = commonDao.findById(Article.class, cid);
		commonDao.update("update " + prefix + "article set views = views+1 where id=?", cid);// 更新浏览量
		request.setAttribute("article", article); 
	}


	
	public void doPage(Channel current, WebParam param) {				println "page query ";
		String prefix = dbconfig.getPrefix();//表前缀，如："yl_"
		HttpServletRequest request = ActionContext.getReq();
		
		long pid  = current.getId();//当前栏目ID
		int limit = current.getRows();//每页内容条数
 
		
		int pageNo = 1;
		if(param.page != null && !"".equals(param.page)){
			try{
				pageNo = Integer.parseInt(param.page);
			}catch (Exception e) {e.printStackTrace(); }
		}
		StringBuilder sql = new StringBuilder();
		sql.append("select A.id,A.title,C.name as cname ,A.time ,concat('/cms?p=',C.url,'&type=article&id=',CAST(A.id as char),'&time=',DATE_FORMAT(A.time,'%Y%m%d')) as url from ");
		sql.append(prefix).append("article").append(Sql.QUERY_FOR_ALIAS)
		 .append(" join ").append(prefix).append("channel").append(" C on A.pid=C.id");
		sql.append(" where A.pid=").append(pid);
						ISupportDao commonDao = SpringContextHolder.getBean(DAO.COMMON);
	 
		Page currentPage = commonDao.findByPage(pageNo, limit, sql.toString());
		
		request.setAttribute(AppStatic.WEB_APP_PAGE, currentPage);
		
		//传递分页信息
		String nextPage = "/cms?p="+param.pageName+"&page="+currentPage.getNextPageNo();
		String prevPage = "/cms?p="+param.pageName+"&page="+currentPage.getPrevPageNo();
		 
		request.setAttribute("nextpage", urlrewrite.encoder(nextPage));
		request.setAttribute("prevpage", urlrewrite.encoder(prevPage));
	}

}
