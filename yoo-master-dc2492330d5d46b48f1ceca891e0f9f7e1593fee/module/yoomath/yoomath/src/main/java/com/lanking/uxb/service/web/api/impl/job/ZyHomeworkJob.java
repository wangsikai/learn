package com.lanking.uxb.service.web.api.impl.job;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserSettings;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkOperationType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserSettingsService;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkOperationLogService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 作业监控任务
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月18日
 */
public class ZyHomeworkJob implements SimpleJob {

	private Logger logger = LoggerFactory.getLogger(ZyHomeworkJob.class);

	private static final int SIZE = 500;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private ZyHomeworkService hkService;
	@Autowired
	private ZyStudentHomeworkStatService stuHkStatService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private ZyCorrectUserService zyCorrectUserService;
	@Autowired
	private UserSettingsService userSettingsService;
	@Autowired
	private ZyHomeworkStatService hkStatService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZyStudentHomeworkService zyStuHkService;
	@Autowired
	private ZyHomeworkOperationLogService hkLogService;
	@Autowired
	private ZyWXMessageService zyWXMessageService;
	@Autowired
	private ZyHomeworkStatisticService hkStatisticService;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private ZyHomeworkQuestionService zyHomeworkQuestionService;
	@Autowired
	private ZyHomeworkClassGroupStudentService zyHomeworkClassGroupStudentService;
	@Autowired
	private CorrectProcessor correctProcessor;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;

	/**
	 * 自动发布作业和自动强制提交作业(涉及作业状态的改变和自动批改)
	 * 
	 * @since 2.1
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 */
	@Override
	public void execute(ShardingContext shardingContext) {
		publishHomeworkTask();
		commitHomeworkTask();

		// @since 小悠快批，2018-2-26，废弃
		// afterLastCommitStatTask();

		beforeCommitTask();

		// @since 小悠快批，2018-2-26，废弃
		// autoIssueTask();

		// @since 小悠快批，2018-2-26，学生作业全部批改完成后的处理逻辑（自动+人工+教师）
		afterAllStudentHomeworkCorrectComplete();
		delayDeadlineTask();

		// @since 小悠快批，处理那些因异常处于批改中或者未批改的作业
		checkStudentHomeworkTask();
	}

