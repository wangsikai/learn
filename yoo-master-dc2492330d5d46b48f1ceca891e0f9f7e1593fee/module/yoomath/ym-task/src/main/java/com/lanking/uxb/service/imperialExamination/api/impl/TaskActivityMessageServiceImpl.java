package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityMessageLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityMessageService;

/**
 * 活动消息相关接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月7日
 */
@Service
@Transactional(readOnly = true)
public class TaskActivityMessageServiceImpl implements TaskActivityMessageService {
	@Autowired
	@Qualifier("ImperialExaminationActivityMessageLogRepo")
	private Repo<ImperialExaminationActivityMessageLog, Long> repo;

	@Autowired
	@Qualifier("AccountRepo")
	private Repo<Account, Long> accountRepo;

	@Autowired
	@Qualifier("DeviceRepo")
	private Repo<Device, Long> deviceRepo;

	@Override
	public List<ImperialExaminationActivityMessageLog> findMessageLogs(long activityCode) {
		return repo.find("$findMessageLogs", Params.param("activityCode", activityCode)).list();
	}

	@Override
	public List<String> getMobilesFromNotAPPTeacher() {
		return accountRepo.find("$getMobilesFromNotAPPTeacher").list(String.class);
	}

	@Override
	public List<Device> getDeviceFromNotSignUpTeacher() {
		return deviceRepo.find("$getDeviceFromNotSignUpTeacher").list();
	}

	@Override
	public List<Device> getDeviceFromSignUpTeacher(Long code) {
		Params params = Params.param();
		params.put("code", code);
		return deviceRepo.find("$getDeviceFromSignUpTeacher", params).list();
	}

	@Override
	public List<Device> getDeviceFromTeacher() {
		return deviceRepo.find("$getDeviceFromTeacher").list();
	}

	@Override
	@Transactional
	public void saveLogs(List<ImperialExaminationActivityMessageLog> logs) {
		for (ImperialExaminationActivityMessageLog log : logs) {
			if (log.getId() != null) {
				ImperialExaminationActivityMessageLog oldLog = repo.get(log.getId());
				oldLog.setCreateTime(log.getCreateTime());
				oldLog.setSuccess(log.getSuccess());
				repo.save(oldLog);
			} else {
				repo.save(log);
			}
		}
	}

	@Override
	public List<Device> getDeviceFromStudent() {
		return deviceRepo.find("$getDeviceFromStudent").list();
	}

	@Override
	public List<Device> getDeviceFromNotCommitStudent(Long code,ImperialExaminationType type) {
		Params params = Params.param("code", code);
		if (type != null) {
			params.put("type", type.getValue());
		}
		
		return deviceRepo.find("$getDeviceFromNotCommitStudent",params).list();
	}
}
