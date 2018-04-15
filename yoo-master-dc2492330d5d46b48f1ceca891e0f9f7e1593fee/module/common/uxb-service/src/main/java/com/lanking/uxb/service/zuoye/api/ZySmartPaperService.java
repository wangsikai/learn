package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaper;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperQuestion;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.zuoye.form.PaperPullForm;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

/**
 * 智能出卷接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author wangsenhao
 *
 */
public interface ZySmartPaperService {

	/**
	 * 保存试卷
	 * 
	 * @param paperPullForm
	 * @return
	 */
	List<SmartExamPaperQuestion> savePaper(PaperPullForm paperPullForm);

	/**
	 * 获取试卷题目，如果没有表示第一次拉取
	 * 
	 * @param paperPullForm
	 * @return
	 */
	List<SmartExamPaperQuestion> queryPaperQuestion(PaperPullForm paperPullForm);

	/**
	 * 保存试卷批改结果
	 * 
	 * @param result
	 * @param rightCount
	 *            正确数
	 * @param paperId
	 *            试卷ID
	 */
	VUserReward savePaperResult(VExerciseResult result, int rightCount, long paperId,
			List<Map<Long, List<String>>> answerList, Integer homeworkTime, long userId);

	/**
	 * 换一批时更新试卷状态为disabled
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param studentId
	 *            学生ID
	 * @param textbookCode
	 *            教材代码
	 */
	void updateStatus(long studentId, int textbookCode);

	/**
	 * 游标方式获取历史试卷
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param studentId
	 *            学生ID
	 * @param textbookCode
	 *            教材代码
	 * @param cpr
	 *            分页条件
	 * @return 历史试卷分页数据
	 */
	CursorPage<Long, SmartExamPaper> queryHistoryPaperList(long studentId, int textbookCode, CursorPageable<Long> cpr);

	/**
	 * 获取历史试卷的平均正确率
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param studentId
	 *            学生ID
	 * @param textbookCode
	 *            教材代码
	 * @return 历史试卷的平均正确率
	 */
	BigDecimal getHistoryPaperAvg(long studentId, int textbookCode);

	/**
	 * 获取试卷题目
	 * 
	 * @param paperId
	 * @return
	 */
	List<SmartExamPaperQuestion> queryPaperQuestion(Long paperId);

	/**
	 * 获取历史试卷数量
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param studentId
	 *            学生ID
	 * @param textbookCode
	 *            教材代码
	 * @return 试卷数量
	 */
	Long getHistoryPaperCount(long studentId, int textbookCode);

	/**
	 * 获取学生薄弱知识点集合
	 * 
	 * @param studentId
	 * @param phaseCode
	 * @param count
	 *            随机取几个知识点
	 * @return
	 */
	List<Long> queryWeakKnowpoints(long studentId, Integer phaseCode, int count);

	/**
	 * 重新练习，把之前试卷先置为DELETE.<br>
	 * 1.如果用户重新练习不提交，之前的试卷看不到。<br>
	 * 2.如果提交,会在提交处做处理
	 * 
	 * @param paperId
	 */
	void rePractice(Long paperId);

	/**
	 * 保存没有提交的试卷
	 * 
	 * @param paperId
	 * @param answerList
	 * @param qIds
	 * @param homeworkTime
	 *            试卷所花时间
	 */
	void saveNotCommitPaper(long paperId, List<Map<Long, List<String>>> answerList, List<Long> qIds,
			Integer homeworkTime);

	/**
	 * 获取试卷
	 * 
	 * @param paperId
	 * @return
	 */
	SmartExamPaper get(long paperId);

}
