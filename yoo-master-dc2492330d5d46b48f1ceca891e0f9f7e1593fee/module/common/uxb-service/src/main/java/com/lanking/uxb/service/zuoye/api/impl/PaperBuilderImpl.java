package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.zuoye.api.Paper;
import com.lanking.uxb.service.zuoye.api.PaperBuilder;
import com.lanking.uxb.service.zuoye.api.PaperParam;

@Service
public class PaperBuilderImpl implements PaperBuilder {

	// 从search server中获取的条数
	private static final int SEARCH_PAGESIZE = 1000;
	// 从search server中获取的最大条数
	private static final int SEARCH_MAXSIZE = 10000;

	@Autowired
	private SearchService searchService;

	@Override
	public Paper generate(PaperParam param) {
		if (param.isMobile()) {
			return generateForMobile(param);
		} else {
			return generateForWeb(param);
		}
	}

	public Paper generateForMobile(PaperParam param) {
		Paper paper = new Paper();

		List<Long> questionIds = new ArrayList<Long>(param.getCount());
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		// 支持的题型
		BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.MULTIPLE_CHOICE.getValue()));
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.TRUE_OR_FALSE.getValue()));
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.QUESTION_ANSWERING.getValue()));
		qb.must(typeQb);
		// 匹配知识点
		// 首先匹配新知识点，没有新知识点时再匹配旧知识点 @since v2.1.2
		if (CollectionUtils.isNotEmpty(param.getKnowledgePoints())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					param.getKnowledgePoints()));
		} else if (CollectionUtils.isNotEmpty(param.getMetaKnowpoints())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					param.getMetaKnowpoints()));
		}
		Order order = null;
		qb.must(QueryBuilders.rangeQuery("difficulty").lte(param.getMaxDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue())
				.gte(param.getMinDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
		order = new Order("difficulty", Direction.ASC);

		Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null, order);

		if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
			List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
			for (Document doc : searchPage.getDocuments()) {
				QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				qIds.add(indexDoc.getResourceId());
			}
			Random rand = new Random();
			for (int i = 0; i < param.getCount(); i++) {
				if (qIds.size() > 0) {
					int index = rand.nextInt(qIds.size());
					questionIds.add(qIds.get(index));
					qIds.remove(index);
				}
			}
		}
		if (CollectionUtils.isEmpty(questionIds)) {// 根据知识点查询
			BoolQueryBuilder recommendQb = QueryBuilders.boolQuery();
			// 审核通过
			recommendQb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
			// 获取公共题库
			recommendQb.must(QueryBuilders.termQuery("schoolId", 0));
			// 支持的题型
			recommendQb.must(typeQb);
			// 匹配知识点
			if (CollectionUtils.isNotEmpty(param.getMetaKnowpoints())) {
				recommendQb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
						param.getMetaKnowpoints()));
			}
			Page recommendSearchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, recommendQb, null);

			if (CollectionUtils.isNotEmpty(recommendSearchPage.getDocuments())) {
				List<Long> recommendQIds = new ArrayList<Long>(recommendSearchPage.getDocuments().size());
				for (Document doc : recommendSearchPage.getDocuments()) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					recommendQIds.add(indexDoc.getResourceId());
				}
				Random rand = new Random();
				for (int i = 0; i < param.getCount(); i++) {
					if (recommendQIds.size() > 0) {
						int index = rand.nextInt(recommendQIds.size());
						questionIds.add(recommendQIds.get(index));
						recommendQIds.remove(index);
					}
				}
			}
			paper.setRecommend(true);
		} else {
			if (questionIds.size() < param.getCount()) {
				paper.setRecommend(true);
			}
		}
		paper.setIds(questionIds);
		return paper;
	}

	public Paper generateForWeb(PaperParam param) {
		Paper paper = new Paper();
		List<Long> qids = new ArrayList<Long>(param.getCount());
		// 筛选题目
		Map<String, List<QuestionIndexDoc>> matchingQuestions = selectFromDb(param.getMetaKnowpoints(),
				param.getKnowledgePoints(), param.getVersion());
		List<QuestionIndexDoc> highMatchings = matchingQuestions.get("highMatchings");// 高匹配
		List<QuestionIndexDoc> lowMatchings = matchingQuestions.get("lowMatchings");// 低匹配

		Random rand = new Random();
		Map<String, List<QuestionIndexDoc>> highMatchingGroupMap = groupByDifficulty(highMatchings);
		Map<String, List<QuestionIndexDoc>> lowMatchingGroupMap = groupByDifficulty(lowMatchings);
		// 目前难度范围优先于
		QuestionIndexDoc doc = null;
		List<QuestionIndexDoc> highMatchingdocs = highMatchingGroupMap.get("simple");
		List<QuestionIndexDoc> lowMatchingdocs = lowMatchingGroupMap.get("simple");
		for (int i = 0; i < param.getSimple(); i++) {
			if (highMatchingdocs.size() > 0) {
				int index = rand.nextInt(highMatchingdocs.size());
				doc = highMatchingdocs.get(index);
				qids.add(doc.getResourceId());
				highMatchingdocs.remove(index);
			} else if (lowMatchingdocs.size() > 0) {
				int index = rand.nextInt(lowMatchingdocs.size());
				doc = lowMatchingdocs.get(index);
				qids.add(doc.getResourceId());
				lowMatchingdocs.remove(index);
			}
			if (CollectionUtils.isEmpty(highMatchingdocs) && CollectionUtils.isEmpty(lowMatchingdocs)) {
				break;
			}
		}
		highMatchingdocs = highMatchingGroupMap.get("difficult");
		lowMatchingdocs = lowMatchingGroupMap.get("difficult");
		for (int i = 0; i < param.getDifficult(); i++) {
			if (highMatchingdocs.size() > 0) {
				int index = rand.nextInt(highMatchingdocs.size());
				doc = highMatchingdocs.get(index);
				qids.add(doc.getResourceId());
				highMatchingdocs.remove(index);
			} else if (lowMatchingdocs.size() > 0) {
				int index = rand.nextInt(lowMatchingdocs.size());
				doc = lowMatchingdocs.get(index);
				qids.add(doc.getResourceId());
				lowMatchingdocs.remove(index);
			}
			if (CollectionUtils.isEmpty(highMatchingdocs) && CollectionUtils.isEmpty(lowMatchingdocs)) {
				break;
			}
		}
		highMatchingdocs = highMatchingGroupMap.get("improve");
		lowMatchingdocs = lowMatchingGroupMap.get("improve");
		for (int i = 0; i < param.getImprove(); i++) {
			if (highMatchingdocs.size() > 0) {
				int index = rand.nextInt(highMatchingdocs.size());
				doc = highMatchingdocs.get(index);
				qids.add(doc.getResourceId());
				highMatchingdocs.remove(index);
			} else if (lowMatchingdocs.size() > 0) {
				int index = rand.nextInt(lowMatchingdocs.size());
				doc = lowMatchingdocs.get(index);
				qids.add(doc.getResourceId());
				lowMatchingdocs.remove(index);
			}
			if (CollectionUtils.isEmpty(highMatchingdocs) && CollectionUtils.isEmpty(lowMatchingdocs)) {
				break;
			}
		}
		paper.setIds(qids);
		return paper;
	}

	// 初步筛选(分为完全匹配和部分匹配)
	// @since yoomath v2.1.2 添加新知识点参数，有新知识点时仅处理新知识点
	Map<String, List<QuestionIndexDoc>> selectFromDb(List<Integer> metaKnowpoints, List<Long> knowledgePoints,
			String version) {
		Map<String, List<QuestionIndexDoc>> matchingMap = new HashMap<String, List<QuestionIndexDoc>>(2);
		List<QuestionIndexDoc> highMatchings = Lists.newArrayList();// 高匹配
		List<QuestionIndexDoc> lowMatchings = Lists.newArrayList();// 低匹配
		int index = 1;
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		if ("1.2".equals(version)) {
			qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));// 目前只有填空题
		} else if ("1.3".equals(version)) {
			BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
			qb.must(typeQb);
		}
		if (knowledgePoints != null && knowledgePoints.size() > 0) {
			// 新知识点
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes", knowledgePoints));
		} else {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes", metaKnowpoints));
		}
		Page searchPage = searchService.search(IndexType.QUESTION, (index - 1) * SEARCH_PAGESIZE, SEARCH_PAGESIZE, qb,
				null);
		List<Integer> tmpMetaKnowpointCode = null;
		List<Long> tmpKnowledgePointCode = null;
		int matchingSize = 0;

		if (knowledgePoints != null && knowledgePoints.size() > 0) {
			List<Long> removeCodes = new ArrayList<Long>();
			for (Long code : knowledgePoints) {
				removeCodes.add(code);
				removeCodes.add((long) (code / 100));
				removeCodes.add((long) (code / 1000));
				removeCodes.add((long) (code / 100000));
			}
			while (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
				for (Document doc : searchPage.getDocuments()) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					tmpKnowledgePointCode = new ArrayList<Long>();
					tmpKnowledgePointCode.addAll(indexDoc.getKnowledgePointCodes());
					tmpKnowledgePointCode.removeAll(removeCodes);
					if (tmpKnowledgePointCode.size() > 0) {// 如果包含其他知识点则为低匹配
						lowMatchings.add(indexDoc);
					} else {// 完全匹配
						highMatchings.add(indexDoc);
					}
					matchingSize++;
				}
				if (matchingSize > SEARCH_MAXSIZE) {
					break;
				}
				index++;
				searchPage = searchService.search(IndexType.QUESTION, (index - 1) * SEARCH_PAGESIZE, SEARCH_PAGESIZE,
						qb, null);
			}
		} else {
			List<Integer> removeCodes = new ArrayList<Integer>();
			for (Integer code : metaKnowpoints) {
				removeCodes.add(code);
				removeCodes.add((int) (code / 100));
				removeCodes.add((int) (code / 10000));
			}
			while (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
				for (Document doc : searchPage.getDocuments()) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					tmpMetaKnowpointCode = new ArrayList<Integer>();
					tmpMetaKnowpointCode.addAll(indexDoc.getMetaKnowpointCodes());
					tmpMetaKnowpointCode.removeAll(metaKnowpoints);
					if (tmpMetaKnowpointCode.size() > 0) {// 如果包含其他知识点则为低匹配
						lowMatchings.add(indexDoc);
					} else {// 完全匹配
						highMatchings.add(indexDoc);
					}
					matchingSize++;
				}
				if (matchingSize > SEARCH_MAXSIZE) {
					break;
				}
				index++;
				searchPage = searchService.search(IndexType.QUESTION, (index - 1) * SEARCH_PAGESIZE, SEARCH_PAGESIZE,
						qb, null);
			}
		}
		matchingMap.put("highMatchings", highMatchings);
		matchingMap.put("lowMatchings", lowMatchings);
		return matchingMap;
	}

	// 按照难度分组
	Map<String, List<QuestionIndexDoc>> groupByDifficulty(List<QuestionIndexDoc> questionIndexDocs) {
		Map<String, List<QuestionIndexDoc>> map = new HashMap<String, List<QuestionIndexDoc>>(3);
		map.put("simple", Lists.<QuestionIndexDoc>newArrayList());
		map.put("improve", Lists.<QuestionIndexDoc>newArrayList());
		map.put("difficult", Lists.<QuestionIndexDoc>newArrayList());
		for (QuestionIndexDoc doc : questionIndexDocs) {
			// 提高题[0,0.4)难题[0.4,0.8)普通题[0.8,1]
			if (doc.getDifficulty().compareTo(BigDecimal.valueOf(0.4)) == -1) {// 提高题
				map.get("improve").add(doc);
			} else if (doc.getDifficulty().compareTo(BigDecimal.valueOf(0.4)) == 0) {// 难题
				map.get("difficult").add(doc);
			} else if (doc.getDifficulty().compareTo(BigDecimal.valueOf(0.4)) == 1
					&& doc.getDifficulty().compareTo(BigDecimal.valueOf(0.8)) == -1) {// 难题
				map.get("difficult").add(doc);
			} else if (doc.getDifficulty().compareTo(BigDecimal.valueOf(0.8)) == 0) {// 普通题
				map.get("simple").add(doc);
			} else if (doc.getDifficulty().compareTo(BigDecimal.valueOf(0.8)) == 1) {// 普通题
				map.get("simple").add(doc);
			}
		}
		return map;
	}

}
