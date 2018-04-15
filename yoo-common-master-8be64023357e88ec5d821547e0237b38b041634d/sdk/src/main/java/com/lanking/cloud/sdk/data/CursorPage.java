package com.lanking.cloud.sdk.data;

import java.util.List;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <C>
 * @param <T>
 */
public interface CursorPage<C, T> extends Iterable<T>, CursorPageable<C> {

	String NEXT = "next";
	String PREV = "prev";
	String TOTAL = "_total";

	C getPrevCursor();

	C getNextCursor();

	Long getTotal();

	int getItemSize();

	List<T> getItems();

	T get(int index);

	T getFirst();

	T getLast();

	boolean isEmpty();

	boolean isNotEmpty();

	boolean hasPrev();

	boolean hasNext();

	CursorPageable<C> current();

	CursorPageable<C> next();

	CursorPageable<C> previous();
}
