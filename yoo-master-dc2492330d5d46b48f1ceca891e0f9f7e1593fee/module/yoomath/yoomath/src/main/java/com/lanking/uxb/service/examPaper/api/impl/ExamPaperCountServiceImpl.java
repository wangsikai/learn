package com.lanking.uxb.service.examPaper.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperCount;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examPaper.api.ExamPaperCountService;

/**
 * 试卷统计接口
 * 
 * @author zemin.song
 *
 * @version 2017年2月13日
 */
@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ExamPaperCountServiceImpl implements ExamPaperCountService {

	@Autowired
	@Qualifier("ExamPaperCountRepo")
	private Repo<ExamPaperCount, Long> repo;

	@Override
	public int updateCount(long examPaperId, int dayOfN, int nDay) {
		return repo.execute("$updatePaperCount",
				Params.param("examPaperId", examPaperId).put("dayOfN", dayOfN).put("nDay", nDay));
	}

	@Transactional
	@Override
	public void addOneClick(long examPaperId, int dayOfN, int nDay) {
		if (updateCount(examPaperId, dayOfN, nDay) == 0) {
			ExamPaperCount ep = new ExamPaperCount();
			ep.setClickCount(1);
			ep.setDayOfN(dayOfN);
			ep.setExamPaperId(examPaperId);
			ep.setnDay(nDay);
			ep.setUpdateAt(new Date());
			repo.save(ep);
		}
	}

	@Override
	public Map<Long, Long> get(long examPaperId, Integer dayOfN, int nDay) {
		return repo.find("$findPaperCount",
				Params.param("examPaperIds", examPaperId).put("dayOfN", dayOfN).put("nDay", nDay)).get(Map.class);

	}

	@Override
	public Map<Long, Long> mget(Collection<Long> examPaperIds, int nDay) {
		List<Map> countList = repo
				.find("$findPaperCount", Params.param("examPaperIds", examPaperIds).put("nDay", nDay)).list(Map.class);
		Map<Long, Long> paperCountMap = new HashMap<Long, Long>(countList.size());
		for (Map map : countList) {
			paperCountMap.put(Long.parseLong(map.get("id").toString()), Long.parseLong(map.get("count").toString()));
		}
		return paperCountMap;
	}
}
