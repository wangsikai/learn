package com.lanking.uxb.service.thirdparty.scedu.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
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
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduClass;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduStudent;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduTeacher;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduUser;

import httl.util.StringUtils;

/**
 * 四川教育平台客户端.
 * 
 * @author wlche
 *
 */
@Component
public class SCClient implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;

	private String zuoyeKey;
	private String zuoyePkey;
	private String zuoyeAppkey;
	private String userinfoUrl;
	private String authUrl;

	public SCEduUser getuserinfo(Product product, String ticket) throws Exception {
		HttpPost httpPost = new HttpPost(this.authUrl);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-type", "application/json");

		JSONObject jo = new JSONObject();
		JSONObject params = new JSONObject();
		params.put("ticket", ticket);
		params.put("pkey", AESTool.Encrypt(this.zuoyePkey, this.zuoyeKey));
		params.put("appkey", AESTool.Encrypt(this.zuoyeAppkey, this.zuoyeKey));
		jo.put("params", params);
		httpPost.setEntity(new StringEntity(jo.toString(), "utf-8"));

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("getuserinfo = " + result);
			if (StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = (JSONObject) JSON.parse(result);
				JSONObject resp = jsonObject.getJSONObject("response");
				if (resp.getInteger("valid") != 1) {
					logger.info("获取四川教育云用户数据出错！result = " + result);
				} else {
					JSONObject content = resp.getJSONObject("content");
					SCEduUser user = new SCEduUser();
					user.setPersonid(content.getString("id"));
					String name = StringUtils.isBlank(content.getString("name")) ? "sichuan"
							: content.getString("name");
					String newName = "";
					char[] cs = name.toCharArray();
					int len = 0;
					for (char c : cs) {
						String n = String.valueOf(c);
						if (Pattern.compile("([a-z]|[A-Z]|[0-9]|[_])+").matcher(n).matches()) {
							len += 1;
						} else if (Pattern.compile("([\\u4e00-\\u9fa5])+").matcher(n).matches()) {
							len += 2;
						}
						newName += n;
						if (len >= 16) {
							break;
						}
					}
					if (len < 4) {
						newName = "sc_" + newName;
					}
					user.setName(newName);
					user.setOaccount(newName);
					user.setAccount(newName);
					user.setType(content.getInteger("type"));
					TokenInfo tokenInfo = new TokenInfo();
					tokenInfo.setToken(ticket); // ticket -> token
					user.setTokenInfo(tokenInfo);
					return user;
				}
			}
		}

		return null;
	}

	/**
	 * 获得教师详细数据.
	 * 
	 * @param product
	 * @param ticket
	 * @return
	 * @throws Exception
	 */
	public SCEduTeacher getTeacher(Product product, String ticket) throws Exception {
		HttpPost httpPost = new HttpPost(this.userinfoUrl);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-type", "application/json");

		JSONObject jo = new JSONObject();
		JSONObject params = new JSONObject();
		params.put("ticket", ticket);
		params.put("pkey", AESTool.Encrypt(this.zuoyePkey, this.zuoyeKey));
		params.put("appkey", AESTool.Encrypt(this.zuoyeAppkey, this.zuoyeKey));
		jo.put("params", params);
		httpPost.setEntity(new StringEntity(jo.toString(), "utf-8"));

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("[scedu] getTeacher = " + result);
			JSONObject jsonObject = (JSONObject) JSON.parse(result);
			JSONObject resp = jsonObject.getJSONObject("response");

			if (resp.getInteger("valid") != 1) {
				logger.info("获取四川教育云用户数据出错！result = " + result);
			} else {
				JSONObject content = resp.getJSONObject("content");
				JSONArray clazzArray = content.getJSONArray("classList");
				SCEduTeacher teacher = new SCEduTeacher();
				teacher.setSchoolId(content.getString("schoolId"));
				teacher.setSchoolOrgName(content.getString("schoolOrgName"));

				List<SCEduClass> clazzes = new ArrayList<SCEduClass>();
				if (clazzArray != null) {
					for (int i = 0; i < clazzArray.size(); i++) {
						SCEduClass clazz = new SCEduClass();
						JSONObject clazzObject = clazzArray.getJSONObject(i);
						clazz.setClassId(clazzObject.getString("classId"));
						clazz.setClassname(clazzObject.getString("classname"));
						clazz.setGradename(clazzObject.getString("gradename"));
						clazz.setSection(clazzObject.getInteger("section"));
						clazzes.add(clazz);
					}
				}
				teacher.setClazzes(clazzes);
				return teacher;
			}
		}

		return null;
	}

	/**
	 * 获得学生详细数据.
	 * 
	 * @param product
	 * @param ticket
	 * @return
	 * @throws Exception
	 */
	public SCEduStudent getStudent(Product product, String ticket) throws Exception {
		HttpPost httpPost = new HttpPost(this.userinfoUrl);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-type", "application/json");

		JSONObject jo = new JSONObject();
		JSONObject params = new JSONObject();
		params.put("ticket", ticket);
		params.put("pkey", AESTool.Encrypt(this.zuoyePkey, this.zuoyeKey));
		params.put("appkey", AESTool.Encrypt(this.zuoyeAppkey, this.zuoyeKey));
		jo.put("params", params);
		httpPost.setEntity(new StringEntity(jo.toString(), "utf-8"));

		HttpResponse response = httpClient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("[scedu] getStudent = " + result);
			JSONObject jsonObject = (JSONObject) JSON.parse(result);
			JSONObject resp = jsonObject.getJSONObject("response");

			if (resp.getInteger("valid") != 1) {
				logger.info("获取四川教育云用户数据出错！result = " + result);
			} else {
				JSONObject content = resp.getJSONObject("content");
				JSONArray clazzArray = content.getJSONArray("classList");
				SCEduStudent student = new SCEduStudent();
				student.setSchoolId(content.getString("schoolId"));
				student.setSchoolOrgName(content.getString("schoolOrgName"));

				List<SCEduClass> clazzes = new ArrayList<SCEduClass>();
				if (clazzArray != null) {
					for (int i = 0; i < clazzArray.size(); i++) {
						SCEduClass clazz = new SCEduClass();
						JSONObject clazzObject = clazzArray.getJSONObject(i);
						clazz.setClassId(clazzObject.getString("classId"));
						clazz.setClassname(clazzObject.getString("classname"));
						clazz.setGradename(clazzObject.getString("gradename"));
						clazzes.add(clazz);
					}
				}
				student.setClazzes(clazzes);
				return student;
			}
		}

		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.zuoyeKey = Env.getString("scedu.key.zuoye");
		this.zuoyePkey = Env.getString("scedu.pkey.zuoye");
		this.zuoyeAppkey = Env.getString("scedu.appkey.zuoye");
		this.userinfoUrl = Env.getString("scedu.service.userinfo");
		this.authUrl = Env.getString("scedu.service.auth");
	}
}
