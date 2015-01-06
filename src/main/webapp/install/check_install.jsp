<%@page import="java.io.File"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
//如果已经安装了本系统，那么就转向主页
String lockFile = ".."+File.separator+"data"+File.separator+"install.lock";
if(new File(lockFile).exists()){ 
	response.setHeader("locaion", "../"); 
}
%>