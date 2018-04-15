/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import httl.util.StringUtils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityUserService;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycImperialExaminationActivityUserServiceImpl implements ZycImperialExaminationActivityUserService {

	@Autowired
	@Qualifier("ImperialExaminationActivityUserRepo")
	private Repo<ImperialExaminationActivityUser, Long> repo;

	@Override
	public Page<Map> findActivityUser(ZycActivityUserForm form, Pageable p) {
		Params params = Params.param("activityCode", form.getActivityCode());
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getChannelName())) {
			params.put("channelName", '%' + form.getChannelName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (null != form.getGrade()) {
			params.put("grade", form.getGrade().getValue());
		}
		if (null != form.getRoom() && 0 != form.getRoom()) {
			params.put("room", form.getRoom());
		}
		if (null != form.getCategory()) {
			params.put("category", form.getCategory());
		}
		return repo.find("$queryActivityUser", params).fetch(p, Map.class);
	}
}
