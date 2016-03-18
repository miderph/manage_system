<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="net.zhilink.tools.InitManager"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
response.setHeader("Cache-Control", "10");
response.setHeader("Expires", "Fri, 07 Jun 1971 08:22:39 GMT");

//Object admin = request.getSession().getAttribute("admin");
Object admin = request.getSession().getAttribute("user");
if (admin == null) {
	response.sendRedirect(".");
	return ;
}

String reportUri = InitManager.getReportUri();
%>
<!DOCTYPE HTML>
<html manifest="">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta charset="UTF-8">

  <title>v2.8-OTT业务管理平台</title>
  
  <link rel="stylesheet" type="text/css" href="fontawesome/css/font-awesome.min.css" />
  <link rel="stylesheet" type="text/css" href="ext/packages/ext-theme-classic/build/resources/ext-theme-classic-all.css" />
  <link rel="stylesheet" type="text/css" href="app/resources/app.css" />
  
  <link rel="stylesheet" type="text/css" href="app/resources/css/creatingSiteStructureController.css"/>
	<link rel="stylesheet" type="text/css" href="app/resources/css/input.css"/>

	<!-- 添加键盘事件 -->
	<script type="text/javascript" src="app/addKeyEvent.js"></script>
	
	<script type="text/javascript">
		var ReportUri = '<%= reportUri %>';
	</script>
	<script type="text/javascript" src="ext/ext-all.js"></script>
	<script type="text/javascript" src="ext/packages/ext-locale/build/ext-locale-zh_CN.js"></script>
	<script type="text/javascript" src="app/app.js"></script>
	<script type="text/javascript" src="app/AppUtil.js"></script>
	<!-- 临时使用 -->
	<script type="text/javascript" src="app/common.js"></script>
	<script type="text/javascript" src="app/select_area.js"></script>


</head>
<body></body>
</html>
