package com.onewave.common.dao;

import java.util.List;

public class Pagination<T> {
	//当前页
	private long pageNum=1;
	//每页条数
	private long pageSize=25;
	//总页数
	private long pageTotal=0;
	//总条数
	private long totalSize=0;
	private List<T> list;
	public long getPageNum() {
		return pageNum;
	}
	public void setPageNum(long pageNum) {
		this.pageNum = pageNum;
	}
	public long getPageSize() {
		return pageSize;
	}
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
	public long getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(long pageTotal) {
		this.pageTotal = pageTotal;
	}
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
		this.pageTotal =  (long)(totalSize/pageSize) + totalSize%pageSize == 0? 0 : 1;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	
	public boolean hasNext(){
		return pageNum < pageTotal;
	}
	
}
