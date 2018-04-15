package com.lanking.uxb.service.thirdparty.qq.client;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.qq.connect.QQConnectException;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;

public class LankingQQAauth extends Oauth {
	private static final long serialVersionUID = -5719929362466454682L;
	private String appid;
	private String appkey;
	private String redirectURI;

	public LankingQQAauth(String appid, String appkey, String redirectURI) {
		this.appid = appid;
		this.appkey = appkey;
		this.redirectURI = redirectURI;
	}

	public AccessToken getAccessTokenByRequest(ServletRequest request) throws QQConnectException {
		String queryString = ((HttpServletRequest) request).getQueryString();
		if (queryString == null) {
			return new AccessToken();
		}
		// String state = (String) ((HttpServletRequest)
		// request).getSession().getAttribute("qq_connect_state");
		// if ((state == null) || (state.equals(""))) {
		// return new AccessToken();
		// }

		// String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
		// String returnState = authCodeAndState[1];
		String returnAuthCode = request.getParameter("code");

		AccessToken accessTokenObj = null;

		// if ((returnState.equals("")) || (returnAuthCode.equals(""))) {
		// accessTokenObj = new AccessToken();
		// } else
		accessTokenObj = new AccessToken(this.client.post(QQConnectConfig.getValue("accessTokenURL"),
				new PostParameter[] { new PostParameter("client_id", appid),
						new PostParameter("client_secret", appkey),
						new PostParameter("grant_type", "authorization_code"),
						new PostParameter("code", returnAuthCode), new PostParameter("redirect_uri", redirectURI) },
				Boolean.valueOf(false)));

		return accessTokenObj;
	}
}
