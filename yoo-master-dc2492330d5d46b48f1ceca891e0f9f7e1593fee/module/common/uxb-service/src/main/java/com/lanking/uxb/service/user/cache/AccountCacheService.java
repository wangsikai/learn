package com.lanking.uxb.service.user.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Service
public class AccountCacheService extends AbstractCacheService {

	private ValueOperations<String, String> strOpt;
	private ValueOperations<String, Long> longOpt;
	private ValueOperations<String, List<AccountPasswordQuestion>> apqListOpt;

	/**
	 * 帐号激活key
	 */
	private static final String ACTIVE_SECURITY_KEY = "a";
	/**
	 * 重设密码key
	 */
	private static final String RESETPWD_SECURITY_KEY = "r";
	/**
	 * 登录错误次数key
	 */
	private static final String LOGIN_WRONG_TIME_KEY = "w";
	/**
	 * 密码找回key
	 */
	private static final String PASSWORD_FORGET_KEY = "p";
	/**
	 * 密码保护问题回答正确key
	 */
	private static final String ANSWER_RIGHT_KEY = "ar";
	/**
	 * 验证码key
	 */
	private static final String VERIFY_CODE_KEY = "vc";
	/**
	 * 重置密码验证码验证正确key
	 */
	private static final String RESET_PASSWORD_CODE_RIGHT_KEY = "rpr";
	/**
	 * 存储第一步设置的密保问题
	 */
	private static final String FIRST_ACCOUNT_PASSWORD_QUESTION_KEY = "f";

	/**
	 * 会员卡试错缓存key.
	 */
	private static final String ERROR_MEMBERCARD_TRY_KEY = "mc";

	@Override
	public String getNs() {
		return "a";
	}

	@Override
	public String getNsCn() {
		return "账户";
	}

	private String getActiveSecurityKey(String email) {
		return assemblyKey(ACTIVE_SECURITY_KEY, email);
	}

	public void setActiveSecurity(String email, String security) {
		strOpt.set(getActiveSecurityKey(email), security, 1, TimeUnit.DAYS);
	}

	public String getActiveSecurity(String email) {
		return strOpt.get(getActiveSecurityKey(email));
	}

	public void invalidActiveSecurity(String email) {
		getRedisTemplate().delete(getActiveSecurityKey(email));
	}

	private String getRestPwdSecurityKey(String email) {
		return assemblyKey(RESETPWD_SECURITY_KEY, email);
	}

	public void setRestPwdSecurity(String email, String security) {
		strOpt.set(getRestPwdSecurityKey(email), security, 1, TimeUnit.DAYS);
	}

	public String getRestPwdSecurity(String email) {
		return strOpt.get(getRestPwdSecurityKey(email));
	}

	public void invalidRestPwdSecurity(String email) {
		getRedisTemplate().delete(getRestPwdSecurityKey(email));
	}

	private String getLoginWrongTimeKey(String token) {
		return assemblyKey(LOGIN_WRONG_TIME_KEY, token);
	}

	public long incrLoginWrongTime(String token) {
		String key = getLoginWrongTimeKey(token);
		Long time = longOpt.get(key);
		long wrongTime = 1L;
		if (time != null) {
			wrongTime = time + 1;
		}
		longOpt.set(key, wrongTime);
		return wrongTime;

	}

	public Long getLoginWrongTime(String token) {
		Long time = longOpt.get(getLoginWrongTimeKey(token));
		if (time == null) {
			return 0L;
		}
		return time;
	}

	public void invalidLoginWrongTime(String token) {
		getRedisTemplate().delete(getLoginWrongTimeKey(token));
	}

	private String getPasswordForgetKey(String token) {
		return assemblyKey(PASSWORD_FORGET_KEY, token);
	}

	public void setPasswordForget(String token, String accountId) {
		strOpt.set(getPasswordForgetKey(token), accountId);
	}

	public String getPasswordForget(String token) {
		return strOpt.get(getPasswordForgetKey(token));
	}

	public void invalidPasswordForget(String token) {
		getRedisTemplate().delete(getPasswordForgetKey(token));
	}

	private String getAnswerRightKey(String token) {
		return assemblyKey(ANSWER_RIGHT_KEY, token);
	}

	public void setAnswerRight(String token, String accountId) {
		strOpt.set(getAnswerRightKey(token), accountId);
	}

	public String getAnswerRight(String token) {
		return strOpt.get(getAnswerRightKey(token));
	}

	public void invalidAnswerRight(String token) {
		getRedisTemplate().delete(getAnswerRightKey(token));
	}

	private String getVerifyCodeKey(String token, String target) {
		if (StringUtils.isBlank(target)) {
			return assemblyKey(VERIFY_CODE_KEY, token);
		} else {
			return assemblyKey(VERIFY_CODE_KEY, token, target);
		}

	}

	public void setVerifyCode(String token, String target, String code, int timeout, TimeUnit timeUnit) {
		strOpt.set(getVerifyCodeKey(token, target), code, timeout, timeUnit);
	}

	public String getVerifyCode(String token, String target) {
		return strOpt.get(getVerifyCodeKey(token, target));
	}

	public void invalidVerifyCode(String token, String target) {
		getRedisTemplate().delete(getVerifyCodeKey(token, target));
	}

	private String getResetPasswordCodeRightKey(String token) {
		return assemblyKey(RESET_PASSWORD_CODE_RIGHT_KEY, token);
	}

	public void setResetPasswordCodeRight(String token, String accountId) {
		strOpt.set(getResetPasswordCodeRightKey(token), accountId);
	}

	public String getResetPasswordCodeRight(String token) {
		return strOpt.get(getResetPasswordCodeRightKey(token));
	}

	public void invalidResetPasswordCodeRight(String token) {
		getRedisTemplate().delete(getResetPasswordCodeRightKey(token));
	}

	private String getFirstAccountPasswordQuestionKey(String token) {
		return assemblyKey(FIRST_ACCOUNT_PASSWORD_QUESTION_KEY, token);
	}

	public void setFirstAccountPasswordQuestion(String token, List<AccountPasswordQuestion> qpqs) {
		apqListOpt.set(getFirstAccountPasswordQuestionKey(token), qpqs);
	}

	public List<AccountPasswordQuestion> getFirstAccountPasswordQuestion(String token) {
		return apqListOpt.get(getFirstAccountPasswordQuestionKey(token));
	}

	public void invalidFirstAccountPasswordQuestion(String token) {
		getRedisTemplate().delete(getFirstAccountPasswordQuestionKey(token));
	}

	/**
	 * 获取会员卡激活错误时间缓存KEY
	 * 
	 * @param accountId
	 * @return
	 */
	private String getMemberpackageCardActiveKey(String accountId) {
		return assemblyKey(ERROR_MEMBERCARD_TRY_KEY, accountId);
	}

	/**
	 * 获取会员卡激活错误次数
	 * 
	 * @param accountId
	 * @return
	 */
	public long getMemberpackageCardActiveTime(String accountId) {
		Long count = longOpt.get(getMemberpackageCardActiveKey(accountId));
		return count == null ? 0 : count;
	}

	/**
	 * 设置会员卡激活错误次数
	 * 
	 * @param accountId
	 * @param count
	 *            错误次数
	 * @param minute
	 *            超时时长（分）
	 */
	public void setMemberpackageCardActiveTime(String accountId, long count, int minute) {
		longOpt.set(getMemberpackageCardActiveKey(accountId), count, minute, TimeUnit.MINUTES);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
		longOpt = getRedisTemplate().opsForValue();
		apqListOpt = getRedisTemplate().opsForValue();
	}
}
