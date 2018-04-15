package com.lanking.uxb.zycon.holiday.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayHomeworkService;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryForm;

/**
 * @author xinyu.zhou
 */
@Service
@Transactional(readOnly = true)
public class ZycHolidayHomeworkServiceImpl implements ZycHolidayHomeworkService {

	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> repo;

	@Override
	public Page<HolidayHomework> page(HomeworkQueryForm form) {
		int offset = (form.getPage() - 1) * form.getSize();
		Pageable pageable = P.offset(offset, form.getSize());
		Params params = Params.param();
		if (StringUtils.isNotEmpty(form.getStartTime())) {
			params.put("startTime", form.getStartTime());
		}
		if (StringUtils.isNotEmpty(form.getEndTime())) {
			params.put("endTime", form.getEndTime());
		}
		if (StringUtils.isNotEmpty(form.getSchoolName())) {
			params.put("schoolName", "%" + form.getSchoolName() + "%");
		}
		Page<HolidayHomework> page = repo.find("$zycGetTodo", params).fetch(pageable);
		return page;
	}

	@Override
	public Map<Long, Long> getDistribute(Collection<Long> ids) {
		Params params = Params.param("ids", ids);
		Map<Long, Long> retMap = new HashMap<Long, Long>(ids.size());
		List<Map> list = repo.find("$zycGetDistribute", params).list(Map.class);
		for (Map map : list) {
			retMap.put(Long.valueOf(map.get("id").toString()), Long.valueOf(map.get("distribute").toString()));
		}
		return retMap;
	}

	@Override
	public Map<Long, Long> getStuHomeworkItmeSubmitCount(Collection<Long> ids) {
		Params params = Params.param("ids", ids);
		Map<Long, Long> retMap = new HashMap<Long, Long>(ids.size());
		List<Map> list = repo.find("$zycGetItemSubmitCount", params).list(Map.class);
		for (Map map : list) {
			retMap.put(Long.valueOf(map.get("id").toString()), Long.valueOf(map.get("submitcount").toString()));
		}
		return retMap;
	}

}
