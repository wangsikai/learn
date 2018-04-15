package com.lanking.uxb.service.message.api.impl.sms.emay;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.message.api.impl.sms.SmsSender;

public class EmaySmsSenderImpl implements SmsSender {

	private Logger logger = LoggerFactory.getLogger(EmaySmsSenderImpl.class);

	void init() {
		try {
			int i = SingletonClient.getClient().registEx(Env.getString("sms.emay.key"));
			logger.info("send sms result:{}", i);
		} catch (RemoteException e) {
			logger.error("send sms fail:", e);
		}
	}

	@Override
	public int send(String mobile, String content) {
		try {
			int i = SingletonClient.getClient().sendSMS(new String[] { mobile }, content, "", 5);
			logger.info("send sms result:{}", i);
			return i;
		} catch (RemoteException e) {
			logger.error("send sms fail:", e);
			return -1;
		}
	}

	@Override
	public int send(Collection<String> mobiles, String content) {
		try {
			String[] mobileArr = new String[mobiles.size()];
			int index = 0;
			for (String mobile : mobiles) {
				mobileArr[index] = mobile;
				index++;
			}
			int i = SingletonClient.getClient().sendSMS(mobileArr, content, "", 5);
			logger.info("send sms result:{}", i);
			return i;
		} catch (RemoteException e) {
			logger.error("send sms fail:", e);
			return -1;
		}
	}

	@Override
	public String send(String mobile, String signName, String smsTemplateCode, Map<String, Object> params) {
		return "-1";
	}

	@Override
	public String send(Collection<String> mobiles, String signName, String smsTemplateCode, Map<String, Object> params) {
		return "-1";
	}
}
