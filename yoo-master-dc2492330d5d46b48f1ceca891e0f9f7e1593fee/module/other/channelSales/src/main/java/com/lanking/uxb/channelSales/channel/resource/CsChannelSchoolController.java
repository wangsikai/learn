package com.lanking.uxb.channelSales.channel.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsAggSchoolService;
import com.lanking.uxb.channelSales.channel.api.CsChannelSchoolService;
import com.lanking.uxb.channelSales.channel.api.CsDmnChannelService;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 我的渠道--学校数据
 * 
 * @author zemin.song
 *
 */
@RestController
@RequestMapping("channelSales/school")
public class CsChannelSchoolController {
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private CsAggSchoolService csAggSchoolHomeworkHisService;
	@Autowired
	private CsDmnChannelService dmnChannelService;
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private CsChannelSchoolService csChannelSchoolService;

	/**
	 * 获取学校的数据
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping(value = "schoolData")
	public Value schoolData(String schoolCode, Integer phaseCode) {
		Integer channelCode = dmnChannelService.getIdChannel(userChannelService.getChannelByUser(Security.getUserId())
				.getCode());
		Map<String, Object> data = new HashMap<String, Object>();
		School school = schoolService.get(schoolCode);
		if (school == null) {
			return new Value(new IllegalArgException());
		}
		data.put("data", schoolConvert.to(school));
		data.put("bindData", csChannelSchoolService.getSchoolBindDate(school.getId()));
		data.put("studentCount", csAggSchoolHomeworkHisService.studentStat(schoolCode, channelCode));
		data.put("teacherCount", csAggSchoolHomeworkHisService.teacherStat(schoolCode, channelCode));
		data.put("clazzHkCount", csAggSchoolHomeworkHisService.getClazzHkCount(schoolCode, channelCode));
		data.put("nohomeWorkCount", csAggSchoolHomeworkHisService.getNohomeWorkCount(schoolCode, channelCode));
		return new Value(data);
	}

	/**
	 * 作业量数据
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping(value = "getAggHomeWork")
	public Value getAggHomeWork(String schoolCode, Integer day) {
		Integer channelId = dmnChannelService.getIdChannel(userChannelService.getChannelByUser(Security.getUserId())
				.getCode());
		return new Value(csAggSchoolHomeworkHisService.getAggHomeWork(schoolCode, channelId, day));
	}

	/**
	 * 用户活跃数据
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping(value = "getRegisterCount")
	public Value getRegisterCount(String schoolCode, Integer day) {
		Integer channelId = dmnChannelService.getIdChannel(userChannelService.getChannelByUser(Security.getUserId())
				.getCode());
		return new Value(csAggSchoolHomeworkHisService.getRegisterCount(schoolCode, channelId, day));
	}

	/**
	 * 按届查找班级
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping(value = "getClazzDateByYeart")
	public Value getClazzDateByYeart(String schoolCode, Integer year) {
		Integer channelId = dmnChannelService.getIdChannel(userChannelService.getChannelByUser(Security.getUserId())
				.getCode());

		return new Value(csAggSchoolHomeworkHisService.getClazzDateByYeart(schoolCode, channelId, year));

	}

	/**
	 * 按天找未布置作业统计
	 * 
	 * @param school
	 * @return
	 */
	@RequestMapping(value = "getNoClazzDateByDay")
	public Value getNoClazzDateByDay(String schoolCode, Integer day) {
		Integer channelId = dmnChannelService.getIdChannel(userChannelService.getChannelByUser(Security.getUserId())
				.getCode());

		return new Value(csAggSchoolHomeworkHisService.getNoClazzDateByDay(schoolCode, channelId, day));

	}

}
