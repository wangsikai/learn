package com.lanking.cloud.component.db.support.hibernate;

import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.MySQL5InnoDBDialect;

public class MysqlDialect extends MySQL5InnoDBDialect {

	@Override
	public String getTypeName(int code, long length, int precision, int scale) throws HibernateException {
		switch (code) {
		case Types.INTEGER:
		case Types.BIGINT:
			if (precision < 4) {
				return "tinyint";
			} else if (precision < 6) {
				return "smallint";
			} else if (precision < 8) {
				return "mediumint";
			} else if (precision < 11) {
				return "int";
			} else if (precision < 21) {
				return "bigint";
			}
		}
		return super.getTypeName(code, length, precision, scale);
	}
}
