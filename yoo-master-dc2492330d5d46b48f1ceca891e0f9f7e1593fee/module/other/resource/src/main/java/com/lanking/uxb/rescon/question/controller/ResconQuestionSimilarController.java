package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeService;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionSimilarManage;
import com.lanking.uxb.rescon.question.cache.ResconQuestionSimilarCacheService;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.form.SimilarQuestionsForm;
import com.lanking.uxb.rescon.question.form.SimilarSameQuestionForm;
import com.lanking.uxb.service.index.value.QuestionSimilarBaseIndexDoc;
import com.lanking.uxb.service.index.value.QuestionSimilarIndexDoc;
import com.lanking.uxb.service.search.api.IndexBuildService;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

/**
 * 相似题处理相关.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月17日
 */
@RestController
@RequestMapping("rescon/que/similar")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK" })
public class ResconQuestionSimilarController {
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	@Qualifier(value = "questionSimilarIndexHandle")
	private IndexBuildService indexBuildService;
	@Autowired
	private ResconQuestionSimilarManage questionSimilarManage;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconQuestionSimilarCacheService questionSimilarCacheService;
	@Autowired
	private ResconQuestionKnowledgeService questionKnowledgeService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 删除.
	 * 
	 * @return
	 */
	@RequestMapping("deleteAll")
	public Value deleteAll() {
		int page = 1;
		int size = 500;

		VPage<Document> questionSimilarDocPage = this.queryAllQuestionSimilar(page, size);
		for (Document document : questionSimilarDocPage.getItems()) {
			indexBuildService.deleteDocument(IndexType.QUESTION_SIMILAR, document.getId());
		}
		if (questionSimilarDocPage.getTotalPage() >= 2) {
			for (int i = 2; i <= questionSimilarDocPage.getTotalPage(); i++) {
				questionSimilarDocPage = this.queryAllQuestionSimilar(i, size);
				for (Document document : questionSimilarDocPage.getItems()) {
					indexBuildService.deleteDocument(IndexType.QUESTION_SIMILAR, document.getId());
				}
			}
		}

		// indexBuildService.deleteByType(IndexType.QUESTION_SIMILAR);
		return new Value();
	}

	/**
	 * 删除.
	 * 
	 * @return
	 */
	@RequestMapping("deleteAllBase")
	public Value deleteAllBase() {
		int page = 1;
		int size = 500;

		VPage<Document> questionSimilarDocPage = this.queryAllQuestionSimilarBase(page, size);
		long totalPage = questionSimilarDocPage.getTotalPage();
		for (int i = 0; i < totalPage; i++) {
			questionSimilarDocPage = this.queryAllQuestionSimilarBase(1, size);
			for (Document document : questionSimilarDocPage.getItems()) {
				indexBuildService.deleteDocument(IndexType.QUESTION_SIMILAR_BASE, document.getId());
			}
		}
		return new Value();
	}

