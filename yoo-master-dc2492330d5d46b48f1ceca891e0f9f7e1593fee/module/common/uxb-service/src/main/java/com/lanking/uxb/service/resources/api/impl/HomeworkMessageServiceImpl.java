package com.lanking.uxb.service.resources.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessage;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessageScene;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessageType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.resources.api.HomeworkMessageService;
import com.lanking.uxb.service.session.api.impl.Security;

@Transactional(readOnly = true)
@Service
public class HomeworkMessageServiceImpl implements HomeworkMessageService {
	
	@Autowired
	@Qualifier("HomeworkMessageRepo")
	Repo<HomeworkMessage, Long> homeworkMessageRepo;

	@Override
	public List<HomeworkMessage> findByStudentHkId(long stuHkId) {
		Params param = Params.param("id", stuHkId);
		param.put("scene", 1);
		
		return homeworkMessageRepo.find("$findHomeworkMessages", param).list();
	}

	@Override
	public List<HomeworkMessage> findByStudentHkQId(long stuHkQId) {
		Params param = Params.param("id", stuHkQId);
		param.put("scene", 0);
		
		return homeworkMessageRepo.find("$findHomeworkMessages", param).list();
	}
	
	@Override
	public List<HomeworkMessage> findByHkId(long hkId) {
		Params param = Params.param("id", hkId);
		param.put("scene", 2);
		
		return homeworkMessageRepo.find("$findHomeworkMessages", param).list();
	}

	@Override
	@Transactional
	public Long addComment(Integer voiceTime, String fileKey, Long id, HomeworkMessageType type, Integer scene, String comment,
			String iconKey,UserType userType) {
		HomeworkMessage message = new HomeworkMessage();
		message.setBizId(id);
		message.setVoiceFileKey(fileKey);
		message.setVoiceTime(voiceTime);
		message.setComment(comment);
		message.setCreator(Security.getUserId());
		message.setCreateAt(new Date());
		message.setUserType(userType);
		message.setIconKey(iconKey);
		message.setStatus(Status.ENABLED);
		message.setScene(HomeworkMessageScene.findByValue(scene));
		message.setType(type);
		
		homeworkMessageRepo.save(message);
		
		return message.getId();
	}

	@Override
	public List<HomeworkMessage> findMessages(long id, long userId, Integer scene) {
		Params param = Params.param("id", id);
		param.put("scene", scene);
		param.put("userId", userId);
		
		return homeworkMessageRepo.find("$findHomeworkMessages", param).list();
	}

	@Override
	public HomeworkMessage get(long id) {
		return homeworkMessageRepo.get(id);
	}

	@Override
	@Transactional
	public void updateStatus(long id, Status status) {
		HomeworkMessage message = homeworkMessageRepo.get(id);
		message.setStatus(status);
		homeworkMessageRepo.save(message);
	}

	@Override
	public Boolean isMessagesExceed(long id, long userId, Integer scene) {
		Params param = Params.param("id", id);
		param.put("scene", scene);
		param.put("userId", userId);
		
		Integer count = homeworkMessageRepo.find("$findHomeworkMessagesCount", param).get(Integer.class);
		
		return count >= 20;
	}


}
