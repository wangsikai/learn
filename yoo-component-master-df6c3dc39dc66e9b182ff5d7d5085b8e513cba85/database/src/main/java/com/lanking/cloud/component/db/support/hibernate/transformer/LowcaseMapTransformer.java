package com.lanking.cloud.component.db.support.hibernate.transformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LowcaseMapTransformer implements ResultTransformer {
	public static final LowcaseMapTransformer INSTANCE = new LowcaseMapTransformer();
	private static final long serialVersionUID = 2878994087258770751L;

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map result = new HashMap(tuple.length);
		for (int i = 0; i < tuple.length; i++) {
			String alias = aliases[i];
			if (alias != null) {
				result.put(alias.toLowerCase(), tuple[i]);
			}
		}
		return result;
	}

	@Override
	public List transformList(List list) {
		return list;
	}
}
