package com.lanking.uxb.service.search.api;

import java.util.ArrayList;
import java.util.List;

import com.lanking.cloud.component.searcher.api.Document;

public class Page {
	private long totalCount;
	private int pageSize;
	private long totalPage;
	private List<Document> documents = new ArrayList<Document>();

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		if (totalPage == 0 && totalCount != 0) {
			setTotalPage(totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize + 1));
		}
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocument(List<Document> documents) {
		this.documents = documents;
	}
}
