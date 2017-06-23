<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page import="org.marker.app.utils.EnvUtils" %>
<%@ page import="org.marker.mushroom.core.config.impl.SystemConfig" %>
<%@ page import="org.marker.mushroom.holder.SpringContextHolder" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );


    /** 系统配置对象 */
    SystemConfig systemConfig = SpringContextHolder.getBean("systemConfig");

    // 获取文件存储地址
    String saveRootPath = systemConfig.getFilePath();
    if(!(saveRootPath != null && !"".equals(saveRootPath))){
        saveRootPath = rootPath;
    }

//    if( EnvUtils.isDev()){
//
//    }

	out.write( new ActionEnter( request, rootPath, saveRootPath ).exec() );
	
%>