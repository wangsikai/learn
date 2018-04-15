package com.lanking.cloud.job.paperReport.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.job.paperReport.dao.ParameterServiceDAO;
import com.lanking.cloud.job.paperReport.service.ParameterService;

@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	private ParameterServiceDAO parameterServiceDAO;

	@Override
	public Parameter get(Product product, String key, String... args) {
		return parameterServiceDAO.get(product, key, args);
	}

	@Override
	public Map<String, Parameter> mget(Product product, Collection<String> keys) {
		return parameterServiceDAO.mget(product, keys);
	}

	@Override
	public Map<String, Parameter> mget(Product product, Collection<String> keys, Map<String, String[]> values) {
		return parameterServiceDAO.mget(product, keys, values);
	}

	@Override
	public List<String> mgetValueList(Product product, String key, List<String[]> argsList) {
		return parameterServiceDAO.mgetValueList(product, key, argsList);
	}

}
