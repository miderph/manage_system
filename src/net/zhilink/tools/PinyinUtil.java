package net.zhilink.tools;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	
	//多音字个数最大值
	private static final int MAX_SIZE = 5;

	/**
	 * 将字符串转换成拼音数组
	 * 
	 * @param src
	 * @return
	 */
	public static String[] stringToPinyin(String src) {
		return stringToPinyin(src, false, null);
	}

	/**
	 * 将字符串转换成拼音数组
	 * 
	 * @param src
	 * @return
	 */
	public static String[] stringToPinyin(String src, String separator) {
		return stringToPinyin(src, true, separator);
	}

	/**
	 * 将字符串转换成拼音数组
	 * 
	 * @param src
	 * @param isPolyphone
	 *            是否查出多音字的所有拼音
	 * @param separator
	 *            多音字拼音之间的分隔符
	 * @return
	 */
	public static String[] stringToPinyin(String src, boolean isPolyphone,
			String separator) {
		// 判断字符串是否为空
		if ("".equals(src) || null == src) {
			return null;
		}
		char[] srcChar = src.toCharArray();
		int srcCount = srcChar.length;
		String[] srcStr = new String[srcCount];

		for (int i = 0; i < srcCount; i++) {
			srcStr[i] = charToPinyin(srcChar[i], isPolyphone, separator);
		}
		return srcStr;
	}

	/**
	 * 将单个字符转换成拼音
	 * 
	 * @param src
	 * @return
	 */
	public static String charToPinyin(char src, boolean isPolyphone,
			String separator) {
		// 创建汉语拼音处理类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		StringBuffer tempPinying = new StringBuffer();

		// 如果是中文
		if (src > 128) {
			try {
				// 转换得出结果
				String[] strs = PinyinHelper.toHanyuPinyinStringArray(src,
						defaultFormat);

				// 是否查出多音字，默认是查出多音字的第一个字符
				if (isPolyphone && null != separator) {
					for (int i = 0; i < strs.length; i++) {
						tempPinying.append(strs[i]);
						if (strs.length != (i + 1)) {
							// 多音字之间用特殊符号间隔起来
							tempPinying.append(separator);
						}
					}
				} else {
					tempPinying.append(strs[0]);
				}

			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		} else {
			tempPinying.append(src);
		}

		return tempPinying.toString();

	}

	public static String hanziToPinyin(String hanzi) {
		return hanziToPinyin(hanzi, " ");
	}

	/**
	 * 将汉字转换成拼音
	 * 
	 * @param hanzi
	 * @param separator
	 * @return
	 */
	public static String hanziToPinyin(String hanzi, String separator) {
		// 创建汉语拼音处理类
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		// 输出设置，大小写，音标方式
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		String pinyingStr = "";
		try {
			pinyingStr = PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat,
					separator);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pinyingStr;
	}

	/**
	 * 将字符串数组转换成字符串
	 * 
	 * @param str
	 * @param separator
	 *            各个字符串之间的分隔符
	 * @return
	 */
	public static String stringArrayToString(String[] str, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) {
			sb.append(str[i]);
			if (str.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 简单的将各个字符数组之间连接起来
	 * 
	 * @param str
	 * @return
	 */
	public static String stringArrayToString(String[] str) {
		return stringArrayToString(str, "");
	}

	/**
	 * 将字符数组转换成字符串
	 * 
	 * @param str
	 * @param separator
	 *            各个字符串之间的分隔符
	 * @return
	 */
	public static String charArrayToString(char[] ch, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			sb.append(ch[i]);
			if (ch.length != (i + 1)) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符数组转换成字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String charArrayToString(char[] ch) {
		return charArrayToString(ch, " ");
	}

	/**
	 * 取汉字的首字母
	 * 
	 * @param src
	 * @param isCapital
	 *            是否是大写
	 * @return
	 */
	public static char[] getHeadByChar(char src, boolean isCapital) {
		// 如果不是汉字直接返回
		if (src <= 128) {
			return new char[] { src };
		}
		// 获取所有的拼音
		String[] pinyingStr = PinyinHelper.toHanyuPinyinStringArray(src);
		// 过滤中文符号
		if (pinyingStr == null) {
			return new char[] { src };
		}
		// 创建返回对象
		int polyphoneSize = pinyingStr.length;
		char[] headChars = new char[polyphoneSize];
		int i = 0;
		// 截取首字符
		for (String s : pinyingStr) {
			char headChar = s.charAt(0);
			// 首字母是否大写，默认是小写
			if (isCapital) {
				headChars[i] = Character.toUpperCase(headChar);
			} else {
				headChars[i] = headChar;
			}
			i++;
		}

		return headChars;
	}

	/**
	 * 取汉字的首字母(默认是大写)
	 * 
	 * @param src
	 * @return
	 */
	public static char[] getHeadByChar(char src) {
		return getHeadByChar(src, true);
	}

	/**
	 * 查找字符串首字母
	 * 
	 * @param src
	 * @return
	 */
	public static String[] getHeadByString(String src) {
		return getHeadByString(src, true);
	}

	public static String getHeadStringByString(String src) {
		return stringArrayToString(getHeadByString(src));
	}

	/**
	 * 查找字符串首字母
	 * 
	 * @param src
	 * @param isCapital
	 *            是否大写
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital) {
		return getHeadByString(src, isCapital, null);
	}

	/**
	 * 查找字符串首字母
	 * 
	 * @param src
	 * @param isCapital
	 *            是否大写
	 * @param separator
	 *            分隔符
	 * @return
	 */
	public static String[] getHeadByString(String src, boolean isCapital,
			String separator) {
		char[] chars = {};
		if (null != src) {
			chars = src.toCharArray();
		}
		String[] headString = new String[chars.length];
		int i = 0;
		for (char ch : chars) {

			char[] chs = getHeadByChar(ch, isCapital);
			StringBuffer sb = new StringBuffer();
			if (null != separator) {
				int j = 1;

				for (char ch1 : chs) {
					sb.append(ch1);
					if (j != chs.length) {
						sb.append(separator);
					}
					j++;
				}
			} else {
				sb.append(chs[0]);
			}
			headString[i] = sb.toString();
			i++;
		}
		return headString;
	}

	/**
	 * 获取拼音首字母 只需要字母和数字，其他符号一律去除
	 * 
	 * @param src
	 * @return
	 */
	public static String getHeadStringNoAnySymbol(String src) {
		String headString = getHeadStringByString(src);

		return headString.replaceAll("[^A-Za-z0-9", "");
	}
	
	public static String getHeadStringWithoutAnySymbol(String src){
		return converterToFirstSpell(src).toUpperCase();
	}

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（重当参:cdc,zds,cds,zdc）
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		if(StringUtils.isEmpty(chines)){
			return "";	
		}
		chines=chines.replaceAll("[^A-Za-z0-9\u4e00-\u9fa5]", "");
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		int duoyinzi_index =0;
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					// 取得当前汉字的所有全拼
					String[] strs = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat);
					if (strs != null) {
						for (int j = 0; j < strs.length; j++) {
							// 取首字母
							pinyinName.append(strs[j].charAt(0));
							if (j != strs.length - 1) {
								++duoyinzi_index;
								if(duoyinzi_index >=MAX_SIZE){
									break;
								}
								pinyinName.append(",");
							}
						}
					}
					// else {
					// pinyinName.append(nameChar[i]);
					// }
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
			pinyinName.append(" ");
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString())).toUpperCase();
	}

	/**
	 * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
	 * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
	 * ,chongdangshen,zhongdangshen,chongdangcan）
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToSpell(String chines) {
		StringBuffer pinyinName = new StringBuffer();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		int duoyinzi_index =0;
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					// 取得当前汉字的所有全拼
					String[] strs = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat);
					if (strs != null) {
						for (int j = 0; j < strs.length; j++) {
							pinyinName.append(strs[j]);
							//超过个数的多音字只取第一个拼音首字母
							if (j != strs.length - 1) {
								++duoyinzi_index;
								System.out.println(duoyinzi_index +" "+(duoyinzi_index >=MAX_SIZE) );
								if(duoyinzi_index >=MAX_SIZE){
									break;
								}
								pinyinName.append(",");
							}
						}
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
			pinyinName.append(" ");
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 去除多音字重复数据
	 * 
	 * @param theStr
	 * @return
	 */
	private static List<Map<String, Integer>> discountTheChinese(String theStr) {
		// 去除重复拼音后的拼音列表
		List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
		// 用于处理每个字的多音字，去掉重复
		Map<String, Integer> onlyOne = null;

		String[] firsts = theStr.split(" ");
		// 读出每个汉字的拼音
		for (String str : firsts) {
			onlyOne = new Hashtable<String, Integer>();
			String[] china = str.split(",");
			// 多音字处理
			for (String s : china) {
				Integer count = onlyOne.get(s);
				if (count == null) {
					onlyOne.put(s, new Integer(1));
				} else {
					onlyOne.remove(s);
					count++;
					onlyOne.put(s, count);
				}
			}
			mapList.add(onlyOne);
		}
		return mapList;
	}

	/**
	 * 解析并组合拼音，对象合并方案(推荐使用)
	 * 
	 * @return
	 */
	private static String parseTheChineseByObject(
			List<Map<String, Integer>> list) {
		Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
		// 遍历每一组集合
		for (int i = 0; i < list.size(); i++) {
			// 每一组集合与上一次组合的Map
			Map<String, Integer> temp = new Hashtable<String, Integer>();
			// 第一次循环，first为空
			if (first != null) {
				// 取出上次组合与此次集合的字符，并保存
				for (String s : first.keySet()) {
					for (String s1 : list.get(i).keySet()) {
						String str = s + s1;
						temp.put(str, 1);
					}
				}
				// 清理上一次组合数据
				if (temp != null && temp.size() > 0) {
					first.clear();
				}
			} else {
				for (String s : list.get(i).keySet()) {
					String str = s;
					temp.put(str, 1);
				}
			}
			// 保存组合数据以便下次循环使用
			if (temp != null && temp.size() > 0) {
				first = temp;
			}
		}
		String returnStr = "";
		if (first != null) {
			// 遍历取出组合字符串
			for (String str : first.keySet()) {
				returnStr += (str + ",");
			}
		}
		if (returnStr.length() > 0) {
			returnStr = returnStr.substring(0, returnStr.length() - 1);
		}
		return returnStr;
	}

	/**
	 * 解析并组合拼音，循环读取方案（不灵活，不推荐使用）
	 * 
	 * 现在有如下几个数组: {1,2,3} {4,5} {7,8,9} {5,2,8}
	 * 要求写出算法对以上数组进行数据组合,如:1475,1472,1478
	 * ,1485,1482....如此类推，得到的组合刚好是以上数组的最隹组合（不多不少）.
	 * 注：要求有序组合（并非象“全排列算法”那般得到的组合是无序的）：组合过程中，第一组数组排第一位、第二组排第二位、第三组排第三位....
	 * 
	 * @param list
	 * @return
	 */
	private static String parseTheChineseByFor(List<Map<String, Integer>> list) {
		StringBuffer sbf = new StringBuffer();
		int size = list.size();
		switch (size) {
		case 1:
			for (String s : list.get(0).keySet()) {
				String str = s;
				sbf.append(str + ",");
			}
			break;
		case 2:
			for (String s : list.get(0).keySet()) {
				for (String s1 : list.get(1).keySet()) {
					String str = s + s1;
					sbf.append(str + ",");
				}
			}
			break;
		case 3:
			for (String s : list.get(0).keySet()) {
				for (String s1 : list.get(1).keySet()) {
					for (String s2 : list.get(2).keySet()) {
						String str = s + s1 + s2;
						sbf.append(str + ",");
					}
				}
			}
			break;
		// 此处省略了数据组装过程，组装后的数据结构如下。
		// 注:List<Map<String, Integer>> list：List存的就是有多少组数据上面的是4组
		// Map就是具体的某一个数组（此处用Map主要是方便对其中数组中重复元素作处理）
		// StringBuffer sbf = new StringBuffer();--用于记录组合字符的缓冲器
		case 4:
			for (String s : list.get(0).keySet()) {
				for (String s1 : list.get(1).keySet()) {
					for (String s2 : list.get(2).keySet()) {
						for (String s3 : list.get(3).keySet()) {
							String str = s + s1 + s2 + s3;
							// 此处的sbf为StringBuffer
							sbf.append(str + ",");
						}
					}
				}
			}
			break;
		case 5:
			for (String s : list.get(0).keySet()) {
				for (String s1 : list.get(1).keySet()) {
					for (String s2 : list.get(2).keySet()) {
						for (String s3 : list.get(3).keySet()) {
							for (String s4 : list.get(4).keySet()) {
								String str = s + s1 + s2 + s3 + s4;
								sbf.append(str + ",");
							}
						}
					}
				}
			}
			break;
		case 6:
			for (String s : list.get(0).keySet()) {
				for (String s1 : list.get(1).keySet()) {
					for (String s2 : list.get(2).keySet()) {
						for (String s3 : list.get(3).keySet()) {
							for (String s4 : list.get(4).keySet()) {
								for (String s5 : list.get(5).keySet()) {
									String str = s + s1 + s2 + s3 + s4 + s5;
									sbf.append(str + ",");
								}
							}
						}
					}
				}
			}
			break;
		case 7:
			for (String s : list.get(0).keySet()) {
				for (String s1 : list.get(1).keySet()) {
					for (String s2 : list.get(2).keySet()) {
						for (String s3 : list.get(3).keySet()) {
							for (String s4 : list.get(4).keySet()) {
								for (String s5 : list.get(5).keySet()) {
									for (String s6 : list.get(6).keySet()) {
										String str = s + s1 + s2 + s3 + s4 + s5
												+ s6;
										sbf.append(str + ",");
									}
								}
							}
						}
					}
				}
			}
			break;
		}
		String str = sbf.toString();
		return str.substring(0, str.length() - 1);
	}

	public static void main(String[] args) {
		String str = "曾曾曾曾曾曾曾曾曾曾曾曾曾曾!!!!@@@###";
		// System.out.println(getHeadStringByString(str));
		// System.out.println(hanziToPinyin("大话西游",""));
//		System.out.println(getHeadStringByString(str));
		long start =System.currentTimeMillis();
		System.out.println(getHeadStringWithoutAnySymbol(str));
//		System.out.println(converterToFirstSpell(str));
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000.0+"s");
	}

}
