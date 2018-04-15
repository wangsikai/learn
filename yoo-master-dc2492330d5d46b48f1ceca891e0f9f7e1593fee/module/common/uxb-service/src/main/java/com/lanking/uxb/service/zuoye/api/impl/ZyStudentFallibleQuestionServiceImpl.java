package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDataRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionStudentStat;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.question.api.QuestionSectionService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.QuestionBaseTypeService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.cache.StuFallQuestionCacheService;
import com.lanking.uxb.service.zuoye.form.StuFallibleQuestion2Form;

@Transactional(readOnly = true)
@Service
public class ZyStudentFallibleQuestionServiceImpl implements ZyStudentFallibleQuestionService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("StudentFallibleQuestionRepo")
	Repo<StudentFallibleQuestion, Long> sfQuestionRepo;
	@Autowired
	@Qualifier("DoQuestionStudentStatRepo")
	Repo<DoQuestionStudentStat, Long> doQuestionStudentStatRepo;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private QuestionBaseTypeService questionBaseTypeService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private StuFallQuestionCacheService stuFallQuestionCacheService;
	@Autowired
	private QuestionSectionService questionSectionService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Override
	public Page<StudentFallibleQuestion> query(ZyStudentFallibleQuestionQuery query, Pageable pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getTextbookCode() != null) {
			params.put("textbookCode", query.getTextbookCode());
		}
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		if (query.getSectionCode() != null) {
			params.put("sectionCode", query.getSectionCode());
		}
		if (query.getIsCreateAtDesc() != null) {
			params.put("createAtDesc", query.getIsCreateAtDesc() ? 1 : 0);
		}
		if (query.getIsUpdateAtDesc() != null) {
			params.put("updateAtDesc", query.getIsUpdateAtDesc() ? 1 : 0);
		}
		if (query.getIsMistakeNumDesc() != null) {
			params.put("mistakeNumDesc", query.getIsMistakeNumDesc() ? 1 : 0);
		}
		return sfQuestionRepo.find("$zyQuery", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> exportQuery(ZyStudentFallibleQuestionQuery query, Pageable pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getTextbookCode() != null) {
			params.put("textbookCode", query.getTextbookCode());
		}
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		if (query.getSectionCode() != null) {
			params.put("sectionCode", query.getSectionCode());
		}
		return sfQuestionRepo.find("$zyExportQuery", params).fetch(pageable, Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> staticFallibleCount(long studentId, int textbookCode) {
		Map<Long, Long> data = Maps.newHashMap();
		List<Map> list = sfQuestionRepo
				.find("$zyStaticFallibleCount",
						Params.param("studentId", studentId).put("textbookCode", textbookCode).put("version", 2))
				.list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				data.put(((BigInteger) m.get("section_code")).longValue(), ((BigInteger) m.get("coun")).longValue());
			}
		}
		return data;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> staticFallibleCounts(long studentId, Collection<Integer> textbookCodes) {
		Map<Long, Long> data = Maps.newHashMap();
		List<Map> list = sfQuestionRepo.find("$zyStaticFallibleCounts",
				Params.param("studentId", studentId).put("textbookCodes", textbookCodes)).list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				data.put(((BigInteger) m.get("section_code")).longValue(), ((BigInteger) m.get("coun")).longValue());
			}
		}
		return data;
	}

	@Override
	public CursorPage<Long, StudentFallibleQuestion> query(ZyStudentFallibleQuestionQuery query,
			CursorPageable<Long> pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getTextbookCode() != null) {
			params.put("textbookCode", query.getTextbookCode());
		}
		if (query.getUpdateAtCursor() != null) {
			params.put("updateAt", query.getUpdateAtCursor());
		}
		if (query.getSectionCodes() != null && query.getSectionCodes().size() > 0) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		return sfQuestionRepo.find("$zyQueryByCursor", params).fetch(pageable);
	}

	@Override
	public CursorPage<Long, StudentFallibleQuestion> query2(ZyStudentFallibleQuestionQuery query,
			CursorPageable<Long> pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		if (query.getCreateAtCursor() != null) {
			params.put("createAtCursor", query.getCreateAtCursor());
		}
		if (query.getCategoryCode() != null) {
			params.put("categoryCode", query.getCategoryCode() + "%");
		}
		if (query.getOther() != null) {
			params.put("other", query.getOther() ? 1 : 0);
		}
		if (query.getOcr() != null) {
			params.put("ocr", query.getOcr() ? 1 : 0);
		}
		if (query.getAll() != null) {
			params.put("all", query.getAll() ? 1 : 0);
		}
		if (pageable.getCursor() == Long.MAX_VALUE) {
			params.put("dateParam", new Date());
		} else {
			params.put("dateParam", new Date(pageable.getCursor()));
		}

		return sfQuestionRepo.find("$zyQueryStuFall", params).fetch(pageable);
	}

	@Transactional
	@Override
	public void update(long questionId, long studentId, HomeworkAnswerResult result,
			List<HomeworkAnswerResult> itemResults, Map<Long, List<String>> answer,
			Map<Long, List<String>> asciimathAnswer, List<Long> answerImgs, Integer rightRate,
			StudentQuestionAnswerSource source) {
		StudentFallibleQuestion p = sfQuestionRepo
				.find("$zyFind", Params.param("studentId", studentId).put("questionId", questionId)).get();
		Question question = questionService.get(questionId);

		// 过滤
		if (CollectionUtils.isNotEmpty(answer)) {
			for (Object id : answer.keySet()) {
				List<String> as = answer.get(Long.parseLong(id.toString()));
				if (as != null) {
					for (String a : as) {
						a = StringUtils.defaultIfBlank(a);
					}
				}
				if (CollectionUtils.isNotEmpty(as) && as.get(0) == null) {
					if (as != null) {
						for (String a : as) {
							a = StringUtils.defaultIfBlank(a);
						}
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(asciimathAnswer)) {
			for (Object id : asciimathAnswer.keySet()) {
				List<String> as = asciimathAnswer.get(Long.parseLong(id.toString()));
				if (CollectionUtils.isNotEmpty(as) && as.get(0) == null) {
					as.set(0, "");
				}
			}
		}

		if (p == null) {
			p = new StudentFallibleQuestion();
			p.setQuestionId(questionId);
			p.setStudentId(studentId);
			p.setCreateAt(new Date());
			if (source == StudentQuestionAnswerSource.CUSTOM_EXAMPAPER) {
				// since v2.0.6 学生组卷首次添加进来的错题练习次数设为0
				// since v2.3.0 2017-01-16 设置组卷添加的错题练习及错误次数均为1
				p.setDoNum(1L);
				p.setMistakeNum(1L);
			} else {
				p.setDoNum(1L);
				p.setMistakeNum(result == HomeworkAnswerResult.WRONG ? 1L : 0L);
			}
			p.setStatus(Status.ENABLED);
			p.setType(question.getType());
			p.setTypeCode(question.getTypeCode());
		} else {
			if (p.getDoNum() == null) {
				p.setDoNum(1L);
			} else {
				p.setDoNum(p.getDoNum() + 1);
			}
			if (p.getMistakeNum() == null) {
				p.setMistakeNum(1L);
			} else {
				p.setMistakeNum(result == HomeworkAnswerResult.WRONG ? p.getMistakeNum() + 1 : p.getMistakeNum());
			}
			if (p.getStatus() == Status.DELETED && result == HomeworkAnswerResult.WRONG) {
				p.setStatus(Status.ENABLED);
			}
		}
		p.setMistakeTimes(p.getMistakeNum().intValue());
		p.setExerciseNum(p.getDoNum());
		p.setDifficulty(question.getDifficulty());
		p.setUpdateAt(p.getUpdateAt() == null ? p.getCreateAt() : new Date());

		p.setLatestResult(result);
		p.setLatestItemResults(itemResults);
		p.setLatestAnswer(answer);
		// 当在错题本中再进行练习时来源会传错题，自2.1.0后此字段会显示出原本来源，故此处做如下处理
		// 除此之外则更新来源
		if (source != StudentQuestionAnswerSource.FALLIBLE) {
			p.setLatestSource(source);
		}
		p.setLatestAsciimathAnswer(asciimathAnswer);
		p.setLatestAnswerImgs(answerImgs);
		p.setLatestRightRate(rightRate);
		sfQuestionRepo.save(p);
		indexService.syncUpdate(IndexType.STUDENT_FALLIBLE_QUESTION, p.getId());
		this.updateTextbookStuFallCache(questionId, studentId);

	}

	@Async
	@Override
	@Transactional
	public void updateFromStudentCustompaper(List<StudentFallibleQuestion> sfQuestions) {
		for (StudentFallibleQuestion sfQuestion : sfQuestions) {
			update(sfQuestion.getQuestionId(), sfQuestion.getStudentId(), HomeworkAnswerResult.WRONG, null, null, null,
					null, null, StudentQuestionAnswerSource.CUSTOM_EXAMPAPER);
		}
	}

	@Override
	@Transactional
	public void update(long id, long studentId, List<Long> imageIds, StudentQuestionAnswerSource source,
			HomeworkAnswerResult result) {
		StudentFallibleQuestion question = sfQuestionRepo.get(id);
		if (!question.getStudentId().equals(studentId)) {
			throw new IllegalArgException();
		}
		question.setLatestAnswerImgs(imageIds);
		question.setUpdateAt(new Date());
		question.setDoNum(question.getDoNum() + 1);
		question.setExerciseNum(question.getDoNum());
		if (result == HomeworkAnswerResult.WRONG) {
			question.setMistakeNum(question.getMistakeNum() + 1);
		}
		question.setMistakeTimes(question.getMistakeNum().intValue());
		question.setLatestResult(result);
		List<Map> historyAnswerImgs = Lists.newArrayList();
		Map<String, Object> m = new HashMap<String, Object>(2);

		// 处理答题图片
		if (CollectionUtils.isNotEmpty(imageIds)) {
			if (CollectionUtils.isEmpty(question.getOcrHisAnswerImgs())) {
				historyAnswerImgs = new ArrayList<Map>(1);
				m.put("imageId", imageIds.get(0));
				m.put("imageIds", imageIds); // 添加对多图支持
				m.put("answerAt", question.getUpdateAt().getTime());
				historyAnswerImgs.add(m);
			} else {
				m.put("imageId", imageIds.get(0));
				m.put("imageIds", imageIds); // 添加对多图支持
				m.put("answerAt", question.getUpdateAt().getTime());
				historyAnswerImgs.add(m);
			}

			// 保证图片只有5张
			List<Map> historyAnswerImgs2 = question.getOcrHisAnswerImgs();
			if (question.getOcrHisAnswerImgs().size() >= 5) {
				for (int i = 1; i < question.getOcrHisAnswerImgs().size() - 3; i++) {
					historyAnswerImgs2.remove(question.getOcrHisAnswerImgs().size() - i);
				}
			}
			// 后存入数据库原本的,保证顺序和非图片题目的一致
			for (Map map : historyAnswerImgs2) {
				historyAnswerImgs.add(map);
			}
			question.setOcrHisAnswerImgs(historyAnswerImgs);
			sfQuestionRepo.save(question);
		}

		indexService.syncUpdate(IndexType.STUDENT_FALLIBLE_QUESTION, question.getId());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("studentId", studentId);
		jsonObject.put("source", source);
		jsonObject.put("createAt", new Date());
		jsonObject.put("result", result);

		// 临时(不能一个一个处理)
		mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
				MqYoomathDataRegistryConstants.RK_YM_DATA_DOQUESTIONGOAL, MQ.builder().data(jsonObject).build());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> mgetQuestionExerciseNums(Collection<Long> questionIds, long studentId) {
		List<Map> list = sfQuestionRepo
				.find("$mgetQuestionExerciseNums", Params.param("qIds", questionIds).put("stuId", studentId))
				.list(Map.class);
		Map<Long, Long> map = Maps.newHashMap();
		for (Map m : list) {
			map.put(Long.parseLong(m.get("qid").toString()), Long.parseLong(m.get("donum").toString()));
		}

		return map;
	}

	@Override
	public long countDoNum(long studentId) {
		Long count = sfQuestionRepo.find("$zyCountDoNum", Params.param("studentId", studentId)).get(Long.class);
		return count == null ? 0 : count;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> sfQuestionRateQuery(long studentId, Collection<Long> questionIds) {
		return sfQuestionRepo
				.find("$sfQuestionRateQuery", Params.param("studentId", studentId).put("questionIds", questionIds))
				.list(Map.class);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<Map> getStudentSFCount(Collection<Long> studentIds, Collection<Long> sectionCodes) {
		return sfQuestionRepo
				.find("$getStudentSFCount", Params.param("studentIds", studentIds).put("sectionCodes", sectionCodes))
				.list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getStudentFallTSCount(long studentId, int categoryCode) {
		String codeParam = categoryCode + "%";
		return sfQuestionRepo.find("$zyCountTS", Params.param("studentId", studentId).put("categoryCode", codeParam))
				.list(Map.class);
	}

	@Override
	public Long countByDate(long studentId, int categoryCode, Date beginDate, Date endDate) {
		Params params = Params.param("studentId", studentId);
		params.put("categoryCode", categoryCode + "%");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return sfQuestionRepo.find("$zyCountByDate", params).get(Long.class);
	}

	@Override
	public Long countOther(long studentId, int categoryCode) {
		return sfQuestionRepo
				.find("$zyCountOther", Params.param("studentId", studentId).put("categoryCode", categoryCode + "%"))
				.get(Long.class);
	}

	@Override
	public Long countOcr(long studentId, int categoryCode) {
		return sfQuestionRepo
				.find("$zyCountOCR", Params.param("studentId", studentId).put("categoryCode", categoryCode + "%"))
				.get(Long.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> countMistakePeople(Collection<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return Maps.newHashMap();
		}

		List<Map> ret = sfQuestionRepo.find("$zyCountMistakePeople", Params.param("questionIds", questionIds))
				.list(Map.class);
		Map<Long, Long> retMap = new HashMap<Long, Long>(ret.size());
		for (Map m : ret) {
			retMap.put(((BigInteger) m.get("question_id")).longValue(), ((BigInteger) m.get("amount")).longValue());
		}
		return retMap;
	}

	@Override
	public Map<Long, Boolean> exist(Collection<Long> questionIds, long userId) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return null;
		}

		Map<Long, Boolean> existMap = new HashMap<Long, Boolean>(questionIds.size());
		List<StudentFallibleQuestion> sfQuestions = sfQuestionRepo
				.find("$zyFindByQuestion", Params.param("studentId", userId).put("questionIds", questionIds)).list();
		if (CollectionUtils.isEmpty(sfQuestions)) {
			for (Long id : questionIds) {
				existMap.put(id, false);
			}
		} else {
			for (StudentFallibleQuestion q : sfQuestions) {
				existMap.put(q.getQuestionId(), true);
			}
		}

		return existMap;
	}

	@Override
	@Transactional
	public List<Integer> add(long questionId, long studentId, Long fileId, StudentQuestionAnswerSource source) {
		// 修复二维码有重复题
		StudentFallibleQuestion studentFallibleQuestion = sfQuestionRepo
				.find("$zyFind", Params.param("studentId", studentId).put("questionId", questionId)).get();
		Question question = questionService.get(questionId);

		if (studentFallibleQuestion == null) {
			studentFallibleQuestion = new StudentFallibleQuestion();
			studentFallibleQuestion.setCreateAt(new Date());
			studentFallibleQuestion.setDifficulty(question.getDifficulty());
			studentFallibleQuestion.setQuestionId(questionId);
			studentFallibleQuestion.setDoNum(1L);
			studentFallibleQuestion.setExerciseNum(1L);
			studentFallibleQuestion.setMistakeNum(1L);
			studentFallibleQuestion.setMistakeTimes(1);
			studentFallibleQuestion.setUpdateAt(new Date());
			studentFallibleQuestion.setLatestSource(source);
			studentFallibleQuestion.setStudentId(studentId);
			studentFallibleQuestion.setOcrImageId(fileId);
			studentFallibleQuestion.setType(question.getType());
			studentFallibleQuestion.setTypeCode(question.getTypeCode());
		} else {
			studentFallibleQuestion.setUpdateAt(new Date());
			studentFallibleQuestion.setLatestSource(source);
			studentFallibleQuestion.setStudentId(studentId);
			studentFallibleQuestion.setOcrImageId(fileId);
		}

		sfQuestionRepo.save(studentFallibleQuestion);
		indexService.syncUpdate(IndexType.STUDENT_FALLIBLE_QUESTION, studentFallibleQuestion.getId());
		this.updateTextbookStuFallCache(questionId, studentId);

		List<QuestionSection> questionSections = questionSectionService.findByQuestionId(2, questionId);

		List<Integer> textbookCodes = new ArrayList<Integer>(questionSections.size());
		for (QuestionSection qs : questionSections) {
			textbookCodes.add(qs.getTextBookCode());
		}

		return textbookCodes;
	}

	@Override
	@Transactional
	public void addOcr(long studentId, long fileId, List<Long> codes) {
		StudentFallibleQuestion studentFallibleQuestion = new StudentFallibleQuestion();
		studentFallibleQuestion.setCreateAt(new Date());
		studentFallibleQuestion.setDoNum(1L);
		studentFallibleQuestion.setExerciseNum(1L);
		studentFallibleQuestion.setMistakeNum(1L);
		studentFallibleQuestion.setMistakeTimes(1);
		studentFallibleQuestion.setUpdateAt(new Date());
		studentFallibleQuestion.setLatestSource(StudentQuestionAnswerSource.OCR);
		studentFallibleQuestion.setOcrImageId(fileId);
		studentFallibleQuestion.setOcrKnowpointCodes(codes);
		studentFallibleQuestion.setStudentId(studentId);
		// TODO: 根据传入的Code查找教材码
		studentFallibleQuestion.setOcrTextbookCode(0);

		sfQuestionRepo.save(studentFallibleQuestion);
		indexService.syncUpdate(IndexType.STUDENT_FALLIBLE_QUESTION, studentFallibleQuestion.getId());
	}

	@Override
	public Page<StudentFallibleQuestion> queryStuFallibleQuestionByIndex(StuFallibleQuestion2Form form, Pageable p) {

		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.STUDENT_FALLIBLE_QUESTION); // 搜索学生的错题
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();

		qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("studentId", Security.getUserId()));
		// 关键字查询题干选项和知识点
		if (form.getKey() != null) {
			orders.add(new Order("_score", Direction.DESC));
			// 这次先用旧知识点的
			if (form.getNewKeyQuery()) {
				qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "metaKnowpoints"));
			} else {
				qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "knowpointnames"));
			}
		}
		if (form.getOrderByCreateAt()) {
			orders.add(new Order("createAt", Direction.DESC));
		} else {
			orders.add(new Order("updateAt", Direction.DESC));
		}
		if (form.getCategoryCode() != null) {
			// 查询其他题目是指查询当前版本以外的其他版本
			if (form.getOther()) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCategoryCodes",
						Lists.newArrayList(form.getCategoryCode())));
				qb.mustNot(QueryBuilders.termQuery("questionId", 0));
			} else {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCategoryCodes",
						Lists.newArrayList(form.getCategoryCode())));
			}
		}
		if (form.getTextbookCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(form.getTextbookCode())));
		}
		qb.must(QueryBuilders.rangeQuery("mistakeNum").gte(1));
		qb.must(QueryBuilders.termQuery("status", 0));
		if (form.getMistakeNum() != null) {
			if (form.getMistakeNum() == 3) {
				qb.must(QueryBuilders.rangeQuery("mistakeNum").gte(3));
			} else {
				qb.must(QueryBuilders.termQuery("mistakeNum", form.getMistakeNum()));
			}
		}
		if (form.getOther()) {
			qb.mustNot(QueryBuilders.termQuery("textbookCategoryCode", form.getCategoryCode()));
			qb.mustNot(QueryBuilders.termQuery("questionId", 0));
		}
		if (form.getDay() != null) {
			Calendar cal = Calendar.getInstance();
			long t = cal.getTimeInMillis();
			long t2 = t - form.getDay().intValue() * 86400 * 1000L;

			if (null != form.getUpdateAtCursor()) {
				if (form.getUpdateAtCursor() < t) {
					qb.must(QueryBuilders.rangeQuery("updateAt").gte(t2).lt(form.getUpdateAtCursor()));
				} else {
					qb.must(QueryBuilders.rangeQuery("updateAt").gte(t2).lt(t));
				}
			} else {
				qb.must(QueryBuilders.rangeQuery("updateAt").gte(t2).lt(t));
			}
		} else {
			if (null != form.getUpdateAtCursor()) {
				qb.must(QueryBuilders.rangeQuery("updateAt").lt(form.getUpdateAtCursor()).gt(0));
			}
		}
		// 全部错题、拍照错题
		if (form.getSource() != null) {
			qb.must(QueryBuilders.termQuery("latestSource", form.getSource()));
			qb.must(QueryBuilders.termQuery("questionId", 0));
		}
		if (CollectionUtils.isNotEmpty(form.getQuestionTypes())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("type", form.getQuestionTypes()));
		}
		if (form.getIsKp()) {
			qb.mustNot(QueryBuilders.termQuery("questionId", 0));
		}
		if (form.getTypeCodes() != null) {
			Set<Long> questionTypes = form.getTypeCodes();
			for (Long typecode : questionTypes) {
				questionTypes.add(typecode);
			}
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode", questionTypes));
			// 说明当前查询的是解答题
			qb.must(QueryBuilders.termQuery("type", Question.Type.QUESTION_ANSWERING.getValue()));
		} else {
			if (form.getTypeCode() != null) {
				qb.must(QueryBuilders.termQuery("typeCode", form.getTypeCode()));
				List<Integer> list = questionBaseTypeService.findBaseCodeList(form.getTypeCode());
				if (CollectionUtils.isNotEmpty(list)) {
					qb.must(QueryBuilders.termQuery("type", list.get(0)));
				}
			} else {
				// 过滤复合题不显示
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.COMPOSITE.getValue()));
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
			}
		}

		if (null != form.getMetaknowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(form.getMetaknowCode())));
		}
		if (null != form.getNewKnowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowpointCodes",
					Lists.newArrayList(form.getNewKnowCode())));
		}
		if (null != form.getSectionCodes()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(form.getSectionCodes())));
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);

		// 查询数据库
		List<Long> failIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			failIds.add(Long.parseLong(document.getId()));
		}
		Map<Long, StudentFallibleQuestion> stufailMap = this.mget(failIds);
		List<StudentFallibleQuestion> list = Lists.newArrayList();
		for (Long failId : failIds) {
			if (stufailMap.get(failId) != null) {
				list.add(stufailMap.get(failId));
			}
		}
		Page<StudentFallibleQuestion> collectPage = new PageImpl<StudentFallibleQuestion>(list, docPage.getTotalCount(),
				p);
		return collectPage;

	}

	@Override
	public Map<Long, StudentFallibleQuestion> mget(Collection<Long> ids) {
		return sfQuestionRepo.mget(ids);
	}

	@Transactional
	@Override
	public void deleteFailQuestion(Long id, Long studentId) {
		StudentFallibleQuestion sfq = sfQuestionRepo.get(id);
		if (sfq != null && sfq.getStudentId() == studentId.longValue()) {
			sfq.setUpdateAt(new Date());
			sfq.setMistakeNum(0L);
			sfq.setMistakeTimes(0);
			sfq.setDoNum(0L);
			sfq.setExerciseNum(0L);
			sfq.setStatus(Status.DELETED);
			sfQuestionRepo.save(sfq);
			indexService.delete(IndexType.STUDENT_FALLIBLE_QUESTION, id);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> statisKnowPoint(Long userId) {
		Params params = Params.param();
		params.put("userId", userId);
		List<Map> list = sfQuestionRepo.find("$statisKnowPoint", params).list(Map.class);
		Map<Integer, Integer> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				map.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("cou")).intValue());
			}
		}
		return map;
	}

	@Override
	public List<StudentFallibleQuestion> queryDoStuFallibleQuestions(StuFallibleQuestion2Form form) {
		int size = form.getPageSize();
		List<Long> ids = new ArrayList<Long>(size);

		// 查询全部错题
		if (CollectionUtils.isEmpty(form.getQuestionTypes())) {
			int choiceCount = (int) (size * 0.3);
			form.setPageSize(choiceCount);
			// 只查询单选题
			form.setQuestionTypes(Lists.newArrayList(Question.Type.SINGLE_CHOICE.getValue()));
			List<Long> choiceIds = query(form, null);

			int fillBlankCount = (int) (size * 0.6);
			form.setPageSize(fillBlankCount);
			form.setQuestionTypes(Lists.newArrayList(Question.Type.FILL_BLANK.getValue()));
			List<Long> fillBlankIds = query(form, null);

			int qaCount = (int) (size * 0.1);
			form.setPageSize(qaCount);
			form.setQuestionTypes(Lists.newArrayList(Question.Type.QUESTION_ANSWERING.getValue()));
			List<Long> qaIds = query(form, null);

			ids.addAll(choiceIds);
			ids.addAll(fillBlankIds);
			ids.addAll(qaIds);

			// 当按照比例取数据不正确时，则随机补全
			if (ids.size() < size) {
				form.setPageSize(size - ids.size());
				form.setQuestionTypes(null);
				ids.addAll(query(form, ids));
			}
		} else {
			ids.addAll(query(form, null));
		}

		return sfQuestionRepo.mgetList(ids);

	}

	/**
	 * 学生练习错题查询接口
	 *
	 * @param form
	 *            {@link StuFallibleQuestion2Form}
	 * @param notInIds
	 *            不在查询范围内的错题id列表
	 * @return 错题id
	 */
	private List<Long> query(StuFallibleQuestion2Form form, Collection<Long> notInIds) {
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.STUDENT_FALLIBLE_QUESTION); // 搜索学生的错题
		BoolQueryBuilder qb = null;
		int size = form.getPageSize();
		int offset = 0;
		orders = new ArrayList<Order>();

		orders.add(new Order("updateAt", Direction.DESC));
		orders.add(new Order("_score", Direction.DESC));

		qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("studentId", form.getUserId()));
		if (form.getTextbookCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(form.getTextbookCode())));
		}
		if (form.getSectionCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(form.getSectionCode())));
		}
		if (form.getSectionCodes() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(form.getSectionCodes())));
		}
		if (null != form.getMetaknowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(form.getMetaknowCode())));
		}
		if (null != form.getNewKnowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowpointCodes",
					Lists.newArrayList(form.getNewKnowCode())));
		}
		if (CollectionUtils.isNotEmpty(form.getQuestionTypes())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("type", form.getQuestionTypes()));
		}
		// 查询近几个月的错题
		if (form.getDay() != null) {
			Long end = new Date().getTime();
			Long from = DateUtils.addDays(new Date(), -form.getDay()).getTime();
			qb.must(QueryBuilders.rangeQuery("updateAt").gte(from).lte(end));
		}
		if (form.getMistakeNum() != null) {
			if (form.getMistakeNum() == 3) {
				qb.must(QueryBuilders.rangeQuery("mistakeNum").gte(3).lte(Long.MAX_VALUE));
			} else {
				qb.must(QueryBuilders.termQuery("mistakeNum", form.getMistakeNum()));
			}
		} else {
			qb.must(QueryBuilders.rangeQuery("mistakeNum").gte(1).lte(Long.MAX_VALUE));
		}

		qb.must(QueryBuilders.termQuery("status", 0));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.COMPOSITE.getValue()));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));

		if (form.getSource() != null) {
			if (form.getSource() == 7) {
				qb.must(QueryBuilders.termQuery("latestSource", form.getSource()));
				qb.must(QueryBuilders.termQuery("questionId", 0));
			}
		}
		if (form.getOther()) {
			if (form.getCategoryCode() != null) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCategoryCodes",
						Lists.newArrayList(form.getCategoryCode())));
			}
			qb.mustNot(QueryBuilders.termQuery("latestSource", StudentQuestionAnswerSource.OCR.getValue()));
		}
		Order orderArray[] = new Order[orders.size()];
		int i = 0;
		for (Order o : orders) {
			orderArray[i] = o;
			i++;
		}

		if (CollectionUtils.isNotEmpty(form.getQuestionTypes())) {
			BoolQueryBuilder typeBuilder = QueryBuilders.boolQuery();
			for (Integer tc : form.getQuestionTypes()) {
				typeBuilder.should(QueryBuilders.termQuery("type", tc));
			}

			qb.must(typeBuilder);
		}
		if (CollectionUtils.isNotEmpty(form.getTypeCodes())) {
			BoolQueryBuilder typeBuilder = QueryBuilders.boolQuery();
			for (Long tc : form.getTypeCodes()) {
				typeBuilder.should(QueryBuilders.termQuery("typeCode", tc));
			}

			qb.must(typeBuilder);
		}

		if (CollectionUtils.isNotEmpty(notInIds)) {
			qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("id", notInIds));
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);
		List<Long> ids = new ArrayList<Long>(docPage.getPageSize());
		for (Document document : docPage.getDocuments()) {
			ids.add(Long.valueOf(document.getId()));
		}

		return ids;
	}

	@Override
	@Transactional
	public long getStudentExportCount(long studentId, Collection<Long> sectionCodes, Date timeScope,
			Collection<Integer> questionTypes, Integer errorTimes) {
		Params params = Params.param("studentId", studentId);
		if (CollectionUtils.isNotEmpty(sectionCodes)) {
			params.put("sectionCodes", sectionCodes);
		}
		if (null != timeScope) {
			params.put("timeScope", timeScope);
		}
		if (CollectionUtils.isNotEmpty(questionTypes)) {
			params.put("questionTypes", questionTypes);
		}
		if (null != errorTimes && errorTimes > 0) {
			params.put("errorTimes", errorTimes);
		}
		return sfQuestionRepo.find("$zyStuExportCount", params).count();
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public List<Map> queryStudentExportQuestion(long studentId, Collection<Long> sectionCodes, Date timeScope,
			Collection<Integer> questionTypes, Integer errorTimes) {
		Params params = Params.param("studentId", studentId);
		if (CollectionUtils.isNotEmpty(sectionCodes)) {
			params.put("sectionCodes", sectionCodes);
		}
		if (null != timeScope) {
			params.put("timeScope", timeScope);
		}
		if (CollectionUtils.isNotEmpty(questionTypes)) {
			params.put("questionTypes", questionTypes);
		}
		if (null != errorTimes) {
			params.put("errorTimes", errorTimes);
		}
		return sfQuestionRepo.find("$zyStuExportFQuestions", params).list(Map.class);
	}

	@Override
	public long getTextbookQuestionCount(long studentId, Collection<Integer> textbookCodes) {
		return sfQuestionRepo.find("$zyStuTextbookFQuestionCount",
				Params.param("studentId", studentId).put("textbookCodes", textbookCodes)).count();
	}

	@Override
	public StudentFallibleQuestion getFirst(long studentId) {
		return sfQuestionRepo.find("$getFirst", Params.param("studentId", studentId)).get();

	}

	@Override
	public Long getFallibleCount(long studentId) {
		Long count = sfQuestionRepo.find("$getFallibleCountByStuId", Params.param("studentId", studentId))
				.get(Long.class);
		return count == null ? 0 : count;
	}

	@Override
	public Long getLast30Stat(long studentId) {
		Params params = Params.param("studentId", studentId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long time2 = cal.getTimeInMillis();
		long time1 = time2 - 86400 * 1000L * 30;
		Date endTime = null;
		Date startTime = null;
		try {
			endTime = sdf.parse(sdf.format(new Date(time2)));
			startTime = sdf.parse(sdf.format(new Date(time1)));
		} catch (ParseException e) {
			logger.debug("getLast30Stat error", e);
		}
		params.put("endTime", endTime);
		params.put("startTime", startTime);
		return doQuestionStudentStatRepo.find("$getLast30Stat", params).get(Long.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryLast6MonthStat(long studentId) {
		return doQuestionStudentStatRepo.find("$queryLast6MonthStat", Params.param("studentId", studentId))
				.list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryWeakKpList(long studentId) {
		return doQuestionStudentStatRepo.find("$queryWeakKpList", Params.param("studentId", studentId)).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> studentFallibleLevel1SectionCounts(long studentId, Collection<Integer> textbookCodes) {
		Map<Long, Long> data = Maps.newHashMap();
		List<Map> list = sfQuestionRepo.find("$zyStuFallibleLevel1SectionCounts",
				Params.param("studentId", studentId).put("textbookCodes", textbookCodes)).list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				data.put(Long.parseLong(m.get("scode").toString()), ((BigInteger) m.get("cont")).longValue());
			}
		}
		return data;
	}

	@Override
	public Map<Integer, Boolean> statisTextbookExistStuFallWithCache2(List<Integer> textbookCodes, Long userId) {
		Map<Integer, Boolean> data = Maps.newHashMap();
		Map<Integer, String> cacheFlags = stuFallQuestionCacheService.mgetTextbookFlag(userId, textbookCodes);
		boolean queryDb = false;
		for (Integer code : textbookCodes) {
			String cacheFlag = cacheFlags.get(code);
			if ("1".equals(cacheFlag)) {
				data.put(code, true);
			} else if ("0".equals(cacheFlag)) {
				data.put(code, false);
			} else {
				queryDb = true;
				break;
			}
		}
		if (queryDb) {
			List<Map> list = sfQuestionRepo
					.find("$statisTextbookSfq",
							Params.param("textbookCodes", textbookCodes).put("userId", userId).put("version", 2))
					.list(Map.class);
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					data.put(((BigInteger) m.get("textbook_code")).intValue(),
							((BigInteger) m.get("cou")).longValue() > 0);
				}
			}
			for (Integer code : textbookCodes) {
				if (!data.containsKey(code)) {
					stuFallQuestionCacheService.setTextbookFlag(userId, code, "0");
					data.put(code, false);
				} else {
					stuFallQuestionCacheService.setTextbookFlag(userId, code, "1");
				}
			}
		}
		return data;
	}

	@Override
	public void updateTextbookStuFallCache(final long questionId, final long userId) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				List<QuestionSection> list = questionSectionService.findByQuestionId(1, questionId);
				for (QuestionSection questionSection : list) {
					stuFallQuestionCacheService.setTextbookFlag(userId, questionSection.getTextBookCode(), "1");
				}
			}
		});
	}

	@Override
	public Map<Long, StudentFallibleQuestion> mgetQuestion(Collection<Long> questionIds, long studentId) {
		List<StudentFallibleQuestion> list = sfQuestionRepo
				.find("$mgetFallQuestion", Params.param("qIds", questionIds).put("stuId", studentId)).list();
		Map<Long, StudentFallibleQuestion> map = Maps.newHashMap();
		for (StudentFallibleQuestion s : list) {
			map.put(s.getQuestionId(), s);
		}
		return map;
	}

	@Override
	public Map<Long, Long> statisNewKnowPoint(Long userId) {
		Params params = Params.param();
		params.put("userId", userId);
		List<Map> list = sfQuestionRepo.find("$statisNewKnowPoint", params).list(Map.class);
		Map<Long, Long> map = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				map.put(Long.parseLong(String.valueOf(m.get("knowledge_code"))),
						Long.parseLong(String.valueOf(m.get("cou"))));
			}
		}
		return map;
	}

	@Override
	public List<StudentFallibleQuestion> mgetQuestionList(Collection<Long> questionIds, long studentId) {
		List<StudentFallibleQuestion> list = sfQuestionRepo
				.find("$mgetFallQuestion", Params.param("qIds", questionIds).put("stuId", studentId)).list();
		return list;
	}

	@Override
	public StudentFallibleQuestion findByStudentAndQuestion(long studentId, long questionId) {
		return sfQuestionRepo
				.find("$queryByStudentAndQuestion", Params.param("studentId", studentId).put("questionId", questionId))
				.get();
	}

	@Override
	public Map<Long, Long> queryWeakKpCount(long studentId, Collection<Long> sectionCodes) {
		List<Map> list = sfQuestionRepo
				.find("$queryWeakKpCount", Params.param("studentId", studentId).put("sectionCodes", sectionCodes))
				.list(Map.class);
		Map<Long, Long> map = new HashMap<Long, Long>();
		if (CollectionUtils.isEmpty(list)) {
			return map;
		}
		for (Map pa : list) {
			Long sectionCode = Long.parseLong(String.valueOf(pa.get("section_code")));
			Long count = Long.parseLong(String.valueOf(pa.get("count0")));
			map.put(sectionCode, count);
		}
		return map;
	}

	@Override
	public List<Map> queryFallKpBySectionCodes(long studentId, Collection<Long> sectionCodes) {
		List<Map> list = sfQuestionRepo.find("$queryFallKpBySectionCodes",
				Params.param("studentId", studentId).put("sectionCodes", sectionCodes)).list(Map.class);
		return list;
	}

	@Override
	public Map<Integer, Long> getWeakKpCountByTextbookCodes(long studentId, Collection<Integer> textbookCodes) {
		List<Map> list = sfQuestionRepo.find("$getWeakKpCountByTextbookCodes",
				Params.param("studentId", studentId).put("textbookCodes", textbookCodes)).list(Map.class);
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		if (CollectionUtils.isEmpty(list)) {
			return map;
		}
		for (Map pa : list) {
			Integer textbookCode = Integer.parseInt(String.valueOf(pa.get("textbook_code")));
			Long count = Long.parseLong(String.valueOf(pa.get("count0")));
			map.put(textbookCode, count);
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getStudentFallibleFirstSectionCount(long studentId, int categoryCode) {
		Params params = Params.param();
		params.put("studentId", studentId);
		params.put("categoryCode", categoryCode + "%");
		return sfQuestionRepo.find("$findStudentFallibleFirstSectionCount", params).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Long> getStudentFallibleTextbookCodeCount(long studentId, List<Integer> textbookCodes) {
		Params params = Params.param();
		params.put("studentId", studentId);
		params.put("textbookCodes", textbookCodes);

		List<Map> maps = sfQuestionRepo.find("$findStudentFallibleTextbookCodeCount", params).list(Map.class);

		Map<Integer, Long> data = new HashMap<>();
		if (CollectionUtils.isEmpty(maps)) {
			return data;
		}

		for (Map value : maps) {
			Long count = ((BigInteger) value.get("amount")).longValue();
			Integer textbookCode = ((BigInteger) value.get("textbook_code")).intValue();

			data.put(textbookCode, count);
		}

		return data;
	}

	@Override
	public CursorPage<Long, StudentFallibleQuestion> queryOtherCategoryCode(ZyStudentFallibleQuestionQuery query,
			CursorPageable<Long> pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getCreateAtCursor() != null) {
			params.put("createAtCursor", query.getCreateAtCursor());
		}
		if (query.getCategoryCode() != null) {
			params.put("categoryCode", query.getCategoryCode() + "%");
		}
		if (pageable.getCursor() == Long.MAX_VALUE) {
			params.put("dateParam", new Date());
		} else {
			params.put("dateParam", new Date(pageable.getCursor()));
		}

		return sfQuestionRepo.find("$zyQueryOtherCategoryCode", params).fetch(pageable);
	}

}
