package com.lanking.uxb.service.youngyedu.syncdata.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduClass;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduUser;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
@Service
public class YoungyeduHttpClient {

	private static final Logger logger = LoggerFactory.getLogger(YoungyeduHttpClient.class);

	@Autowired
	private HttpClient httpClient;

	// 根据token查询用户信息url
	private String queryUserByTokenUrl;
	// 同步数据url
	private String syncDataUrl;
	// 更新版本数据url
	private String syncResponseUrl;

	@PostConstruct
	private void postContruct() {
		this.queryUserByTokenUrl = Env.getString("youngy.query.user.token.url");
		this.syncDataUrl = Env.getString("youngy.sync.data.url");
		this.syncResponseUrl = Env.getString("youngy.sync.response.url");
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
				JSONObject data = JSONObject.parseObject(jsonArray.get(0).toString());
				// 学生信息
				JSONObject userInfo = data.getJSONObject("userInfo");
				YoungyeduUser user = parseUserData(userInfo);

				JSONArray teacherList = data.getJSONObject("teacherList").getJSONArray("data");
				YoungyeduUser teacherInfo = null;
				for (Object obj : teacherList) {
					JSONObject teacherJsonObj = JSONObject.parseObject(obj.toString());
					YoungyeduUser u = parseUserData(teacherJsonObj);
					if (u != null) {
						teacherInfo = u;
						break;
					}
				}

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
				YoungyeduClass clazz = parseClassInfo(classInfo, teacherInfo.getUserId());

				retMap.put("studentInfo", user);
				retMap.put("teacherInfo", teacherInfo);
				retMap.put("classInfo", classInfo);

			} else {
				logger.error("when query youngyedu user by token has error: {}", response);
			}
		} catch (Exception e) {
			logger.error("when execute the http post has error: {}", e);
		}

		return retMap;
	}

	private YoungyeduClass parseClassInfo(JSONObject classJsonObj, String teacherId) {
		YoungyeduClass clazz = new YoungyeduClass();
		clazz.setName(classJsonObj.getString("className"));
		clazz.setId(classJsonObj.getString("classOrg"));
		clazz.setTeacherId(teacherId);

		return clazz;
	}

	/**
	 * 查询需要同步的用户数据列表
	 *
	 * @param id
	 *            同步班级id
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<YoungyeduUser> querySyncUsers(String id) {

		if (StringUtils.isBlank(id)) {
			return Collections.EMPTY_LIST;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orgId", id);
		jsonObject.put("type", 2);

		HttpPost httpPost = new HttpPost(syncDataUrl);

		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setHeader("accept", "application/json;charset=UTF-8");

		StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
		httpPost.setEntity(entity);

		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);

			int status = httpResponse.getStatusLine().getStatusCode();
			String response = EntityUtils.toString(httpResponse.getEntity());

			if (status == 200) {
				JSONObject jsonRet = JSONObject.parseObject(response);
				JSONArray result = jsonRet.getJSONArray("userList");

				List<YoungyeduUser> users = new ArrayList<YoungyeduUser>(result.size());

				for (Object obj : result) {
					JSONObject userJson = JSONObject.parseObject(obj.toString());

					YoungyeduUser user = parseUserData(userJson);
					if (user == null)
						continue;

					YoungyeduClass clazz = new YoungyeduClass();
					clazz.setId(id);
					clazz.setName(userJson.getString("className"));

					user.setClazz(clazz);
					users.add(user);
				}

				return users;
			} else {
				logger.error("when query the youngyedu sync users has error: {}", response);
			}
		} catch (Exception e) {
			logger.error("when execute the http post has error: {}", e);
		}

		return Collections.EMPTY_LIST;
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

	/**
	 * 更新版本号
	 *
	 * @param classOrg
	 *            班级唯一标识
	 */
	public void updateVersion(String classOrg, boolean success) {
		if (StringUtils.isNotBlank(classOrg)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orgId", classOrg);
			jsonObject.put("success", success);

			HttpPost httpPost = new HttpPost(syncResponseUrl);

			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			httpPost.setHeader("accept", "application/json;charset=UTF-8");

			StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
			httpPost.setEntity(entity);

			try {
				HttpResponse httpResponse = httpClient.execute(httpPost);

				int status = httpResponse.getStatusLine().getStatusCode();
				if (status == 200) {
					logger.info("notice the youngyedu add {} to queue success, at {}", classOrg,
							System.currentTimeMillis());
				} else {
					logger.error("notice the youngyedu add {} to queue failed, at {}", classOrg,
							System.currentTimeMillis());
				}
			} catch (Exception e) {
				logger.error("when execute the http post has error: {}", e);
			}
		}
	}

}
