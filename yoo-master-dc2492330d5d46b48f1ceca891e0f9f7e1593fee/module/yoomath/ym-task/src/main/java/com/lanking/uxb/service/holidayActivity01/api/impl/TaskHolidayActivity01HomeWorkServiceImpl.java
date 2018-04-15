package com.lanking.uxb.service.holidayActivity01.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionKnowledge;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseQuestion;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01User;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkKnowledge;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01HomeWorkService;
import com.lanking.uxb.service.holidayActivity01.api.form.TaskPublishHomeworkForm;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.form.HomeworkForm;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;

@Service
@Transactional(readOnly = true)
public class TaskHolidayActivity01HomeWorkServiceImpl implements TaskHolidayActivity01HomeWorkService {

	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ZyExerciseService zyExerciseService;

	@Autowired
	private HomeworkService hkService;

	@Autowired
	private ZyHomeworkStatService hkStatService;

	@Autowired
	@Qualifier("HomeworkKnowledgeRepo")
	Repo<HomeworkKnowledge, Long> homeworkKnowledgeRepo;

	@Autowired
	@Qualifier("HolidayActivity01HomeworkRepo")
	private Repo<HolidayActivity01Homework, Long> holidayActivity01HomeworkRepo;

	@Autowired
	@Qualifier("HolidayActivity01ExerciseQuestionRepo")
	private Repo<HolidayActivity01ExerciseQuestion, Long> holidayActivity01ExerciseQuestionRepo;

	@Autowired
	@Qualifier("HolidayActivity01ExerciseRepo")
	private Repo<HolidayActivity01Exercise, Long> holidayActivity01ExerciseRepo;

	@Autowired
	@Qualifier("HolidayActivity01Repo")
	private Repo<HolidayActivity01, Long> holidayActivity01Repo;

	@Autowired
	@Qualifier("HolidayActivity01UserRepo")
	private Repo<HolidayActivity01User, Long> holidayActivity01UserRepo;

	@Autowired
	private MqSender mqSender;

