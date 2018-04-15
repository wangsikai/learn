package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.ParameterService;

@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	@Qualifier("ParameterRepo")
	private Repo<Parameter, Long> parameterRepo;

	@Override
	public Parameter get(Product product, String key, String... args) {
		Params params = Params.param("key0", key);
		if (product == null) {
			product = Product.NULL;
		}

		params.put("product", product.getValue());
		Parameter parameter = parameterRepo.find("$findByKey", params).get();
		if (parameter != null) {
			String value = parameter.getValue();
			Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
			Matcher matcher = pattern.matcher(value);

			List<String> keys = Lists.newArrayList();
			while (matcher.find()) {
				keys.add(matcher.group(1));
			}

			Map<String, Parameter> parameterMap = mget(product, keys);
			for (Map.Entry<String, Parameter> parameterEntry : parameterMap.entrySet()) {
				value = value.replace("${" + parameterEntry.getKey() + "}", parameterEntry.getValue().getValue());
			}

			if (args.length > 0) {
				int index = 0;
				for (String v : args) {
					value = value.replace("[" + index + "]", v);
				}
			}

			parameter.setValue(value);
		}
		return parameter;
	}

	@Override
	public Map<String, Parameter> mget(Product product, Collection<String> keys, Map<String, String[]> values) {
		Params params = Params.param("keys", keys);
		if (product == null) {
			product = Product.NULL;
		}
		params.put("product", product.getValue());
		List<Parameter> parameters = parameterRepo.find("$findByKey", params).list();
		if (CollectionUtils.isEmpty(parameters) || parameters.size() != values.size()) {
			return Collections.EMPTY_MAP;
		}
		List<String> patternKeys = Lists.newArrayList();
		Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
		Map<String, List<String>> parameterKeys = new HashMap<String, List<String>>(parameters.size());
		for (Parameter p : parameters) {
			Matcher matcher = pattern.matcher(p.getValue());
			List<String> pKeys = Lists.newArrayList();
			while (matcher.find()) {
				String key = matcher.group(1);
				patternKeys.add(key);
				pKeys.add(key);
			}

			parameterKeys.put(p.getKey(), pKeys);
		}
		params.put("keys", patternKeys);
		List<Parameter> patternParameters = parameterRepo.find("$findByKey", params).list();
		Map<String, String> patternParameterMap = new HashMap<String, String>(patternParameters.size());
		for (Parameter p : patternParameters) {
			patternParameterMap.put(p.getKey(), p.getValue());
		}

		Map<String, Parameter> retMap = new HashMap<String, Parameter>(parameters.size());
		for (Parameter p : parameters) {
			String value = p.getValue();
			if (CollectionUtils.isNotEmpty(parameterKeys.get(p.getKey()))) {
				for (String k : parameterKeys.get(p.getKey())) {
					value = value.replace("${" + k + "}", patternParameterMap.get(k));
				}
			}

			if (values.get(p.getKey()) != null && values.get(p.getKey()).length > 0) {
				int index = 0;

				for (String v : values.get(p.getKey())) {
					value = value.replace("[" + index + "]", v);
					index++;
				}
			}

			p.setValue(value);

			retMap.put(p.getKey(), p);
		}

		return retMap;
	}

	@Override
	public Map<String, Parameter> mget(Product product, Collection<String> keys) {
		Params params = Params.param("keys", keys);
		if (product == null) {
			product = Product.NULL;
		}
		params.put("product", product.getValue());
		List<Parameter> parameters = parameterRepo.find("$findByKey", params).list();
		List<String> patternKeys = Lists.newArrayList();
		Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
		Map<String, List<String>> parameterKeys = new HashMap<String, List<String>>(parameters.size());
		for (Parameter p : parameters) {
			Matcher matcher = pattern.matcher(p.getValue());
			List<String> pKeys = Lists.newArrayList();
			while (matcher.find()) {
				String key = matcher.group(1);
				patternKeys.add(key);
				pKeys.add(key);
			}

			parameterKeys.put(p.getKey(), pKeys);
		}
		params.put("keys", patternKeys);
		List<Parameter> patternParameters = parameterRepo.find("$findByKey", params).list();
		Map<String, String> patternParameterMap = new HashMap<String, String>(patternParameters.size());
		for (Parameter p : patternParameters) {
			patternParameterMap.put(p.getKey(), p.getValue());
		}

		Map<String, Parameter> retMap = new HashMap<String, Parameter>(parameters.size());
		for (Parameter p : parameters) {
			String value = p.getValue();
			if (CollectionUtils.isNotEmpty(parameterKeys.get(p.getKey()))) {
				for (String k : parameterKeys.get(p.getKey())) {
					value = value.replace("${" + k + "}", patternParameterMap.get(k));
				}
			}

			p.setValue(value);

			retMap.put(p.getKey(), p);
		}

		return retMap;
	}

	@Override
	public List<String> mgetValueList(Product product, String key, List<String[]> argsList) {
		Params params = Params.param("key0", key);
		if (product == null) {
			product = Product.NULL;
		}

		params.put("product", product.getValue());
		Parameter parameter = parameterRepo.find("$findByKey", params).get();
		if (parameter != null) {
			List<String> values = new ArrayList<String>(argsList.size());

			Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
			String value = parameter.getValue();
			Matcher matcher = pattern.matcher(value);
			List<String> keys = Lists.newArrayList();
			while (matcher.find()) {
				keys.add(matcher.group(1));
			}

			Map<String, Parameter> parameterMap = mget(product, keys);
			for (Map.Entry<String, Parameter> parameterEntry : parameterMap.entrySet()) {
				value = value.replace("${" + parameterEntry.getKey() + "}", parameterEntry.getValue().getValue());
			}

			for (String[] args : argsList) {
				String newValue = value;
				if (args.length > 0) {
					int index = 0;
					for (String v : args) {
						newValue = value.replace("[" + index + "]", v);
					}
				}
				values.add(newValue);
			}
			return values;
		}
		return null;
	}
}
