package com.lanking.cloud.sdk.value;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class VCursorPage<T> implements Iterable<T>, Serializable {

	private static final long serialVersionUID = -1862533524686511994L;

	private long cursor;
	private long total;
	private List<T> items;

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	public long getCursor() {
		return cursor;
	}

	public void setCursor(long cursor) {
		this.cursor = cursor;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
