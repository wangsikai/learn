package com.lanking.uxb.service.thirdparty.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.uxb.service.thirdparty.weixin.response.Userinfo;
import com.lanking.uxb.service.thirdparty.weixin.response.WXButton;
import com.lanking.uxb.service.thirdparty.weixin.response.WXMenus;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateContent;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateVideo;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplates;
import com.lanking.uxb.service.thirdparty.weixin.template.PublishHomeworkTemplate;

/**
 * 微信通用接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
public interface WXService {

	/**
	 * 获取服务号access_token.
	 * 
	 * @param product
	 *            产品
	 * @return
	 */
	String getServiceAccessToken(Product product);

	/**
	 * 获取用户基本信息.
	 * 
	 * @param openid
	 *            OPENID
	 * @param product
	 *            产品
	 * @return
	 */
	Userinfo getUserinfo(String openid, Product product);

	/**
	 * 发送模板消息.
	 * 
	 * @param template
	 *            模板
	 * @param url
	 *            跳转地址.
	 * @param openid
	 *            用户OPENID
	 * @param product
	 *            产品
	 */
	void sendTemplateMessage(PublishHomeworkTemplate template, String url, String openid, Product product);

	/**
	 * 获得自定义菜单.
	 * 
	 * @param product
	 *            产品
	 */
	WXMenus getMenus(Product product);

	/**
	 * 获得永久素材列表.
	 * 
	 * @param product
	 * @param type
	 *            素材类型.
	 * @param offset
	 *            起始位置
	 * @param count
	 *            搜索条数
	 * @return
	 */
	WXTemplates getTemplates(Product product, String type, int offset, int count);

	/**
	 * 获得四种类型的素材数量.
	 * 
	 * @param product
	 * @return
	 */
	public Map<String, Integer> getTemplateCount(Product product);

	/**
	 * 获得图文素材.
	 * 
	 * @param product
	 * @param mediaId
	 *            mediaID
	 * @return
	 */
	public WXTemplateContent getNewsTemplate(Product product, String mediaId);

	/**
	 * 获得视频素材.
	 * 
	 * @param product
	 * @param mediaId
	 *            mediaID
	 * @return
	 */
	public WXTemplateVideo getVideoTemplate(Product product, String mediaId);

	/**
	 * 直接下载图片、音频资源.
	 * 
	 * @param product
	 * @param mediaId
	 * @param response
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void getStreamTemplate(Product product, String mediaId, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 保存微信菜单.
	 * 
	 * @param accessToken
	 * @param menus
	 * @return
	 */
	boolean saveMenus(Product product, List<WXButton> menus);
}
