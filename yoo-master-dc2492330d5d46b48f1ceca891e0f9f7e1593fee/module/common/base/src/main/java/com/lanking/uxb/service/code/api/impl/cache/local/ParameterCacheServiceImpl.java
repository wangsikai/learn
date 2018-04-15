package com.lanking.uxb.service.code.api.impl.cache.local;

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
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.ParameterService;

/**
 * 集群环境下的缓存实现
 * 
 * @author zemin.song
 * @version 2016年12月9日
 */
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
@Service
@ConditionalOnExpression("${common.code.cache}")
public class ParameterCacheServiceImpl extends AbstractBaseDataHandle implements ParameterService {

	@Autowired
	@Qualifier("ParameterRepo")
	private Repo<Parameter, Long> parameterRepo;

	private Map<Product, Map<String, Parameter>> allMap = null;

	@Override
	public Parameter get(Product product, String key, String... values) {
		if (allMap == null) {
			reload();
		}
		if (product == null) {
			product = Product.NULL;
		}
		Map<String, Parameter> productMap = allMap.get(product);

		Parameter parameter = productMap.get(key);
		if (parameter != null) {
			String value = parameter.getValue();
			Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
			Matcher matcher = pattern.matcher(value);

			List<String> keys = Lists.newArrayList();
			while (matcher.find()) {
				keys.add(matcher.group(1));
			}
			if (CollectionUtils.isNotEmpty(keys)) {
				Map<String, Parameter> parameterMap = mget(product, keys);
				for (Map.Entry<String, Parameter> parameterEntry : parameterMap.entrySet()) {
					value = value.replace("${" + parameterEntry.getKey() + "}", parameterEntry.getValue().getValue());
				}

				// 通配参数替换之后再缓存起来
				parameter.setValue(value);
				productMap.put(key, parameter);
			}

			if (values.length > 0) {
				int index = 0;
				for (String v : values) {
					value = value.replace("[" + index + "]", v);
					index++;
				}

				Parameter retParameter = new Parameter();
				retParameter.setValue(value);
				retParameter.setId(parameter.getId());
				retParameter.setKey(parameter.getKey());
				retParameter.setNote(parameter.getNote());
				retParameter.setProduct(product);
				retParameter.setStatus(parameter.getStatus());

				return retParameter;
			}

		}
		return parameter;
	}

	@Override
	public Map<String, Parameter> mget(Product product, Collection<String> keys, Map<String, String[]> values) {
		if (allMap == null) {
			reload();
		}
		if (product == null) {
			product = Product.NULL;
		}
		Map<String, Parameter> productMap = allMap.get(product);
		if (productMap != null) {
			Map<String, Parameter> retMap = new HashMap<String, Parameter>(keys.size());
			for (String key : keys) {
				Parameter parameter = productMap.get(key);

				if (parameter != null) {
					String value = parameter.getValue();
					Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
					Matcher matcher = pattern.matcher(value);

					boolean has = false;
					while (matcher.find()) {
						String rk = matcher.group(0);
						String k = matcher.group(1);

						Parameter keyParam = this.get(Product.YOOMATH, k);
						value = value.replace(rk, keyParam.getValue());
						has = true;
					}

					if (has) {
						parameter.setValue(value);
						productMap.put(parameter.getKey(), parameter);
					}

					if (values.get(parameter.getKey()) != null && values.get(parameter.getKey()).length > 0) {
						int index = 0;
						for (String v : values.get(parameter.getKey())) {
							value = value.replace("[" + index + "]", v);
							index++;
						}

						Parameter p = new Parameter();
						p.setValue(value);
						p.setId(parameter.getId());
						p.setKey(parameter.getKey());
						p.setNote(parameter.getNote());
						p.setProduct(product);
						p.setStatus(parameter.getStatus());
						retMap.put(parameter.getKey(), p);
					} else {
						retMap.put(parameter.getKey(), parameter);
					}

				}
			}

			return retMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public Map<String, Parameter> mget(Product product, Collection<String> keys) {
		if (allMap == null) {
			reload();
		}
		if (product == null) {
			product = Product.NULL;
		}
		Map<String, Parameter> productMap = allMap.get(product);
		if (productMap != null) {
			Map<String, Parameter> retMap = new HashMap<String, Parameter>(keys.size());
			for (String key : keys) {
				Parameter parameter = productMap.get(key);

				if (parameter != null) {
					String value = parameter.getValue();
					Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
					Matcher matcher = pattern.matcher(value);

					boolean has = false;
					while (matcher.find()) {
						String rk = matcher.group(0);
						String k = matcher.group(1);

						Parameter keyParam = this.get(Product.YOOMATH, k);
						value = value.replace(rk, keyParam.getValue());
						has = true;
					}

					parameter.setValue(value);

					retMap.put(parameter.getKey(), parameter);

					if (has) {
						productMap.put(parameter.getKey(), parameter);
					}
				}
			}

			return retMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public List<String> mgetValueList(Product product, String key, List<String[]> argsList) {
		if (allMap == null) {
			reload();
		}
		if (product == null) {
			product = Product.NULL;
		}
		Map<String, Parameter> productMap = allMap.get(product);

		Parameter parameter = productMap.get(key);
		if (parameter != null) {
			List<String> values = new ArrayList<String>(argsList.size());

			String value = parameter.getValue();
			Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9.-]{1,})\\}");
			Matcher matcher = pattern.matcher(value);

			List<String> keys = Lists.newArrayList();
			while (matcher.find()) {
				keys.add(matcher.group(1));
			}
			if (CollectionUtils.isNotEmpty(keys)) {
				Map<String, Parameter> parameterMap = mget(product, keys);
				for (Map.Entry<String, Parameter> parameterEntry : parameterMap.entrySet()) {
					value = value.replace("${" + parameterEntry.getKey() + "}", parameterEntry.getValue().getValue());
				}

				// 通配参数替换之后再缓存起来
				parameter.setValue(value);
				productMap.put(key, parameter);
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

	@Override
	public BaseDataType getType() {
		return BaseDataType.PARAMETER;
	}

	@Override
	public void reload() {
		List<Parameter> tmpAllList = parameterRepo.find("$listAll").list();
		Map<Product, Map<String, Parameter>> tmpAllMap = Maps.newHashMap();
		for (Parameter parameter : tmpAllList) {
			if (parameter.getProduct() != null) {
				Map<String, Parameter> productMap = tmpAllMap.get(parameter.getProduct());
				if (productMap == null) {
					productMap = Maps.newHashMap();
				}
				productMap.put(parameter.getKey(), parameter);
				tmpAllMap.put(parameter.getProduct(), productMap);
			}
		}
		allMap = tmpAllMap;
	}

	@Override
	public long size() {
		return getObjectDeepSize(allMap);
	}

}
