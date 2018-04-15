package com.lanking.uxb.zycon.HolidayActivity02.resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02PKRecordService;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02PowerRankService;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02Service;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02UserService;
import com.lanking.uxb.zycon.HolidayActivity02.api.ZycHolidayActivity02WeekPowerRankService;
import com.lanking.uxb.zycon.HolidayActivity02.form.ZycHolidayActivity02PKRecordForm;
import com.lanking.uxb.zycon.HolidayActivity02.form.ZycHolidayActivity02UserForm;
import com.lanking.uxb.zycon.HolidayActivity02.value.VHolidayActivity02PKRecord;
import com.lanking.uxb.zycon.HolidayActivity02.value.VHolidayActivity02PowerRank;
import com.lanking.uxb.zycon.HolidayActivity02.value.VHolidayActivity02WeekPowerRank;
import com.lanking.uxb.zycon.HolidayActivity02.value.VWeekPhases;

/**
 * 寒假活动接口
 *
 * @author peng.zhao
 * @version 2018-1-16
 */
@RestController
@RequestMapping(value = "zyc/holidayActivity02")
public class ZycHolidayActivity02Controller {

	@Autowired
	private ZycHolidayActivity02UserService holidayActivity02UserService;
	@Autowired
	private ZycHolidayActivity02PKRecordService holidayActivity02PKRecordService;
	@Autowired
	private ZycHolidayActivity02WeekPowerRankService holidayActivity02WeekPowerRankService;
	@Autowired
	private ZycHolidayActivity02Service holidayActivity02Service;
	@Autowired
	private ZycHolidayActivity02PowerRankService holidayActivity02PowerRankService;
	
	/**
	 * 查询用户信息
	 *
	 * @param form 查询条件
	 * @return Value
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "userInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value userInfo(ZycHolidayActivity02UserForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		if (form.getActivityCode() == null) {
			return new Value(new MissingArgumentException());
		}
		Page<Map> userInfo = holidayActivity02UserService.queryUserByForm(form, P.index(page, pageSize));
		
		VPage<Map> vp = new VPage<>();
		int tPage = (int) (userInfo.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(userInfo.getTotalCount());
		vp.setItems(userInfo.getItems());

		return new Value(vp);
	}
	
	/**
	 * 查询用户对战记录
	 *
	 * @param form 查询条件
	 * @return Value
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "pkRecords", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pkRecords(ZycHolidayActivity02PKRecordForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		if (form.getActivityCode() == null) {
			return new Value(new MissingArgumentException());
		}

		Page<HolidayActivity02PKRecord> pkRecordsPage = holidayActivity02PKRecordService.getRedoresByForm(form, P.index(page, pageSize));
		
		List<VHolidayActivity02PKRecord> vrecords = Lists.newArrayList();
		for (HolidayActivity02PKRecord value : pkRecordsPage.getItems()) {
			VHolidayActivity02PKRecord vrecord = new VHolidayActivity02PKRecord();
			vrecord.setActivityCode(value.getActivityCode());
			vrecord.setPkAt(value.getPkAt());
			
			Map userDTO = holidayActivity02PKRecordService.queryUserInfoById(value.getId());
			Map pkUserDTO = holidayActivity02PKRecordService.queryUserInfoById(value.getPkRecordId());
			
			if (userDTO.get("accountname") != null) {
				vrecord.setAccountName(String.valueOf(userDTO.get("accountname")));
			}
			if (userDTO.get("realname") != null) {
				vrecord.setRealName(String.valueOf(userDTO.get("realname")));
			}
			if (userDTO.get("schoolname") != null) {
				vrecord.setSchoolName(String.valueOf(userDTO.get("schoolname")));
			}
			if (userDTO.get("rightrate") != null) {
				vrecord.setRightRate(getRightRate(userDTO.get("rightrate")));
			}
			if (pkUserDTO != null) {
				if (pkUserDTO.get("accountname") != null) {
					vrecord.setPkAccountName(String.valueOf(pkUserDTO.get("accountname")));
				}
				if (pkUserDTO.get("realname") != null) {
					vrecord.setPkRealName(String.valueOf(pkUserDTO.get("realname")));
				}
				if (pkUserDTO.get("rightrate") != null) {
					vrecord.setPkRightRate(getRightRate(pkUserDTO.get("rightrate")));
				}
				if (pkUserDTO.get("schoolname") != null) {
					vrecord.setPkSchoolName(String.valueOf(pkUserDTO.get("schoolname")));
				}
			}
			
			vrecords.add(vrecord);
		}
		
		VPage<VHolidayActivity02PKRecord> vp = new VPage<>();
		int tPage = (int) (pkRecordsPage.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(pkRecordsPage.getTotalCount());
		vp.setItems(vrecords);
		return new Value(vp);
	}
	
	/**
	 * 转换正确率
	 * 
	 * @param right 正确率
	 * @return "x%"
	 */
	private String getRightRate(Object right) {
		String rightStr = String.valueOf(right);
		if ("null".equals(rightStr)) {
			return "0%";
		} else if (rightStr != null && !"".equals(rightStr)) {
			BigDecimal decRight = new BigDecimal(rightStr);
			return String.valueOf(decRight.intValue()) + "%";
		}

		return "0%";
	}
	
