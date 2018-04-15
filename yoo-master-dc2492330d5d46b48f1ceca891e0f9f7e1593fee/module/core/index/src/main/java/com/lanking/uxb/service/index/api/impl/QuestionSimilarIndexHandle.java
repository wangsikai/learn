package com.lanking.uxb.service.index.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.index.value.QuestionSimilarIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 相似题索引.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月18日
 */
@Service
public class QuestionSimilarIndexHandle extends AbstractIndexHandle {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private QuestionSimilarService questionSimilarService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;

	private int maxSimilar = 20;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.QUESTION_SIMILAR == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.QUESTION_SIMILAR;
	}

	@Override
	public String getDescription() {
		return "相似题索引";
	}

	@Override
	public long dataCount() {
		return -1;
	}

	/**
	 * @param id
	 *            习题ID
	 */
	@Override
	public void add(Long id) {
		Question question = questionSimilarService.getQuestion(id);

		// 知识点
		List<Long> knowledgeIds = questionKnowledgeService.queryKnowledgeByQuestionId(question.getId());
		Document document = this.handleOneQuestionDoc(question, knowledgeIds);
		if (document != null) {
			putDocument(document);
		}
	}

	/**
	 * @param ids
	 *            习题IDs
	 */
	@Override
	public void add(Collection<Long> ids) {
		List<Question> questions = questionSimilarService.mgetQuestionList(ids);

		// 知识点
		List<Long> questionIds = new ArrayList<Long>(questions.size());
		for (Question question : questions) {
			questionIds.add(question.getId());
		}
		Map<Long, List<Long>> questionKnowledges = questionKnowledgeService.mgetByQuestions(questionIds);

		List<Document> documents = new ArrayList<Document>(questions.size());
		for (Question question : questions) {
			List<Long> knowledgeIds = questionKnowledges.get(question.getId());
			Document document = this.handleOneQuestionDoc(question, knowledgeIds);
			if (document != null) {
				documents.add(document);
			}
		}
		if (documents.size() > 0) {
			putDocuments(documents);
		}
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.QUESTION_SIMILAR, id.toString());
	}

	@Override
	public void delete(Collection<Long> bizIds) {
		for (Long bizId : bizIds) {
			this.delete(bizId);
		}
	}

	/**
	 * 相似题组索引修改
	 * <p>
	 * 仅针对2017-03-28之后新录入的已通过的题做处理
	 * </p>
	 * 
	 * @since 2017-05-09
	 */
	@Override
	public void reindex() {
		int page = 1;
		int size = 200;
		int total = 0;

		long t1 = System.currentTimeMillis();
		Page<Question> questionPage = questionSimilarService.queryNewSimilarBaseQuestion(page, size);

		// 知识点
		List<Long> questionIds = new ArrayList<Long>(questionPage.getItemSize());
		for (Question question : questionPage.getItems()) {
			questionIds.add(question.getId());
		}
		Map<Long, List<Long>> questionKnowledges = questionKnowledgeService.mgetByQuestions(questionIds);

		List<Document> documents = new ArrayList<Document>();
		for (Question question : questionPage.getItems()) {
			List<Long> knowledgeIds = questionKnowledges.get(question.getId());
			Document document = this.handleOneQuestionDoc(question, knowledgeIds);
			if (null != document) {
				documents.add(document);
			}
		}

		long t2 = System.currentTimeMillis();
		logger.info("[相似题索引] t2-t1=" + (t2 - t1) / 1000);
		if (documents.size() > 0) {
			total += documents.size();
			logger.info("[相似题索引]共" + questionPage.getPageCount() + "页，已处理" + page + "页，已完成" + documents.size() + "条，当前共"
					+ total + "条");
			putDocuments(documents);
		} else {
			logger.info("[相似题索引]共" + questionPage.getPageCount() + "页，已处理" + page + "页，已完成" + documents.size() + "条，当前共"
					+ total + "条");
		}
		if (questionPage.getPageCount() >= 2) {
			for (int i = 2; i <= questionPage.getPageCount(); i++) {
				questionPage = questionSimilarService.queryNewSimilarBaseQuestion(i, size);

				// 知识点
				questionIds = new ArrayList<Long>(questionPage.getItemSize());
				for (Question question : questionPage.getItems()) {
					questionIds.add(question.getId());
				}
				questionKnowledges = questionKnowledgeService.mgetByQuestions(questionIds);

				documents = new ArrayList<Document>();
				long t3 = System.currentTimeMillis();
				for (Question question : questionPage.getItems()) {
					List<Long> knowledgeIds = questionKnowledges.get(question.getId());
					Document document = this.handleOneQuestionDoc(question, knowledgeIds);
					if (null != document) {
						documents.add(document);
					}
				}
				long t4 = System.currentTimeMillis();
				logger.info("[相似题索引] t3-t4=" + (t3 - t4) / 1000);

				if (documents.size() > 0) {
					total += documents.size();
					logger.info("[相似题索引]共" + questionPage.getPageCount() + "页，已处理" + i + "页，已完成" + documents.size()
							+ "条，当前共" + total + "条");
					putDocuments(documents);
				} else {
					logger.info("[相似题索引]共" + questionPage.getPageCount() + "页，已处理" + i + "页，已完成" + documents.size()
							+ "条，当前共" + total + "条");
				}
			}
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(QuestionSimilarIndexDoc doc) {
		return new Document(IndexType.QUESTION_SIMILAR.toString(), String.valueOf(doc.getMd5()), doc.documentValue());
	}

	/**
	 * 处理单个习题文档.
	 * 
	 * @param document
	 */
	private Document handleOneQuestionDoc(Question question, List<Long> knowledgeIds) {
		return handleOneQuestionDoc(question.getId(), question.getContent(), question.getPhaseCode(),
				question.getSubjectCode(), knowledgeIds, question.getVendorId());
	}

	/**
	 * 处理单个习题文档.
	 * 
	 * @param questionId
	 *            指定习题的ID
	 * @param content
	 *            题干
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @param vendorId
	 *            供应商
	 * @return
	 */
	private Document handleOneQuestionDoc(long questionId, String content, int phaseCode, int subjectCode,
			List<Long> knowledgeIds, long vendorId) {

		Set<Long> kds = new HashSet<Long>();
		if (knowledgeIds != null) {
			for (Long code : knowledgeIds) {
				kds.add(code / 100);
			}
		}

		// 搜索相似题
		List<Long> questionIds = new ArrayList<Long>();
		VPage<Document> similarDocPage = this.queryAllQuestionBase(1, maxSimilar, content, subjectCode, vendorId, kds,
				true);
		float baseScore = 1;
		boolean hasCurrentQuestion = false;
		for (int i = 0; i < similarDocPage.getItems().size(); i++) {
			Document similarDocument = similarDocPage.getItems().get(i);
			if (i == 0) {
				baseScore = similarDocument.getScore();
			}
			long qid = Long.parseLong(similarDocument.getId());
			if (qid == questionId) {
				hasCurrentQuestion = true;
			}

			if (similarDocument.getScore() / baseScore >= 0.9) {
				questionIds.add(qid);
			} else {
				break;
			}
		}
		if (!hasCurrentQuestion) {
			// 不包含当前评分基准题时，需要将基准题加入
			if (questionIds.size() < maxSimilar) {
				questionIds.add(questionId);
			} else {
				questionIds.set(maxSimilar - 1, questionId);
			}
		}
		if (questionIds.size() <= 1) {
			return null;
		}
		Collections.sort(questionIds);
		String md5Str = "";
		for (Long id : questionIds) {
			md5Str += id;
		}

		// 已有相似题组
		Document sameDocument = searchService.get(IndexType.QUESTION_SIMILAR, md5Str);
		if (sameDocument != null && StringUtils.isNotBlank(sameDocument.getValue())) {
			return null;
		}

		if (questionIds.size() > 0) {
			QuestionSimilarIndexDoc questionSimilarIndexDoc = new QuestionSimilarIndexDoc();
			questionSimilarIndexDoc.setCreateAt(System.currentTimeMillis());
			questionSimilarIndexDoc.setPhaseCode(phaseCode);
			questionSimilarIndexDoc.setSubjectCode(subjectCode);
			questionSimilarIndexDoc.setQuestionIds(questionIds);
			questionSimilarIndexDoc.setMd5(Codecs.md5Hex(md5Str));
			questionSimilarIndexDoc.setBaseQuestionId(questionId);
			questionSimilarIndexDoc.setVendorId(vendorId);
			return this.convert(questionSimilarIndexDoc);
		}
		return null;
	}

	/**
	 * 分页查询需要分组的题库题目.
	 * 
	 * @since 2017-05-09 去除未校验的题目
	 * 
	 * @param offset
	 *            偏移量
	 * @param size
	 *            数量
	 * @param resourceId
	 *            指定习题ID
	 * @param content
	 *            题干
	 * @param subjectCode
	 *            学科
	 * @return
	 */
	private VPage<Document> queryAllQuestionBase(int page, int size, String content, int subjectCode, long vendorId,
			Set<Long> knowledges, boolean scoreOrder) {
		BoolQueryBuilder qb = null;
		VPage<Document> vPage = new VPage<Document>();
		vPage.setCurrentPage(page);
		vPage.setPageSize(size);
		List<Document> documents = new ArrayList<Document>();
		try {
			List<Order> orders = new ArrayList<Order>();
			if (scoreOrder) {
				orders.add(Order.desc("_score"));
			} else {
				orders.add(Order.asc("questionId"));
			}
			qb = QueryBuilders.boolQuery();

			// 非校本题目
			qb.must(QueryBuilders.termQuery("schoolId", 0));

			// 题目状态
			Set<Integer> status = new HashSet<Integer>();
			// status.add(CheckStatus.EDITING.getValue());
//			status.add(CheckStatus.CHECKING.getValue());
//			status.add(CheckStatus.ONEPASS.getValue());
			status.add(CheckStatus.PASS.getValue());
//			status.add(CheckStatus.NOPASS.getValue());
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("status", status));

			// 习题类型
			Set<Integer> types = new HashSet<Integer>();
			types.add(Question.Type.SINGLE_CHOICE.getValue());
			types.add(Question.Type.MULTIPLE_CHOICE.getValue());
			types.add(Question.Type.FILL_BLANK.getValue());
			types.add(Question.Type.TRUE_OR_FALSE.getValue());
			types.add(Question.Type.QUESTION_ANSWERING.getValue());

			// 学科
			qb.must(QueryBuilders.termQuery("subjectCode", subjectCode));

			// 供应商
			qb.must(QueryBuilders.termQuery("vendorId", vendorId));

			// 题干处理
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

			if (knowledges.size() > 0) {
				// 限定知识点（三级）
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes", knowledges));
			}

			Order[] orderArray = new Order[orders.size()];
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				orderArray[i] = order;
			}
			com.lanking.uxb.service.search.api.Page docPage = searchService.search(
					Lists.<IndexTypeable>newArrayList(IndexType.QUESTION_SIMILAR_BASE), (page - 1) * size, size, qb,
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

	@Override
	public void continueReindex(Integer startPage, Integer endPage) {
		reindex();
	}
}
