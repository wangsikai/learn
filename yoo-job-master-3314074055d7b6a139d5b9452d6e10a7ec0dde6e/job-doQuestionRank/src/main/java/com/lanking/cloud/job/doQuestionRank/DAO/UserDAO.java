package com.lanking.cloud.job.doQuestionRank.DAO;

import java.util.Map;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * User 相关数据库操作接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月12日
 */
public interface UserDAO extends IHibernateDAO<User, Long> {

	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable);
}
