package com.lanking.uxb.rescon.question.api.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.rescon.question.api.ResconQuestionSimilarBaseManage;

/**
 * 基础相似题库服务接口实现.
 * 
 * @author wlche
 *
 */
@Service
public class ResconQuestionSimilarBaseManageImpl implements ResconQuestionSimilarBaseManage {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SearchService searchService;

	@Override
	public VPage<Document> queryAllQuestionBase(int page, int size, Question question) {
		BoolQueryBuilder qb = null;
		VPage<Document> vPage = new VPage<Document>();
		vPage.setCurrentPage(page);
		vPage.setPageSize(size);
		List<Document> documents = new ArrayList<Document>();
		try {
			List<Order> orders = new ArrayList<Order>();
			orders.add(Order.desc("_score"));
			qb = QueryBuilders.boolQuery();

			// 排除本题
			qb.mustNot(QueryBuilders.termQuery("questionId", question.getId()));

			// 题目状态
			Set<Integer> status = new HashSet<Integer>();
			// status.add(CheckStatus.EDITING.getValue());
			status.add(CheckStatus.ONEPASS.getValue());
			status.add(CheckStatus.PASS.getValue());
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("status", status));

			// 习题类型
			Set<Integer> types = new HashSet<Integer>();
			types.add(Question.Type.SINGLE_CHOICE.getValue());
			types.add(Question.Type.MULTIPLE_CHOICE.getValue());
			types.add(Question.Type.FILL_BLANK.getValue());
			types.add(Question.Type.TRUE_OR_FALSE.getValue());
			types.add(Question.Type.QUESTION_ANSWERING.getValue());

			// 学科
			qb.must(QueryBuilders.termQuery("subjectCode", question.getSubjectCode()));

			// 供应商
			qb.must(QueryBuilders.termQuery("vendorId", question.getVendorId()));

			// 题干处理
			String content = question.getContent();
			if (StringUtils.isNotBlank(content)) {
				// 去除标签
				content = content.replaceAll(
						"<(p|/p|table|/table|tr|/tr|td|/td|th|/th|span|/span|ux-mth|/ux-mth|img|br|/br|div|/div|ux-blank|/ux-blank)+[\\s\\S]*?>",
						"");
				content = content.replace("&nbsp;", " ");
				Set<String> texts = this.getSearchTexts(content);
				for (String text : texts) {
					qb.must(QueryBuilders.termQuery("content", text));
				}
				Set<String> notexts = this.getNoSearchTexts(content);
				for (String text : notexts) {
					qb.mustNot(QueryBuilders.termQuery("content", text));
				}
				qb.must(QueryBuilders.matchQuery("content", content).minimumShouldMatch("90%"));
			}

			Order[] orderArray = new Order[orders.size()];
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				orderArray[i] = order;
			}
			com.lanking.uxb.service.search.api.Page docPage = searchService.search(
					Lists.<IndexTypeable> newArrayList(IndexType.QUESTION_SIMILAR_BASE), (page - 1) * size, size, qb,
					null, orderArray);

			for (Document document : docPage.getDocuments()) {
				if (document.getId() != null && !document.getId().equals("null")) {
					documents.add(document);
				}
			}

			vPage.setTotal(docPage.getTotalCount());
			vPage.setTotalPage(docPage.getTotalPage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		vPage.setItems(documents);
		return vPage;
	}

	/**
	 * 需要添加搜索的字串.
	 * 
	 * @param content
	 *            输入字串
	 * @return
	 */
	private Set<String> getSearchTexts(String content) {
		Set<String> texts = new HashSet<String>();
		if (content.indexOf("frac") != -1) {
			texts.add("frac");
		}
		if (content.indexOf("sqrt") != -1) {
			texts.add("sqrt");
		}
		if (content.indexOf("left") != -1) {
			texts.add("left");
		}
		if (content.indexOf("right") != -1) {
			texts.add("right");
		}
		if (content.indexOf("prime") != -1) {
			texts.add("prime");
		}
		if (content.indexOf("because") != -1) {
			texts.add("because");
		}
		if (content.indexOf("in") != -1) {
			texts.add("in");
		}
		if (content.indexOf("int") != -1) {
			texts.add("int");
		}
		if (content.indexOf("therefore") != -1) {
			texts.add("therefore");
		}
		if (content.indexOf("angle") != -1) {
			texts.add("angle");
		}
		if (content.indexOf("triangle") != -1) {
			texts.add("triangle");
		}
		if (content.indexOf("bigcap") != -1) {
			texts.add("bigcap");
		}
		if (content.indexOf("bigcup") != -1) {
			texts.add("bigcup");
		}
		if (content.indexOf("bar") != -1) {
			texts.add("bar");
		}
		if (content.indexOf("hat") != -1) {
			texts.add("hat");
		}
		if (content.indexOf("limits") != -1) {
			texts.add("limits");
		}
		if (content.indexOf("log") != -1) {
			texts.add("log");
		}
		if (content.indexOf("sum") != -1) {
			texts.add("sum");
		}
		if (content.indexOf("begin") != -1) {
			texts.add("begin");
		}
		if (content.indexOf("end") != -1) {
			texts.add("end");
		}
		if (content.indexOf("circ") != -1) {
			texts.add("circ");
		}
		return texts;
	}

	/**
	 * 不需要添加搜索的字串.
	 * 
	 * @param content
	 *            输入字串
	 * @return
	 */
	private Set<String> getNoSearchTexts(String content) {
		Set<String> texts = new HashSet<String>();
		if (content.indexOf("frac") == -1) {
			texts.add("frac");
		}
		if (content.indexOf("sqrt") == -1) {
			texts.add("sqrt");
		}
		if (content.indexOf("left") == -1) {
			texts.add("left");
		}
		if (content.indexOf("right") == -1) {
			texts.add("right");
		}
		if (content.indexOf("prime") == -1) {
			texts.add("prime");
		}
		if (content.indexOf("because") == -1) {
			texts.add("because");
		}
		if (content.indexOf("in") == -1) {
			texts.add("in");
		}
		if (content.indexOf("int") == -1) {
			texts.add("int");
		}
		if (content.indexOf("therefore") == -1) {
			texts.add("therefore");
		}
		if (content.indexOf("angle") == -1) {
			texts.add("angle");
		}
		if (content.indexOf("triangle") == -1) {
			texts.add("triangle");
		}
		if (content.indexOf("limits") == -1) {
			texts.add("limits");
		}
		if (content.indexOf("log") == -1) {
			texts.add("log");
		}
		if (content.indexOf("sum") == -1) {
			texts.add("sum");
		}
		if (content.indexOf("begin") == -1) {
			texts.add("begin");
		}
		if (content.indexOf("end") == -1) {
			texts.add("end");
		}
		if (content.indexOf("circ") == -1) {
			texts.add("circ");
		}
		return texts;
	}
}
