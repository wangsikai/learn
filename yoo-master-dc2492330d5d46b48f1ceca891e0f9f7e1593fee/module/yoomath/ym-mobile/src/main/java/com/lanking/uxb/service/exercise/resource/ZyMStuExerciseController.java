package com.lanking.uxb.service.exercise.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractise;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.base.type.CommonSettings;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseService;
import com.lanking.uxb.service.zuoye.convert.ZySectionPractiseQuestionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentExerciseSectionConvert;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.form.SectionPractiseForm;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;
import com.lanking.uxb.service.zuoye.value.VSectionPractiseQuestion;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseSection;

/**
 * 悠数学移动端(学生章节知识点练习相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月11日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/exe")
public class ZyMStuExerciseController extends ZyMBaseController {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZyStudentExerciseSectionConvert studentExerciseSectionConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private ZySectionPractiseService zySectionPractiseService;
	@Autowired
	private ZyCorrectingService zyCorrectingService;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	@Autowired
	private ZySectionPractiseQuestionConvert sectionPractiseQuestionConvert;
	@Autowired
	private ZySectionPractiseQuestionService zySectionPractiseQuestionService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 章节练习的首页数据<br>
	 * 1.学生可选教材列表: 先判断学生自己有没有设置阶段版本教材信息,如果没有设置则取此学生加入班级的老师的阶段信息(此时则高中苏教版初中苏科版)
	 * <br>
	 * 2.获取默认教材下面的错题章节列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		Map<String, Object> dataMap = textbookList(Security.getUserType(), Security.getUserId(), 5);
		List<VTextbook> tbs = (List<VTextbook>) dataMap.get("textbooks");
		if (CollectionUtils.isNotEmpty(tbs)) {
			Integer textbookCode = (Integer) dataMap.get("textbookCode");
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			if (CollectionUtils.isNotEmpty(vsections)) {
				List<VStudentExerciseSection> vs = studentExerciseSectionConvert.to(vsections);
				studentExerciseSectionConvert.statisticsBeforeAssembleTree(vs, Security.getUserId(), null);
				dataMap.put("exerciseSections", studentExerciseSectionConvert.assembleTree(vs));
			} else {
				dataMap.put("exerciseSections", Lists.newArrayList());
			}
		}
		return new Value(dataMap);
	}

	/**
	 * 根据教材代码获取章节练习树形结构数据
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param textbookCode
	 *            教材代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "exerciseSectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value exerciseSectionTree(int textbookCode) {
		Map<String, Object> dataMap = new HashMap<String, Object>(1);
		List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));

		List<VStudentExerciseSection> vs = studentExerciseSectionConvert.to(vsections);
		studentExerciseSectionConvert.statisticsBeforeAssembleTree(vs, Security.getUserId(), null);
		dataMap.put("exerciseSections", studentExerciseSectionConvert.assembleTree(vs));
		return new Value(dataMap);
	}

	/**
	 * 根据章节代码获取章节题目
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param sectionCode
	 *            章节代码
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(long sectionCode) {

		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		PullQuestionForm form = new PullQuestionForm();
		form.setVersion("1.4");
		form.setSectionCode(sectionCode);
		form.setCount(CommonSettings.QUESTION_PULL_COUNT);
		form.setType(PullQuestionType.PRACTISE);
		List<Long> ids = pullQuestionService.pull(form);
		if (CollectionUtils.isEmpty(ids)) {
			dataMap.put("questions", Collections.EMPTY_LIST);
			dataMap.put("predictTime", 0);
		} else {
			List<Question> qs = new ArrayList<Question>(ids.size());
			Map<Long, Question> questions = questionService.mget(ids);
			for (Long qid : ids) {
				qs.add(questions.get(qid));
			}
			MemberType memberType = SecurityContext.getMemberType();
			QuestionConvertOption option = new QuestionConvertOption();
			option.setAnswer(true);
			option.setAnalysis(memberType != null && memberType == MemberType.VIP);
			option.setInitMetaKnowpoint(false);
			option.setInitPhase(false);
			option.setInitTextbookCategory(false);
			List<VQuestion> vquestions = questionConvert.to(qs, option);
			dataMap.put("questions", vquestions);

			int predictTime = 0;
			for (VQuestion v : vquestions) {
				predictTime += questionService.calPredictTime(v.getType(), v.getDifficulty(), v.getSubject().getCode());
			}

			dataMap.put("predictTime", predictTime);
		}
		return new Value(dataMap);
	}

	/**
	 * 章节练习提交
	 *
	 * @since yoomath(mobile) V1.0.0
	 * @param form
	 *            提交参数
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit(ExerciseCommitForm form,
			@RequestParam(value = "sectionCode", required = false) Long sectionCode) {
		if (form.getType() != 1 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		if (sectionCode == null && form.getPaperId() == null) {
			// 为兼容1.0接口，若不传sectionCode则此次数据不入库
			try {
				List<Map<String, Object>> results = zyCorrectingService.simpleCorrect(form.getqIds(),
						form.getAnswerList());
				VExerciseResult result = new VExerciseResult(1);
				result.setqIds(form.getqIds());
				List<HomeworkAnswerResult> rets = new ArrayList<HomeworkAnswerResult>(form.getCount());
				List<Boolean> dones = new ArrayList<Boolean>(form.getCount());
				int rightCount = 0;
				for (Map<String, Object> map : results) {
					HomeworkAnswerResult ret = (HomeworkAnswerResult) map.get("result");
					boolean done = (Boolean) map.get("done");
					rets.add(ret);
					if (ret == HomeworkAnswerResult.RIGHT) {
						rightCount++;
					}
					dones.add(done);
				}
				result.setDones(dones);
				result.setRightRate(BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(2,
						BigDecimal.ROUND_HALF_UP));
				result.setResults(rets);
				// 记录答案、统计学情、记录错题
				zyStuQaService.asynCreate(Security.getUserId(), form.getqIds(), form.getAnswerList(),
						form.getAnswerList(), null, null, null, rets, StudentQuestionAnswerSource.SECTION_EXERCISE,
						new Date());

				// 用户动作行为
				userActionService.action(UserAction.SUBMIT_SECTION_PRACTISE, Security.getUserId(), null);

				JSONObject messageObj = new JSONObject();
				messageObj.put("userId", Security.getUserId());
				Map<String, Object> params = new HashMap<String, Object>(1);
				params.put("rightRate", result.getRightRate().intValue());
				messageObj.put("params", params);
				messageObj.put("isClient", Security.isClient());
				messageObj.put("taskCode", 101010005);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

				// 章节练习，题量任务
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
			} catch (AbstractException e) {
				return new Value(new IllegalArgException());
			}
		} else {
			SectionPractiseForm practiseForm = new SectionPractiseForm();
			if (form.getPaperId() == null && null != sectionCode) {
				Section section = sectionService.get(sectionCode);
				if (section == null) {
					return new Value(new IllegalArgException());
				}
				practiseForm.setName(section.getName());
				practiseForm.setSectionCode(sectionCode);
			}
			practiseForm.setAnswerList(form.getAnswerList());
			practiseForm.setHomeworkTime(form.getHomeworkTime());
			practiseForm.setQuestionIds(form.getqIds());
			practiseForm.setUserId(Security.getUserId());
			practiseForm.setId(form.getPaperId());

			Map<String, Object> retMap = zySectionPractiseService.commit(practiseForm);
			List<HomeworkAnswerResult> results = (List<HomeworkAnswerResult>) retMap.get("results");
			List<Boolean> dones = (List<Boolean>) retMap.get("dones");
			BigDecimal rightRate = (BigDecimal) retMap.get("rightRate");

			zyStuQaService.asynCreate(practiseForm.getUserId(), practiseForm.getQuestionIds(),
					practiseForm.getAnswerList(), practiseForm.getAnswerList(), null, null, null, results,
					StudentQuestionAnswerSource.SECTION_EXERCISE, new Date());

			VExerciseResult result = new VExerciseResult();
			result.setDones(dones);
			result.setResults(results);
			result.setRightRate(rightRate);
			result.setqIds((List<Long>) retMap.get("qIds"));
			result.setExeId((Long) retMap.get("practiseId"));

			// 用户动作行为
			userActionService.action(UserAction.SUBMIT_SECTION_PRACTISE, Security.getUserId(), null);

			JSONObject messageObj = new JSONObject();
			messageObj.put("userId", Security.getUserId());
			Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("rightRate", result.getRightRate().intValue());
			messageObj.put("params", params);
			messageObj.put("isClient", true);
			messageObj.put("taskCode", 101010005);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(messageObj).build());

			// 章节练习，题量任务
			JSONObject obj = new JSONObject();
			obj.put("taskCode", 101020016);
			obj.put("userId", Security.getUserId());
			obj.put("isClient", true);
			Map<String, Object> pms = new HashMap<String, Object>(1);
			pms.put("questionCount", form.getCount());
			obj.put("params", pms);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(obj).build());

			return new Value(result);
		}
	}

	/**
	 * 暂存章节练习答案
	 *
	 * @param form
	 *            {@link ExerciseCommitForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "draft", method = { RequestMethod.GET, RequestMethod.POST })
	public Value draft(ExerciseCommitForm form,
			@RequestParam(value = "sectionCode", required = false) Long sectionCode) {
		if (form.getType() != 1 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		SectionPractiseForm practiseForm = new SectionPractiseForm();
		if (form.getPaperId() == null && sectionCode != null) {
			Section section = sectionService.get(sectionCode);
			if (section == null) {
				return new Value(new IllegalArgException());
			}
			practiseForm.setName(section.getName());
			practiseForm.setSectionCode(sectionCode);
		}
		practiseForm.setAnswerList(form.getAnswerList());
		practiseForm.setHomeworkTime(form.getHomeworkTime());
		practiseForm.setQuestionIds(form.getqIds());
		practiseForm.setUserId(Security.getUserId());
		practiseForm.setId(form.getPaperId());

		SectionPractise sectionPractise = zySectionPractiseService.draft(practiseForm);

		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("practiseId", sectionPractise.getId());

		// 用户动作行为
		userActionService.action(UserAction.SUBMIT_SECTION_PRACTISE, Security.getUserId(), null);
		return new Value(retMap);
	}

	/**
	 * 历史列表中查看章节练习详情及题目
	 *
	 * @param id
	 *            章节练习id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryQuestions(long id) {
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		SectionPractise sectionPractise = zySectionPractiseService.get(id);
		retMap.put("sectionCode", sectionPractise.getSectionCode());
		List<VSectionPractiseQuestion> vs = sectionPractiseQuestionConvert
				.to(zySectionPractiseQuestionService.mgetListByPractise(id));
		retMap.put("sectionPractiseQuestions", vs);

		// 此两处为练习历史 查看详情产生练习报告所需的数据 以及答题卡时间
		retMap.put("homeworkTime", sectionPractise.getHomeworkTime());
		retMap.put("difficulty", sectionPractise.getDifficulty());

		return new Value(retMap);
	}

}
