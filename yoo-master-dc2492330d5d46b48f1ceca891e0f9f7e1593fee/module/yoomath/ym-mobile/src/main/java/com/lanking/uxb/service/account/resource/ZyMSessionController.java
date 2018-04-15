package com.lanking.uxb.service.account.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.value.VMSession;
import com.lanking.uxb.service.account.value.VMStuSession;
import com.lanking.uxb.service.account.value.VMTeaSession;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 悠数学移动端(会话相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/session")
public class ZyMSessionController {

	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private ZyHomeworkClassService hkClassService;

	/**
	 * 获取当前会话信息
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */

	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "get" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value get() {
		VMSession session = null;
		if (Security.isLogin()) {
			VUserProfile up = userProfileConvert.get(Security.getUserId());
			// 凭证列表
			List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
			List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, up.getAccount().getId());
			for (Credential credential : credentials) {
				credentialTypes.add(credential.getType());
			}
			up.getAccount().setCredentialTypes(credentialTypes);
			if (Security.getUserType() == UserType.STUDENT) {
				session = new VMStuSession(up);
				((VMStuSession) session).setNeedSetTextbook(up.getS().getTextbook() == null);
			} else if (Security.getUserType() == UserType.TEACHER) {
				session = new VMTeaSession(up);
				((VMTeaSession) session).setNeedCreateClass(hkClassService.currentCount(Security.getUserId()) <= 0);
			}
		} else {
			session = new VMSession();
		}
		return new Value(session);
	}
}
