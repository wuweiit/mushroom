<%@page import="java.io.File"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="check_install.jsp" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>第一步、阅读软件安装协议 Power By marker</title>
	<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div id="install">
	<h1>第一步、阅读软件使用协议</h1>
    <div class="content">
    

版权所有(C) 2012-2017 MRCMS保留所有权利。<br />
为了保证您和他人的利益请遵循以下几条使用规则：<br />
1、您可以完全遵循本协议的情况下，将本软件用于商业用途，而不必支付软件的使用费用，但我们也不承诺对非赞助用户提供任何形式的技术支持与帮助。<br />
2、使用本程序您可以不用在明显页面保留程序版权信息，但程序最终版权贵原作者所有，为了程序能持续发展建议您在网站底部注明：Powered By MRCMS ，另外我们可能不会对不保留版权信息的用户提供帮助。<br />
3、您可以免费使用本软件，但是禁止对软件进行改名发布，禁止以任何形式对MRCMS形成竞争。<br />
4、您可以对程序进行二次开发，但是二次开发后的软件也禁止公开发布，可以自己分配使用版权参考第三条。<br />
5、非赞助用户我可能不会提供程序方面支持与改写。<br />
6、觉得有用可以 赞助我：<a href="" style="color:red" target="_banlk"> marker</a><br />
    </div>
    <div class="menu">
    	<button type="button" onclick="no()">不同意</button>
        <button type="button" class="submit" onclick="window.location.href='install.jsp'">同意</button>
    </div>
</div>
</body>
<script type="text/javascript">
function no(){
	alert('感谢您对MRCMS的支持！\n我的邮箱: admin@wuweibi.com');
	window.close();
}
</script>
</html>
