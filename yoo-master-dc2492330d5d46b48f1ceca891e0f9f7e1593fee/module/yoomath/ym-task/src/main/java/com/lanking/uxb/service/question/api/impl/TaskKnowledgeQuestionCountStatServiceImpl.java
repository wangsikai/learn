package com.lanking.uxb.service.question.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.stat.KnowledgeQuestionStat;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.KnowledgeQuestionCountStatService;
import com.lanking.uxb.service.question.api.TaskKnowledgeQuestionCountStatService;

@Service
public class TaskKnowledgeQuestionCountStatServiceImpl implements TaskKnowledgeQuestionCountStatService {

	private static final int KNOWLEDGE_SIZE = 50;
	@Autowired
	private KnowledgeQuestionCountStatService statService;

	@SuppressWarnings("rawtypes")
	@Override
	public void countKnowledge(int version) {
		// 执行Knowpoint游标
		if (version == 1) {
			executeKnowpoint();
		}
		if (version == 2) {
			executeKnowledgePoint();
		}

	}

	private void executeKnowledgePoint() {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MIN_VALUE, KNOWLEDGE_SIZE);
		CursorPage<Long, KnowledgePoint> cursorPage = statService.queryKnowledgePoint(cursorPageable);
		List<KnowledgeQuestionStat> kqsList = null;
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			kqsList = new ArrayList<KnowledgeQuestionStat>();
			List<KnowledgePoint> list = cursorPage.getItems();
			List<Long> codelist = new ArrayList<Long>();
			for (KnowledgePoint kt : list) {
				codelist.add(kt.getCode());
			}
			List<Map> maps = statService.getCountNewData(codelist);
			KnowledgeQuestionStat kqs = null;
			for (KnowledgePoint kt : list) {
				kqs = new KnowledgeQuestionStat();
				kqs.setName(kt.getName());
				kqs.setKnowpointCode(kt.getCode());
				kqs.setSubjectCode(kt.getSubjectCode());
				kqs.setPhaseCode(kt.getPhaseCode());
				kqs.setVersion(2);
				if (CollectionUtils.isNotEmpty(maps)) {
					for (Map m : maps) {
						if (Long.parseLong(m.get("knowledge_code").toString()) == kt.getCode()) {
							kqs.setInputCount(Integer.parseInt(m.get("total").toString()));
							kqs.setPassCount(Integer.parseInt(m.get("pass").toString()));
							kqs.setNopassCount(Integer.parseInt(m.get("nopass").toString()));
							kqs.setOnepassCount(Integer.parseInt(m.get("onepass").toString()));
							kqs.setNocheckCount(Integer.parseInt(m.get("editing").toString()));
						}

					}

				}
				kqsList.add(kqs);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, KNOWLEDGE_SIZE);
			cursorPage = statService.queryKnowledgePoint(cursorPageable);
			statService.saveKnowledgeQuestionStat(kqsList);
		}
	}

	private void executeKnowpoint() {
		CursorPageable<Integer> cursorPageable = CP.cursor(Integer.MIN_VALUE, KNOWLEDGE_SIZE);
		CursorPage<Integer, MetaKnowpoint> cursorPage = statService.queryKnowpoint(cursorPageable);
		List<KnowledgeQuestionStat> kqsList = null;
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			kqsList = new ArrayList<KnowledgeQuestionStat>();
			List<MetaKnowpoint> list = cursorPage.getItems();
			List<Integer> codelist = new ArrayList<Integer>();
			for (MetaKnowpoint kt : list) {
				codelist.add(kt.getCode());
			}
			List<Map> maps = statService.getCountData(codelist);
			KnowledgeQuestionStat kqs = null;
			for (MetaKnowpoint kt : list) {
				kqs = new KnowledgeQuestionStat();
				kqs.setName(kt.getName());
				kqs.setKnowpointCode(kt.getCode());
				kqs.setSubjectCode(kt.getSubjectCode());
				kqs.setPhaseCode(kt.getPhaseCode());
				kqs.setVersion(1);
				if (CollectionUtils.isNotEmpty(maps)) {
					for (Map m : maps) {
						if (Long.parseLong(m.get("meta_code").toString()) == kt.getCode()) {
							kqs.setInputCount(Integer.parseInt(m.get("total").toString()));
							kqs.setPassCount(Integer.parseInt(m.get("pass").toString()));
							kqs.setNopassCount(Integer.parseInt(m.get("nopass").toString()));
							kqs.setOnepassCount(Integer.parseInt(m.get("onepass").toString()));
							kqs.setNocheckCount(Integer.parseInt(m.get("editing").toString()));
						}

					}

				}
				kqsList.add(kqs);
			}
			Integer nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, KNOWLEDGE_SIZE);
			cursorPage = statService.queryKnowpoint(cursorPageable);
			statService.saveKnowledgeQuestionStat(kqsList);
		}
	}
}
