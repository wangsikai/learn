package com.lanking.uxb.service.examactivity001.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001User;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01UserService;

/**
 * 期末考试活动礼包相关接口
 * 
 * @author qiuxue.jiang
 *
 * @version 2017年12月27日
 */
@Service
@Transactional(readOnly = true)
public class ExamActivity01UserServiceImpl implements ExamActivity01UserService {
	
	@Autowired
	@Qualifier("ExamActivity001UserRepo")
	private Repo<ExamActivity001User, Long> userRepo;
	

	@Override
	public ExamActivity001User getUser(Long code, Long userId) {
		Params userParams = Params.param("code", code);
		
		userParams.put("userId", userId);
		
		ExamActivity001User user = userRepo.find("$ymFindUser", userParams).get(ExamActivity001User.class);
		
		return user;
	}


	@Override
	@Transactional
	public void addUser(ExamActivity001User user) {
		userRepo.save(user);
	}


	@Override
	@Transactional
	public void updateUser(ExamActivity001User user) {
		Params userParams = Params.param("id", user.getId());
		
		userParams.put("grade", user.getGrade());
		userParams.put("category", user.getTextbookCategoryCode());
		
		userRepo.find("$ymUpdateUser", userParams).execute();
	}

}
