package com.lanking.cloud.job.correctQuestionDistribute.service;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月8日
 * @since 小优秀快批
 */
public interface CorrectQuestionService {

	/**
	 * <p>
	 * Description:派题给等待的批改员
	 * <p>
	 * 
	 * @date: 2018年3月8日
	 * @author: pengcheng.yu
	 * @param question
	 * @param correctUser
	 */
	void distributeCorrectQuestion(CorrectQuestion question, CorrectUser correctUser);

	/**
	 * <p>
	 * Description:将题目退回到待批改池
	 * <p>
	 * 
	 * @date: 2018年3月10日
	 * @author: pengcheng.yu
	 * @param correctQuestionIds
	 */
	void sendBackCorrectQuestions(List<Long> correctQuestionIds);

	void batchSave(Collection<CorrectQuestion> entities);

	/**
	 * 搜索需要普通批改员批改的习题.
	 * 
	 * @param size
	 *            搜索条数
	 * @param exqids
	 *            需要排除的习题
	 * @return
	 */
	List<CorrectQuestion> queryCorrectQuestionsToTeacher(int size, List<Long> exqids);

	/**
	 * 搜索需要管理员批改员批改的习题.
	 * 
	 * @param size
	 *            搜索条数
	 * @return
	 */
	List<CorrectQuestion> queryCorrectQuestionsToAdmin(int size);
	
	Page<CorrectQuestion> queryCorrectQuestions(Pageable page);
	/**
	 * 查询待批改池中异常的数据
	 * @return
	 */
	List<CorrectQuestion> queryAbnormalCorrectQuestions();
	
	void clearAbnormalCorrectQuestions(List<Long> questionIds);
}
