package com.lanking.uxb.rescon.question.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.cache.ResconAccountCacheService;
import com.lanking.uxb.rescon.auth.api.DataPermission;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingQuestionService;
import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogManage;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionManage;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionCategoryManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionSimilarBaseManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionTagManage;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconTaggingManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionCategoryConvert;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.convert.ResconQuestionTagConvert;
import com.lanking.uxb.rescon.question.form.CheckForm;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.rescon.statistics.api.VendorUserStatisManage;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.counter.api.impl.VendorUserCounterProvider;
import com.lanking.uxb.service.question.api.QuestionQRCodeService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.cache.BaseQuestionCacheService;
import com.lanking.uxb.service.question.util.QuestionKatexUtils;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

import httl.util.CollectionUtils;

@RestController
@RequestMapping("rescon/check")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_CHECK" })
public class ResconCheckController {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconAccountCacheService accountCacheService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private VendorUserStatisManage vendorUserStatisManage;
	@Autowired
	private ResconBookManage bookManage;
	@Autowired
	@Qualifier("bookCatalogManage")
	private ResconBookCatalogManage resconBookCatalogManage;
	@Autowired
	private ResconBookQuestionManage resconBookQuestionManage;
	@Autowired
	private IndexService indexService;
	@Autowired
	private ResconSchoolQuestionManage resconSchoolQuestionManage;
	@Autowired
	private VendorUserCounterProvider vendorUserCounterProvider;

	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private ResconAnswerManage answerManage;
	@Autowired
	private ResconTaggingManage resconTaggingManage;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private QuestionQRCodeService questionQRCodeService;
	@Autowired
	private ResconQuestionSimilarBaseManage questionSimilarBaseManage;
	@Autowired
	private ResconExamManage qxamManage;
	@Autowired
	private ResconSpecialTrainingQuestionService specialTrainingQuestionService;
	@Autowired
	private BaseQuestionCacheService questionCacheService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SubjectService subjectService;

	@Autowired
	private ResconQuestionTagManage questionTagManage;
	@Autowired
	private ResconQuestionTagConvert questionTagConvert;
	@Autowired
	private ResconQuestionCategoryManage questionCategoryManage;
	@Autowired
	private ResconQuestionCategoryConvert questionCategoryConvert;

	// 获取题目加锁
	private static byte[] lock = new byte[0];

