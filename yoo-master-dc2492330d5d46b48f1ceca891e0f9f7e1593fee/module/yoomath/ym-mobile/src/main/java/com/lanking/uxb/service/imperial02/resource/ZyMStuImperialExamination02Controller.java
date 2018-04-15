package com.lanking.uxb.service.imperial02.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRankStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationHomeworkStudent;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationUserProcess;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityAwardStudentService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityLotteryService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityRankServiceStudent;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityStudentService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationHomeworkStudentService;
import com.lanking.uxb.service.imperial.convert.ImperialExaminationActivityAwardStudentConvert;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivityAward;
import com.lanking.uxb.service.imperial.value.VImperialExaminationScore;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationActivityLottery;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationAwardDatas2;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

import httl.util.CollectionUtils;

/**
 * 科举活动02学生端接口.
 * 
 * @author peng.zhao
 * @version 2017年11月21日
 */
@RestController
@RequestMapping("zy/m/s/imperial02")
public class ZyMStuImperialExamination02Controller {

	@Autowired
	private ImperialExaminationActivityService imperialService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStudentClazzService;
	@Autowired
	private ImperialExaminationActivityStudentService imperialExamActivityStudentService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private ImperialExaminationActivityLotteryService imperialExaminationActivityLotteryService;
	@Autowired
	private ImperialExaminationActivityRankServiceStudent activityRankServiceStudent;
	@Autowired
	private ImperialExaminationHomeworkStudentService homeworkStudentService;
	@Autowired
	private ImperialExaminationActivityAwardStudentService awardStudent;
	@Autowired
	private ImperialExaminationActivityAwardStudentConvert awardStudentConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SchoolService schoolService;

