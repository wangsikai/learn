package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.QuestionBaseTypeService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.schoolQuestion.cache.SchoolQuestionCacheService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZySchoolQuestionService;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZySchoolQuestionServiceImpl implements ZySchoolQuestionService {

	@Autowired
	@Qualifier("SchoolQuestionRepo")
	Repo<SchoolQuestion, Long> schoolQuestionRepo;
	@Autowired
	@Qualifier("QuestionSchoolRepo")
	Repo<QuestionSchool, Long> questionSchoolRepo;

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private SchoolQuestionCacheService schoolQuestionCacheService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionBaseTypeService questionBaseTypeService;

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> statisKnowPointSchool(Integer subjectCode, Long schoolId, List<Integer> qtCodes) {
		Params params = Params.param();
		params.put("subjectCode", subjectCode);
		params.put("schoolId", schoolId);
		params.put("qtCodes", qtCodes);
		List<Map> collectList = schoolQuestionRepo.find("$statisKnowPointSchool", params).list(Map.class);
		Map<Integer, Integer> collectMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(collectList)) {
			for (Map m : collectList) {
				collectMap.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("cou")).intValue());
			}
		}
		return collectMap;
	}

	@Override
	public SchoolQuestion get(Long questionId, Long schoolId) {
		Params params = Params.param();
		params.put("questionId", questionId);
		params.put("schoolId", schoolId);
		SchoolQuestion q = schoolQuestionRepo.find("$getQuestionSchool", params).get();
		return q;
	}

	@Override
	public Map<Long, SchoolQuestion> mget(Collection<Long> questionIds, Long schoolId) {
		Params params = Params.param();
		params.put("questionIds", questionIds);
		params.put("schoolId", schoolId);
		List<SchoolQuestion> list = schoolQuestionRepo.find("$getQuestionSchools", params).list();
		Map<Long, SchoolQuestion> newMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(list)) {
			for (SchoolQuestion qc : list) {
				newMap.put(qc.getQuestionId(), qc);
			}
		}
		return newMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Long> statisSectionSchool(Integer textbookCode, Long schoolId, List<Integer> qtCodes) {
		Params params = Params.param();
		params.put("textbookCode", textbookCode);
		params.put("schoolId", schoolId);
		params.put("qtCodes", qtCodes);
		List<Map> collectList = schoolQuestionRepo.find("$statisSectionSchool", params).list(Map.class);
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
	public Page<SchoolQuestion> querySchoolQuestionByIndex(QuestionQueryForm form, Pageable p) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.SCHOOL_QUESTION); // 搜索学校题库
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();
		qb = QueryBuilders.boolQuery();
		// 目前解答题不显示
		Teacher teacher = (Teacher) teacherService.getUser(form.getUserId());
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode", Lists.newArrayList(qtCodes)));
		qb.must(QueryBuilders.termQuery("schoolId", form.getSchoolId()));
		// 关键字查询题干选项和知识点
		if (form.getKey() != null) {
			orders.add(new Order("_score", Direction.DESC));
			qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "metaKnowpoints"));
		}
		if (form.getSortType() == 1) {
			// 创建时间
			orders.add(new Order("createAt", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
		} else if (form.getSortType() == 2) {
			// 难度系数
			orders.add(new Order("difficulty", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
			orders.add(new Order("createAt", Direction.ASC));
		}
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
		List<Long> schoolQuestionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			schoolQuestionIds.add(Long.parseLong(document.getId()));
		}
		Map<Long, SchoolQuestion> schoolMap = this.mget(schoolQuestionIds);
		List<SchoolQuestion> schoolList = Lists.newArrayList();
		for (Long schoolQuestionId : schoolQuestionIds) {
			if (schoolMap.get(schoolQuestionId) != null) {
				schoolList.add(schoolMap.get(schoolQuestionId));
			}
		}
		Page<SchoolQuestion> collectPage = new PageImpl<SchoolQuestion>(schoolList, docPage.getTotalCount(), p);
		return collectPage;
	}

	@Override
	public SchoolQuestion get(Long id) {
		return schoolQuestionRepo.get(id);
	}

	@Override
	public Map<Long, SchoolQuestion> mget(Collection<Long> ids) {
		return schoolQuestionRepo.mget(ids);
	}

	@Override
	public QuestionSchool getQuestionSchool(Long schoolId) {
		return questionSchoolRepo.get(schoolId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Boolean> statisTextbookExistSchoolWithCache(List<Integer> textbookCodes, Long schoolId,
			List<Integer> qtCodes) {
		Map<Integer, Boolean> data = Maps.newHashMap();
		Map<Integer, String> cacheFlags = schoolQuestionCacheService.mgetTextbookFlag(schoolId, textbookCodes);
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
			List<Map> list = schoolQuestionRepo.find("$statisTextbookSchool",
					Params.param("textbookCodes", textbookCodes).put("schoolId", schoolId).put("qtCodes", qtCodes))
					.list(Map.class);
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					data.put(((BigInteger) m.get("textbook_code")).intValue(),
							((BigInteger) m.get("cou")).longValue() > 0);
				}
			}
			for (Integer code : textbookCodes) {
				if (!data.containsKey(code)) {
					schoolQuestionCacheService.setTextbookFlag(schoolId, code, "0");
					data.put(code, false);
				} else {
					schoolQuestionCacheService.setTextbookFlag(schoolId, code, "1");
				}
			}
		}
		return data;
	}

	@Override
	public Page<SchoolQuestion> querySchoolQuestionByIndex2(QuestionQueryForm form, Pageable p) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.SCHOOL_QUESTION); // 搜索学校题库
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();
		qb = QueryBuilders.boolQuery();
		// 导学题过滤
		qb.mustNot(QueryBuilders.termQuery("sceneCode", 1));
		qb.must(QueryBuilders.termQuery("schoolId", form.getSchoolId()));
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
		if (form.getSortType() == 1) {
			// 创建时间
			orders.add(new Order("createAt", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
		} else if (form.getSortType() == 2) {
			// 难度系数
			orders.add(new Order("difficulty", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
			orders.add(new Order("createAt", Direction.ASC));
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
		List<Long> schoolQuestionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			schoolQuestionIds.add(Long.parseLong(document.getId()));
		}
		Map<Long, SchoolQuestion> schoolMap = this.mget(schoolQuestionIds);
		List<SchoolQuestion> schoolList = Lists.newArrayList();
		for (Long schoolQuestionId : schoolQuestionIds) {
			if (schoolMap.get(schoolQuestionId) != null) {
				schoolList.add(schoolMap.get(schoolQuestionId));
			}
		}
		Page<SchoolQuestion> collectPage = new PageImpl<SchoolQuestion>(schoolList, docPage.getTotalCount(), p);
		return collectPage;
	}

	@Override
	public Map<Integer, Boolean> statisTextbookExistSchoolWithCache2(List<Integer> textbookCodes, Long schoolId) {
		Map<Integer, Boolean> data = Maps.newHashMap();
		Map<Integer, String> cacheFlags = schoolQuestionCacheService.mgetTextbookFlag(schoolId, textbookCodes);
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
			List<Map> list = schoolQuestionRepo.find("$statisTextbookSchool",
					Params.param("textbookCodes", textbookCodes).put("schoolId", schoolId)).list(Map.class);
			if (CollectionUtils.isNotEmpty(list)) {
				for (Map m : list) {
					data.put(((BigInteger) m.get("textbook_code")).intValue(),
							((BigInteger) m.get("cou")).longValue() > 0);
				}
			}
			for (Integer code : textbookCodes) {
				if (!data.containsKey(code)) {
					schoolQuestionCacheService.setTextbookFlag(schoolId, code, "0");
					data.put(code, false);
				} else {
					schoolQuestionCacheService.setTextbookFlag(schoolId, code, "1");
				}
			}
		}
		return data;
	}

	@Override
	public Map<Long, Long> statisSectionSchool2(Integer textbookCode, Long schoolId) {
		Params params = Params.param();
		params.put("textbookCode", textbookCode);
		params.put("schoolId", schoolId);
		List<Map> collectList = schoolQuestionRepo.find("$statisSectionSchool", params).list(Map.class);
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
	public Map<Integer, Integer> statisKnowPointSchool2(Integer subjectCode, Long schoolId) {
		Params params = Params.param();
		params.put("subjectCode", subjectCode);
		params.put("schoolId", schoolId);
		List<Map> collectList = schoolQuestionRepo.find("$statisKnowPointSchool", params).list(Map.class);
		Map<Integer, Integer> collectMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(collectList)) {
			for (Map m : collectList) {
				collectMap.put(((BigInteger) m.get("meta_code")).intValue(), ((BigInteger) m.get("cou")).intValue());
			}
		}
		return collectMap;
	}

	@Override
	public Map<Long, Long> statisSectionSchool2(Integer textbookCode, Long schoolId, List<Integer> qtCodes) {
		Params params = Params.param();
		params.put("textbookCode", textbookCode);
		params.put("schoolId", schoolId);
		params.put("qtCodes", qtCodes);
		params.put("version", 2);
		List<Map> collectList = schoolQuestionRepo.find("$statisSectionSchool", params).list(Map.class);
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
	public Map<Long, Long> getNewKnowpointSchoolQCount(Long schoolId, Integer subjectCode) {
		List<Map> kpMapList = schoolQuestionRepo.find("$getNewKnowpointSchoolQCount",
				Params.param("schoolId", schoolId).put("subjectCode", subjectCode)).list(Map.class);
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
}
