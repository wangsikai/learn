package com.lanking.uxb.rescon.exam.api.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory.OperateType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.exam.api.ResconExamHistoryManage;

@Transactional(readOnly = true)
@Service
public class ResconExamHistoryManageImpl implements ResconExamHistoryManage {
	@Autowired
	@Qualifier("ExamPaperHistoryRepo")
	private Repo<ExamPaperHistory, Long> examHistoryRepo;

	@Override
	public List<ExamPaperHistory> getByOperate(Long examId, List<OperateType> operateTypes, Integer limit) {
		List<Integer> types = Lists.newArrayList();
		for (OperateType operateType2 : operateTypes) {
			types.add(operateType2.getValue());
		}
		List<ExamPaperHistory> ephList = examHistoryRepo.find("$getByOperate",
				Params.param("examId", examId).put("types", types).put("limit", limit)).list();
		Collections.reverse(ephList);
		return ephList;
	}

	@Override
	public Map<Long, List<ExamPaperHistory>> mgetByOperate(List<Long> examIds, List<OperateType> operateTypes) {
		List<Integer> types = Lists.newArrayList();
		for (OperateType operateType2 : operateTypes) {
			types.add(operateType2.getValue());
		}
		List<ExamPaperHistory> ephList = examHistoryRepo.find("$mgetByOperate",
				Params.param("examIds", examIds).put("operateType", types)).list();
		Map<Long, List<ExamPaperHistory>> historyMap = Maps.newHashMap();
		for (Long examId : examIds) {
			List<ExamPaperHistory> historyList = Lists.newArrayList();
			for (ExamPaperHistory examPaperHistory : ephList) {
				if (examPaperHistory.getExamPaperId().equals(examId)) {
					historyList.add(examPaperHistory);
				}
			}
			historyMap.put(examId, historyList);

		}
		return historyMap;
	}

	@Transactional
	@Override
	public void save(long userId, Long examId, OperateType type) {
		ExamPaperHistory ep = new ExamPaperHistory();
		ep.setCreateAt(new Date());
		ep.setCreateId(userId);
		ep.setExamPaperId(examId);
		ep.setOperateType(type);
		examHistoryRepo.save(ep);
	}

}
