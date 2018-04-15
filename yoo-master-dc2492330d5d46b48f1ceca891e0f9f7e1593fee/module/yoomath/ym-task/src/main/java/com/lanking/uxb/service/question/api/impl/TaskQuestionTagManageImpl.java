package com.lanking.uxb.service.question.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.TaskQuestionTagManage;

@Service
@Transactional(readOnly = true)
public class TaskQuestionTagManageImpl implements TaskQuestionTagManage {

	@Autowired
	@Qualifier("QuestionTagRepo")
	Repo<QuestionTag, Long> questionTagRepo;
	@Autowired
	@Qualifier("Question2TagRepo")
	Repo<Question2Tag, Long> question2TagRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Override
	public List<QuestionTag> listAll(Status status, QuestionTagType questionTagType) {
		Params params = Params.param();
		if (status != null) {
			params.put("status", status.getValue());
		}
		if (questionTagType != null) {
			params.put("questionTagType", questionTagType.getValue());
		}
		return questionTagRepo.find("$taskListAll", params).list();
	}

	@Override
	public Page<Question> findQuestionsForInitTag(Date now, Pageable pageable) {
		return questionRepo.find("$findQuestionsForInitTag", Params.param("nowdate", now)).fetch(pageable);
	}

	@Override
	public Page<ExaminationPoint> findExaminationPointForInitTag(Pageable pageable) {
		return questionRepo.find("$findExaminationPointForInitTag").fetch(pageable, ExaminationPoint.class);
	}

	@Override
	@Transactional
	public void handleInitQuestions(List<Question> questions) {
		if (CollectionUtils.isEmpty(questions)) {
			return;
		}
		List<Long> questionIds = new ArrayList<Long>(questions.size());
		for (Question question : questions) {
			if (CollectionUtils.isNotEmpty(question.getCategoryTypes())) {
				questionIds.add(question.getId());
			}
		}
		if (questionIds.size() > 0) {
			List<Question2Tag> all = question2TagRepo
					.find("$taskListByQuestions", Params.param("questionIds", questionIds)).list();
			Map<Long, List<Long>> map = new HashMap<Long, List<Long>>(questionIds.size());
			for (Question2Tag question2Tag : all) {
				List<Long> tagCodes = map.get(question2Tag.getQuestionId());
				if (tagCodes == null) {
					tagCodes = new ArrayList<Long>();
					map.put(question2Tag.getQuestionId(), tagCodes);
				}
				tagCodes.add(question2Tag.getTagCode());
			}

			List<Question2Tag> tags = new ArrayList<Question2Tag>();
			for (Question question : questions) {
				List<Long> hasTagCodes = map.get(question.getId());
				List<QuestionCategoryType> questionCategoryTypes = question.getCategoryTypes();
				for (QuestionCategoryType questionCategoryType : questionCategoryTypes) {
					Long tagCode = QuestionTag.getTagCode(questionCategoryType);
					if (hasTagCodes == null || !hasTagCodes.contains(tagCode)) {
						Question2Tag question2Tag = new Question2Tag();
						question2Tag.setQuestionId(question.getId());
						question2Tag.setSystem(true);
						question2Tag.setTagCode(tagCode);
						tags.add(question2Tag);
					}
				}
			}
			question2TagRepo.save(tags);
		}
	}

	@Override
	@Transactional
	public void handleInitExaminationPointQuestions(Set<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return;
		}
		List<Question2Tag> all = question2TagRepo.find("$taskListByQuestions", Params.param("questionIds", questionIds))
				.list();
		for (Question2Tag question2Tag : all) {
			if (question2Tag.getTagCode() == 5) {
				// 已经有了典型题标签
				questionIds.remove(question2Tag.getQuestionId());
			}
		}

