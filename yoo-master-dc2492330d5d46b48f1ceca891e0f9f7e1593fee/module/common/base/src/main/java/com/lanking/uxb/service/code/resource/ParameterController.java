package com.lanking.uxb.service.code.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.convert.ParameterConvert;
import com.lanking.uxb.service.code.value.VParameter;

/**
 * 系统配置相关接口
 * 
 * @since 2.3.0
 * @author zemin.song
 * @version 2016年12月20日
 */
@RestController
@RequestMapping("common/parameter")
public class ParameterController {
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ParameterConvert parameterConvert;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "parameters", method = { RequestMethod.GET, RequestMethod.POST })
	public Value values(Product product, @RequestParam(value = "keys") List<String> keys) {
		Map<String, Parameter> map = parameterService.mget(product, keys);
		List<Parameter> list = new ArrayList<Parameter>(map.values());
		List<VParameter> vlist = null;
		if (CollectionUtils.isNotEmpty(map)) {
			list.removeAll(Collections.singleton(null));
			vlist = parameterConvert.to(list);
		} else {
			vlist = new ArrayList<VParameter>(keys.size());
		}
		for (String key : keys) {
			if (!map.containsKey(key)) {
				vlist.add(new VParameter(key));
			}
		}
		return new Value(vlist);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public Value values(Product product, @RequestParam(value = "key") String key) {
		Parameter parameter = parameterService.get(product, key);
		return new Value(parameter == null ? StringUtils.EMPTY : parameter.getValue());
	}
}
