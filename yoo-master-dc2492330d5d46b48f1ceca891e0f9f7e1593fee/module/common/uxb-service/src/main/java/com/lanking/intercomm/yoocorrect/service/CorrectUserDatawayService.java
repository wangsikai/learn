package com.lanking.intercomm.yoocorrect.service;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.client.CorrectUserDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.AddCorrectUserRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserAuthRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserStatData;
import com.lanking.intercomm.yoocorrect.dto.ModifyCorrectUserRequest;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

@Component
public class CorrectUserDatawayService {

	@Autowired
	private CorrectUserDatawayClient correctUserDatawayClient;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TeacherService teacherService;

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;

	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;

	public CorrectUserResponse getCorrectUserByUxbUserId(Long uxbUserId, Boolean canUsedBalance) {
		CorrectUserResponse correctUser = null;
		Value value = correctUserDatawayClient.get(uxbUserId, canUsedBalance);
		if (value.getRet_code() == 0) {
			correctUser = null;
			if (value.getRet() == null) {
				Account account = accountService.getAccountByUserId(uxbUserId);
				User user = teacherService.get(uxbUserId);

				AddCorrectUserRequest request = new AddCorrectUserRequest();
				request.setName(user.getName());
				request.setAccountName(account.getName());
				request.setCorrectUserType(user.getUserType().getValue());
				request.setSource(2);
				request.setUserId(uxbUserId);
				value = correctUserDatawayClient.add(request);
			}
			correctUser = JSON.parseObject(value.getRet().toString(), CorrectUserResponse.class);
			return correctUser;
		} else {
			throw new ZuoyeException(value.getRet_code());
		}
	}

	public Value list(CorrectUserRequest request) {
		return correctUserDatawayClient.list(request);
	}

	@Transactional
	public Value add(AddCorrectUserRequest request) {
		if (accountService.getAccount(GetType.NAME, request.getAccountName()) != null) {
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EXIST));
		}
		RegisterForm form = new RegisterForm();
		form.setType(UserType.TEACHER);
		form.setName(request.getAccountName());
		form.setPassword(request.getPassword());
		form.setPwd(request.getPassword());
		form.setSource(Product.NULL);
		Account account = accountService.createAccount2(form, true);
		User user = accountService.getUserByAccountId(account.getId());
		// 默认设置为老师
		request.setCorrectUserType(0);
		request.setPhaseId(2l);
		request.setUserId(user.getId());
		// return CorrectUserDatawayService.add(request);
		try {
			return correctUserDatawayClient.add(request);
		} catch (Exception e) {
			return new Value(new ZuoyeException(CoreExceptionCode.SERVER_EX), e);
		}
	}

	@Transactional
	public Value modify(ModifyCorrectUserRequest request) {
		if (!request.getIsReset()) {
			User user = userRepo.get(request.getUserId());
			if (null != user) {
				// 修改uxb中的account的密码
				if (!TextUtils.isBlank(request.getPassWord())) {
					accountService.updatePassword(user.getAccountId(), request.getPassWord());
				}
				// 修改uxb中user和account的状态
				if (user.getStatus() != request.getStatus()) {
					user.setStatus(request.getStatus());
					userRepo.save(user);
					Account account = accountRepo.get(user.getAccountId());
					if (null != account) {
						account.setStatus(request.getStatus());
						accountRepo.save(account);
					}
				}
			}
		}
		try {
			return correctUserDatawayClient.modify(request);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public Value get2(Long correctUserId, UserType userType) {
		return correctUserDatawayClient.get2(correctUserId, userType);
	}

	public Value auth(CorrectUserAuthRequest request) {
		return correctUserDatawayClient.auth(request);
	}

	public Value queryAuthCorrectUsers() {
		return correctUserDatawayClient.queryAuthCorrectUsers();
	}

	/**
	 * 获取用户统计数据.
	 * 
	 * @param correctUserId
	 *            快批用户ID
	 * @return
	 */
	public CorrectUserStatData getUserStatData(Long correctUserId) {
		CorrectUserStatData userStatData = null;

		Value value = correctUserDatawayClient.getUserStatData(correctUserId);
		if (value != null && value.getRet() != null && value.getRet_code() == 0) {
			userStatData = JSON.parseObject(value.getRet().toString(), CorrectUserStatData.class);
		} else {
			throw new ZuoyeException(value == null ? CoreExceptionCode.SERVER_EX : value.getRet_code());
		}
		return userStatData;
	}
}
