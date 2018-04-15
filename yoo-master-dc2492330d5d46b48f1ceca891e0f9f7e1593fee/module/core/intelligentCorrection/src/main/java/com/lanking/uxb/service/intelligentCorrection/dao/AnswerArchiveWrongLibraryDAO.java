package com.lanking.uxb.service.intelligentCorrection.dao;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.base.intelligentCorrection.AnswerArchiveWrongLibrary;

/**
 * AnswerArchiveWrongLibrary 相关数据库操作接口
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月8日
 */
public interface AnswerArchiveWrongLibraryDAO extends IHibernateDAO<AnswerArchiveWrongLibrary, Long> {

	void delete(long answerId, String content);

	int update(long answerId, String content);
}