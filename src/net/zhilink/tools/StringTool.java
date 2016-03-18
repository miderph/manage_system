package net.zhilink.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringTool {

	public static String null2Empty(String inStr) {
		return isEmpty(inStr) ? "" : inStr;
	}

	public static boolean isEmpty(String inStr) {
		return inStr == null || "".equals(inStr) || "null".equals(inStr);
	}

	/**
	 * Turns an array of bytes into a String representing each byte as an
	 * unsigned hex number.
	 * <p>
	 * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	 * Distributed under LGPL.
	 * 
	 * @param hash
	 *            an rray of bytes to convert to a hex-string
	 * @return generated hex string
	 */
	public static final String toHex(byte hash[]) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if (((int) hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) hash[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	public static final byte[] toByte(String strHex) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

		int posStart = 0;
		int posEnd = 0;
		while (posStart < strHex.length()) {
			posEnd = posStart + 2;
			String str = strHex.substring(posStart, posEnd);
			baos.write(Integer.parseInt(str, 16) & 0xff);
			posStart = posEnd;

		}

		return baos.toByteArray();
	}
	
	public static final String appendValue(String array, String value) {
		if (array == null)
			array = "";
		if (StringUtils.isEmpty(value))
			return array;
		List<String> idsList = new ArrayList<String>();
		String[] ids = array.split(",");
		for (String id : ids) {
			if (StringUtils.isEmpty(id))
				continue;
			idsList.add(id);
		}
		if (!idsList.contains(value)) {
			idsList.add(value);
		}
		return arrayToStr(quote(idsList));
	}

	public static final String removeValue(String array, String value) {
		if (array == null)
			array = "";
		if (StringUtils.isEmpty(value))
			return array;
		List<String> idsList = new ArrayList<String>();
		String[] ids = array.split(",");
		for (String id : ids) {
			if (StringUtils.isEmpty(id))
				continue;
			if (id.startsWith("'") && id.endsWith("'")) {
				id = id.substring(1, id.length() - 1);
			}
			if (!value.equals(id)) {
				idsList.add(id);
			}
		}
		return arrayToStr(quote(idsList));
	}

	public static final String listToStr(List<String> list) {
		if (list == null || list.size() == 0)
			return "";
		String[] array = new String[list.size()];
		return arrayToStr(list.toArray(array));
	}

	public static final String arrayToStr(String[] array) {
		String str = "";
		if (array == null || array.length == 0)
			return "";
		for (int i = 0; i < array.length; i++) {
			if (StringUtils.isEmpty(array[i]))
				continue;
			str += array[i];
			if (i < array.length - 1) {
				str += ",";
			}
		}
		return str;
	}
	public static final String quote(String values) {
		if(null == values ) return null;
		return arrayToStr(quote(values.split(",")));
	}
	
	public static final String[] quote(List<String> values) {
		if(null == values ) return null;
		return quote(values.toArray(new String[values.size()]));
	}
	public static final String[] quote(String[] values) {
		if(null == values ) return null;
		for (int i=0;i< values.length;i++) {
			String item = values[i];
			if (StringUtils.isEmpty(item))
				continue;
			if ( item.startsWith("'") && item.endsWith("'")) {
				values[i] = item ;
			}else{
				if(!item.startsWith("'"))
					item = "'" + item;
				if(!item.endsWith("'"))
					item += "'";
				values[i] = item;
			}
		}
		return values;
	}

	public static void main(String[] args) {
		String[] arr = { "a", "b", "c", "d" };
		System.out.println(arr);
	}
}
