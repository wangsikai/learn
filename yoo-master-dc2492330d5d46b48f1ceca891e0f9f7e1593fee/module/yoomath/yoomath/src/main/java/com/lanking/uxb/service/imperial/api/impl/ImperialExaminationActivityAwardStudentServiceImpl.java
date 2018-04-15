package com.lanking.uxb.service.imperial.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityAwardStudentService;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityAwardStudentServiceImpl
		implements ImperialExaminationActivityAwardStudentService {

	@Autowired
	@Qualifier("ImperialExaminationActivityAwardStudentRepo")
	private Repo<ImperialExaminationActivityAwardStudent, Long> repo;

	@Override
	public List<ImperialExaminationActivityAwardStudent> awardList(long code, int num) {
		Page<ImperialExaminationActivityAwardStudent> page = repo.find("$findEffectiveList", Params.param("code", code))
				.fetch(P.first(num));
		return page.getItems();
	}

	@Override
	public ImperialExaminationActivityAwardStudent getByUser(long code, long userId) {
		List<ImperialExaminationActivityAwardStudent> list = repo
				.find("$getByUser", Params.param("code", code).put("userId", userId)).list();
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public ImperialExaminationActivityAwardStudent get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void improve(long awardId, String contact, String mobile, String address) {
		ImperialExaminationActivityAwardStudent award = repo.get(awardId);
		if (award != null) {
			award.setAwardContact(contact);
			award.setAwardContactNumber(mobile);
			award.setAwardDeliveryAddress(address);
			repo.save(award);
		}
	}

}
