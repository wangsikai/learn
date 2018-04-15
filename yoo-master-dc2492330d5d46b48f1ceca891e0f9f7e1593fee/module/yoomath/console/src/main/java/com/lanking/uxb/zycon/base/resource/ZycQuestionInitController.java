package com.lanking.uxb.zycon.base.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.MetaKnowSectionService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.base.api.ZycExerciseService;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;
import com.lanking.uxb.zycon.base.api.ZycTextbookExerciseService;
import com.lanking.uxb.zycon.base.convert.ZycExerciseSectionConvert;
import com.lanking.uxb.zycon.base.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.base.convert.ZycTextbookExerciseConvert;
import com.lanking.uxb.zycon.base.value.CExerciseSection;
import com.lanking.uxb.zycon.base.value.CQuestion;
import com.lanking.uxb.zycon.base.value.CTextbookExercise;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;

/**
 * U数学 管控台预支习题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午9:56:47
 */
@RestController
@RequestMapping("zyc/q")
public class ZycQuestionInitController {

	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private ZycTextbookExerciseService tbeService;
	@Autowired
	private ZycTextbookExerciseConvert tbeConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZycExerciseSectionConvert esConvert;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZycExerciseService zyExerciseService;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycQuestionConvert questionConvert;
	@Autowired
	private MetaKnowSectionService metaKnowpointSectionService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;

	/**
	 * 获取预置习题 左侧列表及相关信息
	 * 
	 * @param textbookCode
	 *            教材
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @return
	 */
	@RequestMapping(value = "sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree(@RequestParam(value = "textbookCode", required = false) Integer textbookCode,
			@RequestParam(value = "phaseCode", required = false) Integer phaseCode,
			@RequestParam(value = "subjectCode", required = false) Integer subjectCode) {
		Map<String, Object> data = new HashMap<String, Object>(6);
		data.put("textbookExerciseId", null);
		if (phaseCode == null && subjectCode == null) {
			phaseCode = 3;
			subjectCode = 302;
		}
		if (textbookCode == null) {
			List<VTextbookCategory> categories = tbcConvert.to(tbcService.getAll());
			data.put("textbookCategories", categories);
			List<Integer> categoryCodes = new ArrayList<Integer>(categories.size());
			for (VTextbookCategory v : categories) {
				categoryCodes.add(v.getCode());
			}
			List<VTextbook> textbooks = tbConvert.to(tbService.getAll());
			data.put("textbooks", textbooks);
		}
		List<CTextbookExercise> vexercises = tbeConvert.to(tbeService.findByTextbook(textbookCode == null ? 12330201
				: textbookCode));
		List<CExerciseSection> vs = esConvert.to(sectionConvert.to(sectionService
				.findByTextbookCode(textbookCode == null ? 12330201 : textbookCode)));
		esConvert.assemblyExercise(vs, vexercises);
		data.put("sections", esConvert.assemblySectionTree(vs));
		List<VPhase> phaseList = phaseConvert.to(phaseService.getAll());
		List<VSubject> subjectList = subjectConvert.to(subjectService.getAll());
		data.put("phases", phaseList);
		data.put("subjects", subjectList);
		return new Value(data);
	}

	/**
	 * 通过预置习题ID拉取题目列表以及其他信息
	 * 
	 * @since 2.1
	 * @param textbookExerciseId
	 *            预置习题ID
	 * @return {@link Value}
	 */
	@RequestMapping(value = "listQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listQuestions(@RequestParam(value = "textbookExerciseId", required = false) Long textbookExerciseId) {
		if (textbookExerciseId == null) {
			return new Value();
		}
		Map<String, Object> data = new HashMap<String, Object>(2);
		// 返回题目
		List<Long> qIds = zyExerciseService.listQuestions(Security.getUserId(), textbookExerciseId);
		if (qIds.size() == 0) {
			data.put("questions", Collections.EMPTY_LIST);
		} else {
			Map<Long, Question> questions = questionService.mget(qIds);
			Map<Long, CQuestion> vquestions = questionConvert.to(questions, false, true, false, null);
			List<CQuestion> qs = new ArrayList<CQuestion>(qIds.size());
			for (Long qId : qIds) {
				qs.add(vquestions.get(qId));
			}
			data.put("questions", qs);
		}
		data.put("isAutoGenerate", tbeService.get(textbookExerciseId).isAutoGenerate());
		return new Value(data);
	}

	/**
	 * 预览预置习题
	 * 
	 * @param qIds
	 * @return
	 */
	@RequestMapping(value = "viewQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value viewQuestions(@RequestParam(value = "qCodes", required = true) List<String> qCodes) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<Long> qIds = questionService.findQuestionIdsByCode(qCodes, "isOrderDif");
		Map<Long, Question> questions = questionService.mget(qIds);
		Map<Long, CQuestion> vquestions = questionConvert.to(questions, false, true, true, null);
		List<CQuestion> qs = new ArrayList<CQuestion>(qIds.size());
		for (Long qId : qIds) {
			qs.add(vquestions.get(qId));
		}
		data.put("questions", qs);
		// 排序
		// Collections.sort(qs, new Comparator<CQuestion>() {
		// public int compare(CQuestion arg0, CQuestion arg1) {
		// return arg0.getDifficulty().compareTo(arg1.getDifficulty());
		// }
		// });
		// Collections.reverse(qs);
		return new Value(data);
	}

