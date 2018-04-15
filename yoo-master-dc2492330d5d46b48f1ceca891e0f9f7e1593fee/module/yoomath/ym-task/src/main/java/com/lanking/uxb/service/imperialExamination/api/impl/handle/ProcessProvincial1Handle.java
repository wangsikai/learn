package com.lanking.uxb.service.imperialExamination.api.impl.handle;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityProcessLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationExamTag;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationQuestion;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.imperialExamination.api.AbstractImperialExaminationProcessHandle;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityProcessLogService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityUserService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationClassStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationQuestionService;

/**
 * 乡试答题阶段<br>
 * 1.推送通知已报名教师<br>
 * 2.作业自动分发给学生
 * 3.定时查询老师和学生的乡试成绩并排名，这个阶段只会有首次考试的成绩
 */
@Component
public class ProcessProvincial1Handle extends AbstractImperialExaminationProcessHandle {
	@Autowired
	private TaskImperialExaminationActivityUserService activityUserService;
	@Autowired
	private TaskImperialExaminationActivityService activityService;
	@Autowired
	private TaskActivityHomeworkService homeworkService;
	@Autowired
	private TaskImperialExaminationQuestionService questionService;
	@Autowired
	private TaskImperialExaminationActivityProcessLogService activityProcessLogService;
	@Autowired
	private TaskImperialExaminationHomeworkService imperialExaminationQuestionService;
	@Autowired
	private TaskImperialExaminationHomeworkStudentService homeworkStudentService;
	@Autowired
	private TaskImperialExaminationClassStudentService classStudentService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ImperialExaminationProcess getProcess() {
		return ImperialExaminationProcess.PROCESS_PROVINCIAL1;
	}
	
	@Override
	public boolean isPublished(long code) {
		ImperialExaminationActivityProcessLog log = activityProcessLogService.get(code, getProcess(),PROVINCIAL_PUBLISHED);
		if (log != null) {
			return true;
		} 
		
		return false;
	}

	@Override
	public synchronized void process(long code) {
		logger.error("0123456789 000");
		//不是当前阶段，不处理
		if(!isCurrentProcess(code)){
			return;
		}
		logger.error("0123456789 001");
		//判断是否已发布题目，如果没有发布，先发布题目
		if(!isPublished(code)){
			ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
			activityLog.setActivityCode(code);
			activityLog.setProcess(getProcess());
			activityLog.setStartTime(new Date());
			logger.error("0123456789 002");
			// 获取活动
			ImperialExaminationActivity activity = activityService.get(code);
			List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
			//查询某个活动特定阶段的首次考试题目总数，不涉及考场及教材
			List<ImperialExaminationQuestion> questions = questionService.get(code,
					ImperialExaminationType.PROVINCIAL_EXAMINATION,null,null,ImperialExaminationExamTag.FIRST_EXAM);
			Date startTime = null;
			Date endTime = null;
			logger.error("0123456789 003");
			for (ImperialExaminationProcessTime ptime : timeList) {
				// 乡试答题时间
				if (ptime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2) {
					startTime = ptime.getStartTime();
					endTime = ptime.getEndTime();
					break;
				}
			}
			logger.error("0123456789 004");
			if (questions.size() == 0) {
				logger.error("PROVINCIAL_EXAMINATION FIRST EXAM QUESTIONS SIZE IS 0 ！");
				return;
			}
			logger.error("0123456789 005");
			//有两个考场，5种教材，需要循环分发作业
			for(int room = 1; room < 3; room++){
				if(room == 1){
					logger.error("0123456789 006");
					//获取第一考场的所有教材
					List<Integer> firstTextbookCodes = activity.getCfg().getRooms().get(0).getTextbookCategoryCodes(); 
					for(int textbookCode : firstTextbookCodes){
						questions = questionService.get(code,ImperialExaminationType.PROVINCIAL_EXAMINATION,
								                        room,textbookCode,ImperialExaminationExamTag.FIRST_EXAM);
						logger.error("0123456789 007");
						if (questions.size() == 0) {
							logger.error("PROVINCIAL_EXAMINATION CATEGORY" + textbookCode + "FIRST EXAM QUESTIONS SIZE IS 0 ！");
							continue;
						}
						logger.error("0123456789 008");
						publishHomeWork(code, questions, startTime, endTime, room, textbookCode);
					}
				} else {
					//获取第二考场的所有教材
					logger.error("0123456789 009");
					List<Integer> secondTextbookCodes = activity.getCfg().getRooms().get(1).getTextbookCategoryCodes(); 
					for(int textbookCode : secondTextbookCodes){
						questions = questionService.get(code,ImperialExaminationType.PROVINCIAL_EXAMINATION,
		                        room,textbookCode,ImperialExaminationExamTag.FIRST_EXAM);
						logger.error("0123456789 00a");
						if (questions.size() == 0) {
							logger.error("PROVINCIAL_EXAMINATION CATEGORY" + textbookCode + "FIRST EXAM QUESTIONS SIZE IS 0 ！");
							continue;
						}
						logger.error("0123456789 00b");
						publishHomeWork(code, questions, startTime, endTime, room, textbookCode);
					}
				}
				
			}
			logger.error("0123456789 00c");
			activityLog.setProcessData(PROVINCIAL_PUBLISHED);
			activityLog.setSuccess(true);
			activityLog.setEndTime(new Date());
			// 更新日志
			activityProcessLogService.create(activityLog);
		} 
	}

