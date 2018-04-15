package com.lanking.uxb.service.thirdparty.weixin.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.TokenInfo;
import com.lanking.uxb.service.thirdparty.weixin.request.TemplateMessageParams;
import com.lanking.uxb.service.thirdparty.weixin.response.ServiceAccessToken;
import com.lanking.uxb.service.thirdparty.weixin.response.UserAccessToken;
import com.lanking.uxb.service.thirdparty.weixin.response.Userinfo;
import com.lanking.uxb.service.thirdparty.weixin.response.WXButton;
import com.lanking.uxb.service.thirdparty.weixin.response.WXMenus;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateContent;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateVideo;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplates;
import com.lanking.uxb.service.thirdparty.weixin.response.WXUser;

/**
 * 微信客户端.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */

@Component
public class WXClient {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;

	/**
	 * 获取应用APPID.
	 * 
	 * @param product
	 *            产品
	 * @return
	 */
	public String getAppid(Product product) {
		String appid = "";
		if (product == Product.YOOMATH) {
			appid = Env.getString("weixin.appid.zuoye");
		}
		return appid;
	}

	/**
	 * 获取应用Appsecret.
	 * 
	 * @param product
	 *            产品
	 * @return
	 */
	public String getAppsecret(Product product) {
		String appsecret = "";
		if (product == Product.YOOMATH) {
			appsecret = Env.getString("weixin.appsecret.zuoye");
		}
		return appsecret;
	}

