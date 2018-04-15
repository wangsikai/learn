package com.lanking.cloud.sdk.data;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <C>
 */
public interface CursorPageable<C> {

	int SIZE_DEFAULT = 20;

	int MAX_PAGE_SIZE = 10000;

	C getCursor();

	int getSize();

	boolean isDown();
}
