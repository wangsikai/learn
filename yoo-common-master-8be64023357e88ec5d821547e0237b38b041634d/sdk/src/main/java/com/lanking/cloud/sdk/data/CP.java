package com.lanking.cloud.sdk.data;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 */
public final class CP {
	public static final CursorPageable DEFAULT = first();

	public static <C> CursorPageable<C> first() {
		return cursor(null, CursorPageable.SIZE_DEFAULT);
	}

	public static <C> CursorPageable<C> cursor(C cursor, int size, boolean down) {
		return new CursorPageRequest<C>(cursor, size, down);
	}

	public static <C> CursorPageable<C> cursor(C cursor, int size) {
		return new CursorPageRequest<C>(cursor, size);
	}

	public static <C> CursorPageable<C> cursor(C cursor) {
		return new CursorPageRequest<C>(cursor);
	}

	public static <C, T> CursorPage<C, T> empty() {
		return new CursorPageImpl<C, T>(new CursorPageRequest<C>(null, CursorPageable.SIZE_DEFAULT),
				Collections.<T> emptyList(), 0l, null);
	}

	public static <C, T> CursorPage<C, T> empty(CursorPageable<C> cursorPageable) {
		return new CursorPageImpl<C, T>(cursorPageable, Collections.<T> emptyList(), 0l, null);
	}

	public static <C, T> CursorPage<C, T> page(CursorPageable<C> cursorPageable, List<T> items, Long total,
			C prevCursor, C nextCursor) {
		return new CursorPageImpl<C, T>(cursorPageable, items, total, prevCursor, nextCursor);
	}

	public static <C, T> CursorPage<C, T> page(CursorPageable<C> cursorPageable, List<T> items, Long total, C cursor) {
		return new CursorPageImpl<C, T>(cursorPageable, items, total, cursor);
	}

	public static <C, T> CursorPage<C, T> page(CursorPageable<C> cursorPageable, List<T> items, C cursor) {
		return new CursorPageImpl<C, T>(cursorPageable, items, cursor);
	}

	private CP() {
	}
}
