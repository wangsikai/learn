package com.lanking.uxb.service.session.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.uxb.service.session.form.DeviceForm;

/**
 * 设备信息相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月27日
 */
public interface DeviceService {

	Device findByToken(String token, Product product);

	Device register(DeviceForm form);

	void unregister(String token, Product product);

	List<Device> findByUserId(long userId, Product product);

	List<String> findTokenByUserIds(Collection<Long> userIds, Product product);
}
