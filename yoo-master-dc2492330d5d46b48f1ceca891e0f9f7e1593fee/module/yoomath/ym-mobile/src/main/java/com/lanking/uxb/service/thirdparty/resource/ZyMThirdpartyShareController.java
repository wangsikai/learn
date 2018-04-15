package com.lanking.uxb.service.thirdparty.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.thirdparty.ShareLog;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractise;
import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractiseQuestion;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaper;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvertOption;
import com.lanking.uxb.service.holiday.value.VHolidayQuestion;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ShareLogService;
import com.lanking.uxb.service.thirdparty.convert.ShareLogConvert;
import com.lanking.uxb.service.thirdparty.form.ShareForm;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseService;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseService;
import com.lanking.uxb.service.zuoye.api.ZySmartPaperService;

/**
 * 分享相关restAPI接口
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月11日
 */
@RestController
@RequestMapping("zy/m/thirdparty/share")
public class ZyMThirdpartyShareController {

	@Autowired
	private ShareLogService logService;
	@Autowired
	private ShareLogConvert logConvert;
	@Autowired
	private ZyDailyPractiseService dailyPractiseService;
	@Autowired
	private ZyDailyPractiseQuestionService dailyPractiseQuestionService;
	@Autowired
	private ZySmartPaperService smartPaperService;
	@Autowired
	private ZySectionPractiseService sectionPracticeService;
	@Autowired
	private ZySectionPractiseQuestionService sectionPractiseQuestionService;
	@Autowired
	private StudentHomeworkService stuHomeworkService;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HolidayStuHomeworkItemService holidayStuHomeworkItemService;
	@Autowired
	private HolidayHomeworkItemService holidayHomeworkItemService;
	@Autowired
	private HolidayHomeworkItemQuestionService holidayHomeworkItemQuestionService;
	@Autowired
	private QuestionBaseConvert<VHolidayQuestion> questionBaseConvert;
	@Autowired
	private HolidayQuestionConvert holidayQuestionConvert;
	@Autowired
	private UserActionService userActionService;

	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "log", method = { RequestMethod.POST, RequestMethod.GET })
	public Value log(ShareForm form) {
		if (form.getType() == null || form.getBiz() == null) {
			return new Value(new IllegalArgException());
		}
		form.setId(SnowflakeUUID.next());
		form.setUserId(Security.getUserId());
		form.setCreateAt(new Date());

		// 异步保存
		asyncLog(form);

		ShareLog log = new ShareLog();
		log.setId(form.getId());
		log.setUserId(form.getUserId());
		log.setBiz(form.getBiz());
		log.setBizId(form.getBizId());
		log.setType(form.getType());
		log.setTitle(form.getTitle());
		log.setBody(form.getBody());
		log.setUrl(form.getUrl());
		log.setContent(form.getContent());
		log.setCreateAt(form.getCreateAt());
		if (StringUtils.isNotBlank(form.getExtend())) {
			// 解析p0
			JSONObject jo = JSONObject.parseObject(form.getExtend());
			if (jo.containsKey("p0")) {
				log.setP0(jo.getString("p0"));
			}
		}
		return new Value(logConvert.to(log));

	}

	@Async
	void asyncLog(ShareForm form) {
		Map<String, Object> contentMap = Maps.newHashMap();
		List<Map<String, Object>> listMap = Lists.newArrayList();
		if (form.getBiz() == Biz.SMART_PAPER) {// 智能出卷
			SmartExamPaper examPaper = smartPaperService.get(form.getBizId());
			contentMap.put("name", examPaper.getName());
			contentMap.put("rightRate", examPaper.getRightRate());
			contentMap.put("date", examPaper.getCommitAt());
			List<SmartExamPaperQuestion> examPaperQuestions = smartPaperService.queryPaperQuestion(form.getBizId());
			for (SmartExamPaperQuestion q : examPaperQuestions) {
				Map<String, Object> one = new HashMap<String, Object>(2);
				one.put("done", q.isDone());
				one.put("result", q.getResult());
				listMap.add(one);
			}
		} else if (form.getBiz() == Biz.SECTION_EXERCISE) {// 章节练习
			SectionPractise practise = sectionPracticeService.get(form.getBizId());
			contentMap.put("name", practise.getName());
			contentMap.put("rightRate", practise.getRightRate());
			contentMap.put("date", practise.getCommitAt() == null ? practise.getCreateAt() : practise.getCommitAt());
			List<SectionPractiseQuestion> sectionPractiseQuestions = sectionPractiseQuestionService
					.mgetListByPractise(form.getBizId());
			for (SectionPractiseQuestion q : sectionPractiseQuestions) {
				Map<String, Object> one = new HashMap<String, Object>(2);
				one.put("done", q.isDone());
				one.put("result", q.getResult());
				listMap.add(one);
			}
		} else if (form.getBiz() == Biz.DAILY_PRACTICE) {// 每日练
			DailyPractise dailyPractise = dailyPractiseService.get(form.getBizId());
			contentMap.put("name", dailyPractise.getName());
			contentMap.put("rightRate", dailyPractise.getRightRate());
			contentMap.put("date",
					dailyPractise.getCommitAt() == null ? dailyPractise.getCreateAt() : dailyPractise.getCommitAt());
			List<DailyPractiseQuestion> dailyPractiseQuestions = dailyPractiseQuestionService.findByPractise(form
					.getBizId());
			for (DailyPractiseQuestion q : dailyPractiseQuestions) {
				Map<String, Object> one = new HashMap<String, Object>(2);
				one.put("done", q.isDone());
				one.put("result", q.getResult());
				listMap.add(one);
			}
		} else if (form.getBiz() == Biz.HOMEWORK) {// 作业
			StudentHomework studentHomework = stuHomeworkService.getByHomeworkAndStudentId(form.getBizId(),
					form.getUserId());
			if (studentHomework.getStatus() != StudentHomeworkStatus.ISSUED) {
				return;
			}
			Homework hk = hkService.get(form.getBizId());
			contentMap.put("name", hk.getName());
			contentMap.put("rightRate", studentHomework.getRightRate());
			contentMap.put("date", studentHomework.getIssueAt());

			List<Long> qids = hqService.getQuestion(studentHomework.getHomeworkId());
			List<Question> qs = new ArrayList<Question>(qids.size());
			Map<Long, Question> qsMap = questionService.mget(qids);
			for (Long qid : qids) {
				qs.add(qsMap.get(qid));
			}

			List<VQuestion> vs = questionConvert.to(qs,
					new QuestionConvertOption(false, false, false, studentHomework.getId()));

			for (VQuestion v : vs) {
				Map<String, Object> one = new HashMap<String, Object>(2);
				one.put("done", v.isDone());
				one.put("result", v.getStudentHomeworkQuestion().getResult());
				listMap.add(one);
			}
		} else if (form.getBiz() == Biz.HOLIDAY_HOMEWORK) {// 假期作业
			HolidayStuHomeworkItem holidayStuHomeworkItem = holidayStuHomeworkItemService.find(form.getBizId(),
					form.getUserId());
			if (holidayStuHomeworkItem == null || holidayStuHomeworkItem.getStatus() != StudentHomeworkStatus.SUBMITED
					|| holidayStuHomeworkItem.getRightRate() == null) {
				return;
			}
			HolidayHomeworkItem holidayHomeworkItem = holidayHomeworkItemService.get(form.getBizId());
			contentMap.put("name", holidayHomeworkItem.getName());
			contentMap.put("rightRate", holidayStuHomeworkItem.getRightRate());
			contentMap.put("date",
					holidayStuHomeworkItem.getSubmitAt() == null ? new Date() : holidayStuHomeworkItem.getSubmitAt());

			List<Long> questionIds = holidayHomeworkItemQuestionService.queryQuestions(holidayStuHomeworkItem
					.getHolidayHomeworkItemId());
			List<Question> qs = questionService.mgetList(questionIds);
			List<VHolidayQuestion> vqs = questionBaseConvert.to(qs, new QuestionBaseConvertOption(false, true, true,
					true));
			List<VHolidayQuestion> vs = holidayQuestionConvert.to(vqs, new HolidayQuestionConvertOption(
					holidayStuHomeworkItem.getId()));
			for (VHolidayQuestion v : vs) {
				Map<String, Object> one = new HashMap<String, Object>(2);
				one.put("done", v.isDone());
				one.put("result", v.getHolidayStuHomeworkItemQuestion().getResult());
				listMap.add(one);
			}
		} else if (form.getBiz() == Biz.ACTIVITY) {
			contentMap = null;
			// 用户分享动作
			userActionService.action(UserAction.SHARE_LOTTERY_ACTIVITY, form.getUserId(), null);
		}
		if (contentMap != null) {
			contentMap.put("detailList", listMap);
			form.setContent(JSONObject.toJSONString(contentMap));
		}
		if (form.getBiz() == Biz.TEACHERS_DAY) {
			logService.log2(form);
		} else {
			logService.log(form);
		}

	}
}
