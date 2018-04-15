package com.lanking.cloud.component.db.support.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

public abstract class AbstractListType implements UserType {
	@Override
	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object deepCopy(Object arg0) throws HibernateException {
		List source = (List) arg0;
		List target = new ArrayList();
		if (source != null) {
			target.addAll(source);
		}
		return target;
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if (x != null && y != null) {
			List xList = (List) x;
			List yList = (List) y;
			if (xList.size() != yList.size())
				return false;

			for (int i = 0; i < xList.size(); i++) {
				if (!(xList.get(i).equals(yList.get(i))))
					return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return 0;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	/**
	 * 将对象属性放入到字段中
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void nullSafeSet(PreparedStatement st, Object arg1, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (arg1 != null) {
			List list = (List) arg1;
			StringBuilder sb = new StringBuilder();
			if (list.size() > 0) {
				for (int i = 0; i < list.size() - 1; i++) {
					sb.append(list.get(i).toString()).append(";");
				}
				sb.append(list.get(list.size() - 1).toString());
			}
			StringType.INSTANCE.nullSafeSet(st, sb.toString(), index, session);
		} else {
			StringType.INSTANCE.nullSafeSet(st, arg1, index, session);
		}
	}

	@Override
	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return List.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}
}