	/**
	 * 获取当前时间对应的阶段，用来判断使用哪一个页面、调哪一个接口
	 * 
	 * @param code
	 * @return 0.报名页,1.活动页,2.颁奖页
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCurretStage", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getCurretStage(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		Map<String, Object> data = new HashMap<String, Object>(1);

		// 0.报名页,1.活动页,2.颁奖页
		int flag = 0;
		// 活动结束时间
		Long endTime_signup = imperialService.get(ImperialExaminationProcess.PROCESS_SIGNUP, code).getEndTime()
				.getTime();
		// 颁奖开始时间
		Long awardTime = imperialService.get(ImperialExaminationProcess.PROCESS_AWARDS, code).getStartTime().getTime();
		// 未到报名截止时间,都在报名页
		if (new Date().getTime() < endTime_signup) {
			// ImperialExaminationActivityStudent activityStudentUser =
			// imperialExamActivityStudentService.getUser(code,
			// Security.getUserId());
			// 如果在报名时间内，用户没有报名进入看到的就是报名页，如果已经报名进来看到的就是活动页
			// if (activityStudentUser == null) {
			// flag = 0;
			// } else {
			// flag = 1;
			// }
			// 报名时间永远处在报名页 戚让改的
			flag = 0;
		} else if (new Date().getTime() > endTime_signup && new Date().getTime() < awardTime) {
			flag = 1;
		} else {
			flag = 2;
		}

		data.put("curretStage", flag);
		return new Value(data);
	}

	/**
	 * 根据当前时间比较，不同阶段获取不同倒计时
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCountdown", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getCountdown(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}
		Map<ImperialExaminationUserProcess, Long> map = imperialService.getCountDownTimeStudent(code, time);

		return new Value(map);
	}

	/**
	 * 获取报名相关数据,查询学生是否报名,是否有班级
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "querySignUpInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value querySignUpInfo(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> data = new HashMap<>();
		ImperialExaminationActivityStudent ieStudent = imperialExamActivityStudentService.getUser(code,
				Security.getUserId());
		if (ieStudent == null) {
			data.put("isSignup", false);
			List<HomeworkStudentClazz> studentClazzs = homeworkStudentClazzService
					.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isNotEmpty(studentClazzs)) {
				data.put("hasClass", true);
			} else {
				data.put("hasClass", false);
			}
		} else {
			data.put("isSignup", true);
		}

		return new Value(data);
	}

	/**
	 * 活动页首页接口<br>
	 * 1.老师查看每一个阶段考试中数据<br>
	 * 2.每一个阶段的成绩公布
	 * 
	 * @param code
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "examData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value examData(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}

		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		ImperialExaminationActivityStudent ieStudent = imperialExamActivityStudentService.getUser(code,
				Security.getUserId());
		for (ImperialExaminationProcessTime time1 : timeList) {
			if (time1.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				if (time1.getEndTime().getTime() > System.currentTimeMillis() && ieStudent == null) {
					// 当前阶段属于报名阶段
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_ACTIVITY_TIME_ERROR));
				}
			}
		}

		Map<String, Object> data = new HashMap<String, Object>();
		// 说明没有报名
		if (ieStudent == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}

		ImperialExaminationType type = ImperialExaminationType.PROVINCIAL_EXAMINATION;
		// 如果当前是报名阶段，已报名跳转到乡试阶段，理解为是乡试阶段准备阶段
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_PROVINCIAL1, code);
		}
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_AWARDS) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, code);
			data.put("isAwards", true);
		} else {
			data.put("isAwards", false);
		}
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_TOTALRANKING) {
			time = imperialService.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, code);
		}
		if (time.getProcess().getParentId() == 1) {
			// 乡试
			type = ImperialExaminationType.PROVINCIAL_EXAMINATION;
		} else if (time.getProcess().getParentId() == 2) {
			// 会试
			type = ImperialExaminationType.METROPOLITAN_EXAMINATION;
		} else if (time.getProcess().getParentId() == 3 || time.getProcess().getParentId() == 4) {
			// 殿试
			type = ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION;
		}

		// @since 11-23 如果当前位于乡试会试公布成绩阶段,公布成绩开始时间加3天后开放下一阶段
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
			long addTime = time.getStartTime().getTime() + 3 * 24 * 60 * 60 * 1000;
			if (System.currentTimeMillis() > addTime) {
				// 会试
				type = ImperialExaminationType.METROPOLITAN_EXAMINATION;
				for (ImperialExaminationProcessTime t : timeList) {
					if (t.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN1) {
						time = t;
						break;
					}
				}
			}
		} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
			long addTime = time.getStartTime().getTime() + 3 * 24 * 60 * 60 * 1000;
			if (System.currentTimeMillis() > addTime) {
				// 殿试
				type = ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION;
				for (ImperialExaminationProcessTime t : timeList) {
					if (t.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1) {
						time = t;
						break;
					}
				}
			}
		}

		data.put("type", type);

		// 对应界面1.准备阶段/2.正在考试/3.批改下发/4.公布成绩
		int process = 1;
		Date processEndTime = null; // 对应着四个的结束时间，页面用来到截止时间刷新
		// 如果当前时间在乡试/会试/殿试的答题时间和批改下发时间范围内，查出题目的属性和学生的答题情况
		if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2) {
				// 正在考试
				process = 2;
				// 作业名称
				String homeworkName = "";
				if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
					homeworkName = "科举大典-乡试试卷";
				} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
					homeworkName = "科举大典-会试试卷";
				} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
					homeworkName = "科举大典-殿试试卷";
				}
				data.put("homeworkName", homeworkName);
			} else {
				// 作业下发批改
				process = 3;
			}
		} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4
				|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4) {
			process = 4;
			ImperialExaminationActivityRankStudent myBestRank = activityRankServiceStudent.queryBest(code, type,
					Security.getUserId());

			BigDecimal rightRate = BigDecimal.ZERO;
			Integer homeworkTime = 0;
			int tag = 1;

			boolean issued = false; // 是否下发 false:未按时下发
			boolean submited = false; // 作业正确率为0或未提交作业 false:未提交作业
			if (myBestRank != null) {
				ImperialExaminationHomeworkStudent activityHomework = homeworkStudentService
						.get(myBestRank.getActivityHomeworkId());
				StudentHomework studentHomework = studentHomeworkService
						.getByHomeworkAndStudentId(activityHomework.getHomeworkId(), Security.getUserId());
				tag = activityHomework.getTag();

				rightRate = studentHomework.getRightRate();
				homeworkTime = studentHomework.getHomeworkTime();
				// 统计出来分数说明满足通过条件
				if (myBestRank.getManualScore() > 0) {
					issued = true;
					submited = true;
				} else {
					if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
						Date homeworkEndTime = new Date();
						// 判断作业下发时间
						for (ImperialExaminationProcessTime processTime : timeList) {
							if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
								if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
									homeworkEndTime = processTime.getEndTime();
								}
							} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
								if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
									homeworkEndTime = processTime.getEndTime();
								}
							} else {
								if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
									homeworkEndTime = processTime.getEndTime();
								}
							}
						}

						if (studentHomework.getIssueAt() == null) {
							issued = false;
						} else {
							if (studentHomework.getIssueAt().before(homeworkEndTime)) {
								issued = true;
							} else {
								issued = false;
							}
						}
					} else {
						issued = false;
						submited = false;
					}

					if (studentHomework.getRightRate() == null
							|| studentHomework.getRightRate().compareTo(BigDecimal.ZERO) == 0) {
						submited = false;
					} else {
						submited = true;
					}
				}
			} else {
				// 没统计数据直接设没下发
				issued = false;
				submited = false;
			}

			data.put("issued", issued);
			data.put("submited", submited);

			UserConvertOption option = new UserConvertOption();
			option.setInitMemberType(true);
			VUser vUser = userConvert.get(Security.getUserId(), option);

			VImperialExaminationScore myBestScore = new VImperialExaminationScore();
			if (myBestRank != null) {
				if (submited) {
					// 本场成绩
					myBestScore.setScore(myBestRank.getManualScore());
					myBestScore.setRank(activityRankServiceStudent.getRank(code, type, myBestRank.getManualScore(),
							myBestRank.getDoTime(), myBestRank.getSubmitAt()));
				}
				String schoolName = "";
				Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
				if (student != null && student.getSchoolId() != null) {
					School school = schoolService.get(student.getSchoolId());
					if (school != null) {
						schoolName = school.getName();
					}
				}
				myBestScore.setSchoolName(schoolName);
				myBestScore.setRightRate(rightRate);
				myBestScore.setHomeworkTime(homeworkTime);

			} else {
				String schoolName = "";
				Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
				if (student != null && student.getSchoolId() != null) {
					School school = schoolService.get(student.getSchoolId());
					if (school != null) {
						schoolName = school.getName();
					}
				}
				myBestScore.setSchoolName(schoolName);
			}
			myBestScore.setStudentName(vUser.getName());
			myBestScore.setImageUrl(vUser.getAvatarUrl());
			myBestScore.setUserId(Security.getUserId());
			myBestScore.setMemberType(vUser.getMemberType());
			myBestScore.setTag(tag);

			// 成绩调整阶段页面不返回排名(页面显示还在统计中)，成绩公布阶段返回排名
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5) {
				// 排名前十
				List<Map> rankList = activityRankServiceStudent.queryTopList(code, type, 10);
				List<Long> userIds = new ArrayList<Long>();
				List<Long> classIds = new ArrayList<Long>();
				List<VImperialExaminationScore> scoreList = new ArrayList<VImperialExaminationScore>();
				for (Map map : rankList) {
					userIds.add(Long.parseLong(map.get("user_id").toString()));
					classIds.add(Long.parseLong(map.get("clazz_id").toString()));
					VImperialExaminationScore v = new VImperialExaminationScore();
					v.setBestClassId(Long.parseLong(map.get("clazz_id").toString()));
					v.setScore(Integer.parseInt(map.get("manual_score").toString()));
					v.setUserId(Long.parseLong(map.get("user_id").toString()));
					v.setHomeworkTime(Integer.parseInt(map.get("do_time").toString()));
					v.setTag(Integer.parseInt(map.get("tag").toString()));
					scoreList.add(v);
				}

				Map<Long, VUser> userMap = userConvert.mget(userIds, option);
				Map<Long, HomeworkClazz> clazzMap = classService.mget(classIds);
				for (VImperialExaminationScore v : scoreList) {
					v.setBestClassName(clazzMap.get(v.getBestClassId()).getName());
					v.setStudentName(userMap.get(v.getUserId()).getName());
					v.setImageUrl(userMap.get(v.getUserId()).getAvatarUrl());
					v.setMemberType(userMap.get(v.getUserId()).getMemberType());
					String schoolName = "";
					Student student = (Student) studentService.getUser(UserType.STUDENT, v.getUserId());
					if (student != null && student.getSchoolId() != null) {
						School school = schoolService.get(student.getSchoolId());
						if (school != null) {
							schoolName = school.getName();
						}
					}
					v.setSchoolName(schoolName);
				}
				data.put("rankList", scoreList);
			} else {
				// 成绩调整阶段rank设置成0
				myBestScore.setRank(0L);
				data.put("rankList", Lists.newArrayList());
			}

			data.put("myBestScore", myBestScore);
		}

		// 根据这个阶段，页面控制显示小贴士、考试提交情况、考试成绩情况
		data.put("process", process);
		processEndTime = imperialService.get(time.getProcess(), code).getEndTime();
		data.put("processEndTime", processEndTime);

		if (process == 4) {
			// 奖券信息 乡试会试殿试的公布成绩阶段才有此返回值
			ImperialExaminationActivityLottery lottery = null;
			// 乡试
			if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
				lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
						ImperialExaminationProcess.PROCESS_PROVINCIAL5, Status.ENABLED);
			} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
				// 会试
				lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
						ImperialExaminationProcess.PROCESS_METROPOLITAN5, Status.ENABLED);
			} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
				// 殿试
				lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
						ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, Status.ENABLED);
			}

			data.put("lottery", lottery);

			// 冲刺题按钮,只有公布成绩阶段才有此按钮
			int dashCount = 0; // 剩余冲刺机会
			// 校验时间
			if (checkSprintTime(activity.getCfg().getTimeList(), type)) {
				List<ImperialExaminationHomeworkStudent> ieHomeworks = homeworkStudentService.list(code, type,
						Security.getUserId(), 2, null);
				if (CollectionUtils.isEmpty(ieHomeworks)) {
					dashCount = 2;
				} else {
					ieHomeworks = homeworkStudentService.list(code, type, Security.getUserId(), 3, null);
					if (CollectionUtils.isEmpty(ieHomeworks)) {
						dashCount = 1;
					}
				}
			}
			data.put("sprintCount", dashCount);
		}

		return new Value(data);
	}

	/**
	 * 根据ImperialExaminationType校验是否超过相应成绩公布时间
	 * 
	 * @param processTimes
	 * @param type
	 */
	private boolean checkSprintTime(List<ImperialExaminationProcessTime> processTimes, ImperialExaminationType type) {
		long now = System.currentTimeMillis(); // 当前时间
		long provincial = 0L;
		long metropolitan = 0L;
		for (ImperialExaminationProcessTime time : processTimes) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
				provincial = time.getEndTime().getTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
				metropolitan = time.getEndTime().getTime();
			}
		}

		boolean result = false;
		if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
			if (now <= provincial) {
				result = true;
			}
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
			if (now <= metropolitan) {
				result = true;
			}
		}

		return result;
	}

	/***
	 * 切换乡试、会试、殿试.为保证代码清晰，和上一个接口分开,返回值一样<br>
	 * 1.当会试的时候可以查看乡试的公布成绩阶段<br>
	 * 2.当殿试的时候可以查看会试和乡试的公布成绩阶段<br>
	 * 3.再调用这个方法之前，需要前端记住默认的阶段是哪一个，最好默认的数据不需要再访问一次接口了
	 * 
	 * @param type
	 * @param code
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "change", method = { RequestMethod.POST, RequestMethod.GET })
	public Value change(ImperialExaminationType type, Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		ImperialExaminationProcessTime time = activity.getCfg().getCurretStage();
		if (time == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_TIME_ERROR));
		}
		// if (time.getProcess().getParentId() == 1) {
		// if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR));
		// } else if (type ==
		// ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
		// }
		// return new Value();
		// } else if (time.getProcess().getParentId() == 2) {
		// if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
		// }
		// } else if (time.getProcess().getParentId() == 0) {
		// if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_METROPOLITAN_AUTHORITY_ERROR));
		// } else if (type ==
		// ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
		// return new Value(new YoomathMobileException(
		// YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_FINAL_AUTHORITY_ERROR));
		// }
		// }

		ImperialExaminationActivityStudent ieStudent = imperialExamActivityStudentService.getUser(code,
				Security.getUserId());
		// 说明没有报名
		if (ieStudent == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", type);
		int process = 4;

		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();

		ImperialExaminationActivityRankStudent myBestRank = activityRankServiceStudent.queryBest(code, type,
				Security.getUserId());

		BigDecimal rightRate = BigDecimal.ZERO;
		Integer homeworkTime = 0;
		int tag = 1;

		boolean issued = false; // 是否下发 false:未按时下发
		boolean submited = false; // 作业正确率为0或未提交作业 false:未提交作业
		if (myBestRank != null) {
			ImperialExaminationHomeworkStudent activityHomework = homeworkStudentService
					.get(myBestRank.getActivityHomeworkId());
			StudentHomework studentHomework = studentHomeworkService
					.getByHomeworkAndStudentId(activityHomework.getHomeworkId(), Security.getUserId());
			rightRate = studentHomework.getRightRate();
			homeworkTime = studentHomework.getHomeworkTime();
			tag = activityHomework.getTag();

			// 统计出来分数说明满足通过条件
			if (myBestRank.getManualScore() > 0) {
				issued = true;
				submited = true;
			} else {
				if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
					Date homeworkEndTime = new Date();
					// 判断作业下发时间
					for (ImperialExaminationProcessTime processTime : timeList) {
						if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
							if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
								homeworkEndTime = processTime.getEndTime();
							}
						} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
							if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
								homeworkEndTime = processTime.getEndTime();
							}
						} else {
							if (processTime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
								homeworkEndTime = processTime.getEndTime();
							}
						}
					}

					if (studentHomework.getIssueAt().before(homeworkEndTime)) {
						issued = true;
					} else {
						issued = false;
					}
				} else {
					issued = false;
					submited = false;
				}

				if (studentHomework.getRightRate() == null
						|| studentHomework.getRightRate().compareTo(BigDecimal.ZERO) == 0) {
					submited = false;
				} else {
					submited = true;
				}
			}
		} else {
			// 没统计数据直接设没下发
			issued = false;
			submited = false;
		}

		data.put("issued", issued);
		data.put("submited", submited);

		UserConvertOption option = new UserConvertOption();
		option.setInitMemberType(true);
		VUser vUser = userConvert.get(Security.getUserId(), option);

		VImperialExaminationScore myBestScore = new VImperialExaminationScore();
		if (myBestRank != null) {
			if (submited) {
				// 本场成绩
				myBestScore.setScore(myBestRank.getManualScore());
				myBestScore.setRank(activityRankServiceStudent.getRank(code, type, myBestRank.getManualScore(),
						myBestRank.getDoTime(), myBestRank.getSubmitAt()));
			}

			String schoolName = "";
			Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
			if (student != null && student.getSchoolId() != null) {
				School school = schoolService.get(student.getSchoolId());
				if (school != null) {
					schoolName = school.getName();
				}
			}
			myBestScore.setSchoolName(schoolName);
			myBestScore.setRightRate(rightRate);
			myBestScore.setHomeworkTime(homeworkTime);

		} else {
			String schoolName = "";
			Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
			if (student != null && student.getSchoolId() != null) {
				School school = schoolService.get(student.getSchoolId());
				if (school != null) {
					schoolName = school.getName();
				}
			}
			myBestScore.setSchoolName(schoolName);
		}
		myBestScore.setStudentName(vUser.getName());
		myBestScore.setImageUrl(vUser.getAvatarUrl());
		myBestScore.setUserId(Security.getUserId());
		myBestScore.setMemberType(vUser.getMemberType());
		myBestScore.setTag(tag);

		data.put("myBestScore", myBestScore);

		// 排名前十
		List<Map> rankList = activityRankServiceStudent.queryTopList(code, type, 10);
		List<Long> userIds = new ArrayList<Long>();
		List<Long> classIds = new ArrayList<Long>();
		List<VImperialExaminationScore> scoreList = new ArrayList<VImperialExaminationScore>();
		for (Map map : rankList) {
			userIds.add(Long.parseLong(map.get("user_id").toString()));
			classIds.add(Long.parseLong(map.get("clazz_id").toString()));
			VImperialExaminationScore v = new VImperialExaminationScore();
			v.setBestClassId(Long.parseLong(map.get("clazz_id").toString()));
			v.setScore(Integer.parseInt(map.get("manual_score").toString()));
			v.setUserId(Long.parseLong(map.get("user_id").toString()));
			v.setHomeworkTime(Integer.parseInt(map.get("do_time").toString()));
			v.setTag(Integer.parseInt(map.get("tag").toString()));
			scoreList.add(v);
		}

		Map<Long, VUser> userMap = userConvert.mget(userIds, option);
		Map<Long, HomeworkClazz> clazzMap = classService.mget(classIds);
		for (VImperialExaminationScore v : scoreList) {
			v.setBestClassName(clazzMap.get(v.getBestClassId()).getName());
			v.setStudentName(userMap.get(v.getUserId()).getName());
			v.setImageUrl(userMap.get(v.getUserId()).getAvatarUrl());
			v.setMemberType(userMap.get(v.getUserId()).getMemberType());
			String schoolName = "";
			Student student = (Student) studentService.getUser(UserType.STUDENT, v.getUserId());
			if (student != null && student.getSchoolId() != null) {
				School school = schoolService.get(student.getSchoolId());
				if (school != null) {
					schoolName = school.getName();
				}
			}
			v.setSchoolName(schoolName);
		}
		data.put("rankList", scoreList);

		// 根据这个阶段，页面控制显示小贴士、考试提交情况、考试成绩情况
		data.put("process", process);

		// 奖券信息 乡试会试殿试的公布成绩阶段才有此返回值
		ImperialExaminationActivityLottery lottery = null;
		// 乡试
		if (type == ImperialExaminationType.PROVINCIAL_EXAMINATION) {
			lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
					ImperialExaminationProcess.PROCESS_PROVINCIAL5, Status.ENABLED);
		} else if (type == ImperialExaminationType.METROPOLITAN_EXAMINATION) {
			// 会试
			lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
					ImperialExaminationProcess.PROCESS_METROPOLITAN5, Status.ENABLED);
		} else if (type == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION) {
			// 殿试
			lottery = imperialExaminationActivityLotteryService.getLotteryByUser(code, Security.getUserId(),
					ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5, Status.ENABLED);
		}

		data.put("lottery", lottery);

		// 冲刺题按钮,只有公布成绩阶段才有此按钮
		int dashCount = 0; // 剩余冲刺机会
		// 校验时间
		if (checkSprintTime(activity.getCfg().getTimeList(), type)) {
			List<ImperialExaminationHomeworkStudent> ieHomeworks = homeworkStudentService.list(code, type,
					Security.getUserId(), 2, null);
			if (CollectionUtils.isEmpty(ieHomeworks)) {
				dashCount = 2;
			} else {
				ieHomeworks = homeworkStudentService.list(code, type, Security.getUserId(), 3, null);
				if (CollectionUtils.isEmpty(ieHomeworks)) {
					dashCount = 1;
				}
			}
		}
		data.put("sprintCount", dashCount);

		return new Value(data);
	}

	/**
	 * 分享接口,返回biz和bizId
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "share", method = { RequestMethod.POST, RequestMethod.GET })
	public Value share(Long code) {
		Map<String, Object> data = new HashMap<>();
		data.put("biz", Biz.ACTIVITY);
		data.put("bizId", code);

		return new Value(data);
	}

	/**
	 * 获奖数据.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "awardData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value awardData(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}

		// 获取活动配置
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		// 判断是否报名(未报名也可看颁奖页)
		ImperialExaminationActivityStudent ieStudent = imperialExamActivityStudentService.getUser(code,
				Security.getUserId());

		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_AWARDS) {
				if (time.getStartTime().getTime() > System.currentTimeMillis()) {
					// 未到颁奖时间
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_TIME_ERROR));
				}
			}
		}

		// 获奖页面数据
		VImperialExaminationAwardDatas2 awardDatas = new VImperialExaminationAwardDatas2();

		// 奖品设置
		int num = 6; // 需要取得获奖的人数固定前6个
		List<ImperialExaminationActivityAwardStudent> awardList = awardStudent.awardList(code, num);
		List<VImperialExaminationActivityAward> vawardList = awardStudentConvert.to(awardList);
		if (CollectionUtils.isNotEmpty(vawardList)) {
			List<VImperialExaminationActivityAward> awardLevel1 = Lists.newArrayList();
			List<VImperialExaminationActivityAward> awardLevel2 = Lists.newArrayList();
			List<VImperialExaminationActivityAward> awardLevel3 = Lists.newArrayList();
			// 出现冻结用户情况,需要特殊处理
			int awardCount = 1;
			for (VImperialExaminationActivityAward value : vawardList) {
				// 设置学校名称
				String schoolName = "";
				Student student = (Student) studentService.getUser(UserType.STUDENT, value.getUserId());
				if (student != null && student.getSchoolId() != null) {
					School school = schoolService.get(student.getSchoolId());
					if (school != null) {
						schoolName = school.getName();
					}
				}
				value.setSchoolName(schoolName);

				if (awardCount == 1) {
					awardLevel1.add(value);
				} else if (awardCount == 2 || awardCount == 3) {
					awardLevel2.add(value);
				} else if (awardCount == 4 || awardCount == 5 || awardCount == 6) {
					awardLevel3.add(value);
				}
				awardCount++;
			}

			awardDatas.setAwardLevel1(awardLevel1);
			awardDatas.setAwardLevel2(awardLevel2);
			awardDatas.setAwardLevel3(awardLevel3);
		}

		// 当前用户获奖信息
		if (ieStudent != null) {
			VImperialExaminationActivityAward selfAward = null;
			for (VImperialExaminationActivityAward vAward : vawardList) {
				if (vAward.getUserId() == Security.getUserId()) {
					selfAward = vAward;
				} else {
					vAward.setAwardContact(null);
					vAward.setAwardContactNumber(null);
					vAward.setAwardDeliveryAddress(null);
				}
			}
			if (selfAward == null) {
				// 可能为冻结用户、未获奖用户
				selfAward = awardStudentConvert.to(awardStudent.getByUser(code, Security.getUserId()));
				if (selfAward.getStatus() != Status.ENABLED) {
					selfAward.setRank(0L); // 冻结用户没有排名
				}
			}
			awardDatas.setSelf(selfAward);
		}

		// 当前用户报名信息
		String name = "";
		if (ieStudent != null) {
			Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
			if (student != null) {
				name = student.getName();
			}
		}
		awardDatas.setName(name);

		return new Value(awardDatas);
	}

	/**
	 * 完善中奖联系信息.
	 * 
	 * @param awardId
	 *            获奖排名ID
	 * @param contact
	 *            收货人
	 * @param mobile
	 *            收货人手机
	 * @param address
	 *            收货人地址
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "improve", method = { RequestMethod.POST, RequestMethod.GET })
	public Value improve(Long awardId, String contact, String mobile, String address) {
		if (awardId == null || StringUtils.isBlank(contact) || StringUtils.isBlank(mobile)
				|| StringUtils.isBlank(address)) {
			return new Value(new MissingArgumentException());
		}
		if (contact.length() > 15) {
			// 收货人姓名长度超出限制
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_CONTACT_ERROR));
		}
		if (address.length() > 50) {
			// 收货人地址长度超出限制
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_ADDRESS_ERROR));
		}
		if (mobile.length() != 11 || mobile.charAt(0) != '1') {
			// 收货人手机号码不正确
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_AWARDS_MOBILE_ERROR));
		}

		ImperialExaminationActivityAwardStudent award = awardStudent.get(awardId);
		if (award == null) {
			return new Value(new EntityNotFoundException());
		} else if (award.getUserId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}

		try {
			awardStudent.improve(awardId, contact, mobile, address);
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 抽奖
	 * 
	 * @param code
	 *            活动code
	 * @param lotteryId
	 *            lottery表主键
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "luckyDraw", method = { RequestMethod.POST, RequestMethod.GET })
	public Value luckyDraw(Long code, Long lotteryId) {
		ImperialExaminationActivityStudent ieStudent = imperialExamActivityStudentService.getUser(code,
				Security.getUserId());
		if (ieStudent == null) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IMPERIAL_NOT_SIGNUP_ERROR));
		}
		ImperialExaminationActivityLottery lottery = imperialExaminationActivityLotteryService.luckDrawStudent(code,
				lotteryId, Security.getUserType());
		if (lottery == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> data = new HashMap<String, Object>(1);
		// 1.添加奖券
		VImperialExaminationActivityLottery vLottery = new VImperialExaminationActivityLottery();
		vLottery.setId(lottery.getId());
		vLottery.setCode(lottery.getActivityCode());
		vLottery.setName(lottery.getName());
		data.put("lottery", vLottery);

		return new Value(data);
	}

	/**
	 * 确认抽奖
	 * 
	 * @param lotteryId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "confirmLottery", method = { RequestMethod.POST, RequestMethod.GET })
	public Value confirmLottery(Long lotteryId) {
		if (lotteryId == null) {
			return new Value(new MissingArgumentException());
		}

		ImperialExaminationActivityLottery lottery = imperialExaminationActivityLotteryService
				.confirmLottery(lotteryId);
		if (lottery == null) {
			return new Value(new EntityNotFoundException());
		}

		return new Value();
	}

}
