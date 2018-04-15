package com.lanking.uxb.rescon.error.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.rescon.error.value.VErrorUser;

/**
 * 资源管控台纠错接口
 * 
 * @author wangsenhao
 *
 */
public interface ResconErrorManage {

	/**
	 * 查询纠错
	 * 
	 * @param p
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryErrorQuestion(Pageable p);

	/**
	 * 查询对应题目的纠错情况
	 * 
	 * @param questionId
	 *            纠错题目id
	 * @return
	 */
	List<VErrorUser> queryError(Long questionId);

	/**
	 * 批量查询题目纠错情况
	 * 
	 * @param questionIds
	 * @return
	 */
	Map<Long, List<VErrorUser>> mQueryError(Collection<Long> questionIds);

	/**
	 * 更新纠错题状态.
	 * 
	 * @param questionId
	 */
	void updateStatusByQuestionId(Long questionId, Status status);

	/**
	 * 取未处理的最新的一条纠错
	 * 
	 * @return
	 */
	Map getlatestQuestionError();
}
