package com.lanking.cloud.sdk.data;

import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <C>
 * @param <T>
 */
public class CursorPageImpl<C, T> implements CursorPage<C, T> {
	private final CursorPageable<C> cursorPageable;
	private final List<T> items;
	private final Long total;
	private final C prevCursor;
	private final C nextCursor;

	public CursorPageImpl(CursorPageable<C> cursorPageable, List<T> items, Long total, C prevCursor, C nextCursor) {
		this.cursorPageable = cursorPageable;
		this.items = items;
		this.total = total;
		this.prevCursor = prevCursor;
		this.nextCursor = nextCursor;
	}

	public CursorPageImpl(CursorPageable<C> cursorPageable, List<T> items, Long total, C cursor) {
		this.cursorPageable = cursorPageable;
		this.items = items;
		this.total = total;
		this.prevCursor = null;
		this.nextCursor = cursor;
	}

	public CursorPageImpl(CursorPageable<C> cursorPageable, List<T> items, C cursor) {
		this.cursorPageable = cursorPageable;
		this.items = items;
		this.total = null;
		this.prevCursor = null;
		this.nextCursor = cursor;
	}

	@Override
	public C getPrevCursor() {
		return prevCursor;
	}

	@Override
	public C getNextCursor() {
		return nextCursor;
	}

	@Override
	public Long getTotal() {
		return total;
	}

	@Override
	public int getItemSize() {
		return items.size();
	}

	@Override
	public List<T> getItems() {
		return items;
	}

	@Override
	public T get(int index) {
		return items.get(index);
	}

	@Override
	public T getFirst() {
		return items.get(0);
	}

	@Override
	public T getLast() {
		return items.get(items.size() - 1);
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public boolean isNotEmpty() {
		return !isEmpty();
	}

	@Override
	public boolean hasPrev() {
		return !cursorPageable.isDown() && items.size() == getSize();
	}

	@Override
	public boolean hasNext() {
		return cursorPageable.isDown() && items.size() == getSize();
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public C getCursor() {
		return cursorPageable.getCursor();
	}

	@Override
	public int getSize() {
		return cursorPageable.getSize();
	}

	@Override
	public boolean isDown() {
		return cursorPageable.isDown();
	}

	@Override
	public CursorPageable<C> current() {
		return cursorPageable;
	}

	@Override
	public CursorPageable<C> next() {
		return CP.cursor(nextCursor, getSize(), isDown());
	}

	@Override
	public CursorPageable<C> previous() {
		return CP.cursor(prevCursor, getSize(), isDown());
	}
}
