package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.cache.ResconQuestionKatexSpecsCacheService;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.form.QuestionForm;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.rescon.question.value.VQuestion2;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.util.QuestionKatexUtils;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

/**
 * 处理题目符号纠错.
 * 
 * @author peng.zhao
 * @version 2017年11月1日
 */
@RestController
@RequestMapping("rescon/questionKatex")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK" })
public class ResconQuestionKatexSpecsController {

	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconAnswerManage answerManage;
	@Autowired
	private ResconQuestionKatexSpecsCacheService questionKatexSpecsCacheService;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private IndexService indexService;

	/**
	 * 获取校验使用的相关数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "questionList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questionList(Integer pageSize, Integer page) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 查询条件
		int offset = 0;
		int size = 0;
		offset = (page - 1) * pageSize;
		size = pageSize;

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		qb.must(QueryBuilders.termQuery("isKatexSpecs", false));
		// 排除正在处理的题目
		List<Long> excludeQuestions = questionKatexSpecsCacheService.getAllQuestions();
		if (CollectionUtils.isNotEmpty(excludeQuestions)) {
			for (Long qId : excludeQuestions) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}
		Page docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
				new Order("resourceId", Direction.ASC));
		if (CollectionUtils.isEmpty(docPage.getDocuments())) {
			data.put("todoCount", 0);

			// 已处理的题目数量
			long finishCount = questionKatexSpecsCacheService.getFinishedQuestionsCount();
			data.put("finishCount", finishCount);
			return new Value(data);
		}

		List<Document> documents = docPage.getDocuments();
		List<Long> questionIds = Lists.newArrayList();

		for (Document value : documents) {
			questionIds.add(Long.valueOf(value.getId()));
		}

		Map<Long, VQuestion> qusetionMap = questionConvert.to(questionManage.mget(questionIds));
		List<VQuestion> questionList = new ArrayList<VQuestion>(questionIds.size());
		for (Long id : questionIds) {
			VQuestion question = qusetionMap.get(id);
			questionList.add(question);
		}

		VPage<VQuestion> vPage = new VPage<VQuestion>();
		vPage.setCurrentPage(page);
		vPage.setPageSize(pageSize);
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(questionList);
		data.put("vPage", vPage);
		data.put("todoCount", docPage.getTotalCount());

		// 已处理的题目数量
		long finishCount = questionKatexSpecsCacheService.getFinishedQuestionsCount();
		data.put("finishCount", finishCount);

		return new Value(data);
	}

	/**
	 * 开始处理题目.
	 * 
	 * @return question
	 */
	@RequestMapping(value = "handleQuestion", method = { RequestMethod.POST, RequestMethod.GET })
	public Value handleQuestion() {
		Map<String, Object> data = new HashMap<String, Object>();

		Long questionId = questionKatexSpecsCacheService.getUserDoingCache(Security.getUserId());
		if (null != questionId) {
			VQuestion2 question2 = getQuestionByDocument(questionId);
			data.put("question", question2);
			return new Value(data);
		}

		// 查询条件
		int offset = 0;
		int size = 1;

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		qb.must(QueryBuilders.termQuery("isKatexSpecs", false));
		// 排除正在处理的题目
		List<Long> excludeQuestions = questionKatexSpecsCacheService.getAllQuestions();
		if (CollectionUtils.isNotEmpty(excludeQuestions)) {
			for (Long qId : excludeQuestions) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
		}
		Page docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
				new Order("resourceId", Direction.ASC));
		if (CollectionUtils.isEmpty(docPage.getDocuments())) {
			return new Value();
		}

		Document document = docPage.getDocuments().get(0);
		VQuestion2 question2 = getQuestionByDocument(Long.parseLong(document.getId()));

		// 记录该题目正在处理
		excludeQuestions.add(question2.getId());
		questionKatexSpecsCacheService.setDoingQuestion(Security.getUserId(), question2.getId());
		data.put("question", question2);

