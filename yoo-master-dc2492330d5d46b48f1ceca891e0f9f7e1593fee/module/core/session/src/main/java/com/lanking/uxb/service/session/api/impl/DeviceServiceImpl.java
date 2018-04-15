package com.lanking.uxb.service.session.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.session.form.DeviceForm;

@Transactional(readOnly = true)
@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	@Qualifier("DeviceRepo")
	private Repo<Device, Long> deviceRepo;

	@Override
	public Device findByToken(String token, Product product) {
		return deviceRepo.find("$findByToken", Params.param("token", token).put("product", product.getValue())).get();
	}

	@Transactional
	@Override
	public Device register(DeviceForm form) {
		Device device = findByToken(form.getToken(), form.getProduct());
		if (device == null) {
			device = new Device();
		}
		device.setChannel(form.getChannel());
		device.setClientVersion(form.getClientVersion());
		device.setImei(form.getImei());
		device.setImsi(form.getImsi());
		device.setIsp(form.getIsp());
		device.setModel(form.getModel());
		device.setOs(form.getOs());
		device.setScreenHeight(form.getScreenHeight());
		device.setScreenWidth(form.getScreenWidth());
		device.setToken(form.getToken());
		device.setType(form.getType());
		device.setUserId(form.getUserId());
		device.setProduct(form.getProduct());
		return deviceRepo.save(device);
	}

	@Transactional
	@Override
	public void unregister(String token, Product product) {
		Device device = findByToken(token, product);
		if (device != null) {
			device.setUserId(0L);
			deviceRepo.save(device);
		}
	}

	@Override
	public List<Device> findByUserId(long userId, Product product) {
		return deviceRepo.find("$findByUserId", Params.param("userId", userId).put("product", product.getValue()))
				.list();
	}

	@Override
	public List<String> findTokenByUserIds(Collection<Long> userIds, Product product) {
		return deviceRepo
				.find("$findTokenByUserIds", Params.param("userIds", userIds).put("product", product.getValue()))
				.list(String.class);
	}
}
