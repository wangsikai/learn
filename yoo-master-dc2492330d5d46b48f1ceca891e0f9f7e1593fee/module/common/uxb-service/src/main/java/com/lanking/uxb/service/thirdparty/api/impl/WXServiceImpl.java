package com.lanking.uxb.service.thirdparty.api.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.api.WXService;
import com.lanking.uxb.service.thirdparty.cache.WXCacheService;
import com.lanking.uxb.service.thirdparty.weixin.client.WXClient;
import com.lanking.uxb.service.thirdparty.weixin.request.TemplateMessageParams;
import com.lanking.uxb.service.thirdparty.weixin.response.ServiceAccessToken;
import com.lanking.uxb.service.thirdparty.weixin.response.Userinfo;
import com.lanking.uxb.service.thirdparty.weixin.response.WXButton;
import com.lanking.uxb.service.thirdparty.weixin.response.WXMenus;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateContent;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateVideo;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplates;
import com.lanking.uxb.service.thirdparty.weixin.template.PublishHomeworkTemplate;

/**
 * 微信相关通用服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
@Service
public class WXServiceImpl implements WXService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private WXCacheService wxCacheService;
	@Autowired
	private WXClient client;

	@Override
	public String getServiceAccessToken(Product product) {
		String appid = client.getAppid(product);
		String token = wxCacheService.getServiceAccessToken(appid);
		if (StringUtils.isBlank(token)) {
			try {
				ServiceAccessToken accessToken = client.getServiceAccessToken(product);
				token = accessToken.getAccess_token();
				if (StringUtils.isNotBlank(token)) {
					// 前置10分钟有效期
					long timeout = accessToken.getExpires_in() == null ? 110 * 60 : (long) accessToken.getExpires_in()
							- (10 * 60);
					wxCacheService.setServiceAccessToken(appid, token, timeout, TimeUnit.SECONDS);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		return token;
	}

	@Override
	public Userinfo getUserinfo(String openid, Product product) {
		try {
			return client.getUserinfo(this.getServiceAccessToken(product), openid);
		} catch (ParseException | IOException | DocumentException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void sendTemplateMessage(PublishHomeworkTemplate template, String url, String openid, Product product) {
		boolean on = Env.getBoolean("weixin.msg.template.on");
		if (!on) {
			return;
		}
		String token = this.getServiceAccessToken(product);
		try {
			TemplateMessageParams param = new TemplateMessageParams();
			param.setTemplate_id(Env.getString("weixin.msg.template.remind.id"));
			param.setUrl(url);
			param.setTouser(openid);
			param.setData(template.getObject());

			logger.info("[WEIXIN] begin send templateMessage -> template id: {}, url: {}, openid:{}",
					param.getTemplate_id(), param.getUrl(), param.getTouser());
			client.sendTemplateMessage(param, token);
		} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获得自定义菜单.
	 * 
	 * @param product
	 */
	public WXMenus getMenus(Product product) {
		String token = this.getServiceAccessToken(product);
		try {
			return client.getMenus(token);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public WXTemplateContent getNewsTemplate(Product product, String mediaId) {
		String token = this.getServiceAccessToken(product);
		try {
			return client.getNewsTemplate(token, mediaId);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void getStreamTemplate(Product product, String mediaId, HttpServletRequest request,
			HttpServletResponse response) {
		String token = this.getServiceAccessToken(product);
		try {
			client.getStreamTemplate(token, mediaId, request, response);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Integer> getTemplateCount(Product product) {
		String token = this.getServiceAccessToken(product);
		try {
			return client.getTemplateCount(token);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return new HashMap<String, Integer>(0);
	}

	@Override
	public WXTemplates getTemplates(Product product, String type, int offset, int count) {
		String token = this.getServiceAccessToken(product);
		try {
			return client.getTemplates(token, type, offset, count);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public WXTemplateVideo getVideoTemplate(Product product, String mediaId) {
		String token = this.getServiceAccessToken(product);
		try {
			return client.getVideoTemplate(token, mediaId);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public boolean saveMenus(Product product, List<WXButton> menus) {
		String token = this.getServiceAccessToken(product);
		try {
			return client.saveMenus(token, menus);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
}
