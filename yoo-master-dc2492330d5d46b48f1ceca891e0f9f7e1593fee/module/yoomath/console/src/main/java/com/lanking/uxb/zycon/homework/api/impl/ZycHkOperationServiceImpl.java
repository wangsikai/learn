package com.lanking.uxb.zycon.homework.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycHkOperationService;

@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ZycHkOperationServiceImpl implements ZycHkOperationService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;

	@Override
	public Long queryQuestionId(long stuHkId, int index) {
		return homeworkRepo.find("$zycQueryQuestionId", Params.param("stuHkId", stuHkId).put("idx", index)).get(
				Long.class);
	}

	@Override
	public List<String> findStandardAnswer(long stuHkId, int index, long qId) {
		return homeworkRepo.find("$zycFindStandardAnswer",
				Params.param("stuHkId", stuHkId).put("idx", index).put("qId", qId)).list(String.class);
	}

	@Override
	public List<String> findStudentAnswer(long stuHkId, int index, long qId, Boolean newCorrect) {
		Params param = Params.param("stuHkId", stuHkId);
		
		param.put("idx", index);
		param.put("qId", qId);
		
		//如果是订正题，需要查询订正题的答案
		if(newCorrect){
			param.put("newCorrect", newCorrect);
		} else {
			//否则，查询非订正题的答案
			param.put("noCorrect", 1);
		}
		
		return homeworkRepo.find("$zycFindStudentAnswer",param).list(String.class);
	}

}
