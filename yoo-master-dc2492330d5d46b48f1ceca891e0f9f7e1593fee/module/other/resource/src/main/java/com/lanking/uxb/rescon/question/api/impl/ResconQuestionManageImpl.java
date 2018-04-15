package com.lanking.uxb.rescon.question.api.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question2Category;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.domain.common.resource.question.QuestionExaminationPoint;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.common.resource.question.QuestionMetaKnow;
import com.lanking.cloud.domain.common.resource.question.QuestionSection;
import com.lanking.cloud.domain.common.resource.question.QuestionSource;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.EntityException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeReviewService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeSyncService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionSectionService;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionTagManage;
import com.lanking.uxb.rescon.question.form.CheckForm;
import com.lanking.uxb.rescon.question.form.QuestionForm;
import com.lanking.uxb.rescon.question.value.VAnswer;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.search.api.IndexService;

@Transactional(readOnly = true)
@Service
@SuppressWarnings("unchecked")
public class ResconQuestionManageImpl implements ResconQuestionManage {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;
	@Autowired
	@Qualifier("AnswerRepo")
	Repo<Answer, Long> answerRepo;
	@Autowired
	@Qualifier("QuestionMetaKnowRepo")
	Repo<QuestionMetaKnow, Long> questionMetaKnowRepo;
	@Autowired
	@Qualifier("QuestionKnowledgeRepo")
	Repo<QuestionKnowledge, Long> questionKnowledgeRepo;
	@Autowired
	@Qualifier("QuestionExaminationPointRepo")
	Repo<QuestionExaminationPoint, Long> questionExaminationRepo;
	@Autowired
	@Qualifier("Question2CategoryRepo")
	Repo<Question2Category, Long> question2CategoryRepo;
	@Autowired
	@Qualifier("Question2TagRepo")
	Repo<Question2Tag, Long> question2TagRepo;

	@Autowired
	private MetaKnowSectionService metaKnowSectionService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ResconQuestionSectionService questionSectionService;
	@Autowired
	private ResconAnswerManage answerManage;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private ResconQuestionTagManage questionTagManage;
	@Autowired
	private IndexService indexService;

	@Autowired
	private ResconQuestionKnowledgeSyncService questionKnowledgeSyncService;
	@Autowired
	private ResconQuestionKnowledgeReviewService questionKnowledgeReviewService;

	@Override
	public Question get(long id) {
		return questionRepo.get(id);
	}

	@Override
	public Map<Long, Question> mget(Collection<Long> ids) {
		return questionRepo.mget(ids);
	}

	@Override
	public List<Question> mgetList(Collection<Long> ids) {
		return questionRepo.mgetList(ids);
	}

	@Override
	public List<Question> getSubQuestions(long id) {
		return questionRepo.find("$getSubQuestions", Params.param("parentId", id)).list();
	}

	/**
	 * 录入题目.
	 */
	@Override
	@Transactional
	public Map<String, Object> saveQuestion(QuestionForm form, VendorUser vendorUser) throws ResourceConsoleException {

		Date date = new Date();
		Map<String, Object> map = this.formToQuestion(form, date, vendorUser);
		Question question = (Question) map.get("question");
		question.setSubFlag(false);
		if (form.getKnowledgePointCodes() != null && form.getKnowledgePointCodes().size() > 0) {
			question.setKnowledgeCreateId(vendorUser.getId());
			question.setKnowledgeCreateAt(new Date());
		}
		questionRepo.save(question);

		this.saveAnswer(question, form);
		this.saveQuestionMetaKnow(question.getId(), form.getMetaCodes());
		this.saveQuestionKnowledgePoint(question.getId(), form.getKnowledgePointCodes());
		this.saveQuestionExaminationPoint(question.getId(), form.getExaminationPointCodes());
		this.saveQuestionCategorys(question.getId(), form.getQuestionCategorys());
		this.saveQuestionTags(question.getId(), form.getQuestionTags());

		// 根据知识点找到对应的章节
		Map<String, QuestionSection> questionSectionMap = new HashMap<String, QuestionSection>();
		if (null != form.getMetaCodes() && form.getMetaCodes().size() > 0 && form.getTextbookCategoryCode() != null) {
			List<Long> sectionIds = metaKnowSectionService.findByMetaknowCodes(form.getMetaCodes());
			if (sectionIds != null && sectionIds.size() > 0) {
				List<Section> sections = sectionService.mgetListByTextbookCategory(sectionIds,
						form.getTextbookCategoryCode());
				if (sectionIds != null && sectionIds.size() > 0) {
					for (Section section : sections) {
						QuestionSection questionSection = new QuestionSection();
						questionSection.setQuestionId(question.getId());
						questionSection.setSectionCode(section.getCode());
						questionSection.setTextBookCode(section.getTextbookCode());
						questionSection.setV1(true);
						questionSectionMap.put(question.getId() + "-" + section.getCode(), questionSection);
					}
				}
			}
		}

		// 根据新知识点找到对应的章节
		if (null != form.getKnowledgePointCodes() && form.getTextbookCategoryCode() != null
				&& form.getKnowledgePointCodes().size() > 0) {
			List<Section> sections = knowledgeSectionService.findSectionsByMetaknowCodes(form.getKnowledgePointCodes(),
					form.getTextbookCategoryCode());
			if (sections.size() > 0) {
				for (Section section : sections) {
					if (section.isComprehensiveSection()) {
						// 如果是本章综合与测试则跳过
						continue;
					}
					QuestionSection questionSection = questionSectionMap
							.get(question.getId() + "-" + section.getCode());
					if (questionSection == null) {
						questionSection = new QuestionSection();
						questionSection.setQuestionId(question.getId());
						questionSection.setSectionCode(section.getCode());
						questionSection.setTextBookCode(section.getTextbookCode());
					}
					questionSection.setV2(true);
					questionSectionMap.put(question.getId() + "-" + section.getCode(), questionSection);
				}
			}
		}

		if (questionSectionMap.size() > 0) {
			questionSectionService.saveQuestionSections(question.getId(), questionSectionMap.values());
		}

		// 清空知识点缓存
		questionKnowledgeService.removeKnowledgeCacheByQuestionId(question.getId());

		if (form.getChildren() != null) {
			List<Question> oldChildren = new ArrayList<Question>();
			if (form.getId() != null) {
				// 找到原子题集合
				oldChildren = questionRepo.find("$getSubQuestions", Params.param("parentId", question.getId())).list();
			}

			for (int i = 0; i < form.getChildren().size(); i++) {
				QuestionForm child = form.getChildren().get(i);
				for (int j = oldChildren.size() - 1; j >= 0; j--) {
					if (child.getId() != null && oldChildren.get(j).getId().longValue() == child.getId()) {
						oldChildren.remove(j);
						break;
					}
				}
				Question childQuestion = (Question) this.formToQuestion(child, date, vendorUser).get("question"); // 子题
				childQuestion.setParentId(question.getId());
				childQuestion.setDifficulty(question.getDifficulty());
				childQuestion.setSequence(i + 1);
				childQuestion.setSubFlag(true);
				childQuestion.setPhaseCode(question.getPhaseCode());
				childQuestion.setSubjectCode(question.getSubjectCode());
				childQuestion.setTextbookCategoryCode(question.getTextbookCategoryCode());
				childQuestion.setVendorId(vendorUser.getVendorId());
				questionRepo.save(childQuestion);
				this.saveAnswer(childQuestion, child);
				this.saveQuestionMetaKnow(childQuestion.getId(), form.getMetaCodes());
			}

			if (oldChildren.size() > 0) {
				for (Question oldChild : oldChildren) {
					oldChild.setDelStatus(Status.DELETED);
					questionRepo.save(oldChild);
				}
			}
		}
		return map;
	}

