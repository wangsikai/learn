package com.lanking.uxb.service.imperialExamination.api.impl.handle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityCfg;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityMessageLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationMessageTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityMessageService;
import com.lanking.uxb.service.message.api.MessageSender;

/**
 * 信息发送.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月7日
 */
@Component
public class ProcessMessageHandle {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TaskActivityMessageService taskActivityMessageService;
	@Autowired
	private MessageSender messageSender;

	public boolean isProcessed(ImperialExaminationActivity activity) {
		return false;
	}
	
	public ImperialExaminationType getType(ImperialExaminationActivity activity,Date processTime){
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		ImperialExaminationProcess process = null;
		Date startTime = null;
		Date endTime = null;
		for (ImperialExaminationProcessTime ptime : timeList) {
			// 获取当前阶段的开始时间和结束时间
			startTime = ptime.getStartTime();
			endTime = ptime.getEndTime();
			if(processTime.compareTo(startTime) >= 0 && processTime.compareTo(endTime) <= 0){
				process = ptime.getProcess();
			}
		}

		if(process != null){
			if(process.getParentId() == 1){
				return ImperialExaminationType.PROVINCIAL_EXAMINATION;
			} else if(process.getParentId() == 2){
				return ImperialExaminationType.METROPOLITAN_EXAMINATION;
			} else if(process.getParentId() == 3){
				return ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION;
			}
		} else {
			//正常不会出现
			logger.error("时间配置错误！"); 
		}
		return null;
	}

