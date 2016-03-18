package com.onewave.backstage.enums;

import javax.xml.namespace.QName;

public enum QName_Enums {
	
	DATA(new QName("data")),
	CATALOG(new QName("catalog_id")),
	TYPE(new QName("type")),
	STATUS(new QName("status")),
	TITLE(new QName("title")),
	DESC(new QName("description")),
	ICON(new QName("icon_url")),
	AUTHOR(new QName("author")),
	IMGURL(new QName("img_url")),
	SINGER(new QName("singer")),
	DURATION(new QName("duration")),
	ALBUM(new QName("album")),
	TRYURL(new QName("try_url")),
	PLAYURL(new QName("play_url")),
	DOWNLOAD(new QName("download_url")),
	LRCURL(new QName("lrc_url")),
	RATING(new QName("rating")),
	MVLINK(new QName("mv_link")),
	MVURL(new QName("mv_url")),
	RPT(new QName("rbt")),
	IMG(new QName("img")),
	LINKURL(new QName("link_url")),
	ISCOMMENT(new QName("is_comment")),
	ISGRADE(new QName("is_grade")),
	SOURCEID(new QName("source_id"));
	
	
	private QName qname;
	private QName_Enums(QName qname) {
		this.qname = qname;
	}
	
	public String getLocalName() {
		return qname.getLocalPart();
	}
	/**
	 * @return the qname
	 */
	public QName getQname() {
		return qname;
	}
}
