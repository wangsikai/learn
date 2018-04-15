package com.lanking.uxb.service.nationalDayActivity01.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01StuAward;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01Service;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01StuAwardService;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01StuService;
import com.lanking.uxb.service.nationalDayActivity01.convert.NationalDayActivity01StuAwardConvert;
import com.lanking.uxb.service.nationalDayActivity01.convert.NationalDayActivity01StuConvert;
import com.lanking.uxb.service.nationalDayActivity01.value.VNationalDayActivity01Rank;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

/**
 * 国庆节活动学生相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@RestController
@RequestMapping("zy/m/s/nda01")
public class ZyMStuNationalDayActivity01Controller {

	@Autowired
	private NationalDayActivity01Service nationalDayActivity01Service;
	@Autowired
	private NationalDayActivity01StuService nationalDayActivity01StuService;
	@Autowired
	private NationalDayActivity01StuConvert nationalDayActivity01StuConvert;
	@Autowired
	private NationalDayActivity01StuAwardService nationalDayActivity01StuAwardService;
	@Autowired
	private NationalDayActivity01StuAwardConvert nationalDayActivity01StuAwardConvert;

	/**
	 * 介绍页接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		Map<String, Object> data = new HashMap<String, Object>();

		NationalDayActivity01 activity = nationalDayActivity01Service.getActivity();
		if (activity != null) {
			data.put("startTime", activity.getStartTime().getTime());
			data.put("endTime", activity.getEndTime().getTime());
		}

		return new Value(data);
	}

	/**
	 * 排行榜接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("null")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "rankingList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value rankingList() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<NationalDayActivity01Stu> stus = nationalDayActivity01StuService.getTopN(50);
		if (CollectionUtils.isEmpty(stus)) {
			data.put("items", Collections.EMPTY_LIST);
		} else {
			data.put("items", getRanks(stus));
		}

		// 该用户自己的排行
		NationalDayActivity01Stu meStuRank = null;
		int meRank = 0;
		for (int i = 0; i < stus.size(); i++) {
			if (stus.get(i).getUserId() == Security.getUserId()) {
				meStuRank = stus.get(i);
				meRank = i + 1;
				break;
			}
		}
		VNationalDayActivity01Rank vMeRank = null;
		if (meStuRank == null) {
			Map<String, Object> meRankMap = nationalDayActivity01StuService.getStuByUser(Security.getUserId());
			if (meRankMap != null && !meRankMap.isEmpty()) {
				meStuRank = (NationalDayActivity01Stu) meRankMap.get("activityTea");
				vMeRank = nationalDayActivity01StuConvert.to(meStuRank);
				vMeRank.setRank((int) meRankMap.get("rownum"));
			} else {
				meStuRank = new NationalDayActivity01Stu();
				meStuRank.setUserId(Security.getUserId());
				meStuRank.setRightCount(0L);
				vMeRank = nationalDayActivity01StuConvert.to(meStuRank);
			}
		} else {
			vMeRank = nationalDayActivity01StuConvert.to(meStuRank);
			vMeRank.setRank(meRank);
		}

		data.put("meRank", vMeRank);

		return new Value(data);
	}

	/**
	 * 设置排行榜排名
	 * 
	 * @param teas
	 * @return
	 */
	private List<VNationalDayActivity01Rank> getRanks(List<NationalDayActivity01Stu> stus) {
		List<VNationalDayActivity01Rank> vRanks = nationalDayActivity01StuConvert.to(stus);
		for (int i = 0; i < vRanks.size(); i++) {
			vRanks.get(i).setRank(i + 1);
		}

		return vRanks;
	}

	/**
	 * 获奖榜单接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "awardList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value awardList() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<NationalDayActivity01StuAward> awards = nationalDayActivity01StuAwardService.getStuAward();
		if (CollectionUtils.isEmpty(awards)) {
			data.put("items", Collections.EMPTY_LIST);
		}

		data.put("items", nationalDayActivity01StuAwardConvert.to(awards));

		return new Value(data);
	}
}
