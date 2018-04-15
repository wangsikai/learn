package com.lanking.uxb.service.thirdparty.school.jlms.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.school.jlms.resource.JLMSUser;
import com.tencent.common.MD5;

import httl.util.StringUtils;

@Component
public class JLMSClient {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;

	/**
	 * 九龙中学应用Key.
	 */
	private String appKey;

	/**
	 * 九龙中学应用Secret.
	 */
	private String appSecret;

	/**
	 * 九龙中学电教馆应用CODE.
	 */
	private String dtsAppCode;

	/**
	 * 九龙中学电教馆应用Key.
	 */
	private String dtsAppKey;

	/**
	 * 电教馆获取用户信息.
	 */
	public JLMSUser getUserInfo(String userId, UserType userType) {
		this.initProperties();
		String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
		String encrytString = Codecs.md5Hex(this.dtsAppKey + timeSpan).toLowerCase();

		String params = "AppCode=" + this.dtsAppCode + "&EncrytString=" + encrytString + "&TimeSpan=" + timeSpan
				+ "&Id=" + userId;
		String method = userType == UserType.TEACHER ? "GetJS" : "GetXS";
		HttpGet httpGet = new HttpGet("http://pib.nje.cn/Service/Common.asmx/" + method + "?" + params);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Content-type", "text/xml; charset=utf-8");

		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				if (StringUtils.isNotBlank(result)) {
					JLMSUser user = new JLMSUser();
					user.setUserid(userId);
					user.setUserType(userType);
					Pattern pattern = Pattern.compile("<XM>([^\"]+?)</XM>");
					Matcher matcher = pattern.matcher(result);
					while (matcher.find()) {
						user.setRealName(matcher.group(1));
						break;
					}
					pattern = Pattern.compile("<XBM>([^\"]+?)</XBM>");
					matcher = pattern.matcher(result);
					while (matcher.find()) {
						user.setSex("2".equals(matcher.group(1)) ? Sex.FEMALE : Sex.MALE);
						break;
					}
					return user;
				} else {
					logger.error("[JLMS] 电教馆获取用户信息出错！result is empty");
				}
				logger.info("getuserinfo = " + result);
			} else {
				String result = EntityUtils.toString(response.getEntity());
				logger.error("[JLMS] 电教馆获取用户信息出错！" + result);
			}
		} catch (Exception e) {
			logger.error("[JLMS] 电教馆获取用户信息出错！" + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 验证签名.
	 * 
	 * @param request
	 * @return
	 */
	public boolean signCheck(HttpServletRequest request) {
		this.initProperties();
		String sign = request.getParameter("sign");
		if (StringUtils.isBlank(sign)) {
			return false;
		}

		Map<String, String[]> requestParams = request.getParameterMap();

		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length > 0 && !entry.getKey().equalsIgnoreCase("sign")
					&& !entry.getKey().equalsIgnoreCase("signmethod")) {
				list.add(entry.getKey() + "=" + entry.getValue()[0] + "&");
			}
		}
		int size = list.size();

		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.substring(0, sb.length() - 1).toString() + this.appSecret;
		return sign.toUpperCase().equals(MD5.MD5Encode(result).toUpperCase());
	}

	private void initProperties() {
		this.appKey = Env.getDynamicString("jlms.app.key");
		this.appSecret = Env.getDynamicString("jlms.app.secret");
		this.dtsAppCode = Env.getDynamicString("jlms.app.dts.code");
		this.dtsAppKey = Env.getDynamicString("jlms.app.dts.key");
	}

	public static void main(String args[]) {
		String appkey = "f98d62300273df52";
		String format = "text";
		String timestamp = "1509332401720";
		String tokenid = "aa5f3b89-e84c-4a16-800d-8fbcae2ac9ed";
		String userRole = "1";
		String userid = "fe672968-cbad-4d11-8652-db86e7a5a34d";
		String signmethod = "MD5";
		String v = "1.0";

		String sign = "F24022584F3728278F6913CF238F8BDD";
		String secretcode = "11bdd3d111e94eb58f4db8e724b531d6";

		String temp = "appkey=" + appkey + "&format=" + format + "&timestamp=" + timestamp + "&tokenid=" + tokenid
				+ "&user_role=" + userRole + "&userid=" + userid + "&v=" + v + secretcode;
		// sign=B10813C5DC57D771656DD19FF4A58D0A
		// temp =
		// "appkey=1005003&format=text&signmethod=MD5&timestamp=1508147724173&tokenid=240be839-116f-470d-bae6-57f51195a4a5&user_role=1&userid=11B5D39F-690E-421C-8ED9-8C2C2CF9ECE9&v=1.0";
		System.out.println(temp);
		String x = MD5.MD5Encode(temp);
		String encrytString = Codecs.md5Hex(temp).toUpperCase();
		// String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
		// String encrytString =
		// Codecs.md5Hex("43468899-16F5-40B7-91CB-C88260BF4278" +
		// timeSpan).toLowerCase();
		// System.out.println(timeSpan);
		System.out.println("x=" + x);
		System.out.println(encrytString);

		JLMSClient client = new JLMSClient();
		temp = "appkey=f98d62300273df52&format=text&timestamp=1509332401720&tokenid=aa5f3b89-e84c-4a16-800d-8fbcae2ac9ed&user_role1&userid=fe672968-cbad-4d11-8652-db86e7a5a34d&v=1.011bdd3d111e94eb58f4db8e724b531d6";
		// String y = client.EncryStr(temp);
		// System.out.println("y=" + y);

		// client.signCheck(request);
	}
}
