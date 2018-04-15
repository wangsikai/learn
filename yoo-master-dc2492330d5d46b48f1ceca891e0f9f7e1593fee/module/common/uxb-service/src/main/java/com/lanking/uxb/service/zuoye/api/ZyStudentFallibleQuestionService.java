package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.zuoye.form.StuFallibleQuestion2Form;

/**
 * 学生错题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public interface ZyStudentFallibleQuestionService {
	/**
	 * 查询学生错题
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<StudentFallibleQuestion> query(ZyStudentFallibleQuestionQuery query, Pageable pageable);

	/**
	 * 查询学生错题（word导出专用）
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> exportQuery(ZyStudentFallibleQuestionQuery query, Pageable pageable);

	/**
	 * 统计某个学生某个教材的错题
	 * 
	 * @since 2.1
	 * @param studentId
	 *            学生ID
	 * @param textbookCode
	 *            教材代码
	 * @return KEY:sectionCode VALUE:错题数量
	 */
	Map<Long, Long> staticFallibleCount(long studentId, int textbookCode);

	/**
	 * 统计某个学生多个教材的错题
	 * 
	 * @since 2.1
	 * @param studentId
	 *            学生ID
	 * @param textbookCodes
	 *            教材代码集合
	 * @return KEY:sectionCode VALUE:错题数量
	 */
	Map<Long, Long> staticFallibleCounts(long studentId, Collection<Integer> textbookCodes);

	/**
	 * 查询错题集合
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, StudentFallibleQuestion> query(ZyStudentFallibleQuestionQuery query, CursorPageable<Long> pageable);

	/**
	 * 查询错题
	 *
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, StudentFallibleQuestion> query2(ZyStudentFallibleQuestionQuery query, CursorPageable<Long> pageable);

	/**
	 * 更新错题记录
	 * 
	 * @since 2.1
	 * @param questionId
	 *            题目ID
	 * @param studentId
	 *            学生ID
	 * @param result
	 *            答题结果
	 * @param answer
	 *            答案
	 * @param source
	 *            来源
	 */
	void update(long questionId, long studentId, HomeworkAnswerResult result, List<HomeworkAnswerResult> itemResults,
			Map<Long, List<String>> answer, Map<Long, List<String>> asciimathAnswer, List<Long> answerImgs,
			Integer rightRate, StudentQuestionAnswerSource source);

	/**
	 * 更新学生错题（来源于学生组卷统计）.
	 * 
	 * @param sfQuestions
	 *            错题集合
	 */
	void updateFromStudentCustompaper(List<StudentFallibleQuestion> sfQuestions);

	/**
	 * 更新错题记录（只针对OCR图片错题使用）
	 *
	 * @since 2.1.0
	 * @param id
	 *            错题本id
	 * @param studentId
	 *            学生id
	 * @param imageIds
	 *            答题图片ids
	 * @param result
	 *            批改结果
	 */
	void update(long id, long studentId, List<Long> imageIds, StudentQuestionAnswerSource source,
			HomeworkAnswerResult result);

	/**
	 * 批量获得学生题目的练习次数
	 *
	 * @param questionIds
	 *            题目id
	 * @param studentId
	 *            学生id
	 * @return Map类型questionId -> 练习次数
	 */
	Map<Long, Long> mgetQuestionExerciseNums(Collection<Long> questionIds, long studentId);

	/**
	 * 统计学生练习题目次数
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param studentId
	 *            学生ID
	 * @return 练习题目次数
	 */
	long countDoNum(long studentId);

	/**
	 * 获取错题班级正确率
	 * 
	 * @param studentId
	 *            学生ID
	 * @param questionIds
	 *            习题ID集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> sfQuestionRateQuery(long studentId, Collection<Long> questionIds);

	/**
	 * 获取错题班级正确率
	 * 
	 * @param studentIds
	 *            学生ID
	 * @param sections
	 *            章节集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getStudentSFCount(Collection<Long> studentIds, Collection<Long> sectionCodes);

	/**
	 * 查询学生教材章节 错题数据
	 *
	 * @param studentId
	 *            学生id
	 * @param categoryCode
	 *            版本码
	 * @return 教材章节下的错题数量
	 */
	List<Map> getStudentFallTSCount(long studentId, int categoryCode);

	/**
	 * 根据时间日期去查询
	 *
	 * @param studentId
	 *            学生id
	 * @param categoryCode
	 *            版本码
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return 查询结果
	 */
	Long countByDate(long studentId, int categoryCode, Date beginDate, Date endDate);

	/**
	 * 查询其他错题数量
	 *
	 * @param studentId
	 *            学生id
	 * @param categoryCode
	 *            版本码
	 * @return 查询结果
	 */
	Long countOther(long studentId, int categoryCode);

	/**
	 * 查询OCR的题目数量
	 *
	 * @param studentId
	 *            学生id
	 * @return 查询结果
	 */
	Long countOcr(long studentId, int categoryCode);

	/**
	 * 查询题目的做错人数
	 *
	 * @param questionIds
	 *            题目id列表
	 * @return 题目id及错误人数
	 */
	Map<Long, Long> countMistakePeople(Collection<Long> questionIds);

	/**
	 * 判断学生错题本是否有传入进来的题目
	 *
	 * @param questionIds
	 *            题目id
	 * @param userId
	 *            学生id
	 * @return 题目对应是否存在 true 存在 false 不存在
	 */
	Map<Long, Boolean> exist(Collection<Long> questionIds, long userId);

	/**
	 * 添加题目至错题本
	 *
	 * @param questionId
	 *            题目id
	 * @param studentId
	 *            学生id
	 * @param fileId
	 *            OCR图片识别id
	 * @return 错题是属于哪个教材下的教材码
	 */
	List<Integer> add(long questionId, long studentId, Long fileId, StudentQuestionAnswerSource source);

	/**
	 * 添加拍照题至错题本
	 *
	 * @param studentId
	 *            学生id
	 * @param fileId
	 *            拍照图片id
	 * @param codes
	 *            学生选择的知识点列表
	 */
	void addOcr(long studentId, long fileId, List<Long> codes);

	/**
	 * 索引查询学生的错题
	 * 
	 * @param query
	 * @param p
	 * @return
	 */
	Page<StudentFallibleQuestion> queryStuFallibleQuestionByIndex(StuFallibleQuestion2Form form, Pageable p);

	/**
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, StudentFallibleQuestion> mget(Collection<Long> ids);

	/**
	 * 删除学生错题
	 *
	 * @param id
	 * @param studentId
	 *            当前学生ID
	 */
	void deleteFailQuestion(Long id, Long studentId);

	/**
	 * 学生知识点统计
	 * 
	 * @param userId
	 * @return
	 */
	Map<Integer, Integer> statisKnowPoint(Long userId);

	/**
	 * 学生知识点统计(新)
	 * 
	 * @param userId
	 * @return
	 */
	Map<Long, Long> statisNewKnowPoint(Long userId);

	/**
	 * 学生查询自己的错题（推荐）
	 *
	 * @param form
	 *            {@link StuFallibleQuestion2Form}
	 * @return 推荐的错题
	 */
	List<StudentFallibleQuestion> queryDoStuFallibleQuestions(StuFallibleQuestion2Form form);

	/**
	 * 获得学生错题导出统计.
	 * 
	 * @since web v2.0.3
	 * 
	 * @param studentId
	 *            学生ID
	 * @param sectionCodes
	 *            章节
	 * @param timeScope
	 *            时间范围
	 * @param questionTypes
	 *            题目类型
	 * @param errorTimes
	 *            错误次数
	 * @return
	 */
	long getStudentExportCount(long studentId, Collection<Long> sectionCodes, Date timeScope,
			Collection<Integer> questionTypes, Integer errorTimes);

	/**
	 * 获得学生导出错题.
	 * 
	 * @since web v2.0.3
	 * 
	 * @param studentId
	 *            学生ID
	 * @param sectionCodes
	 *            章节
	 * @param timeScope
	 *            时间范围
	 * @param questionTypes
	 *            题目类型
	 * @param errorTimes
	 *            错误次数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> queryStudentExportQuestion(long studentId, Collection<Long> sectionCodes, Date timeScope,
			Collection<Integer> questionTypes, Integer errorTimes);

	/**
	 * 获得学生指定教材的错题数量（错题下载使用）
	 * 
	 * @since web 2.0.3
	 * @param studentId
	 *            学生ID
	 * @param textbookCodes
	 *            教材集合
	 * @return
	 */
	long getTextbookQuestionCount(long studentId, Collection<Integer> textbookCodes);

	/**
	 * 获取学生的第一道错题
	 * 
	 * @param studentId
	 * @return
	 */
	StudentFallibleQuestion getFirst(long studentId);

	/**
	 * 获取学生错题数量
	 * 
	 * @param studentId
	 * @return
	 */
	Long getFallibleCount(long studentId);

	/**
	 * 获取最新30天的统计
	 * 
	 * @param studentId
	 * @return
	 */
	Long getLast30Stat(long studentId);

	/**
	 * 近6个月错题统计
	 * 
	 * @param studentId
	 * @return
	 */
	List<Map> queryLast6MonthStat(long studentId);

	/**
	 * 查询易错知识点
	 * 
	 * @param studentId
	 * @return
	 */
	List<Map> queryWeakKpList(long studentId);

	/**
	 * 统计某个学生多个教材下第一级章下的不重复错题数量（学生导出错题使用）.
	 * 
	 * @since web 2.0.3
	 * @param studentId
	 *            学生ID
	 * @param textbookCodes
	 *            教材版本集合
	 * @return
	 */
	Map<Long, Long> studentFallibleLevel1SectionCounts(long studentId, Collection<Integer> textbookCodes);

	/**
	 * 统计教材是否存在错题
	 * 
	 * @since yoomath V2.0.3
	 * @param textbookCodes
	 *            教材代码
	 * @param userId
	 *            用户ID
	 * @return 统计数据
	 */

	Map<Integer, Boolean> statisTextbookExistStuFallWithCache2(List<Integer> textbookCodes, Long userId);

	/**
	 * 有错题产生时更新缓存
	 * 
	 * @param questionId
	 * @param userId
	 */
	void updateTextbookStuFallCache(final long questionId, final long userId);

	/**
	 * 通过学生和批量获取错题对象
	 * 
	 * @param questionIds
	 * @param studentId
	 * @return
	 */
	Map<Long, StudentFallibleQuestion> mgetQuestion(Collection<Long> questionIds, long studentId);

	/**
	 * 通过学生和批量获取错题对象，list返回
	 * 
	 * @param questionIds
	 * @param studentId
	 * @return
	 */
	List<StudentFallibleQuestion> mgetQuestionList(Collection<Long> questionIds, long studentId);

	/**
	 * 根据学生id以及questionId查询错题
	 *
	 * @param studentId
	 *            学生id
	 * @param questionId
	 *            题目id
	 * @return {@link StudentFallibleQuestion}
	 */
	StudentFallibleQuestion findByStudentAndQuestion(long studentId, long questionId);

	/**
	 * 查询学生不同章节对应易错知识点个数
	 * 
	 * @param studentId
	 * @param sectionCodes
	 * @return
	 */
	Map<Long, Long> queryWeakKpCount(long studentId, Collection<Long> sectionCodes);

	/**
	 * 查询做题本里易错知识点集合
	 * 
	 * @param studentId
	 * @param sectionCodes
	 * @return
	 */
	List<Map> queryFallKpBySectionCodes(long studentId, Collection<Long> sectionCodes);

	/**
	 * 获取当前学生，教材下
	 * 
	 * @param studentId
	 * @param textbookCode
	 * @return
	 */
	Map<Integer, Long> getWeakKpCountByTextbookCodes(long studentId, Collection<Integer> textbookCodes);

	/**
	 * 查询学生教材章节 错题数据,只查询到第一层并且去重
	 *
	 * @param studentId
	 *            学生id
	 * @param categoryCode
	 *            版本码
	 * @return 教材章节下的错题数量
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getStudentFallibleFirstSectionCount(long studentId, int categoryCode);

	/**
	 * 查询学生教材错题数据,并且去重
	 *
	 * @param studentId
	 *            学生id
	 * @param textbookCodes
	 *            教材code
	 * @return 教材下的错题数量
	 */
	Map<Integer, Long> getStudentFallibleTextbookCodeCount(long studentId, List<Integer> textbookCodes);

	/**
	 * 查询其它版本下错题
	 *
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, StudentFallibleQuestion> queryOtherCategoryCode(ZyStudentFallibleQuestionQuery query,
			CursorPageable<Long> pageable);
}
