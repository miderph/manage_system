package com.onewave.backstage.model;

import org.springframework.web.multipart.MultipartFile;

public class CatalogImgFileUploadBean {

	private MultipartFile imgFile;

	public MultipartFile getImgFile() {
		return imgFile;
	}

	public void setImgFile(MultipartFile imgFile) {
		this.imgFile = imgFile;
	}
	
}
