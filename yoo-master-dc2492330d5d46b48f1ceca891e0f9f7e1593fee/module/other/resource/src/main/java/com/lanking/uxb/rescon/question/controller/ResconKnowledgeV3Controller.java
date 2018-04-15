package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgFormatException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeReviewService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSyncService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeReviewService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeSyncService;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.rescon.book.convert.ResconBookConvert;
import com.lanking.uxb.rescon.book.convert.ResconBookVersionConvert;
import com.lanking.uxb.rescon.book.form.BookQueryForm;
import com.lanking.uxb.rescon.book.value.VBook;
import com.lanking.uxb.rescon.book.value.VBookVersion;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperQuestionManage;
import com.lanking.uxb.rescon.exam.convert.ResconExamConvert;
import com.lanking.uxb.rescon.exam.convert.ResconExamOption;
import com.lanking.uxb.rescon.exam.form.QueryForm;
import com.lanking.uxb.rescon.exam.value.VExam;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;
import com.lanking.uxb.rescon.question.api.ResconKnowledgeV3Service;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.cache.ResconQuestionKnowledgeV3CacheService;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.form.QuestionQueryForm;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 知识点V3转换相关.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping("rescon/okn3")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK" })
public class ResconKnowledgeV3Controller {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconBookVersionConvert bookVersionConvert;
	@Autowired
	private ResconBookManage bookManage;
	@Autowired
	private ResconBookConvert bookConvert;
	@Autowired
	private ResconKnowledgeV3Service knowledgeV3Service;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconKnowledgeSyncService knowledgeSyncService;
	@Autowired
	private ResconKnowledgeReviewService knowledgeReviewService;
	@Autowired
	private ResconQuestionKnowledgeSyncService questionKnowledgeSyncService;
	@Autowired
	private ResconQuestionKnowledgeReviewService questionKnowledgeReviewService;
	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	private ResconQuestionConvert resconQuestionConvert;
	@Autowired
	private ResconExamManage resconExamManage;
	@Autowired
	private ResconExamConvert examConvert;
	@Autowired
	private ResconExamPaperQuestionManage examPaperQuestionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private IndexService indexService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconQuestionKnowledgeV3CacheService questionKnowledgeV3CacheService;

	/**
	 * 查询书本.
	 * 
	 * @param form
	 *            查询参数
	 * @return
	 */
	@RequestMapping(value = "queryBook", method = RequestMethod.POST)
	public Value queryBook(BookQueryForm form) {
		int offset = (form.getPage() - 1) * form.getPageSize();
		int size = form.getPageSize();

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId()); // 供应商限定
		com.lanking.cloud.sdk.data.Page<Long> page = bookManage.queryBook(user.getVendorId(), form,
				P.offset(offset, size));

		// 查询数据库
		List<Long> bookIds = page.getItems();
		List<VBookVersion> bookVersions = bookVersionConvert
				.to(bookManage.listMainBookVersion(bookIds, BookStatus.PASS)); // 版本
		Map<Long, VBook> bookMap = bookConvert.to(bookManage.mgetBook(bookIds)); // 书本
		for (VBookVersion bookVersion : bookVersions) {
			VBook book = bookMap.get(bookVersion.getBookId());
			if (book == null) {
				book = new VBook();
				book.setId(bookVersion.getBookId());
				book.setNum(1);
				bookMap.put(bookVersion.getBookId(), book);
			} else {
				book.setNum(book.getNum() + 1);
			}
			if (bookVersion.isMainFlag()) {
				book.setMainVersion(bookVersion);
			} else {
				book.setDeputyVersion(bookVersion);
			}
		}

		List<VBook> books = new ArrayList<VBook>(bookMap.size());
		for (Long id : bookIds) {
			books.add(bookMap.get(id));
		}

		// 获取书本未转换知识点题目个数
		List<Long> versionIds = new ArrayList<Long>(books.size());
		for (VBook book : books) {
			versionIds.add(book.getMainVersion().getId());
		}
		Map<Long, Integer> counts = knowledgeV3Service.findVersionNoV3Counts(versionIds);
		for (VBook book : books) {
			long versionId = book.getMainVersion().getId();
			book.getMainVersion().setNoV3Count(counts.get(versionId) == null ? 0 : counts.get(versionId));
		}

