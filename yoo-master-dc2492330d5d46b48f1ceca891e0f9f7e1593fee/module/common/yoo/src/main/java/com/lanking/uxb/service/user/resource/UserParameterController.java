package com.lanking.uxb.service.user.resource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserParameter;
import com.lanking.cloud.domain.yoo.user.UserParameterType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserParameterService;

@RestController
@RequestMapping("up")
public class UserParameterController {

	@Autowired
	private UserParameterService upService;

	@RequestMapping(value = "guide/get", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getGuide(Product product) {
		String version = Env.getString(product.name().toLowerCase() + ".version");
		UserParameter up = upService.findOne(product, version, UserParameterType.GUIDE, Security.getUserId());
		Map<String, String> guideMap = Maps.newHashMap();
		if (up == null) {
			guideMap.put("index", "0");
			guideMap.put("homework", "0");
			guideMap.put("clazz", "0");
		} else {
			guideMap.put("index", up.getP0() == null ? "0" : up.getP0());
			guideMap.put("homework", up.getP1() == null ? "0" : up.getP1());
			guideMap.put("clazz", up.getP2() == null ? "0" : up.getP2());
		}
		return new Value(guideMap);
	}

	@RequestMapping(value = "guide/put", method = { RequestMethod.GET, RequestMethod.POST })
	public Value putGuide(Product product, String index, String homework, String clazz) {
		String version = Env.getString(product.name().toLowerCase() + ".version");
		UserParameter up = upService.findOne(product, version, UserParameterType.GUIDE, Security.getUserId());
		if (up == null) {
			up = new UserParameter();
			up.setProduct(product);
			up.setVersion(version);
			up.setType(UserParameterType.GUIDE);
			up.setUserId(Security.getUserId());
		}
		if ("1".equals(index)) {
			up.setP0("1");
		}
		if ("1".equals(homework)) {
			up.setP1("1");
		}
		if ("1".equals(clazz)) {
			up.setP2("1");
		}
		upService.save(up);
		return new Value();
	}

	@RequestMapping(value = "put", method = { RequestMethod.GET, RequestMethod.POST })
	public Value put(Product product, String version, UserParameterType type, String p0) {
		UserParameter up = upService.findOne(product, version, type, Security.getUserId());
		if (up == null) {
			up = new UserParameter();
			up.setProduct(product);
			up.setVersion(version);
			up.setType(type);
			up.setUserId(Security.getUserId());
		}
		up.setP0(p0);
		upService.save(up);
		return new Value();
	}
}
