package com.lanking.cloud.job.correctQuestionDistribute.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优秀快批
 */
public interface CorrectQuestionDao {

	/**
	 * 
	 * <p>
	 * Description:分配待批改题目给批改老师
	 * <p>
	 * 
	 * @date: 2018年3月8日
	 * @author: pengcheng.yu
	 * @param bizId
	 *            student_homework_question的ID
	 * @param correctUserId
	 *            当前批改人员Id
	 * @param firstAllotAt
	 *            首次分配批改人的时间.
	 * @param allotAt
	 *            分配当前批改人的时间
	 */
	void disTributeCorrectQuestionToUser(CorrectQuestion correctQuestion, CorrectUser correctUser, Date firstAllotAt,
			Date allotAt);

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
