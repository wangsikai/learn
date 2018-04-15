package com.lanking.uxb.service.thirdparty.youngy.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.youngy.form.YoungyeduUser;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
@Service(value = "youngyClient")
public class YoungyeduHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(YoungyeduHttpClient.class);

	@Autowired
	private HttpClient httpClient;

	// 根据token查询用户信息url
	private String queryUserByTokenUrl;

	@PostConstruct
	private void postContruct() {
		this.queryUserByTokenUrl = Env.getString("youngy.web.login.token.url");
	}

	/**
	 * 根据token查询登录情况数据
	 *
	 * key : value
	 *
	 * studentInfo : 学生信息(YoungyeduUser) teacherInfo : 教师信息(YoungyeduUser)
	 * classInfo : 班级信息(YoungyeduClass)
	 *
	 * @param token
	 *            登录后token数据
	 * @return {@link Map}
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryUserByToken(String token) {
		if (StringUtils.isBlank(token)) {
			return Collections.EMPTY_MAP;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userToken", token);
		HttpPost httpPost = new HttpPost(queryUserByTokenUrl);

		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setHeader("accept", "application/json;charset=UTF-8");

		StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(entity);

		Map<String, Object> retMap = new HashMap<String, Object>(3);

		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);

			int status = httpResponse.getStatusLine().getStatusCode();
			String response = EntityUtils.toString(httpResponse.getEntity());
			if (status == 200) {
				JSONObject result = JSONObject.parseObject(response);

				JSONArray jsonArray = result.getJSONArray("data");
				if (jsonArray == null || jsonArray.size() == 0) {
					logger.error("youngyedu response data error!");
					return null;
				}
				JSONObject data = JSONObject.parseObject(jsonArray.get(0).toString());
				// 用户信息
				JSONObject userInfo = data.getJSONObject("userInfo");
				YoungyeduUser user = parseUserData(userInfo);

				// 班级信息
				JSONObject classInfo = data.getJSONObject("classInfo");
				Integer gradeId = Integer.valueOf(classInfo.getString("gradeCode").substring(2));
				// 小学数据不处理
				if (gradeId <= 6) {
					return Collections.EMPTY_MAP;
				} else if (gradeId >= 7 && gradeId <= 9) {
					user.setPhase(2);
				} else {
					user.setPhase(3);
				}
				switch (user.getPhase()) {
				case 2:
					user.setCategoryCode(11);
					break;
				case 3:
					user.setCategoryCode(13);
					break;
				default:
					user.setCategoryCode(11);
				}

				retMap.put("teacherInfo", user);

			} else {
				logger.error("when query youngyedu user by token has error: {}", response);
			}
		} catch (Exception e) {
			logger.error("when execute the http post has error: {}", e);
		}

		return retMap;
	}

	/**
	 * UB09在融捷那里表示 教师身份 UB10表示学生身份
	 *
	 * SH02表示是数学学科相关数据，其他学科不关心。
	 *
	 *
	 * 目前融捷那里的业务逻辑是一个班级可以对应多个教师（不同学科教师）
	 *
	 * @param userJson
	 *            学生JSON数据信息
	 * @return {@link YoungyeduUser}
	 */
	private YoungyeduUser parseUserData(JSONObject userJson) {
		String subjectCodes = userJson.getString("subjectCodes");
		if (!subjectCodes.contains("SH02")) {
			return null;
		}
		YoungyeduUser user = new YoungyeduUser();
		user.setName(userJson.getString("userName"));
		String userRole = userJson.getString("userRole");
		switch (userRole) {
		case "UB09":
			user.setUserType(1);
			break;
		case "UB10":
			user.setUserType(2);
			break;
		default:
			user.setUserType(1);
		}
		// 返回数据格式为SE06 SE07 SE10 分别代表 1-12年的教育阶段
		String gradeCode = userJson.getString("gradeCode");
		if (gradeCode != null) {
			Integer gradeId = Integer.valueOf(userJson.getString("gradeCode").substring(2));
			// 小学数据不处理
			if (gradeId <= 6) {
				return null;
			} else if (gradeId >= 7 && gradeId <= 9) {
				user.setPhase(2);
			} else {
				user.setPhase(3);
			}
		}

		if (user.getPhase() != null) {
			switch (user.getPhase()) {
			case 2:
				user.setCategoryCode(11);
				break;
			case 3:
				user.setCategoryCode(13);
				break;
			default:
				user.setCategoryCode(11);
			}
		}

		user.setRealName(userJson.getString("userRealName"));
		String userId = userJson.getString("userId");
		user.setUserId(userId);
		return user;
	}

}
