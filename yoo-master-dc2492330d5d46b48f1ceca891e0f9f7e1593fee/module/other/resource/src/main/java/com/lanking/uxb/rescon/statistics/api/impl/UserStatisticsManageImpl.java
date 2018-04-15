package com.lanking.uxb.rescon.statistics.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatis;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.statistics.api.UserStatisticsManage;

@Transactional(readOnly = true)
@Service
public class UserStatisticsManageImpl implements UserStatisticsManage {

	@Autowired
	@Qualifier("VendorUserStatisRepo")
	private Repo<VendorUserStatis, Long> statisRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getAllDetail(long vendorId, UserType userType) {
		Params params = Params.param("vendorId", vendorId);
		if (userType != null && userType != UserType.NULL) {
			params.put("userType", userType.getValue());
		}
		return statisRepo.find("resconGetAllDetail", params).list(Map.class);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Map getUserAllDetail(long userId) {
		Params params = Params.param();
		params.put("userId", userId);
		List<Map> list = statisRepo.find("resconGetAllDetail", params).list(Map.class);
		return list.size() == 0 ? Maps.newHashMap() : list.get(0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryDayDetail(long vendorId, UserType userType, Date startTime, Date endTime) {
		Params params = Params.param("vendorId", vendorId);
		if (userType != null && userType != UserType.NULL) {
			params.put("userType", userType.getValue());
		}
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.DATE, 1);
			params.put("endTime", calendar.getTime());
		}
		return statisRepo.find("$resconQueryDayDetail", params).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryUserDayDetail(long userId, Date startTime, Date endTime) {
		Params params = Params.param();
		params.put("userId", userId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		return statisRepo.find("$resconQueryDayDetail", params).list(Map.class);
	}
}
