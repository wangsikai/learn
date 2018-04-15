package com.lanking.uxb.service.web.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;
import com.lanking.uxb.service.web.api.ZyTeaPertinenceHomeworkService;
import com.lanking.uxb.service.web.form.PertinenceHomeworkForm;

/**
 * 针对性训练作业相关接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version yoomath v2.3.2 2016年12月8日
 */
@Service
public class ZyTeaPertinenceHomeworkServiceImpl implements ZyTeaPertinenceHomeworkService {

	@Autowired
	private SearchService searchService;
	@Autowired
	private KnowledgePointService knowledgePointService;

	/**
	 * 获取针对性训练作业题目.
	 * 
	 */
	@Override
	public List<Long> queryPertinenceHomeworkQuestions(PertinenceHomeworkForm form) {
		int maxSize = 25;

		List<KnowledgePoint> noKnowledgePoints = knowledgePointService.findAll(form.getKnowledgeCodes());
		List<Long> noKnowledgePointCodes = new ArrayList<Long>(noKnowledgePoints.size());
		for (KnowledgePoint knowledgePoint : noKnowledgePoints) {
			noKnowledgePointCodes.add(knowledgePoint.getCode());
		}

		// 题目难度百分比排序
		List<Map<String, Integer>> diffPercents = this.diffPercent(form.getBasePercent(), form.getRaisePercent(),
				form.getSprintPercent());

		// 选择题不同难度题目数量
		Map<String, Integer> choiceNums = this.calQuestionNum(form.getChoiceNum(), diffPercents);

		// 填空题不同难度题目数量
		Map<String, Integer> blankNums = this.calQuestionNum(form.getFillBlankNum(), diffPercents);

		// 简答题不同难度题目数量
		Map<String, Integer> answerNums = this.calQuestionNum(form.getAnswerNum(), diffPercents);

		// 题目集合
		Map<Question.Type, List<Long>> dataMap = new HashMap<Question.Type, List<Long>>(3);
		dataMap.put(Question.Type.SINGLE_CHOICE, new ArrayList<Long>());
		dataMap.put(Question.Type.FILL_BLANK, new ArrayList<Long>());
		dataMap.put(Question.Type.QUESTION_ANSWERING, new ArrayList<Long>());

		// 基础题搜索
		Set<Long> noQuestionIds = new HashSet<Long>();
		if (form.getBasePercent() > 0) {
			String questionDiffType = "base";
			this.findQuestionsByRule(choiceNums.get(questionDiffType), blankNums.get(questionDiffType),
					answerNums.get(questionDiffType), maxSize, questionDiffType, form.getKnowledgeCodes(),
					noKnowledgePointCodes, dataMap, noQuestionIds, questionDiffType);
		}

		// 提高题搜索
		if (form.getRaisePercent() > 0) {
			String questionDiffType = "raise";
			this.findQuestionsByRule(choiceNums.get(questionDiffType), blankNums.get(questionDiffType),
					answerNums.get(questionDiffType), maxSize, questionDiffType, form.getKnowledgeCodes(),
					noKnowledgePointCodes, dataMap, noQuestionIds, questionDiffType);
		}

		// 冲刺题搜索
		if (form.getSprintPercent() > 0) {
			String questionDiffType = "sprint";
			this.findQuestionsByRule(choiceNums.get(questionDiffType), blankNums.get(questionDiffType),
					answerNums.get(questionDiffType), maxSize, questionDiffType, form.getKnowledgeCodes(),
					noKnowledgePointCodes, dataMap, noQuestionIds, questionDiffType);
		}

		List<Long> questionIds = new ArrayList<Long>();
		questionIds.addAll(dataMap.get(Question.Type.SINGLE_CHOICE));
		questionIds.addAll(dataMap.get(Question.Type.FILL_BLANK));
		questionIds.addAll(dataMap.get(Question.Type.QUESTION_ANSWERING));

		return questionIds;
	}

	/**
	 * 百分比排序.
	 * 
	 * @param basePercent
	 *            基础题
	 * @param raisePercent
	 *            提高题
	 * @param sprintPercent
	 *            冲刺题
	 * @return
	 */
	private List<Map<String, Integer>> diffPercent(int basePercent, int raisePercent, int sprintPercent) {
		List<Map<String, Integer>> diffPercents = new ArrayList<Map<String, Integer>>(3);
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		map1.put("base", basePercent);
		map2.put("raise", raisePercent);
		map3.put("sprint", sprintPercent);
		diffPercents.add(map1);
		diffPercents.add(map2);
		diffPercents.add(map3);
		Collections.sort(diffPercents, new Comparator<Map<String, Integer>>() {
			@Override
			public int compare(Map<String, Integer> o1, Map<String, Integer> o2) {
				Integer a1 = o1.get(o1.keySet().toArray()[0]);
				Integer b1 = o2.get(o2.keySet().toArray()[0]);
				if (a1 > b1) {
					return 1;
				} else if (b1 > a1) {
					return -1;
				}
				return 0;
			}
		});
		return diffPercents;
	}

