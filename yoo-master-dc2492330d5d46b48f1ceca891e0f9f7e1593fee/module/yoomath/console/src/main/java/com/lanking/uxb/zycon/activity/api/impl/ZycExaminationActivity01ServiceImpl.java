/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycExaminationActivity01Service;

/**
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycExaminationActivity01ServiceImpl implements ZycExaminationActivity01Service {

	@Autowired
	@Qualifier("ExamActivity001Repo")
	private Repo<ExamActivity001, Long> repo;

	@Override
	public ExamActivity001 get(long code) {
		return repo.find("$csGetActivity", Params.param("code", code)).get();
	}

}
