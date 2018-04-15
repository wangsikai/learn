package com.lanking.uxb.rescon.exam.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory.OperateType;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperQuestionManage;
import com.lanking.uxb.rescon.exam.form.ExamForm;
import com.lanking.uxb.rescon.exam.form.QueryForm;
import com.lanking.uxb.rescon.question.api.ResconQuestion2TagManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;

/**
 * 
 * @since 教师端 v1.3.0 2017-8-2 添加新的标签处理
 *
 */
@Transactional(readOnly = true)
@Service
public class ResconExamManageImpl implements ResconExamManage {
	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> examRepo;
	@Autowired
	@Qualifier("ExamPaperHistoryRepo")
	private Repo<ExamPaperHistory, Long> examHisRepo;
	@Autowired
	@Qualifier("ExamPaperQuestionRepo")
	private Repo<ExamPaperQuestion, Long> examQuestionRepo;
	@Autowired
	@Qualifier("ExamPaperTopicRepo")
	private Repo<ExamPaperTopic, Long> examTopicRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconExamPaperQuestionManage resconExamPaperQuestionManage;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconSchoolQuestionManage resconSchoolQuestionManage;
	// @Autowired
	// private ResconQuestionTypeCacheService qtcService;

	@Autowired
	private ResconQuestion2TagManage question2TagManage;

	@Override
	public ExamPaper get(long id) {
		return examRepo.get(id);
	}

	@Override
	public Map<Long, ExamPaper> mget(List<Long> ids) {
		return examRepo.mget(ids);
	}

	@Transactional
	@Override
	public ExamPaper create(long userId, ExamForm examForm) {
		ExamPaper ep = new ExamPaper();
		Date date = new Date();
		ep.setName(examForm.getName());
		ep.setDistrictCode(examForm.getDistrictCode());
		ep.setPhaseCode(examForm.getPhaseCode());
		ep.setResourceCategoryCode(examForm.getTypeCode());
		ep.setSchoolId(examForm.getSchoolId());
		ep.setOwnSchoolId(examForm.getOwnSchoolId());
		ep.setSectionCode(examForm.getSectionCode());
		ep.setDifficulty(new BigDecimal(examForm.getDifficulty()));
		ep.setCreateId(userId);
		ep.setSubjectCode(examForm.getSubjectCode());
		ep.setTextbookCategoryCode(examForm.getTextBookCategoryCode());
		ep.setTextbookCode(examForm.getTextBookCode());
		ep.setYear(examForm.getYear());
		ep.setCreateAt(date);
		ep.setStatus(ExamPaperStatus.EDITING);
		ep.setUpdateAt(date);
		ep.setUpdateId(userId);
		return examRepo.save(ep);
	}

	@Transactional
	@Override
	public void edit(ExamForm examForm, Long userId) {
		ExamPaper ep = examRepo.get(examForm.getId());

		if (ep.getOwnSchoolId() != null && examForm.getOwnSchoolId() == null) {
			// 校本试卷变为普通试卷
			List<ExamPaperQuestion> oldExamQuestions = resconExamPaperQuestionManage.getExamQuestionByExam(ep.getId());
			Set<Long> deleteQuestions = new HashSet<Long>(oldExamQuestions.size());
			if (oldExamQuestions.size() > 0) {
				for (ExamPaperQuestion pq : oldExamQuestions) {
					deleteQuestions.add(pq.getQuestionId());
				}
				examQuestionRepo.execute("$removeQuestionSchool", Params.param("questionIds", deleteQuestions));
				List<Question> qs = questionManage.mgetList(deleteQuestions);
				List<Long> qss = new ArrayList<Long>(qs.size());
				for (Question question : qs) {
					// 只有已通过的题目进行校本题目计数
					if (question.getStatus() == CheckStatus.PASS) {
						qss.add(question.getId());
					}
				}
				if (qss.size() > 0) {
					questionManage.updateQuestionSchoolCount(ep.getOwnSchoolId(), -qss.size());
					resconSchoolQuestionManage.delSchoolQuestion(ep.getOwnSchoolId(), qss);
				}
			}
		}

		ep.setName(examForm.getName());
		ep.setDistrictCode(examForm.getDistrictCode());
		ep.setPhaseCode(examForm.getPhaseCode());
		ep.setResourceCategoryCode(examForm.getTypeCode());
		ep.setSchoolId(examForm.getSchoolId());
		ep.setOwnSchoolId(examForm.getOwnSchoolId());
		ep.setSectionCode(examForm.getSectionCode());
		// ep.setDifficulty(new BigDecimal(examForm.getDifficulty()));
		ep.setSubjectCode(examForm.getSubjectCode());
		ep.setTextbookCategoryCode(examForm.getTextBookCategoryCode());
		ep.setTextbookCode(examForm.getTextBookCode());
		ep.setYear(examForm.getYear());
		// ep.setCreateAt(new Date());
		ep.setUpdateAt(new Date());
		ep.setUpdateId(userId);
		examRepo.save(ep);
		ExamPaperHistory examHistory = new ExamPaperHistory();
		examHistory.setCreateAt(new Date());
		examHistory.setCreateId(userId);
		examHistory.setExamPaperId(examForm.getId());
		examHistory.setOperateType(OperateType.EDIT);
		examHisRepo.save(examHistory);
	}

