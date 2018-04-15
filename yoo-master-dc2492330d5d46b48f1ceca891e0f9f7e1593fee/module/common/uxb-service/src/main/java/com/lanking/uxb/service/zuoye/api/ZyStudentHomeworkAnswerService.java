package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;

/**
 * 学生作业答案相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月22日
 */
public interface ZyStudentHomeworkAnswerService {


	void doQuestion(long studentHomeworkQuestionId, List<String> answers, List<String> answerAsciis, Long solvingImg,
			long studentId);

	void doQuestion(Map<Long, List<String>> answers, Map<Long, List<String>> answerAsciis, Long solvingImg,
			long studentId);

	/**
	 * 同时保存多个题答案
	 *
	 * @param answers
	 *            Latex答案Map: StudentHomeworkQuestion.id -> 答案列表
	 * @param answerAsciis
	 *            Ascii答案Map: StudentHomeworkQuestion.id -> 答案列表
	 * @param solvingImgs
	 *            解题过程图片Map: StudentHomeworkQuestion.id -> image.id
	 * @param questionTypes
	 *            题目类型Map StudentHomeworkQuestion.id -> 所对应的题目类型
	 * @param studentId
	 *            学生id
	 */
	void doQuestion(Map<Long, List<String>> answers, Map<Long, List<String>> answerAsciis,
	        Map<Long, Long> solvingImgs, Map<Long, Question.Type> questionTypes, long studentId);

	List<StudentHomeworkAnswer> find(long homeworkId, long questionId);

	/**
	 * 统计一个学生作业是否答题了
	 * 
	 * @since yoomath V1.6
	 * @param studentHomeworkId
	 *            学生作业
	 * @return true|false
	 */
	boolean isDoHomework(long studentHomeworkId);

	/**
	 * 保存学生答案
	 *
	 * @param id
	 *            答案id
	 * @param result
	 *            结果
	 */
	void saveAnswerResult(long id, HomeworkAnswerResult result);

	List<StudentHomeworkAnswer> listByQuestionId(long homeworkId, long questionId);

	/**
	 * 新做题，可保存多张解题过程图片，支持墨水屏手写答案上传
	 * 所有的Map对象key值都是StudentHomeworkQuestion.id
	 *
	 * @param answers
	 *            latex编码答案
	 * @param answerAsciis
	 *            ascii编码答案
	 * @param solvingImgs
	 *            答题过程图片
	 * @param questionTypes
	 *            题目类型
	 * @param studentId
	 *            学生id
	 * @param handWritings
	 *            墨水屏手写数据
	 */
	void doQuestion(Map<Long, List<String>> answers, Map<Long, List<String>> answerAsciis,
	        Map<Long, List<Long>> solvingImgs, Map<Long, Question.Type> questionTypes, long studentId,
	        Map<Long, List<String>> handWritings, Map<Long, Integer> doTime);
}
