package com.lanking.cloud.component.db.support.hibernate;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.lanking.cloud.sdk.bean.Assembler;

@SuppressWarnings("rawtypes")
public class HibernateRepoFactoryBean<E, ID> implements FactoryBean<Repo<E, ID>>, InitializingBean {
	private RepoFactory repoFactory;
	private Class<E> entityClass;
	private List<Assembler> assemblers = Collections.emptyList();

	private Repo<E, ID> repo;

	public void setRepoFactory(RepoFactory repoFactory) {
		this.repoFactory = repoFactory;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void setAssemblers(List<Assembler> assemblers) {
		this.assemblers = assemblers;
	}

	@Override
	public Repo<E, ID> getObject() throws Exception {
		return repo;
	}

	@Override
	public Class<?> getObjectType() {
		return Repo.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		repo = repoFactory.create(entityClass, assemblers);
	}
}
