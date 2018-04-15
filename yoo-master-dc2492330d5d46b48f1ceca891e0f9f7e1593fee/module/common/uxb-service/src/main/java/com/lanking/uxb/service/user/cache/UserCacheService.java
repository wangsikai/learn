package com.lanking.uxb.service.user.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@Service
public class UserCacheService extends AbstractCacheService {

	private ValueOperations<String, String> strOpt;

	private static final String SCHOOL_CERT_SMS_VERIFYCODE_KEY = "s";

	private static final String SCHOOL_CERT_CODE_KEY = "c";

	private String getSchoolCertSmsVerifyCodeKey(String token, String mobile) {
		return assemblyKey(SCHOOL_CERT_SMS_VERIFYCODE_KEY, token, mobile);
	}

	public String getSchoolCertSmsVerifyCode(String token, String mobile) {
		return strOpt.get(getSchoolCertSmsVerifyCodeKey(token, mobile));
	}

	public void setSchoolCertSmsVerifyCode(String token, String mobile, String code) {
		strOpt.set(getSchoolCertSmsVerifyCodeKey(token, mobile), code, 1, TimeUnit.MINUTES);
	}

	public void invalidSchoolCertSmsVerifyCode(String token, String mobile) {
		strOpt.set(getSchoolCertSmsVerifyCodeKey(token, mobile), StringUtils.EMPTY, 1, TimeUnit.SECONDS);
	}

	private String getSchoolCertCodeKey(String token) {
		return assemblyKey(SCHOOL_CERT_CODE_KEY, token);
	}

	public String getSchoolCertCode(String token) {
		return strOpt.get(getSchoolCertCodeKey(token));
	}

	public void setSchoolCertCode(String token, String schoolCode) {
		strOpt.set(getSchoolCertCodeKey(token), schoolCode);
	}

	public void invalidSchoolCertCode(String token) {
		strOpt.set(getSchoolCertCodeKey(token), StringUtils.EMPTY, 1, TimeUnit.SECONDS);
	}

	@Override
	public String getNs() {
		return "u";
	}

	@Override
	public String getNsCn() {
		return "用户";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
	}

}
