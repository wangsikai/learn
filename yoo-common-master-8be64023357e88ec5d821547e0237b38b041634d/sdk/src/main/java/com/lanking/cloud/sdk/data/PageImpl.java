package com.lanking.cloud.sdk.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class PageImpl<T> implements Page<T>, Serializable {

	private static final long serialVersionUID = 1831642480676287957L;
	public static final Pageable NULL = new PageRequest(0, Pageable.SIZE_ALL);
	public static final int SHOW_PAGES = 12;
	private final Pageable pageable;
	private final long total;
	private final List<T> items;

	public PageImpl(List<T> items, long total, Pageable pageable) {
		this.items = items == null ? Collections.<T> emptyList() : items;
		this.total = total;
		this.pageable = pageable;
	}

	public PageImpl(List<T> items) {
		this(items, items == null ? 0 : items.size(), NULL);
	}

	public PageImpl() {
		this(Collections.<T> emptyList());
	}

	@Override
	public long getTotalCount() {
		return total;
	}

	@Override
	@JSONField(serialize = false)
	public int getPageCount() {
		if (getSize() < 0) {
			return 0;
		}
		int count = (int) (total / getSize());
		return total % getSize() != 0 ? count + 1 : count;
	}

	@Override
	@JSONField(serialize = false)
	public int getItemSize() {
		return items.size();
	}

	@Override
	public List<T> getItems() {
		return items;
	}

	@Override
	public T getItem(int index) {
		return items.get(index);
	}

	@Override
	@JSONField(serialize = false)
	public T getFirst() {
		return items.get(0);
	}

	@Override
	@JSONField(serialize = false)
	public T getLast() {
		return items.get(items.size() - 1);
	}

	@Override
	@JSONField(serialize = false)
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	@JSONField(serialize = false)
	public boolean isNotEmpty() {
		return !isEmpty();
	}

	@Override
	@JSONField(serialize = false)
	public boolean hasPrevious() {
		return !isFirst();
	}

	@Override
	@JSONField(serialize = false)
	public boolean hasNext() {
		return !isEmpty() && !isLast();
	}

	@Override
	@JSONField(serialize = false)
	public boolean isFirst() {
		return getIndex() == Pageable.INDEX_FIRST;
	}

	@Override
	@JSONField(serialize = false)
	public boolean isLast() {
		return getIndex() >= getPageCount();
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	@JSONField(serialize = false)
	public int getSize() {
		return pageable.getSize();
	}

	@Override
	@JSONField(serialize = false)
	public int getOffset() {
		return pageable.getOffset();
	}

	@Override
	@JSONField(serialize = false)
	public int getIndex() {
		return pageable.getIndex();
	}

	@Override
	@JSONField(serialize = false)
	public Collection<Order> getOrders() {
		return pageable.getOrders();
	}

	@Override
	public Pageable next() {
		return pageable.next();
	}

	@Override
	public Pageable previous() {
		return pageable.previous();
	}

	@Override
	public Pageable current() {
		return pageable;
	}

	@Override
	@JSONField(serialize = false)
	public List<Integer> getShowIndexs() {
		List<Integer> indexs = new ArrayList<Integer>();
		int index = getIndex();
		int total = getPageCount();
		int start, end;
		if (total <= SHOW_PAGES) {
			start = 1;
			end = total;
		} else {
			start = index + 1 - SHOW_PAGES / 3;
			end = index + 1 + SHOW_PAGES * 2 / 3;
			if (end > total) {
				end = total;
				start = total - SHOW_PAGES;
			} else if (start <= 0) {
				start = 1;
				end = SHOW_PAGES;
			}
		}
		for (int i = start; i <= end; i++) {
			indexs.add(i);
		}
		return indexs;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + pageable.hashCode();
		result = 31 * result + items.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Page)) {
			return false;
		}
		Page other = (Page) obj;
		return total == other.getTotalCount() && items.equals(other.getItems()) && pageable.equals(this);
	}

	@Override
	public String toString() {
		String contentType = "UNKNOWN";
		if (isNotEmpty()) {
			contentType = getFirst().getClass().getName();
		}
		return "Page " + getIndex() + "/" + getPageCount() + " containing [" + contentType + "] instances";
	}
}
