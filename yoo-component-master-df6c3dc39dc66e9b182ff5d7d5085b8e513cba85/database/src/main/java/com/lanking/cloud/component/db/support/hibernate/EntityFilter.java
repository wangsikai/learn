package com.lanking.cloud.component.db.support.hibernate;

import java.util.List;

public interface EntityFilter<E> {

	public abstract <S extends E> S prepare(S entity);

	public abstract List<E> prepare(List<E> entities);

	public abstract E beforeSave(E entity);

	public abstract E afterSave(E entity);

	public abstract E beforeDelete(E entity);

	public abstract E afterDelete(E entity);

}