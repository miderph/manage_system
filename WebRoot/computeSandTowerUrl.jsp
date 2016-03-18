<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%
        Object admin = request.getSession().getAttribute("user");
        if (admin == null) {
                response.sendRedirect(".");
                //pw.print("{ret:'请重新登录'}");
                return;
        }

String downloadurl = request.getParameter("downloadurl");
String playurl = request.getParameter("playurl");
String cdnDownloadUrl = "";
String cdnPlayUrl = "";

if (downloadurl == null){
   downloadurl = "";
}
else {
   cdnDownloadUrl = net.zhilink.tools.ApkTools.computeSandTowerDownloadUrl(downloadurl);
}
if (playurl == null){
   playurl = "";
}
else{
   cdnPlayUrl = net.zhilink.tools.ApkTools.computeSandTowerVideoPlayUrl(playurl);
}
logger.info("downloadurl="+ downloadurl);
logger.info("cdnDownloadUrl="+ cdnDownloadUrl);
logger.info("playurl="+ playurl);
logger.info("cdnPlayUrl="+ cdnPlayUrl);

%><%!
org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>boyile-Welcome to ZhilinkTV TestCase</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	
    <link rel="stylesheet" type="text/css" href="fontawesome/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="ext/packages/ext-theme-classic/build/resources/ext-theme-classic-all.css" />
    <link rel="stylesheet" type="text/css" href="app/resources/app.css" />
    
    <link rel="stylesheet" type="text/css" href="temp/css/creatingSiteStructureController.css"/>
	<link rel="stylesheet" type="text/css" href="temp/css/input.css"/>
</head>

<body>
	<p align="center">
		<font size="5" face="Arial Black" color="#008000">沙塔地址</font>
	</p>
	
	<form method="post">
		<p align="center">下载地址：<input type="text" name="downloadurl" size="70" value="<%=downloadurl%>"></p>
		<p align="center">播放地址：<input type="text" name="playurl" size="70" value="<%=playurl%>"></p>
		<p align="center"><input type="submit" value="提交"></p>
	</form>
	<p align="center">下载地址（3小时内有效）：<textarea rows="3" cols="50"><%=cdnDownloadUrl%></textarea></p>
	<p align="center">播放地址（24小时内有效）：<textarea rows="3" cols="50"><%=cdnPlayUrl%></textarea></p>
	<p align="center"></p>
</body>
</html>
