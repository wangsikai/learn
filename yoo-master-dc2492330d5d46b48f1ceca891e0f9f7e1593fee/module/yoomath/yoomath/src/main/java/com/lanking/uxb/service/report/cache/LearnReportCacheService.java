package com.lanking.uxb.service.report.cache;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 学情分析相关缓存
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月31日
 */
@Service
public class LearnReportCacheService extends AbstractCacheService {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyMM");

	private ValueOperations<String, String> strOpt;

	private final String CUR_MONTH_TIPS_FLAG_KEY = "cmtf";

	private final String CUR_WEEK_TIPS_FLAG_KEY = "cwtf";

	@Override
	public String getNs() {
		return "lr";
	}

	@Override
	public String getNsCn() {
		return "悠数学-学情分析";
	}

	public String getCurMonthTips(long id) {
		return strOpt.get(assemblyKey(CUR_MONTH_TIPS_FLAG_KEY, String.valueOf(id), sdf.format(new Date())));
	}

	public void setCurMonthTips(long id, boolean flag) {
		strOpt.set(assemblyKey(CUR_MONTH_TIPS_FLAG_KEY, String.valueOf(id), sdf.format(new Date())),
				String.valueOf(flag));
	}

	public String getCurWeekTips(long id) {
		return strOpt.get(assemblyKey(CUR_WEEK_TIPS_FLAG_KEY, String.valueOf(id), getFirstdayOfWeek()));
	}

	public void setCurWeekTips(long id, boolean flag) {
		strOpt.set(assemblyKey(CUR_WEEK_TIPS_FLAG_KEY, String.valueOf(id), getFirstdayOfWeek()), String.valueOf(flag));
	}

	private String getFirstdayOfWeek() {
		int week = LocalDate.now().getDayOfWeek().getValue();
		return LocalDate.now().minusDays(week - 1).toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
	}

}
