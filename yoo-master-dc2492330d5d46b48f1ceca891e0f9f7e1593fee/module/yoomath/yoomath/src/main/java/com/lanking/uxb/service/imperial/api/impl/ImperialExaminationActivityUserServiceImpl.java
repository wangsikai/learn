package com.lanking.uxb.service.imperial.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityUserService;
import com.lanking.uxb.service.imperial.form.ImperialExaminationActivityUserForm;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityUserServiceImpl implements ImperialExaminationActivityUserService {

	@Autowired
	@Qualifier("ImperialExaminationActivityUserRepo")
	private Repo<ImperialExaminationActivityUser, Long> repo;

	@Transactional
	@Override
	public void signUp(ImperialExaminationActivityUserForm form) {
		ImperialExaminationActivityUser user = new ImperialExaminationActivityUser();
		user.setActivityCode(form.getCode());
		user.setClassList(form.getClassList());
		user.setCreateAt(new Date());
		user.setMobile(form.getMobile());
		user.setName(form.getName());
		user.setGrade(form.getGrade());
		user.setUserId(form.getUserId());
		user.setTextbookCategoryCode(form.getTextbookCategoryCode());
		user.setRoom(form.getRoom());
		repo.save(user);
	}

	@Override
	public ImperialExaminationActivityUser getUser(long code, long userId) {
		List<ImperialExaminationActivityUser> list = repo.find("$getByUser",
				Params.param("userId", userId).put("code", code)).list();
		return list.size() > 0 ? list.get(0) : null;
	}
}
