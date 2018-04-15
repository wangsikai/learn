package com.lanking.uxb.service.thirdparty.eduyun.resource;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.TokenInfo;
import com.lanking.uxb.service.thirdparty.eduyun.Client;
import com.lanking.uxb.service.thirdparty.eduyun.ex.EduyunException;
import com.lanking.uxb.service.thirdparty.eduyun.response.YunUser;
import com.lanking.uxb.service.thirdparty.eduyun.response.YunUserType;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.user.resource.Register2EduYunController;

/**
 * 教育云.
 * 
 * @since v2.1
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年7月2日
 */
@RestController
@RequestMapping("eduyun")
@RolesAllowed(anyone = true)
public class YunController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private Client client;

	@Autowired
	private SessionService sessionService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private Register2EduYunController register2EduYunController;

	@RequestMapping(value = "ssoUrl")
	public Value getSsoUrl(Product product, String rpath) {
		return new Value(client.getSsoUrl(product, rpath));
	}

	@RequestMapping(value = "/{system}/callback")
	private ModelAndView callback(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("system") String systemStr) {
		int system = 0;
		String rpath = ""; // 工程上下文路径
		Product product = Product.YOOMATH;
		if (systemStr.indexOf("_") != -1) {
			if (systemStr.split("_").length >= 2) {
				rpath = systemStr.split("_")[1].replace("@", "/");
			}
			try {
				system = Integer.parseInt(systemStr.split("_")[0]);
				product = Product.findByValue(system); // 产品来源
			} catch (NumberFormatException e) {
			}
		}
		return this.yoomathChannel(request, response, product, rpath);
	}

	/**
	 * 悠数学流程.
	 * 
	 * @return
	 */
	private ModelAndView yoomathChannel(HttpServletRequest request, HttpServletResponse response, Product product,
			String rpath) {
		String ticket = request.getParameter("ticket");
		String usessionid = "";
		try {
			usessionid = client.ticketValidate(ticket);
			TokenInfo tokenInfo = client.getaccesstoken(product);
			String token = tokenInfo.getToken();

			if (StringUtils.isNotBlank(token)) {
				YunUser yunUser = client.getuserinfo(token, usessionid);
				yunUser.setOaccount(yunUser.getAccount());
				yunUser.setTokenInfo(tokenInfo);
				yunUser.setUsessionid(usessionid);
				YunUserType type = YunUserType.findByValue(yunUser.getUsertype());
				//
				// if (type == YunUserType.TEACHER
				// || type == YunUserType.SCHOOLSTAFF
				// || type == YunUserType.ORGSTAFF
				// || type == YunUserType.EXPERTS
				// || (product != Product.YOOSHARE && (type ==
				// YunUserType.STUDENT || type == YunUserType.LEARNER))) {
				Credential credential = credentialService.getCredentialByPersonId(product, CredentialType.EDUYUN,
						yunUser.getPersonid());
				if (credential == null) {
					// if (Security.isLogin()) {
					// Long userId = Security.getUserId();
					// Account account =
					// accountService.getAccountByUserId(userId);
					//
					// // 如果教育云账户类型与本地账户类型不一致
					// // if (Security.getUserType() == UserType.TEACHER
					// // && (type == YunUserType.STUDENT || type ==
					// // YunUserType.LEARNER)) {
					// if (Security.getUserType() == UserType.TEACHER && type ==
					// YunUserType.STUDENT) {
					// client.logout(token, usessionid);
					// return new ModelAndView(
					// "redirect: " + rpath +
					// "/template/ucenter/bind-r.html?f=2&t=EDUYUN");
					// } else if (Security.getUserType() == UserType.STUDENT &&
					// type != YunUserType.STUDENT) {
					// client.logout(token, usessionid);
					// return new ModelAndView("redirect:" + rpath +
					// "/template/ucenter/bind-r.html?f=3&t=EDUYUN");
					// }
					//
					// // 绑定账户，创建凭证
					// credential = new Credential();
					// Date date = new Date();
					// credential.setAccountId(account.getId());
					// credential.setCreateAt(new Date());
					// credential.setType(CredentialType.EDUYUN);
					// credential.setUid(yunUser.getPersonid());
					// credential.setToken(token);
					// credential.setUpdateAt(date);
					// credential.setName(yunUser.getAccount());
					// credential.setProduct(product);
					// credentialService.save(credential);
					// return new ModelAndView("redirect:" + rpath +
					// "/template/ucenter/bind-r.html?f=1&t=EDUYUN");
					// } else {
					int max = accountService.getMaxAccountNameNum(yunUser.getAccount());
					if (max > 0) {
						yunUser.setAccount(yunUser.getAccount() + max);
					}

					// 缓存数据
					SessionPacket packet = new SessionPacket();
					packet.getAttrSession().setAttr("yunuser", yunUser);
					packet.getAttrSession().setAttr("credentialType", CredentialType.EDUYUN.getValue());
					sessionService.refreshCurrentSession(packet, false);
					// if (type == YunUserType.STUDENT || type ==
					// YunUserType.LEARNER) {
					if (type == YunUserType.STUDENT) {
						RegisterForm form = new RegisterForm();
						form.setCredentialType(CredentialType.EDUYUN);
						form.setType(UserType.STUDENT);
						form.setName(yunUser.getAccount());
						form.setThirdName(yunUser.getOaccount());
						form.setRealName(yunUser.getOaccount());
						form.setToken(yunUser.getTokenInfo().getToken());
						form.setUid(yunUser.getPersonid());
						form.setSource(Product.YOOMATH);
						form.setEndTime(yunUser.getTokenInfo().getValidtime());
						Value regValue = register2EduYunController.register(form, request, response); // 自动注册

						WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
						return new ModelAndView("redirect:" + rpath + "/index.html");
					} else {
						// 教师提示页
						return new ModelAndView("redirect:" + rpath + "/thirdparty/welcome-yun-t.html");
					}
					// }
				} else {
					// if (Security.isLogin()) {
					// Long userId = Security.getUserId();
					// Account account =
					// accountService.getAccountByUserId(userId);
					// if (credential != null &&
					// credential.getAccountId().longValue() !=
					// account.getId().longValue()) {
					// // 已被其他账户绑定
					// return new ModelAndView("redirect:" + rpath +
					// "/template/ucenter/bind-r.html?f=0&t=EDUYUN");
					// }
					// }

					User user = accountService.getUserByAccountId(credential.getAccountId());
					user.setLoginSource(product);
					accountService.handleLogin(user, request, response);
					// 缓存数据
					SessionPacket packet = new SessionPacket();
					packet.getAttrSession().setAttr("yunuser", yunUser);
					packet.getAttrSession().setAttr("credentialType", CredentialType.EDUYUN.getValue());
					sessionService.refreshCurrentSession(packet, false);

					WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
					return new ModelAndView("redirect:" + rpath + "/index.html");
				}
				// } else {
				// // 教育云退出
				// client.logout(token, usessionid);
				// Map<String, Object> map = new HashMap<String, Object>(2);
				// map.put("t", type);
				// map.put("p", product);
				// return new ModelAndView("redirect:/thirdparty/eduyun-s.html",
				// map);
				// }
			}
		} catch (IOException | ParseException | DocumentException | EduyunException e) {
			logger.error(e.getMessage(), e);
		}
		return new ModelAndView("redirect:" + rpath + "/login.html");
	}

	/**
	 * 悠学流程.
	 * 
	 * @return
	 */
	private ModelAndView yooshareChannel(HttpServletRequest request, HttpServletResponse response, Product product) {
		String ticket = request.getParameter("ticket");
		String usessionid = "";
		try {
			usessionid = client.ticketValidate(ticket);
			TokenInfo tokenInfo = client.getaccesstoken(product);
			String token = tokenInfo.getToken();

			if (StringUtils.isNotBlank(token)) {
				YunUser yunUser = client.getuserinfo(token, usessionid);
				yunUser.setOaccount(yunUser.getAccount());
				yunUser.setTokenInfo(tokenInfo);
				yunUser.setUsessionid(usessionid);
				if (yunUser.getUserlogolist() != null && yunUser.getUserlogolist().size() > 0) {
					yunUser.setLogourl(client.host + "/"
							+ yunUser.getUserlogolist().get(yunUser.getUserlogolist().size() - 1).getLogourl());
				}
				YunUserType type = YunUserType.findByValue(yunUser.getUsertype());

				if (type == YunUserType.TEACHER || type == YunUserType.SCHOOLSTAFF || type == YunUserType.ORGSTAFF
						|| type == YunUserType.EXPERTS) {
					// 首先获取教育云凭证
					Credential credential = credentialService.getCredentialByPersonId(product, CredentialType.EDUYUN,
							yunUser.getPersonid());
					if (credential == null) {
						if (Security.isLogin()) {
							// 已登录，表示在用户界面进行绑定操作
							Long userId = Security.getUserId();
							Account account = accountService.getAccountByUserId(userId);

							// 如果教育云账户类型与本地账户类型不一致
							if (Security.getUserType() == UserType.TEACHER
									&& (type == YunUserType.STUDENT || type == YunUserType.LEARNER)) {
								client.logout(token, usessionid);
								return new ModelAndView("redirect:/template/ucenter/bind-r.html?f=2&t=EDUYUN");
							} else if (Security.getUserType() == UserType.STUDENT
									&& !(type == YunUserType.STUDENT || type == YunUserType.LEARNER)) {
								client.logout(token, usessionid);
								return new ModelAndView("redirect:/template/ucenter/bind-r.html?f=3&t=EDUYUN");
							}

							// 绑定账户，创建凭证
							credential = new Credential();
							Date date = new Date();
							credential.setAccountId(account.getId());
							credential.setCreateAt(new Date());
							credential.setType(CredentialType.EDUYUN);
							credential.setUid(yunUser.getPersonid());
							credential.setToken(token);
							credential.setUpdateAt(date);
							credential.setName(yunUser.getAccount());
							credential.setProduct(product);
							credentialService.save(credential);
							return new ModelAndView("redirect:/template/ucenter/bind-r.html?f=1&t=EDUYUN");
						} else {
							// 未登录，需要跳转至完善教师学科阶段界面，创建教育云凭证
							int max = accountService.getMaxAccountNameNum(yunUser.getAccount());
							if (max > 0) {
								yunUser.setAccount(yunUser.getAccount() + max);
							}

							// 缓存数据
							SessionPacket packet = new SessionPacket();
							packet.getAttrSession().setAttr("yunuser", yunUser);
							packet.getAttrSession().setAttr("credentialType", CredentialType.EDUYUN.getValue());
							sessionService.refreshCurrentSession(packet, false);
							return new ModelAndView("redirect:/thirdparty/welcome.html");
						}
					} else {
						if (Security.isLogin()) {
							// 如果有教育云凭证，判断当前用户与绑定的教育云凭证之间的关系
							Long userId = Security.getUserId();
							Account account = accountService.getAccountByUserId(userId);
							if (credential != null
									&& credential.getAccountId().longValue() != account.getId().longValue()) {
								// 已被其他账户绑定
								return new ModelAndView("redirect:/template/ucenter/bind-r.html?f=0&t=EDUYUN");
							}
						}

						// 登录系统
						User user = accountService.getUserByAccountId(credential.getAccountId());
						user.setLoginSource(product);
						accountService.handleLogin(user, request, response);
						// 缓存数据
						SessionPacket packet = new SessionPacket();
						packet.getAttrSession().setAttr("yunuser", yunUser);
						packet.getAttrSession().setAttr("credentialType", CredentialType.EDUYUN.getValue());
						sessionService.refreshCurrentSession(packet, false);

						WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
						return new ModelAndView("redirect:/index.html");
					}
				} else {
					// 学生登录、家长登录
					// 教育云退出
					client.logout(token, usessionid);
					Map<String, Object> map = new HashMap<String, Object>(2);
					map.put("t", type);
					map.put("p", product);
					return new ModelAndView("redirect:/thirdparty/eduyun-s.html", map);
				}
			}
		} catch (IOException | ParseException | DocumentException | EduyunException e) {
			logger.error(e.getMessage(), e);
		}
		return new ModelAndView("redirect:/login.html");
	}

	@RequestMapping(value = "test")
	private ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("redirect:/thirdparty/eduyun-s.html");
	}
}
