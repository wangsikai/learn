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
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStatService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityUserService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationClassStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationQuestionService;

/**
 * 殿试准备阶段
 * 定时查询老师和学生的会试成绩并排名，这个阶段可能会有首次考试、冲刺1、冲刺2的成绩
 * */
@Component
public class ProcessFinalExamination1Handle extends AbstractImperialExaminationProcessHandle {
	@Autowired
	private TaskImperialExaminationActivityRankStatService imperialExaminationActivityRankStatService;
	@Autowired
	private TaskImperialExaminationActivityProcessLogService activityProcessLogService;
	@Autowired
	private TaskImperialExaminationActivityUserService activityUserService;
	@Autowired
	private TaskImperialExaminationActivityService activityService;
	@Autowired
	private TaskActivityHomeworkService homeworkService;
	@Autowired
	private TaskImperialExaminationQuestionService questionService;
	@Autowired
	private TaskImperialExaminationHomeworkService imperialExaminationQuestionService;
	@Autowired
	private TaskImperialExaminationHomeworkStudentService homeworkStudentService;
	@Autowired
	private TaskImperialExaminationClassStudentService classStudentService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ImperialExaminationProcess getProcess() {
		return ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1;
	}
	
	@Override
	public boolean isPublished(long code) {
		ImperialExaminationActivityProcessLog log = activityProcessLogService.get(code, getProcess(),FINAL_PUBLISHED);
		if (log != null) {
			return true;
		} 
		
		return false;
	}

	@Override
	public synchronized void process(long code) {
		//不是当前阶段，不处理
		if(!isCurrentProcess(code)){
			return;
		}
		//判断是否已发布题目，如果没有发布，先发布题目，如果已发布，查询成绩并排名
		if(!isPublished(code)){
			// 获取活动
			ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
			activityLog.setActivityCode(code);
			activityLog.setProcess(getProcess());
			activityLog.setStartTime(new Date());
			// 获取活动
			ImperialExaminationActivity activity = activityService.get(code);
			List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
			//查询某个活动特定阶段的首次考试题目总数，不涉及考场及教材
			List<ImperialExaminationQuestion> questions = questionService.get(code,
					ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION,null,null,ImperialExaminationExamTag.FIRST_EXAM);
			Date startTime = null;
			Date endTime = null;
			for (ImperialExaminationProcessTime ptime : timeList) {
				// 殿试答题时间
				if (ptime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2) {
					startTime = ptime.getStartTime();
					endTime = ptime.getEndTime();
					break;
				}
			}
			if (questions.size() == 0) {
				logger.error("FINAL_IMPERIAL_EXAMINATION FIRST EXAM QUESTIONS SIZE IS 0 ！");
				return;
			}
			//有两个考场，5种教材，需要循环分发作业
			for(int room = 1; room < 3; room++){
				if(room == 1){
					//获取第一考场的所有教材
					List<Integer> firstTextbookCodes = activity.getCfg().getRooms().get(0).getTextbookCategoryCodes(); 
					for(int textbookCode : firstTextbookCodes){
						questions = questionService.get(code,ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION,
								                        room,textbookCode,ImperialExaminationExamTag.FIRST_EXAM);
						
						if (questions.size() == 0) {
							logger.info("FINAL_IMPERIAL_EXAMINATION CATEGORY" + textbookCode + "FIRST EXAM QUESTIONS SIZE IS 0 ！");
							continue;
						}
						
						publishHomeWork(code, questions, startTime, endTime, room, textbookCode);
					}
				} else {
					//获取第二考场的所有教材
					List<Integer> secondTextbookCodes = activity.getCfg().getRooms().get(1).getTextbookCategoryCodes(); 
					for(int textbookCode : secondTextbookCodes){
						questions = questionService.get(code,ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION,
		                        room,textbookCode,ImperialExaminationExamTag.FIRST_EXAM);
						
						if (questions.size() == 0) {
							logger.info("FINAL_IMPERIAL_EXAMINATION CATEGORY" + textbookCode + "FIRST EXAM QUESTIONS SIZE IS 0 ！");
							continue;
						}
						
						publishHomeWork(code, questions, startTime, endTime, room, textbookCode);
					}
				}
				
			}
			
			activityLog.setProcessData(FINAL_PUBLISHED);
			activityLog.setSuccess(true);
			activityLog.setEndTime(new Date());
			// 更新日志
			activityProcessLogService.create(activityLog);
		} else {
			ImperialExaminationActivityProcessLog activityLog = new ImperialExaminationActivityProcessLog();
			activityLog.setActivityCode(code);
			activityLog.setProcess(getProcess());
			activityLog.setStartTime(new Date());
			try {
				//这个阶段需要生成冲刺1和冲刺2考试的成绩
				imperialExaminationActivityRankStatService.countRank(code, ImperialExaminationType.METROPOLITAN_EXAMINATION,
						ImperialExaminationExamTag.SECOND_EXAM.getValue(),ImperialExaminationExamTag.THIRD_EXAM.getValue());

				activityLog.setSuccess(true);
			} catch (Exception e) {
				logger.error("PROCESS_FINAL_EXAMINATION1 fail:"+e);
				activityLog.setSuccess(false);
			}
			activityLog.setProcessData(PROCESS_SCORE + getProcess());
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
						continue;
					}
					//分发作业		
					Homework hk = homeworkService.publish(user.getUserId(), clazzId, startTime.getTime(),
							endTime.getTime(), questionIds, "科举大典-殿试试题");
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
		imperialExaminationHomework.setType(ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION);
		imperialExaminationHomework.setUserId(user.getUserId());
		imperialExaminationHomework.setRoom(room);
		imperialExaminationHomework.setTextbookCategoryCode(category);
		//分发的都是首次
		imperialExaminationHomework.setTag(ImperialExaminationExamTag.FIRST_EXAM.getValue());
		
		//根据班级id查询出该班级下的所有学生，给学生保存作业
		List<Student> students = classStudentService.query(clazzId);
		if (students.size() == 0) {
			logger.info("FINAL_IMPERIAL_EXAMINATION CLASS " + clazzId + " STUDENT SIZE IS 0 ！");
			return;
		}
		
		for(Student student : students) {
			ImperialExaminationHomeworkStudent imperialExaminationHomeworkStudent = new ImperialExaminationHomeworkStudent();
			imperialExaminationHomeworkStudent.setActivityCode(code);
			imperialExaminationHomeworkStudent.setClazzId(clazzId);
			imperialExaminationHomeworkStudent.setGrade(user.getGrade());
			imperialExaminationHomeworkStudent.setHomeworkId(hk.getId());
			imperialExaminationHomeworkStudent.setType(ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION);
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
