<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
Connection conn = null;
try{
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	
	String host = request.getParameter("DB_HOST");
	String port = request.getParameter("DB_PORT");
	String user = request.getParameter("DB_USER");
	String password = request.getParameter("DB_PWD");
	
	
	String url = "jdbc:mysql://"+host+":"+port+"/"+"?useUnicode=true&characterEncoding=UTF-8";
	Class.forName("org.gjt.mm.mysql.Driver");
	
	conn = DriverManager.getConnection(url, user, password);
	out.print("1");
}catch(Exception e){
	out.print("数据库连接失败，请检查连接信息是否正确！");
}finally {
    if(conn != null)
		conn.close();

}
%>


