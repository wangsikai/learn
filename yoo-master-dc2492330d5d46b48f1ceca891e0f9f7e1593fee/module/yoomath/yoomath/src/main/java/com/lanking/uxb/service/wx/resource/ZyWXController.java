package com.lanking.uxb.service.wx.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.thirdparty.WXCallbackMessage;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.XmlBeanMarshall;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ThirdpartyService;
import com.lanking.uxb.service.thirdparty.weixin.response.WXArticle;
import com.lanking.uxb.service.thirdparty.weixin.response.WXMessage;
import com.lanking.uxb.service.thirdparty.weixin.response.WXUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.wx.api.ZyWXCallbackMessageService;
import com.lanking.uxb.service.wx.api.ZyWXReportService;
import com.lanking.uxb.service.wx.form.BindForm;

/**
 * 悠数学微信服务号控制器.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
@RestController
@RequestMapping("zy/wx")
public class ZyWXController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private ThirdpartyService thirdpartyService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ZyWXReportService zyWXReportService;
	@Autowired
	private ZyWXCallbackMessageService zyWXCallbackMessageService;

	/**
	 * 微信消息接收.
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "call")
	public void call(HttpServletRequest request, HttpServletResponse response) {
		String echostr = request.getParameter("echostr");

		String rmsg = "";
		if (this.checkSignature(request)) {
			rmsg = this.dataProcess(request, response);
		}

		PrintWriter writer = null;
		try {
			if (StringUtils.isBlank(rmsg)) {
				rmsg = echostr == null ? "" : echostr;
			}
			response.setHeader("content-type", "text/xml; charset=UTF-8");
			writer = response.getWriter();
			writer.write(rmsg);
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 验证.
	 * 
	 * @param request
	 * @return
	 */
	private boolean checkSignature(HttpServletRequest request) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");

		if (signature == null || timestamp == null || nonce == null) {
			return false;
		}
		String token = "elanking";

		List<String> tmpArr = new ArrayList<String>(3);
		tmpArr.add(token);
		tmpArr.add(timestamp);
		tmpArr.add(nonce);
		Collections.sort(tmpArr);
		String tmpStr = tmpArr.get(0) + tmpArr.get(1) + tmpArr.get(2);
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(tmpStr.getBytes());
			tmpStr = this.bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		if (tmpStr.equals(signature)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 转码.
	 * 
	 * @param bts
	 * @return
	 */
	private String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	/**
	 * 微信消息数据处理.
	 * 
	 * @param request
	 * @param response
	 */
	private String dataProcess(HttpServletRequest request, HttpServletResponse response) {
		InputStream in = null;
		try {
			in = request.getInputStream();
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			String xml = out.toString();
			if (StringUtils.isBlank(xml)) {
				return "";
			}

			String code = request.getParameter("code");
			WXMessage wxMessage = (WXMessage) XmlBeanMarshall.xml2Bean(WXMessage.class, xml);
			String openId = wxMessage.getFromUserName() == null ? code : wxMessage.getFromUserName(); // 发送方

			// 判断消息事件
			if (null != wxMessage.getMsgType() && wxMessage.getEvent() != null
					&& wxMessage.getMsgType().toLowerCase().equals("event")
					&& wxMessage.getEvent().toLowerCase().equals("click")) {
				// 菜单点击动作消息
				String key = wxMessage.getEventKey();
				if (StringUtils.isBlank(key)) {
					return "";
				}
				// 查找本地存储的菜单关系数据
				WXCallbackMessage wxCallbackMessage = zyWXCallbackMessageService.getWXCallbackMessage(Product.YOOMATH,
						"event", "click", key);
				if (wxCallbackMessage != null) {
					WXMessage responesMessage = new WXMessage();
					responesMessage.setFromUserName(Env.getString("weixin.developer")); // 开发者微信号
					responesMessage.setToUserName(openId); // 接收人
					responesMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));

					if (StringUtils.isNotBlank(wxCallbackMessage.getMediaId())) {
						// 图文消息
						List<WXArticle> articles = JSONArray.parseArray(wxCallbackMessage.getMessage(),
								WXArticle.class);
						responesMessage.setArticleCount(String.valueOf(articles.size()));
						responesMessage.setArticles(articles);
						responesMessage.setMsgType("news");
					} else {
						// 文本消息
						responesMessage.setContent(wxCallbackMessage.getMessage());
						responesMessage.setMsgType("text");
					}
					return XmlBeanMarshall.bean2Xml(responesMessage, "utf-8", true);
				} else {
					return "";
				}
			} else if (null != wxMessage.getMsgType() && wxMessage.getEvent() != null
					&& wxMessage.getMsgType().toLowerCase().equals("event")
					&& wxMessage.getEvent().toLowerCase().equals("subscribe")) {
				// 关注动作消息
				// 查找本地存储的菜单关系数据
				WXCallbackMessage wxCallbackMessage = zyWXCallbackMessageService.getWXCallbackMessage(Product.YOOMATH,
						"event", "subscribe", "subscribe");
				if (wxCallbackMessage != null) {
					WXMessage responesMessage = new WXMessage();
					responesMessage.setFromUserName(Env.getString("weixin.developer")); // 开发者微信号
					responesMessage.setToUserName(openId); // 接收人
					responesMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
					responesMessage.setContent(wxCallbackMessage.getMessage());
					responesMessage.setMsgType("text");

					return XmlBeanMarshall.bean2Xml(responesMessage, "utf-8", true);
				} else {
					return "";
				}
			}

			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, CredentialType.WEIXIN_MP,
					openId);
			if (credential == null) {
				return "";
			}
			User user = accountService.getUserByAccountId(credential.getAccountId());
			if (user.getUserType() == UserType.STUDENT && "text".equals(wxMessage.getMsgType())
					&& MessageEventType.REPORT_CODE.getValue().equals(wxMessage.getContent())) {
				// 报告兑换码
				String reportCode = zyWXReportService.getReportCodeAndSave();
				StringBuffer msg = new StringBuffer("学业报告兑换码：").append(reportCode).append("\n---------------\n兑换码赠送活动：")
						.append("\n1、在悠数学公众号中输入“我要兑换码”，获取免费兑换码；").append("\n2、每个兑换码只能查看一份报告，查看多份报告需要使用新的兑换码；")
						.append("\n3、活动时间2015-12-01——2016-12-31；\n4、本活动解释权归“悠数学”所有！");
				WXMessage responesMessage = new WXMessage();
				responesMessage.setFromUserName(Env.getString("weixin.developer")); // 开发者微信号
				responesMessage.setToUserName(openId); // 接收人
				responesMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
				responesMessage.setContent(msg.toString());
				responesMessage.setMsgType("text");

				return XmlBeanMarshall.bean2Xml(responesMessage, "utf-8", true);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 登录绑定.
	 * 
	 * @param username
	 * @param password
	 * @param openid
	 * @return
	 */
	@RequestMapping(value = "loginBind", method = {RequestMethod.POST})
	public Value loginBind(BindForm form, HttpServletRequest request, HttpServletResponse response) {
		if (form == null || StringUtils.isBlank(form.getUsername()) || StringUtils.isBlank(form.getPassword())
				|| StringUtils.isBlank(form.getOpenid()) || StringUtils.isBlank(form.getToken())) {
			return new Value(new MissingArgumentException());
		}
		String username = form.getUsername();
		String password = form.getPassword();
		String openid = form.getOpenid();
		Account account = null;
		if (username.contains("@")) {
			try {
				ValidateUtils.validateEmail(username);
			} catch (AccountException e) {
				accountCacheService.incrLoginWrongTime(Security.getToken());
				return new Value(e);
			}
			account = accountService.getAccount(GetType.EMAIL, username);
		} else {
			try {
				ValidateUtils.validateMobile(username);
				account = accountService.getAccount(GetType.MOBILE, username);
			} catch (AccountException ex) {
				try {
					ValidateUtils.validateName(username);
				} catch (AccountException e) {
					accountCacheService.incrLoginWrongTime(Security.getToken());
					return new Value(e);
				}
				account = accountService.getAccount(GetType.NAME, username);
			}
		}
		if (account == null) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		User user = accountService.getUserByAccountId(account.getId());
		if (user.getStatus() == Status.DISABLED) {
			return new Value(new AccountException(AccountException.ACCOUNT_FORBIDDEN));
		}
		if (user.getStatus() == Status.DELETED) {
			return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
		}
		if (account.getStatus() == Status.DELETED) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
		}
		if (account.getStatus() == Status.DISABLED) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_NOT_ACTIVE), account.getEmail());
		}

		if (user.getUserType() != form.getBindType()) {
			return new Value(new AccountException(AccountException.ACCOUNT_EXCEPTION));
		}

		if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
			accountCacheService.incrLoginWrongTime(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}
		try {
			user.setLoginSource(Product.YOOMATH); // 微信端登录

			// 绑定微信凭证
			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, CredentialType.WEIXIN_MP,
					openid);
			Date date = new Date();
			if (credential == null) {
				credential = new Credential();
				credential.setAccountId(account.getId());
				credential.setCreateAt(date);
				credential.setType(CredentialType.WEIXIN_MP);
				credential.setUid(openid);
				credential.setProduct(Product.YOOMATH);
				user = accountService.getUserByAccountId(account.getId());
			} else {
				user = accountService.getUserByAccountId(credential.getAccountId());
			}
			credential.setEndAt(new Date(System.currentTimeMillis() + form.getValidtime()));
			credential.setToken(form.getToken());
			credential.setUpdateAt(date);
			credential.setUserId(user.getId());

			// 调用新的凭证创建方法，金币成长值在方法内部处理
			credentialService.save(credential, false, user.getUserType());

			// 登录处理
			accountService.handleLogin(user, request, response);
			accountCacheService.invalidLoginWrongTime(Security.getToken());
			WebUtils.removeCookie(request, response, "BIND_TYPE");
			WebUtils.removeCookie(request, response, "USER_TYPE");

		} catch (AccountException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 取消绑定.
	 * 
	 * @return
	 */
	@RequestMapping(value = "unbind", method = {RequestMethod.POST})
	public Value unbind(HttpServletRequest request, HttpServletResponse response) {
		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType");
		if (credentialValue != null && credentialValue == CredentialType.WEIXIN_MP.getValue()) {
			WXUser wxuser = Security.getSession().getAttrSession().getObject("wxuser", WXUser.class);
			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, CredentialType.WEIXIN_MP,
					wxuser.getPersonid());
			if (null != credential) {
				credentialService.deleteCredential(credential.getId());
			}
		}

		if (Security.isLogin()) {
			thirdpartyService.logout(); // 第三方退出
			sessionService.offline(request, response);
		}

		WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERTYPE);
		return new Value();
	}
}
