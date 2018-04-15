package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionSectionService;
import com.lanking.uxb.rescon.question.api.ResconQuestionRebuildService;
import com.lanking.uxb.rescon.question.form.SelectDatasForm;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.search.api.Page;

/**
 * 针对习题进行一些重构或者处理使用.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping("rescon/que/rebuild")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconQuestionRebuildController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IndexService indexService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconQuestionRebuildService resconQuestionRebuildService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private ResconQuestionSectionService questionSectionService;

	/**
	 * 该项处理之前必须首先重建习题索引.
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "initKatexDrawQuestions")
	public Value initKatexDrawQuestions(SelectDatasForm form) {
		int offset = 0;
		int size = 200;
		long scrollId = 0;
		BoolQueryBuilder qb = null;
		try {
			qb = QueryBuilders.boolQuery();
			// qb.must(QueryBuilders.termQuery("checkStatus",
			// CheckStatus.PASS.getValue()));
			qb.must(QueryBuilders.termQuery("isKatexSpecs", false));
			qb.must(QueryBuilders.rangeQuery("resourceId").gt(scrollId));
			Page docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
					new Order("resourceId", Direction.ASC));

			logger.info("[KATEX - init] total : " + docPage.getTotalCount());
			while (CollectionUtils.isNotEmpty(docPage.getDocuments())) {
				List<Long> questionIds = new ArrayList<Long>(docPage.getDocuments().size());
				for (Document doc : docPage.getDocuments()) {
					long questionId = Long.parseLong(doc.getId());
					questionIds.add(questionId);
				}
				if (questionIds.size() > 0) {
					resconQuestionRebuildService.handleKatexInputQuestions(questionIds);
					indexService.syncUpdate(IndexType.QUESTION, questionIds);

					// 继续下一步
					scrollId = questionIds.get(questionIds.size() - 1);
					qb = QueryBuilders.boolQuery();
					qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
					qb.must(QueryBuilders.termQuery("isKatexSpecs", false));
					qb.must(QueryBuilders.rangeQuery("resourceId").gt(scrollId));
					docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
							new Order("resourceId", Direction.ASC));
					logger.info("[KATEX - init] total : " + docPage.getTotalCount());
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Value();
	}

	/**
	 * 初始化习题与章节V3之间的关系.
	 * 
	 * @return
	 */
	public Value initQuestionSectionV3() {
		int offset = 0;
		int size = 200;
		long scrollId = 0;
		BoolQueryBuilder qb = null;
		try {
			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.rangeQuery("resourceId").gt(scrollId));
			Page docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
					new Order("resourceId", Direction.ASC));

			while (CollectionUtils.isNotEmpty(docPage.getDocuments())) {
				Set<Long> knowledgeSyncCodes = new HashSet<Long>();
				for (Document document : docPage.getDocuments()) {
					QuestionIndexDoc doc = JSON.parseObject(document.getValue(), QuestionIndexDoc.class);
					if (CollectionUtils.isEmpty(doc.getKnowledgeSyncCodes())) {
						continue;
					}
					knowledgeSyncCodes.addAll(doc.getKnowledgeSyncCodes()); // 同步知识点
				}

				if (knowledgeSyncCodes.size() > 0) {
					Map<String, QuestionSection> questionSectionMap = new HashMap<String, QuestionSection>(); // 临时保存
					Map<Long, Set<Section>> knowledgeSectionMap = knowledgeSectionService
							.findSectionRelationByKnowledgeCodes(knowledgeSyncCodes);
					for (Document document : docPage.getDocuments()) {
						QuestionIndexDoc doc = JSON.parseObject(document.getValue(), QuestionIndexDoc.class);
						if (CollectionUtils.isEmpty(doc.getKnowledgeSyncCodes())) {
							continue;
						}
						for (Long kcode : doc.getKnowledgeSyncCodes()) {
							Set<Section> sections = knowledgeSectionMap.get(kcode);
							if (CollectionUtils.isEmpty(sections)) {
								continue;
							}
							for (Section section : sections) {
								QuestionSection questionSection = questionSectionMap
										.get(doc.getResourceId() + "-" + section.getCode());
								if (questionSection == null) {
									questionSection = new QuestionSection();
									questionSection.setQuestionId(doc.getResourceId());
									questionSection.setSectionCode(section.getCode());
									questionSection.setTextBookCode(section.getTextbookCode());
								}
								questionSection.setV3(true);
								questionSectionMap.put(doc.getResourceId() + "-" + section.getCode(), questionSection);
							}
						}
					}

					if (questionSectionMap.size() > 0) {
						questionSectionService.saveQuestionSections(questionSectionMap.values());
					}
				}

				// List<Long> questionIds = new
				// ArrayList<Long>(docPage.getDocuments().size());

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Value();
	}

	/**
	 * 处理重复题V3知识点.
	 * 
	 * @param show
	 *            处理的是否是展示题，0 不是，1 是
	 * @return
	 */
	@RequestMapping(value = "initV3SameQuestions")
	public Value initV3SameQuestions(Integer show) {
		if (show == null || show < 0 || show > 1) {
			return new Value(new IllegalArgException());
		}

		int offset = 0;
		int size = 100;
		long scrollId = 0;
		BoolQueryBuilder qb = null;
		try {
			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
			qb.must(QueryBuilders.rangeQuery("resourceId").gt(scrollId));
			if (show == 1) {
				// 通过有知识点的展示题处理没有知识点的重复题
				qb.must(QueryBuilders.termQuery("sameShow", true));
				BoolQueryBuilder qbin = QueryBuilders.boolQuery();
				qbin.should(QueryBuilders.existsQuery("knowledgeSyncCodes"));
				qbin.should(QueryBuilders.existsQuery("knowledgeReviewCodes"));
				qb.must(qbin);
			} else if (show == 0) {
				// 通过有知识点的重复题处理没有知识点的展示题
				qb.must(QueryBuilders.existsQuery("sameShowId"));
				qb.must(QueryBuilders.termQuery("sameShow", false));
				BoolQueryBuilder qbin = QueryBuilders.boolQuery();
				qbin.should(QueryBuilders.existsQuery("knowledgeSyncCodes"));
				qbin.should(QueryBuilders.existsQuery("knowledgeReviewCodes"));
				qb.must(qbin);
			}
			Page docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
					new Order("resourceId", Direction.ASC));
			logger.info("[V3 - init] total : " + docPage.getTotalCount());
			while (CollectionUtils.isNotEmpty(docPage.getDocuments())) {
				if (docPage.getDocuments().size() > 0) {
					List<Long> returnQuestionIds = resconQuestionRebuildService
							.handleV3SameQuestions(docPage.getDocuments());
					indexService.syncUpdate(IndexType.QUESTION, returnQuestionIds);

					// 继续下一步
					qb = QueryBuilders.boolQuery();
					scrollId = Long.parseLong(docPage.getDocuments().get(docPage.getDocuments().size() - 1).getId());
					qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
					qb.must(QueryBuilders.rangeQuery("resourceId").gt(scrollId));
					if (show == 1) {
						// 通过有知识点的展示题处理没有知识点的重复题
						qb.must(QueryBuilders.termQuery("sameShow", true));
						BoolQueryBuilder qbin = QueryBuilders.boolQuery();
						qbin.should(QueryBuilders.existsQuery("knowledgeSyncCodes"));
						qbin.should(QueryBuilders.existsQuery("knowledgeReviewCodes"));
						qb.must(qbin);
					} else if (show == 0) {
						// 通过有知识点的重复题处理没有知识点的展示题
						qb.must(QueryBuilders.existsQuery("sameShowId"));
						qb.must(QueryBuilders.termQuery("sameShow", false));
						BoolQueryBuilder qbin = QueryBuilders.boolQuery();
						qbin.should(QueryBuilders.existsQuery("knowledgeSyncCodes"));
						qbin.should(QueryBuilders.existsQuery("knowledgeReviewCodes"));
						qb.must(qbin);
					}
					docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
							new Order("resourceId", Direction.ASC));
					logger.info("[V3 - init] total : " + docPage.getTotalCount());
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Value();
	}
}
