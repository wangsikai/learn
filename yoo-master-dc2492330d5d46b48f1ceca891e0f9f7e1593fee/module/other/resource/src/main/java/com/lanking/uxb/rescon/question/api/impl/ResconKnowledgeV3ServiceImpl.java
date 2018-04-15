package com.lanking.uxb.rescon.question.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.question.api.ResconKnowledgeV3Service;

/**
 * v3知识点转换相关接口实现.
 * 
 * @author wlche
 *
 */
@Transactional(readOnly = true)
@Service
public class ResconKnowledgeV3ServiceImpl implements ResconKnowledgeV3Service {

	@Autowired
	@Qualifier("BookQuestionRepo")
	Repo<BookQuestion, Long> bookQuestionRepo;

	@Autowired
	@Qualifier("ExamPaperQuestionRepo")
	private Repo<ExamPaperQuestion, Long> examQuestionRepo;

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Integer> findVersionNoV3Counts(Collection<Long> bookVersionIds) {
		if (CollectionUtils.isEmpty(bookVersionIds)) {
			return Maps.newHashMap();
		}
		List<Map> results = bookQuestionRepo
				.find("$findVersionNoV3Counts", Params.param("bookVersionIds", bookVersionIds)).list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>(results.size());
		for (Map m : results) {
			map.put(Long.parseLong(m.get("book_version_id").toString()), Integer.parseInt(m.get("cont").toString()));
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Integer> findPaperNoV3Counts(Collection<Long> examPaperIds) {
		if (CollectionUtils.isEmpty(examPaperIds)) {
			return Maps.newHashMap();
		}
		List<Map> results = examQuestionRepo.find("$findExamNoV3Counts", Params.param("examPaperIds", examPaperIds))
				.list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>(results.size());
		for (Map m : results) {
			map.put(Long.parseLong(m.get("exam_paper_id").toString()), Integer.parseInt(m.get("cont").toString()));
		}
		return map;
	}

	@Override
	public int findVersionNoV3Count(long versionId) {
		return (int) bookQuestionRepo.find("$findVersionNoV3Count", Params.param("bookVersionId", versionId)).count();
	}

	@Override
	public int findPaperNoV3Count(long examPaperId) {
		return (int) examQuestionRepo.find("$findExamNoV3Count", Params.param("examPaperId", examPaperId)).count();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Integer> findCatalogNoV3Counts(long versionId, Collection<Long> catalogIds) {
		if (CollectionUtils.isEmpty(catalogIds)) {
			return Maps.newHashMap();
		}
		Params params = Params.param("bookVersionId", versionId);
		if (CollectionUtils.isNotEmpty(catalogIds)) {
			params.put("catalogIds", catalogIds);
		}
		List<Map> results = bookQuestionRepo.find("$findCatalogNoV3Counts", params).list(Map.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>(results.size());
		for (Map m : results) {
			map.put(Long.parseLong(m.get("book_catalog_id").toString()), Integer.parseInt(m.get("cont").toString()));
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> calNoKnowledgePointL1(int subjectCode, long vendorId) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		List<Map> list = questionRepo
				.find("$calNoKnowledgePointL1", Params.param("subjectCode", subjectCode).put("vendorId", vendorId))
				.list(Map.class);
		for (Map m : list) {
			map.put(Integer.parseInt(m.get("code").toString()), Integer.parseInt(m.get("counts").toString()));
		}
		return map;
	}

	@Override
	public long getNoHasV3KPQuestionCount(long vendorId, Integer phaseCode) {
		Params params = Params.param("vendorId", vendorId);
		if (phaseCode != null) {
			params.put("phaseCode", phaseCode);
		}
		return questionRepo.find("$getNoHasV3KPQuestionCount", params).count();
	}

	@Override
	public long getSyncKPQuestionCount(long vendorId, Long knowledgeCode, Integer phaseCode) {
		Params params = Params.param("vendorId", vendorId);
		if (knowledgeCode != null) {
			params.put("kp", knowledgeCode);
		}
		if (phaseCode != null) {
			params.put("phaseCode", phaseCode);
		}
		return questionRepo.find("$getSyncKPQuestionCount", params).count();
	}

	@Override
	public long getReviewKPQuestionCount(long vendorId, Long knowledgeCode, Integer phaseCode) {
		Params params = Params.param("vendorId", vendorId);
		if (knowledgeCode != null) {
			params.put("kp", knowledgeCode);
		}
		if (phaseCode != null) {
			params.put("phaseCode", phaseCode);
		}
		return questionRepo.find("$getReviewKPQuestionCount", params).count();
	}
}
