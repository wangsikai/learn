package com.lanking.uxb.service.wx.api;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.thirdparty.WXCallbackMessage;

/**
 * 微信回调消息接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月30日
 */
public interface ZyWXCallbackMessageService {

	WXCallbackMessage getWXCallbackMessage(Product product, String messageType, String event, String key);
}
