package com.lanking.uxb.service.thirdparty.qq.client;

import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.thirdparty.TokenInfo;
import com.lanking.uxb.service.thirdparty.qq.response.QQUser;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

public class LankingQQUserInfo extends UserInfo {
	private static final long serialVersionUID = 4067135050721181421L;

	private String appid;
	private String token;
	private String openID;
	private Long validtime;

	public LankingQQUserInfo(String appid, String token, String openID, Long validtime) {
		super(token, openID);
		this.appid = appid;
		this.token = token;
		this.openID = openID;
		this.validtime = validtime;
	}

	public QQUser getQQUser() throws QQConnectException {
		// UserInfoBean userInfoBean = this.getUserInfo();
		JSONObject userObj = this.getUserObject();
		QQUser user = new QQUser();
		try {
			user.setNickname(userObj.getString("nickname"));
			user.setPersonid(this.openID);
			user.setLogourl(userObj.getString("figureurl_qq_2"));
			if (StringUtils.isNotBlank(userObj.getString("gender"))) {
				if ("男".equals(userObj.getString("gender"))) {
					user.setSex(Sex.MALE);
				} else if ("女".equals(userObj.getString("gender"))) {
					user.setSex(Sex.FEMALE);
				} else {
					user.setSex(Sex.UNKNOWN);
				}
			}
		} catch (JSONException e) {
			throw new QQConnectException(e);
		}
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(this.token);
		tokenInfo.setValidtime(this.validtime);
		user.setTokenInfo(tokenInfo);
		return user;
	}

	public UserInfoBean getUserInfo() throws QQConnectException {
		return getUserInfo(this.client.getOpenID());
	}

	private UserInfoBean getUserInfo(String openid) throws QQConnectException {
		return new UserInfoBean(this.client
				.get(QQConnectConfig.getValue("getUserInfoURL"),
						new PostParameter[] { new PostParameter("openid", openid),
								new PostParameter("oauth_consumer_key", this.appid),
								new PostParameter("access_token", this.client.getToken()),
								new PostParameter("format", "json") }).asJSONObject());
	}

	private JSONObject getUserObject() throws QQConnectException {
		JSONObject obj = this.client
				.get(QQConnectConfig.getValue("getUserInfoURL"),
						new PostParameter[] { new PostParameter("openid", this.client.getOpenID()),
								new PostParameter("oauth_consumer_key", this.appid),
								new PostParameter("access_token", this.client.getToken()),
								new PostParameter("format", "json") }).asJSONObject();
		return obj;
	}
}
