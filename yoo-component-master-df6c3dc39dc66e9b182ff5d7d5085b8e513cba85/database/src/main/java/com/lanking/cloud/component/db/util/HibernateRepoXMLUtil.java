package com.lanking.cloud.component.db.util;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.HibernateRepoFactoryBean;

public class HibernateRepoXMLUtil {

	public static List<String> generateRepoBeanXMLConfig(SessionFactory sessionFactory) {
		List<String> beans = Lists.newArrayList();
		StringBuilder sb = new StringBuilder(300);
		for (ClassMetadata classMetadata : sessionFactory.getAllClassMetadata().values()) {
			String className = classMetadata.getMappedClass().getName();
			sb.append("<bean class=\"" + HibernateRepoFactoryBean.class.getName() + "\">");
			sb.append("<property name=\"entityClass\" value=\"" + className + "\"/>");
			sb.append("<property name=\"repoFactory\" ref=\"repoFactory\"/>");
			String[] classNamePackages = className.split("\\.");
			String entityName = classNamePackages[classNamePackages.length - 1];
			sb.append("<qualifier value=\"" + entityName + "Repo\"/>");
			sb.append("</bean>");
			beans.add(sb.toString());
			sb.setLength(0);
		}
		return beans;
	}
}
