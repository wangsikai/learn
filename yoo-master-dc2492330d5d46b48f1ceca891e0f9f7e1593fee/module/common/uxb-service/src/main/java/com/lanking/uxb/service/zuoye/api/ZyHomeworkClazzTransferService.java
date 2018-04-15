package com.lanking.uxb.service.zuoye.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzTransfer;

/**
 * 班级转让相关接口
 * 
 * @author wangsenhao
 *
 */
public interface ZyHomeworkClazzTransferService {

	/**
	 * 转让班级,每次转让都会产生新的数据
	 * 
	 * @param classId
	 * @param from0
	 *            原始老师
	 * @param to
	 *            转让给的老师
	 */
	void classTransfer(long classId, long from0, long to);

	/**
	 * 查询用户
	 * 
	 * @param userNameStr
	 * @param mobile
	 * @param schoolId
	 * @param createId
	 *            当前班级对应的老师
	 * @return
	 */
	List<Long> findUserList(String userNameStr, String mobile, long schoolId, Long createId);

	/**
	 * 获取最新的(没有读过的)一条转让记录
	 * 
	 * @param userId
	 * @return
	 */
	HomeworkClazzTransfer getLastest(long userId);

	/**
	 * 更新已读信息，<=id的转让信息都标记为已读
	 * 
	 * @param id
	 * @param userId
	 */
	void read(long id, long userId);

}
