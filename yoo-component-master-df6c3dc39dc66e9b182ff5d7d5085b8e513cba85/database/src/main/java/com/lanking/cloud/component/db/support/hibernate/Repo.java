package com.lanking.cloud.component.db.support.hibernate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.sdk.data.Params;

@SuppressWarnings("unchecked")
public interface Repo<E, ID> {

	E get(ID id);

	E load(ID id) throws EntityNotFoundException;

	boolean exists(ID id);

	List<E> getAll();

	Map<ID, E> mget(Collection<ID> ids);

	Map<ID, E> mget(ID... ids);

	List<E> mgetList(Collection<ID> ids);

	List<E> mgetList(ID... ids);

	Query<E, ID> find(String query, Object... params);

	Query<E, ID> find(String query, Params params);

	int execute(String query, Object... params);

	int execute(String query, Params params);

	E save(E entity) throws EntityException;

	List<E> save(Collection<E> entities) throws EntityException;

	void delete(E entity);

	void delete(Collection<E> entities);

	void deleteById(ID id) throws EntityNotFoundException;

	void deleteByIds(Iterable<ID> ids);

	void deleteByIds(ID... ids);

	void flush();

	void clear();

	void flushAndClear();

	void evict(E entity);
}