		List<Question2Tag> tags = new ArrayList<Question2Tag>();
		for (Long questionId : questionIds) {
			Question2Tag question2Tag = new Question2Tag();
			question2Tag.setQuestionId(questionId);
			question2Tag.setSystem(true);
			question2Tag.setTagCode(5);
			tags.add(question2Tag);
		}
		if (CollectionUtils.isNotEmpty(tags)) {
			question2TagRepo.save(tags);
		}
	}

	@Override
	@Transactional
	public void deleteSystemQuestions() {
		question2TagRepo.execute("$taskDeleteSystemQuestions");
	}

	@Override
	public Page<Long> findHotQuestionsForInitTag(int minPulishCount, Pageable pageable) {
		return questionRepo.find("$findHotQuestionsForInitTag", Params.param("minPulishCount", minPulishCount))
				.fetch(pageable, Long.class);
	}

	@Override
	public Page<Long> findFallQuestionsForInitTag(int maxRightRate, int minDoNum, Pageable pageable) {
		return questionRepo
				.find("$findFallQuestionsForInitTag",
						Params.param("maxRightRate", maxRightRate).put("minDoNum", minDoNum))
				.fetch(pageable, Long.class);
	}

	@Override
	public Page<Long> findGoodQuestionsForInitTag(int minCollectCount, Pageable pageable) {
		return questionRepo.find("$findGoodQuestionsForInitTag", Params.param("minCollectCount", minCollectCount))
				.fetch(pageable, Long.class);
	}

	@Override
	@Transactional
	public void handleInitTagQuestions(List<Long> questionIds, long tagCode) {
		List<Question2Tag> tags = new ArrayList<Question2Tag>();
		for (Long questionId : questionIds) {
			Question2Tag question2Tag = new Question2Tag();
			question2Tag.setQuestionId(questionId);
			question2Tag.setSystem(true);
			question2Tag.setTagCode(tagCode);
			tags.add(question2Tag);
		}
		question2TagRepo.save(tags);
	}

	// TASK
	@Override
	public Page<Long> findHotQuestionsForDEL(int minPulishCount, Pageable pageable) {
		return question2TagRepo.find("$findHotQuestionsForDEL", Params.param("minPulishCount", minPulishCount))
				.fetch(pageable, Long.class);
	}

	// TASK
	@Override
	public Page<Long> findHotQuestionsForADD(int minPulishCount, Pageable pageable) {
		return question2TagRepo.find("$findHotQuestionsForADD", Params.param("minPulishCount", minPulishCount))
				.fetch(pageable, Long.class);
	}

	// TASK
	@Override
	public Page<Long> findFallQuestionsForDEL(int maxRightRate, int minDoNum, Pageable pageable) {
		return question2TagRepo
				.find("$findFallQuestionsForDEL", Params.param("maxRightRate", maxRightRate).put("minDoNum", minDoNum))
				.fetch(pageable, Long.class);
	}

	// TASK
	@Override
	public Page<Long> findFallQuestionsForADD(int maxRightRate, int minDoNum, Pageable pageable) {
		return question2TagRepo
				.find("$findFallQuestionsForADD", Params.param("maxRightRate", maxRightRate).put("minDoNum", minDoNum))
				.fetch(pageable, Long.class);
	}

	// TASK
	@Override
	public Page<Long> findGoodQuestionsForDEL(int minCollectCount, Pageable pageable) {
		return question2TagRepo.find("$findGoodQuestionsForDEL", Params.param("minCollectCount", minCollectCount))
				.fetch(pageable, Long.class);
	}

	// TASK
	@Override
	public Page<Long> findGoodQuestionsForADD(int minCollectCount, Pageable pageable) {
		return question2TagRepo.find("$findGoodQuestionsForADD", Params.param("minCollectCount", minCollectCount))
				.fetch(pageable, Long.class);
	}

	// TASK
	@Override
	@Transactional
	public void handleTaskTagQuestions(List<Long> delQuestionIds, List<Long> addQuestionIds, long tagCode) {
		if (CollectionUtils.isNotEmpty(delQuestionIds)) {
			question2TagRepo.execute("$deleteByQuestionAndTag",
					Params.param("questionIds", delQuestionIds).put("tagCode", tagCode));
		}
		if (CollectionUtils.isNotEmpty(addQuestionIds)) {
			List<Question2Tag> tags = new ArrayList<Question2Tag>();
			for (Long questionId : addQuestionIds) {
				Question2Tag question2Tag = new Question2Tag();
				question2Tag.setQuestionId(questionId);
				question2Tag.setSystem(true);
				question2Tag.setTagCode(tagCode);
				tags.add(question2Tag);
			}
			question2TagRepo.save(tags);
		}
	}
}
