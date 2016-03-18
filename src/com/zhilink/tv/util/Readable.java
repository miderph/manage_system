package com.zhilink.tv.util;

import java.util.List;

import org.apache.log4j.Logger;


public abstract class Readable {
	private static Logger logger = Logger.getLogger(Readable.class);

	public abstract void setContent(Object obj);

	public abstract long getId();

	public abstract String getLandscapeUrl();

	public abstract String getPortraitUrl();

	public abstract String getIconUrl();
	
	public abstract boolean isUsedPortraitUrl();
	public abstract boolean isUsedLandscapeUrl();
	public abstract boolean isUsedIconUrl();
	public abstract boolean isUsed4SquaresUrl();
	public abstract boolean isUsed6SquaresUrl();
	public abstract boolean isUsedOhterUrl();

	public static <T extends Readable, TT extends Object> String generateStyle(
			List<TT> aList, Class<T> ClassReader, String logPrefix) {
		List<T> contentList = new java.util.ArrayList<T>();
		for (Object obj : aList)
			try {
				T content = ClassReader.newInstance();
				content.setContent(obj);
				contentList.add(content);
			} catch (Exception E) {
				logger.error("message="+E.getMessage(), E);
			}
		int colCount = contentList.size() * 3;// contentList.size() / 2 +
												// (contentList.size() % 2 ==0 ?
												// 0 : 1);
		long[] positionRow1 = new long[colCount];
		long[] positionRow2 = new long[colCount];
		byte[] styleRow1 = new byte[colCount];
		byte[] styleRow2 = new byte[colCount];

		final int S_Icon = 1 << 0;// 小图
		final int S_Landscape = 1 << 1;// 横图
		final int S_Portrait = 1 << 2;// 竖图
		final int S_4_Squares = 1<<3; //四方图
		final int S_6_Squares = 1<<4; //六方图
		
		int style = 0;
		int pos = 0;
		while (contentList.size() > 0) {
			style = 0;
			if (positionRow1[pos] == 0)
				style |= S_Icon;
			if (pos < colCount - 1 && positionRow1[pos + 1] == 0)
				style |= S_Landscape;
			if (positionRow2[pos] == 0)
				style |= S_Portrait;
			if(pos < colCount-1 && positionRow1[pos]==0 && positionRow2[pos+1]==0 
					&& positionRow2[pos] ==0 && positionRow2[pos+1] ==0){
				style |= S_4_Squares;
			}
			if(pos < colCount-2 && positionRow1[pos] ==0 && positionRow1[pos+1]==0 && positionRow1[pos+2] ==0
					&& positionRow2[pos] ==0 && positionRow2[pos+1] ==0 && positionRow2[pos+2] == 0 ){
				style |= S_6_Squares;
			}
			
			
			if ((style & S_Icon) > 0) {
				for (T content : contentList) {
					if (content.isUsedLandscapeUrl()) {
						if ((style & S_Landscape) > 0) {
							positionRow1[pos] = content.getId();
							positionRow1[pos + 1] = content.getId();
							styleRow1[pos] = S_Landscape;
							// styleRow1[pos+1] = S_Landscape;
						}
					} else if (content.isUsedIconUrl() || content.isUsedOhterUrl() ) {
						positionRow1[pos] = content.getId();
						styleRow1[pos] = S_Icon;
					} else if (content.isUsedPortraitUrl()) {
						if ((style & S_Portrait) > 0) {
							positionRow1[pos] = content.getId();
							positionRow2[pos] = content.getId();
							styleRow1[pos] = S_Portrait;
							// styleRow2[pos] = S_Portrait;
						}
					}else if(content.isUsed4SquaresUrl()){
						if((style & S_4_Squares) > 0){
							positionRow1[pos] = content.getId();
							positionRow1[pos+1] = content.getId();
							positionRow2[pos] = content.getId();
							positionRow2[pos+1] = content.getId();
							styleRow1[pos] = S_4_Squares;
						}
					}else if(content.isUsed6SquaresUrl()){
						if((style & S_6_Squares ) > 0){
							positionRow1[pos] = content.getId();
							positionRow1[pos+1] = content.getId();
							positionRow1[pos+2] = content.getId();
							
							positionRow2[pos] = content.getId();
							positionRow2[pos+1] = content.getId();
							positionRow2[pos+2] = content.getId();
							
							styleRow1[pos] = S_6_Squares;
						}
					}

					if (positionRow1[pos] > 0) {
						contentList.remove(content);
						break;
					}
				}// end of: for (T content : contentList)
			}
			style = 0;
			if (positionRow2[pos] == 0)
				style |= S_Icon;
			if (pos < colCount - 1 && positionRow2[pos + 1] == 0)
				style |= S_Landscape;

			if ((style & S_Icon) > 0) {
				for (T content : contentList) {
					if (content.isUsedLandscapeUrl()) {
						if ((style & S_Landscape) > 0) {
							positionRow2[pos] = content.getId();
							positionRow2[pos + 1] = content.getId();
							styleRow2[pos] = S_Landscape;
							// styleRow2[pos+1] = S_Landscape;
						}
					} else if (content.isUsedIconUrl() || content.isUsedOhterUrl()) {
						positionRow2[pos] = content.getId();
						styleRow2[pos] = S_Icon;
					}

					if (positionRow2[pos] > 0) {
						contentList.remove(content);
						break;
					}
				}// end of: for (T content : contentList)
			}
			pos++;
		}// end of: while (contentList.size() > 0)
		final String RowModel = ",{\"rowspan\":@rowspan@,\"colspan\":@colspan@,\"id\":\"@contentid@\"}";
		final String CellModel = "{\"rows\":@rows@,\"columns\":@cols@,\"cells\":[@cells@]}";
		final String CellBlankModel = ",{\"rowspan\":@rowspan@,\"colspan\":@colspan@,\"id\":\"0\"}";
		String strRow1 = "";
		String strRow2 = "";
		String strDebugRow1 = "";
		String strDebugRow2 = "";
		int retColCount = 0;
		boolean isHasBlank = false;
		for (int ii = 0; ii < colCount; ii++) {
			if (positionRow1[ii] == 0 && positionRow2[ii] == 0) {
				if (!isHasBlank)
	            {
	               isHasBlank = true;
	            }
				break;
			}
			retColCount++;

			int styleRow = 1;
			int styleCol = 1;
			if (styleRow1[ii] > 0) {
				if ((styleRow1[ii] & S_6_Squares) > 0){
					styleCol = 3;
				}else if((styleRow1[ii] & S_4_Squares) > 0 ||  (styleRow1[ii] & S_Landscape)>0 ){
					styleCol = 2;
				}else{
					styleCol = 1 ;
				}
				
				styleRow = ((styleRow1[ii] & S_6_Squares )>0 || 
							(styleRow1[ii] & S_4_Squares )>0 ||
							(styleRow1[ii] & S_Portrait) > 0)  ? 2 : 1;

				strRow1 += RowModel.replace("@rowspan@", "" + styleRow)
						.replace("@colspan@", "" + styleCol).replace("@contentid@", "" + positionRow1[ii]);
				strDebugRow1 += (styleRow1[ii] & S_Landscape) > 0 ? "-": (styleRow1[ii] & S_Portrait) > 0 ? "|" : "0";
			} else {
				if (!isHasBlank && positionRow1[ii] == 0)
	            {
	               isHasBlank = true;
	               strRow1 += CellBlankModel.replace("@rowspan@", ""+1).replace("@colspan@", ""+1);;
	               strDebugRow1 += "x";
	            }
	            else{
	            	strDebugRow1 += " ";
	            }
			}

			if (styleRow2[ii] > 0) {
				styleCol = (styleRow2[ii] & S_Landscape) > 0 ? 2 : 1;
				strRow2 += RowModel.replace("@rowspan@", "1").replace("@colspan@", "" + styleCol)
							.replace("@contentid@","" + positionRow2[ii]);
				strDebugRow2 += (styleRow2[ii] & S_Landscape) > 0 ? "-" : "0";
			} else {
				if (!isHasBlank && positionRow2[ii] == 0)
	            {
	               isHasBlank = true;
	               if (ii < colCount-1 && (positionRow2[ii+1] == 0 && positionRow1[ii+1] != 0))
	               {
	                  strRow2 += CellBlankModel.replace("@rowspan@", ""+1).replace("@colspan@", ""+2);
	                  strDebugRow2 += "xx";
	               }
	               else
	               {
	                  strRow2 += CellBlankModel.replace("@rowspan@", ""+1).replace("@colspan@", ""+1);                  
	                  strDebugRow2 += "x";
	               }
	            }
	            else{
					strDebugRow2 += " ";
	            }
			}

		}
		strRow1 = strRow1.length() > 0 ? strRow1.substring(1) : "";
		strRow2 = strRow2.length() > 0 ? strRow2.substring(1) : "";
		logger.info(logPrefix + "Row1:" + strDebugRow1 + "<");
		logger.info(logPrefix + "Row2:" + strDebugRow2 + "<");
		logger.info(logPrefix + "strRow1:" + strRow1);
		logger.info(logPrefix + "strRow2:" + strRow2);

		String strRet = CellModel.replace("@rows@", "2").replace("@cols@",
				"" + retColCount).replace("@cells@",
				"" + strRow1 + "," + strRow2);
		{//test genarate table
			String strTable = "<table><tr><td bgcolor='#0000ff'><table cellpadding='0' cellspacing='2' border='0'><tr>{tr0}</tr><tr>{tr1}</tr><tr>{tr2}</tr></table></td></tr></table>";
			String strRow_0 = ""; 
			for (int ii = 0; ii < retColCount; ii++){
				strRow_0 += "<td width='{colspan1x1}'></td>";
			}
			String strRow_1 = strRow1.replace("},{","}{").replaceAll("\\{\"rowspan\":(.*?),\"colspan\":(.*?),\"id\":\"(.*?)\"\\}?", "<td rowspan='$1' colspan='$2' bgcolor='#FFFFFF'><img height='{rowspan$1}' width='{colspan$2x$1}' src='{$3}' title='{title$3}'/></td>");
			String strRow_2 = strRow2.replace("},{","}{").replaceAll("\\{\"rowspan\":(.*?),\"colspan\":(.*?),\"id\":\"(.*?)\"\\}?", "<td rowspan='$1' colspan='$2' bgcolor='#FFFFFF'><img height='{rowspan$1}' width='{colspan$2x$1}' src='{$3}' title='{title$3}'/></td>");
			strTable = strTable.replace("{tr0}", strRow_0).replace("{tr1}", strRow_1).replace("{tr2}", strRow_2);
			logger.info(logPrefix + "strTable:" + strTable);
			strRet = strTable;
		}
		
		return strRet;
	}
}
