package com.lanking.uxb.service.wx.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.thirdparty.WXCallbackMessage;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.wx.api.ZyWXCallbackMessageService;

/**
 * 微信回调消息接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月30日
 */
@Service
@Transactional(readOnly = true)
public class ZyWXCallbackMessageServiceImpl implements ZyWXCallbackMessageService {
	@Autowired
	@Qualifier("WXCallbackMessageRepo")
	Repo<WXCallbackMessage, Long> repo;

	@Override
	public WXCallbackMessage getWXCallbackMessage(Product product, String messageType, String event, String key) {
		List<WXCallbackMessage> list = repo
				.find("select * from wx_callback_message where product=:product and type=:messageType and event=:event and key0=:key0",
						Params.param("product", product.getValue()).put("messageType", messageType).put("event", event)
								.put("key0", key)).list();

		return list.size() > 0 ? list.get(0) : null;
	}
}
