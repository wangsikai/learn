/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001User;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001UserQ;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycExamActivity01LotteryUserService;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

import httl.util.StringUtils;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycExamActivityLotteryUserServiceImpl implements ZycExamActivity01LotteryUserService {

	@Autowired
	@Qualifier("ExamActivity001UserRepo")
	private Repo<ExamActivity001User, Long> userRepo;
	
	@Autowired
	@Qualifier("ExamActivity001UserQRepo")
	private Repo<ExamActivity001UserQ, Long> userQRepo;

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
		if (null != form.getPhase()) {
			params.put("phase", form.getPhase());
		}
		if (null != form.getStatus()) {
			params.put("status", form.getStatus().getValue());
		}
		
	    return userRepo.find("$csQueryExamActivity01User", params).fetch(p, Map.class);
	}

	@Override
	public List<ExamActivity001UserQ> get(Long code, Long userId) {
		Params params = Params.param("code", code);
		
		params.put("userId", userId);
		
		return userQRepo.find("$csGetGifts", params).list();
	}

	@Override
	public Long getLotteryUserCount(Long code) {
		Params params = Params.param("code", code);
		
		return userRepo.find("$csCountGiftUser", params).get(Long.class);
	}
	
	@Transactional
	@Override
	public void updateReceived(long id, Integer received) {
		ExamActivity001User ieal = userRepo.get(id);
		ieal.setReceived(received);
		userRepo.save(ieal);
	}

	@Override
	public Long getLotteryTotalQ(Long code) {
		Params params = Params.param("code", code);
		
		return userRepo.find("$csCountTotalQ", params).get(Long.class);
	}

}
