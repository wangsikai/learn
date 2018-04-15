package com.lanking.uxb.service.homework.resource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.web.api.ZyTeaPertinenceHomeworkService;
import com.lanking.uxb.service.web.form.PertinenceHomeworkForm;
import com.lanking.uxb.service.web.resource.ZyTeaHomeworkController;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;

/**
 * 针对性训练.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version yoomath v2.3.0 2016年12月8日
 */
@RestController
@RequestMapping(value = "zy/t/perth")
public class ZyTeaPertinenceHomeworkController {

	@Autowired
	private ZyTeaPertinenceHomeworkService pertinenceHomeworkService;
	@Autowired
	private ZyTeaHomeworkController teaHomeworkController;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkClazzConvert hkClassConvert;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;

	/**
	 * 获得布置针对性训练作业的参数.
	 */
	@RequestMapping(value = "getPushDatas", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value getPushDatas(PertinenceHomeworkForm form) {

		// 搜索题目
		List<Long> questionIds = pertinenceHomeworkService.queryPertinenceHomeworkQuestions(form);

		// 判断题目类型
		List<Question> questions = questionService.mgetList(questionIds);
		boolean hasAnswerType = false; // 是否包含解答题
		boolean hasBlank = false; // 是否包含填空题
		for (Question question : questions) {
			if (question.getType() == Question.Type.QUESTION_ANSWERING) {
				hasAnswerType = true;
			} else if (question.getType() == Question.Type.FILL_BLANK) {
				hasBlank = true;
			}
		}
		if (hasAnswerType) {
			// 优先判断是否包含解答题
			hasBlank = false;
		}

		// 平均难度
		BigDecimal avgDifficulty = new BigDecimal(1);
		if (questionIds.size() > 0) {

			avgDifficulty = pertinenceHomeworkService.getAvgDifficulty(questionIds);
		}
		int issueHour = Env.getDynamicInt("homework.issued.time"); // 自动下发小时数

		Map<String, Object> map = new HashMap<String, Object>(6);
		List<HomeworkClazz> clazzs = hkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			map.put("clazzs", Collections.EMPTY_LIST);
		} else {
			map.put("clazzs", hkClassConvert.to(clazzs));
		}
		Map<Long, VQuestion> qvm = questionConvert.toMap(questions, new QuestionConvertOption(false, true, true, false,
				true, null));
		List<VKnowledgePoint> vPoint = knowledgePointConvert
				.to(knowledgePointService.mgetList(form.getKnowledgeCodes()));
		map.put("knowledge", vPoint);
		map.put("questions", qvm.values());
		map.put("hasAnswerType", hasAnswerType);
		map.put("hasBlank", hasBlank);
		map.put("avgDifficulty", avgDifficulty);
		map.put("issueHour", issueHour);
		return new Value(map);
	}
}
