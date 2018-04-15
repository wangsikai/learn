package com.lanking.uxb.service.examPaper.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.examPaper.api.SmartPaperService;
import com.lanking.uxb.service.examPaper.form.SmartExamPaperForm;

/**
 * 智能组卷拉题逻辑
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class SmartPaperServiceImpl implements SmartPaperService {
	@Autowired
	private SearchService searchService;
	@Autowired
	private SectionService sectionService;

	@Override
	public List<Long> queryQuestionsByIndex(SmartExamPaperForm form) {
		int size = form.getAnswerNum() + form.getChoiceNum() + form.getFillBlankNum();
		List<Long> ids = new ArrayList<Long>(size);

		if (form.getChoiceNum() > 0) {
			form.setQuestionType(Question.Type.SINGLE_CHOICE.getValue());
			// 需要拉取的题目总数，且存在那么多的题目
			form.setSize(form.getChoiceNum());
			form.setNotQuestionIds(null);
			List<Long> choiceIds = queryByType(form);
			ids.addAll(choiceIds);
		}
		if (form.getFillBlankNum() > 0) {
			form.setQuestionType(Question.Type.FILL_BLANK.getValue());
			form.setSize(form.getFillBlankNum());
			form.setNotQuestionIds(null);
			List<Long> fillBlankIds = queryByType(form);
			ids.addAll(fillBlankIds);
		}
		if (form.getAnswerNum() > 0) {
			form.setQuestionType(Question.Type.QUESTION_ANSWERING.getValue());
			form.setSize(form.getAnswerNum());
			form.setNotQuestionIds(null);
			List<Long> qaIds = queryByType(form);
			ids.addAll(qaIds);
		}
		return ids;
	}

	/**
	 * 不同题型下根据不同难度百分比<br>
	 * 1.如果只有1个,给比例大的。如果只有2个，给比例大的两个。<br>
	 * 2.如果用户所需题目数量正好等于索引里存在数量,直接全部取出来，不需要判断<br>
	 * 3.百分比2个较小的,向下取整,最大的取剩下的<br>
	 * 
	 * @param form
	 * @return
	 */
	public List<Long> queryByType(SmartExamPaperForm form) {
		Integer size = form.getSize();
		List<Long> ids = new ArrayList<Long>(size);
		// 排序规则，先按百分比排序，再按难易程序排序
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		map1.put("base", form.getBasePercent());
		map2.put("raise", form.getRaisePercent());
		map3.put("sprint", form.getSprintPercent());
		list.add(map1);
		list.add(map2);
		list.add(map3);
		Collections.sort(list, new Comparator<Map<String, Integer>>() {

			@Override
			public int compare(Map<String, Integer> o1, Map<String, Integer> o2) {
				Integer a1 = o1.get(o1.keySet().toArray()[0]);
				Integer b1 = o2.get(o2.keySet().toArray()[0]);
				if (a1 > b1) {
					return -1;
				} else if (b1 > a1) {
					return 1;
				}
				return 0;
			}
		});
		if (form.getSize() == 1) {
			String key = list.get(0).keySet().iterator().next();
			form.setQuestionDiffType(key);
			form.setSize(1);
			ids = query(form);
			// 如果比例大的没有取到元素,去相邻难度拉取题目
			if (CollectionUtils.isEmpty(ids)) {
				ids = this.supplementQuestions(key, 1, form);
			}
		} else if (form.getSize() == 2) {
			// 如果第二大为0，全给比例大的
			if (list.get(1).get(list.get(1).keySet().iterator().next()) == 0) {
				String key = list.get(0).keySet().iterator().next();
				form.setQuestionDiffType(key);
				form.setSize(2);
				ids = query(form);
				if (ids.size() < 2) {
					ids.addAll(this.supplementQuestions(key, 2 - ids.size(), form));
				}
			} else {
				form.setQuestionDiffType(list.get(0).keySet().iterator().next());
				form.setSize(1);
				List<Long> temp1 = query(form);
				form.setQuestionDiffType(list.get(1).keySet().iterator().next());
				form.setSize(1);
				List<Long> temp2 = query(form);
				ids.addAll(temp1);
				ids.addAll(temp2);
				if (ids.size() < 2) {
					form.setQuestionDiffType(list.get(2).keySet().iterator().next());
					form.setSize(2 - ids.size());
					form.setNotQuestionIds(ids);
					List<Long> temp3 = query(form);
					ids.addAll(temp3);
					if (ids.size() < 2) {
						form.setNotQuestionIds(ids);
						if (temp1.size() == 0) {
							form.setQuestionDiffType(list.get(1).keySet().iterator().next());
							form.setSize(1);
							ids.addAll(query(form));
						}
						if (temp2.size() == 0) {
							form.setQuestionDiffType(list.get(0).keySet().iterator().next());
							form.setSize(1);
							ids.addAll(query(form));
						}
					}
				}
			}
		} else {
			// 比例最小的两个，向下取整
			//
			String key1 = list.get(1).keySet().iterator().next();
			Integer val1 = list.get(1).get(key1);
			form.setQuestionDiffType(key1);
			int size1 = 0;
			// 剩下多少没有取到
			int left1 = 0;
			if (val1 != 0) {
				// 只要有百分比至少保证1个
				size1 = (int) Math.floor((size * val1) / 100) == 0 ? 1 : (int) Math.floor((size * val1) / 100);
				// 向下取整
				form.setSize(size1);
				List<Long> temp1 = query(form);
				ids.addAll(temp1);
				// 如果实际取到的值不够,需要到其他难度的题里面补
				if (temp1.size() < size1) {
					left1 = size1 - temp1.size();
				}
			}
			//
			String key2 = list.get(2).keySet().iterator().next();
			Integer val2 = list.get(2).get(key2);
			form.setQuestionDiffType(key2);
			int size2 = 0;
			int left2 = 0;
			if (val2 != 0) {
				size2 = (int) Math.floor((size * val2) / 100) == 0 ? 1 : (int) Math.floor((size * val2) / 100);
				form.setSize(size2);
				List<Long> temp2 = query(form);
				ids.addAll(temp2);
				if (temp2.size() < size2) {
					left2 = size2 - temp2.size();
				}
			}

			// 比例大的取剩下的
			String key3 = list.get(0).keySet().iterator().next();
			form.setQuestionDiffType(key3);
			int size3 = size - size1 - size2;
			int left3 = 0;
			form.setSize(size3);
			List<Long> temp3 = query(form);
			ids.addAll(temp3);
			if (temp3.size() < size3) {
				left3 = size3 - temp3.size();
			}
			if (left1 > 0) {
				form.setNotQuestionIds(ids);
				ids.addAll(this.supplementQuestions(key1, left1, form));
			}
			if (left2 > 0) {
				form.setNotQuestionIds(ids);
				ids.addAll(this.supplementQuestions(key2, left2, form));
			}
			if (left3 > 0) {
				form.setNotQuestionIds(ids);
				ids.addAll(this.supplementQuestions(key3, left3, form));
			}

		}
		return ids;
	}

	/**
	 * 补数据规则<br>
	 * a.基础题不够，从提高题中补，提高也不够，才从冲刺题中取<br>
	 * b.提高题不够，从基础题中补，基础也不够，再考虑冲刺题<br>
	 * c.冲刺题不够，从提高题中补，提高题不够再从基础题中补
	 * 
	 * @param ids
	 * @param currentType
	 *            当前需要补充的类型
	 * @param size
	 *            需要补充的个数
	 * @param form
	 *            查询索引所需要的条件
	 * @return
	 */
	public List<Long> supplementQuestions(String currentType, int size, SmartExamPaperForm form) {
		List<Long> ids = new ArrayList<Long>();
		if (currentType == "base") {
			form.setQuestionDiffType("raise");
			form.setSize(size);
			ids = query(form);
			if (CollectionUtils.isEmpty(ids)) {
				form.setQuestionDiffType("sprint");
				form.setSize(size);
				ids = query(form);
			} else {
				if (ids.size() < size) {
					form.setQuestionDiffType("sprint");
					form.setSize(size - ids.size());
					ids.addAll(query(form));
				}
			}
		} else if (currentType == "raise") {
			form.setQuestionDiffType("base");
			form.setSize(size);
			ids = query(form);
			if (CollectionUtils.isEmpty(ids)) {
				form.setQuestionDiffType("sprint");
				form.setSize(size);
				ids = query(form);
			} else {
				if (ids.size() < size) {
					form.setQuestionDiffType("sprint");
					form.setSize(size - ids.size());
					ids.addAll(query(form));
				}
			}
		} else if (currentType == "sprint") {
			form.setQuestionDiffType("raise");
			form.setSize(size);
			ids = query(form);
			if (CollectionUtils.isEmpty(ids)) {
				form.setQuestionDiffType("base");
				form.setSize(size);
				ids = query(form);
			} else {
				if (ids.size() < size) {
					form.setQuestionDiffType("base");
					form.setSize(size - ids.size());
					ids.addAll(query(form));
				}
			}
		}
		return ids;
	}

	@Override
	public List<Long> query(SmartExamPaperForm form) {
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable> newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = null;
		int offset = 0;
		int size = form.getSize();
		orders = new ArrayList<Order>();
		orders.add(new Order("difficulty", Direction.DESC));
		qb = QueryBuilders.boolQuery();
		if (form.getTextbookCode() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes2",
					Lists.newArrayList(form.getTextbookCode())));
		}
		if (form.getKnowledgeCodes() != null) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
					Lists.newArrayList(form.getKnowledgeCodes())));
		}
		if (form.getQuestionType() != null) {
			qb.must(QueryBuilders.termQuery("type", form.getQuestionType()));
		}
		if (form.getNotQuestionIds() != null) {
			qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("resourceId",
					Lists.newArrayList(form.getNotQuestionIds())));
		}
		if (form.getSectionCode() != null) {
			Section section = sectionService.get(form.getSectionCode());
			if (section.isComprehensiveSection()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
						Lists.newArrayList(section.getPcode())));
			} else {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes2",
						Lists.newArrayList(form.getSectionCode())));
			}
		}

		// 审核通过
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		// 获取公共题库
		qb.must(QueryBuilders.termQuery("schoolId", 0));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.COMPOSITE.getValue()));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.MULTIPLE_CHOICE.getValue()));
		qb.mustNot(QueryBuilders.termQuery("type", Question.Type.TRUE_OR_FALSE.getValue()));
		if (form.getQuestionDiffType() != null) {
			Double leDifficulty = 0.0;
			Double reDifficulty = 0.0;
			if (form.getQuestionDiffType() == "base") {
				leDifficulty = 0.8;
				reDifficulty = 1.1;
			} else if (form.getQuestionDiffType() == "raise") {
				leDifficulty = 0.4;
				reDifficulty = 0.8;
			} else if (form.getQuestionDiffType() == "sprint") {
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
		return ids;
	}
}
