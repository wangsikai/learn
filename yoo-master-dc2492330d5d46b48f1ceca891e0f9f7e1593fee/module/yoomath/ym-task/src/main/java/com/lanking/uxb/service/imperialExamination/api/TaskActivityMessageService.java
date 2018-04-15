package com.lanking.uxb.service.imperialExamination.api;

import java.util.List;

import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityMessageLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 活动消息相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月7日
 */
public interface TaskActivityMessageService {

	/**
	 * 获取活动的消息发送日志.
	 * 
	 * @param activityCode
	 *            活动CODE.
	 * @return
	 */
	List<ImperialExaminationActivityMessageLog> findMessageLogs(long activityCode);

	/**
	 * 获取未登录过APP的渠道初中教师用户的电话集合.
	 * 
	 * @return
	 */
	List<String> getMobilesFromNotAPPTeacher();

	/**
	 * 获取渠道未报名初中教师用户的设备.
	 * 
	 * @return
	 */
	List<Device> getDeviceFromNotSignUpTeacher();

	/**
	 * 获取渠道已报名初中教师用户的设备.
	 * 
	 * @return
	 */
	List<Device> getDeviceFromSignUpTeacher(Long code);

	/**
	 * 获取所有渠道初中教师用户的设备.
	 * 
	 * @return
	 */
	List<Device> getDeviceFromTeacher();
	
	/**
	 * 获取所有初中学生用户的设备.
	 * 
	 * @return
	 */
	List<Device> getDeviceFromStudent();
	
	/**
	 * 获取所有未提交作业初中学生用户的设备.
	 * 
	 * @return
	 */
	List<Device> getDeviceFromNotCommitStudent(Long activityCode,ImperialExaminationType type);

	/**
	 * 存储日志.
	 * 
	 * @param logs
	 */
	void saveLogs(List<ImperialExaminationActivityMessageLog> logs);
}
