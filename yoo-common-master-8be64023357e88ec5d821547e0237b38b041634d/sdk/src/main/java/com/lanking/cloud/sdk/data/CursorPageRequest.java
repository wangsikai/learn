package com.lanking.cloud.sdk.data;

import java.io.Serializable;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <C>
 */
public final class CursorPageRequest<C> implements CursorPageable<C>, Serializable {
	private static final long serialVersionUID = 4668590265538872363L;
	private final C cursor;
	private final int size;
	private final boolean down;

	public CursorPageRequest(C cursor, int size, boolean down) {
		this.cursor = cursor;
		this.size = size;
		this.down = down;
	}

	public CursorPageRequest(C cursor, int size) {
		this.cursor = cursor;
		this.size = size;
		this.down = true;
	}

	public CursorPageRequest(C cursor) {
		this.cursor = cursor;
		this.size = SIZE_DEFAULT;
		this.down = true;
	}

	@Override
	public C getCursor() {
		return cursor;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean isDown() {
		return down;
	}
}
