package com.lanking.uxb.service.thirdparty.jsedu.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.TokenInfo;
import com.lanking.uxb.service.thirdparty.jsedu.response.JSEduStudent;
import com.lanking.uxb.service.thirdparty.jsedu.response.JSEduTeacherClass;
import com.lanking.uxb.service.thirdparty.jsedu.response.JSEduUser;

import httl.util.StringUtils;

/**
 * 江苏电信教育平台.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月26日
 */
@Component
public class JSEduClient implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;

	private String yoomathKey;
	private String yoomathClientID;
	private String getTokenURL;
	private String tokenBackURL;
	private String getUserURL;
	private String getTeacherClassURL;
	private String getStudentURL;

	/**
	 * 跳转获取TOKEN.
	 * 
	 * @param request
	 * @param response
	 */
	public void jumpToGetToken(HttpServletRequest request, HttpServletResponse response) {
		String url = this.getTokenURL + "?client_id=" + this.yoomathClientID + "&spring-security-redirect="
				+ this.tokenBackURL;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取云用户信息.
	 * 
	 * @param token
	 *            access_token
	 * @return
	 */
	public JSEduUser getUser(String token) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = format.format(new Date());
		String hashcode = Codecs.md5Hex(timestamp + this.yoomathClientID + this.yoomathKey);
		String params = "access_token=" + token + "&caller=" + this.yoomathClientID + "&timestamp=" + timestamp
				+ "&hashcode=" + hashcode;
		HttpPost httpPost = new HttpPost(this.getUserURL + "?" + params);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("[JSEDU] getuserinfo = " + result);
			if (StringUtils.isNotBlank(result)) {
				JSEduUser user = JSON.parseObject(result, JSEduUser.class);
				if ("200".equals(user.getResultCode())) {
					TokenInfo tokenInfo = new TokenInfo();
					tokenInfo.setToken(token);
					user.setTokenInfo(tokenInfo);
					if ("1".equals(user.getIdentityType())) {
						// 老师
						// user.setTeacherClasses(this.getTeacherClass(user.getIdentityId()));
					} else if ("5".equals(user.getIdentityType())) {
						// 家长学生
						// user.setStudentinfo(this.getStudent(user.getIdentityId()));
					}
					return user;
				} else {
					logger.error("[JSEDU] getuserinfo = " + result);
				}
			}
		}
		return null;
	}

	/**
	 * 获取教师班级信息.
	 * 
	 * @return
	 */
	public List<JSEduTeacherClass> getTeacherClass(String parentId) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = format.format(new Date());
		String hashcode = Codecs.md5Hex(timestamp + this.yoomathClientID + this.yoomathKey);
		String params = "client_id=" + this.yoomathClientID + "&timestamp=" + timestamp + "&hashcode=" + hashcode;
		HttpPost httpPost = new HttpPost(this.getTeacherClassURL + "/" + parentId + "?" + params);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("[JSEDU] getTeacherClass = " + result);
			if (StringUtils.isNotBlank(result)) {
				try {
					List<JSEduTeacherClass> classes = JSON.parseArray(result, JSEduTeacherClass.class);
					return classes;
				} catch (Exception e) {
					logger.error("[JSEDU] getTeacherClass = " + result);
				}
			}
		}
		return null;
	}

	/**
	 * 获取学生信息.
	 * 
	 * @return
	 */
	public JSEduStudent getStudent(String parentId) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = format.format(new Date());
		String hashcode = Codecs.md5Hex(timestamp + this.yoomathClientID + this.yoomathKey);
		String params = "client_id=" + this.yoomathClientID + "&timestamp=" + timestamp + "&hashcode=" + hashcode;
		HttpPost httpPost = new HttpPost(this.getStudentURL + "/" + parentId + "?" + params);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("[JSEDU] getStudent = " + result);
			if (StringUtils.isNotBlank(result)) {
				JSEduStudent student = JSON.parseObject(result, JSEduStudent.class);
				if ("200".equals(student.getResultCode())) {
					return student;
				} else {
					logger.error("[JSEDU] getStudent = " + result);
				}
			}
		}
		return null;
	}

	/**
	 * 校验合法性.
	 * 
	 * @param timestamp
	 *            时间戳
	 * @param hashcode
	 * @return
	 */
	public boolean check(String timestamp, String hashcode) {
		String rightHashcode = Codecs.md5Hex(timestamp + this.yoomathClientID + this.yoomathKey);
		return rightHashcode.equals(hashcode);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.yoomathKey = Env.getString("jsedu.key.yoomath");
		this.yoomathClientID = Env.getString("jsedu.clientID.yoomath");
		this.getTokenURL = Env.getString("jsedu.api.getTokenURL");
		this.tokenBackURL = Env.getString("jsedu.api.tokenBackURL");
		this.getUserURL = Env.getString("jsedu.api.getUserURL");
		this.getTeacherClassURL = Env.getString("jsedu.api.getTeacherClassURL");
		this.getStudentURL = Env.getString("jsedu.api.getStudentURL");
	}
}
