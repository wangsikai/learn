package com.lanking.cloud.component.db.support.hibernate.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.ex.core.EntityNotFoundException;

@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDAO<E, ID> implements IHibernateDAO<E, ID> {

	public Repo<E, ID> repo;

	@Override
	public E get(ID id) {
		return repo.get(id);
	}

	@Override
	public E load(ID id) throws EntityNotFoundException {
		return repo.load(id);
	}

	@Override
	public boolean exists(ID id) {
		return repo.exists(id);
	}

	@Override
	public List<E> getAll() {
		return repo.getAll();
	}

	@Override
	public Map<ID, E> mget(Collection<ID> ids) {
		return repo.mget(ids);
	}

	@Override
	public Map<ID, E> mget(ID... ids) {
		return repo.mget(ids);
	}

	@Override
	public List<E> mgetList(Collection<ID> ids) {
		return repo.mgetList(ids);
	}

	@Override
	public List<E> mgetList(ID... ids) {
		return repo.mgetList(ids);
	}

	@Override
	public E save(E entity) throws EntityException {
		return repo.save(entity);
	}

	@Override
	public List<E> save(Collection<E> entities) throws EntityException {
		return repo.save(entities);
	}

	@Override
	public void delete(E entity) {
		repo.delete(entity);
	}

	@Override
	public void delete(Collection<E> entities) {
		repo.delete(entities);

	}

	@Override
	public void deleteById(ID id) throws EntityNotFoundException {
		repo.deleteById(id);
	}

	@Override
	public void deleteByIds(Iterable<ID> ids) {
		repo.deleteByIds(ids);
	}

	@Override
	public void deleteByIds(ID... ids) {
		repo.deleteByIds(ids);
	}

	public abstract void setRepo(Repo<E, ID> repo);

}
