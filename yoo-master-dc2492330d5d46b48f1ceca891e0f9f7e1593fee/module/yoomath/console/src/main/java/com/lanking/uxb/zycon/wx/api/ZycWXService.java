package com.lanking.uxb.zycon.wx.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.thirdparty.WXCallbackMessage;

/**
 * 微信相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
public interface ZycWXService {

	/**
	 * 获得本地存储的微信回调消息.
	 * 
	 * @param ids
	 * @return
	 */
	List<WXCallbackMessage> listWXCallbackMessages(Product product);

	/**
	 * 清空并重新保存微信菜单配置.
	 * 
	 * @param buttons
	 * @return
	 */
	void cleanAndSaveWXCallbackMessages(Product product, Collection<WXCallbackMessage> messages);
}
