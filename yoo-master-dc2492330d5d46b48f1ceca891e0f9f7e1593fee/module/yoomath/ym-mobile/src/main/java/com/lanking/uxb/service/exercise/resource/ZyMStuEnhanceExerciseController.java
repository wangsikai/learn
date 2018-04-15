package com.lanking.uxb.service.exercise.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

/**
 * 针对薄弱知识点加强练习
 *
 * @author xinyu.zhou
 * @since 3.9.0
 */
@RestController
@RequestMapping(value = "zy/m/s/ee")
public class ZyMStuEnhanceExerciseController {
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyCorrectingService correctingService;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 拉取题目
	 *
	 * @param questionIds
	 *            题目id列表(排除的题目列表)
	 * @param codes
	 *            新知识点code列表
	 * @param size
	 *            拉取题目数量
	 * @return {@link Value}
	 */
	@MemberAllowed(memberType = "VIP")
	@RolesAllowed(userTypes = "STUDENT")
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "pullQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pullQuestions(@RequestParam(value = "questionIds", required = false) List<Long> questionIds,
			@RequestParam(value = "codes") List<Long> codes,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		if (CollectionUtils.isEmpty(codes)) {
			return new Value(new IllegalArgException());
		}

		PullQuestionForm form = new PullQuestionForm();
		form.setKnowledgePoints(codes);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			form.setqIds(questionIds);
		}
		form.setCount(size);
		form.setType(PullQuestionType.KNOWPOINT_ENHANCE_EXERCISE);

		List<Long> ids = pullQuestionService.pull(form);

		List<Question> questions = questionService.mgetList(ids);

		Map<String, Object> data = new HashMap<String, Object>(2);
		int predictTime = 0;
		double difficulty = 0;
		for (Question q : questions) {
			predictTime += questionService.calPredictTime(q);
			difficulty += q.getDifficulty();
		}

		data.put("predictTime", predictTime);
		if (CollectionUtils.isNotEmpty(questions)) {
			BigDecimal difficultyDecimal = new BigDecimal(difficulty / questions.size())
					.setScale(BigDecimal.ROUND_HALF_UP, 2);
			data.put("difficulty", difficultyDecimal.doubleValue());
			QuestionConvertOption option = new QuestionConvertOption();
			option.setAnswer(true);
			option.setAnalysis(true);
			option.setCollect(true);
			data.put("questions", questionConvert.to(questions, option));
		}

		return new Value(data);
	}
	

	/**
	 * 提交加强练习结果
	 *
	 * @param form
	 *            {@link ExerciseCommitForm}
	 * @return {@link Value}
	 */
	@MemberAllowed(memberType = "VIP")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "commit")
	public Value commit(ExerciseCommitForm form) {
		if (form.getType() != 4 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		
		List<Map<String, Object>> results = correctingService.simpleCorrect(form.getqIds(), form.getAnswerList());

		VExerciseResult result = new VExerciseResult(1);
		result.setqIds(form.getqIds());
		List<HomeworkAnswerResult> rets = new ArrayList<HomeworkAnswerResult>(form.getCount());
		List<Boolean> dones = new ArrayList<Boolean>(form.getCount());
		int rightCount = 0, getGrowth = 0, earnCoin = 0;
		GrowthLog honorGrowthLog = null;
		for (Map<String, Object> map : results) {
			HomeworkAnswerResult ret = (HomeworkAnswerResult) map.get("result");
			boolean done = (Boolean) map.get("done");
			rets.add(ret);
			if (ret == HomeworkAnswerResult.RIGHT) {
				rightCount++;
			}
			dones.add(done);

			if (done) {
				GrowthLog growthLog = growthService.grow(GrowthAction.DOING_DAILY_EXERCISE, Security.getUserId(), true);
				coinsService.earn(CoinsAction.DOING_DAILY_EXERCISE, Security.getUserId());

				if (growthLog.getHonor() != null) {
					getGrowth++;
					earnCoin++;

					honorGrowthLog = growthLog;
				}
			}
		}
		result.setDones(dones);
		result.setRightRate(
				BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		result.setResults(rets);
		
		// 记录答案、统计学情、记录错题
		zyStuQaService.asynCreate(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
				null, null, null, rets, StudentQuestionAnswerSource.ENHANCE_PRACTICE, new Date());

		if (honorGrowthLog != null) {
			VUserReward userReward = new VUserReward(honorGrowthLog.getHonor().getUpRewardCoins(),
					honorGrowthLog.getHonor().isUpgrade(), honorGrowthLog.getHonor().getLevel(), getGrowth, earnCoin);
			result.setUserReward(userReward);
		}

		// 薄弱知识点练习，题量任务
		JSONObject obj = new JSONObject();
		obj.put("taskCode", 101020016);
		obj.put("userId", Security.getUserId());
		obj.put("isClient", Security.isClient());
		Map<String, Object> pms = new HashMap<String, Object>(1);
		pms.put("questionCount", form.getCount());
		obj.put("params", pms);
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(obj).build());

		return new Value(result);
	}

}