	// 处理表单数据，创建习题.
	private Map<String, Object> formToQuestion(QuestionForm form, Date date, VendorUser vendorUser) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		Question question = null;
		CheckStatus oldStatus = null;
		if (form.getId() != null) {
			question = questionRepo.get(form.getId());
			if (question == null) {
				logger.error("[formToQuestion] 习题编辑，question为null,id = " + form.getId());
			}
			oldStatus = question.getStatus();
			if (question.getNewFlag()) {
				question.setAsciiStatus(AsciiStatus.PASS);
			}
		} else {
			question = new Question();
			question.setNewFlag(true); // 新题标记
			question.setCreateAt(date);
			question.setCreateId(vendorUser.getId());
		}
		question.setAsciiStatus(AsciiStatus.PASS);
		if (form.getType() == Question.Type.FILL_BLANK) {
			question.setOpenAnswerFlag(form.getOpenAnswerFlag() == null ? false : form.getOpenAnswerFlag());
		} else {
			question.setOpenAnswerFlag(false);
		}
		question.setAnalysis(form.getAnalysis()); // 解析
		question.setAnswerNumber(form.getAnswers().size()); // 答案数量
		question.setType(form.getType()); // 题型
		question.setSource(form.getSource()); // 来源
		if (null != form.getChoices() && form.getChoices().size() > 0
				&& (form.getType() == Question.Type.SINGLE_CHOICE || form.getType() == Question.Type.MULTIPLE_CHOICE)) {
			int choiceSize = form.getChoices().size();
			question.setChoiceA(form.getChoices().get(0));
			question.setChoiceB(null);
			question.setChoiceC(null);
			question.setChoiceD(null);
			question.setChoiceE(null);
			question.setChoiceF(null);
			if (choiceSize >= 2) {
				question.setChoiceB(form.getChoices().get(1));
			}
			if (choiceSize >= 3) {
				question.setChoiceC(form.getChoices().get(2));
			}
			if (choiceSize >= 4) {
				question.setChoiceD(form.getChoices().get(3));
			}
			if (choiceSize >= 5) {
				question.setChoiceE(form.getChoices().get(4));
			}
			if (choiceSize >= 6) {
				question.setChoiceF(form.getChoices().get(5));
			}
		}
		question.setContent(form.getContent());
		question.setDifficulty(form.getDifficulty());
		question.setHint(form.getHint());
		question.setPhaseCode(form.getPhaseCode());

		if (form.getStatus() != null) {
			question.setStatus(form.getStatus());
		} else {
			if (question.getStatus() == CheckStatus.DRAFT) {
				// 草稿保存变为正式题目
				question.setStatus(CheckStatus.EDITING);
			} else if (question.getStatus() == CheckStatus.EDITING && form.getCheckFlag() == 1) {
				// 一校校验修改 2016-12-19 校验修改不改变题目状态
				// question.setStatus(CheckStatus.ONEPASS);
				// question.setVerifyId(vendorUser.getId());
				// question.setVerifyAt(date);
				// question.setNopassContent(null);
				// question.setNopassFiles(null);
			} else if (question.getStatus() == CheckStatus.ONEPASS && form.getCheckFlag() == 1) {
				// 二校校验修改 2016-12-19 校验修改不改变题目状态
				// question.setStatus(CheckStatus.PASS);
				// question.setVerify2Id(vendorUser.getId());
				// question.setVerify2At(date);
				// question.setNopassContent(null);
				// question.setNopassFiles(null);
			} else if (question.getStatus() == CheckStatus.PASS) {
				// 已通过的题目被抽查， 不改变题目状态
			} else {
				if (form.getId() != null && question.getCreateId() != vendorUser.getId().longValue()
						&& vendorUser.getType() == UserType.VENDOR_ADMIN) {
					// 管理员编辑其他人录入的题目，不影响题目状态
				} else if (form.getErrorFlag() != null && form.getErrorFlag() == 1) {
					// 纠错题修改，不影响题目状态
				} else {
					question.setStatus(CheckStatus.EDITING); // 新增或编辑重新变成未校验状态
				}
			}
		}