	/**
	 * 获取应用Access_token.
	 * 
	 * @param product
	 *            产品
	 * @return
	 */
	public ServiceAccessToken getServiceAccessToken(Product product)
			throws ClientProtocolException, IOException, ParseException, DocumentException {
		String appid = this.getAppid(product);
		String appsecret = this.getAppsecret(product);

		HttpGet httpGet = new HttpGet(Env.getString("weixin.service.token") + "?grant_type=client_credential&appid="
				+ appid + "&secret=" + appsecret);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("content-type", "text/html");
		HttpResponse response = httpClient.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		ServiceAccessToken accessToken = new ServiceAccessToken();
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				accessToken = JSON.parseObject(result, ServiceAccessToken.class);
				if (StringUtils.isNotBlank(accessToken.getErrcode())) {
					logger.info("status: {0}, errorcode: {1}, errormsg: {2}", status, accessToken.getErrcode(),
							accessToken.getErrmsg());
				}
			} else {
				logger.info("status: {0}, result: {1}", status, result);
			}
		} else {
			logger.info("status: {0}, result: {1}", status, result);
		}

		return accessToken;
	}

	/**
	 * 获取用户基本信息.
	 * 
	 * @param accessToken
	 *            ACCESS-TOKEN
	 * @param openid
	 *            用户OPENID
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 * @throws DocumentException
	 */
	public Userinfo getUserinfo(String accessToken, String openid)
			throws ClientProtocolException, IOException, ParseException, DocumentException {
		HttpGet httpGet = new HttpGet(
				Env.getString("weixin.service.userinfo") + "?access_token=" + accessToken + "&openid=" + openid);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("content-type", "text/html");
		HttpResponse response = httpClient.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		Userinfo userinfo = null;
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				userinfo = JSON.parseObject(result, Userinfo.class);
				if (StringUtils.isNotBlank(userinfo.getErrcode())) {
					logger.info("status: {0}, errorcode: {1}, errormsg: {2}", status, userinfo.getErrcode(),
							userinfo.getErrmsg());
				}
			} else {
				logger.info("status: {0}, result: {1}", status, result);
			}
		} else {
			logger.info("status: {0}, result: {1}", status, result);
		}

		return userinfo;
	}

	/**
	 * 获取用户Access_token.
	 * 
	 * @param product
	 *            产品
	 * @return
	 */
	public UserAccessToken getUserAccessToken(String code, Product product)
			throws ClientProtocolException, IOException, ParseException, DocumentException {
		String appid = this.getAppid(product);
		String appsecret = this.getAppsecret(product);

		HttpGet httpGet = new HttpGet(Env.getString("weixin.user.token") + "?appid=" + appid + "&secret=" + appsecret
				+ "&code=" + code + "&grant_type=authorization_code");
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("content-type", "text/html");
		HttpResponse response = httpClient.execute(httpGet);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		UserAccessToken accessToken = new UserAccessToken();
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				accessToken = JSON.parseObject(result, UserAccessToken.class);
				if (StringUtils.isNotBlank(accessToken.getErrcode())) {
					logger.info("status: {0}, errorcode: {1}, errormsg: {2}", status, accessToken.getErrcode(),
							accessToken.getErrmsg());
				}
			} else {
				logger.info("status: {0}, result: {1}", status, result);
			}
		} else {
			logger.info("status: {0}, result: {1}", status, result);
		}

		return accessToken;
	}

	/**
	 * 获得微信用户.
	 * 
	 * @param accessToken
	 * @return
	 */
	public WXUser getUser(UserAccessToken accessToken) {
		WXUser user = new WXUser();
		user.setPersonid(accessToken.getOpenid());
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(accessToken.getAccess_token());
		tokenInfo.setValidtime(accessToken.getExpires_in() == null ? 0 : accessToken.getExpires_in() * 1000);
		user.setTokenInfo(tokenInfo);
		return user;
	}

	/**
	 * 发送模板消息.
	 * 
	 * @param param
	 *            模板消息参数.
	 * @param accessToken
	 *            ACCESSTOKEN
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void sendTemplateMessage(TemplateMessageParams param, String accessToken)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(Env.getString("weixin.msg.template.url") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		String json = JSON.toJSONString(param);
		StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(se);

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				logger.info("send weixin template Message Success! result: {}", result);
			} else {
				logger.info("status: {}, result: {}", status, result);
			}
		} else {
			logger.info("status: {}, result: {}", status, result);
		}
	}

	/**
	 * 获得自定义菜单.
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public WXMenus getMenus(String accessToken) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(Env.getString("weixin.api.getMenu") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		WXMenus menus = null;
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (StringUtils.isBlank(result)) {
				logger.error("[WX-api] get menu error! result is empty");
			} else {
				menus = JSON.parseObject(result, WXMenus.class);
			}
		} else {
			logger.error("[WX-api] get menu error! status: {}", status);
		}
		return menus;
	}

	/**
	 * 搜索永久素材列表.
	 * 
	 * @param accessToken
	 *            凭证
	 * @param type
	 *            素材类型
	 * @param offset
	 *            偏移量
	 * @param count
	 *            搜索数量
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public WXTemplates getTemplates(String accessToken, String type, int offset, int count)
			throws ClientProtocolException, IOException {
		// accessToken =
		// "SuKloMDW_0mjDWJ1O76TdrtroJnz6U2s1E75gKMB3cdBTTJrSvFk8IskzIiiOm7U0lVWa73xwrcQXY7BRywxxE6Jl1I1VepOEVrrCGizkXAmVkwFTF5H2Z0AUdSSdHQPMPIjAJAQTM";
		String url = Env.getString("weixin.api.getTemplates");
		HttpPost httpPost = new HttpPost(url + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		String json = "{\"type\":\"" + type + "\",\"offset\":" + offset + ",\"count\":" + count + "}";
		StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(se);

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		WXTemplates templates = null;
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (StringUtils.isBlank(result)) {
				logger.error("[WX-api] get templates error! result is empty");
			} else {
				templates = JSON.parseObject(result, WXTemplates.class);
			}
		} else {
			logger.error("[WX-api] get templates error! status: {}", status);
		}
		return templates;
	}

	/**
	 * 获得四种类型的素材数量.
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public Map<String, Integer> getTemplateCount(String accessToken) throws ClientProtocolException, IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();

		HttpPost httpPost = new HttpPost(Env.getString("weixin.api.getTemplateCount") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (StringUtils.isBlank(result)) {
				logger.error("[WX-api] get menu error! result is empty");
			} else {
				JSONObject obj = JSON.parseObject(result);
				map.put("voice_count", obj.getInteger("voice_count") == null ? 0 : obj.getInteger("voice_count"));
				map.put("video_count", obj.getInteger("video_count") == null ? 0 : obj.getInteger("video_count"));
				map.put("image_count", obj.getInteger("image_count") == null ? 0 : obj.getInteger("image_count"));
				map.put("news_count", obj.getInteger("news_count") == null ? 0 : obj.getInteger("news_count"));
			}
		} else {
			logger.error("[WX-api] get menu error! status: {}", status);
		}

		return map;
	}

	/**
	 * 获得图文素材.
	 * 
	 * @param accessToken
	 * @param mediaId
	 *            mediaID
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public WXTemplateContent getNewsTemplate(String accessToken, String mediaId)
			throws ClientProtocolException, IOException {
		// accessToken =
		// "SuKloMDW_0mjDWJ1O76TdrtroJnz6U2s1E75gKMB3cdBTTJrSvFk8IskzIiiOm7U0lVWa73xwrcQXY7BRywxxE6Jl1I1VepOEVrrCGizkXAmVkwFTF5H2Z0AUdSSdHQPMPIjAJAQTM";
		HttpPost httpPost = new HttpPost(Env.getString("weixin.api.getTemplate") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		String json = "{\"media_id\":\"" + mediaId + "\"}";
		StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(se);

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		WXTemplateContent wxTemplateContent = null;
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (StringUtils.isBlank(result)) {
				logger.error("[WX-api] get news template error! result is empty");
			} else {
				wxTemplateContent = JSON.parseObject(result, WXTemplateContent.class);
			}
		} else {
			logger.error("[WX-api] get news template error! status: {}", status);
		}
		return wxTemplateContent;
	}

	/**
	 * 获得视频素材.
	 * 
	 * @param accessToken
	 * @param mediaId
	 *            mediaID
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public WXTemplateVideo getVideoTemplate(String accessToken, String mediaId)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(Env.getString("weixin.api.getTemplate") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		String json = "{\"media_id\":\"" + mediaId + "\"}";
		StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(se);

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		WXTemplateVideo wxTemplateVideo = null;
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (StringUtils.isBlank(result)) {
				logger.error("[WX-api] get video template error! result is empty");
			} else {
				wxTemplateVideo = JSON.parseObject(result, WXTemplateVideo.class);
			}
		} else {
			logger.error("[WX-api] get video template error! status: {}", status);
		}
		return wxTemplateVideo;
	}

	/**
	 * 直接下载图片、音频资源.
	 * 
	 * @param accessToken
	 * @param mediaId
	 * @param response
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void getStreamTemplate(String accessToken, String mediaId, HttpServletRequest request,
			HttpServletResponse response) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(Env.getString("weixin.api.getTemplate") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		String json = "{\"media_id\":\"" + mediaId + "\"}";
		StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(se);

		HttpResponse wxResponse = httpClient.execute(httpPost);
		int status = wxResponse.getStatusLine().getStatusCode();
		if (status == 200) {
			OutputStream os = null;
			InputStream inputStream = null;
			try {
				Header[] headers = wxResponse.getAllHeaders();
				for (Header header : headers) {
					response.setHeader(header.getName(), header.getValue());
				}

				os = response.getOutputStream();
				inputStream = wxResponse.getEntity().getContent();
				ByteArrayOutputStream bops = new ByteArrayOutputStream();
				int data = -1;
				while ((data = inputStream.read()) != -1) {
					bops.write(data);
				}
				os.write(bops.toByteArray());
				os.flush();
			} catch (Exception e) {
				logger.error("[WX-api] get stream template error!", e);
			} finally {
				try {
					if (os != null) {
						os.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					logger.error("[WX-api] get stream template error!", e);
				}
			}
		} else {
			logger.error("[WX-api] get stream template error! status: {}", status);

			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 保存微信菜单.
	 * 
	 * @param menus
	 *            微信菜单
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public boolean saveMenus(String accessToken, List<WXButton> menus) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(Env.getString("weixin.api.saveMenus") + "?access_token=" + accessToken);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json; charset=UTF-8");

		String json = "{\"button\":" + JSONArray.toJSONString(menus) + "}";
		StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
		httpPost.setEntity(se);

		HttpResponse response = httpClient.execute(httpPost);
		int status = response.getStatusLine().getStatusCode();
		String result = "";
		if (status == 200) {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			if (StringUtils.isBlank(result)) {
				logger.error("[WX-api] saveMenus error! result is empty");
			} else {
				JSONObject obj = JSON.parseObject(result);
				int errcode = obj.getInteger("errcode");
				if (errcode == 0) {
					logger.info("[WX-api] saveMenus success! json: {}", json);
					return true;
				} else {
					String errmsg = obj.getString("errmsg");
					logger.error("[WX-api] saveMenus error! errcode: {}, errmsg {} , json: {}", errcode, errmsg, json);
				}
			}
		} else {
			logger.error("[WX-api] saveMenus error! status: {}", status);
		}
		return false;
	}

}
