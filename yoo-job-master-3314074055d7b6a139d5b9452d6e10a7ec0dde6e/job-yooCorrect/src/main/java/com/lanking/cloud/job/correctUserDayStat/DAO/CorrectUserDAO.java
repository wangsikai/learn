package com.lanking.cloud.job.correctUserDayStat.DAO;

import java.util.Map;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;

/**
 * User 相关数据库操作接口
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
public interface CorrectUserDAO extends IHibernateDAO<CorrectUser, Long> {

	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable);
}
