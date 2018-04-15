package com.lanking.uxb.service.holidayActivity01.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Medal;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.HolidayActivity02MedalService;
import com.lanking.uxb.service.activity.api.HolidayActivity02PkRecordService;
import com.lanking.uxb.service.activity.api.HolidayActivity02UserService;
import com.lanking.uxb.service.holidayActivity01.value.VHolidayActivity02Medal;
import com.lanking.uxb.service.holidayActivity01.value.VHolidayActivity02PKRecord;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;

import httl.util.CollectionUtils;

/**
 * 寒假活动勋章&对战历史相关接口
 * 
 * @author peng.zhao
 * @version 2018年1月19日
 */
@RestController
@RequestMapping("zy/m/s/holidayActivity02")
public class ZyMHolidayActivity02Controller2 {

	@Autowired
	private HolidayActivity02MedalService holidayActivity02MedalService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private HolidayActivity02UserService holidayActivity02UserService;
	@Autowired
	private HolidayActivity02PkRecordService holidayActivity02PkRecordService;
	
	/**
	 * 勋章列表接口
	 * 
	 * @param code 活动code
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getMedal", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getMedal(Long code) {
		Map<String, Object> data = new HashMap<String, Object>();

		List<HolidayActivity02Medal> medals = holidayActivity02MedalService.getMedals(code, Security.getUserId());
		HolidayActivity02User user = holidayActivity02UserService.getUserActivityInfo(code, Security.getUserId());
		if (CollectionUtils.isEmpty(medals) || user == null) {
			return new Value(new IllegalArgException());
		}

		List<VHolidayActivity02Medal> vmedals = Lists.newArrayList();
		for (HolidayActivity02Medal value : medals) {
			VHolidayActivity02Medal v = new VHolidayActivity02Medal();
			v.setId(value.getId());
			v.setActivityCode(value.getActivityCode());
			v.setLevel(value.getLevel());
			v.setAchieved(value.getAchieved());
			v.setReceived(value.getReceived());
			if (value.getAchieved() == 0) {
				// 胜1场
				if (value.getLevel() == 1) {
					v.setProgress(0);
				} else if (value.getLevel() == 2) {
					// 胜50场
					v.setProgress(user.getWin());
				} else if (value.getLevel() == 3) {
					// 参赛100场
					v.setProgress(user.getDraw() + user.getLose() + user.getWin());
				} else if (value.getLevel() == 4) {
					// 战力值达到1000
					v.setProgress(user.getPower());
				} else if (value.getLevel() == 5) {
					// 获胜500场
					v.setProgress(user.getWin());
				}
			}
			
			vmedals.add(v);
		}
		
		data.put("user", userConvert.get(Security.getUserId(), new UserConvertOption(true)));
		data.put("medals", vmedals);
		
		return new Value(data);
	}
	
	/**
	 * 领取勋章接口
	 * 
	 * @param code 活动code
	 * @param medalId medal表主键
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "receiveMedal", method = { RequestMethod.POST, RequestMethod.GET })
	public Value receiveMedal(Long code, Long medalId) {
		Map<String, Object> data = new HashMap<String, Object>(1);
		
		// 战力200-概率10,战力150-概率20,战力100-概率70
		int difference = 0;
		double rand = Math.random() * 100;
		if (rand >= 0 && rand < 10) {
			difference = 200;
		} else if (rand >= 10 && rand < 30) {
			difference = 150;
		} else {
			difference = 100;
		}
		
		// 1.计算获得的战力
		// 2.战力更新到HolidayActivity02Medal表
		// 3.更新到HolidayActivity02User表
		// 4.更新到HolidayActivity02WeekPowerRank表,注意weekPower和realWeekPower都要更新
		// 5.更新到HolidayActivity02PowerRank表,注意power和realPower都要更新
		HolidayActivity02Medal updateMedal = holidayActivity02MedalService.receiveMedal(medalId, code, difference, Security.getUserId());
		if (updateMedal == null ) {
			return new Value(new IllegalArgException());
		}
		
		data.put("award", difference);
		
		return new Value(data);
	}
	
	/**
	 * 历史对战记录接口
	 * 
	 * @param code 活动code
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "rawtypes" })
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getPkRecords", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getPkRecords(Long code) {
		Map<String, Object> data = new HashMap<String, Object>(4);
		
		HolidayActivity02User user = holidayActivity02UserService.getUserActivityInfo(code, Security.getUserId());
		if (user == null) {
			return new Value(new IllegalArgException());
		}
		
		List<Map> recordsMap = holidayActivity02PkRecordService.listAllPkRecord(code, Security.getUserId(), 50);
		
		List<VHolidayActivity02PKRecord> vrecords = formatRecords(recordsMap);
		
		data.put("records", vrecords);
		data.put("user", userConvert.get(Security.getUserId(), new UserConvertOption(true)));
		data.put("power", user.getPower());
		data.put("counts", user.getDraw() + user.getLose() + user.getWin());
		return new Value(data);
	}
	
	/**
	 * 格式化pk记录
	 * 
	 * @param recordsMap
	 * @return records
	 */
	@SuppressWarnings("rawtypes")
	private List<VHolidayActivity02PKRecord> formatRecords(List<Map> recordsMap) {
		List<VHolidayActivity02PKRecord> vrecords = Lists.newArrayList();
		VHolidayActivity02PKRecord v = null;
		for (Map value : recordsMap) {
			v = new VHolidayActivity02PKRecord();
			v.setId(Long.valueOf(String.valueOf(value.get("id"))));
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			try {
				date = dateFormat.parse(value.get("pkat").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			v.setPkAt(date);
			String name = String.valueOf(value.get("pkname"));
			if (name != null) {
				if (name.length() > 2) {
					v.setPkUserName(name.substring(0, 1) + "**");
				} else {
					v.setPkUserName(name.substring(0, 1) + "*");
				}
			}
			
			value.get("power");
			String powerStr = String.valueOf(value.get("power"));
			if (powerStr == null || "null".equals(powerStr)) {
				powerStr = "0";
			}
			int power = Integer.parseInt(powerStr);
			if (power == 0) {
				v.setPkResult(0);
			} else if (power == 10) {
				v.setPkResult(1);
			} else {
				v.setPkResult(2);
			}
			
			vrecords.add(v);
		}

		return vrecords;
	}
}
