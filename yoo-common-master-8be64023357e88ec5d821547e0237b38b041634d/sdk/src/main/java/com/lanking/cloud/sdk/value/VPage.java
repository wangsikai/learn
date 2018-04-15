package com.lanking.cloud.sdk.value;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class VPage<T> implements Iterable<T>, Serializable {

	private static final long serialVersionUID = -1491617064364880924L;

	private List<T> items;
	/**
	 * 总记录数
	 */
	private long total;
	/**
	 * 每页数量
	 */
	private long pageSize;
	/**
	 * 当前页码
	 */
	private long currentPage;
	/**
	 * 总页数
	 */
	private long totalPage;

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

}