		VPage<VBook> vPage = new VPage<VBook>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(page.getTotalCount());
		vPage.setTotalPage(page.getPageCount());
		vPage.setItems(books);
		return new Value(vPage);
	}

	/**
	 * 获取书本目录未处理题目个数.
	 * 
	 * @param bookVersionId
	 * @param
	 * @return
	 */
	@RequestMapping(value = "getBookVersionNoDatas", method = RequestMethod.POST)
	public Value getBookVersionNoDatas(Long bookVersionId,
			@RequestParam(name = "catalogIds", required = false) List<Long> catalogIds) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		int bookVersionNoV3Count = knowledgeV3Service.findVersionNoV3Count(bookVersionId);
		map.put("bookVersionNoV3Count", bookVersionNoV3Count);

		if (CollectionUtils.isNotEmpty(catalogIds)) {
			Map<Long, Integer> catalogNoV3CountMap = knowledgeV3Service.findCatalogNoV3Counts(bookVersionId,
					catalogIds);
			List<Map<String, Object>> catalogNoV3Counts = new ArrayList<Map<String, Object>>(
					catalogNoV3CountMap.size());
			for (Entry<Long, Integer> entry : catalogNoV3CountMap.entrySet()) {
				Map<String, Object> data = new HashMap<String, Object>(2);
				data.put("catalogId", entry.getKey());
				data.put("count", entry.getValue());
				catalogNoV3Counts.add(data);
			}
			map.put("catalogNoV3Counts", catalogNoV3Counts);
		}

		return new Value(map);
	}

	/**
	 * 获得新旧知识点树结构.
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param hasCount
	 *            是否包含统计数量
	 * @return
	 */
	@RequestMapping(value = "getKnowsTree")
	public Value getKnowsTree(Integer phaseCode, Boolean hasCount) {
		if (phaseCode == null) {
			return new Value(new MissingArgumentException());
		}
		hasCount = hasCount == null ? false : hasCount;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int subjectCode = phaseCode * 100 + 2; // 学科代码

		// v2.0 知识点树
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAll(phaseCode, subjectCode, null, null);
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findAllByStatus(phaseCode, subjectCode,
				Status.ENABLED);
		List<Map<String, Object>> knowledgePointTree = new ArrayList<Map<String, Object>>(
				knowledgeSystems.size() + knowledgePoints.size());
		for (int i = 0; i < knowledgeSystems.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgeSystem knowledgeSystem = knowledgeSystems.get(i);
			map.put("id", knowledgeSystem.getCode());
			map.put("pId", knowledgeSystem.getPcode());
			map.put("title", knowledgeSystem.getName());
			map.put("name", knowledgeSystem.getName());
			map.put("nocheck", true);
			map.put("isParent", true);
			knowledgePointTree.add(map);
		}
		for (int i = 0; i < knowledgePoints.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			KnowledgePoint knowledgePoint = knowledgePoints.get(i);
			map.put("id", knowledgePoint.getCode());
			map.put("pId", knowledgePoint.getPcode());
			map.put("name", knowledgePoint.getName());
			map.put("title", knowledgePoint.getName());
			map.put("nocheck", false);
			knowledgePointTree.add(map);
		}
		returnMap.put("v2Tree", knowledgePointTree); // 知识点树
		returnMap.put("knowledgePoints", knowledgePoints); // 元知识点列表

		// v3.0 同步知识点树
		List<KnowledgeSync> allKnowledgeSyncs = knowledgeSyncService.findAll(phaseCode, subjectCode, Status.ENABLED);
		List<Map<String, Object>> knowledgeSyncTree = new ArrayList<Map<String, Object>>(allKnowledgeSyncs.size());
		List<KnowledgeSync> knowledgeSyncs = new ArrayList<KnowledgeSync>();
		for (KnowledgeSync knowledgeSync : allKnowledgeSyncs) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", knowledgeSync.getCode());
			map.put("pId", knowledgeSync.getPcode());
			map.put("title", knowledgeSync.getName());
			map.put("name", knowledgeSync.getName());
			map.put("nocheck", knowledgeSync.getLevel() != 4);
			map.put("isParent", knowledgeSync.getLevel() != 4);
			knowledgeSyncTree.add(map);
			if (knowledgeSync.getLevel() == 4) {
				knowledgeSyncs.add(knowledgeSync);
			}
		}
		returnMap.put("v3SyncTree", knowledgeSyncTree); // 同步知识点树
		returnMap.put("knowledgeSyncs", knowledgeSyncs); // 同步元知识点列表

		// v3.0 复习知识点树
		List<KnowledgeReview> allKnowledgeReviews = knowledgeReviewService.findAll(phaseCode, subjectCode,
				Status.ENABLED);
		List<Map<String, Object>> knowledgeReviewTree = new ArrayList<Map<String, Object>>(allKnowledgeReviews.size());
		List<KnowledgeReview> knowledgeReviews = new ArrayList<KnowledgeReview>();
		for (KnowledgeReview knowledgeReview : allKnowledgeReviews) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", knowledgeReview.getCode());
			map.put("pId", knowledgeReview.getPcode());
			map.put("title", knowledgeReview.getName());
			map.put("name", knowledgeReview.getName());
			map.put("nocheck", knowledgeReview.getLevel() != 4);
			map.put("isParent", knowledgeReview.getLevel() != 4);
			knowledgeReviewTree.add(map);
			if (knowledgeReview.getLevel() == 4) {
				knowledgeReviews.add(knowledgeReview);
			}
		}
		returnMap.put("v3ReviewTree", knowledgeReviewTree); // 复习知识点树
		returnMap.put("knowledgeReviews", knowledgeReviews); // 复习元知识点列表

		return new Value(returnMap);
	}

	/**
	 * 保存题目新知识点.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param syncIds
	 *            同步知识点
	 * @param reviewIds
	 *            复习知识点
	 * @return
	 */
	@RequestMapping(value = "saveCorrect")
	public Value saveCorrect(Long questionId, Long[] syncIds, Long[] reviewIds) {
		if (null == questionId || (null == syncIds && reviewIds == null)) {
			return new Value(new MissingArgumentException());
		}

		try {
			if (syncIds != null && syncIds.length > 0) {
				questionKnowledgeSyncService.saveQuestionKnowledgeSync(questionId, Lists.newArrayList(syncIds));
			}
			if (reviewIds != null && reviewIds.length > 0) {
				questionKnowledgeReviewService.saveQuestionKnowledgeReview(questionId, Lists.newArrayList(reviewIds));
			}
			resconQuestionManage.updateKnowledgeCreator(questionId, Security.getUserId());

			// 更新习题索引
			indexService.syncAdd(IndexType.QUESTION, questionId);

			// 更新统计缓存
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			questionKnowledgeV3CacheService.increment(user.getId(), user.getVendorId());

			// 更新缓存
			questionKnowledgeV3CacheService.invalidDoingQuestionByUser(Security.getUserId());
			questionKnowledgeV3CacheService.invalidSelfDoingQuestion(Security.getUserId());

			Question question = resconQuestionManage.get(questionId);
			return new Value(resconQuestionConvert.to(question));
		} catch (AbstractException e) {
			e.printStackTrace();
			return new Value(e);
		}
	}

	/**
	 * 试卷查询接口
	 * 
	 * @param queryForm
	 *            查询信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "queryExam", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryExam(QueryForm queryForm) {
		Long vendorId = vendorUserManage.getVendorUser(Security.getUserId()).getVendorId();
		queryForm.setVendorId(vendorId);
		Page<ExamPaper> page = resconExamManage.queryResconExam(queryForm);
		VPage<VExam> vpage = new VPage<VExam>();
		vpage.setPageSize(queryForm.getPageSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(examConvert.to(page.getItems()));
		}

		// 获取试卷未转换知识点题目个数
		List<Long> examPaperIds = new ArrayList<Long>(vpage.getItems().size());
		for (VExam examPaper : vpage.getItems()) {
			examPaperIds.add(examPaper.getId());
		}
		Map<Long, Integer> counts = knowledgeV3Service.findPaperNoV3Counts(examPaperIds);
		for (VExam examPaper : vpage.getItems()) {
			examPaper.setNoV3Count(counts.get(examPaper.getId()) == null ? 0 : counts.get(examPaper.getId()));
		}

		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(queryForm.getPage());
		return new Value(vpage);
	}

	/**
	 * 查看试卷
	 * 
	 * @param examId
	 * @return
	 */
	@RequestMapping(value = "viewExam", method = { RequestMethod.POST, RequestMethod.GET })
	public Value viewExam(@RequestParam(value = "examId") long examId) {
		ExamPaper examPaper = resconExamManage.get(examId);
		if (examPaper == null) {
			return new Value(new EntityNotFoundException());
		}
		VExam exam = examConvert.to(examPaper, new ResconExamOption(true, true, true));
		List<ExamPaperQuestion> epqList = examPaperQuestionManage.getExamQuestionByExam(examId);
		List<Long> qIds = Lists.newArrayList();
		for (ExamPaperQuestion question : epqList) {
			qIds.add(question.getQuestionId());
		}
		Map<Long, VQuestion> vqMap = resconQuestionConvert.to(resconQuestionManage.mget(qIds));
		if (CollectionUtils.isNotEmpty(exam.getTopics())) {
			int questionCount = 0;
			int totalScore = 0;
			// 构造题型中包含题目的结构
			for (VExamPaperTopic examTopic : exam.getTopics()) {
				List<VQuestion> vqListTemp = Lists.newArrayList();
				for (ExamPaperQuestion examPaperQuestion : epqList) {
					if (examTopic.getExamId().equals(examPaperQuestion.getExamPaperId())
							&& examTopic.getId().equals(examPaperQuestion.getTopicId())) {
						if (examPaperQuestion.getScore() != null) {
							totalScore = totalScore + examPaperQuestion.getScore();
						}
						vqMap.get(examPaperQuestion.getQuestionId())
								.setScore(examPaperQuestion.getScore() == null ? null : examPaperQuestion.getScore());
						vqListTemp.add(vqMap.get(examPaperQuestion.getQuestionId()));
					}
				}
				examTopic.setQuestionList(vqListTemp);
				questionCount = questionCount + vqListTemp.size();
			}
			exam.setQuestionCount(questionCount);
			exam.setScore(totalScore);
		} else {
			exam.setTopics(new ArrayList<VExamPaperTopic>());
			exam.setQuestionCount(0);
			exam.setScore(0);
		}

		int noV3Count = knowledgeV3Service.findPaperNoV3Count(exam.getId());
		exam.setNoV3Count(noV3Count);

		return new Value(exam);
	}

	/**
	 * 习题查询.
	 * 
	 * @param form
	 *            习题查询表单.
	 * @return
	 */
	@RequestMapping(value = "query")
	@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK", "VENDOR_BUILD" })
	public Value query(QuestionQueryForm form) {
		int offset = 0;
		int size = 0;
		List<Order> orders = new ArrayList<Order>();
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.QUESTION); // 搜索习题
		BoolQueryBuilder qb = null;
		try {
			offset = (form.getPage() - 1) * form.getPageSize();
			size = form.getPageSize();
			orders = new ArrayList<Order>();
			qb = QueryBuilders.boolQuery();
			orders.add(new Order("createAt", Direction.ASC));
			qb.must(QueryBuilders.termQuery("checkStatus", form.getCheckStatus().getValue()));

			if (CollectionUtils.isNotEmpty(form.getNotHasQuestionIds())) {
				qb.mustNot(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("resourceId",
						form.getNotHasQuestionIds()));
			}

			// 排除重复题
			qb.mustNot(QueryBuilders.existsQuery("sameShowId"));

			if (form.getSearchFlag() == 4) {
				// 知识点修正
				BoolQueryBuilder qbin = QueryBuilders.boolQuery();
				qbin.should(QueryBuilders.existsQuery("knowledgeSyncCodes"));
				qbin.should(QueryBuilders.existsQuery("knowledgeReviewCodes"));
				qb.mustNot(qbin);
			}
			if (null != form.getPhaseCode()) {
				qb.must(QueryBuilders.termQuery("phaseCode", form.getPhaseCode()));
			}
			if (null != form.getSubjectCode()) {
				qb.must(QueryBuilders.termQuery("subjectCode", form.getSubjectCode()));
			}

			// 同步与复习知识点取题不分供应商
			if (form.getSearchFlag() != 5 && form.getSearchFlag() != 6) {
				qb.must(QueryBuilders.termQuery("vendorId",
						vendorUserManage.getVendorUser(Security.getUserId()).getVendorId()));
			}

			if (form.getCreateBt() != null && form.getCreateEt() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getCreateEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				qb.must(QueryBuilders.rangeQuery("createAt").gte(form.getCreateBt()).lte(cal.getTime().getTime()));
			}

			// 题型限定单选、填空、解答
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("type",
					Lists.newArrayList(Question.Type.SINGLE_CHOICE.getValue(), Question.Type.FILL_BLANK.getValue(),
							Question.Type.QUESTION_ANSWERING.getValue())));

			if (null != form.getKnowledgePointCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgePointCodes",
						Lists.newArrayList(form.getKnowledgePointCode())));
			}
			if (null != form.getKnowledgeSyncCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgeSyncCodes",
						Lists.newArrayList(form.getKnowledgeSyncCode())));
			}
			if (null != form.getKnowledgeReviewCode()) {
				qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("knowledgeReviewCodes",
						Lists.newArrayList(form.getKnowledgeReviewCode())));
			}
		} catch (Exception e) {
			return new Value(new IllegalArgFormatException());
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);

		// 查询数据库
		List<Long> qustionIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				qustionIds.add(Long.parseLong(document.getId()));
			}
		}

		Map<Long, VQuestion> qusetionMap = questionConvert.to(resconQuestionManage.mget(qustionIds));
		List<VQuestion> qusetionList = new ArrayList<VQuestion>(qustionIds.size());
		for (Long id : qustionIds) {
			VQuestion question = qusetionMap.get(id);
			qusetionList.add(question);
		}

		VPage<VQuestion> vPage = new VPage<VQuestion>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(qusetionList);
		return new Value(vPage);
	}

	/**
	 * 获得需要转换的题目.
	 * 
	 * @param form
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "getQuestion")
	public Value getQuestion(QuestionQueryForm form) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		// 获取当前用户正在处理的习题
		Long questionId = questionKnowledgeV3CacheService.getSelfDoingQuestion(user.getId());
		if (questionId != null) {
			VQuestion vQuestion = resconQuestionConvert.get(questionId);
			return new Value(vQuestion);
		}

		// 避免获取到他人正在校验的题目
		Set<Long> notHasQuestionIds = questionKnowledgeV3CacheService.getDoingCache();
		if (CollectionUtils.isNotEmpty(notHasQuestionIds)) {
			form.setNotHasQuestionIds(Lists.newArrayList(notHasQuestionIds));
		}

		form.setPageSize(1);
		VPage<VQuestion> vPage = (VPage<VQuestion>) this.query(form).getRet();
		VQuestion vQuestion = null;
		if (vPage.getItems().size() > 0) {
			vQuestion = vPage.getItems().get(0);

			// 更新缓存
			questionKnowledgeV3CacheService.setDoingQuestion(user.getId(), vQuestion.getId());
			questionKnowledgeV3CacheService.setSelfDoingQuestion(user.getId(), vQuestion.getId());
		}
		return new Value(vQuestion);
	}

	/**
	 * 获取统计数据等.
	 * 
	 * @return
	 */
	@RequestMapping(value = "calDatas")
	public Value calDatas(Integer phaseCode) {
		if (phaseCode == null) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -1);

		long mAll = questionKnowledgeV3CacheService.getSelfDo(user.getId(), null); // 个人修正总数
		long mYAll = questionKnowledgeV3CacheService.getSelfDo(user.getId(), cal.getTime()); // 个人昨日修正数
		long aYAll = questionKnowledgeV3CacheService.getAllDo(user.getVendorId(), cal.getTime()); // 全部昨日修正数
		long oldAll = knowledgeV3Service.getNoHasV3KPQuestionCount(user.getVendorId(), phaseCode); // 待修正题目数
		long syncAll = knowledgeV3Service.getSyncKPQuestionCount(user.getVendorId(), null, phaseCode); // 同步知识点题目数
		long reviewAll = knowledgeV3Service.getReviewKPQuestionCount(user.getVendorId(), null, phaseCode); // 复习知识点题目数

		Map<String, Long> map = new HashMap<String, Long>();
		map.put("mAll", mAll);
		map.put("mYAll", mYAll);
		map.put("aYAll", aYAll);
		map.put("oldAll", oldAll);
		map.put("syncAll", syncAll);
		map.put("reviewAll", reviewAll);
		return new Value(map);
	}
}
