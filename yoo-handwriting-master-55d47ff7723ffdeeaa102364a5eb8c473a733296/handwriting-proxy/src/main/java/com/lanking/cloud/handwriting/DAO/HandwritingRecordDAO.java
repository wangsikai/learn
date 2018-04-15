package com.lanking.cloud.handwriting.DAO;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.base.hw.HandwritingRecord;

public interface HandwritingRecordDAO extends IHibernateDAO<HandwritingRecord, Long> {

	void create(long id, String scgInk);

	void response(long id, String response);
}