package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
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
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.fallible.cache.TeacherFallibleCacheService;
import com.lanking.uxb.service.resources.api.QuestionBaseTypeService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZyTeacherFallibleQuestionServiceImpl implements ZyTeacherFallibleQuestionService {

	@Autowired
	@Qualifier("TeacherFallibleQuestionRepo")
	Repo<TeacherFallibleQuestion, Long> tfQuestionRepo;

	@Autowired
	private TeacherFallibleCacheService teaFallQCacheService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionBaseTypeService questionBaseTypeService;
	@Autowired
	private IndexService indexService;

	@Override
	public List<TeacherFallibleQuestion> mgetList(List<Long> ids) {
		return tfQuestionRepo.mgetList(ids);
	}

	@Override
	public Map<Long, TeacherFallibleQuestion> mget(List<Long> ids) {
		return tfQuestionRepo.mget(ids);
	}

	@Override
	public Page<TeacherFallibleQuestion> query(ZyTeacherFallibleQuestionQuery query, Pageable pageable) {
		Params params = Params.param("teacherId", query.getTeacherId());
		if (query.getLeRightRate() != null) {
			params.put("leRightRate", query.getLeRightRate());
		}
		if (query.getReRightRate() != null) {
			params.put("reRightRate", query.getReRightRate());
		}
		if (query.getLeDifficulty() != null) {
			params.put("leDifficulty", query.getLeDifficulty());
		}
		if (query.getReDifficulty() != null) {
			params.put("reDifficulty", query.getReDifficulty());
		}
		if (query.getTextbookCode() != null) {
			params.put("textbookCode", query.getTextbookCode());
		}
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		if (query.getSectionCode() != null) {
			params.put("sectionCode", query.getSectionCode());
		}
		int orderCount = 0;
		if (query.getIsCreateAtDesc() != null) {
			params.put("createAtDesc", query.getIsCreateAtDesc() ? 1 : 0);
			orderCount = 1;
		}
		if (query.getIsUpdateAtDesc() != null) {
			params.put("updateAtDesc", query.getIsUpdateAtDesc() ? 1 : 0);
			if (orderCount == 0) {
				orderCount = 2;
			}
		}
		if (query.getIsDifficultyDesc() != null) {
			params.put("difficultyDesc", query.getIsDifficultyDesc() ? 1 : 0);
			if (orderCount == 0) {
				orderCount = 3;
			}
		}
		if (query.getIsRightRateDesc() != null) {
			params.put("rightRateDesc", query.getIsRightRateDesc() ? 1 : 0);
			if (orderCount == 0) {
				orderCount = 4;
			}
		}
		params.put("orderCount", orderCount);
		int timeRange = 0;// 全部
		if (query.getTimeRange() != null) {
			timeRange = query.getTimeRange();
			if (query.getTimeRange() == 1) {// 最近一个月

			}
		}
		params.put("timeRange", timeRange);
		return tfQuestionRepo.find("$zyQuery", params).fetch(pageable);
	}

	@Override
	public Page<TeacherFallibleQuestion> queryFaliableQuestion(Integer subjectCode,
			ZyTeacherFallibleQuestionQuery query, Pageable pageable) {
		Params params = Params.param("teacherId", query.getTeacherId());
		params.put("subjectCode", subjectCode);
		if (query.getTypeCode() != null) {
			params.put("typeCode", query.getTypeCodes());
		}
		if (CollectionUtils.isNotEmpty(query.getTypes())) {
			Set<Integer> types = new HashSet<Integer>(query.getTypes().size());
			for (Type type : query.getTypes()) {
				types.add(type.getValue());
			}
			params.put("types", types);
		}
		if (CollectionUtils.isNotEmpty(query.getTypeCodes())) {
			if (query.getTypeCodes().size() == 1) {
				params.put("typeCode", query.getTypeCodes().get(0));
			} else {
				params.put("typeCodes", query.getTypeCodes());
			}
		}
		if (query.getTextbookCode() != null) {
			params.put("textBookCode", query.getTextbookCode());
		}
		if (query.getLeRightRate() != null) {
			params.put("leRightRate", query.getLeRightRate());
		}
		if (query.getReRightRate() != null) {
			params.put("reRightRate", query.getReRightRate());
		}
		if (query.getLeDifficulty() != null) {
			params.put("leDifficulty", query.getLeDifficulty());
		}
		if (query.getReDifficulty() != null) {
			params.put("reDifficulty", query.getReDifficulty());
		}
		if (query.isSearchInSection()) {
			params.put("searchInSection", "true");
		}
		int orderCount = 0;
		if (query.getIsCreateAtDesc() != null) {
			params.put("createAtDesc", query.getIsCreateAtDesc() ? 1 : 0);
			orderCount = 1;
		}
		if (query.getIsDifficultyDesc() != null) {
			params.put("difficultyDesc", query.getIsDifficultyDesc() ? 1 : 0);
			if (orderCount == 0) {
				orderCount = 3;
			}
		}
		if (query.getIsRightRateDesc() != null) {
			params.put("rightRateDesc", query.getIsRightRateDesc() ? 1 : 0);
			if (orderCount == 0) {
				orderCount = 4;
			}
		}
		if (CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			params.put("sectionCodes", query.getSectionCodes());
		}
		params.put("orderCount", orderCount);
		params.put("leftOpen", query.isLeftOpen());
		params.put("rightOpen", query.isRightOpen());
		params.put("releftOpen", query.isRateleftOpen());
		params.put("rerightOpen", query.isRaterightOpen());
		int timeRange = 0;// 全部
		if (query.getTimeRange() != null) {
			timeRange = query.getTimeRange();
		}
		params.put("timeRange", timeRange);
		if (query.getMetaKnowpointCodes() != null) {
			params.put("metaKnowpointCodes", query.getMetaKnowpointCodes());
		}
		return tfQuestionRepo.find("$zyQuery2", params).fetch(pageable);
	}

	@Override
	public Page<TeacherFallibleQuestion> queryFaliableQuestion2(Integer subjectCode,
			ZyTeacherFallibleQuestionQuery query, Integer page, Integer pageSize) {
		int size = pageSize;
		int p = page;
		int offset = (p - 1) * size;
		List<IndexTypeable> types = Lists.newArrayList();
		List<Order> orders = Lists.newArrayList();
		types.add(IndexType.TEACHER_FALLIBLE_QUESTION);// 类型BIZ.XXX
		// 1
		BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
		// 排除 解答题（解答 ，计算，证明）
		List<QuestionType> qtList = questionTypeService.findBySubject(subjectCode);
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		qb1.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode", Lists.newArrayList(qtCodes)));
		if (query.getKey() != null && StringUtils.isNotBlank(query.getKey())) {
			orders.add(new Order("_score", Direction.DESC));
			qb1.must(QueryBuilders.multiMatchQuery(query.getKey(), "contents", "metaKnowpoints"));
		}
		qb1.must(QueryBuilders.termQuery("teacherId", query.getTeacherId()));
		if (query.getSectionCodes() != null && CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(query.getSectionCodes())));
		}
		if (query.getTextbookCode() != null) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(query.getTextbookCode())));
		}
		if (query.getMetaKnowpointCodes() != null && CollectionUtils.isNotEmpty(query.getMetaKnowpointCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(query.getMetaKnowpointCodes())));
		}
		if (query.getTypeCodes() != null && CollectionUtils.isNotEmpty(query.getTypeCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode",
					Lists.newArrayList(query.getTypeCodes())));
			// 说明页面选择是解答题
			if (query.getTypeCodes().size() > 1) {
				qb1.must(QueryBuilders.termQuery("type", Question.Type.QUESTION_ANSWERING.getValue()));
			} else {
				List<Integer> list = questionBaseTypeService.findBaseCodeList(query.getTypeCodes().get(0));
				if (CollectionUtils.isNotEmpty(list)) {
					qb1.must(QueryBuilders.termQuery("type", list.get(0)));
				}
			}
		}
		if (query.getLeDifficulty() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			if (query.isLeftOpen()) {
				rqb.gt(query.getLeDifficulty().doubleValue());
			}
			if (!query.isLeftOpen()) {
				rqb.gte(query.getLeDifficulty().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getReDifficulty() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			if (query.isLeftOpen()) {
				rqb.lt(query.getReDifficulty().doubleValue());
			}
			if (!query.isLeftOpen()) {
				rqb.lte(query.getReDifficulty().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getLeRightRate() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("rightRate");
			if (query.isRateleftOpen()) {
				rqb.gt(query.getLeRightRate().doubleValue());
			}
			if (!query.isRateleftOpen()) {
				rqb.gte(query.getLeRightRate().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getReRightRate() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("rightRate");
			if (query.isRateleftOpen()) {
				rqb.lt(query.getReRightRate().doubleValue());
			}
			if (!query.isRateleftOpen()) {
				rqb.lte(query.getReRightRate().doubleValue());
			}
			qb1.must(rqb);
		}
		// 时间 1 最近一周 2 最近一个月
		if (query.getTimeRange() != null) {
			Long thisDate = null;
			Long beforeDate = null;
			if (query.getTimeRange() == 1) {
				Calendar nowDate = Calendar.getInstance();
				thisDate = nowDate.getTimeInMillis();
				nowDate.add(Calendar.DAY_OF_YEAR, -7);
				beforeDate = nowDate.getTimeInMillis();
			} else if (query.getTimeRange() == 2) {
				Calendar nowDate = Calendar.getInstance();
				thisDate = nowDate.getTimeInMillis();
				nowDate.add(Calendar.DAY_OF_YEAR, -30);
				beforeDate = nowDate.getTimeInMillis();
			}
			qb1.must(QueryBuilders.rangeQuery("updateAt").lte(thisDate).gte(beforeDate));
		}
		if (query.getIsCreateAtDesc() != null) {
			if (query.getIsCreateAtDesc()) {
				orders.add(Order.desc("updateAt"));
			} else {
				orders.add(Order.asc("updateAt"));
			}
		}
		if (query.getIsRightRateDesc() != null) {
			if (query.getIsRightRateDesc()) {
				orders.add(Order.desc("rightRate"));
			} else {
				orders.add(Order.asc("rightRate"));
			}
		}
		orders.add(Order.desc("difficulty"));
		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb1, null,
				orderArray);
		List<Long> tfqIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				tfqIds.add(Long.valueOf(document.getId()));
			}
		}
		Map<Long, TeacherFallibleQuestion> tfqMap = this.mget(tfqIds);
		List<TeacherFallibleQuestion> tfqList = Lists.newArrayList();
		for (Long tfqId : tfqIds) {
			if (tfqMap.get(tfqId) != null) {
				tfqList.add(tfqMap.get(tfqId));
			}
		}
		Page<TeacherFallibleQuestion> tfPage = new PageImpl<TeacherFallibleQuestion>(tfqList, docPage.getTotalCount(),
				P.index(page, pageSize));
		return tfPage;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> staticFallibleCount(long teacherId, int textbookCode) {
		Map<Long, Long> data = Maps.newHashMap();
		List<Map> list = tfQuestionRepo
				.find("$zyStaticFallibleCount", Params.param("teacherId", teacherId).put("textbookCode", textbookCode))
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
	public Map<Integer, Integer> getKnowpointFailCount(Long uid, Integer subjectCode, List<Integer> qtCodes) {
		List<Map> kpMapList = tfQuestionRepo
				.find("$zyGetKnowpointFailCount",
						Params.param("uid", uid).put("subjectCode", subjectCode).put("questionTypeCodes", qtCodes))
				.list(Map.class);
		Map<Integer, Integer> kpCountMap = null;
		if (CollectionUtils.isNotEmpty(kpMapList)) {
			kpCountMap = new HashMap<Integer, Integer>(kpMapList.size());
			for (Map m : kpMapList) {
				kpCountMap.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("count")).intValue());
			}
			return kpCountMap;
		} else {
			return Collections.EMPTY_MAP;
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Integer> staticQuestionFallibleCount(long userId, Integer textbookCode, Integer subjectCode,
			List<Integer> qtCodes) {
		List<Map> kpMapList = tfQuestionRepo.find("$zyGetQuestionFailCount", Params.param("uid", userId)
				.put("textbookCode", textbookCode).put("subjectCode", subjectCode).put("questionTypeCodes", qtCodes))
				.list(Map.class);
		Map<Long, Integer> kpCountMap = null;
		if (CollectionUtils.isNotEmpty(kpMapList)) {
			kpCountMap = new HashMap<Long, Integer>(kpMapList.size());
			for (Map m : kpMapList) {
				kpCountMap.put(Long.parseLong(m.get("section_code").toString()),
						Integer.parseInt(m.get("count").toString()));
			}
			return kpCountMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Boolean> statisTextbookExistFallibleWithCache(List<Integer> textbookCodes, Long userId) {
		Map<Integer, Boolean> data = Maps.newHashMap();
		Map<Integer, String> cacheFlags = teaFallQCacheService.mgetTextbookFlag(userId, textbookCodes);
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
			List<Map> list = tfQuestionRepo
					.find("$statisTextbookFallible",
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
					teaFallQCacheService.setTextbookFlag(userId, code, "0");
					data.put(code, false);
				} else {
					teaFallQCacheService.setTextbookFlag(userId, code, "1");
				}
			}
		}
		return data;
	}

	@Override
	public Page<TeacherFallibleQuestion> queryFaliableQuestion3(Integer subjectCode,
			ZyTeacherFallibleQuestionQuery query, Integer page, Integer pageSize) {
		int size = pageSize;
		int p = page;
		int offset = (p - 1) * size;
		List<IndexTypeable> types = Lists.newArrayList();
		List<Order> orders = Lists.newArrayList();
		types.add(IndexType.TEACHER_FALLIBLE_QUESTION);// 类型BIZ.XXX
		// 1
		BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
		if (query.getKey() != null && StringUtils.isNotBlank(query.getKey())) {
			orders.add(new Order("_score", Direction.DESC));
			if (query.getNewKeyQuery() != null) {
				if (query.getNewKeyQuery()) {
					qb1.must(QueryBuilders.multiMatchQuery(query.getKey(), "contents", "knowpointnames"));
				}
			} else {
				qb1.must(QueryBuilders.multiMatchQuery(query.getKey(), "contents", "metaKnowpoints"));
			}
		}
		qb1.must(QueryBuilders.termQuery("teacherId", query.getTeacherId()));
		if (query.getSectionCodes() != null && CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(query.getSectionCodes())));
		}
		if (query.getTextbookCode() != null) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(query.getTextbookCode())));
		}
		if (query.getMetaKnowpointCodes() != null && CollectionUtils.isNotEmpty(query.getMetaKnowpointCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(query.getMetaKnowpointCodes())));
		}
		// 新增新知识点的查询
		if (query.getNewKnowpointCodes() != null && CollectionUtils.isNotEmpty(query.getNewKnowpointCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowpointCodes",
					Lists.newArrayList(query.getNewKnowpointCodes())));
		}
		if (query.getTypeCodes() != null && CollectionUtils.isNotEmpty(query.getTypeCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode",
					Lists.newArrayList(query.getTypeCodes())));
			// 说明页面选择是解答题
			if (query.getTypeCodes().size() > 1) {
				qb1.must(QueryBuilders.termQuery("type", Question.Type.QUESTION_ANSWERING.getValue()));
			} else {
				List<Integer> list = questionBaseTypeService.findBaseCodeList(query.getTypeCodes().get(0));
				if (CollectionUtils.isNotEmpty(list)) {
					qb1.must(QueryBuilders.termQuery("type", list.get(0)));
				}
			}
		}
		if (query.getLeDifficulty() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			if (query.isLeftOpen()) {
				rqb.gt(query.getLeDifficulty().doubleValue());
			}
			if (!query.isLeftOpen()) {
				rqb.gte(query.getLeDifficulty().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getReDifficulty() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			if (query.isRightOpen()) {
				rqb.lt(query.getReDifficulty().doubleValue());
			}
			if (!query.isRightOpen()) {
				rqb.lte(query.getReDifficulty().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getLeRightRate() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("rightRate");
			if (query.isRateleftOpen()) {
				rqb.gt(query.getLeRightRate().doubleValue());
			}
			if (!query.isRateleftOpen()) {
				rqb.gte(query.getLeRightRate().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getReRightRate() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("rightRate");
			if (query.isRaterightOpen()) {
				rqb.lt(query.getReRightRate().doubleValue());
			}
			if (!query.isRaterightOpen()) {
				rqb.lte(query.getReRightRate().doubleValue());
			}
			qb1.must(rqb);
		}
		// 时间 1 最近一周 2 最近一个月
		if (query.getTimeRange() != null) {
			Long thisDate = null;
			Long beforeDate = null;
			if (query.getTimeRange() == 1) {
				Calendar nowDate = Calendar.getInstance();
				thisDate = nowDate.getTimeInMillis();
				nowDate.add(Calendar.DAY_OF_YEAR, -7);
				beforeDate = nowDate.getTimeInMillis();
			} else if (query.getTimeRange() == 2) {
				Calendar nowDate = Calendar.getInstance();
				thisDate = nowDate.getTimeInMillis();
				nowDate.add(Calendar.DAY_OF_YEAR, -30);
				beforeDate = nowDate.getTimeInMillis();
			}
			qb1.must(QueryBuilders.rangeQuery("updateAt").lte(thisDate).gte(beforeDate));
		}
		if (query.getIsCreateAtDesc() != null) {
			if (query.getIsCreateAtDesc()) {
				orders.add(Order.desc("updateAt"));
			} else {
				orders.add(Order.asc("updateAt"));
			}
		}
		if (query.getIsUpdateAtDesc() != null) {
			if (query.getIsUpdateAtDesc()) {
				orders.add(Order.desc("updateAt"));
			} else {
				orders.add(Order.asc("updateAt"));
			}
		}
		if (query.getIsRightRateDesc() != null) {
			if (query.getIsRightRateDesc()) {
				orders.add(Order.desc("rightRate"));
			} else {
				orders.add(Order.asc("rightRate"));
			}
		}
		orders.add(Order.desc("difficulty"));
		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		// 关联状态查询
		qb1.must(QueryBuilders.termQuery("status", Status.ENABLED.getValue()));

		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb1, null,
				orderArray);
		List<Long> tfqIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				tfqIds.add(Long.valueOf(document.getId()));
			}
		}
		Map<Long, TeacherFallibleQuestion> tfqMap = this.mget(tfqIds);
		List<TeacherFallibleQuestion> tfqList = Lists.newArrayList();
		for (Long tfqId : tfqIds) {
			if (tfqMap.get(tfqId) != null) {
				tfqList.add(tfqMap.get(tfqId));
			}
		}
		Page<TeacherFallibleQuestion> tfPage = new PageImpl<TeacherFallibleQuestion>(tfqList, docPage.getTotalCount(),
				P.index(page, pageSize));
		return tfPage;
	}

	@Override
	public Map<Long, Integer> staticQuestionFallibleCount2(long userId, Integer textbookCode, Integer subjectCode) {
		List<Map> kpMapList = tfQuestionRepo.find("$zyGetQuestionFailCount", Params.param("uid", userId)
				.put("textbookCode", textbookCode).put("subjectCode", subjectCode).put("version", 2)).list(Map.class);
		Map<Long, Integer> kpCountMap = null;
		if (CollectionUtils.isNotEmpty(kpMapList)) {
			kpCountMap = new HashMap<Long, Integer>(kpMapList.size());
			for (Map m : kpMapList) {
				kpCountMap.put(Long.parseLong(m.get("section_code").toString()),
						Integer.parseInt(m.get("count").toString()));
			}
			return kpCountMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public Map<Integer, Integer> getKnowpointFailCount2(Long uid, Integer subjectCode) {
		List<Map> kpMapList = tfQuestionRepo
				.find("$zyGetKnowpointFailCount", Params.param("uid", uid).put("subjectCode", subjectCode))
				.list(Map.class);
		Map<Integer, Integer> kpCountMap = null;
		if (CollectionUtils.isNotEmpty(kpMapList)) {
			kpCountMap = new HashMap<Integer, Integer>(kpMapList.size());
			for (Map m : kpMapList) {
				kpCountMap.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("count")).intValue());
			}
			return kpCountMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> statisTextbookFallible(long teacherId, int categoryCode) {
		List<Map> countMapList = tfQuestionRepo.find("$statisEveryTextbookFallible",
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
	public Map<Integer, Integer> getNewKnowpointFailCount(Long uid, Integer subjectCode) {
		List<Map> kpMapList = tfQuestionRepo
				.find("$zyGetNewKnowpointFailCount", Params.param("uid", uid).put("subjectCode", subjectCode))
				.list(Map.class);
		Map<Integer, Integer> kpCountMap = null;
		if (CollectionUtils.isNotEmpty(kpMapList)) {
			kpCountMap = new HashMap<Integer, Integer>(kpMapList.size());
			for (Map m : kpMapList) {
				kpCountMap.put(((BigInteger) m.get("knowledge_code")).intValue(),
						((BigInteger) m.get("count")).intValue());
			}
			return kpCountMap;
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	@Transactional
	public void deleteFailQuestion(Long id, Long teacherId) {
		TeacherFallibleQuestion tfq = tfQuestionRepo.get(id);
		if (tfq != null && tfq.getTeacherId() == teacherId.longValue()) {
			tfq.setUpdateAt(new Date());
			tfq.setStatus(Status.DELETED);
			tfQuestionRepo.save(tfq);
			// indexService.syncUpdate(IndexType.TEACHER_FALLIBLE_QUESTION, id);
			// indexService.delete(IndexType.TEACHER_FALLIBLE_QUESTION, id);
		}
	}

	@Override
	public Page<TeacherFallibleQuestion> queryFaliableQuestion4(Integer subjectCode,
			ZyTeacherFallibleQuestionQuery query, Integer pageSize) {
		int size = pageSize;
		// 游标查询
		int offset = 0;
		List<IndexTypeable> types = Lists.newArrayList();
		List<Order> orders = Lists.newArrayList();
		types.add(IndexType.TEACHER_FALLIBLE_QUESTION);// 类型BIZ.XXX
		// 1
		BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
		if (query.getKey() != null && StringUtils.isNotBlank(query.getKey())) {
			orders.add(new Order("_score", Direction.DESC));
			if (query.getNewKeyQuery() != null) {
				if (query.getNewKeyQuery()) {
					qb1.must(QueryBuilders.multiMatchQuery(query.getKey(), "contents", "knowpointnames"));
				}
			} else {
				qb1.must(QueryBuilders.multiMatchQuery(query.getKey(), "contents", "metaKnowpoints"));
			}
		}
		qb1.must(QueryBuilders.termQuery("teacherId", query.getTeacherId()));
		if (query.getSectionCodes() != null && CollectionUtils.isNotEmpty(query.getSectionCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes",
					Lists.newArrayList(query.getSectionCodes())));
		}
		if (query.getTextbookCode() != null) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
					Lists.newArrayList(query.getTextbookCode())));
		}
		if (query.getMetaKnowpointCodes() != null && CollectionUtils.isNotEmpty(query.getMetaKnowpointCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(query.getMetaKnowpointCodes())));
		}
		// 新增新知识点的查询
		if (query.getNewKnowpointCodes() != null && CollectionUtils.isNotEmpty(query.getNewKnowpointCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowpointCodes",
					Lists.newArrayList(query.getNewKnowpointCodes())));
		}
		if (query.getTypeCodes() != null && CollectionUtils.isNotEmpty(query.getTypeCodes())) {
			qb1.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode",
					Lists.newArrayList(query.getTypeCodes())));
			// 说明页面选择是解答题
			if (query.getTypeCodes().size() > 1) {
				qb1.must(QueryBuilders.termQuery("type", Question.Type.QUESTION_ANSWERING.getValue()));
			} else {
				List<Integer> list = questionBaseTypeService.findBaseCodeList(query.getTypeCodes().get(0));
				if (CollectionUtils.isNotEmpty(list)) {
					qb1.must(QueryBuilders.termQuery("type", list.get(0)));
				}
			}
		}
		if (query.getLeDifficulty() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			if (query.isLeftOpen()) {
				rqb.gt(query.getLeDifficulty().doubleValue());
			}
			if (!query.isLeftOpen()) {
				rqb.gte(query.getLeDifficulty().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getReDifficulty() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			if (query.isRightOpen()) {
				rqb.lt(query.getReDifficulty().doubleValue());
			}
			if (!query.isRightOpen()) {
				rqb.lte(query.getReDifficulty().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getLeRightRate() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("rightRate");
			if (query.isRateleftOpen()) {
				rqb.gt(query.getLeRightRate().doubleValue());
			}
			if (!query.isRateleftOpen()) {
				rqb.gte(query.getLeRightRate().doubleValue());
			}
			qb1.must(rqb);
		}
		if (query.getReRightRate() != null) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("rightRate");
			if (query.isRaterightOpen()) {
				rqb.lt(query.getReRightRate().doubleValue());
			}
			if (!query.isRaterightOpen()) {
				rqb.lte(query.getReRightRate().doubleValue());
			}
			qb1.must(rqb);
		}
		// 时间 1 最近一周 2 最近一个月
		if (query.getTimeRange() != null) {
			Long thisDate = null;
			Long beforeDate = null;
			if (query.getTimeRange() == 1) {
				Calendar nowDate = Calendar.getInstance();
				thisDate = nowDate.getTimeInMillis();
				nowDate.add(Calendar.DAY_OF_YEAR, -7);
				beforeDate = nowDate.getTimeInMillis();
			} else if (query.getTimeRange() == 2) {
				Calendar nowDate = Calendar.getInstance();
				thisDate = nowDate.getTimeInMillis();
				nowDate.add(Calendar.DAY_OF_YEAR, -30);
				beforeDate = nowDate.getTimeInMillis();
			}
			qb1.must(QueryBuilders.rangeQuery("updateAt").lte(thisDate).gte(beforeDate));
		}

		// 排序
		orders.add(Order.desc("updateAt"));
		// 游标
		RangeQueryBuilder cursorRqb = QueryBuilders.rangeQuery("updateAt");
		cursorRqb.lte(query.getCursor());
		qb1.must(cursorRqb);

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		// 关联状态查询
		qb1.must(QueryBuilders.termQuery("status", Status.ENABLED.getValue()));

		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb1, null,
				orderArray);
		List<Long> tfqIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				tfqIds.add(Long.valueOf(document.getId()));
			}
		}
		Map<Long, TeacherFallibleQuestion> tfqMap = this.mget(tfqIds);
		List<TeacherFallibleQuestion> tfqList = Lists.newArrayList();
		for (Long tfqId : tfqIds) {
			if (tfqMap.get(tfqId) != null) {
				tfqList.add(tfqMap.get(tfqId));
			}
		}
		Page<TeacherFallibleQuestion> tfPage = new PageImpl<TeacherFallibleQuestion>(tfqList, docPage.getTotalCount(),
				P.index(1, pageSize));
		return tfPage;
	}

	@Override
	public Map<Long, Long> querySectionCode(Long teacherId, Integer textbookCode) {
		Params param = Params.param();
		param.put("teacherId", teacherId);
		param.put("textbookCode", textbookCode);
		List<Map> resultList = tfQuestionRepo.find("$zyGetSectionCode", param).list(Map.class);
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
}
