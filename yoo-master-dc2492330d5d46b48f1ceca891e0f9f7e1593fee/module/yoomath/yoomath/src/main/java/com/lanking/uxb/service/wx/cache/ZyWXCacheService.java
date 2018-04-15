package com.lanking.uxb.service.wx.cache;

import java.util.Set;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 悠数学微信端缓存.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月5日
 */
@Service
public class ZyWXCacheService extends AbstractCacheService {

	// private ValueOperations<String, String> strOpt;
	private SetOperations<String, String> setOpt;

	private static final String REPORT_KEY = "rpt"; // 报告兑换码

	@Override
	public String getNs() {
		return "wx-m";
	}

	@Override
	public String getNsCn() {
		return "悠数学-微信端";
	}

	private String getReportCodeKey() {
		return REPORT_KEY;
	}

	/**
	 * 获得所有报告兑换码.
	 * 
	 * @param openId
	 * @return
	 */
	public Set<String> getReportCodes() {
		return setOpt.members(this.getReportCodeKey());
	}

	/**
	 * 获得报告兑换码.
	 * 
	 * @param openId
	 * @return
	 */
	public String getReportCodeAndSave() {
		String code = String.format("%08d", (int) (Math.random() * 100000000));
		if (setOpt.isMember(this.getReportCodeKey(), code)) {
			code = this.getReportCodeAndSave();
		}
		setOpt.add(this.getReportCodeKey(), code);
		return code;
	}

	/**
	 * 验证兑换码.
	 * 
	 * @param code
	 * @return
	 */
	public Boolean checkReportCode(String code) {
		if (setOpt.isMember(this.getReportCodeKey(), "-" + code)) {
			// 已失效
			return null;
		} else if (setOpt.isMember(this.getReportCodeKey(), code)) {
			// 未使用
			setOpt.remove(this.getReportCodeKey(), code);
			setOpt.add(this.getReportCodeKey(), "-" + code);
			return true;
		}

		// 错误
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		setOpt = getRedisTemplate().opsForSet();
	}
}
