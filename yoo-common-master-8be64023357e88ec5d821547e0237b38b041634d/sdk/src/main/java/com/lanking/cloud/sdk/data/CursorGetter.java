package com.lanking.cloud.sdk.data;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 * @param <C>
 * @param <T>
 */
public interface CursorGetter<C, T> {

	C getCursor(T bean);
}