	/**
	 * 查询周战力记录
	 *
	 * @param activityCode 活动code
	 * @param weekPhaseCode 周阶段code
	 * @return Value
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "weekPowerRanks", method = { RequestMethod.GET, RequestMethod.POST })
	public Value weekPowerRanks(Long activityCode, Integer weekPhaseCode, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		if (activityCode == null) {
			return new Value(new MissingArgumentException());
		}

		HolidayActivity02 activity = holidayActivity02Service.get(activityCode);
		if (activity == null) {
			return new Value(new IllegalArgException());
		}
		
		List<Date> weekPhaseTime = getWeekStartAndEndTime(weekPhaseCode, activity.getCfg());
		
		Page<Map> ranks = holidayActivity02WeekPowerRankService.queryWeekPowerRankByPhase(weekPhaseTime, activityCode, P.index(page, pageSize));
		
		List<VHolidayActivity02WeekPowerRank> vranks = Lists.newArrayList();
		for (Map value : ranks.getItems()) {
			VHolidayActivity02WeekPowerRank v = new VHolidayActivity02WeekPowerRank();
			v.setId(Long.valueOf(value.get("id").toString()));
			if (value.get("channelname") != null) {
				v.setChannelName(value.get("channelname").toString());
			}
			if (value.get("accountname") != null) {
				v.setAccountName(value.get("accountname").toString());	
			}
			if (value.get("enteryear") != null) {
				v.setEnterYear(Integer.parseInt(value.get("enteryear").toString()));
			}
			if (value.get("membertype") != null) {
				v.setMemberType(Integer.parseInt(value.get("membertype").toString()));
			}
			if (value.get("mobile") != null) {
				v.setMobile(value.get("mobile").toString());
			}
			if (value.get("weekpower") != null) {
				v.setWeekPower(Integer.parseInt(value.get("weekpower").toString()));
			}
			if (value.get("rank") != null) {
				v.setRank(Integer.parseInt(value.get("rank").toString()));
			}
			if (value.get("realname") != null) {
				v.setRealName(value.get("realname").toString());
			}
			if (value.get("schoolname") != null) {
				v.setSchoolName(value.get("schoolname").toString());
			}
			
			vranks.add(v);
		}
		VPage<VHolidayActivity02WeekPowerRank> vp = new VPage<>();
		int tPage = (int) (ranks.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(ranks.getTotalCount());
		vp.setItems(vranks);
		
		return new Value(vp);
	}
	
	/**
	 * 根据页面传的weekPhaseCode获取查询起止时间
	 * 
	 * @param weekPhaseCode
	 * @param cfg
	 */
	private List<Date> getWeekStartAndEndTime(Integer weekPhaseCode, HolidayActivity02Cfg cfg) {
		List<Date> data = Lists.newArrayList();
		if (weekPhaseCode == null || cfg == null) {
			return data;
		}
		
		weekPhaseCode = weekPhaseCode - 1;
		List<List<Date>> phases = cfg.getPhases();
		for (int i = 0; i < phases.size(); i++) {
			if (weekPhaseCode == i) {
				data = phases.get(i);
				break;
			}
		}
		
		return data;
	}
	
	/**
	 * 查询活动周期
	 *
	 * @param activityCode 活动code
	 * @return Value
	 */
	@RequestMapping(value = "weekPhases", method = { RequestMethod.GET, RequestMethod.POST })
	public Value weekPhases(Long activityCode) {
		if (activityCode == null) {
			return new Value(new MissingArgumentException());
		}
		
		HolidayActivity02 activity = holidayActivity02Service.get(activityCode);
		if (activity == null) {
			return new Value(new IllegalArgException());
		}
		Map<String, Object> data = new HashMap<>();
		data.put("weekPhases", formatWeekPhases(activity.getCfg()));
		return new Value(data);
	}
	