	/**
	 * 获取相似题组.
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	private VPage<Document> queryAllQuestionSimilar(int page, int size) {
		BoolQueryBuilder qb = null;
		VPage<Document> vPage = new VPage<Document>();
		vPage.setCurrentPage(page);
		vPage.setPageSize(size);
		vPage.setItems(new ArrayList<Document>(0));

		qb = QueryBuilders.boolQuery();
		Page docPage = searchService.search(Lists.<IndexTypeable>newArrayList(IndexType.QUESTION_SIMILAR),
				(page - 1) * size, size, qb, null);

		List<Document> documents = new ArrayList<Document>(docPage.getPageSize());
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				documents.add(document);
			}
		}

		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(documents);
		return vPage;
	}

	/**
	 * 获取相似题组.
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	private VPage<Document> queryAllQuestionSimilarBase(int page, int size) {
		BoolQueryBuilder qb = null;
		VPage<Document> vPage = new VPage<Document>();
		vPage.setCurrentPage(page);
		vPage.setPageSize(size);
		vPage.setItems(new ArrayList<Document>(0));

		qb = QueryBuilders.boolQuery();
		Page docPage = searchService.search(Lists.<IndexTypeable>newArrayList(IndexType.QUESTION_SIMILAR_BASE),
				(page - 1) * size, size, qb, null);

		List<Document> documents = new ArrayList<Document>(docPage.getPageSize());
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				documents.add(document);
			}
		}

		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(documents);
		return vPage;
	}

	/**
	 * 获取相似题组.
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("queryAll")
	public Value queryAll(Integer page, Integer size) {
		page = page == null ? 1 : page;
		size = size == null ? 100 : size;
		VPage<Document> docPage = queryAllQuestionSimilar(page, size);
		return new Value(docPage);
	}

	/**
	 * 查看一道题的相似结果.
	 * 
	 * @param qid
	 * @return
	 */
	@RequestMapping("mk")
	public Value mockQuestionSimilar(Long questionId) {
		int maxSimilar = 20;
		Question question = questionManage.get(questionId);

		// 知识点
		List<KnowledgePoint> knowledgePoints = questionKnowledgeService.listByQuestion(questionId);

		List<Long> questionIds = new ArrayList<Long>();
		VPage<Document> similarDocPage = this.queryAllQuestionBase(1, maxSimilar, question.getContent(),
				question.getSubjectCode(), question.getVendorId(), knowledgePoints, true);
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

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Long> questionIds2 = new ArrayList<Long>((int) similarDocPage.getPageSize());
		for (Document doc : similarDocPage.getItems()) {
			questionIds2.add(Long.parseLong(doc.getId()));
		}
		Map<Long, Question> questionMap = questionManage.mget(questionIds2);
		for (Document doc : similarDocPage.getItems()) {
			QuestionSimilarBaseIndexDoc qsDoc = JSON.parseObject(doc.getValue(), QuestionSimilarBaseIndexDoc.class);
			Map<String, Object> qmap = new HashMap<String, Object>(3);
			qmap.put("id", qsDoc.getQuestionId());
			qmap.put("code", questionMap.get(qsDoc.getQuestionId()).getCode());
			qmap.put("type", Question.Type.findByValue(qsDoc.getType()).getName());
			qmap.put("content", questionMap.get(qsDoc.getQuestionId()).getContent());
			qmap.put("content2", qsDoc.getContent());
			qmap.put("score", doc.getScore());
			list.add(qmap);
		}
		map.put("questionIds", questionIds);
		map.put("questions", list);

		return new Value(map);
	}

