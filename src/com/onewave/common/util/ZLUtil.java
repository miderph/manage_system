package com.onewave.common.util;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

public class ZLUtil {
   private static final String TAG = ZLUtil.class.getName();

	/*public static void createTable(SQLiteDatabase db, Class clazz) {
		IBean iBean = IUtil.parserBean(clazz);
		String sql = createTableSql(iBean);
		
		if(!"".equals(sql)) {
			db.execSQL(sql);
		}
	}
	
	public static String createTableSql(IBean iBean) {
		net.zhilink.tools.MyLog.i(TAG,"--------------------create table sql----------------------");
		
		String sql = "";
		Map<String, Object> beanMap = iBean.getBeanMap();
		Map<String, ZLEnums> fieldTypes = iBean.getFieldTypes();
		Map<String, Boolean> fieldNotNull = iBean.getFieldNotNull();
		
		if(beanMap.size() > 0) {
			String idField = iBean.getIdField();
			if("".equals(idField)) {
				throw new Error("table's id is null ro not exist");
			}
			
			String fieldSql = "";
			
			Object idFieldObj = null;
			if(!"".equals(idField)) {
				boolean auto = iBean.isAuto();
				ZLEnums idType = fieldTypes.get(idField);
				String tempField = createFieldStr(idField, idType.toString(), fieldNotNull.get(idField));
				if(auto == true && idType == ZLEnums.INTEGER) {
					fieldSql += tempField + " primary key autoincrement,";
				} else {
					fieldSql += tempField + " primary key,";
				}
				
				idFieldObj = beanMap.remove(idField);
			}
			
			Iterator<String> iterator = beanMap.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next();
				fieldSql += createFieldStr(key, fieldTypes.get(key).toString(), fieldNotNull.get(key)) + ",";
			}
			
			if(fieldSql.length() > 0) {
				fieldSql = fieldSql.substring(0, fieldSql.length()-1);
				String tableName = iBean.getTableName();
				sql += "create table if not exists "+tableName + "(";
				sql += fieldSql + ")";
			}
			
			if(!"".equals(idField)) {
				beanMap.put(idField, idFieldObj);
			}
			
		}
		
		net.zhilink.tools.MyLog.i(TAG,"--------------------table sql: " + sql);
		
		return sql;
	}
	
	public static String createFieldStr(String key, String type, boolean notNull) {
		String str = " " + key + " " + type;
		
		if(notNull) {
			str += " not null";
		}
		
		return str;
	}
	
	public static String createInsertSql(IBean ibean) {
		net.zhilink.tools.MyLog.i(TAG,"--------------------create insert sql----------------------");
		String sql = "";

		Map<String, Object> beanMap = ibean.getBeanMap();
		String idField = ibean.getIdField();
		Object idFieldObj = "";
		boolean auto = ibean.isAuto();
		boolean isRemoveId = !("".equals(idField)) && auto == true;
		if (isRemoveId) {
			idFieldObj = beanMap.remove(idField);
		}

		String tableName = ibean.getTableName();
		Iterator<String> iterator = beanMap.keySet().iterator();
		String key = "";
		String keySql = "(";
		String objSql = "(";

		while (iterator.hasNext()) {
			key = iterator.next();
			keySql += key + ",";
			objSql += "'" + beanMap.get(key) + "',";
		}

		if (keySql.length() > 1) {
			keySql = keySql.substring(0, keySql.length() - 1);
			objSql = objSql.substring(0, objSql.length() - 1);
		}

		keySql += ")";
		objSql += ")";

		sql = "insert into " + tableName + keySql + " values" + objSql;

		if (isRemoveId) {
			beanMap.put(idField, idFieldObj);
		}
		
		net.zhilink.tools.MyLog.i(TAG,"--------------------insert sql: " + sql);

		return sql;
	}*/
	
	public static Integer toDBType(Type type) {
		Integer eType = Types.VARCHAR;
		
		if("int".equalsIgnoreCase(type.toString())) {
			eType = Types.INTEGER;
		} else if("long".equalsIgnoreCase(type.toString())) {
			eType = Types.BIGINT;
		} else if("byte".equalsIgnoreCase(type.toString())) {
			eType = Types.TINYINT;
		} else if("short".equalsIgnoreCase(type.toString())) {
			eType = Types.SMALLINT;
		} else if("float".equalsIgnoreCase(type.toString())) {
			eType = Types.REAL;
		} else if("double".equalsIgnoreCase(type.toString())) {
			eType = Types.DOUBLE;
		} else if("boolean".equalsIgnoreCase(type.toString())) {
			eType = Types.BIT;
		} else if("class java.lang.String".equalsIgnoreCase(type.toString())) {
			eType = Types.VARCHAR;
		} else if("class java.util.Date".equalsIgnoreCase(type.toString())) {
			eType = Types.TIMESTAMP;
		}
		
		return eType;
	}
	
	/**
	 * 
	 * @Title: getPageFirstResult 
	 * @Description: 计算分页获取数据时游标的起始位置
	 *
	 * @param pageNum
	 * @param pageSize
	 * @return int
	 * @throws
	 */
	public static int getPageFirstResult(int pageNum, int pageSize) {
		return (pageNum - 1) * pageSize;
	}
	
}