	private void publishHomeWork(long code, List<ImperialExaminationQuestion> questions, Date startTime, Date endTime,
								 Integer room, Integer category) {
		Map<Integer, List<Long>> questionMap = new HashMap<Integer, List<Long>>();
		for (ImperialExaminationGrade grade : ImperialExaminationGrade.values()) {
			List<Long> ids = new ArrayList<Long>();
			questionMap.put(grade.getValue(), ids);
		}
		for (ImperialExaminationQuestion q : questions) {
			for (ImperialExaminationGrade grade : ImperialExaminationGrade.values()) {
				if (q.getGrade() == grade) {
					questionMap.get(grade.getValue()).add(q.getQuestionId());
				}
			}
		}
		// 获取报名用户
		List<ImperialExaminationActivityUser> activtyUser = activityUserService.get(code, room, category);
		if (CollectionUtils.isNotEmpty(activtyUser)) {
			for (ImperialExaminationActivityUser user : activtyUser) {
				for (Long clazzId : user.getClassList()) {
					List<Long> questionIds = questionMap.get(user.getGrade().getValue());
					if (questionIds.size() == 0) {
						logger.error("questions size is 0");
						continue;
					}
					//分发作业		
					Homework hk = homeworkService.publish(user.getUserId(), clazzId, startTime.getTime(),
							endTime.getTime(), questionIds, "科举大典-乡试试题");
					//给老师保存作业
					ImperialExaminationHomework imperialExaminationHomework = new ImperialExaminationHomework();
					
					List<ImperialExaminationHomeworkStudent> imperialExaminationHomeworkStudents = new ArrayList<>();
					//生成老师和学生的作业记录
					generateTeaAndStuHomework(code, room, category, user, clazzId, hk,
							imperialExaminationHomework, imperialExaminationHomeworkStudents);
					// 一个班级一个事务。。。
					saveTeaAndStuHomeWork(imperialExaminationHomework,imperialExaminationHomeworkStudents);
				}
			}
		}
	}

	private void generateTeaAndStuHomework(long code, Integer room, Integer category,
			ImperialExaminationActivityUser user, Long clazzId, Homework hk,
			ImperialExaminationHomework imperialExaminationHomework,
			List<ImperialExaminationHomeworkStudent> imperialExaminationHomeworkStudents) {
		imperialExaminationHomework.setActivityCode(code);
		imperialExaminationHomework.setClazzId(clazzId);
		imperialExaminationHomework.setGrade(user.getGrade());
		imperialExaminationHomework.setHomeworkId(hk.getId());
		imperialExaminationHomework.setType(ImperialExaminationType.PROVINCIAL_EXAMINATION);
		imperialExaminationHomework.setUserId(user.getUserId());
		imperialExaminationHomework.setRoom(room);
		imperialExaminationHomework.setTextbookCategoryCode(category);
		//分发的都是首次
		imperialExaminationHomework.setTag(ImperialExaminationExamTag.FIRST_EXAM.getValue());
		//根据班级id查询出该班级下的所有学生，给学生保存作业
		List<Student> students = classStudentService.query(clazzId);
		if (students.size() == 0) {
			logger.error("PROVINCIAL_EXAMINATION CLASS " + clazzId + " STUDENT SIZE IS 0 ！");
			return;
		}
		for(Student student : students) {
			ImperialExaminationHomeworkStudent imperialExaminationHomeworkStudent = new ImperialExaminationHomeworkStudent();
			imperialExaminationHomeworkStudent.setActivityCode(code);
			imperialExaminationHomeworkStudent.setClazzId(clazzId);
			imperialExaminationHomeworkStudent.setGrade(user.getGrade());
			imperialExaminationHomeworkStudent.setHomeworkId(hk.getId());
			imperialExaminationHomeworkStudent.setType(ImperialExaminationType.PROVINCIAL_EXAMINATION);
			imperialExaminationHomeworkStudent.setUserId(student.getId());
			imperialExaminationHomeworkStudent.setRoom(room);
			imperialExaminationHomeworkStudent.setTextbookCategoryCode(category);
			//分发的都是首次
			imperialExaminationHomeworkStudent.setTag(ImperialExaminationExamTag.FIRST_EXAM.getValue());
			imperialExaminationHomeworkStudents.add(imperialExaminationHomeworkStudent);
		}
	}
	
	@Transactional
	private void saveTeaAndStuHomeWork(ImperialExaminationHomework imperialExaminationHomework,
			                           List<ImperialExaminationHomeworkStudent> imperialExaminationHomeworkStudents){
		imperialExaminationQuestionService.save(imperialExaminationHomework);
		homeworkStudentService.save(imperialExaminationHomeworkStudents);
	}
}
