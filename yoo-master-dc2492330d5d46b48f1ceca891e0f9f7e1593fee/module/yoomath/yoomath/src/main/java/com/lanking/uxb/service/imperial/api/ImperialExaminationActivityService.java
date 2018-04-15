package com.lanking.uxb.service.imperial.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationUserProcess;
import com.lanking.uxb.service.imperial.value.VExaminationTime;

/**
 * 科举活动基本信息接口
 * 
 * @author wangsenhao
 *
 */
public interface ImperialExaminationActivityService {

	/**
	 * 临时生成数据的接口，后面删除
	 */
	void createData();

	/**
	 * 获取科举活动对象
	 * 
	 * @param code
	 *            页面链接传过来的活动 code
	 * @return
	 */
	ImperialExaminationActivity getActivity(Long code);

	/**
	 * 倒计时描述<br>
	 * key:距乡试开始 value:倒计时描述
	 * 
	 * @param code
	 * @param time
	 *            当前阶段
	 * @return
	 */
	Map<ImperialExaminationUserProcess, Long> getCountDownTime(Long code, ImperialExaminationProcessTime time);

	/**
	 * 活动时间，对应用户的开始和结束时间
	 * 
	 * @param code
	 * @return
	 */
	List<VExaminationTime> queryExamTime(Long code);

	/**
	 * 获取对应process的ImperialExaminationProcessTime对象
	 * 
	 * @param process
	 * @param code
	 * @return
	 */
	public ImperialExaminationProcessTime get(ImperialExaminationProcess process, long code);

	/**
	 * 活动时间，对应用户的开始和结束时间
	 * 
	 * @param code
	 * @return
	 */
	List<VExaminationTime> queryExamTime2(Long code);

	/**
	 * 倒计时描述<br>
	 * key:距乡试开始 value:倒计时描述
	 * 
	 * @param code
	 * @param time
	 *            当前阶段
	 * @return
	 */
	Map<ImperialExaminationUserProcess, Long> getCountDownTimeStudent(Long code, ImperialExaminationProcessTime time);
}
