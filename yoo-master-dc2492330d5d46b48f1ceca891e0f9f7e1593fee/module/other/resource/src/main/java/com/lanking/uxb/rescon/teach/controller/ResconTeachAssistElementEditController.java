package com.lanking.uxb.rescon.teach.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingQuestion;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficult;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficultExample;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentPreview;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointCardService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingQuestionService;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconSpecialTrainingConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;
import com.lanking.uxb.rescon.basedata.value.VSpecialTraining;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentFallibleDifficultExampleService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentFallibleDifficultService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentPreviewService;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentFallibleDifficultConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentFallibleDifficultExampleConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentPreviewConvert;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentFallibleDifficult;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentFallibleDifficultExample;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;

/**
 * 教辅编辑相关接口处理
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@RestController
@RequestMapping(value = "/rescon/tae")
public class ResconTeachAssistElementEditController {

	@Autowired
	private ResconKnowledgePointCardService knowledgePointCardService;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconTeachAssistPresetContentPreviewService contentPreviewService;
	@Autowired
	private ResconTeachAssistPresetContentPreviewConvert contentPreviewConvert;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultService presetDifficultService;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultConvert presetDifficultConvert;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultExampleService exampleService;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultExampleConvert exampleConvert;
	@Autowired
	private ResconSpecialTrainingService specialTrainingService;
	@Autowired
	private ResconSpecialTrainingConvert specialTrainingConvert;
	@Autowired
	private ResconSpecialTrainingQuestionService specialTrainingQuestionService;

	/**
	 * 教学内知识点添加知识点后题目数据
	 *
	 * @param codes
	 *            知识点代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getKpQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKpQuestions(@RequestParam(value = "codes") Collection<Long> codes) {
		List<KnowledgePointCard> cards = knowledgePointCardService.findByKnowpointCode(codes, CardStatus.PASS);
		List<Long> questionIds = null;
		if (CollectionUtils.isNotEmpty(cards)) {
			questionIds = Lists.newArrayList();
			for (KnowledgePointCard c : cards) {
				if (CollectionUtils.isNotEmpty(c.getQuestions())) {
					questionIds.addAll(c.getQuestions());
				}
			}
		}

		if (questionIds != null) {
			List<Question> questions = questionManage.mgetList(questionIds);
			if (questions.size() > 10) {
				questions = questions.subList(0, 10);
			}
			List<String> questionCodes = new ArrayList<String>(questions.size());
			for (Question q : questions) {
				questionCodes.add(q.getCode());
			}
			return new Value(questionCodes);
		}
		return new Value();
	}

	/**
	 * 根据多个题目code获得数据
	 *
	 * @param codes
	 *            题目code列表
	 * @param phaseCode
	 *            阶段代码
	 * @param subjectCode
	 *            学科code
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getQuestionByCodes", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getQuestionByCodes(@RequestParam(value = "codes") List<String> codes, int phaseCode, int subjectCode) {
		if (CollectionUtils.isEmpty(codes)) {
			return new Value(new IllegalArgException());
		}

		List<Question> questions = questionManage.findQuestionByCode(codes, 2);
		if (CollectionUtils.isNotEmpty(questions)) {
			List<Question> retList = new ArrayList<Question>(questions.size());
			for (Question q : questions) {
				if (q.getPhaseCode().equals(phaseCode) && q.getSubjectCode().equals(subjectCode)
						&& q.getStatus() == CheckStatus.PASS) {
					retList.add(q);
				}
			}

			return new Value(questionConvert.to(retList));
		}

		return new Value();
	}

	/**
	 * 根据题目code 得到题目！
	 *
	 * 注意：此方法教辅专用！！！！ 教辅可以添加校本题库的题目，其他地方勿用
	 *
	 * @param questionCode
	 *            题目代码
	 * @param subjectCode
	 *            学科码
	 * @param phaseCode
	 *            阶段码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getQuestionByCode", method = RequestMethod.POST)
	public Value getQuestionByCode(String questionCode, int subjectCode, int phaseCode) {
		List<Question> qList = questionManage.findQuestionByCode(Lists.newArrayList(questionCode), null);
		if (CollectionUtils.isEmpty(qList) || qList.size() != 1) {
			return new Value(new IllegalArgException());
		}
		Question question = qList.get(0);
		if (question.getSubjectCode() != subjectCode || question.getPhaseCode() != phaseCode) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.SUBJECT_PHASE_NOT_MATCH_KNOWPOINT));
		}
		if (question.getStatus() != CheckStatus.PASS) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_PASS));
		}
		return new Value(questionConvert.to(question));
	}

	/**
	 * 知识点回顾与说明获得知识点所有卡片中的说明内容
	 *
	 * @param code
	 *            知识点code
	 * @return 说明内容总合
	 */
	@RequestMapping(value = "getKpCardContent", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKpCardContent(long code) {
		List<KnowledgePointCard> cards = knowledgePointCardService.findByKnowpointCode(code, CardStatus.PASS);
		if (CollectionUtils.isEmpty(cards)) {
			return new Value();
		}
		StringBuilder content = new StringBuilder();
		for (KnowledgePointCard card : cards) {
			content.append(card.getDescription()).append("\n");
		}

		return new Value(content.toString());
	}

	/**
	 * 获得预习点题目列表
	 *
	 * @param code
	 *            知识专项代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getPresetContentQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getPresetContentQuestion(long code) {
		List<TeachAssistPresetContentPreview> previews = contentPreviewService.findByKnowledgeSystem(code);

		return new Value(contentPreviewConvert.to(previews));
	}

	/**
	 * 树状数据获得 预置内容中的易错疑难点
	 *
	 * @param code
	 *            知识专项代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getPresetFallible", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getPresetFallible(long code) {
		List<TeachAssistPresetContentFallibleDifficult> tapList = presetDifficultService.getByCode(code);
		List<Long> tapIds = new ArrayList<Long>(tapList.size());
		for (TeachAssistPresetContentFallibleDifficult t : tapList) {
			tapIds.add(t.getId());
		}

		List<TeachAssistPresetContentFallibleDifficultExample> examples = exampleService.findListByFallIds(tapIds);

		List<VTeachAssistPresetContentFallibleDifficult> vtapList = presetDifficultConvert.to(tapList);

		if (CollectionUtils.isNotEmpty(examples)) {
			List<VTeachAssistPresetContentFallibleDifficultExample> vexamples = exampleConvert.to(examples);

			Map<Long, VTeachAssistPresetContentFallibleDifficultExample> tapExampleMap = new HashMap<Long, VTeachAssistPresetContentFallibleDifficultExample>(
					vexamples.size());

			// 目前只有一个例题 暂时这样处理 ！ 注意
			for (VTeachAssistPresetContentFallibleDifficultExample v : vexamples) {
				tapExampleMap.put(v.getTeachassistPcFallibleDifficultId(), v);
			}

			for (VTeachAssistPresetContentFallibleDifficult v : vtapList) {
				v.setExample(tapExampleMap.get(v.getId()));
			}
		}

		return new Value(vtapList);
	}

	/**
	 * 获得知识专项层的树结构
	 *
	 * @param subjectCode
	 *            学科码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getKnowledgeSystemTree", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKnowledgeSystemTree(int subjectCode) {
		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.findAllBySubject(subjectCode);

		List<VKnowledgeSystem> vs = knowledgeSystemConvert.to(knowledgeSystems);

		return new Value(vs);
	}

	/**
	 * 根据知识专项代码获得训练列表（树状结构异步加载数据）
	 *
	 * @param code
	 *            知识专项代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getSpecialTraining", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSpecialTraining(long code) {
		List<VSpecialTraining> vs = specialTrainingConvert.to(specialTrainingService.list(code));
		return new Value(vs);
	}

	/**
	 * 获得专项练训中的题目
	 *
	 * @param id
	 *            专项训练id
	 * @return {@link List}
	 */
	@RequestMapping(value = "getSpecialTrainingQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSpecialTrainingQuestion(long id) {
		List<SpecialTrainingQuestion> specialTrainingQuestions = specialTrainingQuestionService.questionList(id);
		List<Long> questionIds = new ArrayList<Long>(specialTrainingQuestions.size());
		for (SpecialTrainingQuestion q : specialTrainingQuestions) {
			questionIds.add(q.getQuestionId());
		}

		List<Question> questions = questionManage.mgetList(questionIds);
		return new Value(questionConvert.to(questions));
	}
}
