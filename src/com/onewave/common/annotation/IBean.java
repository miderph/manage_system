package com.onewave.common.annotation;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBean {
	
	private String idField;
	
	private boolean auto;
	
	private String tableName;
	
	private Map<String, Object> beanMap;
	
	private Map<String, Integer> fieldTypes;
	
	private Map<String, Boolean> fieldNotNull;
	
	private List<String> notUpdateField;
	
	private List<String> notSaveField;
	
	public IBean() {
		tableName = "";
		idField = "";
		beanMap = new HashMap<String, Object>();
		fieldTypes = new HashMap<String, Integer>();
		fieldNotNull = new HashMap<String, Boolean>();
		notUpdateField = new ArrayList<String>();
		notSaveField = new ArrayList<String>();
		auto = false;
	}

	public Map<String, Object> getBeanMap() {
		return beanMap;
	}

	public void setBeanMap(Map<String, Object> beanMap) {
		this.beanMap = beanMap;
	}

	public List<String> getNotUpdateField() {
		return notUpdateField;
	}

	public void setNotUpdateField(List<String> notUpdateField) {
		this.notUpdateField = notUpdateField;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, Integer> getFieldTypes() {
		return fieldTypes;
	}

	public void setFieldTypes(Map<String, Integer> fieldTypes) {
		this.fieldTypes = fieldTypes;
	}

	public Map<String, Boolean> getFieldNotNull() {
		return fieldNotNull;
	}

	public void setFieldNotNull(Map<String, Boolean> fieldNotNull) {
		this.fieldNotNull = fieldNotNull;
	}

	public List<String> getNotSaveField() {
		return notSaveField;
	}

	public void setNotSaveField(List<String> notSaveField) {
		this.notSaveField = notSaveField;
	}

}
