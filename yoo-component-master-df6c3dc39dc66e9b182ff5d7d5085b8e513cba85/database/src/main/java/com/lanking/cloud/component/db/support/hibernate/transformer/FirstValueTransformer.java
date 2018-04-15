package com.lanking.cloud.component.db.support.hibernate.transformer;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

@SuppressWarnings("rawtypes")
public class FirstValueTransformer implements ResultTransformer {
	private static final long serialVersionUID = -7840130701870509530L;
	public static final FirstValueTransformer INSTANCE = new FirstValueTransformer();

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		return tuple[0];
	}

	@Override
	public List transformList(List list) {
		return list;
	}
}
