package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.bean.IndexTypeable;
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
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;
import com.lanking.uxb.service.zuoye.cache.CollectQuestionCacheService;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZyQuestionCollectionServiceImpl implements ZyQuestionCollectionService {

	@Autowired
	@Qualifier("QuestionCollectionRepo")
	Repo<QuestionCollection, Long> questionCollectRepo;

	@Autowired
	private QuestionService questionService;
	@Autowired
	private CollectQuestionCacheService collectQCacheService;
	@Autowired
	private QuestionSectionService questionSectionService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionBaseTypeService questionBaseTypeService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Transactional
	@Override
	public void collect(Long questionId, Long userId) {
		Params params = Params.param();
		params.put("questionId", questionId);
		params.put("userId", userId);
		QuestionCollection qc = questionCollectRepo.find("$getQuestionCollect", params).get();
		if (qc == null) {
			qc = new QuestionCollection();
			Question q = questionService.get(questionId);
			if (q != null) {
				qc.setQuestionId(questionId);
				qc.setUserId(userId);
				qc.setCreateAt(new Date());
				qc.setDifficulty(q.getDifficulty());
				qc.setSubjectCode(q.getSubjectCode());
				qc.setType(q.getType());
				qc.setTypeCode(q.getTypeCode());
				questionCollectRepo.save(qc);
				updateTextbookCollectCache(questionId, userId);
				indexService.syncUpdate(IndexType.USER_QUESTION_COLLECT, qc.getId());
			}
		}
	}

	@Transactional
	@Override
	public void cancelCollect(long questionId, long userId) {
		Params params = Params.param();
		params.put("questionId", questionId);
		params.put("userId", userId);
		QuestionCollection q = questionCollectRepo.find("$getQuestionCollect", params).get();
		if (q != null) {
			questionCollectRepo.delete(q);
			indexService.syncDelete(IndexType.USER_QUESTION_COLLECT, q.getId());
		}
	}

	@Transactional
	@Override
	public void cancel(long id, long userId) {
		QuestionCollection qc = questionCollectRepo.get(id);
		if (qc != null && qc.getUserId() == userId) {
			questionCollectRepo.deleteById(id);
			indexService.delete(IndexType.USER_QUESTION_COLLECT, id);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> statisKnowPointCollect(Integer subjectCode, Long userId, List<Integer> qtCodes) {
		Params params = Params.param();
		params.put("subjectCode", subjectCode);
		params.put("userId", userId);
		params.put("qtCodes", qtCodes);
		List<Map> collectList = questionCollectRepo.find("$statisKnowPointCollect", params).list(Map.class);
		Map<Integer, Integer> collectMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(collectList)) {
			for (Map m : collectList) {
				collectMap.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("cou")).intValue());
			}
		}
		return collectMap;
	}

	@Override
	public QuestionCollection get(Long questionId, Long userId) {
		Params params = Params.param();
		params.put("questionId", questionId);
		params.put("userId", userId);
		QuestionCollection q = questionCollectRepo.find("$getQuestionCollect", params).get();
		return q;
	}

	@Override
	public Map<Long, QuestionCollection> mget(Collection<Long> questionIds, Long userId) {
		Params params = Params.param();
		params.put("questionIds", questionIds);
		params.put("userId", userId);
		List<QuestionCollection> list = questionCollectRepo.find("$getQuestionCollects", params).list();
		Map<Long, QuestionCollection> newMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(list)) {
			for (QuestionCollection qc : list) {
				newMap.put(qc.getQuestionId(), qc);
			}
		}
		return newMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> statisSectionCollect(Integer textbookCode, Long userId, List<Integer> qtCodes) {
		Params params = Params.param();
		params.put("textbookCode", textbookCode);
		params.put("userId", userId);
		params.put("qtCodes", qtCodes);
		params.put("version", 2);
		List<Map> collectList = questionCollectRepo.find("$statisSectionCollect", params).list(Map.class);
		Map<Long, Long> collectMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(collectList)) {
			for (Map m : collectList) {
				collectMap.put(((BigInteger) m.get("section_code")).longValue(),
						((BigInteger) m.get("cou")).longValue());
			}
		}
		return collectMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> statisSectionCollect2(Integer textbookCode, Long userId) {
		Params params = Params.param();
		params.put("textbookCode", textbookCode);
		params.put("userId", userId);
		params.put("version", 2);
		List<Map> collectList = questionCollectRepo.find("$statisSectionCollect", params).list(Map.class);
		Map<Long, Long> collectMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(collectList)) {
			for (Map m : collectList) {
				collectMap.put(((BigInteger) m.get("section_code")).longValue(),
						((BigInteger) m.get("cou")).longValue());
			}
		}
		return collectMap;
	}

	@Override
	public Page<QuestionCollection> queryCollection(QuestionQueryForm query, Pageable p) {
		Params params = Params.param();
		params.put("userId", query.getUserId());

		if (query.getTypeCodes() != null) {
			params.put("typeCodes", query.getTypeCodes());
		} else {
			if (query.getTypeCode() != null) {
				params.put("typeCode", query.getTypeCode());
			}
		}
		if (query.getTextbookCode() != null) {
			params.put("textbookCode", query.getTextbookCode());
		}
		// 收藏时间排序
		if (query.getSort() != null) {
			params.put("sort", query.getSort());
		}
		if (query.getSectionCode() != null) {
			params.put("sectionCode", query.getSectionCode() + "%");
		}
		if (query.getCategoryCode() != null) {
			params.put("categoryCode", query.getCategoryCode());
		}
		if (query.getMetaknowCode() != null) {
			params.put("metaknowCode", query.getMetaknowCode() + "%");
		}
		if (query.getLeDifficulty() != null) {
			params.put("leDif", query.getLeDifficulty());
		}
		if (query.getReDifficulty() != null) {
			params.put("reDif", query.getReDifficulty());
		}
		return questionCollectRepo.find("$queryCollection", params).fetch(p);
	}

	@Override
	public CursorPage<Long, QuestionCollection> queryCollection(QuestionQueryForm query,
			CursorPageable<Long> pageable) {
		Params params = Params.param();
		params.put("userId", query.getUserId());
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keyword", "%" + query.getKey() + "%");
		}
		return questionCollectRepo.find("$zyQueryCollectionByCursor", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Boolean> statisTextbookExistCollectWithCache(List<Integer> textbookCodes, Long userId,
			List<Integer> qtCodes) {
		Map<Integer, Boolean> data = Maps.newHashMap();
		Map<Integer, String> cacheFlags = collectQCacheService.mgetTextbookFlag(userId, textbookCodes);
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
			List<Map> list = questionCollectRepo
					.find("$statisTextbookCollect",
							Params.param("textbookCodes", textbookCodes).put("userId", userId).put("qtCodes", qtCodes))
					.list(Map.class);
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					data.put(((BigInteger) m.get("textbook_code")).intValue(),
							((BigInteger) m.get("cou")).longValue() > 0);
				}
			}
			for (Integer code : textbookCodes) {
				if (!data.containsKey(code)) {
					collectQCacheService.setTextbookFlag(userId, code, "0");
					data.put(code, false);
				} else {
					collectQCacheService.setTextbookFlag(userId, code, "1");
				}
			}
		}
		return data;
	}

	@Override
	public void updateTextbookCollectCache(final long questionId, final long userId) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				List<QuestionSection> list = questionSectionService.findByQuestionId(1, questionId);
				for (QuestionSection questionSection : list) {
					collectQCacheService.setTextbookFlag(userId, questionSection.getTextBookCode(), "1");
				}
			}
		});
	}

	@Override
	public Page<QuestionCollection> queryCollectionByIndex(QuestionQueryForm form, Pageable p) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.USER_QUESTION_COLLECT); // 搜索我的收藏
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();

		qb = QueryBuilders.boolQuery();
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode", Lists.newArrayList(qtCodes)));
		qb.must(QueryBuilders.termQuery("teacherId", Security.getUserId()));
		// 关键字查询题干选项和知识点
		if (form.getKey() != null) {
			orders.add(new Order("_score", Direction.DESC));
			qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "metaKnowpoints"));
		}
		orders.add(new Order("createAt", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
		if (form.getCategoryCode() != null) {
			qb.must(QueryBuilders.termQuery("textbookCategoryCode", form.getCategoryCode()));
		}
		if (form.getTextbookCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(form.getTextbookCode())));
		}
		if (form.getReDifficulty() != null && form.getLeDifficulty() != null) {
			qb.must(QueryBuilders.rangeQuery("difficulty").gte(form.getLeDifficulty().doubleValue()).lt(form.getReDifficulty().doubleValue()));
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
			}
		}

		if (null != form.getMetaknowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(form.getMetaknowCode())));
		}
		if (null != form.getSectionCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(form.getSectionCode())));
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);

		// 查询数据库
		List<Long> collectIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			collectIds.add(Long.parseLong(document.getId()));
		}
		Map<Long, QuestionCollection> collectMap = this.mget(collectIds);
		List<QuestionCollection> collectList = Lists.newArrayList();
		for (Long collectId : collectIds) {
			if (collectMap.get(collectId) != null) {
				collectList.add(collectMap.get(collectId));
			}
		}
		Page<QuestionCollection> collectPage = new PageImpl<QuestionCollection>(collectList, docPage.getTotalCount(),
				p);
		return collectPage;
	}

	@Override
	public QuestionCollection get(Long id) {
		return questionCollectRepo.get(id);
	}

	@Override
	public Map<Long, QuestionCollection> mget(Collection<Long> ids) {
		return questionCollectRepo.mget(ids);
	}

	@Override
	public long count(long userId) {
		return questionCollectRepo.find("$zyCount", Params.param("userId", userId)).count();
	}

	@Override
	public Page<QuestionCollection> queryCollectionByIndex2(QuestionQueryForm form, Pageable p) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.USER_QUESTION_COLLECT); // 搜索我的收藏
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();

		qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("teacherId", Security.getUserId()));
		// 关键字查询题干选项和知识点
		if (form.getKey() != null) {
			orders.add(new Order("_score", Direction.DESC));
			if (form.getNewKeyQuery() != null) {
				if (form.getNewKeyQuery()) {
					qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "knowpointnames"));
				}
			} else {
				qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "metaKnowpoints"));
			}
		}
		orders.add(new Order("createAt", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
		if (form.getTextbookCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(form.getTextbookCode())));
		}
		if (form.getReDifficulty() != null && form.getLeDifficulty() != null) {
			qb.must(QueryBuilders.rangeQuery("difficulty").gte(form.getLeDifficulty().doubleValue()).lt(form.getReDifficulty().doubleValue()));
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
			}
		}

		if (null != form.getMetaknowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(form.getMetaknowCode())));
		}
		if (null != form.getNewKnowpointCodes()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowpointCodes",
					Lists.newArrayList(form.getNewKnowpointCodes())));
		}
		if (null != form.getSectionCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(form.getSectionCode())));
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);

		// 查询数据库
		List<Long> collectIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			collectIds.add(Long.parseLong(document.getId()));
		}
		Map<Long, QuestionCollection> collectMap = this.mget(collectIds);
		List<QuestionCollection> collectList = Lists.newArrayList();
		for (Long collectId : collectIds) {
			if (collectMap.get(collectId) != null) {
				collectList.add(collectMap.get(collectId));
			}
		}
		Page<QuestionCollection> collectPage = new PageImpl<QuestionCollection>(collectList, docPage.getTotalCount(),
				p);
		return collectPage;
	}

	@Override
	public Map<Integer, Boolean> statisTextbookExistCollectWithCache2(List<Integer> textbookCodes, Long userId) {
		Map<Integer, Boolean> data = Maps.newHashMap();
		Map<Integer, String> cacheFlags = collectQCacheService.mgetTextbookFlag(userId, textbookCodes);
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
			List<Map> list = questionCollectRepo
					.find("$statisTextbookCollect",
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
					collectQCacheService.setTextbookFlag(userId, code, "0");
					data.put(code, false);
				} else {
					collectQCacheService.setTextbookFlag(userId, code, "1");
				}
			}
		}
		return data;
	}

	@Override
	public Map<Integer, Integer> statisKnowPointCollect2(Integer subjectCode, Long userId) {
		Params params = Params.param();
		params.put("subjectCode", subjectCode);
		params.put("userId", userId);
		List<Map> collectList = questionCollectRepo.find("$statisKnowPointCollect", params).list(Map.class);
		Map<Integer, Integer> collectMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(collectList)) {
			for (Map m : collectList) {
				collectMap.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("cou")).intValue());
			}
		}
		return collectMap;
	}

	@Override
	public Map<Long, Long> statisNewKnowPointCollect(Integer subjectCode, Long userId) {
		List<Map> kpMapList = questionCollectRepo
				.find("$zyGetNewKnowpointCollectCount", Params.param("userId", userId).put("subjectCode", subjectCode))
				.list(Map.class);
		Map<Long, Long> kpCountMap = null;
		if (CollectionUtils.isNotEmpty(kpMapList)) {
			kpCountMap = new HashMap<Long, Long>(kpMapList.size());
			for (Map m : kpMapList) {
				kpCountMap.put(Long.parseLong(String.valueOf(m.get("knowledge_code"))),
						Long.parseLong(String.valueOf(m.get("count"))));
			}
			return kpCountMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public Map<Integer, Integer> statisTextbookCollect(long teacherId, int categoryCode) {
		List<Map> countMapList = questionCollectRepo.find("$statisEveryTextbookCollect",
				Params.param("teacherId", teacherId).put("categoryCode", categoryCode + "%")).list(Map.class);
		Map<Integer, Integer> countMap = null;
		if (CollectionUtils.isNotEmpty(countMapList)) {
			countMap = new HashMap<Integer, Integer>(countMapList.size());
			for (Map m : countMapList) {
				countMap.put(((BigInteger) m.get("textbook_code")).intValue(), ((BigInteger) m.get("cou")).intValue());
			}
			return countMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public CursorPage<Long, QuestionCollection> queryCollection2(QuestionQueryForm query,
			CursorPageable<Long> pageable) {
		Params params = Params.param();
		params.put("userId", query.getUserId());
		if (CollectionUtils.isNotEmpty(query.getTypeCodes())) {
			params.put("typeCodes", query.getTypeCodes());
		}
		if (query.getLeDifficulty() != null) {
			params.put("leDifficulty", query.getLeDifficulty());
		}
		if (query.getReDifficulty() != null) {
			params.put("reDifficulty", query.getReDifficulty());
		}
		if (query.getTextbookCode() != null) {
			params.put("textBookCode", query.getTextbookCode());
		}
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		if (query.isSearchInSection()) {
			params.put("searchInSection", "true");
		}
		params.put("leftOpen", query.isLeftOpen());
		params.put("rightOpen", query.isRightOpen());
		params.put("releftOpen", query.isRateleftOpen());
		params.put("rerightOpen", query.isRaterightOpen());
		return questionCollectRepo.find("$zyQueryCollectionByCursor2", params).fetch(pageable);
	}

	@Override
	public Map<String, BigInteger> getLastTextbookCode(Long userId, Integer categoryCode) {
		Params params = Params.param();
		params.put("userId", userId);
		params.put("categoryCode", categoryCode + "%");
		return questionCollectRepo.find("$queryLastTextbookCode", params).get(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> querySectionCode(Long teacherId, Integer textbookCode) {
		Params param = Params.param();
		param.put("teacherId", teacherId);
		param.put("textbookCode", textbookCode);
		List<Map> resultList = questionCollectRepo.find("$zyCollectGetSectionCode", param).list(Map.class);
		Map<Long, Long> countMap = new HashMap<Long, Long>();
		if (CollectionUtils.isNotEmpty(resultList)) {
			for (Map m : resultList) {
				BigInteger code = (BigInteger) m.get("section_code");
				if (countMap.containsKey(code.longValue())) {
					long value = countMap.get(code.longValue()).longValue() + 1;
					countMap.put(code.longValue(), value);
				} else {
					countMap.put(code.longValue(), 1l);
				}
			}
		}

		return countMap;
	}

	@Override
	public Long queryCollectionCount(QuestionQueryForm query) {
		Params params = Params.param();
		params.put("userId", query.getUserId());
		if (CollectionUtils.isNotEmpty(query.getTypeCodes())) {
			params.put("typeCodes", query.getTypeCodes());
		}
		if (query.getLeDifficulty() != null) {
			params.put("leDifficulty", query.getLeDifficulty());
		}
		if (query.getReDifficulty() != null) {
			params.put("reDifficulty", query.getReDifficulty());
		}
		if (query.getTextbookCode() != null) {
			params.put("textBookCode", query.getTextbookCode());
		}
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		if (query.isSearchInSection()) {
			params.put("searchInSection", "true");
		}
		params.put("leftOpen", query.isLeftOpen());
		params.put("rightOpen", query.isRightOpen());
		params.put("releftOpen", query.isRateleftOpen());
		params.put("rerightOpen", query.isRaterightOpen());
		return questionCollectRepo.find("$zyQueryCollectionByCursor2Count", params).count();
	}
}
