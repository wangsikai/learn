package com.lanking.cloud.component.db.support.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.IntegerType;
import org.springframework.aop.support.AopUtils;

import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.sdk.bean.Assembler;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.ArrayUtils;
import com.lanking.cloud.sdk.util.Classes;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HibernateRepo<E, ID extends Serializable> implements Repo<E, ID> {

	final Class<E> entityClass;
	List<Assembler> assemblers = Collections.emptyList();
	List<ResultExtractor> resultExtractors = Collections.emptyList();
	SqlRenderer sqlRenderer;

	private ClassMetadata classMetadata;
	private String idName;

	private SessionFactory sessionFactory;

	public HibernateRepo(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public HibernateRepo() {
		entityClass = Classes.getGenericParameter0(getClass());
		if (!AopUtils.isAopProxy(this) && entityClass == null) {
			throw new IllegalArgumentException("Entity class not found");
		}
	}

	public void setAssemblers(List<Assembler> assemblers) {
		this.assemblers = assemblers;
	}

	public void setResultExtractors(List<ResultExtractor> resultExtractors) {
		this.resultExtractors = resultExtractors;
	}

	public void setSqlRenderer(SqlRenderer sqlRenderer) {
		this.sqlRenderer = sqlRenderer;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public E get(ID id) {
		return prepare((E) getSession().get(entityClass, id));
	}

	@Override
	public E load(ID id) throws EntityNotFoundException {
		return assertNotNull(get(id), id);
	}

	@Override
	public boolean exists(ID id) {
		return get(id) != null;
	}

	@Override
	public List<E> getAll() {
		if (getClassMetadata().getIdentifierType().getClass() == IntegerType.class) {
			return getSession().createCriteria(entityClass).list();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Map<ID, E> mget(Collection<ID> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}
		List<E> list = prepare(getSession().createCriteria(entityClass).add(Restrictions.in(getIdName(), ids)).list());
		Map<ID, E> map = new LinkedHashMap<ID, E>(list.size());
		for (E entity : list) {
			map.put(getId(entity), entity);
		}
		return map;
	}

	@Override
	public Map<ID, E> mget(ID... ids) {
		return mget(ArrayUtils.asList(ids));
	}

	@Override
	public List<E> mgetList(Collection<ID> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyList();
		}
		return prepare(getSession().createCriteria(entityClass).add(Restrictions.in(getIdName(), ids)).list());
	}

	@Override
	public List<E> mgetList(ID... ids) {
		return mgetList(ArrayUtils.asList(ids));
	}

	@Override
	public Query<E, ID> find(String query, Object... params) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		if (params != null) {
			for (int i = 0, len = params.length; i < len; i++) {
				map.put(i, params[i]);
			}
		}
		return new HibernateQuery<E, ID>(this, query, map, null);
	}

	@Override
	public Query<E, ID> find(String query, Params params) {
		return new HibernateQuery<E, ID>(this, query, null, params);
	}

	@Override
	public int execute(String query, Object... params) {
		return find(query, params).execute();
	}

	@Override
	public int execute(String query, Params params) {
		return find(query, params).execute();
	}

	@Override
	public E save(E entity) throws EntityException {
		beforeSave(entity);
		try {
			Session session = getSession();
			session.persist(entity);
			session.flush();
		} catch (Exception e) {
			throw new EntityException(entityClass, e);
		}
		return prepare(entity);
	}

	@Override
	public List<E> save(Collection<E> entities) throws EntityException {
		List<E> result = new ArrayList<E>(entities.size());
		for (E entity : entities) {
			result.add(save(entity));
		}
		return result;
	}

	@Override
	public void delete(E entity) {
		getSession().delete(entity);
	}

	@Override
	public void delete(Collection<E> entities) {
		for (E entity : entities) {
			delete(entity);
		}
	}

	@Override
	public void deleteById(ID id) throws EntityNotFoundException {
		delete(load(id));
	}

	@Override
	public void deleteByIds(Iterable<ID> ids) {
		for (ID id : ids) {
			E entity = get(id);
			if (entity != null) {
				delete(entity);
			}
		}
	}

	@Override
	public void deleteByIds(ID... ids) {
		deleteByIds(ArrayUtils.asList(ids));
	}

	final E assertNotNull(E entity, Object arg) {
		if (entity == null) {
			throw new EntityNotFoundException(entityClass, arg.toString());
		}
		return entity;
	}

	final E prepare(E entity) {
		if (entity instanceof Lifecycle) {
			((Lifecycle) entity).onLoad();
		}
		for (Assembler assembler : assemblers) {
			if (assembler.accept(entityClass, Collections.emptyMap())) {
				assembler.assemble(entity);
			}
		}
		return entity;
	}

	final List<E> prepare(List<E> entities) {
		if (entities.isEmpty()) {
			return entities;
		}
		if (entities.get(0) instanceof Lifecycle) {
			for (E entity : entities) {
				((Lifecycle) entity).onLoad();
			}
		}
		for (Assembler assembler : assemblers) {
			if (assembler.accept(entityClass, Collections.emptyMap())) {
				assembler.massemble(entities);
			}
		}
		return entities;
	}

	final SQLQuery createSQLQuery(String sql) {
		return getSession().createSQLQuery(sql);
	}

	final boolean haspersistentClass(Class clazz) {
		return sessionFactory.getClassMetadata(clazz) != null;
	}

	final ID getId(E entity) {
		return (ID) getClassMetadata().getIdentifier(entity, (SessionImplementor) getSession());
	}

	private E beforeSave(E entity) {
		if (entity instanceof Lifecycle) {
			((Lifecycle) entity).onSave();
		}
		return entity;
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	public void flushAndClear() {
		flush();
		clear();
	}

	public void evict(E entity) {
		getSession().evict(entity);
	}

	private ClassMetadata getClassMetadata() {
		if (classMetadata == null) {
			classMetadata = sessionFactory.getClassMetadata(entityClass);
		}
		return classMetadata;
	}

	private String getIdName() {
		if (idName == null) {
			idName = getClassMetadata().getIdentifierPropertyName();
		}
		return idName;
	}

}
