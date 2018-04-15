package com.lanking.uxb.service.session.cache;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.session.form.VerifyCode;

@Service
public final class VerifyCodeCacheService extends AbstractCacheService {

	private ValueOperations<String, Object> validateCodeOpt;

	public String generateVerifyCode() {
		String verifyCode = RandomStringUtils.random(4, true, true);
		validateCodeOpt.set(Security.getToken(), verifyCode);
		return verifyCode;
	}

	@SuppressWarnings("unchecked")
	private String currentVerifyCode() {
		String verifyCode = (String) validateCodeOpt.get(Security.getToken());
		getRedisTemplate().delete(Security.getToken());
		return verifyCode;
	}

	public boolean checkVerifyCode(String code) {
		String verifyCode = currentVerifyCode();
		if (StringUtils.isBlank(code) || StringUtils.isBlank(verifyCode)) {
			return false;
		}
		return code.equalsIgnoreCase(verifyCode);
	}

	@Override
	public String getNs() {
		return "vc";
	}

	@Override
	public String getNsCn() {
		return "验证码";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		validateCodeOpt = getRedisTemplate().opsForValue();
	}

	/**
	 * 设置验证码到缓存
	 */
	public void setPointVerifyCode(VerifyCode verifyCode) {
		String token = Security.getToken();
		validateCodeOpt.set(token, verifyCode);
	}

	/**
	 * 获取验证码并删除
	 */
	@SuppressWarnings("unchecked")
	public VerifyCode getPointVerifyCode() {
		VerifyCode verifyCode = (VerifyCode) validateCodeOpt.get(Security.getToken());
		getRedisTemplate().delete(Security.getToken());
		return verifyCode;
	}

	/**
	 * 只获取验证码不删除
	 */
	public VerifyCode getPointVerifyCodeOnly() {
		VerifyCode verifyCode = (VerifyCode) validateCodeOpt.get(Security.getToken());
		return verifyCode;
	}

	/**
	 * 验证点选式验证码
	 * 
	 * @return result
	 */
	public boolean checkPointVerifyCode(String location) {
		if (StringUtils.isBlank(location)) {
			return false;
		}
		VerifyCode checkCode = getPointVerifyCode();
		if (checkCode == null) {
			// 返回错误码
			return false;
		}

		// 判断验证码是否过期
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		if (now.after(checkCode.getDeadline())) {
			// 返回错误码
			return false;
		}

		// 验证坐标(15为冗余量)
		// x轴范围{x-15, x+size+15}
		// y轴范围{y-15, y+size+15}
		String[] lo = location.split(",");
		Integer[][] webCodes = { { Integer.parseInt(lo[0]), Integer.parseInt(lo[1]) },
				{ Integer.parseInt(lo[2]), Integer.parseInt(lo[3]) },
				{ Integer.parseInt(lo[4]), Integer.parseInt(lo[5]) } };
		Integer[][] locations = checkCode.getLocation();
		boolean flag = true;
		for (int i = 0; i < 3; i++) {
			int webx = webCodes[i][0];
			int weby = webCodes[i][1];
			int x = locations[i][0];
			int y = locations[i][1];
			y = y - 20;
			if (webx < (x - 5) || webx > (x + 26 + 5)) {
				flag = false;
				break;
			}
			if (weby < (y - 5) || weby > (y + 26 + 5)) {
				flag = false;
				break;
			}
		}

		return flag;
	}
}
