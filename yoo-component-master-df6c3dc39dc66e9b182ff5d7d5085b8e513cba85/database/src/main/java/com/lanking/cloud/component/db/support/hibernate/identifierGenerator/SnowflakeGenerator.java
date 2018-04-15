package com.lanking.cloud.component.db.support.hibernate.identifierGenerator;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnowflakeGenerator implements IdentifierGenerator {
	public static final String TYPE = "com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator";

	@Override
	public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
		return SnowflakeUUID.next();
	}
}
