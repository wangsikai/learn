package com.lanking.uxb.rescon.exam.api.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperStatisticService;

/**
 * @see ResconExamPaperStatisticService
 * @author xinyu.zhou
 * @since 2.0.3
 */
@Service
@Transactional(readOnly = true)
public class ResconExamPaperStatisticServiceImpl implements ResconExamPaperStatisticService {
	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> repo;

	@Override
	public Map<String, Map<String, Long>> countStatistic() {
		List<Map> ret = repo.find("$resconCountStatistic", Params.param()).list(Map.class);
		Map<String, Map<String, Long>> retMap = new HashMap<String, Map<String, Long>>(2);
		for (Map m : ret) {
			String phaseName = (String) m.get("phase_name");
			String subjectName = (String) m.get("subject_name") + ":" + ((BigInteger) m.get("code")).intValue();
			Long num = null;
			BigInteger id = (BigInteger) m.get("id");
			if (id == null) {
				num = 0L;
			} else {
				num = ((BigInteger) m.get("num")).longValue();
			}

			Map<String, Long> phaseMap = retMap.get(phaseName);
			if (phaseMap == null) {
				phaseMap = Maps.newHashMap();
			}

			phaseMap.put(subjectName, num);
			retMap.put(phaseName, phaseMap);
		}
		return retMap;
	}

	@Override
	public Map<ExamPaperStatus, Long> countBySubject(int subjectCode) {
		Params params = Params.param("subjectCode", subjectCode);
		List<Map> resMap = repo.find("$resconCountBySubject", params).list(Map.class);
		Map<ExamPaperStatus, Long> retMap = new HashMap<ExamPaperStatus, Long>(resMap.size());

		for (Map m : resMap) {
			Integer status = Integer.parseInt(m.get("status").toString());
			retMap.put(ExamPaperStatus.findByValue(status), ((BigInteger) m.get("num")).longValue());
		}
		return retMap;
	}

	@Override
	public Map<CheckStatus, Integer> countExamQuestionCheckStatusById(long id) {
		return countExamQuestionCheckStatusByIds(Lists.newArrayList(id)).get(id);
	}

	@Override
	public Map<Long, Map<CheckStatus, Integer>> countExamQuestionCheckStatusByIds(Collection<Long> ids) {
		Params params = Params.param("ids", ids);

		List<Map> resMap = repo.find("$resconCountQuestionStatus", params).list(Map.class);
		Map<Long, Map<CheckStatus, Integer>> retMap = Maps.newHashMap();
		for (Map m : resMap) {
			long id = ((BigInteger) m.get("id")).longValue();
			Map<CheckStatus, Integer> statusMap = retMap.get(id);
			if (statusMap == null) {
				statusMap = Maps.newHashMap();
			}

			Integer status = Integer.parseInt(m.get("status").toString());
			statusMap.put(CheckStatus.findByValue(status), ((BigInteger) m.get("num")).intValue());

			retMap.put(id, statusMap);
		}
		return retMap;
	}

}
