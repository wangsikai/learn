package com.lanking.uxb.service.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserCategoryGrade;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserCategoryGradeService;

/**
 * 假期活动01接口实现
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01UserCategoryGradeServiceImpl implements HolidayActivity01UserCategoryGradeService {
	@Autowired
	@Qualifier("HolidayActivity01UserCategoryGradeRepo")
	private Repo<HolidayActivity01UserCategoryGrade, Long> repo;

	@Override
	public HolidayActivity01UserCategoryGrade get(long code, long userId) {
		Params params = Params.param();
		params.put("userId", userId);
		params.put("code", code);
		
		return repo.find("$queryHolidayActivity01UserCategoryGrade", params).get();
	}

	@Transactional
	@Override
	public void create(HolidayActivity01UserCategoryGrade userCategoryGrade) {
		repo.save(userCategoryGrade);
	}

	@Transactional
	@Override
	public void update(HolidayActivity01UserCategoryGrade userCategoryGrade) {
		Params params = Params.param();
		params.put("id", userCategoryGrade.getId());
		params.put("grade", userCategoryGrade.getGrade());
		params.put("category", userCategoryGrade.getTextbookCategoryCode());
		repo.find("$updateHolidayActivity01UserCategoryGrade", params).execute();
	}
	
}
