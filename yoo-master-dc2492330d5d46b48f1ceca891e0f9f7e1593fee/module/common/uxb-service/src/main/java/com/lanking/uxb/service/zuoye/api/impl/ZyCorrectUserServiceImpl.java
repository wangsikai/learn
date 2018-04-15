package com.lanking.uxb.service.zuoye.api.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.support.console.common.CorrectUser;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserSettingsService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.cache.CorrectUserCacheService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZyCorrectUserServiceImpl implements ZyCorrectUserService {

	@Autowired
	@Qualifier("CorrectUserRepo")
	Repo<CorrectUser, Long> correctUserRepo;

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private CorrectUserCacheService correctUserCacheService;
	@Autowired
	private UserSettingsService userSettingsService;

	private static long HOMEWORK_SMS_INTERVAL = 900000L;
	// 截止时间之前多长时间提醒
	private static long HOMEWORK_BEFORE_AUTOCOMMIT = 600000L;

	@PostConstruct
	void init() {
		HOMEWORK_SMS_INTERVAL = Env.getLong("homework.sms.interval");
	}

	@Override
	public List<String> getAllMobile() {
		List<String> mobiles = correctUserRepo.find("$zyGetAllMobile").list(String.class);
		return mobiles;
	}

	@Async
	@Override
	public void asyncNoticeUser(long homeworkId) {
		if (Env.getBoolean("homework.sms.enable")) {
			Homework hk = hkService.get(homeworkId);
			if (!userSettingsService.safeGet(hk.getCreateId()).isHomeworkSms()) {
				return;
			}
			if (hk.getHomeworkClassId() != null && hk.getHomeworkClassId() > 0) {
				Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, hk.getCreateId());
				String schoolName = "";
				if (teacher.getSchoolId() != null && teacher.getSchoolId().longValue() > 0) {
					schoolName = schoolService.get(teacher.getSchoolId()).getName() + "，";
				}
				String className = zyHkClassService.get(hk.getHomeworkClassId()).getName();

				SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				if (hk.getStartTime().getTime() < System.currentTimeMillis()) {// 已经下发
					messageSender.send(new SmsPacket(getAllMobile(), 10000016,
							ValueMap.value("teacherName", schoolName + className + "，" + teacher.getName() + "老师")
									.put("startTime", formater.format(hk.getStartTime()))
									.put("deadline", formater.format(hk.getDeadline()))));
				} else if (hk.getStartTime().getTime() > System.currentTimeMillis()) {// 还未下发
					messageSender.send(new SmsPacket(getAllMobile(), 10000016,
							ValueMap.value("teacherName", schoolName + className + "，" + teacher.getName() + "老师")
									.put("startTime", formater.format(hk.getStartTime()))
									.put("deadline", formater.format(hk.getDeadline()))));
				}
			}
		}
	}

	@Async
	@Override
	public void asyncNoticeUserAfterCommit(long homeworkId) {
		if (Env.getBoolean("homework.sms.enable")) {
			boolean send = false;
			String smsTag = correctUserCacheService.getSmsTag();
			if (StringUtils.isBlank(smsTag)) {
				send = true;
			} else {
				long time = Long.parseLong(smsTag);
				if (time + HOMEWORK_SMS_INTERVAL < System.currentTimeMillis()) {
					send = true;
				}
			}

			Homework hk = hkService.get(homeworkId);
			if (!userSettingsService.safeGet(hk.getCreateId()).isHomeworkSms()) {
				return;
			}
			Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, hk.getCreateId());

			if (hk.getDistributeCount().longValue() == hk.getCommitCount().longValue()) {
				correctUserCacheService.setSmsTag();
				messageSender.send(
						new SmsPacket(getAllMobile(), 10000018, ValueMap.value("teacherName", teacher.getName())));
			} else {
				if (send) {
					correctUserCacheService.setSmsTag();
					messageSender.send(new SmsPacket(getAllMobile(), 10000017,
							ValueMap.value("teacherName", teacher.getName()).put("commitCount", hk.getCommitCount())
									.put("distributeCount", hk.getDistributeCount())));
				}
			}
		}
	}

	@Async
	@Override
	public void asyncNoticeUserAfterAutoCommit(long homeworkId, int autoCommit) {
		if (Env.getBoolean("homework.sms.enable")) {
			if (autoCommit > 0) {
				Homework hk = hkService.get(homeworkId);
				if (!userSettingsService.safeGet(hk.getCreateId()).isHomeworkSms()) {
					return;
				}
				Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, hk.getCreateId());
				correctUserCacheService.setSmsTag();
				messageSender.send(new SmsPacket(getAllMobile(), 10000019,
						ValueMap.value("teacherName", teacher.getName()).put("autoCommit", autoCommit)));
			}
		}
	}

	@Override
	public void noticeUserBeforeAutoCommit(Homework hk) {
		if (Env.getBoolean("homework.sms.enable")) {
			long remaining = hk.getDistributeCount().longValue() - hk.getCommitCount().longValue();
			if (remaining > 0 && hk.getDeadline().getTime() - HOMEWORK_BEFORE_AUTOCOMMIT < System.currentTimeMillis()) {
				String flag = correctUserCacheService.getSmsBeforeAutoCommitTag(hk.getId());
				if (StringUtils.isBlank(flag)) {
					correctUserCacheService.setSmsTag();
					correctUserCacheService.setSmsBeforeAutoCommitTag(hk.getId());
					Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, hk.getCreateId());
					messageSender.send(new SmsPacket(getAllMobile(), 10000020,
							ValueMap.value("teacherName", teacher.getName()).put("remaining", remaining)));
				}
			}
		}
	}
}
