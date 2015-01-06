<%@page import="org.marker.security.DES"%>
<%@page import="java.io.File"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="check_install.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>第二步、配置软件信息</title>
	<link href="css/style.css" rel="stylesheet" type="text/css" />
	<script src="../public/js/jquery.js"></script>
</head>
<body>
	<form action="progress.jsp" method="post" name="form" id="form">
	<div id="install" class="data">
	  <h1>第二步、配置软件信息</h1>
	  <div class="content">
	    <div class="list">
	      <div class="name">数据库地址：</div>
	      <div class="value"><input type="text" class="input" name="DB_HOST" id="DB_HOST" value="localhost" onblur="test_data()" /></div>
	    </div>
	    <div class="list">
	      <div class="name">数据库端口：</div>
	      <div class="value"><input type="text" class="input" name="DB_PORT" id="DB_PORT" value="3306" onblur="test_data()" /></div>
	    </div>
	    <div class="list">
	      <div class="name">数据库名称：</div>
	      <div class="value"><input type="text" class="input" name="DB_NAME" id="DB_NAME" value="db_mrcms"  onblur="test_data()" /> </div>
	      <div class="tip"></div>
	    </div>
	    <div class="list">
	      <div class="name">数据库用户：</div>
	      <div class="value"><input type="text" class="input" name="DB_USER" id="DB_USER" value="root"  onblur="test_data()" /></div>
	    </div>
	    <div class="list">
	      <div class="name">数据库密码：</div>
	      <div class="value"><input type="text" class="input" name="DB_PWD" id="DB_PWD" value="" onblur="test_data()" /></div>
	    </div>
	    <div class="list">
	    <div class="name">数据库状态：</div><div class="msg" style="float:left;"><span style="color:#666">等待检测中...</span></div>
	    </div>
	   
	    <div class="list">
	      <div class="name">数据表前缀：</div>
	      <div class="value"><input type="text" class="input" name="DB_PREFIX"  value="mr_" id="DB_PREFIX" /></div>
	    </div>  
	    <div class="list">
	      <div class="name">安全加密码：</div>
	      <div class="value"><input type="text" class="input" name="spot" value="<%=DES.getSecretKey(null) %>" /></div>
	    </div>
	  </div>
	  <div class="menu">
	  	<input name="test_conn" id="test_conn" type="hidden" value="0" />
	    <button type="button" class="submit" onclick="ins_form();" >准备完毕进入安装</button>
	  </div>
	</div>
	</form>
</body>
<script type="text/javascript" src="js/install.js"></script>
</html>
 
 