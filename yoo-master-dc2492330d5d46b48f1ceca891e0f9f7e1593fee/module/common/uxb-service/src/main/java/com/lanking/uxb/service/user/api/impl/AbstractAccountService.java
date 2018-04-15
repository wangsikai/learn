package com.lanking.uxb.service.user.api.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.Parent;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.ex.core.UnSupportedOperationException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.bean.Userable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.session.api.impl.AbstractSessionUserService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.api.UserTypeService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public abstract class AbstractAccountService extends AbstractSessionUserService
		implements AccountService, UserTypeService {

	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;
	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;
	@Autowired
	@Qualifier("TeacherRepo")
	Repo<Teacher, Long> teacherRepo;
	@Autowired
	@Qualifier("StudentRepo")
	Repo<Student, Long> studentRepo;
	@Autowired
	@Qualifier("ParentRepo")
	Repo<Parent, Long> parentRepo;

	@Autowired
	@Qualifier("userService")
	UserService userService;
	@Autowired
	@Qualifier("accountService")
	AccountService accountService;
	@Autowired
	AccountCacheService accountCacheService;

	@Override
	public void handleLogin() {
		super.handleLogin();
	}

	@Transactional(readOnly = true)
	@Override
	public Userable getUser(long id) {
		return userRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Set<Long> getRoles(long id) {
		return Sets.newHashSet();
	}

	@Transactional(readOnly = true)
	@Override
	public Account getAccount(long accountId) {
		return accountRepo.get(accountId);
	}

	@Override
	public Account getAccountByUserId(long userId) {
		User user = accountService.getUserByUserId(userId);
		return accountRepo.get(user.getAccountId());
	}

	@Override
	public Map<Long, Account> getAccountByUserIds(Set<Long> userIds) {
		Map<Long, User> users = userService.getUsers(userIds);
		Map<Long, Long> userIdsAccountIds = Maps.newHashMap();
		Set<Long> accountIds = Sets.newHashSet();
		for (Long key : users.keySet()) {
			accountIds.add(users.get(key).getAccountId());
			userIdsAccountIds.put(key, users.get(key).getAccountId());
		}
		Map<Long, Account> accounts = accountRepo.mget(accountIds);
		Map<Long, Account> userIdsAccount = Maps.newHashMap();
		for (Long key : userIdsAccountIds.keySet()) {
			userIdsAccount.put(key, accounts.get(userIdsAccountIds.get(key)));
		}
		return userIdsAccount;
	}

	@Transactional(readOnly = true)
	@Override
	public Account getAccount(GetType getType, String value) {
		if (getType == GetType.PASSWORD) {
			throw new UnSupportedOperationException();
		}
		List<Account> accounts = accountRepo.find("SELECT * FROM account WHERE " + getType.getName() + " = ?", value)
				.list();
		if (accounts.size() == 1) {
			return accounts.get(0);
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public int getMaxAccountNameNum(String accountName) {
		List<Long> list = (List<Long>) accountRepo
				.find("SELECT count(t.name) as num FROM account t WHERE t.name REGEXP '^" + accountName
						+ "([0-9]*)$' ORDER BY t.name DESC")
				.list(Long.class);
		if (list != null) {
			// String num = maxNames.get(0).replace(accountName, "");
			// if (StringUtils.isNotBlank(num)) {
			// return Integer.parseInt(num) + 1;
			// } else {
			// return 1;
			// }
			return Integer.parseInt(list.get(0).toString()) + 1;
		} else {
			return 0;
		}
	}

	@Transactional
	@Override
	public Account updateAccountStatus(String email, Status status) {
		Account account = getAccount(GetType.EMAIL, email);
		account.setStatus(status);
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public void updateActivationStatus(long id, Status activationStatus) {
		Account account = getAccount(id);
		account.setActivationStatus(activationStatus);
		if (account.getActivationStatus() == Status.DISABLED) {
			accountRepo.save(account);
		}
	}

	@Transactional
	@Override
	public Account updateAccountMobileStatus(String mobile, Status status, Long userId) {
		Account account = getAccountByUserId(userId);
		account.setMobile(mobile);
		account.setMobileStatus(status);
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public Account updateAccountEmailStatus(String email, Status status, Long userId) {
		Account account = getAccountByUserId(userId);
		account.setEmailStatus(status);
		account.setEmail(email);
		return accountRepo.save(account);
	}

	@Override
	public Account createAccount(RegisterForm form) throws AccountException {
		if (form == null) {
			throw new IllegalArgException();
		}
		if (StringUtils.isBlank(form.getName())) {
			throw new AccountException(AccountException.ACCOUNT_NAME_NULL);
		}
		ValidateUtils.validateName(form.getName());
		if (StringUtils.isBlank(form.getPassword())) {
			throw new AccountException(AccountException.ACCOUNT_PASSWORD_NULL);
		}
		if (!form.getPassword().equals(form.getPwd())) {
			throw new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT);
		}
		ValidateUtils.validatePassword(form.getPassword());
		Account account = getAccount(GetType.NAME, form.getName());
		if (account != null) {
			throw new AccountException(AccountException.ACCOUNT_NAME_EXIST);
		}
		return null;
	}

	/**
	 * @since yooshare v2.3.2 可以没有手机邮箱
	 */
	@Override
	public Account createAccount2(RegisterForm form, boolean hasPassword) throws AccountException {
		if (form == null || form.getSource() == null) {
			throw new IllegalArgException();
		}
		// 公共的check
		if (StringUtils.isBlank(form.getName())) {
			throw new AccountException(AccountException.ACCOUNT_NAME_NULL);
		}
		ValidateUtils.validateName(form.getName());
		if (hasPassword) {
			if (StringUtils.isBlank(form.getPassword())) {
				throw new AccountException(AccountException.ACCOUNT_PASSWORD_NULL);
			}
			if (!form.getPassword().equals(form.getPwd())) {
				throw new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT);
			}
			ValidateUtils.validatePassword(form.getPassword());
		}
		Account account = getAccount(GetType.NAME, form.getName());

		if (account != null) {
			throw new AccountException(AccountException.ACCOUNT_NAME_EXIST);
		}
		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public User getUserByAccountId(long accountId) {
		List<User> users = userRepo.find("$getUserByAccountId", Params.param("accountId", accountId)).list();
		if (users.size() > 1) {
			throw new ServerException();
		}
		return users.get(0);
	}

	@Override
	public User getUserByUserId(long userId) {
		Userable u = getUser(userId);
		if (u == null) {
			return null;
		}
		return (User) u;
	}

	@Transactional
	@Override
	public Account updatePassword(String email, String password) {
		Account account = getAccount(GetType.EMAIL, email);
		account.setPassword(Codecs.md5Hex(password.getBytes()));
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public Account updatePassword(long accountId, String password) {
		Account account = getAccount(accountId);
		account.setPassword(Codecs.md5Hex(password.getBytes()));
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public Account updatePassword(Long accountId, String password, Integer strength) {
		Account account = getAccount(accountId);
		account.setPassword(Codecs.md5Hex(password.getBytes()));
		account.setStrength(passwordStrength(password));
		account.setPasswordStatus(PasswordStatus.ENABLED);
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public Account updateMobileByUserId(long userId, String mobile) {
		Account account = getAccountByUserId(userId);
		account.setMobile(mobile);
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public Account updateNameByUserId(long userId, String name) {
		Account account = getAccountByUserId(userId);
		account.setName(name);
		return accountRepo.save(account);
	}

	@Transactional
	@Override
	public Account updatePasswordMobile(long accountId, String mobile) {
		Account p = accountRepo.get(accountId);
		p.setPasswordMobile(mobile);
		return accountRepo.save(p);
	}

	@Transactional
	@Override
	public Account updateName(long accountId, String name) {
		Account p = accountRepo.get(accountId);
		p.setName(name);
		p.setNameUpdateStatus(Status.DISABLED);
		return accountRepo.save(p);
	}

	@Override
	public String generateName(UserType userType, GetType registerType) {
		String prefix = userType == UserType.STUDENT ? "Y_s" : "Y_t";
		if (registerType == GetType.MOBILE) {
			prefix += "_m";
		}
		String name = prefix + RandomStringUtils.random(6, true, true);
		Account account = accountService.getAccount(GetType.NAME, name);
		if (account != null) {
			name = null;
			for (int i = 0; i < 100; i++) {
				name = prefix + RandomStringUtils.random(6, true, true);
				account = accountService.getAccount(GetType.NAME, name);
				if (account == null) {
					break;
				} else {
					name = null;
				}
			}
		}
		return name;
	}

	@Transactional
	@Override
	public void delete(long accountId) {
		User user = getUserByAccountId(accountId);
		if (user.getUserType() == UserType.TEACHER) {
			teacherRepo.delete(teacherRepo.get(user.getId()));
		} else if (user.getUserType() == UserType.STUDENT) {
			studentRepo.delete(studentRepo.get(user.getId()));
		} else if (user.getUserType() == UserType.PARENT) {
			parentRepo.delete(parentRepo.get(user.getId()));
		}
		userRepo.delete(userRepo.get(user.getId()));
		accountRepo.delete(accountRepo.get(accountId));
	}

	@Override
	@Transactional
	public Account updatePasswordStatus(long accountId, PasswordStatus passwordStatus) {
		Account account = accountRepo.get(accountId);
		account.setPasswordStatus(passwordStatus);

		return accountRepo.save(account);
	}

	public int passwordStrength(String password) {
		int strength = 0;
		Pattern p = Pattern.compile("[a-z]");
		Matcher m = p.matcher(password);
		if (m.find()) {
			strength++;
		}
		p = Pattern.compile("[0-9]");
		m = p.matcher(password);
		if (m.find()) {
			strength++;
		}
		p = Pattern.compile("(.[^a-z0-9])");
		m = p.matcher(password);
		if (m.find()) {
			strength++;
		}
		return strength;
	}

}