	/**
	 * 根据百分比分解题目个数.
	 * 
	 * @param num
	 *            总题目个数
	 * @param diffPersent
	 *            百分比
	 * @return
	 */
	private Map<String, Integer> calQuestionNum(int num, List<Map<String, Integer>> diffPercents) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String key1 = diffPercents.get(0).keySet().iterator().next();
		String key2 = diffPercents.get(1).keySet().iterator().next();
		String key3 = diffPercents.get(2).keySet().iterator().next();
		int count1 = 0, count2 = 0, count3 = 0;
		if (num == 1) {
			count3 = 1;
		} else if (num == 2) {
			count2 = 1;
			count3 = 1;
		} else if (num > 2) {
			count1 = (int) (num * diffPercents.get(0).get(key1) / 100);
			count2 = (int) (num * diffPercents.get(1).get(key2) / 100);
			if (count1 < 1 && diffPercents.get(0).get(key1) > 0) {
				count1 = 1;
			}
			if (count2 < 1 && diffPercents.get(1).get(key2) > 0) {
				count2 = 1;
			}
			count3 = num - count1 - count2;
		}
		map.put(key1, count1);
		map.put(key2, count2);
		map.put(key3, count3);
		return map;
	}

	/**
	 * 按照规则查找题目.
	 * <p>
	 * 题型内数量不够时，按照同一个难度进行补题，选择题 -> 填空题 -> 解答题，然后再进行下一个难度补题<br>
	 * 规则详见：http://dev.proto.elanking.com/exercise/V2.3.0/#g=1&p=教师首页
	 * </p>
	 * 
	 * @param choiceNum
	 *            选择题取值数量
	 * @param blankNum
	 *            填空题取值数量
	 * @param answerNum
	 *            解答题取题数量
	 * @param maxSize
	 *            最大总数
	 * @param questionDiffType
	 *            难度
	 * @param knowledgePointCodes
	 *            包含的知识点
	 * @param noKnowledgePointCodes
	 *            不包含的知识点
	 * @param dataMap
	 *            题目集合
	 * @param noQuestionIds
	 *            排除的题目集合
	 * @param initQuestionDiffType
	 *            初始搜索的难度（递归判断使用）
	 */
	private void findQuestionsByRule(int choiceNum, int blankNum, int answerNum, int maxSize, String questionDiffType,
			List<Long> knowledgePointCodes, List<Long> noKnowledgePointCodes, Map<Question.Type, List<Long>> dataMap,
			Set<Long> noQuestionIds, String initQuestionDiffType) {

		List<Long> choices = new ArrayList<Long>(); // 选择题列表
		List<Long> blanks = new ArrayList<Long>(); // 选择题列表
		List<Long> answers = new ArrayList<Long>(); // 选择题列表
		if (choiceNum > 0) {
			choices = this.query(questionDiffType, Question.Type.SINGLE_CHOICE, knowledgePointCodes,
					noKnowledgePointCodes, maxSize, noQuestionIds);
		}
		if (blankNum > 0 || choiceNum - choices.size() > 0) {
			blanks = this.query(questionDiffType, Question.Type.FILL_BLANK, knowledgePointCodes, noKnowledgePointCodes,
					maxSize, noQuestionIds);
		}
		if (answerNum > 0 || choiceNum + blankNum - choices.size() - blanks.size() > 0) {
			answers = this.query(questionDiffType, Question.Type.QUESTION_ANSWERING, knowledgePointCodes,
					noKnowledgePointCodes, maxSize, noQuestionIds);
		}

		// 选择题处理
		int lastChoicenum = choiceNum - choices.size();
		if (lastChoicenum > 0) {
			// 选择题数量不够，首先从填空题补
			dataMap.get(Question.Type.SINGLE_CHOICE).addAll(choices);
			choices.clear();
			lastChoicenum -= blanks.size();
			if (lastChoicenum > 0) {
				// 填空题仍然不够，从解答题补
				dataMap.get(Question.Type.FILL_BLANK).addAll(blanks);
				blanks.clear();
				lastChoicenum -= answers.size();
				if (lastChoicenum > 0) {
					// 解答题仍然不够，放到下一轮难度
					dataMap.get(Question.Type.QUESTION_ANSWERING).addAll(answers);
					answers.clear();
				} else {
					List<Long> tempList = dataMap.get(Question.Type.QUESTION_ANSWERING);
					for (int i = 0; i < lastChoicenum + answers.size(); i++) {
						tempList.add(answers.get(i));
					}
					answers.subList(0, lastChoicenum + answers.size()).clear();
				}
			} else {
				List<Long> tempList = dataMap.get(Question.Type.FILL_BLANK);
				for (int i = 0; i < lastChoicenum + blanks.size(); i++) {
					tempList.add(blanks.get(i));
				}
				blanks.subList(0, lastChoicenum + blanks.size()).clear();
			}
		} else if (choiceNum > 0) {
			List<Long> tempList = dataMap.get(Question.Type.SINGLE_CHOICE);
			for (int i = 0; i < choiceNum; i++) {
				tempList.add(choices.get(i));
			}
			choices.subList(0, choiceNum).clear();
		}

		// 填空题处理
		int lastBlanknum = blankNum - blanks.size();
		if (lastBlanknum > 0) {
			// 填空题数量不够，首先从选择题补
			dataMap.get(Question.Type.FILL_BLANK).addAll(blanks);
			blanks.clear();
			lastBlanknum -= choices.size();
			if (lastBlanknum > 0) {
				// 选择题仍然不够，从解答题补
				dataMap.get(Question.Type.SINGLE_CHOICE).addAll(choices);
				choices.clear();
				lastBlanknum -= answers.size();
				if (lastBlanknum > 0) {
					// 解答题仍然不够，放到下一轮难度
					dataMap.get(Question.Type.QUESTION_ANSWERING).addAll(answers);
					answers.clear();
				} else {
					List<Long> tempList = dataMap.get(Question.Type.QUESTION_ANSWERING);
					for (int i = 0; i < lastBlanknum + answers.size(); i++) {
						tempList.add(answers.get(i));
					}
					answers.subList(0, lastBlanknum + answers.size()).clear();
				}
			} else {
				List<Long> tempList = dataMap.get(Question.Type.SINGLE_CHOICE);
				for (int i = 0; i < lastBlanknum + choices.size(); i++) {
					tempList.add(choices.get(i));
				}
				choices.subList(0, lastBlanknum + choices.size()).clear();
			}
		} else if (blankNum > 0) {
			List<Long> tempList = dataMap.get(Question.Type.FILL_BLANK);
			for (int i = 0; i < blankNum; i++) {
				tempList.add(blanks.get(i));
			}
			blanks.subList(0, blankNum).clear();
		}

		// 简答题处理
		int lastAnswerknum = answerNum - answers.size();
		if (lastAnswerknum > 0) {
			// 简答题数量不够，首先从填空题补
			dataMap.get(Question.Type.QUESTION_ANSWERING).addAll(answers);
			answers.clear();
			lastAnswerknum -= blanks.size();
			if (lastAnswerknum > 0) {
				// 填空题仍然不够，从选择题补
				dataMap.get(Question.Type.FILL_BLANK).addAll(blanks);
				blanks.clear();
				lastAnswerknum -= choices.size();
				if (lastAnswerknum > 0) {
					// 选择题仍然不够，放到下一轮难度
					dataMap.get(Question.Type.SINGLE_CHOICE).addAll(choices);
					choices.clear();
				} else {
					List<Long> tempList = dataMap.get(Question.Type.SINGLE_CHOICE);
					for (int i = 0; i < lastAnswerknum + choices.size(); i++) {
						tempList.add(choices.get(i));
					}
					choices.subList(0, lastAnswerknum + choices.size()).clear();
				}
			} else {
				List<Long> tempList = dataMap.get(Question.Type.FILL_BLANK);
				for (int i = 0; i < lastAnswerknum + blanks.size(); i++) {
					tempList.add(blanks.get(i));
				}
				blanks.subList(0, lastAnswerknum + blanks.size()).clear();
			}
		} else if (answerNum > 0) {
			List<Long> tempList = dataMap.get(Question.Type.QUESTION_ANSWERING);
			for (int i = 0; i < answerNum; i++) {
				tempList.add(answers.get(i));
			}
			answers.subList(0, answerNum).clear();
		}

		// 本轮未完成，继续临近的难度查询
		// 初始容易难度，临近搜索顺序为：中等 -> 困难
		// 初始中等难度，临近搜索顺序为：容易 -> 困难
		// 初始困难难度，临近搜索顺序为：中等 -> 容易
		if ((lastChoicenum > 0 || lastBlanknum > 0 || lastAnswerknum > 0)) {
			String next = "";
			if (("raise".equals(questionDiffType) && "raise".equals(initQuestionDiffType))
					|| ("raise".equals(questionDiffType) && "sprint".equals(initQuestionDiffType))) {
				next = "base";
			} else if (("base".equals(questionDiffType) && "base".equals(initQuestionDiffType))
					|| ("sprint".equals(questionDiffType) && "sprint".equals(initQuestionDiffType))) {
				next = "raise";
			} else if (("raise".equals(questionDiffType) && "base".equals(initQuestionDiffType))
					|| ("base".equals(questionDiffType) && "raise".equals(initQuestionDiffType))) {
				next = "sprint";
			} else {
				return; // 结束递归
			}

			lastChoicenum = lastChoicenum < 0 ? 0 : lastChoicenum;
			lastBlanknum = lastBlanknum < 0 ? 0 : lastBlanknum;
			lastAnswerknum = lastAnswerknum < 0 ? 0 : lastAnswerknum;
			this.findQuestionsByRule(lastChoicenum, lastBlanknum, lastAnswerknum, maxSize, next, knowledgePointCodes,
					noKnowledgePointCodes, dataMap, noQuestionIds, initQuestionDiffType);
		}
	}

	/**
	 * 索引查找题目.
	 * 
	 * @param questionDiffType
	 *            难度范围
	 * @param questionType
	 *            题型
	 * @param knowledgePointCodes
	 *            知识点
	 * @param size
	 *            个数
	 * @param noQuestionIds
	 *            排除搜索的题目集合
	 * @return
	 */
	private List<Long> query(String questionDiffType, Question.Type questionType, List<Long> knowledgePointCodes,
			List<Long> noKnowledgePointCodes, int size, Set<Long> noQuestionIds) {
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable> newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = null;
		int offset = 0;
		orders = new ArrayList<Order>();
		orders.add(new Order("difficulty", Direction.DESC));
		qb = QueryBuilders.boolQuery();
		if (knowledgePointCodes != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					Lists.newArrayList(knowledgePointCodes)));
		}
		if (questionType != null) {
			qb.must(QueryBuilders.termQuery("type", questionType.getValue()));
		}

		// 排除的题目
		if (noQuestionIds != null && noQuestionIds.size() > 0) {
			qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("resourceId",
					Lists.newArrayList(noQuestionIds)));
		}

		// 排除的知识点
		if (noKnowledgePointCodes != null && noKnowledgePointCodes.size() > 0) {
			qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					noKnowledgePointCodes));
		}

		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));

		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));

		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.COMPOSITE.getValue()));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
		if (questionDiffType != null) {
			Double leDifficulty = 0.0;
			Double reDifficulty = 0.0;
			if ("base".equals(questionDiffType)) {
				leDifficulty = 0.8;
				reDifficulty = 1.1;
			} else if ("raise".equals(questionDiffType)) {
				leDifficulty = 0.4;
				reDifficulty = 0.8;
			} else {
				leDifficulty = 0.0;
				reDifficulty = 0.4;
			}
			qb.must(QueryBuilders.rangeQuery("difficulty").gte(leDifficulty.doubleValue()).lt(reDifficulty.doubleValue()));
		}
		Order orderArray[] = new Order[orders.size()];
		int i = 0;
		for (Order o : orders) {
			orderArray[i] = o;
			i++;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);
		List<Long> ids = new ArrayList<Long>(docPage.getPageSize());
		for (Document document : docPage.getDocuments()) {
			ids.add(Long.valueOf(document.getId()));
		}

		// 填充不再继续搜索的题目ID
		noQuestionIds.addAll(ids);
		return ids;
	}

	public BigDecimal getAvgDifficulty(List<Long> questionIds) {
		List<IndexTypeable> types = Lists.<IndexTypeable> newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		int offset = 0;
		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("resourceId", questionIds));
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, questionIds.size(), qb,
				null);
		double sum = 0.0;
		for (Document document : docPage.getDocuments()) {
			QuestionIndexDoc doc = JSON.parseObject(document.getValue(), QuestionIndexDoc.class);
			sum += doc.getDifficulty().doubleValue();
		}
		BigDecimal avgDiff = new BigDecimal((int) (sum / questionIds.size() * 100) / 100);
		return avgDiff;
	}
}
