package com.lanking.cloud.component.db.support.hibernate;

import java.util.Collection;

import com.lanking.cloud.sdk.bean.Assembler;

@SuppressWarnings("rawtypes")
public interface RepoFactory {

	<E, ID> Repo<E, ID> create(Class<E> entityClass, Assembler... assemblers);

	<E, ID> Repo<E, ID> create(Class<E> entityClass, Collection<Assembler> assemblers);
}
