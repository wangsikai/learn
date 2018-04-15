package com.lanking.uxb.service.search.api;

import java.util.ArrayList;
import java.util.List;

import com.lanking.cloud.component.searcher.api.Document;

public class ScrollPage {
	private String scrollId;
	private long totalCount;

	public List<Document> documents = new ArrayList<Document>();

	public String getScrollId() {
		return scrollId;
	}

	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

}
