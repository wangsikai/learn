package com.lanking.uxb.service.thirdparty.scedu.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.scedu.client.SCClient;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.user.resource.Register2SCEduController;

/**
 * 四川教育平台.
 * 
 * @since v2.1
 * @author wlche
 */
@RestController
@RequestMapping("scedu")
@RolesAllowed(anyone = true)
public class SCController {
	@Autowired
	private SCClient client;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SessionService sessionService;

	@Autowired
	private Register2SCEduController register2SCEduController;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookService textbookService;

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
				SCEduUser user = client.getuserinfo(product, ticket);
				if (user.getType() != null && user.getType() == 26) {
					user.setStudent(client.getStudent(product, ticket));
				} else {
					user.setTeacher(client.getTeacher(product, ticket));
				}
				// SCEduUser user = new SCEduUser();
				// user.setAccount("scstudent");
				// user.setName("scstudent");
				// user.setOaccount("scstudent");
				// user.setPersonid("scstudent");
				// user.setType(26);
				// SCEduStudent student = new SCEduStudent();
				// List<SCEduClass> clazzes = new ArrayList<SCEduClass>();
				// SCEduClass sc = new SCEduClass();
				// sc.setClassId("scstudent-class");
				// sc.setClassname("scstudent-class");
				// sc.setGradename("高中");
				// sc.setSection(2);
				// student.setClazzes(clazzes);
				// student.setSchoolId("scstudent-elanking");
				// student.setSchoolOrgName("蓝舰测试学校");
				// user.setStudent(student);
				// TokenInfo tokenInfo = new TokenInfo();
				// tokenInfo.setToken("XXX");
				// tokenInfo.setValidtime(0L);
				// user.setTokenInfo(tokenInfo);
				Credential credential = credentialService.getCredentialByPersonId(product, CredentialType.SCEDU,
						user.getPersonid());
				if (credential == null) {
					// 不再查询数据库获得序号，使用四川account+毫秒数+随机数
					// int max =
					// accountService.getMaxAccountNameNum(user.getAccount());
					// if (max > 0) {
					// user.setAccount(user.getAccount() + (max + 1));
					// }
					user.setAccount("s" + System.currentTimeMillis() + (int) (Math.random() * 99));

					// 缓存数据
					SessionPacket packet = new SessionPacket();
					packet.getAttrSession().setAttr("scuser", user);
					packet.getAttrSession().setAttr("credentialType", CredentialType.SCEDU.getValue());
					sessionService.refreshCurrentSession(packet, false);
					if (user.getType() == 26) {
						RegisterForm form = new RegisterForm();
						form.setCredentialType(CredentialType.SCEDU);
						form.setType(UserType.STUDENT);
						form.setName(user.getAccount());
						form.setThirdName(user.getOaccount());
						form.setRealName(user.getOaccount());
						form.setToken(user.getTokenInfo().getToken());
						form.setUid(user.getPersonid());
						form.setSource(Product.YOOMATH);
						Value regValue = register2SCEduController.register(form, request, response); // 自动注册

						WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
						return new ModelAndView("redirect:/index.html");
					} else {
						RegisterForm form = new RegisterForm();
						form.setCredentialType(CredentialType.SCEDU);
						form.setType(UserType.TEACHER);
						List<SCEduClass> clazzs = user.getTeacher().getClazzes();
						if (clazzs == null || clazzs.size() == 0) {
							return new ModelAndView("redirect:/third-improve.html");
						}
						Integer section = clazzs.get(0).getSection();
						// section = 3;
						if (section != 3 && section != 4) {
							return new ModelAndView("redirect:/third-improve.html");
						}
						form.setPhaseCode(section == 3 ? 2 : 3);
						form.setSubjectCode(section == 3 ? 202 : 302);
						form.setName(user.getAccount());
						form.setThirdName(user.getOaccount());
						form.setRealName(user.getOaccount());
						form.setToken(user.getTokenInfo().getToken());
						form.setUid(user.getPersonid());
						form.setSource(Product.YOOMATH);
						Value regValue = register2SCEduController.register(form, request, response); // 自动注册

						// 处理教材选择，初中，选择北师大版22，高中，人教A版13，13330201
						int textbookCode = form.getPhaseCode() == 2 ? 22220201 : 13330201;
						Textbook tb = textbookService.get(textbookCode);
						teacherService.updateCategory(Security.getUserId(), tb.getCategoryCode(), textbookCode);

						WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
						return new ModelAndView("redirect:/index.html");
					}
				} else {
					// 缓存数据
					User u = accountService.getUserByAccountId(credential.getAccountId());
					u.setLoginSource(product);
					accountService.handleLogin(u, request, response);

					// 缓存数据
					SessionPacket packet = new SessionPacket();
					packet.getAttrSession().setAttr("scuser", user);
					packet.getAttrSession().setAttr("credentialType", CredentialType.SCEDU.getValue());
					sessionService.refreshCurrentSession(packet, false);

					WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
					return new ModelAndView("redirect:/index.html");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ModelAndView("redirect:/login.html");
	}
}
