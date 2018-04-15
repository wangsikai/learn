package com.lanking.uxb.service.nationalDayActivity01.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01StuService;

@Transactional(readOnly = true)
@Service
public class NationalDayActivity01StuServiceImpl implements NationalDayActivity01StuService {

	@Autowired
	@Qualifier("NationalDayActivity01StuRepo")
	private Repo<NationalDayActivity01Stu, Long> nationalDayActivity01StuRepo;

	@Override
	public List<NationalDayActivity01Stu> getTopN(int topn) {
		Params param = Params.param();
		param.put("topn", topn);

		return nationalDayActivity01StuRepo.find("$findNationalDayStuTopN", param).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getStuByUser(long userId) {
		Map<String, Object> data = new HashMap<>();
		Params param = Params.param();
		param.put("userId", userId);
		Map<String, Object> map = nationalDayActivity01StuRepo.find("$findStuByUser", param).get(Map.class);
		if (map == null) {
			return data;
		}

		Object right_count = map.get("right_count");
		Object rownumObj = map.get("rownum");

		data.put("rownum", Double.valueOf(String.valueOf(rownumObj)).intValue());
		NationalDayActivity01Stu tea = new NationalDayActivity01Stu();
		tea.setUserId(userId);
		tea.setRightCount(Long.valueOf(String.valueOf(right_count)));
		data.put("activityTea", tea);

		return data;
	}

}