	/**
	 * 格式化活动周期
	 * 
	 * @param cfg
	 * @return VWeekPhases
	 */
	private List<VWeekPhases> formatWeekPhases(HolidayActivity02Cfg cfg) {
		if (cfg == null) {
			return Lists.newArrayList();
		}
		List<VWeekPhases> vphases = Lists.newArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat( "MM-dd" );
		
		List<List<Date>> phases = cfg.getPhases();
		
		int count = 1;
		for (List<Date> value : phases) {
			Date start = value.get(0);
			Date end = value.get(1);
			VWeekPhases vphase = new VWeekPhases();
			vphase.setCode(count);
			vphase.setName(
					"第" + count + "周：" + getFormatMonth(sdf.format(start)) + "--" + getFormatMonth(sdf.format(end)));
			vphases.add(vphase);
			count++;
		}

		return vphases;
	}
	
	/**
	 * 格式化日期
	 * 
	 * @param time
	 */
	private String getFormatMonth(String time) {
		String[] tempDate = time.split("-");
		if (tempDate == null) {
			return null;
		}
		
		int monthNum = Integer.parseInt(tempDate[0]);
		int dateNum = Integer.parseInt(tempDate[1]);
		
		String result = monthNum + "月" + dateNum + "日";
		return result;
	}
	
	/**
	 * 更新周战力
	 *
	 * @param activityCode 活动code
	 * @param id holidayActivity02WeekPowerRank表主键
	 * @param power 调整后的战力值
	 * @return Value
	 */
	@RequestMapping(value = "updateWeekPower", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateWeekPower(Long activityCode, Long id, Integer power) {
		if (activityCode == null || id == null || power == null) {
			return new Value(new MissingArgumentException());
		}
		if (power < 0) {
			return new Value(new IllegalArgException());
		}
		
		HolidayActivity02 activity = holidayActivity02Service.get(activityCode);
		if (activity == null) {
			return new Value(new IllegalArgException());
		}
		
		// 更新
		holidayActivity02WeekPowerRankService.updateWeekPower(activityCode, id, power, Security.getUserId());
		
		return new Value();
	}
	
	/**
	 * 查询总战力记录
	 *
	 * @param activityCode 活动code
	 * @param weekPhaseCode 周阶段code
	 * @return Value
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "powerRanks", method = { RequestMethod.GET, RequestMethod.POST })
	public Value powerRanks(Long activityCode, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		if (activityCode == null) {
			return new Value(new MissingArgumentException());
		}

		HolidayActivity02 activity = holidayActivity02Service.get(activityCode);
		if (activity == null) {
			return new Value(new IllegalArgException());
		}
		
		Page<Map> ranks = holidayActivity02PowerRankService.queryPowerRankList(activityCode, P.index(page, pageSize));
		
		List<VHolidayActivity02PowerRank> vranks = Lists.newArrayList();
		for (Map value : ranks.getItems()) {
			VHolidayActivity02PowerRank v = new VHolidayActivity02PowerRank();
			v.setId(Long.valueOf(value.get("id").toString()));
			if (value.get("channelname") != null) {
				v.setChannelName(value.get("channelname").toString());
			}
			if (value.get("accountname") != null) {
				v.setAccountName(value.get("accountname").toString());
			}
 			if (value.get("enteryear") != null) {
				v.setEnterYear(Integer.parseInt(value.get("enteryear").toString()));
			}
			if (value.get("membertype") != null) {
				v.setMemberType(Integer.parseInt(value.get("membertype").toString()));
			}
			if (value.get("mobile") != null) {
				v.setMobile(value.get("mobile").toString());
			}
			if (value.get("power") != null) {
				v.setPower(Integer.parseInt(value.get("power").toString()));
			}
			if (value.get("rank") != null) {
				v.setRank(Integer.parseInt(value.get("rank").toString()));
			}
			if (value.get("realname") != null) {
				v.setRealName(value.get("realname").toString());
			}
			if (value.get("schoolname") != null) {
				v.setSchoolName(value.get("schoolname").toString());
			}
			
			vranks.add(v);
		}
		VPage<VHolidayActivity02PowerRank> vp = new VPage<>();
		int tPage = (int) (ranks.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(ranks.getTotalCount());
		vp.setItems(vranks);
		
		return new Value(vp);
	}
	
	/**
	 * 更新总战力
	 *
	 * @param activityCode 活动code
	 * @param id HolidayActivity02PowerRank表主键
	 * @param power 调整后的战力值
	 * @return Value
	 */
	@RequestMapping(value = "updatePower", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updatePower(Long activityCode, Long id, Integer power) {
		if (activityCode == null || id == null || power == null) {
			return new Value(new MissingArgumentException());
		}
		if (power < 0) {
			return new Value(new IllegalArgException());
		}
		
		HolidayActivity02 activity = holidayActivity02Service.get(activityCode);
		if (activity == null) {
			return new Value(new IllegalArgException());
		}
		
		// 更新
		holidayActivity02PowerRankService.updateByPower(id, power, Security.getUserId());
		return new Value();
	}
}
