package com.lanking.uxb.service.correct.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.correct.vo.QuestionCorrectResult;

/**
 * 批改流程使用的学生作业习题接口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface CorrectStudentHomeworkQuestionService {

	/**
	 * 获取学生作业习题.
	 * 
	 * @param id
	 *            学生作业习题ID
	 * @return
	 */
	StudentHomeworkQuestion get(long id);

	/**
	 * 获取学生作业习题（包含订正题）.
	 * 
	 * @param studentHomeworkQuestionIds
	 *            学生作业习题ID集合
	 * @return
	 */
	List<StudentHomeworkQuestion> mgetList(Collection<Long> studentHomeworkQuestionIds);

	/**
	 * 获取学生作业习题（不包含订正题）.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @return
	 */
	List<StudentHomeworkQuestion> getStudentHomeworkQuestions(long studentHomeworkId);

	/**
	 * 获取学生作业习题（包含订正题）.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @return
	 */
	List<StudentHomeworkQuestion> getAllStudentHomeworkQuestions(long studentHomeworkId);

	/**
	 * 批量保存习题批改结果.
	 * 
	 * @param answerResults
	 *            批改结果集合
	 */
	void saveCorrectResults(List<QuestionCorrectResult> questionResults);

	/**
	 * 保存习题预期批改方式
	 * 
	 * @param questionCorrectTypes
	 *            预期批改方式集合
	 */
	void saveQuestionCorrectType(Map<Long, QuestionCorrectType> questionCorrectTypes);

	/**
	 * 保存习题预期批改方式
	 * 
	 * @param studentHomeworkQuestionId
	 *            习题ID
	 * @param correctType
	 *            批改方式
	 */
	void saveQuestionCorrectType(long studentHomeworkQuestionId, QuestionCorrectType correctType);

	/**
	 * 教师手动批改单道题.
	 * 
	 * @param corrector
	 *            批改员
	 * @param correctorType
	 *            批改员类型
	 * @param questionCorrectObject
	 *            批改对象
	 */
	HomeworkAnswerResult correctSigleQuestion(long corrector, CorrectorType correctorType,
			QuestionCorrectObject questionCorrectObject);

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
	 * @param correctType
	 *            人员批改方式
	 * @return 更新条数
	 */
	int saveNotation(long id, long srcId, long generateId, String notationImg, String notationPoints, Integer rightRate,
			HomeworkAnswerResult result, Date correctAt, QuestionCorrectType correctType);

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
	 * 创建订正题.
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 */
	void createStudentHomeworkCorrectQuestion(long studentHomeworkQuestionId);

	/**
	 * 创建订正题.
	 * 
	 * @param studentHomeworkQuestionIds
	 *            学生作业习题ID集合
	 */
	void createStudentHomeworkCorrectQuestion(Collection<Long> studentHomeworkQuestionIds);

	/**
	 * 获取作业中是该题的订正题.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionId
	 *            作业习题 ID
	 * @return
	 */
	StudentHomeworkQuestion getNewCorrectQuestion(long studentHomeworkId, long questionId);
}
