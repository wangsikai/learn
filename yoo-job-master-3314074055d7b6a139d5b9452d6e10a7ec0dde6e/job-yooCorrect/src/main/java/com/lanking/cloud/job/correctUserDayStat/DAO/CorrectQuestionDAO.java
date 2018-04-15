package com.lanking.cloud.job.correctUserDayStat.DAO;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;

/**
 * CorrectQuestion 相关数据库操作接口
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
public interface CorrectQuestionDAO extends IHibernateDAO<CorrectQuestion, Long> {

	/**
	 * 根据当前批改人查询已经批改完的题目
	 * 
	 * @param userId
	 *            当前分配批改人的ID
	 * @param date
	 * @param dateEnd
	 */
	List<CorrectQuestion> getCompleteQuestionsByUserId(Long userId, Date date, Date dateEnd);
}
