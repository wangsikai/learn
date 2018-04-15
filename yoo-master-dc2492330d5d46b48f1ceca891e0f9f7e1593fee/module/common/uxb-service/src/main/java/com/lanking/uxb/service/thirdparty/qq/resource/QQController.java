package com.lanking.uxb.service.thirdparty.qq.resource;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.qq.client.LankingQQAauth;
import com.lanking.uxb.service.thirdparty.qq.client.LankingQQUserInfo;
import com.lanking.uxb.service.thirdparty.qq.response.QQUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;

/**
 * QQ.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月19日
 */
@RestController
@RequestMapping("qq")
@RolesAllowed(anyone = true)
public class QQController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SessionService sessionService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private UserService userService;

	/**
	 * 获取回调地址.
	 * 
	 * @param product
	 * @param rpath
	 *            上下文路径
	 * @return
	 */
	@RequestMapping(value = "callbackUrl")
	public Value getCallbackUrl(Product product, String rpath) {
		String oauthUrl = Env.getString("qq.oauth.url");
		String appid = "", callbackUrl = "";
		if (product == Product.YOOMATH) {
			// 悠数学
			appid = Env.getString("qq.appid.zuoye");
			callbackUrl = Env.getString("qq.oauth.callback.zuoye");
		}
		return new Value(new StringBuffer(oauthUrl).append("?response_type=code&client_id=").append(appid)
				.append("&redirect_uri=").append(callbackUrl).append("/qq/callback").append("?system=")
				.append(product.getValue() + "_" + StringUtils.defaultString(rpath)).toString());
	}

	@RequestMapping(value = "/callback")
	private ModelAndView callback(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=utf-8");
		String systemStr = request.getParameter("system");
		String rpath = ""; // 工程上下文路径
		Product product = Product.YOOMATH;
		if (StringUtils.isNotBlank(systemStr)) {
			if (systemStr.indexOf("_") != -1) {
				if (systemStr.split("_").length >= 2) {
					rpath = systemStr.split("_")[1];
				}
				systemStr = systemStr.split("_")[0];
				product = Product.findByValue(Integer.parseInt(systemStr)); // 产品来源
			}
		}

		String accessToken = null, openID = null;
		long tokenExpireIn = 0L;
		String appid = "", appkey = "", callbackUrl = "";
		if (product == Product.YOOMATH) {
			// 悠数学
			appid = Env.getString("qq.appid.zuoye");
			appkey = Env.getString("qq.appkey.zuoye");
			callbackUrl = Env.getString("qq.oauth.callback.zuoye");
		}
		try {
			AccessToken accessTokenObj = (new LankingQQAauth(appid, appkey, callbackUrl + "/qq/callback"))
					.getAccessTokenByRequest(request);

			if (accessTokenObj.getAccessToken().equals("")) {
				logger.info("没有获取到响应参数");
			} else {
				accessToken = accessTokenObj.getAccessToken();
				tokenExpireIn = accessTokenObj.getExpireIn();
				logger.info("accessToken=" + accessToken);

				// 利用获取到的accessToken 去获取当前用的openid
				OpenID openIDObj = new OpenID(accessToken);
				openID = openIDObj.getUserOpenID();

				// 当前产品
				LankingQQUserInfo userInfo = new LankingQQUserInfo(appid, accessToken, openID, tokenExpireIn);
				QQUser user = userInfo.getQQUser();

				// 找到绑定的凭证
				Credential credential = credentialService.getCredentialByPersonId(product, CredentialType.QQ, openID);
				if (credential == null) {
					if (Security.isLogin()) {
						Long userId = Security.getUserId();
						Account account = accountService.getAccountByUserId(userId);

						// 已登录，绑定账户，创建凭证
						credential = new Credential();
						Date date = new Date();
						credential.setAccountId(account.getId());
						credential.setCreateAt(new Date());
						credential.setType(CredentialType.QQ);
						credential.setUid(openID);
						credential.setToken(accessToken);
						credential.setUpdateAt(date);
						credential.setName(user.getNickname());
						credential.setProduct(product);
						credential.setUserId(userId);

						// 调用新的凭证创建方法，金币成长值在方法内部处理
						credentialService.save(credential, false, Security.getUserType());
						return new ModelAndView("redirect:" + rpath + "/template/ucenter/bind-r.html?f=1&t=QQ");
					} else {
						// 缓存数据
						SessionPacket packet = new SessionPacket();
						packet.getAttrSession().setAttr("qquser", user);
						packet.getAttrSession().setAttr("credentialType", CredentialType.QQ.getValue());
						sessionService.refreshCurrentSession(packet, false);
						return new ModelAndView("redirect:" + rpath + "/third-login.html");
					}
				} else {
					if (Security.isLogin()) {
						Long userId = Security.getUserId();
						Account account = accountService.getAccountByUserId(userId);
						if (credential != null
								&& credential.getAccountId().longValue() != account.getId().longValue()) {
							// 已被其他账户绑定
							return new ModelAndView("redirect:" + rpath + "/template/ucenter/bind-r.html?f=0&t=QQ");
						}
					}

					User u = accountService.getUserByAccountId(credential.getAccountId());
					u.setLoginSource(product);
					accountService.handleLogin(u, request, response);

					// 缓存数据
					SessionPacket packet = new SessionPacket();
					packet.getAttrSession().setAttr("qquser", user);
					packet.getAttrSession().setAttr("credentialType", CredentialType.QQ.getValue());
					sessionService.refreshCurrentSession(packet, false);

					WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
					return new ModelAndView("redirect:" + rpath + "/index.html");
				}
			}
		} catch (QQConnectException e) {
			logger.error("QQ登录失败 ", e);
		}
		return new ModelAndView("redirect:" + rpath + "/login.html");
	}
}
