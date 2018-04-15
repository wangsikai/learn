package com.lanking.uxb.service.homework.resource;

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

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.AppealType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.homework.form.TeaHomeworkFilterForm2;
import com.lanking.uxb.service.homework.value.VHomeworkPage;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionErrorService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 教师端作业相关接口
 * 
 * @since 1.5.0
 * @author peng.zhao
 * @version 2018年2月6日
 */
@RestController
@RequestMapping("zy/m/t/hk/3")
public class ZyMTeaHomework3Controller {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyQuestionErrorService zyQuestionErrorService;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private QuestionAppealService questionAppealService;
	
	/**
	 * 待分发
	 */
	private static final String HOMEWORK_STATUS_INIT = "INIT";
	/**
	 * 作业中
	 */
	private static final String HOMEWORK_STATUS_PUBLISH = "PUBLISH";
	/**
	 * 待批改
	 */
	private static final String HOMEWORK_STATUS_TODO_CORRECT = "CORRECT";
	/**
	 * 已截止
	 */
	private static final String HOMEWORK_STATUS_CLOSED = "CLOSED";
	
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryFilter", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryFilter() {
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<Map<String, String>> statusList = Lists.newArrayList();
		// 待分发INIT
		Map<String, String> publistMap = new HashMap<>();
		publistMap.put("status", HOMEWORK_STATUS_PUBLISH);
		publistMap.put("statusName", "作业中");
		statusList.add(publistMap);
		Map<String, String> correctMap = new HashMap<>();
		correctMap.put("status", HOMEWORK_STATUS_TODO_CORRECT);
		correctMap.put("statusName", "待批改");
		statusList.add(correctMap);
		Map<String, String> closeMap = new HashMap<>();
		closeMap.put("status", HOMEWORK_STATUS_CLOSED);
		closeMap.put("statusName", "已截止");
		statusList.add(closeMap);
		data.put("homeworkStatus", statusList);

		// 班级
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		data.put("clazzs", clazzs);
		
