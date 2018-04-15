package com.lanking.uxb.zycon.activity.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Cfg;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycHolidayActivity02ConfigService;
import com.lanking.uxb.zycon.activity.form.ZycHolidayActivity02ConfigForm;

@Transactional(readOnly = true)
@Service
public class ZycHolidayActivity02ConfigServiceImpl implements ZycHolidayActivity02ConfigService {

	@Autowired
	@Qualifier("HolidayActivity02Repo")
	private Repo<HolidayActivity02, Long> repo;

	@Override
	@Transactional
	public void save(ZycHolidayActivity02ConfigForm form) {
		HolidayActivity02 activity = new HolidayActivity02();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long currentCode = 1L;
		try {
			if (form.getCode() == null) {
				currentCode = this.maxCode();
				activity.setCode(currentCode);
				
			} else {
				this.deleteByCode(form.getCode());
				activity.setCode(form.getCode());
				currentCode = form.getCode();
			}

			activity.setStartTime(sdf.parse(form.getStartTime()));
			activity.setEndTime(sdf.parse(form.getEndTime()));
			activity.setCreateAt(new Date());
			// 活动配置
			HolidayActivity02Cfg cfg = new HolidayActivity02Cfg();
		    List<List<Date>> phases = new ArrayList<>();

		    List<Date> phase1 = new ArrayList<>();
		    Date phase1Start = (form.getPhase1Start() == null ? null : sdf.parse(form.getPhase1Start()));
		    Date phase1End = (form.getPhase1End() == null ? null : sdf.parse(form.getPhase1End()));
		    phase1.add(phase1Start);
		    phase1.add(phase1End);
		    phases.add(phase1);
		    
		    List<Date> phase2 = new ArrayList<>();
		    Date phase2Start = (form.getPhase2Start() == null ? null : sdf.parse(form.getPhase2Start()));
		    Date phase2End = (form.getPhase2End() == null ? null : sdf.parse(form.getPhase2End()));
		    phase2.add(phase2Start);
		    phase2.add(phase2End);
		    phases.add(phase2);
		    
		    List<Date> phase3 = new ArrayList<>();
		    Date phase3Start = (form.getPhase3Start() == null ? null : sdf.parse(form.getPhase3Start()));
		    Date phase3End = (form.getPhase3End() == null ? null : sdf.parse(form.getPhase3End()));
		    phase3.add(phase3Start);
		    phase3.add(phase3End);
		    phases.add(phase3);
		    
		    List<Date> phase4 = new ArrayList<>();
		    Date phase4Start = (form.getPhase4Start() == null ? null : sdf.parse(form.getPhase4Start()));
		    Date phase4End = (form.getPhase4End() == null ? null : sdf.parse(form.getPhase4End()));
		    phase4.add(phase4Start);
		    phase4.add(phase4End);
		    phases.add(phase4);
		    
		    cfg.setCode(currentCode);
		    cfg.setPhases(phases);
		    
		    activity.setCfg(cfg);
		    
		    //活动名称
		    if (form.getName() != null) {
				activity.setName(form.getName());
			}
		    
		    //活动介绍
		    if (form.getIntroduction() != null) {
				activity.setIntroduction(form.getIntroduction());
			}
		    
		    //活动规则
		    if (form.getRule() != null) {
				activity.setRule(form.getRule());
			}
		    
		    //活动奖励
		    if (form.getReward() != null) {
				activity.setReward(form.getReward());
			}
		    
			repo.save(activity);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public long maxCode() {
		Long maxCode = repo.find("$maxCode").get(Long.class);
		return maxCode == null ? 1 : maxCode + 1;
	}
	
	@Override
	public HolidayActivity02 queryByCode(Long code) {
		return repo.get(code);
	}

	@Transactional
	public void deleteByCode(Long code) {
		repo.execute("$deleteByCode", Params.param("code", code));
	}
	
}