	/**
	 * 发送处理.
	 * 
	 * @param activity
	 */
	public void process(ImperialExaminationActivity activity) {
		ImperialExaminationActivityCfg config = activity.getCfg();
		List<ImperialExaminationMessageTime> messageTimeList = config.getMessageList();
		if (CollectionUtils.isEmpty(messageTimeList)) {
			return;
		}
		List<ImperialExaminationMessageTime> teacherList = Lists.newArrayList(); // 教师推送
		List<ImperialExaminationMessageTime> studentList = Lists.newArrayList();
		for (ImperialExaminationMessageTime value : messageTimeList) {
			if (value.getUserScope() == 4 || value.getUserScope() == 6) {
				teacherList.add(value);
			}
			if (value.getUserScope() == 7 || value.getUserScope() == 8) {
				studentList.add(value);
			}
		}
		
		// 发送教师推送
		if (CollectionUtils.isNotEmpty(teacherList)) {
			// 推送时间不配置直接不处理
			if (teacherList.get(0).getStartTime() == null) {
				return;
			}
			Collections.sort(teacherList, new Comparator<ImperialExaminationMessageTime>() {
				@Override
				public int compare(ImperialExaminationMessageTime o1, ImperialExaminationMessageTime o2) {
					if (o1.getStartTime() != null && o2.getStartTime() != null) {
						return o1.getStartTime().compareTo(o2.getStartTime());
					} else {
						return 0;
					}
				}
			});

			String h5page = ""; // 科举活动H5页面路径地址

			// 整理消息发送日志
			List<ImperialExaminationActivityMessageLog> messageLogs = taskActivityMessageService
					.findMessageLogs(activity.getCode());
			Map<String, ImperialExaminationActivityMessageLog> logs = new HashMap<String, ImperialExaminationActivityMessageLog>(
					messageLogs.size());
			for (ImperialExaminationActivityMessageLog log : messageLogs) {
				logs.put(log.getMessageType().getValue() + "_" + log.getMessageTemplateCode(), log);
			}

			List<ImperialExaminationActivityMessageLog> saveLogs = new ArrayList<ImperialExaminationActivityMessageLog>();
			for (ImperialExaminationMessageTime time : teacherList) {
				ImperialExaminationActivityMessageLog log = logs
						.get(time.getMessageType().getValue() + "_" + time.getMessageTemplateCode());
				if (log != null && log.getSuccess() != null && log.getSuccess()) {
					continue;
				}

				if (Math.abs(System.currentTimeMillis() - time.getStartTime().getTime()) >= 30 * 1000 * 2) {
					continue;
				}

				MessagePacket messagePacket = null;
				if (time.getMessageType() == MessageType.PUSH) {
					List<Device> devices = null;
					if (time.getUserScope() == 6) {
						// 推送，所有渠道初中教师
						devices = taskActivityMessageService.getDeviceFromTeacher();
					} else if (time.getUserScope() == 4) {
						// 推送，已报名的渠道初中教师
						devices = taskActivityMessageService.getDeviceFromSignUpTeacher(activity.getCode());
					}

					if (time.getMessageTemplateCode().equals(12000034)) {
						h5page = Env.getString("activity.imperial.h5.url2.prize.url",
								new Object[] { activity.getCode() });
					} else {
						h5page = Env.getString("activity.imperial.h5.url2", new Object[] { activity.getCode() });
					}
					if (CollectionUtils.isNotEmpty(devices)) {
						List<String> tokens = new ArrayList<String>(devices.size());
						for (Device device : devices) {
							tokens.add(device.getToken());
						}
						messagePacket = new PushPacket(Product.YOOMATH, YooApp.MATH_TEACHER, tokens,
								time.getMessageTemplateCode(), new HashMap<String, Object>(), h5page);
					}
				}

				if (log == null) {
					log = new ImperialExaminationActivityMessageLog();
					log.setActivityCode(activity.getCode());
					log.setMessageTemplateCode(time.getMessageTemplateCode());
					log.setMessageType(time.getMessageType());
				}
				log.setCreateTime(new Date());
				if (messagePacket != null) {
					try {
						messageSender.send(messagePacket);
						log.setSuccess(true);
					} catch (Exception e) {
						log.setSuccess(false);
						logger.error("活动消息发送失败！");
					}
				}
				saveLogs.add(log);
			}

			// 存储日志
			if (CollectionUtils.isNotEmpty(saveLogs)) {
				taskActivityMessageService.saveLogs(saveLogs);
			}
		}
		// 发送学生推送
		if (CollectionUtils.isNotEmpty(studentList)) {
			// 推送时间不配置直接不处理
			if (studentList.get(0).getStartTime() == null) {
				return;
			}
			Collections.sort(studentList, new Comparator<ImperialExaminationMessageTime>() {
				@Override
				public int compare(ImperialExaminationMessageTime o1, ImperialExaminationMessageTime o2) {
					if (o1.getStartTime() != null && o2.getStartTime() != null) {
						return o1.getStartTime().compareTo(o2.getStartTime());
					} else {
						return 0;
					}
				}
			});

			// 科举活动学生H5页面路径地址
			String h5page = null;
			// 整理消息发送日志
			List<ImperialExaminationActivityMessageLog> messageLogs = taskActivityMessageService
					.findMessageLogs(activity.getCode());
			Map<String, ImperialExaminationActivityMessageLog> logs = new HashMap<String, ImperialExaminationActivityMessageLog>(
					messageLogs.size());
			for (ImperialExaminationActivityMessageLog log : messageLogs) {
				logs.put(log.getMessageType().getValue() + "_" + log.getMessageTemplateCode(), log);
			}
			// 判断消息发送状态
			// String cron =
			// Env.getDynamicString("task.yoomath.imperialexamination.cron");
			Integer index = 0;
			List<ImperialExaminationActivityMessageLog> saveLogs = new ArrayList<ImperialExaminationActivityMessageLog>();
			for (ImperialExaminationMessageTime time : studentList) {
				index++;
				ImperialExaminationActivityMessageLog log = logs
						.get(time.getMessageType().getValue() + "_" + time.getMessageTemplateCode());
				if (log != null && log.getSuccess() != null && log.getSuccess()) {
					continue;
				}
				if (Math.abs(System.currentTimeMillis() - time.getStartTime().getTime()) >= 30 * 1000 * 2) {
					continue;
				}
				
				if (time.getMessageTemplateCode() != 12000034) {
					h5page = Env.getString("activity.imperial.h5.student.activity.url", new Object[] { activity.getCode() });
				} else {
					h5page = Env.getString("activity.imperial.h5.student.prize.url", new Object[] { activity.getCode() });
				}
				
				MessagePacket messagePacket = null;

				if (time.getMessageType() == MessageType.PUSH) {
					List<Device> devices = null;
					if (time.getUserScope() == 7) {
						// 推送，所有初中学生
						devices = taskActivityMessageService.getDeviceFromStudent();
					} else if (time.getUserScope() == 8) {
						ImperialExaminationType type = ImperialExaminationType.PROVINCIAL_EXAMINATION;
						if (time.getMessageTemplateCode().equals(12000035)) {
							type = ImperialExaminationType.PROVINCIAL_EXAMINATION;
						} else if (time.getMessageTemplateCode().equals(12000036)) {
							type = ImperialExaminationType.METROPOLITAN_EXAMINATION;
						} else if (time.getMessageTemplateCode().equals(12000037)) {
							type = ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION;
						}
						// 推送，未提交作业的学生
						devices = taskActivityMessageService.getDeviceFromNotCommitStudent(activity.getCode(),type);
					}
					if (CollectionUtils.isNotEmpty(devices)) {
						List<String> tokens = new ArrayList<String>(devices.size());
						for (Device device : devices) {
							tokens.add(device.getToken());
						}
						messagePacket = new PushPacket(Product.YOOMATH, YooApp.MATH_STUDENT, tokens,
								time.getMessageTemplateCode(), new HashMap<String, Object>(), h5page);
					}
				}

				if (log == null) {
					log = new ImperialExaminationActivityMessageLog();
					log.setActivityCode(activity.getCode());
					log.setMessageTemplateCode(time.getMessageTemplateCode());
					log.setMessageType(time.getMessageType());
				}
				log.setCreateTime(new Date());
				if (messagePacket != null) {
					try {
						messageSender.send(messagePacket);
						log.setSuccess(true);
					} catch (Exception e) {
						log.setSuccess(false);
						logger.error("活动消息发送失败！"+e);
					}
				}
				saveLogs.add(log);
			}
			// 存储日志
			if (CollectionUtils.isNotEmpty(saveLogs)) {
				taskActivityMessageService.saveLogs(saveLogs);
			}
		}
	}
}