	public void beforeCommitTask() {
		int index = 1;
		Page<Homework> page = hkService.queryPublishHomework(P.index(index, SIZE));
		while (page.isNotEmpty()) {
			// 批量获取setting数据
			Set<Long> teacherIds = new HashSet<Long>();
			for (Homework s : page) {
				teacherIds.add(s.getCreateId());
			}
			Map<Long, UserSettings> settingMap = userSettingsService.safeGets(teacherIds);

			for (Homework s : page) {
				if (settingMap.get(s.getCreateId()) != null && settingMap.get(s.getCreateId()).isHomeworkSms()) {
					// 需要提醒
					zyCorrectUserService.noticeUserBeforeAutoCommit(s);
				}
				long remaining = s.getDistributeCount().longValue() - s.getCommitCount().longValue();
				if (remaining > 0 && s.getDeadline().getTime() - 3600000 < System.currentTimeMillis()) {
					if (hkLogService.log(HomeworkOperationType.PUSH2STUDENT_BEFORE1HOUR, s.getId()) != null) {
						// 推送消息给学生
						mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH,
								MqYoomathPushRegistryConstants.RK_YM_PUSH,
								MQ.builder().data(new PushHandleForm(PushHandleAction.OVERDUE_HOMEWORK, s.getId()))
										.build());
					}
				}
			}
			index++;
			page = hkService.queryPublishHomework(P.index(index, SIZE));
		}
	}

	/**
	 * @since 小悠快批，2018-2-26，新的批改流程中，由于新加入“小悠快批”系统，学生批改完毕不能靠LastCommitAt字段判断
	 */
	@Deprecated
	public void afterLastCommitStatTask() {
		int index = 1;
		Page<Homework> page = hkService.queryAfterLastCommitStat(P.index(index, SIZE));
		while (page.isNotEmpty()) {
			// 批量获取账户及设置
			Set<Long> teacherIds = new HashSet<Long>();
			for (Homework s : page) {
				teacherIds.add(s.getCreateId());
			}
			Map<Long, Account> accountMap = accountService.getAccountByUserIds(teacherIds);
			Map<Long, UserSettings> settingMap = userSettingsService.safeGets(teacherIds);
			Map<Long, UserInfo> users = teacherService.getUserInfos(UserType.TEACHER, teacherIds);

			for (Homework s : page) {
				if (System.currentTimeMillis() > s.getLastCommitAt().getTime()
						+ Env.getInt("homework.allcommit.then") * 60 * 1000) {

					// zyHomeworkStatisticService.staticAfterLastCommit(s.getId());

					// 更新作业最后一个提交状态
					hkService.udpateAfterLastCommitStat(s.getId());

					// 通知老师
					if (Env.getBoolean("homework.sms.enable") && settingMap.get(s.getCreateId()).isHomeworkSms()) {
						Account account = accountMap.get(s.getCreateId());
						if (StringUtils.isNotBlank(account.getMobile())) {
							Teacher teacher = (Teacher) users.get(s.getCreateId());
							messageSender.send(new SmsPacket(account.getMobile(), 10000026,
									ValueMap.value("teacherName", teacher.getName())
											.put("className", zyHkClassService.get(s.getHomeworkClassId()).getName())
											.put("homeworkName", s.getName()).put("commitCount", s.getCommitCount())));
						}
					}
					// 推送消息
					mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
							MQ.builder().data(new PushHandleForm(PushHandleAction.HOMEWORK_TO_BE_CORRECT, s.getId()))
									.build());
				}
			}
			index++;
			page = hkService.queryAfterLastCommitStat(P.index(index, SIZE));
		}

	}

	/**
	 * 自动发布作业
	 * 
	 * @since 2.1
	 */
	public void publishHomeworkTask() {
		Date now = new Date();
		CursorPage<Long, Homework> page = homeworkService.queryShouldPublish(now, CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (Homework homework : page) {
				logger.info("auto publish homework start {}", JSON.toJSON(homework).toString());
				homeworkService.publishHomework(homework.getId());
				List<Long> studentIds = Lists.newArrayList();
				if (homework.getHomeworkClassGroupId() != null && homework.getHomeworkClassGroupId() != 0) {
					studentIds = zyHomeworkClassGroupStudentService.findGroupStudents(homework.getHomeworkClassId(),
							homework.getHomeworkClassGroupId());
				} else {
					studentIds = zyHkStuClazzService.listClassStudents(homework.getHomeworkClassId());
				}
				for (Long studentId : studentIds) {
					stuHkStatService.updateAfterPublishHomework(studentId, homework.getHomeworkClassId());
				}
				hkStatService.updateAfterPublishHomework(homework.getCreateId(), homework.getHomeworkClassId(),
						HomeworkStatus.INIT, HomeworkStatus.PUBLISH);
				// 推送消息给学生
				mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
						MQ.builder().data(new PushHandleForm(PushHandleAction.NEW_HOMEWORK, homework.getId())).build());
				JSONObject jo = new JSONObject();
				jo.put("teacherId", homework.getCreateId());
				jo.put("homeworkId", homework.getId());
				mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
						MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH, MQ.builder().data(jo).build());
				logger.info("auto publish homework end {}", homework.getName());
			}
			page = homeworkService.queryShouldPublish(now, CP.cursor(page.getLast().getId(), SIZE));
		}
	}

	/**
	 * 自动强制提交作业(涉及作业状态的改变和自动批改)
	 * 
	 * @since 2.1
	 */
	public void commitHomeworkTask() {
		Date now = new Date();
		CursorPage<Long, Homework> page = homeworkService.queryShouldCommit(now, CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (Homework homework : page) {
				// push msg to not commit student
				List<Long> studentIds = zyStuHkService.listNotCommitStudent(homework.getId());
				if (CollectionUtils.isNotEmpty(studentIds)) {
					mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
							MQ.builder().data(new PushHandleForm(PushHandleAction.FORCESUBMITTED_HOMEWORK,
									homework.getId(), studentIds)).build());
				}
				logger.info("auto commit start [{}] student homework", JSON.toJSON(homework).toString());
				int ret = hkService.commitHomework(homework.getId());

				// 强制提交作业后的批改流程
				correctProcessor.afterForceCommitHomework(homework.getId());

				// 异步通知工作人员
				zyCorrectUserService.asyncNoticeUserAfterAutoCommit(homework.getId(), ret);
				logger.info("auto commit end [{}] student homework", ret);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("homeworkId", homework.getId());
				jsonObject.put("type", InteractionType.SERIES_NOTSUBMIT_STU);
			}
			page = homeworkService.queryShouldCommit(now, CP.cursor(page.getLast().getId(), SIZE));
		}
	}

	/**
	 * 自动下发作业.<br>
	 * 1、全部选择题的作业，截止时间下发<br>
	 * 2、非全部选择题的作业，截止时间超过24小时（配置文件定义）下发
	 * 
	 * @since yoomath v2.3.0
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 */
	@Deprecated
	public void autoIssueTask() {
		Date now = new Date();
		int issueHour = Env.getDynamicInt("homework.issued.time"); // 自动下发小时数
		CursorPage<Long, Homework> page = homeworkService.queryShouldIssue(now, issueHour,
				CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (Homework homework : page) {
				// 判断作业题型设置
				long lastTime = (now.getTime() - homework.getDeadline().getTime()) / 1000;
				int choiceCount = 0;
				List<Question> questions = zyHomeworkQuestionService.findQuestionByHomework(homework.getId());
				for (Question question : questions) {
					if (question.getType() == Question.Type.SINGLE_CHOICE
							|| question.getType() == Question.Type.MULTIPLE_CHOICE) {
						choiceCount++;
					} else if (question.getType() == Question.Type.QUESTION_ANSWERING) {
						// 有简答题的作业不做自动下发
						choiceCount = -1;
						break;
					}
				}
				if (choiceCount == -1) {
					continue;
				}
				boolean needIssue = false;
				if (choiceCount == questions.size() && lastTime <= 10 * 60) {
					// 全部选择题的作业需要在超过截止时间附近（暂定10分钟内）下发
					needIssue = true;
				} else if (choiceCount != questions.size() && lastTime >= issueHour * 3600) {
					// 非全部选择题需要在超过截止时间issueHour小时后下发
					needIssue = true;
				}

				if (needIssue) {
					try {
						// 下发作业
						zyHomeworkService.issue(homework, true);

						// 处理相关统计
						hkStatisticService.staticAfterIssue(homework.getId());
						hkStatisticService.asyncStaticHomework(homework.getId());

						// 发送推送消息
						mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH,
								MqYoomathPushRegistryConstants.RK_YM_PUSH,
								MQ.builder()
										.data(new PushHandleForm(PushHandleAction.ISSUED_HOMEWORK, homework.getId()))
										.build());

						// 微信提醒
						zyWXMessageService.sendIssuedHomeworkMessage(homework.getId());

						logger.info("auto issue homework [{}]", JSON.toJSON(homework).toString());
					} catch (AbstractException e) {
						if (e.getCode() != CoreExceptionCode.NO_PERMISSION_EX && !(e instanceof ZuoyeException)) {
							logger.error("auto issue homework error! {}, {}", homework.getId(), e);
						}
					}
				}
			}
			page = homeworkService.queryShouldPublish(now, CP.cursor(page.getLast().getId(), SIZE));
		}
	}

	/**
	 * 作业内所有学生批改完成后的处理逻辑（包括统计homework正确率等）.
	 * 
	 * @since 小优快批，2018-2-26，替换原autoIssueTask过程
	 */
	private void afterAllStudentHomeworkCorrectComplete() {
		// 可以统计正确率的作业
		CursorPage<Long, Homework> page = homeworkService.queryShouldCalRightRate(CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (Homework homework : page) {
				try {
					// 更新统计
					hkStatService.updateAfterIssueHomework(homework.getCreateId(), homework.getHomeworkClassId());

					// 处理相关统计
					hkStatisticService.staticAfterIssue(homework.getId());
					hkStatisticService.asyncStaticHomework(homework.getId());

					// 统计相关成长值和金币
					coinsService.earn(CoinsAction.TEA_HOMEOWORK_RESULT, homework.getCreateId(), 0, Biz.HOMEWORK,
							homework.getId());
					growthService.grow(GrowthAction.ISSUE_HOMEWORK, homework.getCreateId(), -1, Biz.HOMEWORK,
							homework.getId(), true);
					coinsService.earn(CoinsAction.ISSUE_HOMEWORK, homework.getCreateId(), -1, Biz.HOMEWORK,
							homework.getId());
				} catch (AbstractException e) {
					if (e.getCode() != CoreExceptionCode.NO_PERMISSION_EX && !(e instanceof ZuoyeException)) {
						logger.error("auto issue homework error! {}, {}", homework.getId(), e);
					}
				}
			}
			page = homeworkService.queryShouldCalRightRate(CP.cursor(page.getLast().getId(), SIZE));
		}
	}

	/**
	 * 离作业截止时间30分钟的作业（并且还有学生未提交，提示教师是否推迟截止时间）
	 */
	private void delayDeadlineTask() {
		Date now = new Date();
		CursorPage<Long, Homework> page = homeworkService.queryShouldDelay(now, CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (Homework h : page) {
				if (hkLogService.log(HomeworkOperationType.PUSH2TEACHER_DELAYHKDEADLINE, h.getId()) != null) {
					mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
							MQ.builder().data(new PushHandleForm(PushHandleAction.DELAY_HOMEWORK_DEADLINE, h.getId()))
									.build());
				}
			}

			page = homeworkService.queryShouldDelay(now, CP.cursor(page.getLast().getId(), SIZE));
		}

	}

	/**
	 * 处理因异常处于批改中或者未批改的作业.
	 * 
	 * @since 小优快批，2018-4-3
	 */
	private void checkStudentHomeworkTask() {
		CursorPage<Long, StudentHomework> page = zyStuHkService
				.queryNotCompleteStudentHomework(CP.cursor(Long.MAX_VALUE, 50));
		while (page.isNotEmpty()) {
			correctProcessor.notCompleteStudentHomeworkHandle(page.getItems());
			page = zyStuHkService.queryNotCompleteStudentHomework(CP.cursor(page.getLast().getId(), 50));
		}
	}

}
