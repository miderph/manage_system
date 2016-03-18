package com.zhilink.tv.util;

public class ReadRelaObj extends Readable
{
   private RelaObj relaObj;
   public void setContent(Object obj)
   {
      this.relaObj = (RelaObj)obj;
   }
   public long getId()
   {
      return relaObj.getId();
   }
   public String getLandscapeUrl()
   {
      if ("".equals(relaObj.getLandscapeUrl()))
         return null;
      return relaObj.getLandscapeUrl();
   }
   public String getPortraitUrl()
   {
      if ("".equals(relaObj.getPortraitUrl()))
         return null;
      return relaObj.getPortraitUrl();
   }
   public String getIconUrl()
   {
      if ("".equals(relaObj.getIconUrl()))
         return null;
      return relaObj.getIconUrl();
   }
   public boolean isUsedPortraitUrl(){
	   return relaObj.getIs_url_used() == 0;
   }
   public boolean isUsedLandscapeUrl(){
	   return relaObj.getIs_url_used() == 1;
   }
   public boolean isUsedIconUrl(){
	   return relaObj.getIs_url_used() == 2;
   }
   public boolean isUsed4SquaresUrl(){
	   return relaObj.getIs_url_used() == 3;
   }
   
   public boolean isUsed6SquaresUrl(){
	   return relaObj.getIs_url_used() ==4;
   }
   
   public boolean isUsedOhterUrl(){
	   return relaObj.getIs_url_used()<0 || relaObj.getIs_url_used() > 4;
   }
}
