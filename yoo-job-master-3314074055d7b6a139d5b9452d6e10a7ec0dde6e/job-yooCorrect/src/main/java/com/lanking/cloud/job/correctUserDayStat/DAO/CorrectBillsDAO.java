package com.lanking.cloud.job.correctUserDayStat.DAO;

import java.util.List;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.microservice.domain.yoocorrect.CorrectBills;

/**
 * CorrectBills 相关数据库操作接口
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
public interface CorrectBillsDAO extends IHibernateDAO<CorrectBills, Long> {

	/**
	 * 根据CorrectQuestionIds取所有CorrectBills
	 * 
	 * @param CorrectQuestionIds
	 */
	List<CorrectBills> getByCorrectQuestionIds(List<Long> correctQuestionIds);
	
	/**
	 * 分页查询
	 * @param correctUserId
	 * @param page
	 * @return
	 */
	Page<CorrectBills> query(Long correctUserId,Pageable page);
	/**
	 * 得到错该题数量
	 * @param correctUserId
	 * @param correctQuestionIds
	 * @return
	 */
	Long getCorrectErrorCount(Long correctUserId,List<Long> correctQuestionIds);
}
