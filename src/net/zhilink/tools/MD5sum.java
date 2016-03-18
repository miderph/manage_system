package net.zhilink.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5sum {
   
	public static byte[] md5sum(byte[] src) throws Exception {
      MessageDigest alg = MessageDigest.getInstance("MD5");
      return alg.digest(src);
    }
   public static String md5sum(String fileName)
   {
      return md5sum(new File(fileName));
   }

   public static String md5sum(File file)
   {
//      net.zhilink.tools.MyLog.d("");
      String md5 = null;
      try {
         BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
         byte[] buf = new byte[10240];
         int iCount = 0;
         MessageDigest alg = MessageDigest.getInstance("MD5");
         while ((iCount=bis.read(buf))!=-1)
         {
            alg.update(buf, 0, iCount);
         }
         bis.close();
         byte[] ret = alg.digest();
         md5 = StringTool.toHex(ret);
      } catch (FileNotFoundException e) {
//         net.zhilink.tools.MyLog.w(e);
      } catch (NoSuchAlgorithmException e) {
//         net.zhilink.tools.MyLog.w(e);
      } catch (IOException e) {
//         net.zhilink.tools.MyLog.w(e);
      }
      finally
      {
//         net.zhilink.tools.MyLog.d("md5=" + md5);
      }
      return md5;
   }
}
