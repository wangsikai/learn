package com.lanking.uxb.service.web.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyTextbookExerciseService;
import com.lanking.uxb.service.zuoye.form.PaperPullForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;

@Transactional(readOnly = true)
@Service
public class PullQuestionServiceImpl implements PullQuestionService {

	// 从search server中获取的条数
	private static final int SEARCH_PAGESIZE = 1000;
	@Autowired
	private SearchService searchService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyTextbookExerciseService tbeService;

	@Override
	public List<Long> pull(PullQuestionForm form) {
		if (form.getType() == PullQuestionType.CHANGE_DIFFICULTY) {
			return changeDifficulty(form);
		} else if (form.getType() == PullQuestionType.CHANGE_QUESTION) {
			return changeQuestion(form);
		} else if (form.getType() == PullQuestionType.ADD_TWO) {
			return addTwo(form);
		} else if (form.getType() == PullQuestionType.PRACTISE) {
			return practise(form);
		} else if (form.getType() == PullQuestionType.SMART_PAPER) {
			return pullPaperQuestions((PaperPullForm) form);
		} else if (form.getType() == PullQuestionType.DAILY_PRACTISE) {
			return dailyPractise(form);
		} else if (form.getType() == PullQuestionType.TEXTBOOK_EXERCISE) {
			return textbookExercise(form);
		} else if (form.getType() == PullQuestionType.KNOWPOINT_ENHANCE_EXERCISE) {
			return enhanceExercise(form);
		}
		return new ArrayList<Long>(0);
	}

