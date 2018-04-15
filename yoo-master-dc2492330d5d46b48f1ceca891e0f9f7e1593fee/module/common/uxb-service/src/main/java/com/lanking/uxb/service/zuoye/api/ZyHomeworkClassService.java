package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.ClazzFrom;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 作业班级相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月22日
 */
public interface ZyHomeworkClassService {

	HomeworkClazz get(long id);

	Map<Long, HomeworkClazz> mget(Collection<Long> ids);

	List<HomeworkClazz> mgetList(Collection<Long> ids);

	HomeworkClazz findByCode(String code);

	boolean codeExist(String code);

	boolean nameExist(String name, Long teacherId);

	HomeworkClazz create(String name, Long teacherId);

	/**
	 * 从第三方数据创建班级.
	 * 
	 * @since v2.0.1
	 * 
	 * @param name
	 *            班级名称
	 * @param teacherId
	 *            教师ID
	 * @param from
	 *            来源
	 * @param fromCode
	 *            第三方班级编码
	 * @return
	 */
	HomeworkClazz createByThird(String name, Long teacherId, ClazzFrom from, String fromCode);

	HomeworkClazz refreshCode(long id, long teacherId);

	HomeworkClazz close(long id, long teacherId);

	Page<HomeworkClazz> query(ZyHomeworkClassQuery query, Pageable page);

	List<HomeworkClazz> listCurrentClazzs(long teacherId);

	List<HomeworkClazz> listCurrentAllClazzs(long teacherId);

	List<HomeworkClazz> listHistoryClazzs(long teacherId);

	void delete(long id, long teacherId);

	HomeworkClazz updateName(String name, long classId, long teacherId);

	void incrStudentNum(long id, int delta);

	long historyCount(long teacherId);

	long currentCount(long teacherId);

	int lock(long classId, long teacherId);

	int unlock(long classId, long teacherId);

	/**
	 * 根据第三方来源获得班级列表（教师使用）.
	 * 
	 * @param from
	 *            第三方平台.
	 * @param codes
	 *            第三方平台班级CODE
	 * @param teacherId
	 *            教师ID
	 * @return Map<第三方班级CODE, List<HomeworkClazz>>
	 */
	Map<String, List<HomeworkClazz>> findTeaUsedByFromCode(ClazzFrom from, Collection<String> codes);

	/**
	 * 根据第三方班级更新教师.
	 * 
	 * @param from
	 *            第三方平台.
	 * @param fromCode
	 *            第三方平台班级CODE
	 * @param teacherId
	 *            教师ID
	 */
	void updateTeacherByFromCode(ClazzFrom from, String fromCode, Long teacherId);

	/**
	 * 获取班级的已下发的作业平均完成率.
	 * 
	 * @param classIds
	 *            作业ID集合.
	 * @return
	 */
	Map<Long, Double> getMapAVGComplete(Collection<Long> classIds);

	/**
	 * 设置班级教材进度
	 *
	 * @since 2.0.3
	 *
	 * @param classId
	 *            班级id
	 * @param bookId
	 *            书本id
	 * @param bookCataId
	 *            书本章节id
	 * @param userId
	 *            教师id
	 */
	void setBook(long classId, long bookId, long bookCataId, long userId);

	/**
	 * 批量设置班级书本进度
	 *
	 * @param classIds
	 *            班级id列表
	 * @param bookId
	 *            书本id
	 * @param bookCataId
	 *            书本章节id
	 * @param userId
	 *            用户id
	 */
	void setBook(Collection<Long> classIds, long bookId, long bookCataId, long userId);

	/**
	 * 清除教师设置的教辅图书及进度
	 *
	 * @param userId
	 *            教师id
	 */
	void clearBookSetting(long userId);

	/**
	 * 重置班级教辅数据
	 *
	 * @param bookIds
	 *            重置书本列表
	 * @param userId
	 *            用户id
	 */
	void resetBook(Collection<Long> bookIds, long userId);

	/**
	 * 批量获得有效班级list
	 *
	 * @param ids
	 *            id列表
	 * @return {@link List}
	 */
	List<HomeworkClazz> mgetListEnableClazz(Collection<Long> ids);

	/**
	 * 批量获得有效班级Map
	 *
	 * @param ids
	 *            id列表
	 * @return {@link List}
	 */
	Map<Long, HomeworkClazz> mgetEnableClazz(Collection<Long> ids);

	/**
	 * 根据用户id查找此用户同渠道下的学校信息
	 *
	 * @param userId
	 *            用户id
	 * @param key
	 *            查询条件（班级码、教师姓名、班级名称）
	 * @param cursorPageable
	 *            游标分页条件
	 * @return {@link CursorPage}
	 */
	CursorPage<Long, HomeworkClazz> findByChannel(long userId, String key, CursorPageable<Long> cursorPageable);

	/**
	 * 根据班级码或者教师手机进行查询
	 *
	 * @param key
	 *            查询关键字
	 * @return {@link List}
	 */
	List<HomeworkClazz> findByCodeOrMobile(String key);

	/**
	 * 需要申请加入班级
	 * 
	 * @param classId
	 * @param teacherId
	 * @return
	 */
	int needConfirm(long classId, long teacherId);

	/**
	 * 允许任何人加入班级，学生申请即可加入
	 * 
	 * @param classId
	 * @param teacherId
	 * @return
	 */
	int notNeedConfirm(long classId, long teacherId);

	/**
	 * 获取老师的班级，按学生数量倒序排序
	 * 
	 * @param teacherId
	 * @return
	 */
	List<HomeworkClazz> listClazzsOrderByStuNum(long teacherId);

	/**
	 * 根据教师id和班级状态取count值
	 * 
	 * @param teacherId
	 * @param status
	 */
	long allCountByTeacherId(long teacherId, Status status);

	/**
	 * 客户端创建班级
	 * 
	 * @param name
	 * @param teacherId
	 * @param schoolYear
	 */
	HomeworkClazz createByMobile(String name, Long teacherId, int schoolYear);
}
