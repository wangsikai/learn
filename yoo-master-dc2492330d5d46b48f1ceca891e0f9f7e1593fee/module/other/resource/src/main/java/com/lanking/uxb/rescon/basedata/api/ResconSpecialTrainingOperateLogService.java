package com.lanking.uxb.rescon.basedata.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateLog;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateType;

/**
 * 针对性训练操作日志
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public interface ResconSpecialTrainingOperateLogService {
	/**
	 * 根据针对性训练的ID查询对应的日志(取最新的12条)
	 * 
	 * @param specialTrainingId
	 *            针对性训练的ID
	 * @return
	 */
	List<SpecialTrainingOperateLog> findLogList(Long specialTrainingId);

	/**
	 * 针对性训练操作记录
	 * 
	 * @param specialTrainingId
	 *            针对性训练ID
	 * @param operateType
	 *            操作类型
	 * @param createId
	 *            操作用户ID
	 */
	void saveLog(Long specialTrainingId, SpecialTrainingOperateType operateType, Long createId, String p1);

}
