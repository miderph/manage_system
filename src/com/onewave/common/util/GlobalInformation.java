package com.onewave.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class GlobalInformation {

	protected static final Logger logger = Logger.getLogger(GlobalInformation.class);

	public static boolean isPrintBeanParams;

	public static void printBeanParams(Object obj, String msg) {

		if (!isPrintBeanParams) {
			return;
		}
		Class clazz = obj.getClass();

		logger.info("------------------------" + msg+ "------------------------" + clazz.getName());

		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();

		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (int i = 0; i < methods.length; i++) {
			methodMap.put(methods[i].getName(), methods[i]);
		}

		Method method = null;

		for (Field field : fields) {
			String name = field.getName();
			String key = name;
			char c = name.charAt(0);

			if (c >= 'a' && c <= 'z') {
				c = (char) ((int) c - 32);
				name = "get" + c + name.substring(1);
			}

			method = methodMap.get(name);

			if (method != null && method.getParameterTypes().length < 1)
				try {

					logger.info(key + " : " + method.invoke(obj, null));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static boolean getIsPrintBeanParams() {
		return isPrintBeanParams;
	}

	public void setIsPrintBeanParams(boolean isPrintBeanParams) {
		GlobalInformation.isPrintBeanParams = isPrintBeanParams;
	}

}
