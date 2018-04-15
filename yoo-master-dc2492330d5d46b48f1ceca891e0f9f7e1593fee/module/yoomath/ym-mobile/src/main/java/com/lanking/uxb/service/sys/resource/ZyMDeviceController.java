package com.lanking.uxb.service.sys.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.session.form.DeviceForm;

/**
 * 设备注册接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月7日
 */
@RestController
@RequestMapping("zy/m/device")
public class ZyMDeviceController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private DeviceService deviceService;

	/**
	 * 注册设备
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param form
	 *            注册相关参数
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RequestMapping(value = { "register" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value register(DeviceForm form) {
		if (form == null || StringUtils.isBlank(form.getToken()) || form.getType() == null) {
			return new Value(new IllegalArgException());
		}
		form.setUserId(Security.getUserId());
		form.setProduct(Product.YOOMATH);
		try {
			deviceService.register(form);
		} catch (Exception e) {
			logger.warn("register device error:", e);
		}
		return new Value();
	}

	/**
	 * 取消注册设备
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param token
	 *            推送token
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = { "unregister" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value unregister(String token) {
		if (StringUtils.isBlank(token)) {
			return new Value(new IllegalArgException());
		}
		deviceService.unregister(token, Product.YOOMATH);
		return new Value();
	}
}
