package com.lanking.uxb.service.activity.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ClassUser;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01ClassService;
import com.lanking.uxb.service.activity.api.HolidayActivity01ClassUserService;
import com.lanking.uxb.service.activity.convert.HolidayActivity01ClassConvert;
import com.lanking.uxb.service.activity.convert.HolidayActivity01ClassUserConvert;
import com.lanking.uxb.service.activity.value.VHolidayActivity01Class;
import com.lanking.uxb.service.activity.value.VHolidayActivity01ClassUser;

/**
 * 假期活动01参与活动的班级接口实现
 * 
 * @author peng.zhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01ClassServiceImpl implements HolidayActivity01ClassService {

	@Autowired
	@Qualifier("HolidayActivity01ClassRepo")
	private Repo<HolidayActivity01Class, Long> repo;

	@Autowired
	@Qualifier("HolidayActivity01ClassUserRepo")
	private Repo<HolidayActivity01ClassUser, Long> classUserRepo;
	@Autowired
	private HolidayActivity01ClassUserService holidayActivity01ClassUserService;
	@Autowired
	private HolidayActivity01ClassConvert holidayActivity01ClassConvert;
	@Autowired
	private HolidayActivity01ClassUserConvert holidayActivity01ClassUserConvert;

	@Override
	public HolidayActivity01Class get(long id) {
		return repo.get(id);
	}

	@Override
	public List<VHolidayActivity01Class> getByUserId(Long userId, Long code) {
		Params params = Params.param();
		params.put("userId", userId);
		if (code != null) {
			params.put("activityCode", code);
		}
		List<HolidayActivity01Class> clazzs = repo.find("$findClassByUserId", params).list();

		VHolidayActivity01Class holidayActivity01Class = new VHolidayActivity01Class();
		List<VHolidayActivity01Class> vclassList = new ArrayList<>();
		for (HolidayActivity01Class clazz : clazzs) {
			holidayActivity01Class = holidayActivity01ClassConvert.to(clazz);
			List<HolidayActivity01ClassUser> classUserList = holidayActivity01ClassUserService
					.getByClass(clazz.getClassId(), code);
			// 学生信息返回前3条
			if (classUserList != null && classUserList.size() > 3) {
				classUserList = classUserList.subList(0, 3);
			}

			holidayActivity01Class.setUserList(holidayActivity01ClassUserConvert.to(classUserList));
			vclassList.add(holidayActivity01Class);
		}

		return vclassList;
	}

	@Override
	public List<HolidayActivity01Class> getClassByUserId(long userId, long activityCode) {
		return repo.find("$findClassByUserId", Params.param("userId", userId).put("activityCode", activityCode)).list();
	}

	@Transactional
	@Override
	public void create(Collection<HolidayActivity01Class> clazzs) {
		repo.save(clazzs);
	}

	@Override
	public VHolidayActivity01Class getClassById(long id) {
		HolidayActivity01Class clazz = this.get(id);
		VHolidayActivity01Class holidayActivity01Class = holidayActivity01ClassConvert.to(clazz);
		holidayActivity01Class.setClassId(null);
		List<HolidayActivity01ClassUser> classUserList = holidayActivity01ClassUserService
				.getByClass(clazz.getClassId(), clazz.getActivityCode());
		List<VHolidayActivity01ClassUser> userList = new ArrayList<>();
		for (HolidayActivity01ClassUser user : classUserList) {
			VHolidayActivity01ClassUser v = holidayActivity01ClassUserConvert.to(user);
			v.setUserId(0);
			v.setMemberType(null);
			userList.add(v);
		}
		holidayActivity01Class.setUserList(userList);

		return holidayActivity01Class;
	}

}