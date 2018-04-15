package com.lanking.uxb.service.nationalDayActivity01.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01TeaService;

@Transactional(readOnly = true)
@Service
public class NationalDayActivity01TeaServiceImpl implements NationalDayActivity01TeaService {

	@Autowired
	@Qualifier("NationalDayActivity01TeaRepo")
	private Repo<NationalDayActivity01Tea, Long> nationalDayActivity01TeaRepo;

	@Override
	public List<NationalDayActivity01Tea> getTopN(int count) {
		Params param = Params.param();
		param.put("topn", count);

		return nationalDayActivity01TeaRepo.find("$findTopN", param).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getTeaByUser(long userId) {
		Map<String, Object> data = new HashMap<>();
		Params param = Params.param();
		param.put("userId", userId);
		Map<String, Object> map = nationalDayActivity01TeaRepo.find("$findTeaByUser", param).get(Map.class);
		if (map == null) {
			return data;
		}

		Object homework_count = map.get("homework_count");
		Object commit_rate = map.get("commit_rate");
		Object scoreObj = map.get("score");
		Object rownumObj = map.get("rownum");

		data.put("rownum", Double.valueOf(String.valueOf(rownumObj)).intValue());
		NationalDayActivity01Tea tea = new NationalDayActivity01Tea();
		tea.setUserId(userId);
		tea.setCommitRate(Integer.parseInt(String.valueOf(commit_rate)));
		tea.setHomeworkCount(Integer.parseInt(String.valueOf(homework_count)));
		tea.setScore(Integer.parseInt(String.valueOf(scoreObj)));
		data.put("activityTea", tea);

		return data;
	}
}
