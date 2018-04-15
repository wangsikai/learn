package com.lanking.cloud.sdk.value;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class VScrollPage<T> implements Iterable<T>, Serializable {

	private static final long serialVersionUID = -1491617064364880924L;

	private List<T> items;
	private String scrollId;
	private long total;

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

	public String getScrollId() {
		return scrollId;
	}

	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}