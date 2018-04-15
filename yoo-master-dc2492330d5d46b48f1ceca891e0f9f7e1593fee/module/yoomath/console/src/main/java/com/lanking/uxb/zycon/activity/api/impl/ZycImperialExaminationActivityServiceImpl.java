/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityService;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycImperialExaminationActivityServiceImpl implements ZycImperialExaminationActivityService {

	@Autowired
	@Qualifier("ImperialExaminationActivityRepo")
	private Repo<ImperialExaminationActivity, Long> repo;

	@Override
	public ImperialExaminationActivity get(long code) {
		return repo.get(code);
	}

}
