package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.uxb.service.zuoye.form.TeaCorrectQuestionForm2;

/**
 * 学生作业题目相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月21日
 */
public interface ZyStudentHomeworkQuestionService {

	StudentHomeworkQuestion get(Long id);

	Map<Long, StudentHomeworkQuestion> mget(List<Long> stuHKQIds);

	/**
	 * 查询学生一次作业的错题
	 * 
	 * @since yoomath V1.4
	 * @param homeworkId
	 *            作业ID
	 * @return 错题(key:学生ID,value:错题集合)
	 */
	Map<Long, List<Long>> findHomeworkWrongQuestion(long homeworkId);

	/**
	 * 获取学生一次作业的订正题
	 * 
	 * @since yoomath V1.4
	 * @param id
	 *            学生作业ID
	 * @return 订正题ID
	 */
	List<Long> getCorrectQuestions(long stuHkId);

	/**
	 * 获取学生一次作业的订正题
	 * 
	 * @since 小优快批
	 * @param id
	 *            学生作业ID
	 * @return 订正题ID
	 */
	List<Long> getNewCorrectQuestions(long stuHkId);

	/**
	 * 得到待批改的学生题目列表
	 *
	 * 1. 如果题目类型是简答题则无论什么结果都需要查询出来,并且必烦等待其他的题目全部批改完成才可以.<br/>
	 * 2. 若是填空题则只查询作业中答案是错误和未知.
	 *
	 * @param homeworkId
	 *            作业id
	 * @param questionId
	 *            题目id
	 * @param type
	 *            {@link com.lanking.cloud.domain.common.resource.question.Question.Type}
	 * @param isLastCommit
	 *            是否过了人工批改时间
	 * @return 学生题目列表
	 */
	List<StudentHomeworkQuestion> queryCorrectQuestions(long homeworkId, long questionId, Question.Type type,
			boolean isLastCommit);

	/**
	 * 保存教师批改的标注
	 *
	 * @param id
	 *            id
	 * @param srcId
	 *            批注基于的图片ID
	 * @param generateId
	 *            合成后的图片id
	 * @param notationImg
	 *            标注数据图片
	 * @param notationPoints
	 *            标注数据点
	 * @param rightRate
	 *            正确率
	 * @param result
	 *            批改结果
	 * @return 更新条数
	 */
	int saveNotation(long id, long srcId, long generateId, String notationImg, String notationPoints, Integer rightRate,
			HomeworkAnswerResult result);

	/**
	 * 保存教师批改的标注,多个图的时候用,保存到表student_homework_answer_image<br>
	 * 2016-12-23 wangsenhao
	 * 
	 * @param id
	 *            id
	 * @param srcId
	 *            批注基于的图片ID
	 * @param generateId
	 *            合成后的图片id
	 * @param notationImg
	 *            标注数据图片
	 * @param notationPoints
	 *            标注数据点
	 * @param rightRate
	 *            正确率
	 * @param result
	 *            批改结果
	 * @return 更新条数
	 */
	int saveMultiNotation(long id, long oriSrcId, long srcId, long generateId, String notationImg,
			String notationPoints);

	/**
	 * 得到教师批改轨迹
	 *
	 * @param id
	 * @param index
	 *            题目id
	 * @return 批改轨迹JSON字符串
	 */
	String getNotation(long id, Integer index);

	/**
	 * 查找需要批改的题目(未过人工批改时间)
	 *
	 * @param hkId
	 *            作业id
	 * @return {@link Question}
	 */
	List<Question> findNeedCorrectQuestion(long hkId);

	/**
	 * 查询需要批改的题目ID列表(未过人工批改时间)
	 * 
	 * @since 2.0.3
	 * @param hkId
	 *            作业ID
	 * @return 题目ID列表
	 */
	List<Long> findNeedCorrectQuestions(long hkId);

	/**
	 * 查找需要批改的题目(已过人工批改时间)
	 *
	 * @param hkId
	 *            作业id
	 * @return {@link Question}
	 */
	List<Question> findNeedCorrectQuestionAll(long hkId);

	/**
	 * 查找需要批改的题目ID列表(已过人工批改时间)
	 *
	 * @since 2.0.3
	 * @param hkId
	 *            作业id
	 * @return 题目列表
	 */
	List<Long> findNeedCorrectQuestionsAll(long hkId);

	/**
	 * 判断一份作业里面有没有简答题被答过题
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @return true|false
	 */
	boolean isQuestionAnsweringDone(long studentHomeworkId);

	/**
	 * 更新旋转后的原始图片
	 *
	 * @param id
	 *            学生题目id
	 * @param image
	 *            旋转后的图片id
	 */
	void updateAnswerImage(long id, long image);

	/**
	 * 查询一次作业中的错题列表
	 * 
	 * @since 2.0.3
	 * @param hkId
	 *            作业ID
	 * @return 题目列表
	 */
	List<Long> listCorrectQuestions(long hkId);

	List<StudentHomeworkQuestion> listByQuestionId(long homeworkId, long questionId);

	/**
	 * 更新学生作业答题时间.
	 * 
	 * @since yoomath v2.3.0
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业题目ID
	 * @param dotime
	 *            答题耗时（秒数）
	 */
	void updateDoQuestionTime(long studentHomeworkQuestionId, int dotime);

	List<Map> findStuQuestionMapByOldCodes(long studentHomeworkQuestionId, List<Long> codes);

	List<Map> findStuQuestionMapByNewCodes(long studentHomeworkQuestionId, List<Long> codes);

	/**
	 * 轨迹保存
	 * 
	 * @param form
	 */
	void saveNotation(TeaCorrectQuestionForm2 form);

	/**
	 * 更新音频数据
	 *
	 * @param voiceTime
	 *            语音时间
	 * @param fileKey
	 *            存储的七牛文件唯一标识
	 * @param shqId
	 *            学生作业题目id
	 */
	void updateVoice(int voiceTime, String fileKey, long shqId);

	/**
	 * 统计学生作业中的错题数和已订正题数目
	 * 
	 * @param stuHomeworkIds
	 * @return
	 */
	List<Map> findWrongAndcorrectionQuestion(Collection<Long> stuHomeworkIds);

	/**
	 * 统计正在人工批改的题和已经批改过的题
	 * 
	 * @param stuHomeworkIds
	 * @return
	 */
	List<Map> findCorrectedAndcorrectingQuestion(Collection<Long> stuHomeworkIds);
	
	List<StudentHomeworkQuestion> listByStuHomeworkIdAndQuestionId(long stuHomeworkId, long questionId);
	
	/**
	 * 统计学生作业待批改题数目
	 * 
	 * @param stuHomeworkIds
	 * @return
	 */
	List<Map> staticToBeCorrectedQuestionCount(Collection<Long> stuHomeworkIds);
}
