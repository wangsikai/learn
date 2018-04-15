package com.lanking.uxb.service.imperial02.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityService;
import com.lanking.uxb.service.imperial02.convert.ImperialExaminationActivityConvert2;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationActivity2;

/**
 * 科举活动02公共接口.
 * 
 * @author peng.zhao
 * @version 2017年11月21日
 */
@RestController
@RequestMapping("zy/imperial02")
public class ZyCommonImperialExamination02Controller {

	@Autowired
	private ImperialExaminationActivityService imperialService;
	@Autowired
	private ImperialExaminationActivityConvert2 imperialExaminationActivityConvert;

	/**
	 * 获取活动配置
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getActivityCfg", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getActivityCfg(Long code) {
		ImperialExaminationActivity activity = imperialService.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		VImperialExaminationActivity2 v = imperialExaminationActivityConvert.to(activity);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				v.setSignUpStartTime(time.getStartTime());
				v.setSignUpEndTime(time.getEndTime());
				break;
			}
		}
		v.setTimeList(imperialService.queryExamTime2(code));
		return new Value(v);
	}
}
