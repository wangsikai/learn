package com.lanking.cloud.component.db.support.hibernate.transformer;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

@SuppressWarnings("rawtypes")
public class FirstStringTransformer implements ResultTransformer {
	private static final long serialVersionUID = 1631068627606957903L;

	public static final FirstStringTransformer INSTANCE = new FirstStringTransformer();

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return tuple[0] != null ? tuple[0].toString() : null;
	}

	@Override
	public List transformList(List list) {
		return list;
	}
}
