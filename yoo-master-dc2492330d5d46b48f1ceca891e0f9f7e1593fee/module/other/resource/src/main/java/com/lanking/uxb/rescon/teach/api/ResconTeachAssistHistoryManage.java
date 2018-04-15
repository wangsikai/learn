package com.lanking.uxb.rescon.teach.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistHistory;

/**
 * 教辅操作历史接口.
 * 
 * @author wlche
 * @version v1.3
 */
public interface ResconTeachAssistHistoryManage {

	/**
	 * 保存操作历史.
	 * 
	 * @param history
	 *            操作历史
	 * @return
	 */
	TeachAssistHistory save(TeachAssistHistory history);

	/**
	 * 获取教辅历史操作信息
	 * 
	 * @param teachAssistId
	 * @return
	 */
	List<TeachAssistHistory> findList(Long teachAssistId);
}
