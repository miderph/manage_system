package com.onewave.common.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onewave.common.util.ZLUtil;

public class IUtil {
	
	public static String firstCapitalize(String str) {
		char c = str.charAt(0);
		
		if(c >= 'a' && c <= 'z') {
			c = (char) ((int)c -32);
			str = c + str.substring(1);
		}
		
		return str;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static IBean parserBean(Object object) {
		IBean iBean = new IBean();
		Class clazz = object.getClass();
		
		//获取数据库表的名称
		ITable aTable = (ITable) clazz.getAnnotation(ITable.class);
		if(aTable == null) {
			throw new Error("@ITable is null");
		} else {
			iBean.setTableName(aTable.name());
		}
		
		Map<String, Object> beanMap = iBean.getBeanMap();
		Map<String, Integer> fieldTypes = iBean.getFieldTypes();
		Map<String, Boolean> fieldNotNull = iBean.getFieldNotNull();
		List<String> notUpdateField = iBean.getNotUpdateField();
		List<String> notSaveField = iBean.getNotSaveField();
		//获取Bean中的所属性
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			
			field.setAccessible(true);
			
			String key = "";
			String name = "";
			boolean update = true;
			boolean save = true;
			boolean auto = false;
			
			
			IField iField = field.getAnnotation(IField.class);
			if(iField != null) {
				key = iField.title();
				update = iField.update();
				save = iField.save();
			}
			
			name = field.getName();
			if("".equals(key)) {
				key = name;
			}
			
			try {
				beanMap.put(key, field.get(object));
				fieldTypes.put(key, ZLUtil.toDBType(field.getGenericType()));
				
				if(iField != null) {
					fieldNotNull.put(key, iField.notNull());
				} else {
					fieldNotNull.put(key, false);
				}
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if(update == false) {
				notUpdateField.add(key);
			}
			
			if( save == false){
				notSaveField.add(key);
			}
			
			IId iId = field.getAnnotation(IId.class);
			if(iId != null) {
				auto = iId.auto();
				iBean.setAuto(auto);
				iBean.setIdField(key);
			}
		}
		
		return iBean;
	}
	
	public static IBean parserBean(Class clazz) {
		IBean iBean = null;
		try {
			Object object = clazz.newInstance();
			iBean = parserBean(object);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return iBean;
	}
	
	public static String parserBeanForTableName(Class clazz) {
		String tableName = "";
		try {
			Object object = clazz.newInstance();
			Class clazzEnTity = object.getClass();
			
			//获取数据库表的名称
			ITable aTable = (ITable) clazzEnTity.getAnnotation(ITable.class);
			if(aTable == null) {
				throw new Error("@ITable is null");
			} else {
				tableName = aTable.name();
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return tableName;
	}
	
	public static String parserBeanForIdField(Class clazz) {
		
		try {
			Object object = clazz.newInstance();
			Class clazzEnTity = object.getClass();
			
			Field[] fields = clazzEnTity.getDeclaredFields();
			for(Field field : fields) {
				IId iId = field.getAnnotation(IId.class);
				if(iId != null) {
					IField iField = field.getAnnotation(IField.class);
					String key = "";
					if(iField != null) {
						key = iField.title();
					}
					
					String name = field.getName();
					if("".equals(key)) {
						key = name;
					}
					
					return key;
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static int parserBeanForIdType(Class clazz) {
		
		try {
			Object object = clazz.newInstance();
			Class clazzEnTity = object.getClass();
			
			Field[] fields = clazzEnTity.getDeclaredFields();
			for(Field field : fields) {
				IId iId = field.getAnnotation(IId.class);
				if(iId != null) {
					return ZLUtil.toDBType(field.getGenericType());
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return Types.BIGINT;
	}
	
}
