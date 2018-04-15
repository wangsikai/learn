package com.lanking.cloud.sdk.data;

import java.util.List;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <T>
 */
public interface Page<T> extends Pageable, Iterable<T> {

	long getTotalCount();

	int getPageCount();

	int getItemSize();

	List<T> getItems();

	T getItem(int index);

	T getFirst();

	T getLast();

	boolean isEmpty();

	boolean isNotEmpty();

	boolean hasPrevious();

	boolean hasNext();

	boolean isFirst();

	boolean isLast();

	List<Integer> getShowIndexs();
}
