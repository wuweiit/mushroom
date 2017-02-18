<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page import="org.marker.app.utils.EnvUtils" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );

    String saveRootPath = "/usr/local/nginx/html/";
    if( EnvUtils.isDev()){

    }

	out.write( new ActionEnter( request, rootPath,saveRootPath ).exec() );
	
%>