		return new Value(data);
	}

	/**
	 * 从document对象中获取校验过的question2对象
	 * 
	 * @param document
	 * @return question2
	 */
	private VQuestion2 getQuestionByDocument(long questionId) {
		VQuestion vquestion = questionConvert.to(questionManage.get(questionId));
		VQuestion2 question2 = new VQuestion2();
		BeanUtils.copyProperties(vquestion, question2);

		// 题干
		question2.setContentIsKatexSpecs(QuestionKatexUtils.isLatexSpecs(question2.getContent()));

		// 选项
		if (CollectionUtils.isNotEmpty(question2.getChoices())) {
			List<Boolean> choiceIsLatexSpecs = Lists.newArrayList();
			for (String choice : question2.getChoices()) {
				choiceIsLatexSpecs.add(QuestionKatexUtils.isLatexSpecs(choice));
			}

			question2.setChoiceIsKatexSpecs(choiceIsLatexSpecs);
		}

		// 提示
		question2.setHintIsKatexSpecs(QuestionKatexUtils.isLatexSpecs(question2.getHint()));
		// 解析
		question2.setAnalysisIsKatexSpecs(QuestionKatexUtils.isLatexSpecs(question2.getAnalysis()));
		// 答案
		List<Answer> answers = answerManage.getQuestionAnswers(question2.getId());
		if (CollectionUtils.isNotEmpty(answers)) {
			List<Boolean> answerIsKatexSpecs = Lists.newArrayList();
			for (Answer answer : answers) {
				if (StringUtils.isNotBlank(answer.getContent())) {
					answerIsKatexSpecs.add(QuestionKatexUtils.isLatexSpecs(answer.getContent()));
				} else {
					answerIsKatexSpecs.add(QuestionKatexUtils.isLatexSpecs(answer.getContentLatex()));
				}
			}

			question2.setAnswerIsKatexSpecs(answerIsKatexSpecs);
		}

		return question2;
	}

	/**
	 * 保存题目.
	 * 
	 * @param json
	 *            需要id,content,analysis,hint,choices,answers,latexAnswers
	 * @return
	 */
	@RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
	public Value save(String json, HttpServletRequest request, HttpServletResponse response) {
		QuestionForm questionForm = JSON.parseObject(json, QuestionForm.class);

		if (questionForm.getId() == null) {
			return new Value(new MissingArgumentException());
		}

		if (StringUtils.isNotBlank(questionForm.getAnalysis()) && questionForm.getAnalysis().length() > 65000) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.ANALYSIS_OUT_OF_LENGTH));
		}
		if (StringUtils.isNotBlank(questionForm.getHint()) && questionForm.getHint().length() > 65000) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.HINT_OUT_OF_LENGTH));
		}
		if (questionForm.getType() == Question.Type.SINGLE_CHOICE
				|| questionForm.getType() == Question.Type.MULTIPLE_CHOICE) {
			for (String choice : questionForm.getChoices()) {
				if (StringUtils.isNotBlank(choice) && choice.length() > 4000) {
					return new Value(new ResourceConsoleException(ResourceConsoleException.CHOICE_OUT_OF_LENGTH));
				}
			}
		} else if (questionForm.getType() == Question.Type.FILL_BLANK) {
			if (questionForm.getLatexAnswers() == null
					|| questionForm.getLatexAnswers().size() != questionForm.getAnswers().size()) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.ASCII_LATEX_ERROR_LENGTH));
			}
		}

		// 校验
		if (StringUtils.isNotBlank(questionForm.getContent())) {
			if (!QuestionKatexUtils.isLatexSpecs(questionForm.getContent())) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_KATEX_SPECS));
			}
		}

		if (StringUtils.isNotBlank(questionForm.getAnalysis())) {
			if (!QuestionKatexUtils.isLatexSpecs(questionForm.getAnalysis())) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_KATEX_SPECS));
			}
		}

		if (StringUtils.isNotBlank(questionForm.getHint())) {
			if (!QuestionKatexUtils.isLatexSpecs(questionForm.getHint())) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_KATEX_SPECS));
			}
		}

		if (CollectionUtils.isNotEmpty(questionForm.getChoices())) {
			StringBuilder builder = new StringBuilder();
			for (String value : questionForm.getChoices()) {
				builder.append(value);
			}
			if (!QuestionKatexUtils.isLatexSpecs(builder.toString())) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_KATEX_SPECS));
			}
		}

		if (CollectionUtils.isNotEmpty(questionForm.getAnswers())) {
			StringBuilder builder = new StringBuilder();
			for (String value : questionForm.getAnswers()) {
				builder.append(value);
			}
			if (!QuestionKatexUtils.isLatexSpecs(builder.toString())) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_KATEX_SPECS));
			}
		}

		if (CollectionUtils.isNotEmpty(questionForm.getLatexAnswers())) {
			StringBuilder builder = new StringBuilder();
			for (String value : questionForm.getLatexAnswers()) {
				builder.append(value);
			}
			if (!QuestionKatexUtils.isLatexSpecs(builder.toString())) {
				return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_KATEX_SPECS));
			}
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		// 更新
		Question question = questionManage.updateQuestionByCheckKatex(questionForm, user);
		if (question == null) {
			return new Value(new MissingArgumentException());
		}
		// 添加WordML解析缓存
		String host = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
		questionWordMLService.asyncAdd(question, answerManage.getQuestionAnswers(question.getId()), host);

		// 更新习题索引
		indexService.syncAdd(IndexType.QUESTION, question.getId());

		// 删除处理中题目缓存
		questionKatexSpecsCacheService.invalidDoingQuestion(question.getId());

		// 添加已处理题目数量缓存
		questionKatexSpecsCacheService.incrFinishedQuestionsCount();

		// 查询下一题,注意排除本题
		int offset = 0;
		int size = 1;

		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("checkStatus", CheckStatus.PASS.getValue()));
		qb.must(QueryBuilders.termQuery("isKatexSpecs", false));

		// 排除正在处理的题目
		List<Long> excludeQuestions = questionKatexSpecsCacheService.getAllQuestions();
		excludeQuestions.add(question.getId());
		if (CollectionUtils.isNotEmpty(excludeQuestions)) {
			for (Long qId : excludeQuestions) {
				qb.mustNot(QueryBuilders.termQuery("resourceId", qId));
			}
			qb.mustNot(QueryBuilders.termQuery("resourceId", questionForm.getId()));
		}
		Page docPage = searchService.search(IndexType.QUESTION, offset, size, qb, null,
				new Order("resourceId", Direction.ASC));
		if (CollectionUtils.isEmpty(docPage.getDocuments())) {
			return new Value();
		}

		Document document = docPage.getDocuments().get(0);
		VQuestion2 question2 = getQuestionByDocument(Long.parseLong(document.getId()));

		// 记录该题目正在处理
		questionKatexSpecsCacheService.setDoingQuestion(Security.getUserId(), question2.getId());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("question", question2);

		return new Value(data);
	}

}