	@Transactional
	@Override
	public void publish(List<HolidayActivity01Homework> activityhks) {
		// 练习id
		Set<Long> exerciseIds = new HashSet<Long>();
		for (HolidayActivity01Homework activityhk : activityhks) {
			exerciseIds.add(activityhk.getHolidayActivity01ExerciseId());
		}
		// 查询练习对应的题目
		Map<Long, List<HolidayActivity01ExerciseQuestion>> exerciseQuestionMap = queryQuestionMap(exerciseIds);
		// 查询练习
		Map<Long, HolidayActivity01Exercise> exerciseMap = holidayActivity01ExerciseRepo.mget(exerciseIds);
		for (HolidayActivity01Homework activityhk : activityhks) {
			List<HolidayActivity01ExerciseQuestion> questions = exerciseQuestionMap
					.get(activityhk.getHolidayActivity01ExerciseId());
			List<Long> qIds = Lists.newArrayList();
			for (HolidayActivity01ExerciseQuestion question : questions) {
				qIds.add(question.getQuestionId());
			}
			// 防止qids为空
			if (qIds.size() == 0) {
				return;
			}
			List<Question> qList = questionService.mgetList(qIds);
			TaskPublishHomeworkForm publishHomeworkForm = new TaskPublishHomeworkForm();
			publishHomeworkForm.setqIds(qIds);
			publishHomeworkForm.setStartTime(activityhk.getStartTime().getTime());
			publishHomeworkForm.setDeadline(activityhk.getDeadline().getTime());
			publishHomeworkForm.setCreateId(activityhk.getUserId());
			publishHomeworkForm.setHomeworkClassIds(activityhk.getClassId());
			publishHomeworkForm.setAutoIssue(true);
			HolidayActivity01Exercise exercise = exerciseMap.get(activityhk.getHolidayActivity01ExerciseId());
			String exerciseTypeName = exercise.getType() == HolidayActivity01ExerciseType.KNOWLEDGE_POINT ? "寒假薄弱专练  "
					: exercise.getType() == HolidayActivity01ExerciseType.PRESET ? "寒假综合练习  " : "";
			publishHomeworkForm.setName(exerciseTypeName + exercise.getName());
			publishHomeworkForm.setCorrectingType(HomeworkCorrectingType.TEACHER);
			publishHomeworkForm.setDifficulty(getDifficulty(qList));
			publishHomeworkForm.setKnowledgePoints(setKnowledges(qIds));
			for (Question q : qList) {
				if (q.getType() == Type.QUESTION_ANSWERING) {
					publishHomeworkForm.setHasQuestionAnswering(true);
					break;
				}
			}
			// 布置作业
			Homework homeworks = publish2(publishHomeworkForm);
			activityhk.setHomeworkId(homeworks.getId());
			// 更新
			this.update(activityhk.getId(), homeworks.getId());
			mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
					MQ.builder().data(new PushHandleForm(PushHandleAction.NEW_HOMEWORK, homeworks.getId())).build());
			JSONObject jo = new JSONObject();
			jo.put("teacherId", activityhk.getUserId());
			jo.put("homeworkId", homeworks.getId());
			mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
					MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH, MQ.builder().data(jo).build());
		}
	}

	private Homework publish2(TaskPublishHomeworkForm form) {
		// 创建练习
		Exercise exercise = new Exercise();
		exercise.setCreateAt(new Date());
		exercise.setCreateId(form.getCreateId());
		exercise.setName(form.getName().trim());
		exercise.setStatus(Status.ENABLED);
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise = zyExerciseService.createExercise(exercise, form.getqIds());
		long homeworkId = 0;
		Date createAt = new Date();
		HomeworkForm hkForm = new HomeworkForm();
		hkForm.setCreateAt(createAt);
		hkForm.setHomeworkClassId(form.getHomeworkClassIds());
		hkForm.setCreateId(form.getCreateId());
		hkForm.setDeadline(form.getDeadline());
		hkForm.setExerciseId(exercise.getId());
		hkForm.setStartTime(form.getStartTime());
		hkForm.setDifficulty(form.getDifficulty());
		hkForm.setHasQuestionAnswering(form.isHasQuestionAnswering());
		// 是否有值
		hkForm.setTextbookCode(exercise.getTextCode());
		hkForm.setKnowledgePoints(form.getKnowledgePoints()); // 新知识点
		hkForm.setAutoIssue(form.getAutoIssue()); // 自动下发设置
		hkForm.setName(exercise.getName());
		Homework hk = hkService.create(hkForm);
		homeworkId = hk.getId();
		// 更新作业统计
		hkStatService.updateAfterPublishHomework(form.getCreateId(), form.getHomeworkClassIds(), null, hk.getStatus());
		// 保存作业新知识点表
		if (homeworkId > 0 && CollectionUtils.isNotEmpty(form.getKnowledgePoints())) {
			for (Long knowledgeCode : form.getKnowledgePoints()) {
				HomeworkKnowledge hkd = new HomeworkKnowledge();
				hkd.setHomeworkId(homeworkId);
				hkd.setKnowledgeCode(knowledgeCode);
				homeworkKnowledgeRepo.save(hkd);
			}
		}
		return hk;
	}

	@Override
	public CursorPage<Long, HolidayActivity01Homework> findHolidayActivity01HkList(Long activityCode,
			CursorPageable<Long> cursorPageable) {
		return holidayActivity01HomeworkRepo.find("$taskGetByHomeworkIdIsNull", Params.param("code", activityCode))
				.fetch(cursorPageable);
	}

	@Override
	public Map<Long, List<HolidayActivity01ExerciseQuestion>> queryQuestionMap(Collection<Long> exerciseIds) {
		Map<Long, List<HolidayActivity01ExerciseQuestion>> retMap = new HashMap<Long, List<HolidayActivity01ExerciseQuestion>>();
		List<HolidayActivity01ExerciseQuestion> questionlist = holidayActivity01ExerciseQuestionRepo
				.find("$taskQueryHolidayActivity01ExerciseQuestion", Params.param("exerciseIds", exerciseIds)).list();
		for (Long exerciseId : exerciseIds) {
			List<HolidayActivity01ExerciseQuestion> questionList = Lists.newArrayList();
			for (HolidayActivity01ExerciseQuestion question : questionlist) {
				if (question.getHolidayActivity01ExerciseId() == exerciseId.longValue()) {
					questionList.add(question);
				}
			}
			retMap.put(exerciseId, questionList);
		}
		return retMap;
	}

	private BigDecimal getDifficulty(List<Question> qList) {
		double sum = 0.0;
		for (Question q : qList) {
			sum += q.getDifficulty();
		}
		return new BigDecimal(sum / qList.size());
	}

	private List<Long> setKnowledges(List<Long> qIds) {
		Set<Long> knowledgesSet = new HashSet<Long>();
		List<QuestionKnowledge> qks = questionKnowledgeService.findByQuestions(qIds);
		for (QuestionKnowledge qk : qks) {
			knowledgesSet.add(qk.getKnowledgeCode());
		}
		List<Long> knowledgePoints = Lists.newArrayList(knowledgesSet);
		return knowledgePoints;
	}

	@Transactional
	@Override
	public void update(Long id, Long homeworkId) {
		holidayActivity01HomeworkRepo.execute("$taskUpdateHolidayActivity01Homework",
				Params.param("id", id).put("homeworkId", homeworkId));
	}

}
