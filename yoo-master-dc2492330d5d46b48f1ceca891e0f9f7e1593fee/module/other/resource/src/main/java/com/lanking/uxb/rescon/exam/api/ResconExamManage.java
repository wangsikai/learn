package com.lanking.uxb.rescon.exam.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.rescon.exam.form.ExamForm;
import com.lanking.uxb.rescon.exam.form.QueryForm;

/**
 * 提供试卷相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月21日 下午3:56:14
 */
public interface ResconExamManage {

	ExamPaper get(long id);

	Map<Long, ExamPaper> mget(List<Long> ids);

	/**
	 * 创建试卷
	 * 
	 * @param userId
	 *            创建者ID
	 * @param examForm
	 *            创建form
	 * @return
	 */
	ExamPaper create(long userId, ExamForm examForm);

	/**
	 * 试卷查询接口
	 * 
	 * @param queryForm
	 *            查询form
	 * @return 查询结果 分页
	 */
	Page<ExamPaper> queryResconExam(QueryForm queryForm);

	/**
	 * 编辑试卷信息（头部基础信息）
	 * 
	 * @param examForm
	 *            试卷信息
	 * @param userId
	 *            创建人信息
	 */
	void edit(ExamForm examForm, Long userId);

	/**
	 * 修改试卷状态
	 * 
	 * @param examId
	 *            试卷ID
	 * @param status
	 *            状态
	 * @return 需要同步索引的题目id
	 */
	List<Long> updateExamStatus(long examId, ExamPaperStatus status);

	/**
	 * 直接更新试卷时间.
	 * 
	 * @param examId
	 *            试卷ID
	 * @param userId
	 *            用户
	 */
	void updateExamUpdateAt(long examId, Long userId);

	/**
	 * 更新试卷平均难度.
	 * 
	 * @param examId
	 *            试卷ID
	 * @param avgDifficulty
	 *            平均难度
	 */
	void updateExamAvgDifficulty(long examId, BigDecimal avgDifficulty);

	/**
	 * 题目修改后重新计算试卷难度.
	 * 
	 * @param questionId
	 */
	void updateExamAvgDifficultyByQuestion(long questionId);

	/**
	 * 替换习题.
	 * 
	 * @since rescon v1.3.3
	 * @param examId
	 *            试卷ID
	 * @param oldQuestionId
	 *            旧习题ID
	 * @param newQuestionId
	 *            新习题ID
	 */
	void changeQuestion(long examId, long oldQuestionId, long newQuestionId);
}