	/**
	 * 调整难度
	 * 
	 * @since yoomath V1.2
	 * @since yoomath v2.1.2 添加新知识点 wanlong.che 2016-11-23
	 * @param form
	 *            相关参数
	 * @return 题目ID
	 */
	List<Long> changeDifficulty(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		// 当前参数知识点的条件下调整难度
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		// 目前只有填空题
		if ("1.2".equals(form.getVersion())) {
			qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
		} else if ("1.3".equals(form.getVersion())) {
			BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.MULTIPLE_CHOICE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.TRUE_OR_FALSE.getValue()));
			qb.must(typeQb);
		}
		// 去除当前题目
		if (CollectionUtils.isNotEmpty(form.getqIds())) {
			for (Long qId : form.getqIds()) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}
		// 参数知识点
		if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			// 新知识点
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					form.getKnowledgePoints()));
		} else {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					form.getMetaKnowpoints()));
		}

		// 难度
		Order order = null;
		if (form.getMaxDifficulty() != null) {// 降低难度
			qb.must(QueryBuilders.rangeQuery("difficulty")
					.gt(form.getMaxDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue()));
			order = new Order("difficulty", Direction.ASC);
		}
		if (form.getMinDifficulty() != null) {// 提高难度
			qb.must(QueryBuilders.rangeQuery("difficulty")
					.lt(form.getMinDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
			order = new Order("difficulty", Direction.DESC);
		}
		Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null, order);
		// 增加随机性
		if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
			List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
			BigDecimal difficulty = null;
			for (Document doc : searchPage.getDocuments()) {
				QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				if (difficulty == null) {
					qIds.add(indexDoc.getResourceId());
					difficulty = indexDoc.getDifficulty();
				} else {
					if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
						qIds.add(indexDoc.getResourceId());
					} else {
						break;
					}
				}
			}
			if (CollectionUtils.isNotEmpty(qIds)) {
				questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
			}
		}

		return questionIds;
	}

	/**
	 * 调整难度
	 * 
	 * @since yoomath V1.2
	 * @since yoomath v2.1.2 添加新知识点 wanlong.che 2016-11-23
	 * @param form
	 *            相关参数
	 * @return 题目ID
	 */
	List<Long> _changeDifficulty(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		// 当前题目包含知识点的条件下调整难度
		if (questionIds.size() == 0) {
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			// 审核通过
			qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
			// 获取公共题库
			qb.must(QueryBuilders.termQuery("schoolId", 0));
			// 目前只有填空题
			if ("1.2".equals(form.getVersion())) {
				qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			} else if ("1.3".equals(form.getVersion())) {
				BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
				qb.must(typeQb);
			}
			// 去除当前题目
			if (CollectionUtils.isNotEmpty(form.getqIds())) {
				for (Long qId : form.getqIds()) {
					qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
				}
			}
			// 取题目知识点与参数知识点的交集
			if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
				// 新知识点
				List<Long> knowledgePoints = new ArrayList<Long>(form.getKnowledgePoints().size());
				for (Long code : form.getKnowledgePoints()) {
					if (form.getQuestionKnowledgePoints().contains(code)) {
						knowledgePoints.add(code);
					}
				}
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
						knowledgePoints));
			} else {
				List<Long> metaKnowpointCodes = new ArrayList<Long>(form.getQuestionMetaKnowpoints().size());
				for (Long code : form.getMetaKnowpoints()) {
					if (form.getQuestionMetaKnowpoints().contains(code)) {
						metaKnowpointCodes.add(code);
					}
				}
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
						metaKnowpointCodes));
			}

			// 难度
			Order order = null;
			if (form.getMaxDifficulty() != null) {// 降低难度
				qb.must(QueryBuilders.rangeQuery("difficulty")
						.gt(form.getMaxDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue()));
				order = new Order("difficulty", Direction.ASC);
			}
			if (form.getMinDifficulty() != null) {// 提高难度
				qb.must(QueryBuilders.rangeQuery("difficulty")
						.lt(form.getMinDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
				order = new Order("difficulty", Direction.DESC);
			}
			Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null, order);
			// 增加随机性
			if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
				List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
				BigDecimal difficulty = null;
				for (Document doc : searchPage.getDocuments()) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					if (difficulty == null) {
						qIds.add(indexDoc.getResourceId());
						difficulty = indexDoc.getDifficulty();
					} else {
						if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
							qIds.add(indexDoc.getResourceId());
						} else {
							break;
						}
					}
				}
				if (CollectionUtils.isNotEmpty(qIds)) {
					questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
				}
			}
		}
		// 当前参数知识点的条件下调整难度
		if (questionIds.size() == 0) {
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			// 审核通过
			qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
			// 获取公共题库
			qb.must(QueryBuilders.termQuery("schoolId", 0));
			// 目前只有填空题
			if ("1.2".equals(form.getVersion())) {
				qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			} else if ("1.3".equals(form.getVersion())) {
				BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
				qb.must(typeQb);
			}
			// 去除当前题目
			if (CollectionUtils.isNotEmpty(form.getqIds())) {
				for (Long qId : form.getqIds()) {
					qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
				}
			}
			// 参数知识点
			if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
				// 新知识点
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
						form.getKnowledgePoints()));
			} else {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
						form.getMetaKnowpoints()));
			}

			// 难度
			Order order = null;
			if (form.getMaxDifficulty() != null) {// 降低难度
				qb.must(QueryBuilders.rangeQuery("difficulty")
						.gt(form.getMaxDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue()));
				order = new Order("difficulty", Direction.ASC);
			}
			if (form.getMinDifficulty() != null) {// 提高难度
				qb.must(QueryBuilders.rangeQuery("difficulty")
						.lt(form.getMinDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
				order = new Order("difficulty", Direction.DESC);
			}
			Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null, order);
			// 增加随机性
			if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
				List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
				BigDecimal difficulty = null;
				for (Document doc : searchPage.getDocuments()) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					if (difficulty == null) {
						qIds.add(indexDoc.getResourceId());
						difficulty = indexDoc.getDifficulty();
					} else {
						if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
							qIds.add(indexDoc.getResourceId());
						} else {
							break;
						}
					}
				}
				if (CollectionUtils.isNotEmpty(qIds)) {
					questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
				}
			}
		}
		return questionIds;
	}

	/**
	 * @since yoomath v2.1.2 添加新知识点 wanlong.che 2016-11-23
	 * @param form
	 * @return
	 */
	List<Long> changeQuestion(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		// 相同难度和参数知识点的条件下换题
		if (questionIds.size() == 0) {
			BoolQueryBuilder qb = QueryBuilders.boolQuery();
			// 审核通过
			qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
			// 获取公共题库
			qb.must(QueryBuilders.termQuery("schoolId", 0));
			// 目前只有填空题
			if ("1.2".equals(form.getVersion())) {
				qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			} else if ("1.3".equals(form.getVersion())) {
				BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
				// typeQb.should(QueryBuilders.termQuery("type",
				// Question.Type.MULTIPLE_CHOICE.getValue()));
				typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
				// typeQb.should(QueryBuilders.termQuery("type",
				// Question.Type.TRUE_OR_FALSE.getValue()));
				qb.must(typeQb);
			}
			// 去除当前题目
			if (CollectionUtils.isNotEmpty(form.getqIds())) {
				for (Long qId : form.getqIds()) {
					qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
				}
			}

			// 参数知识点
			if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
				// 新知识点
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
						form.getKnowledgePoints()));
			} else {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
						form.getMetaKnowpoints()));
			}

			// 相同难度
			qb.must(QueryBuilders.rangeQuery("difficulty")
					.gt(form.getDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
			qb.must(QueryBuilders.rangeQuery("difficulty")
					.lt(form.getDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue()));
			Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null);
			// 增加随机性
			if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
				List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
				BigDecimal difficulty = null;
				for (Document doc : searchPage.getDocuments()) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					if (difficulty == null) {
						qIds.add(indexDoc.getResourceId());
						difficulty = indexDoc.getDifficulty();
					} else {
						if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
							qIds.add(indexDoc.getResourceId());
						} else {
							break;
						}
					}
				}
				if (CollectionUtils.isNotEmpty(qIds)) {
					questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
				}
			}
		}
		// 参数知识点的条件下换题,难度进行偏移
		if (questionIds.size() == 0) {
			List<Document> ltDocs = null;
			List<Document> gtDocs = null;
			if (ltDocs == null) {
				BoolQueryBuilder qb = QueryBuilders.boolQuery();
				// 审核通过
				qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
				// 获取公共题库
				qb.must(QueryBuilders.termQuery("schoolId", 0));
				// 目前只有填空题
				if ("1.2".equals(form.getVersion())) {
					qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
				} else if ("1.3".equals(form.getVersion())) {
					BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
					qb.must(typeQb);
				}
				// 去除当前题目
				if (CollectionUtils.isNotEmpty(form.getqIds())) {
					for (Long qId : form.getqIds()) {
						qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
					}
				}

				// 参数知识点
				if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
					// 新知识点
					qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
							form.getKnowledgePoints()));
				} else {
					qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
							form.getMetaKnowpoints()));
				}

				// 提高难度
				qb.must(QueryBuilders.rangeQuery("difficulty")
						.lt(form.getDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
				Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null,
						new Order("difficulty", Direction.DESC));
				if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
					ltDocs = searchPage.getDocuments();
				}
			}
			if (gtDocs == null) {
				BoolQueryBuilder qb = QueryBuilders.boolQuery();
				// 审核通过
				qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
				// 获取公共题库
				qb.must(QueryBuilders.termQuery("schoolId", 0));
				// 目前只有填空题
				if ("1.2".equals(form.getVersion())) {
					qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
				} else if ("1.3".equals(form.getVersion())) {
					BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
					typeQb.should(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
					qb.must(typeQb);
				}
				// 去除当前题目
				if (CollectionUtils.isNotEmpty(form.getqIds())) {
					for (Long qId : form.getqIds()) {
						qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
					}
				}

				// 参数知识点
				if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
					// 新知识点
					qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
							form.getKnowledgePoints()));
				} else {
					qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
							form.getMetaKnowpoints()));
				}

				// 降低难度
				qb.must(QueryBuilders.rangeQuery("difficulty")
						.gt(form.getDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue()));
				Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null,
						new Order("difficulty", Direction.ASC));
				if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
					gtDocs = searchPage.getDocuments();
				}
			}
			if (CollectionUtils.isEmpty(gtDocs) && CollectionUtils.isNotEmpty(ltDocs)) {
				List<Long> qIds = new ArrayList<Long>(ltDocs.size());
				BigDecimal difficulty = null;
				for (Document doc : ltDocs) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					if (difficulty == null) {
						qIds.add(indexDoc.getResourceId());
						difficulty = indexDoc.getDifficulty();
					} else {
						if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
							qIds.add(indexDoc.getResourceId());
						} else {
							break;
						}
					}
				}
				if (CollectionUtils.isNotEmpty(qIds)) {
					questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
				}
			} else if (CollectionUtils.isNotEmpty(gtDocs) && CollectionUtils.isEmpty(ltDocs)) {
				List<Long> qIds = new ArrayList<Long>(gtDocs.size());
				BigDecimal difficulty = null;
				for (Document doc : gtDocs) {
					QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					if (difficulty == null) {
						qIds.add(indexDoc.getResourceId());
						difficulty = indexDoc.getDifficulty();
					} else {
						if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
							qIds.add(indexDoc.getResourceId());
						} else {
							break;
						}
					}
				}
				if (CollectionUtils.isNotEmpty(qIds)) {
					questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
				}
			} else if (CollectionUtils.isNotEmpty(gtDocs) && CollectionUtils.isNotEmpty(ltDocs)) {
				QuestionIndexDoc gtDoc = JSON.parseObject(gtDocs.get(0).getValue(), QuestionIndexDoc.class);
				QuestionIndexDoc ltDoc = JSON.parseObject(ltDocs.get(0).getValue(), QuestionIndexDoc.class);
				BigDecimal gt = gtDoc.getDifficulty().subtract(form.getDifficulty());
				BigDecimal lt = form.getDifficulty().subtract(ltDoc.getDifficulty());
				int eq = gt.compareTo(lt);
				if (eq == 0) {
					List<Long> qIds = new ArrayList<Long>();
					BigDecimal ltDifficulty = null;
					for (Document doc : ltDocs) {
						QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
						if (ltDifficulty == null) {
							qIds.add(indexDoc.getResourceId());
							ltDifficulty = indexDoc.getDifficulty();
						} else {
							if (ltDifficulty.compareTo(indexDoc.getDifficulty()) == 0) {
								qIds.add(indexDoc.getResourceId());
							} else {
								break;
							}
						}
					}
					BigDecimal gtDifficulty = null;
					for (Document doc : gtDocs) {
						QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
						if (gtDifficulty == null) {
							qIds.add(indexDoc.getResourceId());
							gtDifficulty = indexDoc.getDifficulty();
						} else {
							if (gtDifficulty.compareTo(indexDoc.getDifficulty()) == 0) {
								qIds.add(indexDoc.getResourceId());
							} else {
								break;
							}
						}
					}
					if (CollectionUtils.isNotEmpty(qIds)) {
						questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
					}
				} else if (eq == 1) {
					List<Long> qIds = new ArrayList<Long>(ltDocs.size());
					BigDecimal difficulty = null;
					for (Document doc : ltDocs) {
						QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
						if (difficulty == null) {
							qIds.add(indexDoc.getResourceId());
							difficulty = indexDoc.getDifficulty();
						} else {
							if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
								qIds.add(indexDoc.getResourceId());
							} else {
								break;
							}
						}
					}
					if (CollectionUtils.isNotEmpty(qIds)) {
						questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
					}
				} else if (eq == -1) {
					List<Long> qIds = new ArrayList<Long>(gtDocs.size());
					BigDecimal difficulty = null;
					for (Document doc : gtDocs) {
						QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
						if (difficulty == null) {
							qIds.add(indexDoc.getResourceId());
							difficulty = indexDoc.getDifficulty();
						} else {
							if (difficulty.compareTo(indexDoc.getDifficulty()) == 0) {
								qIds.add(indexDoc.getResourceId());
							} else {
								break;
							}
						}
					}
					if (CollectionUtils.isNotEmpty(qIds)) {
						questionIds.add(qIds.get(new Random().nextInt(qIds.size())));
					}
				}
			}
		}
		return questionIds;
	}

	/**
	 * 增加两题
	 * 
	 * @since yoomath V1.2
	 * @since yoomath v2.1.2 增加新知识点 wanlong.che 2016-11-23
	 * @param form
	 *            相关参数
	 * @return 题目ID
	 */
	List<Long> addTwo(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		if ("1.2".equals(form.getVersion())) {
			qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));// 目前只有填空题
		} else if ("1.3".equals(form.getVersion())) {
			BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.MULTIPLE_CHOICE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.TRUE_OR_FALSE.getValue()));
			qb.must(typeQb);
		}
		if (CollectionUtils.isNotEmpty(form.getqIds())) {// 去除当前题目
			for (Long qId : form.getqIds()) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}
		if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			// 新知识点
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					form.getKnowledgePoints()));
		} else {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					form.getMetaKnowpoints()));
		}
		Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null);
		if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
			List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
			for (Document doc : searchPage.getDocuments()) {
				QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				qIds.add(indexDoc.getResourceId());
			}
			Random rand = new Random();
			for (int i = 0; i < form.getCount(); i++) {
				if (qIds.size() > 0) {
					int index = rand.nextInt(qIds.size());
					questionIds.add(qIds.get(index));
					qIds.remove(index);
				}
			}
		}
		return questionIds;
	}

	/**
	 * 学生加强练习拉取题目
	 *
	 * @since yoomath v2.1.2 增加新知识点 wanlong.che 2016-11-23
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return 题目的id
	 */
	public List<Long> practise(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		// 支持的题型
		if ("1.2".equals(form.getVersion())) {
			qb.must(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));// 目前只有填空题
		} else if ("1.3".equals(form.getVersion())) {
			BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.MULTIPLE_CHOICE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.TRUE_OR_FALSE.getValue()));
			qb.must(typeQb);
		} else if ("1.4".equals(form.getVersion())) {
			BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.MULTIPLE_CHOICE.getValue()));
			qb.must(typeQb);
		} else if ("1.5".equals(form.getVersion())) {
			BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.MULTIPLE_CHOICE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
			// typeQb.should(QueryBuilders.termQuery("type",
			// Question.Type.TRUE_OR_FALSE.getValue()));
			typeQb.should(QueryBuilders.termQuery("type", Question.Type.QUESTION_ANSWERING.getValue()));
			qb.must(typeQb);
		}
		// 排除题目
		if (CollectionUtils.isNotEmpty(form.getqIds())) {
			for (Long qId : form.getqIds()) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}
		// 匹配知识点
		if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			// 新知识点
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					form.getKnowledgePoints()));
		} else if (CollectionUtils.isNotEmpty(form.getMetaKnowpoints())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					form.getMetaKnowpoints()));
		}

		// 匹配章节
		if (form.getSectionCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
					Lists.newArrayList(form.getSectionCode())));
		}
		Order order = null;
		if (form.getMaxDifficulty() != null && form.getMinDifficulty() != null) {
			qb.must(QueryBuilders.rangeQuery("difficulty")
					.lte(form.getMaxDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue())
					.gte(form.getMinDifficulty().subtract(BigDecimal.valueOf(0.001))));
			order = new Order("difficulty", Direction.ASC);
		}

		Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null, order);

		if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
			List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
			for (Document doc : searchPage.getDocuments()) {
				QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				qIds.add(indexDoc.getResourceId());
			}
			Random rand = new Random();
			for (int i = 0; i < form.getCount(); i++) {
				if (qIds.size() > 0) {
					int index = rand.nextInt(qIds.size());
					questionIds.add(qIds.get(index));
					qIds.remove(index);
				}
			}
		}
		return questionIds;
	}

	// mobile 每日一练拉取题目
	List<Long> dailyPractise(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.MULTIPLE_CHOICE.getValue()));
		qb.must(typeQb);

		// 排除题目
		if (CollectionUtils.isNotEmpty(form.getqIds())) {
			for (Long qId : form.getqIds()) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}

		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
				Lists.newArrayList(form.getSectionCode())));
		Order order = null;
		qb.must(QueryBuilders.rangeQuery("difficulty")
				.lte(form.getMaxDifficulty().add(BigDecimal.valueOf(0.001)).doubleValue())
				.gte(form.getMinDifficulty().subtract(BigDecimal.valueOf(0.001)).doubleValue()));
		order = new Order("difficulty", Direction.ASC);

		Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null, null, order);

		if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
			List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
			for (Document doc : searchPage.getDocuments()) {
				QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				qIds.add(indexDoc.getResourceId());
			}
			Random rand = new Random();
			for (int i = 0; i < form.getCount(); i++) {
				if (qIds.size() > 0) {
					int index = rand.nextInt(qIds.size());
					questionIds.add(qIds.get(index));
					qIds.remove(index);
				}
			}
		}

		/*
		 * 本次题目并没有满足总数要求，且上次有题目，随机从上次拉取的题目中进行填充
		 */
		if (questionIds.size() < form.getCount() && form.getqIds().size() > 0) {
			Random rand = new Random();
			for (int i = 0; i < form.getqIds().size() && questionIds.size() < form.getCount(); i++) {
				if (form.getqIds().size() > 0) {
					int index = rand.nextInt(form.getqIds().size());
					questionIds.add(form.getqIds().get(index));
					form.getqIds().remove(index);
				} else {
					break;
				}
			}
		}
		return questionIds;
	}

	/**
	 * 智能出卷
	 * 
	 * @since yoomath v2.1.2 增加新知识点 wanlong.che 2016-11-23
	 * @param form
	 * @return
	 */
	List<Long> pullPaperQuestions(PaperPullForm form) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		qb.must(QueryBuilders.rangeQuery("difficulty").gt(form.getMinDifficulty().doubleValue())
				.lte(form.getMaxDifficulty().doubleValue()));
		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
				Lists.newArrayList(form.getTextBookCode())));
		BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.MULTIPLE_CHOICE.getValue()));
		qb.must(typeQb);

		// 匹配知识点
		if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			// 新知识点
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					form.getKnowledgePoints()));
		} else if (CollectionUtils.isNotEmpty(form.getMetaKnowpoints())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					form.getMetaKnowpoints()));
		}

		Page docPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null);
		// 查询数据库
		List<Long> qustionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			qustionIds.add(Long.parseLong(document.getId()));
		}
		List<Long> ids = new ArrayList<Long>(form.getCount());
		int num = qustionIds.size() < form.getCount() ? qustionIds.size() : form.getCount();
		// 随机获取20个
		for (int i = 0; i < num; i++) {
			Random rand = new Random();
			int randNum = rand.nextInt(qustionIds.size());
			ids.add(qustionIds.get(randNum));
			qustionIds.remove(randNum);
		}
		return ids;
	}

	/**
	 * 
	 * @since yoomath v2.1.2 增加新知识点 wanlong.che 2016-11-23
	 * @param form
	 * @return
	 */
	List<Long> textbookExercise(PullQuestionForm form) {
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.MULTIPLE_CHOICE.getValue()));
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.FILL_BLANK.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.TRUE_OR_FALSE.getValue()));
		qb.must(typeQb);

		// 匹配知识点
		if (CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			// 新知识点
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					form.getKnowledgePoints()));
		} else if (CollectionUtils.isNotEmpty(form.getMetaKnowpoints())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("metaKnowpointCodes",
					form.getMetaKnowpoints()));
		}

		Page searchPage = searchService.search(IndexType.QUESTION, 0, SEARCH_PAGESIZE, qb, null);
		if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
			List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
			for (Document doc : searchPage.getDocuments()) {
				QuestionIndexDoc indexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				qIds.add(indexDoc.getResourceId());
			}
			Random rand = new Random();
			for (int i = 0; i < form.getCount(); i++) {
				if (qIds.size() > 0) {
					int index = rand.nextInt(qIds.size());
					questionIds.add(qIds.get(index));
					qIds.remove(index);
				}
			}

		}
		return questionIds;

	}

	/**
	 * 根据知识点进行加强练习
	 *
	 * @param form
	 *            {@link PullQuestionForm}
	 * @return 题目id列表
	 */
	@SuppressWarnings("unchecked")
	List<Long> enhanceExercise(PullQuestionForm form) {
		// 若没有新知识点数据则直接返回空串
		if (CollectionUtils.isEmpty(form.getKnowledgePoints())) {
			return Collections.EMPTY_LIST;
		}

		int avgCount = form.getCount() / form.getKnowledgePoints().size();
		// 按照正常取题目
		List<Long> questionIds = new ArrayList<Long>(form.getCount());
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));

		BoolQueryBuilder typeQb = QueryBuilders.boolQuery();
		typeQb.should(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.MULTIPLE_CHOICE.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.FILL_BLANK.getValue()));
		// typeQb.should(QueryBuilders.termQuery("type",
		// Question.Type.TRUE_OR_FALSE.getValue()));
		qb.must(typeQb);

		// 去除当前题目
		if (CollectionUtils.isNotEmpty(form.getqIds())) {
			for (Long qId : form.getqIds()) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}

		// 先易后难
		Order order = new Order("difficulty", Direction.DESC);
		Map<Long, List<Long>> codeQIds = new HashMap<Long, List<Long>>(form.getKnowledgePoints().size());
		List<Long> searchIds = new ArrayList<Long>();
		for (Long code : form.getKnowledgePoints()) {
			List<Long> codes = new ArrayList<Long>(1);
			codes.add(code);
			BoolQueryBuilder searchQuery = QueryBuilders.boolQuery();
			searchQuery.must(qb);
			searchQuery.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes", codes));

			Page searchPage = searchService.search(IndexType.QUESTION, 0, 100, searchQuery, null, order);
			if (CollectionUtils.isNotEmpty(searchPage.getDocuments())) {
				List<Long> qIds = new ArrayList<Long>(searchPage.getDocuments().size());
				for (Document doc : searchPage.getDocuments()) {
					QuestionIndexDoc questionIndexDoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
					qIds.add(questionIndexDoc.getResourceId());
				}

				codeQIds.put(code, qIds);
			}

		}

		// 若上一次拉取题目不为空，此次为空，从头拉取数据
		if (CollectionUtils.isNotEmpty(form.getqIds()) && codeQIds.size() == 0) {
			form.setqIds(Collections.EMPTY_LIST);
			return enhanceExercise(form);
		} else if (codeQIds.size() == 0) {
			return Collections.EMPTY_LIST;
		}

		// 随机从知识点中进行数据取值
		for (Map.Entry<Long, List<Long>> e : codeQIds.entrySet()) {
			List<Long> qIds = e.getValue();
			if (CollectionUtils.isEmpty(qIds)) {
				continue;
			}

			int flag = 0;
			for (Long qId : qIds) {
				if (questionIds.contains(qId)) {
					continue;
				}

				if (flag >= avgCount) {
					break;
				}

				questionIds.add(qId);
				flag++;
			}
			searchIds.addAll(qIds);
		}

		// 当练习的数量不足的时候直接进行补全操作
		if (questionIds.size() < form.getCount()) {
			Random random = new Random();
			while (questionIds.size() < form.getCount()) {
				if (searchIds.size() == 0) {
					break;
				}

				int index = random.nextInt(searchIds.size());
				Long id = searchIds.get(index);
				if (!questionIds.contains(id)) {
					questionIds.add(searchIds.get(index));
				}
				searchIds.remove(index);
			}
		}

		return questionIds;
	}
}
