package com.lanking.cloud.component.db.support.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.sdk.bean.Assembler;
import com.lanking.cloud.sdk.util.ArrayUtils;

@SuppressWarnings("rawtypes")
public class HibernateRepoFactory implements RepoFactory {
	private List<ResultExtractor> resultExtractors;
	private SqlRenderer sqlRenderer;
	private SessionFactory sessionFactory;

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
	public <E, ID> Repo<E, ID> create(Class<E> entityClass, Assembler... assemblers) {
		return create(entityClass, ArrayUtils.asList(assemblers));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E, ID> Repo<E, ID> create(Class<E> entityClass, Collection<Assembler> assemblers) {
		HibernateRepo repo = new HibernateRepo(entityClass);
		repo.setSqlRenderer(sqlRenderer);
		repo.setResultExtractors(resultExtractors);
		repo.setSessionFactory(sessionFactory);
		repo.setAssemblers(Lists.newArrayList(assemblers));
		return repo;
	}
}
