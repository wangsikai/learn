package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.uxb.service.zuoye.value.VQuestionCar;

/**
 * 题目篮子(类似于购物车) <br>
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
public interface ZyQuestionCarService {

	/**
	 * 获取某个用户的题目篮子的题目列表
	 * 
	 * @since yoomath V1.3
	 * @param userId
	 *            用户ID
	 * @return 题目列表
	 */
	List<Question> getQuestions(long userId);

	/**
	 * 将题目加入题目篮子
	 * 
	 * @since yoomath V1.3
	 * @param userId
	 *            用户ID
	 * @param id
	 *            题目ID
	 * @param difficult
	 *            难度系数
	 */
	void addQuestion2Car(long userId, long id, double difficult, Question.Type type);

	/**
	 * 将题目移除题目篮子
	 * 
	 * @since yoomath V1.3
	 * @param userId
	 *            用户ID
	 * @param id
	 *            题目ID
	 */
	void removeFromCar(long userId, long id);

	/**
	 *
	 * 获得当前篮子所有题目的id及其对应的难度系数
	 *
	 * @param userId
	 *            用户的id
	 * @return map类型的id->difficult数据
	 */
	Map<Long, Double> getCarCondition(long userId);

	/**
	 * mgetList作业篮子里的数据
	 *
	 * @param userId
	 *            用户id
	 * @return List类型的题目
	 */
	List<VQuestionCar> mgetList(long userId);

	/**
	 * 清空作业篮子中所有数据
	 *
	 * @param userId
	 *            用户的id
	 */
	void removeAll(long userId);

	/**
	 * 添加多个题目到作业篮子
	 * 
	 * @param userId
	 *            用户Id
	 * @param questionList
	 *            题目列表
	 * @return 放进去的题目列表
	 */
	List<Long> addQuestions2Car(long userId, List<Question> questionList);

	/**
	 * 得到现在作业篮子里的题目数量
	 *
	 * @param userId
	 *            用户id
	 * @return 作业篮子里的题目数量
	 */
	Long countQuestions(long userId);

	/**
	 * 记录下排序过后的题目id
	 *
	 * @param userId
	 *            用户id
	 * @param questionIds
	 *            　排序过后的question id
	 */
	void addSortedQuestions(long userId, Collection<Long> questionIds);

	/**
	 * 获得排序过后的question id
	 *
	 * @param userId
	 *            用户id
	 * @return 排序过后的question id
	 */
	List<Long> getSortedQuestions(long userId);

	List<Long> getQuestionCarIds(long userId);

	/**
	 * 记录用户添加题目顺序的questionId
	 *
	 * @param userId
	 *            用户id
	 * @param question
	 *            id
	 */
	void addQuestionIds(long userId, Collection<Long> questionIds);

	/**
	 * 获得按用户添加顺序排序的question id
	 *
	 * @param userId
	 *            用户id
	 * @return
	 */
	List<Long> getQuestionIds(long userId);
}
