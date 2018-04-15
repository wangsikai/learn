package com.lanking.cloud.component.db.support.hibernate;

import java.util.List;

import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月27日
 *
 * @param <E>
 * @param <ID>
 */
public interface Query<E, ID> {

	Query<E, ID> set(String name, Object value);

	Query<E, ID> set(int position, Object value);

	Query<E, ID> hint(String name, Object value);

	Query<E, ID> hint(String name);

	int execute();

	E get();

	<T> T get(Class<T> clazz);

	E load() throws EntityNotFoundException;

	<T> T load(Class<T> clazz) throws EntityNotFoundException;

	long count();

	List<E> list();

	<T> List<T> list(Class<T> clazz);

	Page<E> fetch(Pageable p);

	<T> Page<T> fetch(Pageable p, Class<T> clazz);

	CursorPage<ID, E> fetch(CursorPageable<ID> cp);

	<T> CursorPage<T, T> fetch(CursorPageable<T> cp, Class<T> clazz);

	<C, T> CursorPage<C, T> fetch(CursorPageable<C> cp, Class<T> clazz, CursorGetter<C, T> cg);
}
