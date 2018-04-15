package com.lanking.uxb.channelSales.user.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lanking.cloud.domain.support.channelSales.user.ChannelUserOperateLog;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.user.form.CsUserQuery;

/**
 * 渠道商--用户管理
 * 
 * @author wangsenhao
 *
 */
public interface CsUserManageService {

	/**
	 * 查询用户相关信息
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> query(CsUserQuery query, Pageable p);

	/**
	 * 用于导出使用
	 * 
	 * @param query
	 * @return
	 */
	List<Map> query(CsUserQuery query);

	/**
	 * 重置密码(为123456)
	 * 
	 * @param userId
	 */
	void resetPassword(Long userId, Integer channelCode, Long createId);

	/**
	 * 获取日志记录
	 * 
	 * @param userId
	 * @param channelCode
	 * @return
	 */
	List<ChannelUserOperateLog> findLogList(Long userId, Integer channelCode);

	/**
	 * 导出用户
	 * 
	 * @param list
	 * @return
	 */
	HSSFWorkbook exportUserList(List<Map> list);

	/**
	 * 获取老师当前所在班级.
	 * 
	 * @param teacherIds
	 *            教师ID集合
	 * @return
	 */
	Map<Long, List<HomeworkClazz>> queryTeacherHomeworkClazzs(Collection<Long> teacherIds);

	/**
	 * 获取学生当前所在班级.
	 * 
	 * @param studentIds
	 *            学生ID集合
	 * @return
	 */
	Map<Long, List<HomeworkClazz>> queryStudentHomeworkClazzs(Collection<Long> studentIds);
}
