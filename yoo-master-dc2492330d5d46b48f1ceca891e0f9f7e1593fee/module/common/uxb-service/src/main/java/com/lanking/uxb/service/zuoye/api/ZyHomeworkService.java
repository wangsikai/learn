package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;

/**
 * 悠作业:作业接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public interface ZyHomeworkService {

	/**
	 * 获取某个老师待处理的作业列表
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param limit
	 *            获取的条数
	 * @param isCourse
	 *            是否为课程上的作业
	 * @return {@link Homework}
	 */
	List<Homework> findProcessHomeworks(long teacherId, int limit, boolean isCourse);

	/**
	 * 获取某个老师待处理的作业列表（仅包含待批改状态）
	 * 
	 * @since 2.0.2（web v2.0）
	 * @param teacherId
	 *            教师ID
	 * @param limit
	 *            获取的条数
	 * @param isCourse
	 *            是否为课程上的作业
	 * @return {@link Homework}
	 */
	List<Homework> findProcessHomeworks2(long teacherId, int limit, boolean isCourse);

	/**
	 * 分页查询作业列表
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件{@link ZyHomeworkQuery}
	 * @param pageable
	 *            分页条件
	 * @return 分页数据 {@link Page}
	 */
	Page<Homework> query(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 手机端教师作业列表的查询
	 * 
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Homework> queryForMobile(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 生成作业名称
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @return 作业名称
	 */
	String getHomeworkName(long teacherId, long textbookExerciseId, Long sectionCode, boolean isBook);

	/**
	 * 生成作业名称
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param TextbookExercise
	 *            预置练习
	 * @return 作业名称
	 */
	String getHomeworkName(long teacherId, TextbookExercise textbookExercise);

	/**
	 * 布置作业
	 * 
	 * @since 2.1
	 * @param form
	 *            {@link PublishHomeworkForm}
	 */
	long publish(PublishHomeworkForm form);

	/**
	 * 下发作业
	 * 
	 * @since 2.1
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 * @param homework
	 *            作业对象
	 * @param inAdvance
	 *            是否可以提前下发
	 */
	@Deprecated
	void issue(Homework homework, boolean inAdvance);

	/**
	 * 提交整个作业
	 * 
	 * @since 2.1
	 * @param homeworkId
	 *            作业ID
	 */
	int commitHomework(long homeworkId);

	/**
	 * 根据习题IDs获取查询作业
	 * 
	 * @since 2.1
	 * @param teacherId
	 *            教师ID
	 * @param exerciseIds
	 *            习题ID
	 * @return 作业列表
	 */
	List<Homework> findByExercise(long teacherId, Collection<Long> exerciseIds);

	/**
	 * 获取班级最近的下发作业列表
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            班级Id
	 * @param limit
	 *            最近的limit次作业
	 * @return 作业列表
	 */
	List<Homework> getLatestIssuedHomeWorks(long classId, int limit);

	/**
	 * 获取班级最近的下发作业列表
	 * 
	 * @since v2.0.2 (web v2.0)
	 * @param classIds
	 *            班级Id集合
	 * @param limit
	 *            最近的limit次作业
	 * @return 作业列表集合
	 */
	Map<Long, List<Homework>> getLatestIssuedHomeWorks(Collection<Long> classIds, int limit);

	/**
	 * 根据习题ID获取最近的一次作业
	 * 
	 * @since yoomath V1.2
	 * @param teacherId
	 *            教师ID
	 * @param exerciseId
	 *            习题ID
	 * @return 作业
	 */
	Homework getLatextHomeworkByExerciseId(long teacherId, long exerciseId);

	/**
	 * 删除作业
	 * 
	 * @since yoomath V1.3
	 * @param teacherId
	 *            教师ID
	 * @param homeworkId
	 *            作业ID
	 * @return 删除的条数
	 */
	int delete(long teacherId, long homeworkId);

	/**
	 * 获取某个班级上一个未订正的作业
	 * 
	 * @since yoomath V1.4
	 * @param classId
	 *            班级ID
	 * @return 作业ID
	 */
	Long findLastNotCorrect(long classId);

	/**
	 * 更新被订正的作业
	 * 
	 * @since yoomath V1.4
	 * @param id
	 *            作业ID
	 */
	void updateCorrectedHomework(long id);

	/**
	 * 更新作业的最后提交时间
	 * 
	 * @since yoomath V1.4
	 * @param id
	 *            作业ID
	 * @param autoEffectiveCount
	 *            自动提交的有效作业数量
	 */
	void updateLastCommitAt(long id, long autoEffectiveCount);

	/**
	 * 查询需要全部提交后统计的作业
	 * 
	 * @since yoomath V1.4
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Homework> queryAfterLastCommitStat(Pageable pageable);

	/**
	 * 更新after_lastcommit_stat
	 * 
	 * @since yoomath V1.4
	 * @param id
	 *            作业ID
	 */
	void udpateAfterLastCommitStat(long id);

	/**
	 * 获取所有发布中的作业
	 * 
	 * @since yoomath V1.4.1
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Homework> queryPublishHomework(Pageable pageable);

	/**
	 * 根据查询条件 获取历史作业
	 * 
	 * @since 1.5
	 * @param sectionCode
	 *            章节code
	 * @param sectionNameKey
	 *            模糊匹配章节名称
	 * @param beginTime
	 *            开始时间（创建时间）
	 * @param endTime
	 *            结束时间
	 * @param textBookCode
	 *            教材code
	 * @param textBookCategoryCode
	 *            版本Code
	 * @param classId
	 *            班级Id
	 * @param isDesc
	 *            排序
	 * @param p
	 *            分页条件
	 * @return
	 */
	Page<Homework> queryHistoryHomework(long userId, Long sectionCode, String sectionNameKey, Date beginTime,
			Date endTime, Long textBookCode, Long textBookCategoryCode, Long classId, boolean isDesc, Pageable p);

	/**
	 * 设置开始时间为当前时间
	 * 
	 * @since yoomath V1.6
	 * @param hkId
	 *            作业ID
	 * @param teacherId
	 *            老师ID
	 * 
	 */
	void setStartTimeNow(long hkId, long teacherId);

	/**
	 * 分页查询作业列表(包括寒假作业)
	 * 
	 * @since 1.9
	 * @param query
	 *            查询条件{@link ZyHomeworkQuery}
	 * @param pageable
	 *            分页条件
	 * @return 分页数据 {@link Page}
	 */
	Page<Map> queryUnionHolidayHomework(ZyHomeworkQuery query, Pageable index);

	/**
	 * 首页追踪,分页查询作业列表(包括寒假作业,)
	 * 
	 * @param pageable
	 *            分页条件
	 * @param userId
	 *            用户ID
	 * @return 分页数据 {@link Page}
	 * 
	 */
	Page<Map> queryHomeworkTracking(long userId, Pageable p);

	/**
	 * 首页追踪,作业列表(包括寒假作业,) 统计
	 * 
	 * @param userId
	 *            用户ID
	 */
	List<Map> queryHomeworkTrackingCount(long userId);

	/**
	 * 获取用户 对应班级 ，年份，月份布置和下发的作业
	 * 
	 * @param userId
	 *            用户ID
	 * @param clazzId
	 *            班级ID
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return
	 */
	List<Homework> listByTime(long userId, long clazzId, int year, int month);

	/**
	 * 获取作业用户下发作业
	 * 
	 * @param userId
	 *            用户ID
	 * @param limit
	 *            获取的条数
	 * @return
	 */
	List<Homework> getIssuedHomework(long userId, int limit);

	/**
	 * 获取时间月份内 某个班级 第一份下发的作业
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param id
	 *            班级ID
	 * @return homework
	 */
	Homework getFirstIssuedHomeworkInMonth(int year, int month, long classId);

	/**
	 * 布置作业优化版
	 *
	 * @param form
	 *            {@link PublishHomeworkForm}
	 * @return 布置完成后作业列表
	 */
	List<Homework> publish2(PublishHomeworkForm form);

	/**
	 * 设置作业时间
	 *
	 * @param homeworkId
	 *            作业ID
	 * @param startTime
	 *            开始时间
	 * @param deadline
	 *            终止时间
	 * @return {@link Homework}
	 * @throws HomeworkException
	 */
	Homework setTime2(long homeworkId, long startTime, long deadline) throws HomeworkException;

	/**
	 * 发作作业
	 *
	 * @param homeworkId
	 *            作业ID
	 * @throws HomeworkException
	 */
	void publishHomework2(long homeworkId) throws HomeworkException;

	/**
	 * 分页查询作业列表
	 * 
	 * @since 2.0.3 (web v2.0)
	 * @param query
	 *            查询条件{@link ZyHomeworkQuery}
	 * @param pageable
	 *            分页条件
	 * @return 分页数据 {@link Page}
	 */
	Page<Homework> queryHomeworkWeb2(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 分页查询作业列表
	 * 
	 * @since 小优快批
	 * @param query
	 *            查询条件{@link ZyHomeworkQuery}
	 * @param pageable
	 *            分页条件
	 * @return 分页数据 {@link Page}
	 */
	Page<Homework> queryHomeworkWeb3(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 查询本次作业全部知识点里面包含的薄弱知识点<br>
	 * 不高于50%为薄弱知识点<br>
	 * 新知识点
	 * 
	 * @param hkId
	 * @param knowledges
	 * @return
	 */
	List<Long> queryWeakByCodes(long hkId, List<Long> knowledges);

	/**
	 * 查询本次作业全部知识点里面包含的薄弱知识点<br>
	 * 不高于50%为薄弱知识点<br>
	 * 旧知识点
	 * 
	 * @param hkId
	 * @param knowledges
	 * @return
	 */
	List<Integer> queryWeakByOldCodes(long hkId, List<Long> codes);

	/**
	 * 根据教师id取count值
	 * 
	 * @param originalCreateId
	 */
	long allCountByOriginalCreateId(long originalCreateId);

	/**
	 * 新版手机端教师作业列表的查询，修改排序规则
	 * 
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Homework> queryForMobile2(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 查询最新的普通作业
	 * 
	 * @param query
	 *            查询条件
	 * @return
	 */
	Homework queryLastestByTeacher(ZyHomeworkQuery query);

	/**
	 * 布置作业
	 *
	 * @since 教师端v1.3.0
	 * @param form
	 *            {@link PublishHomeworkForm}
	 * @return 布置完成后作业列表
	 */
	List<Homework> publish3(PublishHomeworkForm form);

	/**
	 * 布置作业---新增分组布置作业 2017-11-9
	 *
	 * @author wangsenhao
	 * @param form
	 *            {@link PublishHomeworkForm}
	 * @return 布置完成后作业列表
	 */
	List<Homework> publishByGroup(PublishHomeworkForm form);

	/**
	 * 获取班级没有下发的作业数
	 * 
	 * @param classId
	 * @return
	 */
	Long countNotIssue(long classId);

	/**
	 * 手机端教师作业列表的查询
	 * 
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Homework> queryForMobile3(ZyHomeworkQuery query, Pageable pageable);

	/**
	 * 查询作业中是否存在待批改的题目,查询出来的是待批改的作业
	 * 
	 * @param userId
	 * @param homeworkIds
	 */
	Set<Long> queryStudentCorrect(Long userId, List<Long> homeworkIds);

	/**
	 * 手机端教师首页查询作业列表
	 * 
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<Homework> queryForMobileIndex(ZyHomeworkQuery query, Pageable pageable);
}
