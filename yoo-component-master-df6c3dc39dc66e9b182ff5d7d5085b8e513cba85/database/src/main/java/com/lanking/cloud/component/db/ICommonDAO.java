package com.lanking.cloud.component.db;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.ex.core.EntityNotFoundException;

/**
 * DAO 操作接口(此接口方法可能后面会下移到具体orm对应的接口中)
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月8日
 */
@SuppressWarnings("unchecked")
public interface ICommonDAO<E, ID> {

	E get(ID id);

	E load(ID id) throws EntityNotFoundException;

	boolean exists(ID id);

	List<E> getAll();

	Map<ID, E> mget(Collection<ID> ids);

	Map<ID, E> mget(ID... ids);

	List<E> mgetList(Collection<ID> ids);

	List<E> mgetList(ID... ids);

	E save(E entity) throws EntityException;

	List<E> save(Collection<E> entities) throws EntityException;

	void delete(E entity);

	void delete(Collection<E> entities);

	void deleteById(ID id) throws EntityNotFoundException;

	void deleteByIds(Iterable<ID> ids);

	void deleteByIds(ID... ids);

}