	/**
	 * 预置习题
	 * 
	 * @param qIds
	 * @return
	 */
	@RequestMapping(value = "setQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setQuestions(@RequestParam(value = "exerciseId", required = false) Long exerciseId,
			@RequestParam(value = "qCodes", required = false) List<String> qCodes,
			@RequestParam(value = "textBookCode", required = false) Integer textBookCode,
			@RequestParam(value = "sectionCode", required = false) Long sectionCode,
			@RequestParam(value = "exerciseName", required = false) String exerciseName,
			@RequestParam(value = "isDefaultExercise", required = false) Boolean isDefaultExercise) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<Long> qIds = new ArrayList<Long>();
		// 带排序的QID(单选--》多选--》填空,简单到难)
		qIds = questionService.findQuestionIdsByCode(qCodes, "isOrderDif");
		Map<Long, Question> questions = questionService.mget(qIds);
		for (Question question : questions.values()) {
			if (question.getSchoolId() > 0) {
				return new Value(new YoomathConsoleException(YoomathConsoleException.SCHOOLQUESTION_FORBIDDEN));
			}
		}
		TextbookExercise exercise = null;
		// 处理一个题目编号对应多个题目的情况
		if (qCodes != null && qIds.size() > qCodes.size()) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.BAD_QUESTION_CODE));
		}
		// 处理题目状态不对的情况
		if (qCodes != null && qIds.size() < qCodes.size()) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.BAD_QUESTION_STATUS));
		}
		if (exerciseName == null) {
			exerciseName = sectionService.get(sectionCode).getName();
		}
		if (exerciseId == null) {
			exercise = tbeService.add(textBookCode, sectionCode, Security.getUserId(), exerciseName, isDefaultExercise);
			exerciseId = exercise.getId();
		} else {
			exercise = tbeService.updateExercise(exerciseId, exerciseName);
		}
		data.put("exercise", tbeConvert.to(exercise));

		// 预置习题
		tbeService.setQuestions(qIds, exerciseId);
		Map<Long, CQuestion> vquestions = questionConvert.to(questions, false, true, true, null);
		List<CQuestion> qs = new ArrayList<CQuestion>(qIds.size());
		for (Long qId : qIds) {
			qs.add(vquestions.get(qId));
		}
		data.put("exerciseId", exerciseId);
		data.put("questions", qs);
		return new Value(data);
	}

	/**
	 * 检查习题是否有不存在
	 * 
	 * @param qIds
	 * @return
	 */
	@RequestMapping(value = "checkQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkQuestions(@RequestParam(value = "qCodes", required = false) List<String> qCodes) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<String> notExistCodes = new ArrayList<String>();
		if (qCodes == null) {
			return new Value(data);
		}
		List<String> qCodeList = new ArrayList<String>();
		List<String> typeWrongList = new ArrayList<String>();
		List<String> statusWrongList = new ArrayList<String>();
		List<Question> questions = questionService.findQuestionByCode(qCodes);

		for (Question question : questions) {
			qCodeList.add(question.getCode());
			// 判断题型是否正确
			if (question.getType() != Type.SINGLE_CHOICE && question.getType() != Type.MULTIPLE_CHOICE
					&& question.getType() != Type.FILL_BLANK) {
				typeWrongList.add(question.getCode());
			}
			// 判断状态是否正确
			if (question.getStatus() != CheckStatus.PASS) {
				statusWrongList.add(question.getCode());
			}
		}
		for (String long1 : qCodes) {
			if (!qCodeList.contains(long1)) {
				notExistCodes.add(long1);
			}
		}
		// notExistCodes为空的话说明习题都存在
		data.put("notExistCode", notExistCodes);
		// 判断题型时候正确（只能是选择填空）
		data.put("typeWrongList", typeWrongList);
		// 判断题目状态是否正确，只能预置通过状态的题目
		data.put("statusWrongList", statusWrongList);
		return new Value(data);
	}

	/**
	 * 对习题进行禁用、启用、删除操作
	 * 
	 * @since yoomath V1.4
	 * @param textbookExerciseId
	 * @return
	 */
	@RequestMapping(value = "operateByStaus", method = { RequestMethod.POST, RequestMethod.GET })
	public Value operateByStaus(@RequestParam(value = "textbookExerciseId", required = false) Long textbookExerciseId,
			@RequestParam(value = "status") Status status) {
		if (textbookExerciseId == null) {
			return new Value();
		}
		tbeService.operateByStaus(textbookExerciseId, status);
		return new Value();
	}

	/**
	 * 验证,同一章节码下习题名称不能重复
	 * 
	 * @since yoomath V1.4
	 * @param name
	 * @param sectionCode
	 * @return
	 */
	@RequestMapping(value = "checkExerciseName", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkExerciseName(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sectionCode", required = false) Long sectionCode) {
		return new Value(tbeService.getExerciseNameCount(sectionCode, name));
	}
}
