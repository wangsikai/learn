package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.QuestionBaseTypeService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionBankService;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;

@Transactional(readOnly = true)
@Service
public class ZyQuestionBankServiceImpl implements ZyQuestionBankService {

	@Autowired
	private SearchService searchService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionBaseTypeService questionBaseTypeService;
	@Autowired
	private SectionService sectionService;

	@Override
	public VPage<VQuestion> queryQuestionBankByIndex(QuestionQueryForm form) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();
		qb = QueryBuilders.boolQuery();
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
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		if (form.getReDifficulty() != null && form.getLeDifficulty() != null) {
			qb.must(QueryBuilders.rangeQuery("difficulty").gte(form.getLeDifficulty().doubleValue()).lt(form.getReDifficulty().doubleValue()));
		}
		// 默认获取当前老师的学科
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		qb.must(QueryBuilders.termQuery("subjectCode", teacher.getSubjectCode()));
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		// 目前解答题不显示
		List<QuestionType> qtList = questionTypeService.findBySubject(teacher.getSubjectCode());
		List<Integer> qtCodes = new ArrayList<Integer>();
		for (QuestionType questionType : qtList) {
			if (questionType.getName().equals("证明题") || questionType.getName().equals("解答题")
					|| questionType.getName().equals("计算题")) {
				qtCodes.add(questionType.getCode());
			}
		}
		qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("typeCode", Lists.newArrayList(qtCodes)));
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
				// 过滤多选，判断，复合题不显示
				// @since 2017-9-23
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.COMPOSITE.getValue()));
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
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
		Page docPage = searchService.search(types, offset, size, qb, null, orderArray);

		// 查询数据库
		List<Long> qustionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			qustionIds.add(Long.parseLong(document.getId()));
		}
		QuestionConvertOption qco = new QuestionConvertOption();
		qco.setAnalysis(true);
		qco.setAnswer(true);
		qco.setCollect(true);
		qco.setInitSub(true);
		Map<Long, VQuestion> qusetionMap = questionConvert.to(questionService.mget(qustionIds), qco);
		List<VQuestion> qusetionList = new ArrayList<VQuestion>(qustionIds.size());
		for (Long id : qustionIds) {
			qusetionList.add(qusetionMap.get(id));
		}
		VPage<VQuestion> vPage = new VPage<VQuestion>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(qusetionList);
		return vPage;
	}

	@Override
	public VPage<VQuestion> queryQuestionBankByIndex2(QuestionQueryForm form) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = null;
		offset = (form.getPage() - 1) * form.getPageSize();
		size = form.getPageSize();
		orders = new ArrayList<Order>();
		qb = QueryBuilders.boolQuery();
		// 过滤导学题
		qb.mustNot(QueryBuilders.termQuery("sceneCode", 1));
		// 关键字查询题干选项和知识点
		if (form.getKey() != null) {
			orders.add(new Order("_score", Direction.DESC));
			if (form.getNewKeyQuery()) {
				qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "contents", "knowledgePoints"));
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
		} else if (form.getSortType() == 3) {
			orders.add(new Order("difficulty", form.getSort() == 0 ? Direction.ASC : Direction.DESC));
		}
		if (form.getTextbookCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes2",
					Lists.newArrayList(form.getTextbookCode())));
		}
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		if (form.getReDifficulty() != null && form.getLeDifficulty() != null) {
			qb.must(QueryBuilders.rangeQuery("difficulty").gte(form.getLeDifficulty().doubleValue()).lt(form.getReDifficulty().doubleValue()));
		}
		// 默认获取当前老师的学科
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		qb.must(QueryBuilders.termQuery("subjectCode", teacher.getSubjectCode()));
		qb.must(QueryBuilders.termQuery("schoolId", 0));
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
				if (CollectionUtils.isNotEmpty(questionBaseTypeService.findBaseCodeList(form.getTypeCode()))) {
					qb.must(QueryBuilders.termQuery("type",
							questionBaseTypeService.findBaseCodeList(form.getTypeCode()).get(0)));
				}
			} else {
				// 过滤多选题、判断题、复合题不显示
				// @since 2017-9-23
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
				qb.mustNot(QueryBuilders.termQuery("type", Question.Type.COMPOSITE.getValue()));
			}
		}

		if (null != form.getMetaknowCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					Lists.newArrayList(form.getMetaknowCode())));
		}
		if (null != form.getSectionCode()) {
			Section section = sectionService.get(form.getSectionCode());
			if (section.isComprehensiveSection()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
						Lists.newArrayList(section.getPcode())));
			} else {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
						Lists.newArrayList(form.getSectionCode())));
			}

		} else {
			qb.must(QueryBuilders.existsQuery("sectionCodes2"));
		}
		// 题目标签
		if (null != form.getCategoryTypes()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("categoryTypes",
					Lists.newArrayList(form.getCategoryTypes())));
		}
		// since 2.3.0 根据新知识点查询数据
		if (null != form.getKnowledgeCode()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					Lists.newArrayList(form.getKnowledgeCode())));
		}

		/**
		 * 选中3个知识点ABC，则查看的题目可能含有若干知识点，但含有的知识点一定是ABC中的一个或者多个，没有ABC以外的知识点<br>
		 * 2017.3.24新增
		 */
		if (null != form.getNewKnowpointCodes()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					Lists.newArrayList(form.getNewKnowpointCodes())));

		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		Page docPage = searchService.search(types, offset, size, qb, null, orderArray);

		// 查询数据库
		List<Long> qustionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			qustionIds.add(Long.parseLong(document.getId()));
		}
		QuestionConvertOption qco = new QuestionConvertOption();
		qco.setAnalysis(true);
		qco.setAnswer(true);
		qco.setCollect(true);
		qco.setInitSub(true);
		qco.setInitExamination(true);
		qco.setInitExamination(true);
		Map<Long, VQuestion> qusetionMap = questionConvert.to(questionService.mget(qustionIds), qco);
		List<VQuestion> qusetionList = new ArrayList<VQuestion>(qustionIds.size());
		for (Long id : qustionIds) {
			qusetionList.add(qusetionMap.get(id));
		}
		VPage<VQuestion> vPage = new VPage<VQuestion>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(qusetionList);
		return vPage;
	}
}
