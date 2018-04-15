package com.lanking.uxb.service.thirdparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.eduyun.Client;
import com.lanking.uxb.service.thirdparty.eduyun.resource.YunController;
import com.lanking.uxb.service.thirdparty.eduyun.response.YunUser;
import com.lanking.uxb.service.thirdparty.jsedu.response.JSEduUser;
import com.lanking.uxb.service.thirdparty.qq.resource.QQController;
import com.lanking.uxb.service.thirdparty.qq.response.QQUser;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduUser;
import com.lanking.uxb.service.thirdparty.school.jlms.resource.JLMSUser;
import com.lanking.uxb.service.thirdparty.weixin.response.WXUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;

@RestController("thirdpartyCommonController")
@RequestMapping("thirdparty")
public class CommonController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CredentialService credentialService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private QQController qqController;
	@Autowired
	private YunController yunController;
	@Autowired
	private Client yunClient;

	/**
	 * 获取第三方用户数据.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getUser")
	@RolesAllowed(anyone = true)
	private Value getThirdUser(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType");
		if (credentialValue != null) {
			CredentialType credentialType = CredentialType.findByValue(credentialValue);
			map.put("credentialType", credentialType);
			if (credentialType == CredentialType.EDUYUN) {
				YunUser yunUser = Security.getSession().getAttrSession().getObject("yunuser", YunUser.class);
				map.put("user", yunUser);
			} else if (credentialType == CredentialType.QQ) {
				QQUser qqUser = Security.getSession().getAttrSession().getObject("qquser", QQUser.class);
				map.put("user", qqUser);
			} else if (credentialType == CredentialType.WEIXIN_MP) {
				WXUser wxuser = Security.getSession().getAttrSession().getObject("wxuser", WXUser.class);
				map.put("user", wxuser);
			} else if (credentialType == CredentialType.SCEDU) {
				SCEduUser scUser = Security.getSession().getAttrSession().getObject("scuser", SCEduUser.class);
				map.put("user", scUser);
			} else if (credentialType == CredentialType.JSEDU) {
				JSEduUser jsUser = Security.getSession().getAttrSession().getObject("jsuser", JSEduUser.class);
				map.put("user", jsUser);
			} else if (credentialType == CredentialType.JLMS) {
				// 九龙中学
				JLMSUser jlmsUser = Security.getSession().getAttrSession().getObject("jlmsUser", JLMSUser.class);
				map.put("user", jlmsUser);
			}
		}
		return new Value(map);
	}

	/**
	 * 获取用户第三方凭证信息.
	 * 
	 * @param product
	 *            产品
	 * @param rpath
	 * @return
	 */
	@RequestMapping(value = "listThirds")
	public Value listUserThirdCredential(Product product, String rpath) {
		if (product == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> returnmap = new HashMap<String, Object>(3);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		Account account = accountService.getAccountByUserId(Security.getUserId());
		List<Credential> credentials = credentialService.listCredentials(product, account.getId());
		for (Credential credential : credentials) {
			if (credential.getType() != CredentialType.DEFAULT) {
				Map<String, Object> map = new HashMap<String, Object>(3);
				map.put("type", credential.getType());
				map.put("id", credential.getId());
				map.put("account", credential.getAccountId());
				map.put("name", credential.getName());
				list.add(map);
			}
		}
		returnmap.put("thirds", list);
		returnmap.put("qqurl", qqController.getCallbackUrl(product, rpath).getRet().toString());
		returnmap.put("yunurl", yunController.getSsoUrl(product, rpath).getRet().toString());
		return new Value(returnmap);
	}

	/**
	 * 解绑第三方.
	 * 
	 * @return
	 */
	@RequestMapping(value = "removeThird")
	public Value removeUserThirdCredential(Long id) {
		if (id == null) {
			return new Value(new MissingArgumentException("id"));
		}
		try {
			Credential credential = credentialService.get(id);
			if (credential == null) {
				return new Value(new EntityNotFoundException());
			}

			if (credential.getType() == CredentialType.EDUYUN) {
				// 教育云需要将教育云退出
				try {
					yunClient.logout(credential.getToken(), credential.getUid());
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			Account account = accountService.getAccount(Security.getAccountId());
			if (account.getPasswordStatus() == PasswordStatus.DISABLED) {
				return new Value(new AccountException(AccountException.ACCOUNT_NOPWD_CANNOT_DELETECREDENTIAL));
			}
			credentialService.deleteCredential(id);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}
}
