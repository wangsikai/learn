package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRankStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomework;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityAwardService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityAwardStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStatService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityRankStudentService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationHomeworkStudentService;
import com.lanking.uxb.service.imperialExamination.api.data.ImperialExaminationActivityAwardHelper;

@Service
public class TaskImperialExaminationActivityRankStatServiceImpl implements
		TaskImperialExaminationActivityRankStatService {

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;
	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;
	@Autowired
	private TaskImperialExaminationActivityService activityService;
	@Autowired
	private TaskImperialExaminationHomeworkService activityHomeworkService;
	@Autowired
	private TaskImperialExaminationHomeworkStudentService activityHomeworkStudentService;
	@Autowired
	private TaskImperialExaminationActivityRankService activityRankService;
	@Autowired
	private TaskImperialExaminationActivityRankStudentService activityRankStudentService;
	@Autowired
	private TaskImperialExaminationActivityAwardService awardService;
	@Autowired
	private TaskImperialExaminationActivityAwardStudentService awardStudentService;

	
	//判断当前时间是否已经超过当前考试的批改下发截至时间
	private boolean isOverTime(long code,ImperialExaminationType type,Integer tag){
		ImperialExaminationProcess process = null;
		//乡试
		if(type == ImperialExaminationType.PROVINCIAL_EXAMINATION){
			//首次考试的截止时间是批改下发阶段
			if(tag ==1 ){
				process = ImperialExaminationProcess.PROCESS_PROVINCIAL3;
			} else {
				//冲刺1和冲刺2是在下次考试的准备阶段
				process = ImperialExaminationProcess.PROCESS_METROPOLITAN1;
			}
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION){
			//首次考试的截止时间是批改下发阶段
			if(tag ==1 ){
				process = ImperialExaminationProcess.PROCESS_METROPOLITAN3;
			} else {
				//冲刺1和冲刺2是在下次考试的准备阶段
				process = ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1;
			}
		} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION){
			//殿试没有冲刺1和冲刺2
			process = ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3;
		}
		// 获取活动
		ImperialExaminationActivity activity = activityService.get(code);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		Date endTime = null;
		for (ImperialExaminationProcessTime ptime : timeList) {
			// 批改下发时间
			if (ptime.getProcess() == process) {
				endTime = ptime.getEndTime();
				break;
			}
		}
		
		Date currentTime = new Date();
		if(currentTime.getTime() >= endTime.getTime()) {
			return true;
		}
		
		return false;
	}

	@Transactional
	@Override
	public void countRank(long activityCode, ImperialExaminationType type,Integer lowerTimes,Integer higherTimes) {	  
		//老师的分数需要考虑考场和首次成绩、冲刺成绩的影响
		for(int room = 1; room <= 3; room++){
			for(int tag = lowerTimes;
					tag <= higherTimes;
					tag++){
				
				List<ImperialExaminationHomework> activityHks = activityHomeworkService.query(activityCode, type, room, null, tag);
				List<Long> homeworkIds = new ArrayList<>();
				for (ImperialExaminationHomework hk : activityHks) {
					homeworkIds.add(hk.getHomeworkId());
				}
				//首先查询老师已经记录的成绩
				List<ImperialExaminationActivityRank> teacherRanks =  activityRankService.queryAllTeacherRanks(activityCode, type, tag, room);
				//用来记录已经生成的老师成绩和activityhomeworkid对应关系
				Map<Long, ImperialExaminationActivityRank> recordedRankMap = new HashMap<>();
				for (ImperialExaminationActivityRank rk : teacherRanks) {
					recordedRankMap.put(rk.getActivityHomeworkId(),rk);
				}
				
				Map<Long, Homework> map = homeworkRepo.mget(homeworkIds);
				if (CollectionUtils.isNotEmpty(activityHks)) {
					List<ImperialExaminationActivityRank> ranks = new ArrayList<>();
					for (ImperialExaminationHomework activityHk : activityHks) {
						//如果根据这个activityhomeworkid获取到的老师成绩记录不为空，说明该homework已处理，直接跳过
						if(recordedRankMap.get(activityHk.getId())!=null){
							continue;
						}
						//剩下的都是还没有处理的homework
						ImperialExaminationActivityRank rank = new ImperialExaminationActivityRank();
						rank.setActivityCode(activityCode);
						rank.setActivityHomeworkId(activityHk.getId());
						rank.setType(type);
						rank.setUserId(activityHk.getUserId());
						rank.setTag(tag);
						rank.setRoom(room);
						Homework homework = map.get(activityHk.getHomeworkId());
						if(homework == null){
							continue;
						}
						// 平均提交率*70% + 平均正确率*30%
						if (homework.getStatus() == HomeworkStatus.ISSUED && homework.getDistributeCount() != 0) {
							float submitRate = 1.0f * homework.getCommitCount() / homework.getDistributeCount();
							float rightScore = 0;
							if (homework.getRightRate() != null) {
								rightScore = homework.getRightRate().floatValue() * 0.3f;
							} else {
								rightScore = 0;
							}
							int score = Math.round((submitRate * 70 + rightScore));
							rank.setScore(score);
							rank.setManualScore(score);
						} else if(homework.getStatus() != HomeworkStatus.ISSUED) {
							//如果作业还没有下发，应到作业批改时间截止后，才将成绩显示到后台
							if(isOverTime(activityCode,type,tag)){
								rank.setScore(0);
								rank.setManualScore(0);
							} else {
								//否则直接跳过，不生成记录
								continue;
							}
						}
						if (homework.getStatus() == HomeworkStatus.ISSUED) {
							rank.setDoTime(homework.getHomeworkTime());
						}
						ranks.add(rank);
					}
					activityRankService.save(ranks);
				}
			}
		}
		//学生的分数只考虑首次成绩、冲刺成绩的影响
		for(int tag = lowerTimes;
				tag <= higherTimes;
				tag++){
			List<ImperialExaminationHomeworkStudent> activityHkStus = activityHomeworkStudentService.query(activityCode, type, null, null, tag);
			List<Long> homeworkIds = new ArrayList<>();
			for (ImperialExaminationHomeworkStudent hk : activityHkStus) {
				homeworkIds.add(hk.getHomeworkId());
			}
			//首先查询学生已经记录的成绩
			List<ImperialExaminationActivityRankStudent> studentRanks =  activityRankStudentService.queryAllStudentRanks(activityCode, type, tag);
			
			//用来记录已经生成的学生成绩和activityhomeworkid对应关系
			Map<Long, ImperialExaminationActivityRankStudent> recordedRankMap = new HashMap<>();
			for (ImperialExaminationActivityRankStudent rk : studentRanks) {
				recordedRankMap.put(rk.getActivityHomeworkId(),rk);
			}
			//根据活动代码、考试阶段和考试类型查询学生的作业
			List<StudentHomework> studentHomeworks = studentHomeworkRepo
					.find("$findStuHkImperialHomework", Params.param("code", activityCode).put("type", type.getValue()).put("tag", tag))
					.list();
			Map<String, StudentHomework> map = new HashMap<>();
			for (StudentHomework shk : studentHomeworks) {
				String token = shk.getHomeworkId() + ":" +shk.getStudentId();
				map.put(token, shk);
			}
			if (CollectionUtils.isNotEmpty(activityHkStus)) {
				List<ImperialExaminationActivityRankStudent> ranks = new ArrayList<>();
				for (ImperialExaminationHomeworkStudent activityHk : activityHkStus) {
					ImperialExaminationActivityRankStudent oldRank = recordedRankMap.get(activityHk.getId());
					//如果根据这个 activityhomeworkid获取到的学生成绩记录不为空，说明该homework已处理，直接跳过
					if(oldRank != null ){
						continue;
					}
					ImperialExaminationActivityRankStudent rank = new ImperialExaminationActivityRankStudent();
					rank.setActivityCode(activityCode);
					rank.setActivityHomeworkId(activityHk.getId());
					rank.setType(type);
					rank.setUserId(activityHk.getUserId());
					rank.setTag(tag);
					StudentHomework homework = map.get(activityHk.getHomeworkId() + ":" + activityHk.getUserId());
					if(homework == null){
						continue;
					}
					// 学生的得分等于正确率
					if (homework.getStatus() == StudentHomeworkStatus.ISSUED) {
						float rightScore = 0;
						if (homework.getRightRate() != null) {
							rightScore = homework.getRightRate().floatValue();
						} else {
							rightScore = 0;
						}
						int score = Math.round(rightScore);
						rank.setScore(score);
						rank.setManualScore(score);
					} else {
						//如果作业还没有下发，应到作业批改时间截止后，才将成绩显示到后台
						if(isOverTime(activityCode,type,tag)){
							rank.setScore(0);
							rank.setManualScore(0);
						} else {
							//否则直接跳过，不生成记录
							continue;
						}
					}
					if (homework.getStatus() == StudentHomeworkStatus.ISSUED ) {
						rank.setSubmitAt(homework.getSubmitAt());
						rank.setDoTime(homework.getHomeworkTime());
					}
					ranks.add(rank);
				}
				activityRankStudentService.save(ranks);
			}
		}
		
	}
	
	@Override
	public synchronized void statActivityAward(long activityCode) {
		statTeacherActivityAward(activityCode);
		statStudentActivityAward(activityCode);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public void statTeacherActivityAward(long activityCode) {
		/*最终综合得分（以单个班级为单位）
		= 乡试成绩*20%+会试成绩*30%+殿试成绩*50%     取整，四舍五入
		
		综合得分相同的情况下，班级总平均用时少的得奖
		班级总平均用时 = 【乡试、会试、殿试 单场最高分（冲刺成绩也算）用时相加】/【参加考试场数】
		注：最终综合得分以班级为单位，计算该班级3场中每场的最佳得分
		最终取综合得分 最高的班级作为老师的代表班级，参加最终排名
		
		每名教师仅参加一次排名，若报名了多个班级，以最终表现最佳的班级为准。*/
		
		//老师的排名分考场
		for (int room = 1; room < 3; room++){
			//查询老师的乡试分数及用时
			List<Map> clazzProvincialScores = activityRankService.queryTeacherScoreByClazzId(activityCode,room,ImperialExaminationType.PROVINCIAL_EXAMINATION);
			//查询老师的会试分数及用时
			List<Map> clazzMetropolitanScores = activityRankService.queryTeacherScoreByClazzId(activityCode,room,ImperialExaminationType.METROPOLITAN_EXAMINATION);
			//查询老师的殿试分数及用时
			List<Map> clazzFinalExamScores = activityRankService.queryTeacherScoreByClazzId(activityCode,room,ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION);
			Map<String, ImperialExaminationActivityAwardHelper> classHelperMap = new HashMap<String, ImperialExaminationActivityAwardHelper>();
			//根据班级获取最高的乡试分数
			if (CollectionUtils.isNotEmpty(clazzProvincialScores)) {
				for (Map map : clazzProvincialScores) {
					String classId = map.get("clazz_id") + "";
					//如果没有记录，新生成一个放进去
					if (classHelperMap.get(classId) == null) {
						ImperialExaminationActivityAwardHelper awardHelper = new ImperialExaminationActivityAwardHelper();
						awardHelper.setActivityCode(activityCode);
						awardHelper.setUserId(Long.parseLong(map.get("user_id") + ""));
						awardHelper.setClazzId(Long.parseLong(map.get("clazz_id") + ""));
						awardHelper.setProvincialTime(Integer.parseInt(map.get("do_time") + ""));
						awardHelper.setProvincialScore(Integer.parseInt(map.get("score") + ""));
						classHelperMap.put(classId, awardHelper);
					} else {
						//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新分数和用时
						ImperialExaminationActivityAwardHelper oldAwardHelper = classHelperMap.get(classId);
						Integer newScore = Integer.parseInt(map.get("score") + "");
						Integer oldScore =  oldAwardHelper.getProvincialScore();
						//新分数大于旧分数的时候更新
						if(oldScore == null || newScore > oldScore){
							oldAwardHelper.setProvincialScore(newScore);
							oldAwardHelper.setProvincialTime(Integer.parseInt(map.get("do_time") + ""));
						}
					}
				}
			}
			
			//根据班级获取最高的会试分数
			if (CollectionUtils.isNotEmpty(clazzMetropolitanScores)) {
				for (Map map : clazzMetropolitanScores) {
					String classId = map.get("clazz_id") + "";
					//如果没有记录，新生成一个放进去
					if (classHelperMap.get(classId) == null) {
						ImperialExaminationActivityAwardHelper awardHelper = new ImperialExaminationActivityAwardHelper();
						awardHelper.setActivityCode(activityCode);
						awardHelper.setUserId(Long.parseLong(map.get("user_id") + ""));
						awardHelper.setClazzId(Long.parseLong(map.get("clazz_id") + ""));
						awardHelper.setMetropolitanTime(Integer.parseInt(map.get("do_time") + ""));
						awardHelper.setMetropolitanScore(Integer.parseInt(map.get("score") + ""));
						classHelperMap.put(classId, awardHelper);
					} else {
						//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新分数和用时
						ImperialExaminationActivityAwardHelper oldAwardHelper = classHelperMap.get(classId);
						Integer newScore = Integer.parseInt(map.get("score") + "");
						Integer oldScore =  oldAwardHelper.getMetropolitanScore();
						//新分数大于旧分数的时候更新
						if(oldScore == null || newScore > oldScore){
							oldAwardHelper.setMetropolitanScore(newScore);
							oldAwardHelper.setMetropolitanTime(Integer.parseInt(map.get("do_time") + ""));
						}
					}
				}
			}
			
			//根据班级获取最高的殿试分数
			if (CollectionUtils.isNotEmpty(clazzFinalExamScores)) {
				for (Map map : clazzFinalExamScores) {
					String classId = map.get("clazz_id") + "";
					//如果没有记录，新生成一个放进去
					if (classHelperMap.get(classId) == null) {
						ImperialExaminationActivityAwardHelper awardHelper = new ImperialExaminationActivityAwardHelper();
						awardHelper.setActivityCode(activityCode);
						awardHelper.setUserId(Long.parseLong(map.get("user_id") + ""));
						awardHelper.setClazzId(Long.parseLong(map.get("clazz_id") + ""));
						awardHelper.setFinalExamTime(Integer.parseInt(map.get("do_time") + ""));
						awardHelper.setFinalExamScore(Integer.parseInt(map.get("score") + ""));
						classHelperMap.put(classId, awardHelper);
					} else {
						//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新分数和用时
						ImperialExaminationActivityAwardHelper oldAwardHelper = classHelperMap.get(classId);
						Integer newScore = Integer.parseInt(map.get("score") + "");
						Integer oldScore =  oldAwardHelper.getFinalExamScore();
						//新分数大于旧分数的时候更新
						if(oldScore == null || newScore > oldScore){
							oldAwardHelper.setFinalExamScore(newScore);
							oldAwardHelper.setFinalExamTime(Integer.parseInt(map.get("do_time") + ""));
						}
					}
				}
			}
			
			Map<String, ImperialExaminationActivityAward> classMap = new HashMap<String, ImperialExaminationActivityAward>();
			if (CollectionUtils.isNotEmpty(classHelperMap)) {
				/*把ImperialExaminationActivityAwardHelper转换成数据库对应表实体ImperialExaminationActivityAward
				 * ImperialExaminationActivityAward的分数 = 乡试成绩*20%+会试成绩*30%+殿试成绩*50%     取整，四舍五入
				 * ImperialExaminationActivityAward的用时 = 【乡试、会试、殿试 单场最高分（冲刺成绩也算）用时相加】/【参加考试场数】
				 */
				for (Map.Entry<String, ImperialExaminationActivityAwardHelper> entry : classHelperMap.entrySet()) {
					String classId = entry.getKey();
					ImperialExaminationActivityAwardHelper helper = entry.getValue();
					ImperialExaminationActivityAward award = new ImperialExaminationActivityAward();
					award.setActivityCode(helper.getActivityCode());
					award.setUserId(helper.getUserId());
					
					award.setClazzId(Long.parseLong(classId));
					double dScore = (helper.getProvincialScore() == null ? 0: helper.getProvincialScore() ) * 0.2 + 
							         (helper.getMetropolitanScore() == null ? 0: helper.getMetropolitanScore() )* 0.3 + 
							         (helper.getFinalExamScore() == null ? 0: helper.getFinalExamScore() ) * 0.5;
					//做四舍五入,把小数都舍入掉
					int score = (int)Math.round(dScore);
					int times=0;
					Integer provincialTime = helper.getProvincialTime() == null ? 0:helper.getProvincialTime();
					Integer metropolitanTime = helper.getMetropolitanTime() == null ? 0:helper.getMetropolitanTime();
					Integer finalExamTime = helper.getFinalExamTime() == null ? 0:helper.getFinalExamTime();
					
					if(provincialTime!=0)times++;
					if(metropolitanTime!=0)times++;
					if(finalExamTime!=0)times++;
					Integer doTime;
					if(times != 0) {
						doTime = (provincialTime + metropolitanTime + finalExamTime)/times;
					} else {
						doTime = 0;
					}
					award.setDoTime(doTime);
					award.setScore(score);
					award.setStatus(Status.ENABLED);
					award.setRoom(room);
					classMap.put(classId, award);
			    }
			}
			
			Map<Long, ImperialExaminationActivityAward> userMap = new HashMap<Long, ImperialExaminationActivityAward>();
			if (CollectionUtils.isNotEmpty(classMap)) {
				//获取已经记录的老师排名
				List<ImperialExaminationActivityAward> teacherAwards = awardService.queryRank(activityCode, room);
				Map<Long,ImperialExaminationActivityAward> existAwardMap = new HashMap<>();
				if (CollectionUtils.isNotEmpty(teacherAwards)) {
					for (ImperialExaminationActivityAward tAward : teacherAwards) {
						existAwardMap.put(tAward.getUserId(),tAward);
					}
				}
				//classMap是以班级id来划分的，可能存在这种情况，一个老师有几个班级，需要以老师来划分，取最高分的班级
				for (Map.Entry<String, ImperialExaminationActivityAward> entry : classMap.entrySet()) {
					ImperialExaminationActivityAward award = entry.getValue();
					Long userId = award.getUserId();
					//如果数据库已经有记录，跳过
					if(existAwardMap.get(userId)!=null){
						continue;
					}
					//如果没有记录，放进去
					if (userMap.get(userId) == null) {
						userMap.put(userId, award);
					} else {
						//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新
						ImperialExaminationActivityAward oldAward = userMap.get(userId);
						Integer newScore = award.getScore();
						Integer oldScore =  oldAward.getScore();
						//新分数大于旧分数的时候更新
						if(oldScore == null || newScore > oldScore){
							userMap.put(userId, award);
						}
					}
				}
				awardService.save(userMap.values());
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Transactional
	public void statStudentActivityAward(long activityCode) {
		/*最终综合得分（以单个班级为单位）
		= 乡试成绩*20%+会试成绩*30%+殿试成绩*50%     取整，四舍五入
		
		综合得分相同的情况下，班级总平均用时少的得奖
		班级总平均用时 = 【乡试、会试、殿试 单场最高分（冲刺成绩也算）用时相加】/【参加考试场数】
		注：最终综合得分以班级为单位，计算该班级3场中每场的最佳得分
		最终取综合得分 最高的班级作为老师的代表班级，参加最终排名
		
		每名教师仅参加一次排名，若报名了多个班级，以最终表现最佳的班级为准。*/
		
		//查询学生的乡试分数及用时
		List<Map> clazzProvincialScores = activityRankStudentService.queryStudentScoreByClazzId(activityCode,ImperialExaminationType.PROVINCIAL_EXAMINATION);
		//查询学生的会试分数及用时
		List<Map> clazzMetropolitanScores = activityRankStudentService.queryStudentScoreByClazzId(activityCode,ImperialExaminationType.METROPOLITAN_EXAMINATION);
		//查询学生的殿试分数及用时
		List<Map> clazzFinalExamScores = activityRankStudentService.queryStudentScoreByClazzId(activityCode,ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION);
		Map<String, ImperialExaminationActivityAwardHelper> classHelperMap = new HashMap<String, ImperialExaminationActivityAwardHelper>();
		//获取学生最高的乡试分数
		if (CollectionUtils.isNotEmpty(clazzProvincialScores)) {
			for (Map map : clazzProvincialScores) {
				String classId = map.get("clazz_id") + "";
				String userId = map.get("user_id") + "";
				String token = classId + ":" +userId;
				//如果没有记录，新生成一个放进去
				if (classHelperMap.get(token) == null) {
					ImperialExaminationActivityAwardHelper awardHelper = new ImperialExaminationActivityAwardHelper();
					awardHelper.setActivityCode(activityCode);
					awardHelper.setUserId(Long.parseLong(map.get("user_id") + ""));
					awardHelper.setClazzId(Long.parseLong(map.get("clazz_id") + ""));
					awardHelper.setProvincialTime(Integer.parseInt(map.get("do_time") + ""));
					awardHelper.setProvincialScore(Integer.parseInt(map.get("score") + ""));
					classHelperMap.put(token, awardHelper);
				} else {
					//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新分数和用时
					ImperialExaminationActivityAwardHelper oldAwardHelper = classHelperMap.get(token);
					Integer newScore = Integer.parseInt(map.get("score") + "");
					Integer oldScore =  oldAwardHelper.getProvincialScore();
					//新分数大于旧分数的时候更新
					if(oldScore == null || newScore > oldScore){
						oldAwardHelper.setProvincialScore(newScore);
						oldAwardHelper.setProvincialTime(Integer.parseInt(map.get("do_time") + ""));
					}
				}
			}
		}
		
		//获取学生最高的会试分数
		if (CollectionUtils.isNotEmpty(clazzMetropolitanScores)) {
			for (Map map : clazzMetropolitanScores) {
				String classId = map.get("clazz_id") + "";
				String userId = map.get("user_id") + "";
				String token = classId + ":" +userId;
				//如果没有记录，新生成一个放进去
				if (classHelperMap.get(token) == null) {
					ImperialExaminationActivityAwardHelper awardHelper = new ImperialExaminationActivityAwardHelper();
					awardHelper.setActivityCode(activityCode);
					awardHelper.setUserId(Long.parseLong(map.get("user_id") + ""));
					awardHelper.setClazzId(Long.parseLong(map.get("clazz_id") + ""));
					awardHelper.setMetropolitanTime(Integer.parseInt(map.get("do_time") + ""));
					awardHelper.setMetropolitanScore(Integer.parseInt(map.get("score") + ""));
					classHelperMap.put(token, awardHelper);
				} else {
					//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新分数和用时
					ImperialExaminationActivityAwardHelper oldAwardHelper = classHelperMap.get(token);
					Integer newScore = Integer.parseInt(map.get("score") + "");
					Integer oldScore =  oldAwardHelper.getMetropolitanScore();
					//新分数大于旧分数的时候更新
					if(oldScore == null || newScore > oldScore){
						oldAwardHelper.setMetropolitanScore(newScore);
						oldAwardHelper.setMetropolitanTime(Integer.parseInt(map.get("do_time") + ""));
					}
				}
			}
		}
		
		//获取学生最高的殿试分数
		if (CollectionUtils.isNotEmpty(clazzFinalExamScores)) {
			for (Map map : clazzFinalExamScores) {
				String classId = map.get("clazz_id") + "";
				String userId = map.get("user_id") + "";
				String token = classId + ":" +userId;
				//如果没有记录，新生成一个放进去
				if (classHelperMap.get(token) == null) {
					ImperialExaminationActivityAwardHelper awardHelper = new ImperialExaminationActivityAwardHelper();
					awardHelper.setActivityCode(activityCode);
					awardHelper.setUserId(Long.parseLong(map.get("user_id") + ""));
					awardHelper.setClazzId(Long.parseLong(map.get("clazz_id") + ""));
					awardHelper.setFinalExamTime(Integer.parseInt(map.get("do_time") + ""));
					awardHelper.setFinalExamScore(Integer.parseInt(map.get("score") + ""));
					classHelperMap.put(token, awardHelper);
				} else {
					//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新分数和用时
					ImperialExaminationActivityAwardHelper oldAwardHelper = classHelperMap.get(token);
					Integer newScore = Integer.parseInt(map.get("score") + "");
					Integer oldScore =  oldAwardHelper.getFinalExamScore();
					//新分数大于旧分数的时候更新
					if(oldScore == null || newScore > oldScore){
						oldAwardHelper.setFinalExamScore(newScore);
						oldAwardHelper.setFinalExamTime(Integer.parseInt(map.get("do_time") + ""));
					}
				}
			}
		}
		
		Map<String, ImperialExaminationActivityAwardStudent> classMap = new HashMap<String, ImperialExaminationActivityAwardStudent>();
		if (CollectionUtils.isNotEmpty(classHelperMap)) {
			/*把ImperialExaminationActivityAwardHelper转换成数据库对应表实体ImperialExaminationActivityAward
			 * ImperialExaminationActivityAward的分数 = 乡试成绩*20%+会试成绩*30%+殿试成绩*50%     取整，四舍五入
			 * ImperialExaminationActivityAward的用时 = 【乡试、会试、殿试 单场最高分（冲刺成绩也算）用时相加】/【参加考试场数】
			 */
			for (Map.Entry<String, ImperialExaminationActivityAwardHelper> entry : classHelperMap.entrySet()) {
				String token = entry.getKey();
				ImperialExaminationActivityAwardHelper helper = entry.getValue();
				
				ImperialExaminationActivityAwardStudent award = new ImperialExaminationActivityAwardStudent();
				award.setActivityCode(helper.getActivityCode());
				award.setUserId(helper.getUserId());
				String []tokens = token.split(":");
				award.setClazzId(Long.parseLong(tokens[0]));
				double dScore = (helper.getProvincialScore() == null ? 0: helper.getProvincialScore() ) * 0.2 + 
				         (helper.getMetropolitanScore() == null ? 0: helper.getMetropolitanScore() )* 0.3 + 
				         (helper.getFinalExamScore() == null ? 0: helper.getFinalExamScore() ) * 0.5;
				//做四舍五入,把小数都舍入掉
				int score = (int)Math.round(dScore);
				int times = 0;
				Integer provincialTime = helper.getProvincialTime() == null ? 0:helper.getProvincialTime();
				Integer metropolitanTime = helper.getMetropolitanTime() == null ? 0:helper.getMetropolitanTime();
				Integer finalExamTime = helper.getFinalExamTime() == null ? 0:helper.getFinalExamTime();
				
				if(provincialTime!=0)times++;
				if(metropolitanTime!=0)times++;
				if(finalExamTime!=0)times++;
				
				Integer doTime;
				if(times != 0) {
					doTime = (provincialTime + metropolitanTime + finalExamTime)/times;
				} else {
					doTime = 0;
				}
				award.setDoTime(doTime);
				award.setScore(score);
				award.setStatus(Status.ENABLED);
				
				classMap.put(token, award);
		    }

			
			Map<Long, ImperialExaminationActivityAwardStudent> userMap = new HashMap<Long, ImperialExaminationActivityAwardStudent>();
			if (CollectionUtils.isNotEmpty(classMap)) {
				//获取已经记录的学生排名
				List<ImperialExaminationActivityAwardStudent> studentAwards = awardStudentService.queryRank(activityCode);
				Map<Long,ImperialExaminationActivityAwardStudent> existAwardMap = new HashMap<>();
				if (CollectionUtils.isNotEmpty(studentAwards)) {
					for (ImperialExaminationActivityAwardStudent tAward : studentAwards) {
						existAwardMap.put(tAward.getUserId(),tAward);
					}
				}
				//classMap是以班级id来划分的，可能存在这种情况，一个学生有几个班级，需要以学生来划分，取最高分的班级
				for (Map.Entry<String, ImperialExaminationActivityAwardStudent> entry : classMap.entrySet()) {
					ImperialExaminationActivityAwardStudent award = entry.getValue();
					Long userId = award.getUserId();
					//如果数据库已经有记录，跳过
					if(existAwardMap.get(userId)!=null){
						continue;
					}
					//如果没有记录，放进去
					if (userMap.get(userId) == null) {
						userMap.put(userId, award);
					} else {
						//如果已经有记录，判断新的记录是不是分数比原来的更高，更高的话更新
						ImperialExaminationActivityAwardStudent oldAward = userMap.get(userId);
						Integer newScore = award.getScore();
						Integer oldScore =  oldAward.getScore();
						//新分数大于旧分数的时候更新
						if(oldScore == null || newScore > oldScore){
							userMap.put(userId, award);
						}
					}
				}
				awardStudentService.save(userMap.values());
			}
		}
	}

	@Transactional
	@Override
	public void setAwardRank(List<ImperialExaminationActivityAward> awards, Integer room) {
		if (CollectionUtils.isNotEmpty(awards)) {
			Integer index = 1;
			for (ImperialExaminationActivityAward award : awards) {
				award.setRank(index.longValue());
				award.setRoom(room);
				if (index == 1) {
					// 第一名一等奖
					award.setAwardLevel(1);
				} else if ( index == 2 || index == 3){
					// 第二三名二等奖
					award.setAwardLevel(2);
				} else if ( index >= 4 && index <= 6){
					// 第四五六名三等奖
					award.setAwardLevel(3);
				} else {
					// 其它都为0 ？
					award.setAwardLevel(0);
				}
				index++;
				awardService.save(award);
			}
		}
		
	}
	
	@Transactional
	@Override
	public void setAwardRankStudent(List<ImperialExaminationActivityAwardStudent> awards) {
		if (CollectionUtils.isNotEmpty(awards)) {
			Integer index = 1;
			for (ImperialExaminationActivityAwardStudent award : awards) {
				award.setRank(index.longValue());
				if (index == 1) {
					// 第一名一等奖
					award.setAwardLevel(1);
				} else if ( index == 2 || index == 3){
					// 第二三名二等奖
					award.setAwardLevel(2);
				} else if ( index >= 4 && index <= 6){
					// 第四五六名三等奖
					award.setAwardLevel(3);
				} else {
					// 其它都为0 ？
					award.setAwardLevel(0);
				}
				
				index++;
				awardStudentService.save(award);
			}
		}

	}
}
