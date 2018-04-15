package com.lanking.uxb.rescon.statistics.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.statistics.api.UserStatisticsManage;
import com.lanking.uxb.service.counter.api.impl.VendorUserCounterProvider;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 用户统计相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月19日
 */
@RestController
@RequestMapping("rescon/statis/user")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconUserStatisticsController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserStatisticsManage userStatisticsManage;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private VendorUserCounterProvider vendorUserCounterProvider;

	/**
	 * 查询人员统计结果
	 * 
	 * @since yoomath V1.2
	 * @param userType
	 *            用户类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return {@link Value}
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(UserType userType, String startTime, String endTime) {
		long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		if (StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {

				return new Value(userStatisticsManage.queryDayDetail(vendorId, userType,
						StringUtils.isNotBlank(startTime) ? sdf.parse(startTime + " 00:00:00") : null,
						StringUtils.isNotBlank(endTime) ? sdf.parse(endTime + " 00:00:00") : null));
			} catch (ParseException e) {
				return new Value(Collections.EMPTY_LIST);
			}
		} else {
			return new Value(userStatisticsManage.getAllDetail(vendorId, userType));
		}
	}

	/**
	 * 获得人员相关数据.
	 * 
	 * @since yoomath V1.2
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "getUserDatas")
	public Value getUserDatas() {
		long userId = Security.getUserId();

		Map<String, Object> map = userStatisticsManage.getUserAllDetail(userId);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date bt = null;
		Date et = null;
		try {
			// 今日数据
			Counter counter = vendorUserCounterProvider.getTodayCounter(userId);
			map.put("today1", counter.getCount1());
			map.put("today2", counter.getCount2());
			map.put("today", counter.getCount1() + counter.getCount2());

			et = sdf.parse(sdf.format(new Date()));
			cal.setTime(et);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			bt = cal.getTime();

			// 昨日数据
			List<Map> list1 = userStatisticsManage.queryUserDayDetail(userId, bt, et);
			if (list1.size() > 0) {
				map.put("yestoday", list1.get(0));
			}

			// 上周数据（上一个周一至周日）
			cal.set(Calendar.DAY_OF_WEEK, 1); // 本周日
			et = cal.getTime();
			cal.add(Calendar.DAY_OF_YEAR, -6); // 上周一
			bt = cal.getTime();
			List<Map> list2 = userStatisticsManage.queryUserDayDetail(userId, bt, et);
			if (list2.size() > 0) {
				map.put("lastweek", list2.get(0));
			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}

		return new Value(map);
	}
}