package net.zhilink.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * @author xx
 */
public class UpdatePinyinTest {
	public static String driverclass = "oracle.jdbc.driver.OracleDriver";
	public static String url ="";// "jdbc:oracle:thin:@59.151.28.5:1521:zltvdb";
	public static String username = "zlinktv_qdgh";
	public static String password = "zlinktv_qdgh!";
	public static void updatePinyin(String id_name,String name_name,String table_name) {
		String sql = "select "+id_name+","+name_name+",pinyin from "+table_name;
		Statement stmt = null;
		Connection conn =null;
		Statement stmt2 = null;
		ResultSet rs = null;
		try {
			Class.forName(driverclass).newInstance(); // 加载驱动
			conn= DriverManager.getConnection(url, username,
					password); // 获得连接
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			rs = stmt.executeQuery(sql);
			String id="";
			String name ="";
			String pinyin ="";
			int i = 1;
			while (rs.next()) {
				id = rs.getString(id_name);
				name = rs.getString(name_name);
				if(!StringUtils.isEmpty(name)){
					pinyin = PinyinUtil.getHeadStringWithoutAnySymbol(name);
					sql ="update  "+table_name+" set pinyin='"+pinyin+"' where "+id_name+"= "+id;
					System.out.println(sql+ "   "+i);
					try{
						stmt2.executeUpdate(sql);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				i++;
				if(i%100==0){
					conn.commit();
				}
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(stmt2!=null){
				try {
					stmt2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(){
			@Override
			public void run() {
				updatePinyin("id", "name", "zl_cont");
			}
		}.start();
	}

}