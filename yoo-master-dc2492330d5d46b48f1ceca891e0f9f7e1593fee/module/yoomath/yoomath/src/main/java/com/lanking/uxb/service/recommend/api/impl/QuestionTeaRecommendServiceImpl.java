package com.lanking.uxb.service.recommend.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.recommend.api.QuestionTeaRecommendService;
import com.lanking.uxb.service.recommend.cache.TeaRecommendCacheService;
import com.lanking.uxb.service.recommend.form.ResourceRecommendForm;
import com.lanking.uxb.service.recommend.type.RecommendType;
import com.lanking.uxb.service.resources.api.QuestionSimilarService;
import com.lanking.uxb.service.user.cache.TeacherOperationCacheService;

import httl.util.CollectionUtils;

@Service
public class QuestionTeaRecommendServiceImpl implements QuestionTeaRecommendService {
	@Autowired
	private TeaRecommendCacheService teaRecommendCacheService;
	@Autowired
	private TeacherOperationCacheService teacherOperationCacheService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private QuestionSimilarService questionSimilarService;

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("UserSchoolBookRepo")
	private Repo<UserSchoolBook, Long> userSchoolBookRepo;

	@Autowired
	@Qualifier("BookVersionRepo")
	private Repo<BookVersion, Long> bookVersionRepo;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Question> refreshNewQuestions(long teacherId, ResourceRecommendForm form) {
		// 获取教师当前设置的进度
		Long sectionCode = teacherOperationCacheService.getProgress(teacherId, TeacherOperationCacheService.PROGRESS);
		// sectionCode = 1122020101L;

		// 当前教师设置的提取类型
		List<Integer> recommendTypes = teacherOperationCacheService.getRecommendSource(teacherId);
		List<Long> sectionCodes = null;
		if (sectionCode == null) {
			return Lists.newArrayList();
		} else {
			Section section = sectionService.get(sectionCode);
			if (section.isComprehensiveSection()) {
				// 本章综合与测试，找章节下面的所有章节
				sectionCodes = sectionService.findSectionChildren(section.getPcode());
			} else {
				sectionCodes = Lists.newArrayList(sectionCode);
			}
		}

		Set<Long> eliminateQuestionIds = Sets.newHashSet(); // 需要排除的习题

		// 排除缓存中的题目
		Set<Long> initCacheQuestionIds = Sets.newHashSet(); // 初次整理的缓存习题
		Map<String, Object> questionCacheMap = teaRecommendCacheService.getQuestionRecommend(teacherId);
		if (questionCacheMap != null) {
			List<Long> questionIds = (List<Long>) questionCacheMap.get("questionIds");
			eliminateQuestionIds.addAll(questionIds);
			initCacheQuestionIds.addAll(questionIds);

			// 缓存题目中有重复题的展示题也要排除
			List<Question> questions = questionRepo.mgetList(questionIds);
			for (Question question : questions) {
				if (question.getSameShowId() != null) {
					eliminateQuestionIds.add(question.getSameShowId());
				}
			}
		}
		List<Question> questions = new ArrayList<Question>();

		if (CollectionUtils.isNotEmpty(recommendTypes)) {
			// 个人推荐设置不空的情况
			if (recommendTypes.contains(RecommendType.COMMON_BOOK.getValue())) {
				// 1、取教辅习题
				questions.addAll(this.fromTeaBook(teacherId, sectionCodes, eliminateQuestionIds, 4, form));
			}
			if (recommendTypes.contains(RecommendType.HOT_QUESTION.getValue())) {
				// 2、取热门习题
				questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, 2, form));
			}
			if (recommendTypes.contains(RecommendType.FALLIBLE_QUESTION.getValue())) {
				// 3、取错题
				questions.addAll(this.fromFall(teacherId, sectionCodes, eliminateQuestionIds, 1, form));
			}
			if (recommendTypes.contains(RecommendType.COLLECTION_QUESTION.getValue())) {
				// 4、取好题
				questions.addAll(this.fromGood(teacherId, sectionCodes, eliminateQuestionIds, 1, form));
			}
			if (recommendTypes.contains(RecommendType.WEAK_KNOWLEDGEPOINT.getValue())) {
				// 5、取班级薄弱知识点习题
				questions.addAll(this.fromClassWeak(teacherId, sectionCodes, eliminateQuestionIds, 1, form));
			}
			if (recommendTypes.contains(RecommendType.EXAMINATION_POINT.getValue())) {
				// 6、重要考点中的典型题
				questions.addAll(this.fromExampoint(sectionCodes, eliminateQuestionIds, 1, form));
			}
		} else {
			// 个人推荐设置为空，全部取自热门
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, 10, form));
		}

		List<Long> questionIds = new ArrayList<Long>();
		for (Question question : questions) {
			questionIds.add(question.getId());
		}
		if (questions.size() < 10) {
			eliminateQuestionIds.addAll(questionIds);
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, 10 - questionIds.size(), form));
		}

		// 去除重复题
		Set<Long> sameShowIds = new HashSet<Long>();
		for (Question question : questions) {
			if (question.getSameShowId() != null) {
				sameShowIds.add(question.getSameShowId());
			}
		}
		if (sameShowIds.size() > 0) {
			Map<Long, Question> sameShowQuestions = questionRepo.mget(sameShowIds);
			for (int i = 0; i < questions.size(); i++) {
				if (questions.get(i).getSameShowId() != null) {
					Question sameShowQuestion = sameShowQuestions.get(questions.get(i).getSameShowId());
					if (sameShowQuestion.getStatus() == CheckStatus.PASS) {
						sameShowQuestion.setSource(questions.get(i).getSource()); // 填充来源
						questions.set(i, sameShowQuestion);
					}
				}
			}
			eliminateQuestionIds.addAll(sameShowIds);
		}

		// 去除相似题
		Map<Long, Question> questionMap = new HashMap<Long, Question>(questions.size());
		Set<Long> hasLikeIds = new HashSet<Long>();
		for (Question question : questions) {
			if (question.isHasSimilar()) {
				hasLikeIds.add(question.getId());
			}
			questionMap.put(question.getId(), question);
		}
		if (hasLikeIds.size() > 0) {
			Map<Long, QuestionSimilar> questionSimilarMap = questionSimilarService.mGetByQuestion(hasLikeIds);
			Set<Long> removeIds = new HashSet<Long>(); // 被移除的习题，不再处理相关相似题
			for (QuestionSimilar qs : questionSimilarMap.values()) {
				if (removeIds.contains(qs.getBaseQuestionId())) {
					continue;
				}
				List<Long> likeQuestions = qs.getLikeQuestions();
				for (long likeQuestionId : likeQuestions) {
					if (likeQuestionId != qs.getBaseQuestionId()) {
						questionMap.remove(likeQuestionId);
						removeIds.add(likeQuestionId);
						eliminateQuestionIds.add(likeQuestionId);
					}
				}
			}

			List<Question> newQuestions = new ArrayList<Question>();
			for (Question question : questions) {
				if (questionMap.get(question.getId()) != null) {
					newQuestions.add(question);
				}
			}
			questions = newQuestions;
		}

		// questions 列表去重
		if (questions.size() > 0) {
			Set<Long> delSameQSet = new HashSet<Long>();
			delSameQSet.addAll(initCacheQuestionIds);
			for (int i = questions.size() - 1; i >= 0; i--) {
				if (delSameQSet.contains(questions.get(i).getId().longValue())) {
					questions.remove(i);
				} else {
					delSameQSet.add(questions.get(i).getId());
				}
			}
		}

		// 最后直接从章节习题对应关系表中补数据
		if (questions.size() < 10) {
			List<Long> tempQuestionIds = new ArrayList<Long>(questions.size());
			for (Question question : questions) {
				tempQuestionIds.add(question.getId()); // 章节内若有重复题，则对应题不取
			}
			questions.addAll(this.fromQuestionSection(sectionCodes, eliminateQuestionIds, tempQuestionIds,
					10 - questions.size(), form));
		}

		if (CollectionUtils.isNotEmpty(questions)) {
			// 保存缓存
			questionIds = new ArrayList<Long>();
			List<String> sources = new ArrayList<String>(questions.size());
			for (Question question : questions) {
				questionIds.add(question.getId());
				sources.add(question.getSource());
			}
			if (form == null) {
				teaRecommendCacheService.setQuestionRecommend(teacherId, sectionCode, recommendTypes, questionIds,
						sources);
			}
			return questions;
		} else {
			if (form == null) {
				teaRecommendCacheService.setQuestionRecommend(teacherId, sectionCode, recommendTypes,
						Lists.newArrayList(), Lists.newArrayList());
			}
			return Lists.newArrayList();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Question> getQuestionsFromCache(long teacherId) {
		Map<String, Object> questionMap = teaRecommendCacheService.getQuestionRecommend(teacherId);
		if (questionMap == null) {
			return this.refreshNewQuestions(teacherId, null);
		} else {
			// 获取教师当前设置的进度
			Long sectionCode = teacherOperationCacheService.getProgress(teacherId,
					TeacherOperationCacheService.PROGRESS);

			long sectionCodeCahce = 0;
			if (questionMap.get("sectionCode") != null) {
				sectionCodeCahce = Long.parseLong(questionMap.get("sectionCode").toString());
			}
			if (sectionCode != sectionCodeCahce) {
				return this.refreshNewQuestions(teacherId, null);
			}

			// 获取教师当前设置的推荐类型
			List<Integer> recommendTypes = teacherOperationCacheService.getRecommendSource(teacherId);
			if (CollectionUtils.isNotEmpty(recommendTypes) && questionMap.get("recommendTypes") != null) {
				List<Integer> recommendTypesCache = (List<Integer>) questionMap.get("recommendTypes");
				Collections.sort(recommendTypes);
				Collections.sort(recommendTypesCache);
				if (recommendTypes.size() != recommendTypesCache.size()) {
					return this.refreshNewQuestions(teacherId, null);
				}
				for (int i = 0; i < recommendTypes.size(); i++) {
					if (recommendTypes.get(i).intValue() != recommendTypesCache.get(i).intValue()) {
						return this.refreshNewQuestions(teacherId, null);
					}
				}
			}

			Long timestamp = (Long) questionMap.get("timestamp");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timestamp);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			if (format.format(new Date()).equals(format.format(cal.getTime()))) {
				List<Long> questionIds = (List<Long>) questionMap.get("questionIds");
				List<String> sources = (List<String>) questionMap.get("sources");
				if (CollectionUtils.isNotEmpty(questionIds)) {
					Map<Long, Question> qMap = questionRepo.mget(questionIds);
					List<Question> questions = new ArrayList<Question>(questionIds.size());
					for (int i = 0; i < questionIds.size(); i++) {
						Question question = qMap.get(questionIds.get(i));
						if (CollectionUtils.isNotEmpty(sources)) {
							question.setSource(sources.get(i));
						}
						questions.add(question);
					}
					return questions;
				}
			}
		}
		return this.refreshNewQuestions(teacherId, null);
	}

	/**
	 * 2017.11.1新增资源推荐添加题目类型、题目难度、知识点筛选条件(senhao.wang)
	 * 
	 * @param params
	 * @param form
	 * @return
	 */
	private Params handleParams(Params params, ResourceRecommendForm form) {
		if (form != null) {
			if (form.getQuestionType() != null) {
				params.put("questionType", form.getQuestionType().getValue());
			}
			if (form.getDifficultyType() != null) {
				if (form.getDifficultyType() == ResourceRecommendForm.DifficultyType.BASIS) {
					params.put("diff1", 0.8);
					// 小于等于1
					params.put("diff2", 1.01);
				} else if (form.getDifficultyType() == ResourceRecommendForm.DifficultyType.IMPROVE) {
					params.put("diff1", 0.4);
					params.put("diff2", 0.8);
				} else if (form.getDifficultyType() == ResourceRecommendForm.DifficultyType.HARD) {
					params.put("diff1", 0);
					params.put("diff2", 0.4);
				}
			}
			if (form.getKnowledgeCode() != null) {
				params.put("kpCode", form.getKnowledgeCode());
			}
		}
		return params;
	}

	/**
	 * 通过教辅获取题目.
	 * 
	 * @param teacherId
	 *            教师ID
	 * @param sectionCodes
	 *            教师进度章节
	 * @param eliminateQuestionIds
	 *            需要排除的习题ID，不得为null
	 * @param num
	 *            获取数量
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	private List<Question> fromTeaBook(long teacherId, List<Long> sectionCodes, Set<Long> eliminateQuestionIds, int num,
			ResourceRecommendForm form) {
		if (eliminateQuestionIds == null) {
			throw new IllegalArgException();
		}
		List<Question> questions = new ArrayList<Question>(num);

		List<UserSchoolBook> userSchoolBooks = userSchoolBookRepo
				.find("$getUserBook", Params.param("userId", teacherId)).list();

		if (CollectionUtils.isNotEmpty(userSchoolBooks)) {
			// 当前教师设置的进度
			Params params = Params.param("teacherId", teacherId);
			if (sectionCodes.size() == 1) {
				params.put("sectionCode", sectionCodes.get(0));
			} else {
				params.put("sectionCodes", sectionCodes);
			}
			if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
				params.put("eliminateQuestionIds", eliminateQuestionIds);
			}
			// 2017.11.1新增资源推荐添加题目类型、题目难度、知识点筛选条件(senhao.wang)
			params = this.handleParams(params, form);
			Set<Long> bookIds = new HashSet<Long>(userSchoolBooks.size());
			for (UserSchoolBook userSchoolBook : userSchoolBooks) {
				bookIds.add(userSchoolBook.getBookId());
			}
			List<BookVersion> bookVersions = bookVersionRepo
					.find("$getBookVersionByBookIds", Params.param("bookIds", bookIds)).list();
			Map<Long, BookVersion> bookVersionMap = new HashMap<Long, BookVersion>(bookVersions.size());
			for (BookVersion bookVersion : bookVersions) {
				bookVersionMap.put(bookVersion.getBookId(), bookVersion);
			}

			List<Map> list = questionRepo.find("$listRecommendQuestionFromUserBook", params).list(Map.class);
			if (CollectionUtils.isNotEmpty(list)) {
				Map<Long, List<Long>> bookQuestionIdMap = new HashMap<Long, List<Long>>(userSchoolBooks.size());
				Set<Long> allQuestionIds = new HashSet<Long>();
				for (Map map : list) {
					long questionId = Long.parseLong(map.get("question_id").toString());
					long bookId = Long.parseLong(map.get("book_id").toString());
					List<Long> qids = bookQuestionIdMap.get(bookId);
					if (qids == null) {
						qids = new ArrayList<Long>();
						bookQuestionIdMap.put(bookId, qids);
					}
					qids.add(questionId);
					allQuestionIds.add(questionId);
				}
				Map<Long, Question> allQuestionMap = questionRepo.mget(allQuestionIds);

				// 过滤书本，没有题目的去除
				for (int i = userSchoolBooks.size() - 1; i >= 0; i--) {
					UserSchoolBook userSchoolBook = userSchoolBooks.get(i);
					List<Long> qids = bookQuestionIdMap.get(userSchoolBook.getBookId());
					if (CollectionUtils.isEmpty(qids)) {
						userSchoolBooks.remove(i);
					}
				}

				if (userSchoolBooks.size() > 0) {
					for (int i = 0; i < allQuestionMap.size(); i++) {
						UserSchoolBook userSchoolBook = userSchoolBooks.get(i % userSchoolBooks.size());
						List<Long> qids = bookQuestionIdMap.get(userSchoolBook.getBookId());
						if (CollectionUtils.isNotEmpty(qids)) {
							Question question = allQuestionMap.get(qids.get(0));
							if (questions.contains(question)) {
								// 有重复的题需要抛弃
								qids.remove(0);
								continue;
							}
							BookVersion bookVersion = bookVersionMap.get(userSchoolBook.getBookId());
							if (bookVersion == null) {
								question.setSource(RecommendType.COMMON_BOOK.getName(null));
							} else {
								String name = bookVersion.getShortName();
								if (StringUtils.isEmpty(name)) {
									name = bookVersion.getName();
								}
								if (StringUtils.getJsUnicodeLength(name) > 10) {
									name = StringUtils.cutByJsUnicodeLength(name, 10) + "...";
								}
								question.setSource(RecommendType.COMMON_BOOK.getName(name));
							}
							questions.add(question);
							eliminateQuestionIds.add(question.getId());
							qids.remove(0);
						}
						if (questions.size() >= num) {
							break;
						}
					}
				}
			}
		}

		if (questions.size() < num) {
			// 题目不够从热门题中获取
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, num - questions.size(), form));
		}

		return questions;
	}

	/**
	 * 获取热门习题.
	 * 
	 * @param sectionCodes
	 *            章节编码
	 * @param eliminateQuestionIds
	 *            排除的习题集合
	 * @param num
	 *            获取数量
	 * @return
	 */
	@Transactional(readOnly = true)
	private List<Question> fromHot(List<Long> sectionCodes, Set<Long> eliminateQuestionIds, int num,
			ResourceRecommendForm form) {
		Params params = Params.param("num", num);
		if (sectionCodes.size() == 1) {
			params.put("sectionCode", sectionCodes.get(0));
		} else {
			params.put("sectionCodes", sectionCodes);
		}
		if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
			params.put("eliminateQuestionIds", eliminateQuestionIds);
		}
		params = this.handleParams(params, form);
		List<Long> qids = questionRepo.find("$listRecommendQuestionFromHot", params).list(Long.class);
		Map<Long, Question> allQuestionMap = questionRepo.mget(qids);
		List<Question> questions = new ArrayList<Question>(allQuestionMap.size());
		for (Long qid : qids) {
			Question question = allQuestionMap.get(qid);
			question.setSource(RecommendType.HOT_QUESTION.getName(null));
			questions.add(question);
			eliminateQuestionIds.add(qid);
		}
		return questions;
	}

	/**
	 * 获取错题.
	 * 
	 * @param teacherId
	 *            教师ID
	 * @param sectionCodes
	 *            章节CODE
	 * @param eliminateQuestionIds
	 *            排除的习题ID
	 * @param num
	 *            获取数量
	 * @return
	 */
	@Transactional(readOnly = true)
	private List<Question> fromFall(long teacherId, List<Long> sectionCodes, Set<Long> eliminateQuestionIds, int num,
			ResourceRecommendForm form) {
		Params params = Params.param("teacherId", teacherId).put("num", num);
		if (sectionCodes.size() == 1) {
			params.put("sectionCode", sectionCodes.get(0));
		} else {
			params.put("sectionCodes", sectionCodes);
		}
		if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
			params.put("eliminateQuestionIds", eliminateQuestionIds);
		}
		params = this.handleParams(params, form);
		List<Long> qids = questionRepo.find("$listRecommendQuestionFromFall", params).list(Long.class);
		Map<Long, Question> allQuestionMap = questionRepo.mget(qids);
		List<Question> questions = new ArrayList<>(allQuestionMap.size());
		for (Long qid : qids) {
			Question question = allQuestionMap.get(qid);
			question.setSource(RecommendType.FALLIBLE_QUESTION.getName(null));
			questions.add(question);
			eliminateQuestionIds.add(qid);
		}

		if (questions.size() < num) {
			// 题目不够从热门题中获取
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, num - questions.size(), form));
		}
		return questions;
	}

	/**
	 * 获取好题.
	 * 
	 * @param teacherId
	 *            教师ID
	 * @param sectionCodes
	 *            章节CODE
	 * @param eliminateQuestionIds
	 *            排除的习题ID
	 * @param num
	 *            获取数量
	 * @return
	 */
	@Transactional(readOnly = true)
	private List<Question> fromGood(long teacherId, List<Long> sectionCodes, Set<Long> eliminateQuestionIds, int num,
			ResourceRecommendForm form) {
		Params params = Params.param("teacherId", teacherId).put("num", num);
		if (sectionCodes.size() == 1) {
			params.put("sectionCode", sectionCodes.get(0));
		} else {
			params.put("sectionCodes", sectionCodes);
		}
		if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
			params.put("eliminateQuestionIds", eliminateQuestionIds);
		}
		params = this.handleParams(params, form);
		List<Long> qids = questionRepo.find("$listRecommendQuestionFromGood", params).list(Long.class);
		Map<Long, Question> allQuestionMap = questionRepo.mget(qids);
		List<Question> questions = new ArrayList<>(allQuestionMap.size());
		for (Long qid : qids) {
			Question question = allQuestionMap.get(qid);
			question.setSource(RecommendType.COLLECTION_QUESTION.getName(null));
			questions.add(question);
			eliminateQuestionIds.add(qid);
		}

		if (questions.size() < num) {
			// 题目不够从热门题中获取
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, num - questions.size(), form));
		}
		return questions;
	}

	/**
	 * 获取班级薄弱知识点习题.
	 * 
	 * @param teacherId
	 *            教师ID
	 * @param sectionCodes
	 *            章节CODE
	 * @param eliminateQuestionIds
	 *            排除的习题ID
	 * @param num
	 *            获取数量
	 * @return
	 */
	@Transactional(readOnly = true)
	private List<Question> fromClassWeak(long teacherId, List<Long> sectionCodes, Set<Long> eliminateQuestionIds,
			int num, ResourceRecommendForm form) {
		int max = num + 100;
		Params params = Params.param("teacherId", teacherId).put("num", max); // 多取随机
		if (sectionCodes.size() == 1) {
			params.put("sectionCode", sectionCodes.get(0));
		} else {
			params.put("sectionCodes", sectionCodes);
		}
		if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
			params.put("eliminateQuestionIds", eliminateQuestionIds);
		}
		params = this.handleParams(params, form);
		List<Long> qids = questionRepo.find("$listRecommendQuestionFromClassWeek", params).list(Long.class);
		Map<Long, Question> allQuestionMap = questionRepo.mget(qids);
		List<Long> easys = new ArrayList<Long>(); // 难度在[0.8,1]之间的
		for (Long id : qids) {
			Question question = allQuestionMap.get(id);
			if (question.getDifficulty() >= 0.8) {
				easys.add(id);
			}
		}

		// 首先挑选容易的习题
		if (easys.size() >= num) {
			Collections.shuffle(easys);
			qids = easys.subList(0, num);
		} else if (qids.size() > num) {
			Collections.shuffle(qids);
			qids = qids.subList(0, num);
		}

		List<Question> questions = new ArrayList<>(num);
		for (Long qid : qids) {
			Question question = allQuestionMap.get(qid);
			question.setSource(RecommendType.WEAK_KNOWLEDGEPOINT.getName(null));
			questions.add(question);
			eliminateQuestionIds.add(qid);
		}
		if (qids.size() < num) {
			// 题目不够从热门题中获取
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, num - questions.size(), form));
		}
		return questions;
	}

	/**
	 * 获取章节知识点对应考点习题.
	 * 
	 * @param sectionCode
	 *            章节CODE
	 * @param eliminateQuestionIds
	 *            排除的习题ID
	 * @param num
	 *            获取数量
	 * @return
	 */
	@Transactional(readOnly = true)
	private List<Question> fromExampoint(List<Long> sectionCodes, Set<Long> eliminateQuestionIds, int num,
			ResourceRecommendForm form) {
		Params params = Params.param("num", num);
		if (sectionCodes.size() == 1) {
			params.put("sectionCode", sectionCodes.get(0));
		} else {
			params.put("sectionCodes", sectionCodes);
		}

		// 首先获取对应的考点
		List<ExaminationPoint> examinationPoints = questionRepo.find("$listExampointForRecommendQuestion", params)
				.list(ExaminationPoint.class);
		List<Question> questions = new ArrayList<Question>();
		if (CollectionUtils.isNotEmpty(examinationPoints)) {
			// 集合对应的习题ID集合
			Set<Long> questionIds = new HashSet<Long>();
			for (ExaminationPoint ep : examinationPoints) {
				if (CollectionUtils.isNotEmpty(ep.getQuestions())) {
					questionIds.addAll(ep.getQuestions());
				}
			}

			if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
				questionIds.removeAll(eliminateQuestionIds);
			}
			params = this.handleParams(params, form);
			// 获取习题
			if (CollectionUtils.isNotEmpty(questionIds)) {
				params.put("questionIds", questionIds);
				questions = questionRepo.find("$listRecommendQuestionFromExampoint", params).list();
				for (Question question : questions) {
					question.setSource(RecommendType.EXAMINATION_POINT.getName(null));
					eliminateQuestionIds.add(question.getId());
				}
			}
		}

		if (questions.size() < num) {
			// 题目不够从热门题中获取
			questions.addAll(this.fromHot(sectionCodes, eliminateQuestionIds, num - questions.size(), form));
		}
		return questions;
	}

	/**
	 * 最后一步直接从习题章节对应关系中取题.
	 * 
	 * @param sectionCode
	 * @param eliminateQuestionIds
	 * @param num
	 * @return
	 */
	private List<Question> fromQuestionSection(List<Long> sectionCodes, Set<Long> eliminateQuestionIds,
			List<Long> hasQuestionIds, int num, ResourceRecommendForm form) {
		Params params = Params.param("num", num);
		if (sectionCodes.size() == 1) {
			params.put("sectionCode", sectionCodes.get(0));
		} else {
			params.put("sectionCodes", sectionCodes);
		}
		if (CollectionUtils.isNotEmpty(eliminateQuestionIds)) {
			params.put("eliminateQuestionIds", eliminateQuestionIds);
		}
		if (CollectionUtils.isNotEmpty(hasQuestionIds)) {
			params.put("hasQuestionIds", hasQuestionIds);
		}
		params = this.handleParams(params, form);
		List<Long> qids = questionRepo.find("$listRecommendQuestionFromQuestionSection", params).list(Long.class);
		Map<Long, Question> allQuestionMap = questionRepo.mget(qids);
		List<Question> questions = new ArrayList<>(allQuestionMap.size());
		for (Long qid : qids) {
			Question question = allQuestionMap.get(qid);
			question.setSource(RecommendType.HOT_QUESTION.getName(null));
			questions.add(question);
			eliminateQuestionIds.add(qid);
		}
		return questions;
	}
}
