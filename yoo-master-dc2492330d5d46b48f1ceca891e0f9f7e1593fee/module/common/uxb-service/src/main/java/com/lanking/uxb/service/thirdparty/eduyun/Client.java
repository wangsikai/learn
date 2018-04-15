package com.lanking.uxb.service.thirdparty.eduyun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.TokenInfo;
import com.lanking.uxb.service.thirdparty.eduyun.ex.EduyunException;
import com.lanking.uxb.service.thirdparty.eduyun.response.YunClass;
import com.lanking.uxb.service.thirdparty.eduyun.response.YunUser;
import com.whty.apigateway.security.EncryptionUtils;

import httl.util.StringUtils;

/**
 * 教育云客户端.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年7月2日
 */
@Component
public class Client implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;

	public String zuoyeAppId;
	public String zuoyeAppKey;
	public String webAppId;
	public String webAppKey;
	public String ssoUrl;
	public String webCallbackUrl;
	public String zuoyeCallbackUrl;
	public String zuoyePadCallbackUrl;
	public String ticketValidateUrl;
	public String accesstokenUrl;
	public String currentUserinfoUrl;
	public String logoutUrl;
	public String host;

	/**
	 * 获得SSO地址.
	 * 
	 * @param product
	 *            产品
	 * 
	 * @return
	 */
	public String getSsoUrl(Product product, String rpath) {
		String callbackUrl = "";
		if (Product.YOOMATH == product) {
			callbackUrl = this.zuoyeCallbackUrl;
		}
		return new StringBuffer(this.ssoUrl).append("&service=").append(callbackUrl).append("/eduyun/")
				.append(product.getValue() + "_"
						+ com.lanking.cloud.sdk.util.StringUtils.defaultString(rpath).replace("/", "@"))
				.append("/callback").toString();
	}

	/**
	 * 验证凭证，获取sessionID.
	 * 
	 * @param ticket
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws DocumentException
	 * @throws ParseException
	 */
	public String ticketValidate(String ticket)
			throws ClientProtocolException, IOException, ParseException, DocumentException {
		HttpGet httpGet = new HttpGet(this.ticketValidateUrl + "?ticket=" + ticket);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("content-type", "text/html");
		HttpResponse response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			Document document = DocumentHelper.parseText(EntityUtils.toString(response.getEntity()));
			if (null != document) {
				Element root = document.getRootElement();
				Element successElement = root.element("authenticationSuccess");
				Element failureElement = root.element("authenticationFailure");
				if (successElement != null) {
					Element user = successElement.element("user");
					return user.getTextTrim();
				} else if (failureElement != null) {
					logger.info("eduyun ticketValidate error [msg:{}]", failureElement.getText());
				}
			}
		}
		return "";
	}

	/**
	 * 获取AccessTOKEN.
	 * 
	 * @param 0:web，1:zuoye
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public TokenInfo getaccesstoken(Product product) throws ClientProtocolException, IOException {
		String appId = "", appKey = "";
		if (product == Product.YOOMATH) { // 悠数学
			appId = this.zuoyeAppId;
			appKey = this.zuoyeAppKey;
		}

		HttpPost httpPost = new HttpPost(this.accesstokenUrl);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("content-type", "application/json");

		long timestamp = System.currentTimeMillis();
		byte[] hmacSHA = EncryptionUtils.getHmacSHA1(appId + appKey + timestamp, appKey);
		String digest = EncryptionUtils.bytesToHexString(hmacSHA);
		JSONObject jo = new JSONObject();
		jo.put("appid", appId);
		jo.put("timestamp", String.valueOf(timestamp));
		jo.put("keyinfo", digest.toUpperCase());
		httpPost.setEntity(new StringEntity(jo.toString(), "utf-8"));

		String token = "";
		long validtime = timestamp;
		TokenInfo tokenInfo = new TokenInfo();

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = (JSONObject) JSON.parse(result);
				if ("000000".equals(jsonObject.getString("result"))) {
					token = jsonObject.getJSONObject("tokenInfo").getString("token");
					validtime = Long.parseLong(jsonObject.getJSONObject("tokenInfo").getString("validtime"));
					tokenInfo.setToken(token);
					tokenInfo.setValidtime(validtime);
				} else {
					logger.info("eduyun getaccesstoken error [code:{}, msg:{}] ", jsonObject.getString("result"),
							jsonObject.getString("desc"));
				}
			} else {
				logger.info(
						"eduyun getaccesstoken error, url = {}, appId = {}, appKey = {}, timestamp = {}, keyinfo = {}, result = {}",
						this.accesstokenUrl, appId, appKey, timestamp, digest.toUpperCase(), result);
			}
		}
		return tokenInfo;
	}

	/**
	 * 获取当前用户信息.
	 * 
	 * @param token
	 * @param usessionid
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public YunUser getuserinfo(String token, String usessionid) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(this.currentUserinfoUrl + "/" + usessionid + "?token=" + token);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Content-type", "text/html");
		HttpResponse response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("eduyun getuserinfo result [{}] ", result);
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = (JSONObject) JSON.parse(result);
				if ("000000".equals(jsonObject.getString("result"))) {
					JSONObject userinfo = jsonObject.getJSONObject("userinfo");
					JSONArray teachesubjectlist = userinfo.getJSONArray("teachesubjectlist");
					List<YunClass> classes = new ArrayList<YunClass>();
					if (teachesubjectlist != null) {
						for (int i = 0; i < teachesubjectlist.size(); i++) {
							YunClass yunClass = JSONObject.toJavaObject(teachesubjectlist.getJSONObject(i),
									YunClass.class);
							classes.add(yunClass);
						}
					}
					YunUser yunUser = JSONObject.toJavaObject(jsonObject.getJSONObject("userinfo"), YunUser.class);
					yunUser.setTeachesubjectlist(classes);
					return yunUser;
				} else if ("900001".equals(jsonObject.getString("result"))) {
					// TOKEN或会话失效
					throw new EduyunException(EduyunException.TOKEN_OR_DIGITAL_INCORRENT);
				} else {
					logger.info("eduyun getuserinfo error [code:{}] ", jsonObject.getString("result"));
				}
			}
		}
		return null;
	}

	/**
	 * 注销.
	 * 
	 * @param token
	 * @param usessionid
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void logout(String token, String usessionid) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(this.logoutUrl + "/" + usessionid + "?token=" + token);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Content-type", "text/html");
		HttpResponse response = httpClient.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = (JSONObject) JSON.parse(result);
				if ("000000".equals(jsonObject.getString("result"))) {
					// ...
				} else {
					logger.info("eduyun logout error [code:{}] ", jsonObject.getString("result"));
				}
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.zuoyeAppId = Env.getString("eduyun.appid.zuoye");
		this.zuoyeAppKey = Env.getString("eduyun.appkey.zuoye");
		this.webAppId = Env.getString("eduyun.appid.web");
		this.webAppKey = Env.getString("eduyun.appkey.web");
		this.ssoUrl = Env.getString("eduyun.sso.url");
		this.webCallbackUrl = Env.getString("eduyun.sso.callback.web");
		this.zuoyeCallbackUrl = Env.getString("eduyun.sso.callback.zuoye");
		this.zuoyePadCallbackUrl = Env.getString("eduyun.sso.callback.zuoyepad");
		this.ticketValidateUrl = Env.getString("eduyun.sso.ticket");
		this.accesstokenUrl = Env.getString("eduyun.sso.accesstoken");
		this.currentUserinfoUrl = Env.getString("eduyun.api.currentUserinfo");
		this.logoutUrl = Env.getString("eduyun.api.logout");
		this.host = Env.getString("eduyun.host");

	}
}
