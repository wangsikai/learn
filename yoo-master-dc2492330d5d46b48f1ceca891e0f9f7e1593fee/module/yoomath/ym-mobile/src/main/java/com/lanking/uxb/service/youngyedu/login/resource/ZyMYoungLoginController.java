package com.lanking.uxb.service.youngyedu.login.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.value.VMSession;
import com.lanking.uxb.service.account.value.VMStuSession;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserChannelService;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduHttpClient;
import com.lanking.uxb.service.youngyedu.syncdata.api.YoungyeduSyncDataService;
import com.lanking.uxb.service.youngyedu.syncdata.form.YoungyeduUser;

/**
 * 融捷登录后，根据token登录悠数学系统
 *
 * @author xinyu.zhou
 * @since 3.0.3
 */
@RestController
@RequestMapping(value = "router/youngyedu/ym")
public class ZyMYoungLoginController {

	@Autowired
	private YoungyeduHttpClient client;
	@Autowired
	private YoungyeduSyncDataService syncDataService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private UserChannelService ucService;

	/**
	 * 客户端先调用融捷的登录数据接口。 返回token给此接口，此接口再调用融捷的服务，得到用户相关信息，与本地进行比对。
	 *
	 * @param token
	 *            登录用的token
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "login", method = { RequestMethod.GET, RequestMethod.POST })
	public Value login(String token, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isBlank(token)) {
			return new Value(new IllegalArgException());
		}

		// 根据token去取数据后，对现有的用户进行处理，若此用户并未在本库中，则进行创建操作。
		Map<String, Object> retMap = client.queryUserByToken(token);
		if (retMap == null) {
			return new Value(new IllegalArgException());
		}
		YoungyeduUser studentInfo = (YoungyeduUser) retMap.get("studentInfo");
		// 用户信息返回不正确
		if (studentInfo == null) {
			return new Value(new IllegalArgException());
		}
		UserChannel uc = ucService.findByName(UserChannel.YOUNGEDU);
		Integer code = uc == null ? null : uc.getCode();
		if (code == null) {
			return new Value(new IllegalArgException());
		}
		User user = syncDataService.createUser(studentInfo, code);

		accountService.handleLogin(user, request, response);

		// 登录成功后返回当前会话信息
		VMSession session = null;

		VUserProfile up = userProfileConvert.to(user);
		// 凭证列表
		List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
		List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, up.getAccount().getId());
		for (Credential credential : credentials) {
			credentialTypes.add(credential.getType());
		}
		up.getAccount().setCredentialTypes(credentialTypes);

		if (up.getType() == UserType.STUDENT) {
			session = new VMStuSession(up);
			((VMStuSession) session).setNeedSetTextbook(up.getS().getTextbook() == null);
		}
		return new Value(session);
	}
}
