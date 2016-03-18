package net.zhilink.tools;

import java.io.File;

import org.apache.log4j.Logger;

public class ImageUtils {
   private static Logger logger = Logger.getLogger(ImageUtils.class);
   
   public static String scaleImage(File file, int maxWidth){
      return scaleImage(file, new int[]{maxWidth})[0];
   }
   public static String[] scaleImage(File file, int[] maxWidths)
   {
      String[] imgNames = new String[maxWidths.length];
      try
      {
    	  String fileName = file.getAbsolutePath();
    	  String extName = fileName.substring(fileName.lastIndexOf("."));
         File srcFile = new File(fileName.replace(extName, "_ori"+extName));
         file.renameTo(srcFile);
         for (int ii =0; ii < maxWidths.length; ii++){
            int maxWidth = maxWidths[ii];
            String tempFileName = file.getAbsolutePath();
            if (ii > 0){
            	tempFileName = file.getAbsolutePath().replace(extName, "_"+maxWidth+extName);
            }
            
            String format = extName.substring(1);
            if ("bmp".equalsIgnoreCase(format)){
               format = "jpg";
               tempFileName =tempFileName.replaceAll(extName, ".jpg");
            }
            File destFile = new File(tempFileName);
            imgNames[ii] = destFile.getName();
            
            java.awt.Image imgSrc = javax.imageio.ImageIO.read(srcFile);
            int newWidth = imgSrc.getWidth(null);
            int newHeight = imgSrc.getHeight(null);
            
            //scale but not stretch 
            double ratio = 1;
            ratio = newWidth * 1.0 / maxWidth;
            newWidth = (int)(newWidth / ratio);
            newHeight = (int)(newHeight / ratio);
            
            java.awt.image.BufferedImage bfImg = new java.awt.image.BufferedImage(
                  maxWidth, newHeight, java.awt.image.BufferedImage.TYPE_INT_RGB);
            bfImg.getGraphics().drawImage(
                  imgSrc.getScaledInstance(maxWidth, newHeight,
                        java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            javax.imageio.ImageIO.write(bfImg, format, destFile);
         }
      }
      catch(Exception E)
      {
         logger.info("压缩图片失败，msg="+E.getMessage()+",file=" + file.getAbsolutePath());
         E.printStackTrace();
      }
      return imgNames;
   }
}
