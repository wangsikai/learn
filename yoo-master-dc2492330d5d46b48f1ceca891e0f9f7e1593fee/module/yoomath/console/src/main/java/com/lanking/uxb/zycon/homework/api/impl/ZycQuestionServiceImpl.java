package com.lanking.uxb.zycon.homework.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.zycon.homework.api.ZycCorrectingService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.homework.cache.ZycStudentHomeworkQuestionCacheService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Service(value = "zycHomeworkQuestionService")
@Transactional(readOnly = true)
public class ZycQuestionServiceImpl implements ZycQuestionService {

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;
	@Autowired
	private ZycStudentHomeworkQuestionCacheService zycStudentHomeworkQuestionCacheService;
	@Autowired
	private ZycCorrectingService zycCorrectingService;

	@Override
	public List<Question> zycFindStuHkQuestions(int size) {
		Params params = Params.param();
		Date nowDate = new Date();
		Date lastDate = DateUtils.addMinutes(nowDate, -Env.getInt("homework.allcommit.then"));
		params.put("size", size);
		params.put("nowDate", nowDate);
		params.put("lastDate", lastDate);
		List<Long> notInIds = zycStudentHomeworkQuestionCacheService.alreadyPushedIds();
		if (CollectionUtils.isEmpty(notInIds)) {
			params.put("notInIds", null);
		} else {
			params.put("notInIds", notInIds);
		}
		List<Map> list = questionRepo.find("$zycFindStuHKQuestions", params).list(Map.class);
		List<Question> qs = assembleQuestion(list);
		return qs;
	}

	@Override
	public List<Question> zycFindHKQuestions(long homeworkId) {
		Params params = Params.param();
		params.put("hkId", homeworkId);
		return questionRepo.find("$zycFindHKQuestions", params).list();
	}

	@Override
	public Long countUnCommitQuestions(int lastCommitMinute) {
		Date nowDate = new Date();
		Date lastDate = DateUtils.addMinutes(nowDate, -lastCommitMinute);
		Params params = Params.param();
		params.put("nowDate", nowDate);
		params.put("lastDate", lastDate);
		return questionRepo.find("$zycCountNotCorrectedQuestions", params).count();
	}

	@Override
	public Map<Long, Question> mget(Collection<Long> qids) {
		return questionRepo.mget(qids);
	}

	@Override
	public String getQuestionCode(long id) {
		return questionRepo.find("$zycGetQuestionCode", Params.param("id", id)).get(String.class);
	}

	@Override
	public Map<String, Object> zycFindConfirmQuestions(Pageable pageable) {
		Params params = Params.param();
		Date nowDate = new Date();

		Page<Map> page = questionRepo.find("$zycFindCorrected", params).fetch(pageable, Map.class);
		List<Question> questions = assembleQuestion(page.getItems());
		Map<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("totalPage", page.getPageCount());
		retMap.put("totalCount", page.getTotalCount());
		retMap.put("items", questions);

		return retMap;
	}

	private List<Question> assembleQuestion(List<Map> list) {
		List<Question> questions = Lists.newArrayList();
		for (Map m : list) {
			Question q = new Question();
			q.setId(Long.valueOf(m.get("id").toString()));
			q.setCode((String) m.get("code"));
			q.setTypeCode(Integer.valueOf(m.get("type_code").toString()));
			q.setType((Question.Type.findByValue(Integer.valueOf(m.get("type").toString()))));
			q.setContent((String) m.get("content"));
			q.setSubFlag((Boolean) m.get("sub_flag"));
			if (q.isSubFlag()) {
				q.setParentId(Long.valueOf(m.get("parent_id").toString()));
			}
			q.setChoiceA((String) m.get("choice_a"));
			q.setChoiceB((String) m.get("choice_b"));
			q.setChoiceC((String) m.get("choice_c"));
			q.setChoiceD((String) m.get("choice_d"));
			q.setChoiceE((String) m.get("choice_e"));
			q.setChoiceF((String) m.get("choice_f"));
			q.setAnswerNumber(Integer.valueOf(m.get("answer_number").toString()));
			q.setStatus(CheckStatus.findByValue(Integer.valueOf(m.get("status").toString())));
			q.setDelStatus(Status.findByValue(Integer.valueOf(m.get("del_status").toString())));
			if (m.get("choice_format") != null) {
				q.setChoiceFormat(ChoiceFormat.findByValue(Integer.valueOf(m.get("choice_format").toString())));
			}
			q.setStudentHomeworkId(Long.valueOf(m.get("studenthomeworkid").toString()));
			q.setHomeworkId(Long.valueOf(m.get("homeworkid").toString()));
			q.setStudentQuestionId(Long.valueOf(m.get("studentquestionid").toString()));
			q.setAnalysis((String) m.get("analysis"));
			q.setNewCorrect(Boolean.valueOf(m.get("newcorrect").toString()));
			q.setAnswer(true);

			zycStudentHomeworkQuestionCacheService.push(q.getStudentQuestionId());

			questions.add(q);
		}

		return questions;
	}

	@Override
	public Map<String, Object> zycFindConfirmQuestionsByCode(Pageable pageable, String questionCode) {
		Params params = Params.param();
		Date nowDate = new Date();
		params.put("questionCode", questionCode);

		Page<Map> page = questionRepo.find("$zycFindCorrectedById", params).fetch(pageable, Map.class);
		List<Question> questions = assembleQuestion(page.getItems());
		Map<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("totalPage", page.getPageCount());
		retMap.put("totalCount", page.getTotalCount());
		retMap.put("items", questions);

		return retMap;
	}

	@Override
	public Question zycFindQuestion(long id) {
		return questionRepo.get(id);
	}
}
