package com.lanking.cloud.job.doQuestionRank.DAO;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;

/**
 * StudentQuestionAnswer 相关数据库操作接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月12日
 */
public interface StudentQuestionAnswerDAO {

	/**
	 * 统计学生在一段时间内做题信息
	 * 
	 * @param startDate
	 * @param endDate
	 * @param userIds
	 * @return
	 */
	List<StudentQuestionAnswer> taskStaticDoQuestionStudent(Date startDate, Date endDate, List<Long> userIds,
			HomeworkAnswerResult result);
}
