package com.lanking.uxb.zycon.wx.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.thirdparty.WXCallbackMessage;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.wx.api.ZycWXService;

/**
 * 微信相关接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年7月28日
 */
@Service
@Transactional(readOnly = true)
public class ZycWXServiceImpl implements ZycWXService {

	@Autowired
	@Qualifier("WXCallbackMessageRepo")
	Repo<WXCallbackMessage, Integer> repo;

	@Override
	public List<WXCallbackMessage> listWXCallbackMessages(Product product) {
		return repo.find("select * from wx_callback_message where product=:product",
				Params.param("product", product.getValue())).list();
	}

	@Override
	@Transactional
	public void cleanAndSaveWXCallbackMessages(Product product, Collection<WXCallbackMessage> messages) {
		repo.execute("delete from wx_callback_message where product=:product",
				Params.param("product", product.getValue()));
		if (messages.size() > 0) {
			repo.save(messages);
		}
	}
}