	@Override
	public Page<ExamPaper> queryResconExam(QueryForm form) {
		int size = form.getPageSize();
		int p = form.getPage();
		int offset = (p - 1) * size;
		List<IndexTypeable> types = Lists.newArrayList();
		types.add(IndexType.EXAM_PAPER);// 类型BIZ.XXX
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 供应商
		if (form.getVendorId() != null) {
			qb.must(QueryBuilders.matchQuery("vendorId", form.getVendorId()));
		}
		if (null != form.getKey() && form.getKey() != "") {
			qb.must(QueryBuilders.matchQuery("name", form.getKey()));
		}
		if (StringUtils.isNotBlank(form.getExamCode())) {
			try {
				qb.must(QueryBuilders.matchQuery("id", Long.parseLong(form.getExamCode())));
			} catch (NumberFormatException e) {
				return new PageImpl<ExamPaper>(new ArrayList<ExamPaper>(0), 0,
						P.index(form.getPage(), form.getPageSize()));
			}
		}
		if (null != form.getCreateId()) {
			qb.must(QueryBuilders.termQuery("createId", form.getCreateId()));
		}
		if (null != form.getStatus()) {
			qb.must(QueryBuilders.termQuery("status", form.getStatus().getValue()));
		}
		if (null != form.getStartCreateAt()) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("createAt");
			rqb.gte(form.getStartCreateAt());
			qb.must(rqb);
		}
		if (null != form.getEndCreateAt()) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(form.getEndCreateAt());
			cal.add(Calendar.DATE, 1);
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("createAt");
			rqb.lt(cal.getTimeInMillis());
			qb.must(rqb);
		}
		if (null != form.getSubjectCode()) {
			qb.must(QueryBuilders.termQuery("subjectCode", form.getSubjectCode()));
		}
		if (null != form.getPhaseCode()) {
			qb.must(QueryBuilders.termQuery("phaseCode", form.getPhaseCode()));
		}
		if (null != form.getCategory()) {
			qb.must(QueryBuilders.termQuery("category", form.getCategory()));
		}
		if (null != form.getDistrictCode()) {
			qb.must(QueryBuilders.termQuery("districtCode", form.getDistrictCode()));
		}
		if (null != form.getSchoolId()) {
			qb.must(QueryBuilders.termQuery("schoolId", form.getSchoolId()));
		}
		if (null != form.getYear()) {
			// 判断是不是五年以前
			Calendar cal = Calendar.getInstance();
			int nowYear = cal.get(Calendar.YEAR);
			if ((nowYear - form.getYear()) >= 5) {
				RangeQueryBuilder rqb = QueryBuilders.rangeQuery("year");
				rqb.lte(form.getYear());
				qb.must(rqb);
			} else {
				qb.must(QueryBuilders.termQuery("year", form.getYear()));
			}
		}
		if (null != form.getMinDifficulty()) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			rqb.gte(form.getMinDifficulty().doubleValue());
			qb.must(rqb);
		}
		if (null != form.getMaxDifficulty()) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			rqb.lte(form.getMaxDifficulty().doubleValue());
			qb.must(rqb);
		}
		if (null != form.getTextBookCode()) {
			qb.must(QueryBuilders.termQuery("textbookCode", form.getTextBookCode()));
		}
		if (form.getTextBookCategoryCode() != null) {
			qb.must(QueryBuilders.termQuery("textbookcategoryCode", form.getTextBookCategoryCode()));
		}
		if (null != form.getSectionCode()) {
			qb.must(QueryBuilders.termQuery("sectionCode", form.getSectionCode()));
		}
		List<Order> orders = Lists.newArrayList();
		orders.add(new Order("_score", Direction.DESC));
		if (StringUtils.isNotBlank(form.getOrderBy()) && form.getOrder() != null) {
			orders.add(new Order(form.getOrderBy(), form.getOrder() ? Direction.DESC : Direction.ASC));
		}
		orders.add(new Order("createAt", Direction.DESC));
		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);
		List<Long> examIds = Lists.newArrayList();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				examIds.add(Long.valueOf(document.getId()));
			}
		}
		Map<Long, ExamPaper> examMap = this.mget(examIds);
		List<ExamPaper> examList = Lists.newArrayList();
		for (Long examId : examIds) {
			if (examMap.get(examId) != null) {
				examList.add(examMap.get(examId));
			}
		}
		return new PageImpl<ExamPaper>(examList, docPage.getTotalCount(), P.index(form.getPage(), form.getPageSize()));
	}

	@Transactional
	@Override
	public List<Long> updateExamStatus(long examId, ExamPaperStatus status) {
		ExamPaper examPaper = examRepo.get(examId);
		Integer resourceCategoryCode = examPaper.getResourceCategoryCode();
		QuestionCategoryType allAddType = null;
		List<Long> needUpdateQuestions = new ArrayList<Long>();
		// 详见resource_category 表
		switch (resourceCategoryCode) {
		// 模拟题
		case 111:
			allAddType = QuestionCategoryType.SIMULATION;
			break;
		// 真题
		case 112:
			allAddType = QuestionCategoryType.TRUTH;
			break;
		}
		if (status == ExamPaperStatus.PASS) {
			// 试卷发布则最后试卷最后两道题添加上压轴题标签
			List<ExamPaperQuestion> epQuestions = examQuestionRepo
					.find("$resconFindLastQuestion", Params.param("id", examId)).list();
			List<Long> questionIds = new ArrayList<Long>(epQuestions.size());

			List<ExamPaperTopic> topics = examTopicRepo.find("$getTopicsByExam", Params.param("examId", examId)).list();
			ExamPaperTopic lastTopic = null;
			List<Long> lastTopicQuestions = null;
			if (CollectionUtils.isNotEmpty(topics)) {
				lastTopic = topics.get(topics.size() - 1);
				lastTopicQuestions = Lists.newArrayList();
			}

			for (ExamPaperQuestion q : epQuestions) {
				questionIds.add(q.getQuestionId());
				if (lastTopic != null && lastTopic.getId().equals(q.getTopicId())) {
					lastTopicQuestions.add(q.getQuestionId());
				}
			}

			// 若是模拟题和真题的一种则打上相应的标签，且这份试卷中必须有题目才进行打标签操作
			if (CollectionUtils.isNotEmpty(questionIds)) {

				if (allAddType != null) {
					// qtcService.add(allAddType, questionIds);

					List<Question> questions = questionRepo.mgetList(questionIds);
					for (Question q : questions) {
						question2TagManage.systemAdd(Lists.newArrayList(q.getId()), QuestionTag.getTagCode(allAddType));
						needUpdateQuestions.add(q.getId());

						// List<QuestionCategoryType> types =
						// q.getCategoryTypes();
						// if (CollectionUtils.isEmpty(types)) {
						// types = new ArrayList<QuestionCategoryType>(1);
						// types.add(allAddType);
						// q.setCategoryTypes(types);
						//
						// needUpdateQuestions.add(q.getId());
						//
						// questionRepo.save(q);
						// } else {
						// if (!types.contains(allAddType)) {
						// types.add(allAddType);
						// q.setCategoryTypes(types);
						//
						// needUpdateQuestions.add(q.getId());
						//
						// questionRepo.save(q);
						// }
						// }
					}

					List<Long> last2QuestionIds = null;
					// 更新题目最后两题为压轴题
					if (lastTopicQuestions != null) {
						if (lastTopicQuestions.size() >= 2) {
							last2QuestionIds = new ArrayList<Long>(2);
							last2QuestionIds.add(lastTopicQuestions.get(lastTopicQuestions.size() - 1));
							last2QuestionIds.add(lastTopicQuestions.get(lastTopicQuestions.size() - 2));
						} else {
							last2QuestionIds = new ArrayList<Long>(1);
							last2QuestionIds.add(lastTopicQuestions.get(0));
						}
					}

					// qtcService.add(QuestionCategoryType.FINALE,
					// last2QuestionIds);
					question2TagManage.systemAdd(last2QuestionIds, QuestionTag.getTagCode(QuestionCategoryType.FINALE));
					needUpdateQuestions.addAll(last2QuestionIds);

					// questions = questionRepo.mgetList(last2QuestionIds);
					// for (Question q : questions) {
					// List<QuestionCategoryType> types = q.getCategoryTypes();
					// if (CollectionUtils.isEmpty(types)) {
					// types = new ArrayList<QuestionCategoryType>(1);
					// types.add(QuestionCategoryType.FINALE);
					// q.setCategoryTypes(types);
					//
					// needUpdateQuestions.add(q.getId());
					//
					// questionRepo.save(q);
					// } else {
					// if (!types.contains(QuestionCategoryType.FINALE)) {
					// types.add(QuestionCategoryType.FINALE);
					// q.setCategoryTypes(types);
					//
					// needUpdateQuestions.add(q.getId());
					//
					// questionRepo.save(q);
					// }
					// }
					// }
				}

			}

		}

		// 说明试卷重新进行编辑操作，此时需要将原先所有标签及后两题的"压轴"标签去除
		if (examPaper.getStatus() == ExamPaperStatus.PASS && status == ExamPaperStatus.EDITING) {
			List<ExamPaperQuestion> epQuestions = examQuestionRepo
					.find("$resconFindLastQuestion", Params.param("id", examId)).list();

			List<Long> questionIds = new ArrayList<Long>(epQuestions.size());

			List<ExamPaperTopic> topics = examTopicRepo.find("$getTopicsByExam", Params.param("examId", examId)).list();
			ExamPaperTopic lastTopic = null;
			List<Long> lastTopicQuestions = null;
			if (CollectionUtils.isNotEmpty(topics)) {
				lastTopic = topics.get(topics.size() - 1);
				lastTopicQuestions = Lists.newArrayList();
			}

			for (ExamPaperQuestion q : epQuestions) {
				questionIds.add(q.getQuestionId());
				if (lastTopic != null && lastTopic.getId().equals(q.getTopicId())) {
					lastTopicQuestions.add(q.getQuestionId());
				}
			}

			if (CollectionUtils.isNotEmpty(questionIds)) {
				if (allAddType != null) {
					question2TagManage.systemDel(questionIds, QuestionTag.getTagCode(allAddType));
					needUpdateQuestions.addAll(questionIds);

					// List<Long> removedIds = qtcService.remove(allAddType,
					// questionIds);
					// if (CollectionUtils.isNotEmpty(removedIds)) {
					// List<Question> questions =
					// questionRepo.mgetList(removedIds);
					// for (Question q : questions) {
					// List<QuestionCategoryType> categoryTypes =
					// q.getCategoryTypes();
					// if (CollectionUtils.isNotEmpty(categoryTypes)) {
					// if (categoryTypes.contains(allAddType)) {
					// categoryTypes.remove(allAddType);
					//
					// q.setCategoryTypes(categoryTypes);
					//
					// needUpdateQuestions.add(q.getId());
					//
					// questionRepo.save(q);
					// }
					// }
					// }
					// }

					List<Long> last2QuestionIds = null;
					if (lastTopicQuestions != null) {
						if (lastTopicQuestions.size() >= 2) {
							last2QuestionIds = new ArrayList<Long>(2);
							last2QuestionIds.add(lastTopicQuestions.get(lastTopicQuestions.size() - 1));
							last2QuestionIds.add(lastTopicQuestions.get(lastTopicQuestions.size() - 2));
						} else {
							last2QuestionIds = new ArrayList<Long>(1);
							last2QuestionIds.add(lastTopicQuestions.get(0));
						}
					}

					// List<Long> lastRemovedIds =
					// qtcService.remove(QuestionCategoryType.FINALE,
					// last2QuestionIds);
					question2TagManage.systemDel(last2QuestionIds, QuestionTag.getTagCode(QuestionCategoryType.FINALE));
					needUpdateQuestions.addAll(last2QuestionIds);

					// if (CollectionUtils.isNotEmpty(lastRemovedIds)) {
					// List<Question> questions =
					// questionRepo.mgetList(lastRemovedIds);
					// for (Question q : questions) {
					// List<QuestionCategoryType> types = q.getCategoryTypes();
					// if (CollectionUtils.isNotEmpty(types)) {
					// types.remove(QuestionCategoryType.FINALE);
					// q.setCategoryTypes(types);
					//
					// needUpdateQuestions.add(q.getId());
					//
					// questionRepo.save(q);
					// }
					// }
					// }
				}
			}
		}
		examRepo.execute("$updateExamStatus", Params.param("status", status.getValue()).put("examId", examId));

		return needUpdateQuestions;
	}

	@Override
	@Transactional
	public void updateExamUpdateAt(long examId, Long userId) {
		ExamPaper examPaper = examRepo.get(examId);
		if (examPaper != null) {
			examPaper.setUpdateAt(new Date());
			examPaper.setUpdateId(userId);
		}
	}

	@Override
	@Transactional
	public void updateExamAvgDifficulty(long examId, BigDecimal avgDifficulty) {
		ExamPaper examPaper = examRepo.get(examId);
		if (examPaper != null) {
			examPaper.setDifficulty(avgDifficulty);
			examPaper.setUpdateAt(new Date());
		}
	}

	@Override
	@Transactional
	@Async
	public void updateExamAvgDifficultyByQuestion(long questionId) {
		examQuestionRepo.execute("$updateExamAvgDifficultyByQuestion", Params.param("questionId", questionId));
	}

	@Override
	@Transactional
	public void changeQuestion(long examId, long oldQuestionId, long newQuestionId) {
		examQuestionRepo.execute("$changeQuestion",
				Params.param("examId", examId).put("oldQuestionId", oldQuestionId).put("newQuestionId", newQuestionId));
		this.updateExamAvgDifficultyByQuestion(newQuestionId);
	}
}
