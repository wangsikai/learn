package com.lanking.uxb.service.nationalDayActivity01.resource;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

@RestController
@RequestMapping("zy/m/nda01/pvuv")
public class ZyMNationalDayActivity01PVUVController {

	@Autowired
	private MqSender mqSender;

	/**
	 * 记录页面访问
	 * 
	 * @param h5
	 *            页面
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "view", method = { RequestMethod.GET, RequestMethod.POST })
	public Value view(NationalDayActivity01H5 h5) {
		JSONObject jo = new JSONObject();
		jo.put("viewAt", new Date());
		jo.put("userId", Security.getSafeUserId());
		jo.put("h5", h5);
		// mqSender.send(MqActivityRegistryConstants.EX_NDA01,
		// MqActivityRegistryConstants.RK_NDA01_PVUV, jo);
		return new Value();
	}
}
