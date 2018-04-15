package com.lanking.cloud.job.correctUserDayStat.DAO;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.microservice.domain.yoocorrect.CorrectUserMonthStat;

/**
 * CorrectUserMonthStat 相关数据库操作接口
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
public interface CorrectUserMonthStatDAO extends IHibernateDAO<CorrectUserMonthStat, Long> {

	/**
	 * 取指定用户的所有统计
	 */
	List<CorrectUserMonthStat> getAllByUser(Long userId);
	
	/**
	 * 取指定月份的用户统计
	 * 
	 * @param userId
	 * @param monthDate
	 */
	CorrectUserMonthStat getByMonthDate(Long userId, Date monthDate);
}
