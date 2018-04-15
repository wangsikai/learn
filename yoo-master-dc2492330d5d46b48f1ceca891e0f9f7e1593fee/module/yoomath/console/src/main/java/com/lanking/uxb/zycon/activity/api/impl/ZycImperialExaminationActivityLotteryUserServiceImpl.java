/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityLotteryUserService;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

import httl.util.StringUtils;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycImperialExaminationActivityLotteryUserServiceImpl implements ZycImperialExaminationActivityLotteryUserService {

	@Autowired
	@Qualifier("ImperialExaminationActivityLotteryRepo")
	private Repo<ImperialExaminationActivityLottery, Long> repo;

	@Override
	public ImperialExaminationActivityLottery get(Long id) {
		return repo.get(id);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryActivityLotteryUser(ZycActivityUserForm form, Pageable p) {
		Params params = Params.param("activityCode", form.getActivityCode());
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getRealName())) {
			params.put("realName", '%' + form.getRealName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (null != form.getStatus()) {
			params.put("status", form.getStatus().getValue());
		}
		if (null != form.getProcess()) {
			params.put("process", form.getProcess());
		}
		if (null != form.getRoom() && 0 != form.getRoom()) {
			params.put("room", form.getRoom());
		}
		//查询中奖老师
		if(null != form.getRole() && form.getRole() == 1){
			return repo.find("$queryActivityLotteryTeacher", params).fetch(p, Map.class);
		} else { 
			//否则要查询中奖的学生
			return repo.find("$queryActivityLotteryStudent", params).fetch(p, Map.class);
		}

	}

	@Transactional
	@Override
	public void updateReceived(long id, Integer received) {
		ImperialExaminationActivityLottery ieal = repo.get(id);
		ieal.setReceived(received);
		repo.save(ieal);
	}


}