		question.setSubjectCode(form.getSubjectCode());
		question.setTextbookCategoryCode(form.getTextbookCategoryCode());
		question.setTextbookCode(form.getTextbookCode());
		question.setSectionCode(form.getSectionCode());
		question.setTypeCode(form.getTypeCode());
		question.setUpdateAt(date);
		question.setUpdateId(vendorUser.getId());
		question.setVendorId(form.getVendorId());
		if (null != form.getChoiceFormat()) {
			question.setChoiceFormat(form.getChoiceFormat());
		}
		question.setQuestionSource(QuestionSource.RESCON);
		// question.setSceneCode(form.getQuestionSceneCode());

		map.put("question", question);
		map.put("oldStatus", oldStatus);
		return map;
	}

	// 保存答案
	private void saveAnswer(Question question, QuestionForm form) throws ResourceConsoleException {
		List<String> answers = form.getAnswers();
		List<String> latexAnswers = form.getLatexAnswers();
		if (answers == null || answers.size() == 0) {
			return;
		}

		long questionId = question.getId();
		if (form.getType() == Question.Type.FILL_BLANK && latexAnswers != null && latexAnswers.size() > 0) {
			// 找出旧答案
			List<Answer> oldAnswers = answerManage.getQuestionAnswers(questionId);

			for (int i = 0; i < answers.size(); i++) {
				Answer answer = new Answer();
				if (oldAnswers.size() > 0) {
					answer = oldAnswers.get(0);
					oldAnswers.remove(0);
				}

				answer.setQuestionId(questionId);
				answer.setSequence(i + 1);
				answer.setContentAscii(answers.get(i));
				String ltx = StringUtils.isBlank(latexAnswers.get(i)) ? answers.get(i) : latexAnswers.get(i);
				answer.setContentLatex(ltx);
				answer.setContent(ltx);
				answerRepo.save(answer);
			}

			// 剩余的旧答案需要删除
			if (oldAnswers.size() > 0) {
				answerRepo.delete(oldAnswers);
			}
		} else {
			answerRepo.execute("$deleteByQuestion", Params.param("questionId", questionId));
			for (int i = 0; i < answers.size(); i++) {
				Answer answer = new Answer();
				answer.setQuestionId(questionId);
				answer.setSequence(i + 1);
				answer.setContent(answers.get(i));
				answerRepo.save(answer);
			}
		}
	}

	// 保存知识点
	private void saveQuestionMetaKnow(Long questionId, List<Integer> metaKnows) throws ResourceConsoleException {
		if (metaKnows == null || metaKnows.size() == 0) {
			return;
		}
		questionMetaKnowRepo.execute("delete from question_metaknow where question_id=:questionId",
				Params.param("questionId", questionId));
		for (int i = 0; i < metaKnows.size(); i++) {
			QuestionMetaKnow questionMetaKnow = new QuestionMetaKnow();
			questionMetaKnow.setQuestionId(questionId);
			questionMetaKnow.setMetaCode(metaKnows.get(i));
			questionMetaKnowRepo.save(questionMetaKnow);
		}
	}

	// 保存新知识点
	@Override
	@Transactional
	public void saveQuestionKnowledgePoint(Long questionId, List<Long> knowledgePoints)
			throws ResourceConsoleException {
		questionKnowledgeRepo.execute("delete from question_knowledge where question_id=:questionId",
				Params.param("questionId", questionId));
		if (knowledgePoints == null || knowledgePoints.size() == 0) {
			return;
		}
		for (int i = 0; i < knowledgePoints.size(); i++) {
			QuestionKnowledge questionKnowledge = new QuestionKnowledge();
			questionKnowledge.setQuestionId(questionId);
			questionKnowledge.setKnowledgeCode(knowledgePoints.get(i));
			questionKnowledgeRepo.save(questionKnowledge);
		}
	}

	// 保存考点
	@Override
	@Transactional
	public void saveQuestionExaminationPoint(Long questionId, List<Long> examinationPoints)
			throws ResourceConsoleException {
		questionExaminationRepo.execute("delete from question_examination_point where question_id=:questionId",
				Params.param("questionId", questionId));
		if (examinationPoints == null || examinationPoints.size() == 0) {
			return;
		}
		for (int i = 0; i < examinationPoints.size(); i++) {
			QuestionExaminationPoint questionExaminationPoint = new QuestionExaminationPoint();
			questionExaminationPoint.setQuestionId(questionId);
			questionExaminationPoint.setExaminationPointCode(examinationPoints.get(i));
			questionExaminationRepo.save(questionExaminationPoint);
		}
	}

	/**
	 * 保存习题分类.
	 * 
	 * @param questionId
	 * @param questionCategorys
	 * @throws ResourceConsoleException
	 */
	@Override
	@Transactional
	public void saveQuestionCategorys(Long questionId, List<Long> questionCategorys) throws ResourceConsoleException {
		question2CategoryRepo.execute("$deleteByQuestion", Params.param("questionId", questionId));
		if (CollectionUtils.isNotEmpty(questionCategorys)) {
			for (Long categoryCode : questionCategorys) {
				Question2Category question2Category = new Question2Category();
				question2Category.setCategoryCode(categoryCode);
				question2Category.setQuestionId(questionId);
				question2CategoryRepo.save(question2Category);
			}
		}
	}

	/**
	 * 保存习题标签.
	 * 
	 * @param questionId
	 * @param questionTags
	 * @throws ResourceConsoleException
	 */
	@Override
	@Transactional
	public void saveQuestionTags(Long questionId, List<Long> questionTags) throws ResourceConsoleException {
		question2TagRepo.execute("$deleteManualByQuestion", Params.param("questionId", questionId));
		if (CollectionUtils.isNotEmpty(questionTags)) {
			List<Question2Tag> question2Tags = question2TagRepo
					.find("$listByQuestion", Params.param("questionId", questionId)).list();
			Map<Long, Question2Tag> question2TagMap = new HashMap<Long, Question2Tag>(question2Tags.size());
			for (Question2Tag q2t : question2Tags) {
				question2TagMap.put(q2t.getTagCode(), q2t);
			}

			Map<Long, QuestionTag> systemTagMap = questionTagManage.mget(questionTags);
			for (Long tagCode : questionTags) {
				QuestionTag tag = systemTagMap.get(tagCode.longValue());
				Question2Tag q2Tag = question2TagMap.get(tagCode.longValue());

				// 系统标签
				if (tag != null && tag.getType() == QuestionTagType.MANUAL && q2Tag == null) {
					Question2Tag question2Tag = new Question2Tag();
					question2Tag.setQuestionId(questionId);
					question2Tag.setSystem(false);
					question2Tag.setTagCode(tagCode);
					question2TagRepo.save(question2Tag);
				}
			}
		}
	}

	@Override
	@Transactional
	public void delete(long questionId) throws ResourceConsoleException {
		Question question = questionRepo.get(questionId);
		question.setDelStatus(Status.DELETED);
		questionRepo.save(question);
	}

	@Override
	@Transactional
	public void saveCode(long questionId, String code) throws ResourceConsoleException {
		Question question = questionRepo.get(questionId);
		if (question == null) {
			logger.error("[saveCode] 习题保存CODE，question为null,id = " + questionId);
		}
		question.setCode(code);
		try {
			questionRepo.save(question);
		} catch (EntityException e) {
			throw new ResourceConsoleException(e.getCode());
		}
	}

	@Override
	@Transactional
	public void saveCode(Map<Long, String> codeMap) throws ResourceConsoleException {
		List<Question> questions = questionRepo.mgetList(codeMap.keySet());
		for (Question question : questions) {
			question.setCode(codeMap.get(question.getId().longValue()));
		}
		questionRepo.save(questions);
	}

	@Override
	public String getMaxCode(String prefix) {
		Page<String> codes = questionRepo.find("$getMaxCode", Params.param("prefix", prefix + "%")).fetch(P.first(),
				String.class);
		if (codes.getItemSize() > 0) {
			return codes.getItem(0);
		} else {
			return null;
		}
	}

	@Override
	public Page<Question> queryNoCodeQuestions(Pageable pageable) {
		Params params = Params.param();
		return questionRepo.find("$queryNoCodeQuestions", params).fetch(pageable);
	}

	@Override
	public List<Map> getCountByCheckStaus(Long vendorId) {
		return questionRepo.find("$getCountByCheckStaus", Params.param("vendorId", vendorId)).list(Map.class);
	}

	/**
	 * 获取校验题目.
	 * 
	 * @param vendorUserId
	 *            校验员ID
	 * @param vendorId
	 *            供应商ID
	 * @param hasCheckOne
	 *            是否有一校权限
	 * @param hasCheckTwo
	 *            是否有二校权限
	 * @param questionIds
	 *            需要排除的题目
	 * @param checkForm
	 *            校验取题的范围
	 * @return
	 */
	@Override
	public Question getCheckQuestions(Long vendorUserId, Long vendorId, boolean hasCheckOne, boolean hasCheckTwo,
			Collection<Long> questionIds, CheckForm checkForm, Pageable pageable) {
		Params params = Params.param("vendorUserId", vendorUserId).put("vendorId", vendorId)
				.put("hasCheckOne", hasCheckOne).put("hasCheckTwo", hasCheckTwo);
		if (null != questionIds && questionIds.size() > 0) {
			params.put("questionIds", questionIds);
		}
		if (null != checkForm) {
			if (checkForm.getPhaseCode() != null) {
				params.put("phaseCode", checkForm.getPhaseCode());
			}
			if (checkForm.getSubjectCode() != null) {
				params.put("subjectCode", checkForm.getSubjectCode());
			}
			if (checkForm.getBookVersionId() != null) {
				params.put("bookVersionId", checkForm.getBookVersionId());
			}
			if (checkForm.getPaperId() != null) {
				params.put("paperId", checkForm.getPaperId());
			}
			if (checkForm.getTrainId() != null) {
				params.put("trainId", checkForm.getTrainId());
			}
			if (checkForm.getQuestionType() != null) {
				params.put("questionType", checkForm.getQuestionType().getValue());
			}
			if (checkForm.getCreateBt() != null) {
				params.put("createBt", new Date(checkForm.getCreateBt()));
			}
			if (checkForm.getCreateEt() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(checkForm.getCreateEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				params.put("createEt", cal.getTime());
			}
			if (checkForm.getVendorUserId() != null) {
				params.put("vuser", checkForm.getVendorUserId());
			}
			if (checkForm.getKnowpoints() != null && checkForm.getKnowpoints().size() > 0) {
				params.put("knowpoints", checkForm.getKnowpoints());
			}
			params.put("checkRefund", checkForm.isCheckRefund());
		}

		Page<Long> idPage = questionRepo.find("$getCheckQuestions", params).fetch(pageable, Long.class);
		if (idPage.getItemSize() > 0) {
			return questionRepo.get(idPage.getFirst());
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public void updateDoCheckStatus(long questionId, CheckStatus status, Long vendorUserId, String nopassContent,
			List<Long> nopassImages) {
		Question question = questionRepo.get(questionId);
		if (null != question) {
			Date date = new Date();
			if (status == CheckStatus.ONEPASS) {
				question.setVerifyId(vendorUserId);
				question.setVerifyAt(date);
				question.setNopassContent(null);
				question.setNopassFiles(null);
			} else if (status == CheckStatus.PASS) {
				question.setVerify2Id(vendorUserId);
				question.setVerify2At(date);
				question.setNopassContent(null);
				question.setNopassFiles(null);
			} else if (status == CheckStatus.NOPASS) {
				if (question.getStatus() == CheckStatus.EDITING) {
					question.setVerifyId(vendorUserId);
					question.setVerifyAt(date);
				} else if (question.getStatus() == CheckStatus.ONEPASS) {
					question.setVerify2Id(vendorUserId);
					question.setVerify2At(date);
				}
				if (StringUtils.isNotBlank(nopassContent) || (null != nopassImages && nopassImages.size() > 0)) {
					question.setNopassContent(nopassContent);
					if (null != nopassImages && nopassImages.size() > 0) {
						String nfs = "";
						for (Long fileId : nopassImages) {
							nfs += fileId + ";";
						}
						nfs = nfs.substring(0, nfs.length() - 1);
						question.setNopassFiles(nfs);
					} else {
						question.setNopassFiles(null);
					}
				}
			}
			question.setCheckRefund(false);
			question.setStatus(status);
			questionRepo.save(question);
		}
	}

	@Override
	@Transactional
	public void backCheckQuestion(long questionId) {
		Question question = questionRepo.get(questionId);
		if (null != question) {
			question.setStatus(CheckStatus.EDITING);
			questionRepo.save(question);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids) {
		Map<Long, List<Question>> map = new HashMap<Long, List<Question>>(ids.size());
		for (Long id : ids) {
			map.put(id, new ArrayList<Question>());
		}
		List<Question> list = questionRepo.find("$getSubQuestions", Params.param("parentIds", ids)).list();
		for (Question question : list) {
			map.get(question.getParentId()).add(question);
		}
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Question> findQuestionByCode(List<String> qCodes, Integer status) {
		Params params = Params.param();
		if (status != null) {
			params.put("status", status);
		}
		params.put("codes", qCodes);
		return questionRepo.find("$resconFindQuestionByCode", params).list();
	}

	@Transactional(readOnly = true)
	@Override
	public boolean checkBookComplete(Long bookId) {
		long count = questionRepo.find("$checkBookComplete", Params.param("bookId", bookId)).count();
		return count == 0;
	}

	@Transactional(readOnly = true)
	@Override
	public boolean checkPaperComplete(Long paperId) {
		long count = questionRepo.find("$checkPaperComplete", Params.param("paperId", paperId)).count();
		return count == 0;
	}

	@Override
	public boolean checkTrainComplete(Long trainId) {
		long count = questionRepo.find("$checkTrainComplete", Params.param("trainId", trainId)).count();
		return count == 0;
	}

	@Transactional
	@Override
	public void saveQuestionSchool(long questionId, long schoolId) {
		Question question = questionRepo.get(questionId);
		question.setSchoolId(schoolId);
		questionRepo.save(question);
	}

	@Override
	@Transactional
	public void updateQuestionSchoolCount(long schoolId, int num) {
		List<Long> countList = questionRepo.find("select school_id from question_school where school_id=:schoolId",
				Params.param("schoolId", schoolId)).list(Long.class);
		if (countList.size() == 0) {
			questionRepo.execute(
					"insert into question_school(school_id,create_at,question_count,status,teacher_count) values(:schoolId,:createAt,:qcount,0,0)",
					Params.param("schoolId", schoolId).put("createAt", new Date()).put("qcount", num));
		} else {
			questionRepo.execute(
					"update question_school set question_count=question_count+:count where school_id=:schoolId",
					Params.param("count", num).put("schoolId", schoolId));
		}
	}

	@Override
	public Question getConvertQuestion(AsciiStatus status, List<Long> questionIds) {
		Params p = Params.param();
		p.put("asciiStatus", status.getValue());
		if (CollectionUtils.isNotEmpty(questionIds)) {
			p.put("questionIds", questionIds);
		}
		return questionRepo.find("$getConvertQuestion", p).get();
	}

	@Override
	public Map<String, Long> getConvertQuestionCount() {
		Map<String, Long> map = new HashMap<String, Long>(3);
		Map<String, BigInteger> noSubCount = questionRepo.find("$getConvertQuestionCount").get(Map.class);
		Map<String, BigInteger> subCount = questionRepo.find("$getConvertQuestionCountFromSub").get(Map.class);

		long nochangecount1 = noSubCount.get("nochangecount").longValue();
		long nocheckcount1 = noSubCount.get("nocheckcount").longValue();
		long nochangepasscount1 = noSubCount.get("nochangepasscount").longValue();
		long nochangecount2 = subCount.get("nochangecount").longValue();
		long nocheckcount2 = subCount.get("nocheckcount").longValue();
		long nochangepasscount2 = subCount.get("nochangepasscount").longValue();
		map.put("nochangecount", nochangecount1 + nochangecount2);
		map.put("nocheckcount", nocheckcount1 + nocheckcount2);
		map.put("nochangepasscount", nochangepasscount1 + nochangepasscount2);
		return map;
	}

	@Override
	@Transactional
	public void checkAnswer(long questionId, List<VAnswer> answers, boolean checkFlag) {
		Question question = questionRepo.get(questionId);
		if (checkFlag) {
			question.setAsciiStatus(AsciiStatus.PASS);
			questionRepo.save(question);
		} else {
			question.setAsciiStatus(AsciiStatus.NOCHECK);
			questionRepo.save(question);
		}

		if (answers != null && answers.size() > 0) {
			List<Answer> oldAnswers = answerManage.getQuestionAnswers(questionId);
			for (int i = 0; i < oldAnswers.size(); i++) {
				Answer oldAnswer = oldAnswers.get(i);
				VAnswer newAnswer = answers.get(i);
				oldAnswer.setContentAscii(newAnswer.getContentAscii());
				oldAnswer.setContentLatex(newAnswer.getContentLatex());
				// 2016-10-25 补
				oldAnswer.setContent(newAnswer.getContentLatex());
			}
			answerRepo.save(oldAnswers);
		}
	}

	@Override
	@Transactional
	public void updateKnowledgeCreator(Long questionId, Long vendorUserId) throws ResourceConsoleException {
		Question question = questionRepo.get(questionId);
		question.setKnowledgeCreateId(vendorUserId);
		question.setKnowledgeCreateAt(new Date());
		questionRepo.save(question);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Integer> calNewKnowledgeDatas(Long vendorId, Long vendorUserId, int phaseCode) {
		Map<String, Integer> map = new HashMap<String, Integer>(3);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");

		List<Integer> typeCodes = null;
		if (phaseCode == 2) {
			typeCodes = Lists.newArrayList(20201, 20202, 20203, 20204, 20205);
		} else if (phaseCode == 3) {
			typeCodes = Lists.newArrayList(30201, 30202, 30203, 30204, 30205);
		}

		List<Map> list = questionRepo.find("$calNewKnowledgeDatas", Params.param("userId", vendorUserId)
				.put("vendorId", vendorId).put("day", format.format(cal.getTime())).put("typeCodes", typeCodes))
				.list(Map.class);
		map.put("mAll", Integer.parseInt(list.get(0).get("c").toString())); // 个人修正数
		map.put("mYAll", Integer.parseInt(list.get(1).get("c").toString())); // 个人昨日修正数
		map.put("aYAll", Integer.parseInt(list.get(2).get("c").toString())); // 全部昨日修正数
		map.put("oldAll", Integer.parseInt(list.get(3).get("c").toString())); // 原知识点待修正数
		map.put("newAll", Integer.parseInt(list.get(4).get("c").toString())); // 新知识点已修正数
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> calNoKnowledgeL1(int subjectCode, long vendorId) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		List<Integer> typeCodes = null;
		if (subjectCode == 202) {
			typeCodes = Lists.newArrayList(20201, 20202, 20203, 20204, 20205);
		} else if (subjectCode == 302) {
			typeCodes = Lists.newArrayList(30201, 30202, 30203, 30204, 30205);
		}

		List<Map> list = questionRepo
				.find("$calNoKnowledgeL1",
						Params.param("subjectCode", subjectCode).put("vendorId", vendorId).put("typeCodes", typeCodes))
				.list(Map.class);
		for (Map m : list) {
			map.put(Integer.parseInt(m.get("code").toString()), Integer.parseInt(m.get("counts").toString()));
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Integer> calHasKnowledgeL1(int subjectCode, long vendorId) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		List<Integer> typeCodes = null;
		if (subjectCode == 202) {
			typeCodes = Lists.newArrayList(20201, 20202, 20203, 20204, 20205);
		} else if (subjectCode == 302) {
			typeCodes = Lists.newArrayList(30201, 30202, 30203, 30204, 30205);
		}

		List<Map> list = questionRepo
				.find("$calHasKnowledgeL1",
						Params.param("subjectCode", subjectCode).put("vendorId", vendorId).put("typeCodes", typeCodes))
				.list(Map.class);
		for (Map m : list) {
			map.put(Integer.parseInt(m.get("code").toString()), Integer.parseInt(m.get("counts").toString()));
		}
		return map;
	}

	@Override
	@Transactional
	public void saveCheckQuestionDatas(long questionId, Double difficulty, List<Long> checkKnowledgePoints,
			List<Long> allKnowledgePoints, List<Long> sysKnowledgePoints, List<Long> checkKnowledgeSyncs,
			List<Long> checkKnowledgeReviews, long vendorUserId) {
		Question question = questionRepo.get(questionId);
		if (difficulty != null) {
			question.setDifficulty(difficulty);
		}
		if (CollectionUtils.isNotEmpty(allKnowledgePoints)) {
			question.setKnowledgeCreateAt(new Date());
			question.setKnowledgeCreateId(vendorUserId);

			List<QuestionKnowledge> questionKnowledges = new ArrayList<QuestionKnowledge>(allKnowledgePoints.size());
			for (int i = 0; i < allKnowledgePoints.size(); i++) {
				QuestionKnowledge questionKnowledge = new QuestionKnowledge();
				questionKnowledge.setQuestionId(questionId);
				questionKnowledge.setKnowledgeCode(allKnowledgePoints.get(i));
				questionKnowledges.add(questionKnowledge);
			}
			questionKnowledgeRepo.execute("delete from question_knowledge where question_id = :questionId",
					Params.param("questionId", questionId));
			questionKnowledgeRepo.save(questionKnowledges);

			// 根据新知识点找到对应的章节
			Map<String, QuestionSection> questionSectionMap = new HashMap<String, QuestionSection>();
			List<Section> sections = knowledgeSectionService.findSectionsByMetaknowCodes(allKnowledgePoints,
					question.getTextbookCategoryCode());
			if (sections.size() > 0) {
				for (Section section : sections) {
					QuestionSection questionSection = questionSectionMap
							.get(question.getId() + "-" + section.getCode());
					if (questionSection == null) {
						questionSection = new QuestionSection();
						questionSection.setQuestionId(question.getId());
						questionSection.setSectionCode(section.getCode());
						questionSection.setTextBookCode(section.getTextbookCode());
					}
					questionSection.setV2(true);
					questionSectionMap.put(question.getId() + "-" + section.getCode(), questionSection);
				}
			}

			if (questionSectionMap.size() > 0) {
				questionSectionService.saveQuestionSections(question.getId(), questionSectionMap.values());
			}
		}

		// 保存同步知识点
		if (CollectionUtils.isNotEmpty(checkKnowledgeSyncs)) {
			questionKnowledgeSyncService.saveQuestionKnowledgeSync(questionId, checkKnowledgeSyncs);

			// 根据同步知识点找到对应的章节v3（由于v3、v2通用knowledgeSection，需表数据完全转为v3后才正确）
			// Map<String, QuestionSection> questionSectionMap = new
			// HashMap<String, QuestionSection>();
			// List<Section> sections =
			// knowledgeSectionService.findSectionsByMetaknowCodes(allKnowledgePoints,
			// question.getTextbookCategoryCode());
		}

		// 保存复习知识点
		if (CollectionUtils.isNotEmpty(checkKnowledgeReviews)) {
			questionKnowledgeReviewService.saveQuestionKnowledgeReview(questionId, checkKnowledgeReviews);
		}

		question.setManualKnowledgePoints(checkKnowledgePoints);
		question.setAutoKnowledgePoints(sysKnowledgePoints);
		questionRepo.save(question);

		// 清空知识点缓存
		questionKnowledgeService.removeKnowledgeCacheByQuestionId(question.getId());
	}

	@Override
	@Transactional
	public Question copySimilarQuestion(Question question, Question similarQuestion, long operator) {
		List<Answer> answers = answerManage.getQuestionAnswers(similarQuestion.getId());

		Date date = new Date();
		Question newQuestion = new Question();
		newQuestion.setAnalysis(similarQuestion.getAnalysis());
		newQuestion.setAnswerNumber(answers.size());
		newQuestion.setAsciiStatus(similarQuestion.getAsciiStatus());
		newQuestion.setAutoKnowledgePoints(similarQuestion.getAutoKnowledgePoints());
		newQuestion.setCategoryTypes(similarQuestion.getCategoryTypes());
		newQuestion.setChoiceA(similarQuestion.getChoiceA());
		newQuestion.setChoiceB(similarQuestion.getChoiceB());
		newQuestion.setChoiceC(similarQuestion.getChoiceC());
		newQuestion.setChoiceD(similarQuestion.getChoiceD());
		newQuestion.setChoiceE(similarQuestion.getChoiceE());
		newQuestion.setChoiceF(similarQuestion.getChoiceF());
		newQuestion.setChoiceFormat(similarQuestion.getChoiceFormat());
		newQuestion.setContent(similarQuestion.getContent());
		newQuestion.setCreateAt(question.getCreateAt()); // question
		newQuestion.setCreateId(question.getCreateId()); // question
		newQuestion.setDelStatus(similarQuestion.getDelStatus());
		newQuestion.setDifficulty(similarQuestion.getDifficulty());
		newQuestion.setDraft(similarQuestion.getDraft());
		newQuestion.setHasSimilar(question.isHasSimilar()); // question
		newQuestion.setHint(similarQuestion.getHint());
		newQuestion.setKnowledgeCreateAt(question.getKnowledgeCreateAt()); // question
		newQuestion.setKnowledgeCreateId(question.getKnowledgeCreateId()); // question
		newQuestion.setManualKnowledgePoints(similarQuestion.getManualKnowledgePoints());
		newQuestion.setNewFlag(question.getNewFlag());
		newQuestion.setNopassContent(null);
		newQuestion.setOpenAnswerFlag(similarQuestion.getOpenAnswerFlag());
		newQuestion.setPhaseCode(similarQuestion.getPhaseCode());
		newQuestion.setQuestionSource(similarQuestion.getQuestionSource());
		newQuestion.setSameShow(null);
		newQuestion.setSameShowId(null);
		newQuestion.setSceneCode(similarQuestion.getSceneCode());
		newQuestion.setSchoolId(question.getSchoolId()); // question 使用本题的校本范围
		newQuestion.setSectionCode(null);
		newQuestion.setSequence(null);
		newQuestion.setSource(null);
		newQuestion.setStatus(similarQuestion.getStatus());
		newQuestion.setStep(similarQuestion.getStep());
		newQuestion.setStudentHomeworkId(null);
		newQuestion.setStudentQuestionId(null);
		newQuestion.setSubFlag(similarQuestion.isSubFlag());
		newQuestion.setSubjectCode(similarQuestion.getSubjectCode());
		newQuestion.setTextbookCategoryCode(null);
		newQuestion.setTextbookCode(null);
		newQuestion.setType(similarQuestion.getType());
		newQuestion.setTypeCode(similarQuestion.getTypeCode());
		newQuestion.setUpdateAt(date);
		newQuestion.setUpdateId(operator); // operator
		newQuestion.setVendorId(question.getVendorId());
		newQuestion.setVerifyAt(date); // question
		newQuestion.setVerifyId(operator); // operator
		newQuestion.setVerify2Id(question.getVerify2Id()); // question
		newQuestion.setVerify2At(question.getVerify2At()); // question

		questionRepo.save(newQuestion);

		// 拷贝答案
		for (Answer answer : answers) {
			Answer newAnswer = new Answer();
			newAnswer.setContent(answer.getContent());
			newAnswer.setContentAscii(answer.getContentAscii());
			newAnswer.setContentLatex(answer.getContentLatex());
			newAnswer.setQuestionId(newQuestion.getId());
			newAnswer.setSequence(answer.getSequence());
			answerRepo.save(newAnswer);
		}

		return newQuestion;
	}

	@Override
	@Transactional
	public void checkRefund(Long questionId, String nopassContent, Long[] nopassImages, long vendorUserId) {
		Question question = questionRepo.get(questionId);
		Date date = new Date();
		question.setStatus(CheckStatus.EDITING);
		question.setNopassContent(nopassContent);
		String nfs = "";
		if (null != nopassImages) {
			for (Long fileId : nopassImages) {
				nfs += fileId + ";";
			}
			nfs = nfs.substring(0, nfs.length() - 1);
		}
		question.setNopassFiles(nfs);
		question.setCheckRefund(true);
		question.setVerify2At(date);
		question.setVerify2Id(vendorUserId);
		questionRepo.save(question);
	}

	@Override
	public Page<Question> queryReQuestionSection(Pageable pageable) {
		return questionRepo.find("$queryReQuestionSection").fetch(pageable);
	}

	@Override
	@Transactional
	public void saveReQuestionSection(List<Question> questions) {
		List<Long> questionIds = new ArrayList<Long>(questions.size());
		for (Question question : questions) {
			questionIds.add(question.getId());
		}
		Map<Long, List<Long>> knowlegdeMap = questionKnowledgeService.mgetByQuestions(questionIds);

		for (Question question : questions) {
			List<Long> allKnowledgePoints = knowlegdeMap.get(question.getId());
			if (CollectionUtils.isEmpty(allKnowledgePoints)) {
				continue;
			}

			// 根据新知识点找到对应的章节
			Map<String, QuestionSection> questionSectionMap = new HashMap<String, QuestionSection>();
			List<Section> sections = knowledgeSectionService.findSectionsByMetaknowCodes(allKnowledgePoints,
					question.getTextbookCategoryCode());
			if (sections.size() > 0) {
				for (Section section : sections) {
					QuestionSection questionSection = questionSectionMap
							.get(question.getId() + "-" + section.getCode());
					if (questionSection == null) {
						questionSection = new QuestionSection();
						questionSection.setQuestionId(question.getId());
						questionSection.setSectionCode(section.getCode());
						questionSection.setTextBookCode(section.getTextbookCode());
					}
					questionSection.setV2(true);
					questionSectionMap.put(question.getId() + "-" + section.getCode(), questionSection);
				}
			}

			if (questionSectionMap.size() > 0) {
				questionSectionService.saveQuestionSections(question.getId(), questionSectionMap.values());
				indexService.syncAdd(IndexType.QUESTION, question.getId());
			}
		}
	}

	@Override
	@Transactional
	public Question updateQuestionByCheckKatex(QuestionForm form, VendorUser vendorUser) {
		Question question = questionRepo.get(form.getId());
		if (question == null) {
			return null;
		}

		Date date = new Date();

		// 选项
		if (null != form.getChoices() && form.getChoices().size() > 0
				&& (form.getType() == Question.Type.SINGLE_CHOICE || form.getType() == Question.Type.MULTIPLE_CHOICE)) {
			int choiceSize = form.getChoices().size();
			question.setChoiceA(form.getChoices().get(0));
			question.setChoiceB(null);
			question.setChoiceC(null);
			question.setChoiceD(null);
			question.setChoiceE(null);
			question.setChoiceF(null);
			if (choiceSize >= 2) {
				question.setChoiceB(form.getChoices().get(1));
			}
			if (choiceSize >= 3) {
				question.setChoiceC(form.getChoices().get(2));
			}
			if (choiceSize >= 4) {
				question.setChoiceD(form.getChoices().get(3));
			}
			if (choiceSize >= 5) {
				question.setChoiceE(form.getChoices().get(4));
			}
			if (choiceSize >= 6) {
				question.setChoiceF(form.getChoices().get(5));
			}
		}
		if (StringUtils.isNotBlank(form.getContent())) {
			question.setContent(form.getContent());
		}
		if (StringUtils.isNotBlank(form.getAnalysis())) {
			question.setAnalysis(form.getAnalysis());
		}
		if (StringUtils.isNotBlank(form.getHint())) {
			question.setHint(form.getHint());
		}
		question.setUpdateAt(date);
		if (vendorUser != null) {
			question.setUpdateId(vendorUser.getId());
		}
		Question newQuestion = questionRepo.save(question);

		// 更新答案
		if (!CollectionUtils.isEmpty(form.getAnswers())) {
			boolean isAnswerEmpty = false;
			for (String answer : form.getAnswers()) {
				if (answer == null || StringUtils.isEmpty(answer.trim())) {
					isAnswerEmpty = true;
					break;
				}
			}
			if (!isAnswerEmpty) {
				saveAnswer(question, form);
			}
		}
		return newQuestion;
	}
}
