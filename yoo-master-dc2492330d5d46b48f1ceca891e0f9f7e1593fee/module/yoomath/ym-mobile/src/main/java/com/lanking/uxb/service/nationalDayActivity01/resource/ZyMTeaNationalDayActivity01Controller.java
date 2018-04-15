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
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01TeaAward;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01Service;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01TeaAwardService;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01TeaService;
import com.lanking.uxb.service.nationalDayActivity01.convert.NationalDayActivity01TeaAwardConvert;
import com.lanking.uxb.service.nationalDayActivity01.convert.NationalDayActivity01TeaConvert;
import com.lanking.uxb.service.nationalDayActivity01.value.VNationalDayActivity01Rank;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

/**
 * 国庆节活动老师相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月21日
 */
@RestController
@RequestMapping("zy/m/t/nda01")
public class ZyMTeaNationalDayActivity01Controller {

	@Autowired
	private NationalDayActivity01Service nationalDayActivity01Service;
	@Autowired
	private NationalDayActivity01TeaService nationalDayActivity01TeaService;
	@Autowired
	private NationalDayActivity01TeaConvert nationalDayActivity01TeaConvert;
	@Autowired
	private NationalDayActivity01TeaAwardService nationalDayActivity01TeaAwardService;
	@Autowired
	private NationalDayActivity01TeaAwardConvert nationalDayActivity01TeaAwardConvert;

	/**
	 * 介绍页接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
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
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "rankingList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value rankingList() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<NationalDayActivity01Tea> teas = nationalDayActivity01TeaService.getTopN(10);
		if (CollectionUtils.isEmpty(teas)) {
			data.put("items", Collections.EMPTY_LIST);
		} else {
			data.put("items", getRanks(teas));
		}

		// 该用户自己的排行
		NationalDayActivity01Tea meTeaRank = null;
		int meRank = 0;
		for (int i = 0; i < teas.size(); i++) {
			if (teas.get(i).getUserId() == Security.getUserId()) {
				meTeaRank = teas.get(i);
				meRank = i + 1;
				break;
			}
		}

		VNationalDayActivity01Rank vMeRank = null;
		if (meTeaRank == null) {
			Map<String, Object> meRankMap = nationalDayActivity01TeaService.getTeaByUser(Security.getUserId());
			if (meRankMap != null && !meRankMap.isEmpty()) {
				meTeaRank = (NationalDayActivity01Tea) meRankMap.get("activityTea");
				vMeRank = nationalDayActivity01TeaConvert.to(meTeaRank);
				vMeRank.setRank((int) meRankMap.get("rownum"));
			} else {
				meTeaRank = new NationalDayActivity01Tea();
				meTeaRank.setUserId(Security.getUserId());
				vMeRank = nationalDayActivity01TeaConvert.to(meTeaRank);
				vMeRank.setRank(0);
			}
		} else {
			vMeRank = nationalDayActivity01TeaConvert.to(meTeaRank);
			vMeRank.setRank(meRank);
		}

		vMeRank.getUser().setName(formatTeacherName(vMeRank.getUser().getName()));
		data.put("meRank", vMeRank);

		return new Value(data);
	}

	/**
	 * 设置排行榜排名
	 * 
	 * @param teas
	 * @return
	 */
	private List<VNationalDayActivity01Rank> getRanks(List<NationalDayActivity01Tea> teas) {
		List<VNationalDayActivity01Rank> vRanks = nationalDayActivity01TeaConvert.to(teas);
		for (int i = 0; i < vRanks.size(); i++) {
			vRanks.get(i).setRank(i + 1);

			vRanks.get(i).getUser().setName(formatTeacherName(vRanks.get(i).getUser().getName()));
		}

		return vRanks;
	}

	/**
	 * 截取老师名称
	 * 
	 * @param teacherName
	 * @return
	 */
	private String formatTeacherName(String teacherName) {
		if (teacherName != null && teacherName.length() > 0) {
			return teacherName.substring(0, 1) + "老师";
		} else {
			return "老师";
		}
	}

	/**
	 * 获奖榜单接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "awardList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value awardList() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<NationalDayActivity01TeaAward> awards = nationalDayActivity01TeaAwardService.getTeaAward();
		if (CollectionUtils.isEmpty(awards)) {
			data.put("items", Collections.EMPTY_LIST);
		}

		data.put("items", nationalDayActivity01TeaAwardConvert.to(awards));
		return new Value(data);
	}
}
