package com.lanking.cloud.component.db.support.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.transformer.FirstStringTransformer;
import com.lanking.cloud.component.db.support.hibernate.transformer.FirstValueTransformer;
import com.lanking.cloud.component.db.support.hibernate.transformer.LowcaseMapTransformer;
import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.sdk.bean.Assembler;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Pageable;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class HibernateQuery<E, ID extends Serializable> implements Query<E, ID> {
	private static final Pattern KEY_PATTERN = Pattern.compile("^[\\w]+$");
	private static final Pattern QL_PATTERN = Pattern.compile("select(.+?)(from.+?)", Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE);
	private static final Pattern ORDERBY_PATTERN_1 = Pattern.compile("order\\s+by.+?\\)", Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE);
	private static final Pattern ORDERBY_PATTERN_2 = Pattern.compile("order\\s+by.+$", Pattern.DOTALL
			| Pattern.CASE_INSENSITIVE);
	private static final Set PRIMITIVES = Sets.newHashSet(Boolean.class, Short.class, Integer.class, Long.class,
			Float.class, Double.class, Number.class);
	public static final int MAX_PAGE_SIZE = 100;

	private final HibernateRepo<E, ID> hr;
	private final String key;
	private final Map<Integer, Object> listArgs;
	private final Map<String, Object> mapArgs;
	private final Map<String, Object> hints = new HashMap<String, Object>();

	private String sql;

	protected HibernateQuery(HibernateRepo<E, ID> hr, String key, Map<Integer, Object> listArgs,
			Map<String, Object> mapArgs) {
		this.hr = hr;
		this.key = key;
		this.listArgs = listArgs == null ? new HashMap<Integer, Object>() : listArgs;
		this.mapArgs = mapArgs == null ? new HashMap<String, Object>() : mapArgs;
	}

	@Override
	public Query<E, ID> set(String name, Object value) {
		mapArgs.put(name, value);
		return this;
	}

	@Override
	public Query<E, ID> set(int position, Object value) {
		listArgs.put(position, value);
		return this;
	}

	@Override
	public Query<E, ID> hint(String name, Object value) {
		hints.put(name, value);
		return this;
	}

	@Override
	public Query<E, ID> hint(String name) {
		hints.put(name, Boolean.TRUE);
		return this;
	}

	@Override
	public int execute() {
		return createQuery().executeUpdate();
	}

	@Override
	public E get() {
		SQLQuery query = createQuery().addEntity(hr.entityClass);
		return prepare((E) query.uniqueResult());
	}

	@Override
	public <T> T get(Class<T> clazz) {
		Object value = createQuery(clazz).uniqueResult();
		if (!clazz.isInstance(value)) {
			value = convert(value, clazz);
		}
		return prepareCustom((T) value, clazz);
	}

	@Override
	public E load() throws EntityNotFoundException {
		E entity = get();
		if (entity == null) {
			throw new EntityNotFoundException(hr.entityClass, "");
		}
		return entity;
	}

	@Override
	public <T> T load(Class<T> clazz) throws EntityNotFoundException {
		T entity = get(clazz);
		if (entity == null) {
			throw new EntityNotFoundException(clazz, "");
		}
		return entity;
	}

	@Override
	public long count() {
		return get(Long.class);
	}

	@Override
	public List<E> list() {
		return queryAndPrepare(createQuery().addEntity(hr.entityClass));
	}

	@Override
	public <T> List<T> list(Class<T> clazz) {
		return queryAndPrepareCustom(createQuery(clazz), clazz);
	}

	@Override
	public Page<E> fetch(Pageable p) {
		if (p == null) {
			p = P.first();
		}
		int size = p.getSize();
		if (size == Pageable.SIZE_ALL) {
			return new PageImpl<E>(list());
		} else if (size == Pageable.SIZE_NONE) {
			return new PageImpl<E>(null, getTotalCount(), p);
		}
		SQLQuery query = createQuery().addEntity(hr.entityClass);
		query.setFirstResult(p.getOffset());
		if (size < 0) {
			query.setMaxResults(-size);
			return new PageImpl<E>(queryAndPrepare(query));
		}
		query.setMaxResults(size);
		List<E> items = queryAndPrepare(query);
		if (items.size() == size) {
			return new PageImpl<E>(items, getTotalCount(), p);
		}
		return new PageImpl<E>(items, p.getOffset() + items.size(), p);
	}

	@Override
	public <T> Page<T> fetch(Pageable p, Class<T> clazz) {
		if (p == null) {
			p = P.first();
		}
		int size = p.getSize();
		if (size == Pageable.SIZE_ALL) {
			return new PageImpl<T>(list(clazz));
		} else if (size == Pageable.SIZE_NONE) {
			return new PageImpl<T>(null, getTotalCount(), p);
		}
		SQLQuery query = createQuery(clazz);
		query.setFirstResult(p.getOffset());
		if (size < 0) {
			query.setMaxResults(-size);
			return new PageImpl<T>(queryAndPrepareCustom(query, clazz));
		}
		query.setMaxResults(size);
		List<T> items = queryAndPrepareCustom(query, clazz);
		if (items.size() == size) {
			return new PageImpl<T>(items, getTotalCount(), p);
		}
		return new PageImpl<T>(items, p.getOffset() + items.size(), p);
	}

	@Override
	public CursorPage<ID, E> fetch(CursorPageable<ID> cp) {
		if (cp == null) {
			cp = CP.first();
		} else if (cp.getSize() > CursorPageable.MAX_PAGE_SIZE) {
			throw new IllegalArgumentException("Page size must <" + CursorPageable.MAX_PAGE_SIZE);
		}
		if (cp.isDown()) {
			mapArgs.put(CursorPage.NEXT, cp.getCursor());
		} else {
			mapArgs.put(CursorPage.PREV, cp.getCursor());
		}
		SQLQuery query = createQuery().addEntity(hr.entityClass);
		int size = cp.getSize();
		query.setMaxResults(size == 0 ? CursorPageable.SIZE_DEFAULT : size);

		List<E> items = queryAndPrepare(query);
		if (items.isEmpty()) {
			return CP.empty(cp);
		}
		return CP.page(cp, items, null, hr.getId(items.get(0)), hr.getId(items.get(items.size() - 1)));
	}

	@Override
	public <C, T> CursorPage<C, T> fetch(CursorPageable<C> cp, Class<T> clazz, CursorGetter<C, T> cg) {
		if (cp == null) {
			cp = CP.first();
		} else if (cp.getSize() > CursorPageable.MAX_PAGE_SIZE) {
			throw new IllegalArgumentException("Page size must <" + CursorPageable.MAX_PAGE_SIZE);
		}
		if (cp.isDown()) {
			mapArgs.put(CursorPage.NEXT, cp.getCursor());
		} else {
			mapArgs.put(CursorPage.PREV, cp.getCursor());
		}
		SQLQuery query = createQuery(clazz);
		int size = cp.getSize();
		query.setMaxResults(size == 0 ? CursorPageable.SIZE_DEFAULT : size);

		List<T> items = queryAndPrepareCustom(query, clazz);
		if (items.isEmpty()) {
			return CP.empty(cp);
		}
		return CP.page(cp, items, null, cg.getCursor(items.get(0)), cg.getCursor(items.get(items.size() - 1)));
	}

	@Override
	public <T> CursorPage<T, T> fetch(CursorPageable<T> cp, Class<T> clazz) {
		if (cp == null) {
			cp = CP.first();
		} else if (cp.getSize() > CursorPageable.MAX_PAGE_SIZE) {
			throw new IllegalArgumentException("Page size must <" + CursorPageable.MAX_PAGE_SIZE);
		}
		if (cp.isDown()) {
			mapArgs.put(CursorPage.NEXT, cp.getCursor());
		} else {
			mapArgs.put(CursorPage.PREV, cp.getCursor());
		}
		SQLQuery query = createQuery(clazz);
		int size = cp.getSize();
		query.setMaxResults(size == 0 ? CursorPageable.SIZE_DEFAULT : size);

		List<T> items = queryAndPrepareCustom(query, clazz);
		if (items.isEmpty()) {
			return CP.empty(cp);
		}
		return CP.page(cp, items, null, items.get(0), items.get(items.size() - 1));
	}

	private SQLQuery createQuery() {
		SQLQuery query = hr.createSQLQuery(renderSql());
		applyListParams(query, listArgs);
		addMapParams(query, mapArgs);
		return query;
	}

	private SQLQuery createQuery(Class targetClass) {
		SQLQuery query = createQuery();
		if (hr.haspersistentClass(targetClass)) {
			query.addEntity(targetClass);
		} else if (Map.class.isAssignableFrom(targetClass)) {
			query.setResultTransformer(LowcaseMapTransformer.INSTANCE);
		} else if (List.class.isAssignableFrom(targetClass)) {
			query.setResultTransformer(Transformers.TO_LIST);
		} else if (PRIMITIVES.contains(targetClass)) {
			query.setResultTransformer(FirstValueTransformer.INSTANCE);
		} else if (targetClass == String.class) {
			query.setResultTransformer(FirstStringTransformer.INSTANCE);
		} else {
			query.setResultTransformer(LowcaseMapTransformer.INSTANCE);
		}
		return query;
	}

	private long getTotalCount() {
		SQLQuery countQuery = hr.createSQLQuery(toCountQuery(renderSql()));
		applyListParams(countQuery, listArgs);
		addMapParams(countQuery, mapArgs);
		return Hibernates.getLong(countQuery.uniqueResult());
	}

	private List<E> queryAndPrepare(SQLQuery query) {
		return prepare(query.list());
	}

	private <T> List<T> queryAndPrepareCustom(SQLQuery query, Class<T> clazz) {
		List list = query.list();
		if (list.isEmpty()) {
			return list;
		}
		if (!clazz.isInstance(list.get(0))) {
			for (int i = 0, len = list.size(); i < len; i++) {
				list.set(i, convert(list.get(i), clazz));
			}
		}
		return prepareCustom(list, clazz);
	}

	private <T> Object convert(Object entity, Class<T> clazz) {
		if (entity == null) {
			return null;
		}
		if (clazz == String.class) {
			return entity.toString();
		} else if (Number.class.isAssignableFrom(clazz)) {
			Number num;
			if (entity instanceof Number) {
				num = (Number) entity;
			} else {
				num = NumberUtils.createNumber(entity.toString());
			}
			if (clazz == Long.class) {
				return num.longValue();
			} else if (clazz == Integer.class) {
				return num.intValue();
			} else if (clazz == Float.class) {
				return num.floatValue();
			} else if (clazz == Double.class) {
				return num.doubleValue();
			} else if (clazz == Byte.class) {
				return num.byteValue();
			} else if (clazz == Short.class) {
				return num.shortValue();
			}
		}
		throw new EntityException(clazz);
	}

	private E prepare(E entity) {
		return hr.prepare(entity);
	}

	private List<E> prepare(List<E> entities) {
		return hr.prepare(entities);
	}

	private <T> T prepareCustom(T entity, Class<T> clazz) {
		for (Assembler assembler : hr.assemblers) {
			if (assembler.accept(clazz, hints)) {
				assembler.assemble(entity);
			}
		}
		return entity;
	}

	private <T> List<T> prepareCustom(List<T> entities, Class<T> clazz) {
		for (Assembler assembler : hr.assemblers) {
			if (assembler.accept(clazz, hints)) {
				assembler.massemble(entities);
			}
		}
		return entities;
	}

	private static SQLQuery applyListParams(SQLQuery query, Map<Integer, Object> args) {
		if (!CollectionUtils.isEmpty(args)) {
			for (Map.Entry<Integer, Object> entry : args.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		return query;
	}

	private static SQLQuery addMapParams(SQLQuery query, Map<String, Object> args) {
		Set<String> nps = Sets.newHashSet(query.getNamedParameters());
		if (nps.size() > 0) {
			for (Map.Entry<String, Object> entry : args.entrySet()) {
				String key = entry.getKey();
				if (!nps.contains(key)) {
					continue;
				}
				Object arg = entry.getValue();
				if (arg == null) {
					query.setParameter(key, null);
				} else if (arg.getClass().isArray()) {
					query.setParameterList(key, (Object[]) entry.getValue());
				} else if (arg instanceof Collection) {
					query.setParameterList(key, ((Collection) arg));
				} else {
					query.setParameter(key, arg);
				}
			}
		}
		return query;
	}

	private String renderSql() {
		if (sql == null) {
			if (key.charAt(0) == '$') {
				sql = hr.sqlRenderer.render(hr.entityClass, key.substring(1), mapArgs);
			} else if (KEY_PATTERN.matcher(key).matches()) {
				sql = hr.sqlRenderer.render(hr.entityClass, key, mapArgs);
			} else {
				sql = key;
			}
		}
		return sql;
	}

	private static String toCountQuery(String qs) {
		int firstChar = qs.charAt(0);
		if (firstChar == 'f' || firstChar == 'F') {
			qs = ORDERBY_PATTERN_2.matcher("select count(*) " + qs).replaceAll("");
		} else {
			// qs = ORDERBY_PATTERN_1.matcher("select count(*) from (" + qs +
			// ") _ctmp").replaceAll(")");
			// order by 可能有函数，是带括号的
			qs = "select count(*) from (" + ORDERBY_PATTERN_2.matcher(qs).replaceAll("") + ") _ctmp";
		}
		return qs;
	}

	@Override
	public String toString() {
		return "Query [" + renderSql() + "]";
	}
}
