<%@page import="org.marker.mushroom.utils.HttpUtils"%>
<%@page import="org.marker.mushroom.utils.WebUtils"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.marker.mushroom.core.WebAPP"%>
<%@page import="org.marker.mushroom.utils.GeneratePass"%>
<%@page import="org.marker.mushroom.core.config.impl.SystemConfig"%>
<%@page import="org.marker.mushroom.core.AppStatic"%>
<%@page import="org.marker.mushroom.core.config.impl.DataBaseConfig"%>
<%@page import="java.io.FileReader"%>
<%@page import="com.mchange.v2.c3p0.ComboPooledDataSource"%>
<%@page import="org.marker.mushroom.holder.SpringContextHolder"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%> 
<%@page import="org.marker.mushroom.utils.FileTools"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%> 
<%@page import="java.io.File"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.alibaba.druid.pool.DruidDataSource" %>
<%@ include file="check_install.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>MRCMS 安装状态 Power By marker</title>
	<link rel="stylesheet" type="text/css" href="css/style.css">  
  </head>
  <body> 
<%
// 日志记录器
final Log log = LogFactory.getLog(this.getClass());
// 系统安装状态
boolean installStatus = true;
// 项目路径
String ProjectPath = request.getContextPath();

// 项目真实路径
String WebRootRealPath = application.getRealPath(File.separator);

// 虚拟路径
String WebRootPath = HttpUtils.getRequestURL(request);

String host = request.getParameter("DB_HOST");
String name = request.getParameter("DB_NAME");
String port = request.getParameter("DB_PORT");
String user = request.getParameter("DB_USER");
String pass = request.getParameter("DB_PWD");
String spot = request.getParameter("spot");//加密Key
String prefix = request.getParameter("DB_PREFIX");//表前缀

boolean status = (host != null) && name != null &&
		 port != null && user!= null && pass != null && spot != null && prefix != null;

if( status ){
	String jdbcurl = "jdbc:mysql://"+host+":"+port+"/"+name+"?useUnicode=true&characterEncoding=UTF-8";
	 
	 

	try{
	 
	    /* ==============================================
		 *              1. 获取数据库链接
		 * ==============================================
		 */
		DruidDataSource cmpd = SpringContextHolder.getBean("dataSource");
        cmpd.close();

        cmpd.setUrl(jdbcurl); // 设置
		cmpd.setUsername(user);
		cmpd.setPassword(pass);
	 	
	 	
	    /* ==============================================
		 *              2. 数据库设置持久化
		 * ==============================================
		 */
		DataBaseConfig dbc = DataBaseConfig.getInstance();
		dbc.set("mushroom.db.host", host);
		dbc.set("mushroom.db.port", port);
		dbc.set("mushroom.db.demo", name);
		dbc.set("mushroom.db.user", user);
		dbc.set("mushroom.db.pass", pass);
		dbc.set("mushroom.db.prefix", prefix);
		dbc.store();//保存
	 
	 
	 
	    /* ==============================================
		 *              3. 系统加密Key持久化
		 * ==============================================
		 */
		SystemConfig sysconfig = SystemConfig.getInstance();
		sysconfig.set("secret_key", spot);//更新Key
		sysconfig.store();//保存
	
	
	
	
	    /* =======================================================
		 *   4. 获取数据库链接，并判断数据库是否存在，不存在就创建
		 * =======================================================
		 */
		Connection conn = cmpd.getConnection();// 从连接池中获取一个链接
		String checkAndCreateSql = "CREATE database IF NOT EXISTS " + name;
		PreparedStatement ps = conn.prepareStatement(checkAndCreateSql);
		ps.executeUpdate();
		ps.close();
		
		
		
		/* =======================================================
		 *   5. 读取建表SQL信息，并创建表
		 * =======================================================
		 */
		File sqlFile = new File(WebRootRealPath + "/data/sql/db_app.sql");
		String sql = FileTools.getFileContet(sqlFile, FileTools.FILE_CHARACTER_UTF8);
		sql = sql.replaceAll("\\{prefix\\}", prefix);//替换前缀
		
		
		String[] sqla = sql.split(";");
		conn.setCatalog(name);
		Statement statement = conn.createStatement(); 
		System.out.println("==========="); 
		for(int i=0; i<sqla.length; i++){
			String a = sqla[i]; 
			if(a != null && !"".equals(a.trim())){
				log(a);
				statement.addBatch(a);
			}
		}
		System.out.println("==========="); 
		statement.executeBatch();
		statement.close();
		
		
		
		
			
		// 更新密码默认用户admin
		String pass2 = GeneratePass.encode("admin");
		ps = conn.prepareStatement("update "+prefix+"user set pass='"+pass2+"' where id=1");
		ps.executeUpdate();
		ps.close();
		conn.close();
		
	    //设置安装状态
	    application.setAttribute(AppStatic.WEB_APP_INSTALL, true);
	    String BasePath = application.getRealPath("/data/");
	   	OutputStream os = new FileOutputStream(new File(BasePath+"/install.lock"));
	   	os.write(0);
	   	os.flush();
	   	os.close();
	   	
	   	
	   	WebAPP.install = true;// 设置安装状态(必须)
	}catch(Exception e){
		installStatus = false;
		log.error("mrcms install exception", e);
	}
}
%>
<% if(installStatus){ // 安装成功  %>
安装成功！<br />
内置管理员：admin<br />
密码：admin<br />

<script type="text/javascript">
setTimeout(function(){
	window.location.href = "<%=WebRootPath%>";
},3000);

</script>



<%}else{// 安装失败 %>
安装失败！
setTimeout(function(){
	window.history.back();
},3000);





<%}%>
  </body>
</html>