		// TODO 学年2期实现
		return new Value(data);
	}
	
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(TeaHomeworkFilterForm2 form) {
		int pageNo = form.getPageNo() == null ? 1 : (form.getPageNo().intValue() <= 0 ? 1 : form.getPageNo());
		long endTime = form.getEndTime() == null ? System.currentTimeMillis() : form.getEndTime();
		VHomeworkPage vpage = new VHomeworkPage();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setEndTime(new Date(endTime));
		
		// 作业状态修改 作业中 待批改 已截止
		if (form.getStatus() != null && !"".equals(form.getStatus())) {
			if (HOMEWORK_STATUS_TODO_CORRECT.equals(form.getStatus())) {
				query.setHomeworkStatus(3);
			} else if (HOMEWORK_STATUS_PUBLISH.equals(form.getStatus())) {
				query.setHomeworkStatus(1);
			} else {
				query.setHomeworkStatus(2);
			}
		}
		if (form.getClassId() != null) {
			query.setClassId(form.getClassId());
		}
		if (form.isClassManage()) {
			query.setClassManage(true);
		}
		Page<Homework> homeworkPage = zyHkService.queryForMobile3(query, P.index(pageNo, Math.min(form.getSize(), 20)));
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		if (homeworkPage.isNotEmpty()) {
			List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs);
			Map<Long, VHomeworkClazz> vclazzMap = new HashMap<Long, VHomeworkClazz>(vclazzs.size());

			for (VHomeworkClazz v : vclazzs) {
				vclazzMap.put(v.getId(), v);
			}
			List<VHomework> homeworks = homeworkConvert.to(homeworkPage.getItems());
			
			for (VHomework v : homeworks) {
				v.setHomeworkClazz(vclazzMap.get(v.getHomeworkClazzId()));
				// 修改作业状态newHomeworkStatus
				if ("0".equals(v.getNewHomeworkStatus())) {
					v.setNewHomeworkStatus(HOMEWORK_STATUS_INIT);
					v.setNewHomeworkStatusName("待分发");
				} else if ("1".equals(v.getNewHomeworkStatus())) {
					v.setNewHomeworkStatus(HOMEWORK_STATUS_PUBLISH);
					v.setNewHomeworkStatusName("作业中");
				} else {
					v.setNewHomeworkStatus(HOMEWORK_STATUS_CLOSED);
					v.setNewHomeworkStatusName("已截止");
				}
			}

			vpage.setItems(homeworks);
		} else {
			vpage.setItems(Collections.EMPTY_LIST);
		}
		vpage.setCurrentPage(pageNo);
		vpage.setEndTime(endTime);
		vpage.setPageSize(20);
		vpage.setTotal(homeworkPage.getTotalCount());
		vpage.setTotalPage(homeworkPage.getPageCount());
		// 当前班级
		if (CollectionUtils.isEmpty(clazzs)) {
			vpage.setHasClazz(false);
		} else {
			vpage.setHasClazz(true);
		}

		return new Value(vpage);
	}
	
	/**
	 * 反馈题目错误
	 * 
	 * @since yoomath(mobile) V1.5.0
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questionError", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questionError(@RequestParam(value = "types", required = false) List<QuestionErrorType> types,
			@RequestParam(value = "questionId") Long questionId,
			@RequestParam(value = "description", required = false) String description) {
		zyQuestionErrorService.saveError(description, types, questionId, Security.getUserId());
		return new Value();
	}
	
	/**
	 * 教师端申述题目批改错误
	 * 
	 * @since yoomath(mobile) V1.5.0
	 * @param sHkQuestionId 学生作业题目id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "answerCanComplaint", method = { RequestMethod.POST, RequestMethod.GET })
	public Value answerCanComplaint(@RequestParam(value = "sHkQuestionId") Long sHkQuestionId) {
		StudentHomeworkQuestion question = shqService.get(sHkQuestionId);
		if (question == null) {
			return new Value(new IllegalArgException());
		}
		Date now = new Date();
		// 如果批改时间到现在已经超过168小时，不能申述
		if ((now.getTime() - question.getCorrectAt().getTime()) > 168 * 60 * 60 * 1000) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_ANSWER_APPEAL_EXCEED_TIMES));
		}

		// 如果已经有申述记录了，也不能申述
		QuestionAppeal appeal = questionAppealService.getAppeal(sHkQuestionId);
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_IN_APPEAL));
		} else if (appeal != null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_PROCESSED));
		}

		return new Value();
	}
	
	/**
	 * 学生端申述题目批改错误
	 * 
	 * @since yoomath(mobile) V1.5.0
	 * @param sHkQuestionId 学生作业问题id
	 * @param comment 申述留言
	 * @param source 习题来源:0-作业题,1-错题订正,2-自主练习
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "complaintAnswer", method = { RequestMethod.POST, RequestMethod.GET })
	public Value complaintAnswer(@RequestParam(value = "sHkQuestionId") Long sHkQuestionId,
			@RequestParam(value = "comment") String comment, Integer source) {
		StudentHomeworkQuestion question = shqService.get(sHkQuestionId);
		if (question == null) {
			return new Value(new IllegalArgException());
		}
		Date now = new Date();
		// 如果批改时间到现在已经超过168小时，不能申述
		if ((now.getTime() - question.getCorrectAt().getTime()) > 168 * 60 * 60 * 1000) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_ANSWER_APPEAL_EXCEED_TIMES));
		}

		// 如果已经有申述记录了，也不能申述
		QuestionAppeal appeal = questionAppealService.getAppeal(sHkQuestionId);
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_IN_APPEAL));
		} else if (appeal != null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_PROCESSED));
		}

		questionAppealService.addComment(AppealType.CORRECT_ERROR, sHkQuestionId, source, comment, UserType.TEACHER);
		return new Value();
	}
	
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "test", method = { RequestMethod.POST, RequestMethod.GET })
	public Value test() {
		// TODO 测试接口用
		return new Value();
	}
}
