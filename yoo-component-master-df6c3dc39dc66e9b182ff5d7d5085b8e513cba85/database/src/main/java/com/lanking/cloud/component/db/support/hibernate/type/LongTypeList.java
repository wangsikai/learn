package com.lanking.cloud.component.db.support.hibernate.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;

import com.lanking.cloud.sdk.util.StringUtils;

public class LongTypeList extends AbstractListType {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object nullSafeGet(ResultSet rs, String[] name, SessionImplementor session, Object arg3)
			throws HibernateException, SQLException {
		String str = (String) StringType.INSTANCE.get(rs, name[0], session);
		List list = null;
		if (StringUtils.isNotBlank(str)) {
			list = new ArrayList();
			for (String one : str.split(";")) {
				if (StringUtils.isNotBlank(one)) {
					list.add(Long.parseLong(one));
				}
			}
		} else {
			list = new ArrayList(0);
		}
		return list;
	}
}
