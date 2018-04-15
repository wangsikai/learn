package com.lanking.uxb.service.activity.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Medal;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PowerRank;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02WeekPowerRank;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02PowerRankService;
import com.lanking.uxb.service.activity.api.HolidayActivity02Service;
import com.lanking.uxb.service.activity.api.HolidayActivity02UserService;
import com.lanking.uxb.service.activity.api.HolidayActivity02WeekPowerRankService;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02UserServiceImpl implements HolidayActivity02UserService {
	@Autowired
	@Qualifier("HolidayActivity02UserRepo")
	private Repo<HolidayActivity02User, Long> repo;
	@Autowired
	@Qualifier("HolidayActivity02MedalRepo")
	private Repo<HolidayActivity02Medal, Long> medalRepo;
	@Autowired
	@Qualifier("HolidayActivity02PKRecordRepo")
	private Repo<HolidayActivity02PKRecord, Long> pkRecordRepo;
	
	@Autowired
	private HolidayActivity02PowerRankService holidayActivity02PowerRankService;
	
	@Autowired
	private HolidayActivity02WeekPowerRankService holidayActivity02WeekPowerRankService;
	
	@Autowired
	private HolidayActivity02Service holidayActivity02Service;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * 新增活动用户的时候需要先判断有没有，有的话直接返回
	 * 同时插入五条勋章记录，方便后续处理
	 */
	@Override
	@Transactional
	public void addActivity02User(long activityCode, long userId,long activityUserCode) {
		HolidayActivity02User existUser = getUserActivityInfo(activityCode,userId);
		
		if(existUser != null){
			return;
		}
		
		long maxUserCode = getMaxUserCode(activityCode);
		
		long currentUserCode = (maxUserCode > activityUserCode ? ++maxUserCode:activityUserCode);
		HolidayActivity02User user = new HolidayActivity02User();
		user.setActivityCode(activityCode);
		user.setUserId(userId);
		user.setActivityUserCode(currentUserCode);
		user.setCreateAt(new Date());
		user.setPower(0);
		user.setWin(0);
		user.setDraw(0);
		user.setLose(0);
		
		repo.save(user);
		
		List<HolidayActivity02Medal> medals = new ArrayList<>();
		for(Integer level = 1; level <= 5; level++){
			HolidayActivity02Medal medal = new HolidayActivity02Medal();
			medal.setActivityCode(activityCode);
			medal.setUserId(userId);
			medal.setLevel(level);
			medal.setPower(0);
			medal.setReceived(0);
			medal.setAchieved(0);
			
			medals.add(medal);
		}
		
		medalRepo.save(medals);
	}

	@Override
	public Boolean hasJoinActivity(long activityCode, long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		HolidayActivity02User user = repo.find("$findByUserId", params).get(HolidayActivity02User.class);
		
		Boolean result = false;
		
		result = (user == null ? false : true);
		
		return result;
	}

	@Override
	public Long getMaxUserCode(long activityCode) {
		Params params = Params.param("code", activityCode);
		Long userCode = repo.find("$getMaxUserCode", params).get(Long.class);
		
		return userCode == null ? 0:userCode;
	}

	@Override
	public HolidayActivity02User getUserActivityInfo(long activityCode, long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		HolidayActivity02User user = repo.find("$findByUserId", params).get(HolidayActivity02User.class);
		
		return user;
	}

	@Override
	@Transactional
	public void updateActivity02UserStats(long activityCode, long userId, Integer power,HolidayActivity02PKRecord record) {
		HolidayActivity02User existUser = getUserActivityInfo(activityCode,userId);
		
		if(existUser == null){
			return;
		}
		
		if(power == 0){
			existUser.setLose(existUser.getLose() + 1);
		} else {
			if(power == 10){
				existUser.setDraw(existUser.getDraw() + 1);
			} else {
				existUser.setWin(existUser.getWin() + 1);
			}
			
			existUser.setPower(existUser.getPower() + power);
		}
		logger.error("save user start " + System.currentTimeMillis() + " userid " + Security.getUserId());
		repo.save(existUser);
		logger.error("save user end " + System.currentTimeMillis() + " userid " + Security.getUserId());
		//处理pk记录的战力值，如果该值不为空，说明已经处理过，不再处理
		if(record.getPower() == null) {
			logger.error("save record start " + System.currentTimeMillis() + " userid " + Security.getUserId());
			record = pkRecordRepo.get(record.getId());
			record.setPower(power);
			pkRecordRepo.save(record);
			logger.error("save record end " + System.currentTimeMillis() + " userid " + Security.getUserId());
		}
		
		//处理勋章
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		List<HolidayActivity02Medal> medals = medalRepo.find("$findNotAchievedMedals", params).list();
		if(CollectionUtils.isNotEmpty(medals)) {
			for(HolidayActivity02Medal medal : medals) {
				if(medal.getLevel() == 1 && medal.getAchieved() == 0 && existUser.getWin() >= 1 ) {
					logger.error("save level 1 start  " + System.currentTimeMillis() + " userid " + Security.getUserId());
					medal.setAchieved(1);
					medalRepo.save(medal);
					logger.error("save level 1 end " + System.currentTimeMillis() + " userid " + Security.getUserId());
				} else if (medal.getLevel() == 2 && medal.getAchieved() == 0 && existUser.getWin() >= 50) {
					logger.error("save level 2 start  " + System.currentTimeMillis());
					medal.setAchieved(1);
					medalRepo.save(medal);
					logger.error("save level 2 end " + System.currentTimeMillis() + " userid " + Security.getUserId());
				} else if (medal.getLevel() == 3 && medal.getAchieved() == 0 && (existUser.getWin() + existUser.getDraw() + existUser.getLose() ) >= 100) {
					logger.error("save level 3 start  " + System.currentTimeMillis() + " userid " + Security.getUserId());
					medal.setAchieved(1);
					medalRepo.save(medal);
					logger.error("save level 3 end " + System.currentTimeMillis() + " userid " + Security.getUserId());
				} else if (medal.getLevel() == 4 && medal.getAchieved() == 0 && existUser.getPower() >= 1000) {
					logger.error("save level 4 start  " + System.currentTimeMillis() + " userid " + Security.getUserId());
					medal.setAchieved(1);
					medalRepo.save(medal);
					logger.error("save level 4 end " + System.currentTimeMillis()+ " userid " + Security.getUserId());
				} else if (medal.getLevel() == 5 && medal.getAchieved() == 0 && existUser.getWin() >= 500) {
					logger.error("save level 5 start  " + System.currentTimeMillis()+ " userid " + Security.getUserId());
					medal.setAchieved(1);
					medalRepo.save(medal);
					logger.error("save level 5 end " + System.currentTimeMillis()+ " userid " + Security.getUserId());
				}
			}
		}
		
		//更新总战力排行
		HolidayActivity02PowerRank holidayActivity02PowerRank = holidayActivity02PowerRankService.getActivity02PowerRank(activityCode, userId);
		if(holidayActivity02PowerRank == null) {
			logger.error("save power rank start " + System.currentTimeMillis()+ " userid " + Security.getUserId());
			holidayActivity02PowerRankService.addActivity02PowerRank(activityCode, userId, power);
			logger.error("save power rank end " + System.currentTimeMillis()+ " userid " + Security.getUserId());
		} else {
			if(power != 0){
				logger.error("modify power rank start " + System.currentTimeMillis()+ " userid " + Security.getUserId());
				holidayActivity02PowerRank.setPower(holidayActivity02PowerRank.getPower() + power);
				holidayActivity02PowerRank.setRealPower(holidayActivity02PowerRank.getRealPower() + power);
				holidayActivity02PowerRankService.updateActivity02PowerRank(holidayActivity02PowerRank);
				logger.error("modify power rank end " + System.currentTimeMillis()+ " userid " + Security.getUserId());
			}
		}
		//更新周新增战力排行
		HolidayActivity02WeekPowerRank holidayActivity02WeekPowerRank = holidayActivity02WeekPowerRankService.getActivity02WeekPowerRank(activityCode, userId);
		if(holidayActivity02WeekPowerRank == null) {
			HolidayActivity02 activity = holidayActivity02Service.get(activityCode);
			Date phaseStart = null;
			Date phaseEnd = null;
			
			HolidayActivity02Cfg cfg = activity.getCfg();
			
			if(cfg != null){
				List<List<Date>> phases = cfg.getPhases();
				List<Date> currentPhase = null;
				Date currentTime = new Date();
				for(List<Date> phase:phases){
					if(currentTime.after(phase.get(0)) && currentTime.before(phase.get(1))){
						currentPhase = phase;
						break;
					}
				}
				
				if(currentPhase != null){
					phaseStart = currentPhase.get(0);
					phaseEnd = currentPhase.get(1);
				}
				
			}
			logger.error("save week power rank start " + System.currentTimeMillis()+ " userid " + Security.getUserId());
			holidayActivity02WeekPowerRankService.addActivity02WeekPowerRank(activityCode, userId, power,phaseStart, phaseEnd);
			logger.error("save week power rank end " + System.currentTimeMillis());
		} else {
			if(power != 0){
				logger.error("modify week power rank start " + System.currentTimeMillis()+ " userid " + Security.getUserId());
				holidayActivity02WeekPowerRank.setWeekPower(holidayActivity02WeekPowerRank.getWeekPower() + power);
				holidayActivity02WeekPowerRank.setRealWeekPower(holidayActivity02WeekPowerRank.getRealWeekPower() + power);
				holidayActivity02WeekPowerRankService.updateActivity02WeekPowerRank(holidayActivity02WeekPowerRank);
				logger.error("modify week power rank end " + System.currentTimeMillis()+ " userid " + Security.getUserId());
			}
		}
		
	}

	@Override
	@Transactional
	public void updateUserPower(Long code, Long userId, Integer difference) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		params.put("difference", difference);
		repo.execute("$updateUserPower", params);
	}

}
