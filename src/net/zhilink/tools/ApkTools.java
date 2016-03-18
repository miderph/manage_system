package net.zhilink.tools;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.springframework.web.context.ContextLoader;

public class ApkTools {
   public static final SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
   //private static final String AAPT = "F:/tools/aapt.exe d badging";
   //private static final String AAPT = "/home/Android/Source/out/host/linux-x86/bin/aapt d badging";
   public static String AAPT;
   static{
	   if ("\\".equals(File.separator)){
		   AAPT = getWebRoot()+"getapkinfo.cmd";	   
	   }
	   else{
		   AAPT = getWebRoot()+"getapkinfo.sh";
	   }
   }
	
   public static String getWebRoot(){
	   return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
   }
   public static Map<String, String> getApkInfo(String apkName)
   {
      //{md5sum=191FA62C0847D20D99F55019994ADDD4, package_name=la.droid.qr, versionCode=412, file_size=3M, version=4.1.2}
      Map<String, String> ret = new HashMap<String, String>(3);
      try {
         File file = new File(apkName);
         String fileSize = file.length()>1024*1024 ? file.length()/1024/1024+"M" : file.length()/1024+"K"; 
         ret.put("file_size", fileSize);
         ret.put("md5sum", MD5sum.md5sum(file));
         Process proc = java.lang.Runtime.getRuntime().exec(AAPT + " " + apkName);
         InputStream in = proc.getInputStream();
         java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(in));
         String packInfo = null;
         String str = null;
         while ((str = br.readLine()) != null)
         {
        	//System.out.println("str="+str);
            if (packInfo == null && str.startsWith("package"))
               packInfo = str ;
         }
         String[] arr1 = packInfo.replace("'", "").replace("\"", "").split(" ");
         for (String field : arr1){
            String[] value = field.split("=");
            if (value == null || value.length < 2)
               continue;
            if ("name".equals(value[0]))
               value[0] = "package_name";
            else if ("versionName".equals(value[0]))
               value[0] = "version";
            ret.put(value[0], value[1]);
         }
         
         in.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return ret;
   }
   public static String computeSandTowerDownloadUrl(String inPlainUrl){
		//沙塔cdn，应该通过判断url_type=500才认为是沙塔
//	   if (InitManager.usingSandTowerDownloadUrl && inPlainUrl != null && inPlainUrl.startsWith("http://cdn-files.duolebo.com"))
	   try {
		   URL url = new java.net.URL(inPlainUrl);
		   String http = url.getProtocol();
		   String host = url.getHost();
		   String port = url.getPort()>0 ? ":"+url.getPort() : "";
		   String path = url.getPath();path = path.substring(1);
		   String params = url.getQuery();
		   if (StringTool.isEmpty(params))
			   params = "";
		   String strtime = Long.toHexString(System.currentTimeMillis()/1000).toUpperCase();
		   String key = "duoleboapk";key = "eHj48EonpjmUoCM8";
		   String md5 = StringTool.toHex(MD5sum.md5sum((key+path+strtime).getBytes())).toLowerCase();
		   return http+"://"+host+port+"/"+md5+"/"+strtime+"/"+path+("".equals(params)?"":"?")+params;
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return inPlainUrl;
   }
   public static String computeSandTowerVideoPlayUrl(String inPlainUrl){
//	   if (InitManager.usingSandTowerVideoPlayUrl && inPlainUrl != null && inPlainUrl.startsWith("http://cdn.duolebo.com"))
	   try {
		   String key = "";
		   if (inPlainUrl != null){
			   if (inPlainUrl.startsWith("http://cdn01.duolebo.com")){
				   key = "y7jV2BGx5uJ29rBF";
			   }
			   else if (inPlainUrl.startsWith("http://cdn.duolebo.com")){
				   key = "EzgSnNPrv8s9uB7Q";
			   }
			   else{//直接返回
				   return inPlainUrl;
			   }
		   }
		   URL url = new java.net.URL(inPlainUrl);
		   String http = url.getProtocol();
		   String host = url.getHost();
		   String port = url.getPort()>0 ? ":"+url.getPort() : "";
		   String path = url.getPath();path = path.substring(1);
		   String params = url.getQuery();
		   if (StringTool.isEmpty(params))
			   params = "";
		   String strtime = sdf_yyyyMMddHHmmss.format(new java.util.Date(System.currentTimeMillis()-10*60*1000));//减少10分钟
		   String strUri = path+"?"+params+("".equals(params)?"":"&")+"timestamp="+strtime+key;
		   //System.out.println("strUri=" + strUri);

		   String md5 = StringTool.toHex(MD5sum.md5sum(strUri.getBytes())).toLowerCase();
		   return http+"://"+host+port+"/"+path+"?"+params+("".equals(params)?"":"&")+"timestamp="+strtime+"&encrypt="+md5;
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return inPlainUrl;
   }
   public static void main(String[] args) throws JSONException
   {
//      Map<String, String> info = getApkInfo("F:/android/apk/qrdroid412.apk");
//      System.out.println("" + info);
	  /* String info = computeSandTowerDownloadUrl("http://cdn-files.duolebo.com/test/YingYongDaoHang_Release_2.19.1_201503121056_byl.apk");
	   //String info = computeSandTowerUrl("http://cdn-files.duolebo.com/test/dlbtest.png");
	   info = computeSandTowerDownloadUrl("http://cdn-files.duolebo.com/test/com.wukongtv.wkhelper-44-1407138933502.apk");
      System.out.println("DownloadUrl=" + info);
	   info = computeSandTowerDownloadUrl("http://cdn-files.duolebo.com/test/com.quickbird.speedtest.apad-3-1404804040963.apk");
      System.out.println("DownloadUrl=" + info);
	   info = computeSandTowerDownloadUrl("http://cdn-files.duolebo.com/test/com.estrongs.android.pop-84-60d7cd9f89474d76b0c214b57bdf6547-D-2056.apk");
      System.out.println("DownloadUrl=" + info);
      
	  info = computeSandTowerVideoPlayUrl("http://cdn.duolebo.com/HLS_P30S140801D/3800/P30S140801D_3800.m3u8");//?a=aaaa&b=bbbb
      System.out.println("PlayUrl=" + info);
	  info = computeSandTowerVideoPlayUrl("http://cdn01.duolebo.com/HLS_yanagou_5000001236/2200/yanagou_5000001236_2200.m3u8");//?a=aaaa&b=bbbb
      System.out.println("PlayUrl=" + info);   */ 
      
   }
}
