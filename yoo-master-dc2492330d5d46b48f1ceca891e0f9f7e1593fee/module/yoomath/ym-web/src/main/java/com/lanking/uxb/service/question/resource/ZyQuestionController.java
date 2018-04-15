package com.lanking.uxb.service.question.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.counter.api.impl.QuestionUserCouterProvider;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;

/**
 * 习题相关.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月14日
 * @since v2.3.1
 */
@RestController
@RequestMapping(value = "zy/q")
public class ZyQuestionController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionUserCouterProvider questionUserCouterProvider;
	@Autowired
	private ZyStudentFallibleQuestionService studentFallibleQuestionService;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;

	/**
	 * 习题详情.
	 * 
	 * @param id
	 *            习题ID
	 * @param code
	 *            习题CODE
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "detail", method = { RequestMethod.POST })
	@MemberAllowed
	public Value query(Long id, String code) {
		if (id == null && StringUtils.isBlank(code)) {
			throw new MissingArgumentException();
		}
		Map<String, Object> map = new HashMap<String, Object>(4);
		long userId = Security.getUserId();
		MemberType memberType = SecurityContext.getMemberType(); // 会员类型

		Question question = null;
		if (id != null) {
			question = questionService.get(id);
		} else {
			question = questionService.findByCode(code);
		}
		if (question == null) {
			return new Value(new EntityNotFoundException());
		}
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnalysis(true);
		option.setAnswer(true);
		option.setCollect(true);
		option.setInitExamination(true);
		VQuestion vQuestion = questionConvert.to(question, option);
		if (memberType == MemberType.NONE) {
			vQuestion.setAnalysis("");
			vQuestion.setHint("");
			vQuestion.setAllAnalysis(null);
			vQuestion.setAllHints(null);
		}
		map.put("question", vQuestion);

		// 获取教师相关信息
		if (Security.getUserType() == UserType.TEACHER) {
			long publishCount = questionUserCouterProvider.getQuestionPublishCount(id, userId); // 作业布置次数
			map.put("publishCount", publishCount);
		} else if (Security.getUserType() == UserType.STUDENT) {
			long exerciseCount = questionUserCouterProvider.getQuestionExerciseCount(id, userId); // 练习次数
			map.put("exerciseCount", exerciseCount);
			List<Long> questionIds = new ArrayList<Long>(1);
			questionIds.add(id);

			// 原错题记录表数据
			List<StudentFallibleQuestion> fsqs = studentFallibleQuestionService.mgetQuestionList(questionIds, userId);
			StudentFallibleQuestion sfq = null;
			if (CollectionUtils.isNotEmpty(fsqs)) {
				sfq = fsqs.get(0);
			}

			if (sfq != null && sfq.getExerciseNum() > exerciseCount) {
				// 原错题记录中的练习次数 > 新的统计表数据
				exerciseCount = sfq.getExerciseNum();
				map.put("exerciseCount", exerciseCount);
				map.put("avgRate", (int) ((double) (exerciseCount - sfq.getMistakeNum()) * 100 / exerciseCount));
			} else if (sfq != null) {
				map.put("avgRate", (int) ((double) (exerciseCount - sfq.getMistakeNum()) * 100 / exerciseCount));
			}

			// if (CollectionUtils.isNotEmpty(fsqs)) {
			// StudentFallibleQuestion sfq = fsqs.get(0);
			// if (exerciseCount > 0) {
			// if (exerciseCount < sfq.getMistakeNum()) {
			// map.put("avgRate", 0);
			// } else {
			// map.put("avgRate", (int) ((double) (exerciseCount -
			// sfq.getMistakeNum()) * 100 / exerciseCount));
			// }
			// }
			// map.put("avgRate", (int) ((double) sfq.getMistakeNum() * 100
			// / sfq.getDoNum()));
			// if (exerciseCount == 0 && sfq.getDoNum() != 0) {
			// map.put("exerciseCount", sfq.getDoNum());
			// }
			// }
		}

		// 获取新知识点卡片
		List<Long> knowledgeCodes = new ArrayList<Long>(vQuestion.getNewKnowpoints().size());
		for (VKnowledgePoint kp : vQuestion.getNewKnowpoints()) {
			knowledgeCodes.add(kp.getCode());
		}
		List<Long> kcodes = knowledgePointCardService.findHasCardKnowledgePoint(knowledgeCodes);
		for (VKnowledgePoint kp : vQuestion.getNewKnowpoints()) {
			if (kcodes.contains(kp.getCode())) {
				kp.setHasCard(true);
			}
		}

		return new Value(map);
	}
}
