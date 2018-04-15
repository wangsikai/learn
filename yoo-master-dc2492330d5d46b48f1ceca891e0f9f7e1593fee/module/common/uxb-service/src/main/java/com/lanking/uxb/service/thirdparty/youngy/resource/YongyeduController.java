package com.lanking.uxb.service.thirdparty.youngy.resource;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.thirdparty.youngy.api.YoungyeduDataService;
import com.lanking.uxb.service.thirdparty.youngy.client.YoungyeduHttpClient;
import com.lanking.uxb.service.thirdparty.youngy.form.YoungyeduUser;
import com.lanking.uxb.service.user.api.AccountService;

/**
 * 融捷web端登录
 *
 * @since 3.0.4
 * @author xinyu.zhou
 */
@RestController
@RequestMapping("youngyedu")
@RolesAllowed(anyone = true)
public class YongyeduController {
	@Autowired
	@Qualifier(value = "youngyClient")
	private YoungyeduHttpClient client;
	@Autowired
	private YoungyeduDataService dataService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SessionService sessionService;

	@RequestMapping(value = "/{system}/callback")
	private ModelAndView callback(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("system") Integer system) {
		if (system == null)
			system = 0;
		system = 1;
		Product product = Product.findByValue(system); // 产品来源
		String ticket = request.getParameter("Ticket"); // 获得Ticket
		if (StringUtils.isNotBlank(ticket)) {
			try {
				Map<String, Object> retMap = client.queryUserByToken(ticket);
				if (retMap == null) {
					return new ModelAndView("redirect:/login.html");
				}

				YoungyeduUser teacherInfo = (YoungyeduUser) retMap.get("teacherInfo");
				if (teacherInfo == null) {
					return new ModelAndView("redirect:/login.html");
				}

				Integer code = dataService.findYoungyChannelCode();
				if (code == null) {
					return new ModelAndView("redirect:/login.html");
				}
				User user = dataService.createUser(teacherInfo, code);

				user.setLoginSource(product);
				accountService.handleLogin(user, request, response);

				// 缓存数据
				SessionPacket packet = new SessionPacket();
				packet.getAttrSession().setAttr("younyuser", user);
				packet.getAttrSession().setAttr("credentialType", CredentialType.YOUNGY_EDU.getValue());
				sessionService.refreshCurrentSession(packet, false);

				WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
				return new ModelAndView("redirect:/index.html");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ModelAndView("redirect:/login.html");
	}
}
