package com.lanking.uxb.service.message.api.impl.push;

import java.util.Map;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;

/**
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月27日
 */
public interface PushSender {

	String push2Android(Product product, YooApp app, String target, String title, String body,
			Map<String, Object> params);

	String push2IOS(Product product, YooApp app, String target, String body, Map<String, Object> params);

	void push2AllAndroid(Product product, YooApp app, String title, String body, Map<String, Object> params);

	void push2AllIOS(Product product, YooApp app, String body, Map<String, Object> params);

}