	/**
	 * 分页查询需要分组的题库题目.
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
	private VPage<Document> queryAllQuestionBase(int page, int size, String content, Integer subjectCode, long vendorId,
			List<KnowledgePoint> knowledgePoints, boolean scoreOrder) {
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
			status.add(CheckStatus.EDITING.getValue());
			status.add(CheckStatus.CHECKING.getValue());
			status.add(CheckStatus.ONEPASS.getValue());
			status.add(CheckStatus.PASS.getValue());
			status.add(CheckStatus.NOPASS.getValue());
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
				// content = content.replaceAll("<[^>]*>", "");
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

			if (knowledgePoints.size() > 0) {
				// 限定知识点（三级）
				Set<Long> kps = new HashSet<Long>();
				for (KnowledgePoint knowledgePoint : knowledgePoints) {
					kps.add(knowledgePoint.getPcode());
				}
				if (kps.size() > 0) {
					qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes", kps));
				}
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
		if (content.indexOf("cap") != -1) {
			texts.add("cap");
		}
		if (content.indexOf("cup") != -1) {
			texts.add("cup");
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

	/**
	 * 保存题组设置.
	 * 
	 * @param form
	 *            表单参数
	 * @return
	 */
	@RequestMapping("saveSimilarQuestions")
	public Value saveSimilarQuestions(String json) {
		if (StringUtils.isBlank(json)) {
			return new Value(new MissingArgumentException());
		}

		SimilarQuestionsForm form = JSON.parseObject(json, SimilarQuestionsForm.class);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<String> showIdErrors = new ArrayList<String>(); // 展示习题已被设置为重复题
		List<String> sameIdErrors = new ArrayList<String>(); // 重复习题已被设置为展示题
		returnMap.put("showIdErrors", showIdErrors);
		returnMap.put("sameIdErrors", sameIdErrors);

		// 所有题目数据集合
		Set<Long> questionIds = Sets.newHashSet(form.getQuestionIds());
		questionIds.add(form.getBaseQuestionId());
		form.setQuestionIds(Lists.newArrayList(questionIds));
		Map<Long, Question> questions = questionManage.mget(form.getQuestionIds());

		// 重复题组判断
		List<SimilarSameQuestionForm> similarSameQuestionForms = form.getSimilarSameQuestionForms();
		if (similarSameQuestionForms != null && similarSameQuestionForms.size() > 0) {
			for (SimilarSameQuestionForm sameForm : similarSameQuestionForms) {
				Question oldQuestion = questions.get(sameForm.getShowId());
				if (oldQuestion.getSameShow() != null && oldQuestion.getSameShow() == false) {
					showIdErrors.add(oldQuestion.getCode());
				}
				for (Long sameId : sameForm.getSameIds()) {
					Question sameQuestion = questions.get(sameId);
					if (sameQuestion.getSameShow() != null && sameQuestion.getSameShow() == true) {
						sameIdErrors.add(sameQuestion.getCode());
					}
				}
			}
		}

		// 有题目已被设置过
		if (showIdErrors.size() > 0 || sameIdErrors.size() > 0) {
			return new Value(returnMap);
		}

		try {
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			questionSimilarManage.saveSimilarQuestions(form, user.getVendorId());

			// 清空用户当前缓存的相似题组
			questionSimilarCacheService.invalidUserSimilar(Security.getUserId());

			// 删除当前操作题组
			indexBuildService.deleteDocument(IndexType.QUESTION_SIMILAR, form.getMd5());

			// 记录删除的题组
			questionSimilarCacheService.setDeleteSimilar(form.getMd5());

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
				new Value(new ServerException());
			}
			return new Value();
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 获取待处理的相似题组.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("getQuestionSimilar")
	public Value getQuestionSimilarCache() {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		// 获取缓存题组
		Map cacheMap = questionSimilarCacheService.getUserSimilar(user.getId());
		String userCahce = cacheMap.get("userCahce") == null ? null : cacheMap.get("userCahce").toString();

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		if (StringUtils.isNotBlank(userCahce)) {
			// 已有当前用户缓存
			qb.must(QueryBuilders.termQuery("md5", userCahce)); // 搜索指定题组
		} else {
			// 没有当前用户缓存
			Set<String> allCahce = (Set) cacheMap.get("allCahce");
			if (CollectionUtils.isNotEmpty(allCahce)) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("md5", allCahce)); // 排除锁定题组
			}

			// 获取正在删除的题组
			Set<String> deleteSimilarCache = questionSimilarCacheService.getDeleteSimilar();
			if (CollectionUtils.isNotEmpty(deleteSimilarCache)) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("md5", deleteSimilarCache)); // 排除正在删除的题组
			}
		}
		qb.must(QueryBuilders.termQuery("vendorId", user.getVendorId()));

		Page docPage = searchService.search(IndexType.QUESTION_SIMILAR, 0, 1, qb, null, Order.asc("createAt"));

		// 若当前用户缓存已取不到题组
		if (StringUtils.isNotBlank(userCahce) && docPage.getTotalCount() == 0) {
			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("vendorId", user.getVendorId()));
			Set<String> allCahce = (Set) cacheMap.get("allCahce");
			if (CollectionUtils.isNotEmpty(allCahce)) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("md5", allCahce)); // 排除锁定题组
			}
			docPage = searchService.search(IndexType.QUESTION_SIMILAR, 0, 1, qb, null, Order.asc("createAt"));
		}

		// 处理返回数据
		Map<String, Object> returnMap = new HashMap<String, Object>(4);
		if (docPage.getDocuments().size() > 0) {
			Document document = docPage.getDocuments().get(0);
			questionSimilarCacheService.setUserSimilar(Security.getUserId(), document.getId()); // 设置当前操作题组
			QuestionSimilarIndexDoc doc = JSON.parseObject(document.getValue(), QuestionSimilarIndexDoc.class);

			// 处理题组中没有本题Id的问题
			if (!doc.getQuestionIds().contains(doc.getBaseQuestionId().longValue())) {
				doc.getQuestionIds().add(doc.getBaseQuestionId());
			}

			returnMap.put("doc", doc);
			List<Question> questions = questionManage.mgetList(doc.getQuestionIds());
			Set<Long> removeIds = new HashSet<Long>();
			if (questions.size() > 0) {
				for (int i = questions.size() - 1; i >= 0; i--) {
					if (questions.get(i).getSameShowId() != null) {
						// 已经设置过重复题，将该重复题的展示题替换进来
						if (!doc.getQuestionIds().contains(questions.get(i).getSameShowId().longValue())) {
							removeIds.add(questions.get(i).getSameShowId());
						}
						questions.remove(i);
					}
				}
				if (removeIds.size() > 0) {
					List<Question> removeShowQuestions = questionManage.mgetList(removeIds);
					questions.addAll(removeShowQuestions);

					// 替换原 questionIds
					doc.setQuestionIds(new ArrayList<Long>(questions.size()));
					for (Question question : questions) {
						doc.getQuestionIds().add(question.getId());
					}
				}

				// 因重复题去除变空
				if (questions.size() <= 1) {

					// 清空用户当前缓存的相似题组
					questionSimilarCacheService.invalidUserSimilar(Security.getUserId());

					// 删除当前操作题组
					indexBuildService.deleteDocument(IndexType.QUESTION_SIMILAR, document.getId());

					// 记录删除的题组
					questionSimilarCacheService.setDeleteSimilar(document.getId());

					// 继续搜寻下一组
					try {
						Thread.sleep(300);
						return getQuestionSimilarCache();
					} catch (InterruptedException e) {
						logger.error(e.getMessage(), e);
						new Value(new ServerException());
					}
				}
			}
			returnMap.put("vqs", questionConvert.to(questions));
			returnMap.put("total", docPage.getTotalCount());
		}

		return new Value(returnMap);
	}

	/**
	 * 释放缓存.
	 * 
	 * @return
	 */
	@RequestMapping(value = "clearQuestionSimilarCache")
	public Value clearQuestionSimilarCache() {
		try {
			questionSimilarCacheService.invalidAllUserSimilar();
		} catch (Exception e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 首页统计数据.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "similarCount")
	public Value similarCount() {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		Map<String, Object> returnMap = new HashMap<String, Object>(3);

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("vendorId", user.getVendorId()));

		// 缓存题组
		Map cacheMap = questionSimilarCacheService.getUserSimilar(user.getId());
		Set<String> allCahce = (Set) cacheMap.get("allCahce");
		if (CollectionUtils.isNotEmpty(allCahce)) {
			qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("md5", allCahce)); // 排除锁定题组
		}

		long totalSimilarCount = searchService.count(IndexType.QUESTION_SIMILAR, qb);
		returnMap.put("totalSimilarCount", totalSimilarCount);
		returnMap.putAll(questionSimilarManage.similarCounts(user.getVendorId()));
		return new Value(returnMap);
	}

	/**
	 * 获取相似习题.
	 * 
	 * @param questionId
	 *            题目ID
	 * @param size
	 *            获取数量
	 * @return
	 */
	@RequestMapping(value = "listSimilars")
	public Value listSimilars(Long questionId, Integer size) {
		if (questionId == null) {
			return new Value(new MissingArgumentException());
		}
		size = size == null ? 20 : size;
		List<Question> questions = questionSimilarManage.listSimilarQuestionsForWeb(questionId, size);
		return new Value(questionConvert.to(questions));
	}

	/**
	 * 重建相似题库数据（临时）.
	 * 
	 * @return
	 */
	@RequestMapping(value = "rebuild")
	public Value rebuildQuestionSimilar() {
		int pageNo = 0;
		int pageSize = 200;

		com.lanking.cloud.sdk.data.Page<QuestionSimilar> page = questionSimilarManage
				.queryOldDatas(P.offset(pageNo * pageSize, pageSize));
		while (page.isNotEmpty()) {
			questionSimilarManage.buildNewDatas(page.getItems());

			pageNo += 1;
			page = questionSimilarManage.queryOldDatas(P.offset(pageNo * pageSize, pageSize));
		}

		return new Value();
	}
}
