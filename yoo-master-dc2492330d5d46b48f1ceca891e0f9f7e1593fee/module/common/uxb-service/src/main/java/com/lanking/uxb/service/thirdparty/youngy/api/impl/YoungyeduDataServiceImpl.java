package com.lanking.uxb.service.thirdparty.youngy.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.thirdparty.youngy.api.YoungyeduDataService;
import com.lanking.uxb.service.thirdparty.youngy.form.YoungyeduUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
@Service
@Transactional(readOnly = true)
public class YoungyeduDataServiceImpl implements YoungyeduDataService {
	@Autowired
	@Qualifier("UserChannelRepo")
	private Repo<UserChannel, Long> userChannelRepo;

	@Autowired
	private CredentialService credentialService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkStudentClazzService studentClazzService;

	@Override
	@Transactional
	public User createUser(YoungyeduUser user, int code) {
		if (user == null) {
			return null;
		}

		Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, CredentialType.YOUNGY_EDU,
				user.getUserId());

		// 表明此用户之前并没有与系统进行关联,现进行关联
		if (credential == null) {
			RegisterForm form = new RegisterForm();
			form.setRealName(user.getRealName());
			// 临时处理登录名
			form.setName("yy" + user.getName());
			form.setNickname(user.getName());
			form.setCredentialType(CredentialType.YOUNGY_EDU);
			if (user.getUserType() == 1) {
				form.setType(UserType.TEACHER);
			} else {
				form.setType(UserType.STUDENT);
			}
			if (user.getPhase() == 2) {
				form.setSubjectCode(202);
			} else {
				form.setSubjectCode(302);
			}

			form.setPhaseCode(user.getPhase());
			form.setThirdName(user.getName());
			form.setSource(Product.YOOMATH);
			form.setChannelCode(code);

			Account account = accountService.createAccount2(form, false);

			credential = new Credential();
			credential.setAccountId(account.getId());
			credential.setCreateAt(new Date());
			credential.setUid(user.getUserId());
			credential.setName(form.getThirdName());
			credential.setProduct(Product.YOOMATH);
			credential.setType(CredentialType.YOUNGY_EDU);
			credential.setUpdateAt(new Date());

			User saveUser = accountService.getUserByAccountId(account.getId());
			credential.setUserId(saveUser.getId());

			credentialService.save(credential);

			return saveUser;
		}

		return accountService.getUserByAccountId(credential.getAccountId());
	}

	@Override
	public Integer findYoungyChannelCode() {
		UserChannel userChannel = userChannelRepo.find("$findYoungyCode", Params.param()).get();
		return userChannel == null ? null : userChannel.getCode();
	}

}