	/**
	 * 2015.12.01 缓存池主要为了避免获取到他人正在校验的题目
	 * 
	 * @param currentUser
	 * @param questionIds
	 * @param permission
	 * @param checkForm
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Question getQuestion(VendorUser currentUser, List<Long> questionIds, DataPermission permission,
			CheckForm checkForm) {

		long t1 = System.currentTimeMillis();
		logger.info("[校验题目] 校验员：" + currentUser.getName());
		synchronized (lock) {
			Map cacheMap = accountCacheService.getUserCheck();
			cacheMap.remove(currentUser.getId().toString());
			for (Object questionId : cacheMap.values()) {
				if (null != questionId) {
					questionIds.add(Long.parseLong(questionId.toString()));
				}
			}
			Question question = null;

			// 首先查找一校打回的题目
			if (checkForm == null) {
				checkForm = new CheckForm();
			}
			if (permission.getCheckOne() == 1) {
				checkForm.setCheckRefund(true);
				question = questionManage.getCheckQuestions(currentUser.getId(), currentUser.getVendorId(),
						permission.getCheckOne() == 1, permission.getCheckTwo() == 1, questionIds, checkForm,
						P.offset(0, 1));
			}

			if (question == null) {
				// 正常查找题目
				checkForm.setCheckRefund(false);
				question = questionManage.getCheckQuestions(currentUser.getId(), currentUser.getVendorId(),
						permission.getCheckOne() == 1, permission.getCheckTwo() == 1, questionIds, checkForm,
						P.offset(0, 1));
			}

			if (question != null) {
				accountCacheService.setUserCheck(currentUser.getId(), question.getId());
			}

			long t2 = System.currentTimeMillis();
			logger.info("[校验题目] 校验员：" + currentUser.getName() + ", 获取题目耗时：" + (t2 - t1) / 1000);
			return question;
		}
	}

	/**
	 * 获得校验题目.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCheckQuestion", method = RequestMethod.POST)
	public Value getCheckQuestion(CheckForm checkForm) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		DataPermission permission = null;
		if (StringUtils.isNotBlank(user.getPermissions())) {
			permission = (DataPermission) JSON.parseObject(user.getPermissions(), DataPermission.class);
		} else if (user.getType() == UserType.VENDOR_ADMIN) {
			permission = new DataPermission();
			permission.setCheckOne(1);
			permission.setCheckTwo(1);
		}
		if (permission != null && (permission.getCheckOne() == 1 || permission.getCheckTwo() == 1)) {
			List<Long> questionIds = new ArrayList<Long>();

			Question question = null;
			if (checkForm.getQuestionId() != null && checkForm.getQuestionId() > 0) {
				question = questionManage.get(checkForm.getQuestionId());
			} else {
				question = this.getQuestion(user, questionIds, permission, checkForm);
			}

			if (question == null) {
				return new Value(new EntityNotFoundException());
			} else {
				VQuestion vquestion = questionConvert.to(question);

				// 获取题目在书本中的章节
				if (checkForm.getBookVersionId() != null) {
					BookQuestion bq = resconBookQuestionManage.getQuestionFromVersion(question.getId(),
							checkForm.getBookVersionId());
					if (null != bq) {
						BookVersion bookVersion = bookManage.getVersion(checkForm.getBookVersionId());
						String source = "";
						Map<Long, BookCatalog> bookCatalogMap = resconBookCatalogManage
								.mgetBookCatalog(checkForm.getBookVersionId());
						BookCatalog catalog = bookCatalogMap.get(bq.getBookCatalogId());
						if (null != catalog) {
							source = catalog.getName();
							BookCatalog catalog2 = catalog.getPid() == null ? null
									: bookCatalogMap.get(catalog.getPid());
							if (null != catalog2) {
								source = catalog2.getName() + " > " + source;

								BookCatalog catalog3 = catalog2.getPid() == null ? null
										: bookCatalogMap.get(catalog2.getPid());
								if (null != catalog3) {
									source = catalog3.getName() + " > " + source;
								}
							}
						}
						vquestion.setSourceTextbook(bookVersion.getName() + " > " + source);
					}
				} else {
					vquestion.setSourceTextbook(vquestion.getSource());
				}

				Map<String, Object> map = new HashMap<String, Object>(3);
				map.put("question", vquestion);

				// 系统标注知识点
				if (CollectionUtils.isNotEmpty(question.getAutoKnowledgePoints())) {
					map.put("autoKnowledgePoints", knowledgePointService.mgetList(question.getAutoKnowledgePoints()));
				}
				// 手工标注知识点
				if (CollectionUtils.isNotEmpty(question.getManualKnowledgePoints())) {
					map.put("manualKnowledgePoints",
							knowledgePointService.mgetList(question.getManualKnowledgePoints()));
				}

				return new Value(map);
			}
		} else {
			return new Value(new NoPermissionException());
		}
	}

	/**
	 * 校验习题.
	 * 
	 * @since 2016-12-16 校验添加知识点
	 * @since 2017-05-09 添加已通过题目的相似题组处理
	 * 
	 * @param questionId
	 * @param checkStatus
	 * @param nopassContent
	 * @param nopassImages
	 * @param difficulty
	 * @param checkKnowledgePoints
	 *            人工知识点
	 * @param allKnowledgePoints
	 *            合并知识点
	 * @param sysKnowledgePoints
	 *            系统知识点
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ApiAllowed(accessRate = 0)
	@RequestMapping(value = "checkQuestion", method = RequestMethod.POST)
	public Value checkQuestion(Long questionId, CheckStatus checkStatus, String nopassContent, Long[] nopassImages,
			Double difficulty, @RequestParam(required = false) List<Long> checkKnowledgePoints,
			@RequestParam(required = false) List<Long> checkKnowledgeSyncs,
			@RequestParam(required = false) List<Long> checkKnowledgeReviews,
			@RequestParam(required = false) List<Long> allKnowledgePoints,
			@RequestParam(required = false) List<Long> sysKnowledgePoints,
			@RequestParam(required = false) List<Long> questionTags,
			@RequestParam(required = false) List<Long> questionCategorys, HttpServletRequest request,
			HttpServletResponse response) {
		if (null == questionId || null == checkStatus) {
			return new Value(new MissingArgumentException());
		}

		Question question = questionManage.get(questionId);
		if (null == question || (checkStatus != CheckStatus.NOPASS && checkStatus != CheckStatus.ONEPASS
				&& checkStatus != CheckStatus.PASS)) {
			return new Value(new IllegalArgException());
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		DataPermission permission = null;
		if (StringUtils.isNotBlank(user.getPermissions())) {
			permission = (DataPermission) JSON.parseObject(user.getPermissions(), DataPermission.class);
		} else if (user.getType() == UserType.VENDOR_ADMIN) {
			permission = new DataPermission();
			permission.setCheckOne(1);
			permission.setCheckTwo(1);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (permission != null && (permission.getCheckOne() == 1 || permission.getCheckTwo() == 1)) {
			if (question.getStatus() == CheckStatus.ONEPASS && permission.getCheckTwo() == 0) {
				return new Value(new NoPermissionException());
			} else if (question.getStatus() == CheckStatus.EDITING && permission.getCheckOne() == 0) {
				return new Value(new NoPermissionException());
			}

			// 如果是校验通过的题目，需要对题目是否复合Katex解析规范做判断处理，临时去除限制，勿删 2017-11-14
			if (checkStatus == CheckStatus.ONEPASS || checkStatus == CheckStatus.PASS) {
				boolean contentIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(question.getContent());
				boolean analysisIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(question.getAnalysis());
				boolean hintIsLatexSpecs = QuestionKatexUtils.isLatexSpecs(question.getHint());
				boolean choiceIsLatexSpecs = QuestionKatexUtils
						.isLatexSpecs(StringUtils.defaultIfBlank(question.getChoiceA())
								+ StringUtils.defaultIfBlank(question.getChoiceB())
								+ StringUtils.defaultIfBlank(question.getChoiceC())
								+ StringUtils.defaultIfBlank(question.getChoiceD())
								+ StringUtils.defaultIfBlank(question.getChoiceE())
								+ StringUtils.defaultIfBlank(question.getChoiceF()));
				boolean answerIsLatexSpecs = true;
				if (question.getType() != Question.Type.SINGLE_CHOICE
						&& question.getType() != Question.Type.MULTIPLE_CHOICE) {
					List<Answer> answers = answerManage.getQuestionAnswers(question.getId());
					for (Answer answer : answers) {
						if (StringUtils.isNotBlank(answer.getContent())) {
							answerIsLatexSpecs = QuestionKatexUtils
									.isLatexSpecs(StringUtils.defaultIfBlank(answer.getContent()));
						} else {
							answerIsLatexSpecs = QuestionKatexUtils
									.isLatexSpecs(StringUtils.defaultIfBlank(answer.getContentLatex()));
						}
						if (!answerIsLatexSpecs) {
							break;
						}
					}
				}
				if (!(contentIsLatexSpecs & analysisIsLatexSpecs & hintIsLatexSpecs & choiceIsLatexSpecs
						& answerIsLatexSpecs)) {
					map.put("contentIsLatexSpecs", contentIsLatexSpecs);
					map.put("analysisIsLatexSpecs", analysisIsLatexSpecs);
					map.put("hintIsLatexSpecs", hintIsLatexSpecs);
					map.put("choiceIsLatexSpecs", choiceIsLatexSpecs);
					map.put("answerIsLatexSpecs", answerIsLatexSpecs);
					return new Value(map);
				}
			}

			try {
				List nopassFiles = nopassImages != null ? Lists.newArrayList(nopassImages) : Lists.newArrayList();
				questionManage.updateDoCheckStatus(questionId, checkStatus, user.getId(), nopassContent, nopassFiles);

				if (question.getStatus() == CheckStatus.EDITING
						&& question.getQuestionSource() != QuestionSource.VENDOR) {
					if (checkStatus == CheckStatus.ONEPASS) {
						// 一校通过
						vendorUserStatisManage.updateAfterStep1Pass(question.getCreateId(), user.getId(), false,
								question.getCreateAt());
					} else if (checkStatus == CheckStatus.NOPASS) {
						// 一校未通过
						vendorUserStatisManage.updateAfterStep1NoPass(question.getCreateId(), user.getId(),
								question.getCreateAt());
					}
				} else if (question.getStatus() == CheckStatus.ONEPASS
						&& question.getQuestionSource() != QuestionSource.VENDOR) {
					if (checkStatus == CheckStatus.PASS) {
						// 二校通过
						vendorUserStatisManage.updateAfterStep2Pass(question.getCreateId(), user.getId(), false,
								question.getCreateAt());
						// 只有已通过的题目进行校本题目计数
						if (question.getSchoolId() != 0) {
							resconSchoolQuestionManage.addSchoolQuestion(question.getSchoolId(), question.getId());
						}

						// 更新校验修改的知识点等
						if (difficulty != null || allKnowledgePoints != null) {
							questionManage.saveCheckQuestionDatas(questionId, difficulty, checkKnowledgePoints,
									allKnowledgePoints, sysKnowledgePoints, checkKnowledgeSyncs, checkKnowledgeReviews,
									user.getId());
						}

						// 添加WordML解析缓存
						String host = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
						questionWordMLService.asyncAdd(question, answerManage.getQuestionAnswers(questionId), host);

						// 添加二维码
						questionQRCodeService.asyncMakeQRCodeImage(question);

						// 添加相似题组的处理逻辑
						indexService.add(IndexType.QUESTION_SIMILAR, question.getId());
					} else if (checkStatus == CheckStatus.NOPASS) {
						// 二校未通过
						vendorUserStatisManage.updateAfterStep2NoPass(question.getCreateId(), user.getId(),
								question.getCreateAt());
						// 只有已通过的题目进行校本题目计数
						if (question.getSchoolId() != 0) {
							resconSchoolQuestionManage.delSchoolQuestion(question.getSchoolId(), question.getId());
						}
					}
				}

				if (question.getStatus() == CheckStatus.EDITING) {
					vendorUserCounterProvider.incrOneCheck(user.getId(), 1); // 一校计数
					if (checkStatus == CheckStatus.ONEPASS) {
						// 更新校验修改的知识点等
						if (difficulty != null || allKnowledgePoints != null) {
							questionManage.saveCheckQuestionDatas(questionId, difficulty, checkKnowledgePoints,
									allKnowledgePoints, sysKnowledgePoints, checkKnowledgeSyncs, checkKnowledgeReviews,
									user.getId());
						}
					}
				} else if (question.getStatus() == CheckStatus.ONEPASS) {
					vendorUserCounterProvider.incrTwoCheck(user.getId(), 1); // 二校计数
					if (checkStatus == CheckStatus.PASS) {
						// 更新校验修改的知识点等
						if (difficulty != null || allKnowledgePoints != null) {
							questionManage.saveCheckQuestionDatas(questionId, difficulty, checkKnowledgePoints,
									allKnowledgePoints, sysKnowledgePoints, checkKnowledgeSyncs, checkKnowledgeReviews,
									user.getId());
						}
					}
				}

				// 处理选择的题目分类及标签
				if (CollectionUtils.isNotEmpty(questionCategorys)) {
					questionManage.saveQuestionCategorys(questionId, questionCategorys);
				}
				if (CollectionUtils.isNotEmpty(questionTags)) {
					questionManage.saveQuestionTags(questionId, questionTags);
				}

				// 清空用户当前缓存的题目
				accountCacheService.invalidUserCheck(user.getId());

				// 更新题目索引
				this.updateQuestionIndex(questionId);

				// 相似题基础题库
				indexService.add(IndexType.QUESTION_SIMILAR_BASE, question.getId());
			} catch (ResourceConsoleException e) {
				return new Value(e);
			}
		} else {
			return new Value(new NoPermissionException());
		}
		return new Value(map);
	}

	/**
	 * 已通过的题目打回重新校验.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param checkStatus
	 *            状态
	 * @return
	 */
	@RequestMapping(value = "backCheckQuestion", method = RequestMethod.POST)
	public Value backCheckQuestion(Long questionId) {
		if (null == questionId) {
			return new Value(new MissingArgumentException());
		}
		Question question = questionManage.get(questionId);
		if (null == question || question.getStatus() != CheckStatus.PASS) {
			return new Value(new IllegalArgException());
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		DataPermission permission = null;
		if (StringUtils.isNotBlank(user.getPermissions())) {
			permission = (DataPermission) JSON.parseObject(user.getPermissions(), DataPermission.class);
		} else if (user.getType() == UserType.VENDOR_ADMIN) {
			permission = new DataPermission();
			permission.setSpotCheck(1);
		}

		if (permission == null || permission.getSpotCheck() != 1) {
			return new Value(new NoPermissionException());
		}

		try {
			questionManage.backCheckQuestion(questionId);

			// 统计
			if (question.getQuestionSource() != QuestionSource.VENDOR) {
				vendorUserStatisManage.updateAfterPassRecheck(question.getCreateId(), question.getCreateAt());
			}

			// 更新索引
			this.updateQuestionIndex(questionId);

			// 相似题基础题库
			indexService.add(IndexType.QUESTION_SIMILAR_BASE, question.getId());
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 释放校验缓存.
	 * 
	 * @return
	 */
	@RequestMapping(value = "clearCheckCache")
	public Value clearCheckCache() {
		try {
			accountCacheService.invalidAllUserCheck();
		} catch (Exception e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 判断是否已经对整个书本或试卷完成了校验.
	 * 
	 * @param bookId
	 *            书本ID
	 * @param paperId
	 *            试卷ID
	 * @return
	 */
	@RequestMapping(value = "checkComplete", method = RequestMethod.POST)
	public Value checkComplete(Long bookId, Long paperId, Long trainId) {
		boolean complete = false;
		if (bookId != null && bookId > 0) {
			complete = questionManage.checkBookComplete(bookId);
		} else if (paperId != null && paperId > 0) {
			complete = questionManage.checkPaperComplete(paperId);
		} else if (trainId != null && trainId > 0) {
			complete = questionManage.checkTrainComplete(trainId);
		}
		return new Value(complete);
	}

	private void updateQuestionIndex(long questionId) {
		indexService.update(IndexType.QUESTION, questionId);
	}

	/**
	 * 获取题目系统标注知识点.
	 * 
	 * @param questionId
	 *            习题ID
	 * @return
	 */
	@RequestMapping(value = "getSysKnowledgePoints")
	public Value getSysKnowledgePoints(Long questionId) {
		if (questionId == null) {
			return new Value(new MissingArgumentException());
		}
		List<Long> knowledgePointCodes = resconTaggingManage.extractKnowledges(questionId);
		if (knowledgePointCodes.size() > 0) {
			List<VKnowledgePoint> knowledgePoints = new ArrayList<VKnowledgePoint>(knowledgePointCodes.size());
			for (KnowledgePoint knowledgePoint : knowledgePointService.mgetList(knowledgePointCodes)) {
				VKnowledgePoint v = new VKnowledgePoint();
				v.setId(knowledgePoint.getCode());
				v.setName(knowledgePoint.getName());
				knowledgePoints.add(v);
			}
			return new Value(knowledgePoints);
		} else {
			return new Value(Lists.newArrayList());
		}
	}

	/**
	 * 获取相似题组.
	 * 
	 * @param questionId
	 *            校验题的ID
	 * @param size
	 *            获取相似题个数
	 * @return
	 */
	@RequestMapping(value = "getBaseSimilarQuestions", method = RequestMethod.POST)
	public Value getBaseSimilarQuestions(Long questionId, Integer size) {
		if (questionId == null) {
			return new Value(new MissingArgumentException());
		}
		Question question = questionManage.get(questionId);
		if (question == null) {
			return new Value(new EntityNotFoundException());
		}
		size = size == null ? 3 : size;
		Map<String, Object> map = new HashMap<String, Object>(1);
		List<VQuestion> similarQuestions = new ArrayList<VQuestion>();

		// 原来就是重复题，直接把展示题显示出来
		if (question.getSameShowId() != null && question.getSameShowId() > 0) {
			similarQuestions.add(questionConvert.to(questionManage.get(question.getSameShowId())));
		} else {
			VPage<Document> similarBaseDocs = questionSimilarBaseManage.queryAllQuestionBase(1, size, question);
			if (similarBaseDocs.getTotal() > 0) {
				List<Long> qids = new ArrayList<Long>(size);
				for (Document doc : similarBaseDocs.getItems()) {
					qids.add(Long.parseLong(doc.getId()));
				}

				similarQuestions = questionConvert.to(questionManage.mgetList(qids));
			}
		}

		map.put("similarQuestions", similarQuestions);
		return new Value(map);
	}

	/**
	 * 确定替换题目.
	 * 
	 * @param biz
	 *            业务场景
	 * @param bizId
	 *            业务场景对应的ID
	 * @param questionId
	 *            原习题ID
	 * @param similarQuestionId
	 *            替换使用的题目ID
	 * @return
	 */
	@RequestMapping(value = "confrimSimilarUsed", method = RequestMethod.POST)
	public Value confrimSimilarUsed(Biz biz, Long bizId, Long questionId, Long similarQuestionId) {
		if (biz == null || bizId == null || questionId == null || similarQuestionId == null) {
			return new Value(new MissingArgumentException());
		}
		// 取题
		Map<Long, Question> questions = questionManage.mget(Lists.newArrayList(questionId, similarQuestionId));
		Question question = questions.get(questionId);
		Question similarQuestion = questions.get(similarQuestionId);

		if (question == null || similarQuestion == null) {
			return new Value(new EntityNotFoundException());
		}

		Question newQuestion = null;
		if (question.getSchoolId() != similarQuestion.getSchoolId()) {
			// 来源不一致，需要复制题目
			try {
				newQuestion = questionManage.copySimilarQuestion(question, similarQuestion, Security.getUserId());
				Phase phase = phaseService.get(question.getPhaseCode());
				Subject subject = subjectService.get(question.getSubjectCode());
				this.createCode(newQuestion.getId(), phase.getAcronym() + subject.getAcronym());

				// 相似题基础题库
				indexService.add(IndexType.QUESTION_SIMILAR_BASE, newQuestion.getId());
			} catch (AbstractException e) {
				return new Value(e);
			}
		} else {
			// 来源一致，直接替换题目
			newQuestion = similarQuestion;
		}

		if (biz == Biz.BOOK) {
			// 书本替换
			resconBookQuestionManage.changeQuestion(bizId, questionId, newQuestion.getId());
		} else if (biz == Biz.EXAM) {
			// 试卷替换
			qxamManage.changeQuestion(bizId, questionId, newQuestion.getId());
		} else if (biz == Biz.SPECIAL_TRAINING) {
			// 针对性训练替换
			specialTrainingQuestionService.changeQuestion(bizId, questionId, newQuestion.getId());
		}

		// 原题删除
		try {
			questionManage.delete(questionId);

			// 相似题基础题库同时删除
			indexService.delete(IndexType.QUESTION_SIMILAR_BASE, questionId);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 题号处理.
	 * 
	 * @param questionId
	 * @param prefix
	 */
	private void createCode(long questionId, String prefix) {
		String nextCode = "";
		String maxCode = questionCacheService.getMaxCode(prefix);
		if (StringUtils.isBlank(maxCode)) {
			maxCode = questionManage.getMaxCode(prefix);
			if (StringUtils.isBlank(maxCode)) {
				maxCode = prefix + "A000000";
			}
		}
		nextCode = questionCacheService.setCode(prefix, questionCacheService.getNextCode(prefix, maxCode));
		try {
			questionManage.saveCode(questionId, nextCode);
		} catch (ResourceConsoleException e) {
			if (e.getCode() == CoreExceptionCode.ENTITY_EX) {
				maxCode = questionManage.getMaxCode(prefix);
				nextCode = questionCacheService.getNextCode(prefix, maxCode);
				nextCode = questionCacheService.setCode(prefix, nextCode);
				questionManage.saveCode(questionId, nextCode);
			}
		}
	}

	/**
	 * 一校打回功能.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param nopassContent
	 *            不通过理由
	 * @param nopassImages
	 *            不通过图片
	 * @return
	 */
	@RequestMapping(value = "checkRefund", method = RequestMethod.POST)
	public Value checkRefund(Long questionId, String nopassContent, Long[] nopassImages) {
		if (questionId == null) {
			return new Value(new MissingArgumentException());
		}
		Question question = questionManage.get(questionId);
		if (null == question || question.getStatus() != CheckStatus.ONEPASS) {
			return new Value(new IllegalArgException());
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		DataPermission permission = null;
		if (StringUtils.isNotBlank(user.getPermissions())) {
			permission = (DataPermission) JSON.parseObject(user.getPermissions(), DataPermission.class);
		} else if (user.getType() == UserType.VENDOR_ADMIN) {
			permission = new DataPermission();
			permission.setCheckTwo(1);
		}

		if (permission == null || permission.getCheckTwo() != 1
				|| user.getVendorId() != question.getVendorId().longValue()) {
			return new Value(new NoPermissionException());
		}

		try {
			questionManage.checkRefund(questionId, nopassContent, nopassImages, user.getId());

			// 更新题目索引
			this.updateQuestionIndex(questionId);

			// 清空用户当前缓存的题目
			accountCacheService.invalidUserCheck(user.getId());

			// 相似题基础题库
			indexService.add(IndexType.QUESTION_SIMILAR_BASE, question.getId());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 获取校验使用的相关数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCheckDatas", method = RequestMethod.POST)
	public Value getCheckDatas() {
		List<QuestionTag> questionTags = questionTagManage.listAll(Status.ENABLED, null);
		List<QuestionCategory> questionCategorys = questionCategoryManage.listAll(Status.ENABLED);

		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("questionTags", questionTagConvert.to(questionTags));
		map.put("questionCategorys", questionCategoryConvert.to(questionCategorys));

		return new Value(map);
	}
}
