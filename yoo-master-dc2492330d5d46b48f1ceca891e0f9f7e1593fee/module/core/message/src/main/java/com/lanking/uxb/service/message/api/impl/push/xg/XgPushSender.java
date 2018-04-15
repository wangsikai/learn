package com.lanking.uxb.service.message.api.impl.push.xg;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.message.api.impl.push.PushSender;
import com.lanking.uxb.service.message.cache.PushCacheService;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.XingeApp;

@Component
@Configuration
public class XgPushSender implements PushSender {

	@Value("${push.xg.env}")
	private int env;

	private Map<String, XingeApp> apps = Maps.newHashMap();

	@Autowired
	private PushCacheService pushCacheService;

	private XingeApp getClient(Product product, YooApp app, boolean isIOS) {
		StringBuilder sb = new StringBuilder("push.");
		sb.append(product.getValue());
		if (app != null) {
			sb.append(".");
			sb.append(app.getValue());
		}
		if (isIOS) {
			sb.append(".ios.xg.");
		} else {
			sb.append(".android.xg.");
		}
		XingeApp client = apps.get(sb.toString());
		if (client == null) {
			synchronized (apps) {
				client = new XingeApp(Env.getLong(new StringBuilder(sb.toString()).append("accessid").toString()),
						Env.getString(new StringBuilder(sb.toString()).append("secretkey").toString()));
				apps.put(sb.toString(), client);
			}
		}
		return client;
	}

	@Override
	public String push2Android(Product product, YooApp app, String target, String title, String body,
			Map<String, Object> params) {
		pushCacheService.incrUnReadPushCount(product, target);

		if (params == null) {
			params = Maps.newHashMap();
			params.put("app", app.name());
		}

		Message message = new Message();
		message.setTitle(StringUtils.defaultIfBlank(title));
		message.setContent(StringUtils.defaultIfBlank(body));
		message.setCustom(params);
		message.setType(1);

		XingeApp client = getClient(product, app, false);
		if (client != null) {
			JSONObject ret = client.pushSingleDevice(target, message);
			if (ret == null) {
				return StringUtils.EMPTY;
			} else {
				return ret.toString();
			}
		} else {
			return StringUtils.EMPTY;
		}
	}

	@Override
	public void push2AllAndroid(Product product, YooApp app, String title, String body, Map<String, Object> params) {
		if (params == null) {
			params = Maps.newHashMap();
			params.put("app", app.name());
		}

		Message message = new Message();
		message.setTitle(StringUtils.defaultIfBlank(title));
		message.setContent(StringUtils.defaultIfBlank(body));
		message.setCustom(params);
		message.setType(1);

		XingeApp client = getClient(product, app, false);
		client.pushAllDevice(0, message);
	}

	@Override
	public String push2IOS(Product product, YooApp app, String target, String body, Map<String, Object> params) {
		long unReadCount = pushCacheService.incrUnReadPushCount(product, target);

		if (body.length() > 40) {
			body = body.substring(0, 40) + "...";
		}

		if (params == null) {
			params = Maps.newHashMap();
			params.put("app", app.name());
		}

		MessageIOS message = new MessageIOS();
		message.setAlert(StringUtils.defaultIfBlank(body));
		message.setCustom(params);
		message.setBadge((int) unReadCount);
		message.setSound("default");

		XingeApp client = getClient(product, app, true);

		if (client != null) {
			JSONObject ret = client.pushSingleDevice(target, message, env);
			if (ret == null) {
				return StringUtils.EMPTY;
			} else {
				return ret.toString();
			}
		} else {
			return StringUtils.EMPTY;
		}
	}

	@Override
	public void push2AllIOS(Product product, YooApp app, String body, Map<String, Object> params) {
		if (body.length() > 40) {
			body = body.substring(0, 40) + "...";
		}

		if (params == null) {
			params = Maps.newHashMap();
			params.put("app", app.name());
		}

		MessageIOS message = new MessageIOS();
		message.setAlert(StringUtils.defaultIfBlank(body));
		message.setCustom(params);
		message.setBadge(1);
		message.setSound("default");

		XingeApp client = getClient(product, app, true);
		client.pushAllDevice(0, message, env);
	}
}
