package com.lanking.uxb.service.wx.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.thirdparty.api.WXService;
import com.lanking.uxb.service.thirdparty.weixin.template.PublishHomeworkTemplate;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;

/**
 * 作业相关的微信消息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
@Service
@Transactional(readOnly = true)
public class ZyWXMessageServiceImpl implements ZyWXMessageService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private WXService wxService;
	@Autowired
	private HomeworkService hkService;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@SuppressWarnings("rawtypes")
	@Override
	@Async
	public void sendPublishHomeworkMessage(Collection<Long> homeworkClassIds, long homeworkId,
			Collection<Long> groupIds) {
		// if (null == homeworkClassIds || homeworkClassIds.size() == 0) {
		// return;
		// }
		Homework homework = hkService.get(homeworkId);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, homework.getCreateId());

		List<Map> users = new ArrayList<Map>();
		if (CollectionUtils.isNotEmpty(homeworkClassIds)) {
			// 查找需要提醒的学生
			List<Map> users1 = homeworkStudentClazzRepo.find("$findWXStudentByClass",
					Params.param("type", CredentialType.WEIXIN_MP.getValue()).put("homeworkClassIds", homeworkClassIds))
					.list(Map.class);
			users.addAll(users1);
		}

		if (CollectionUtils.isNotEmpty(groupIds)) {
			// 查找需要提醒的学生
			List<Map> users2 = homeworkStudentClazzRepo
					.find("$findWXStudentByClassGroup",
							Params.param("type", CredentialType.WEIXIN_MP.getValue()).put("groupIds", groupIds))
					.list(Map.class);
			users.addAll(users2);
		}

		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date();
		String url = "";

		logger.info("[WEIXIN] begin sendPublishHomeworkMessage , find users! homeworkId: {}, homeworkClassIds: {}",
				homeworkId, homeworkClassIds);

		for (Map user : users) {
			logger.info("[WEIXIN] begin sendPublishHomeworkMessage -> user name : {}, openid: {}",
					user.get("name").toString(), user.get("openid").toString());

			StringBuffer buffer1 = new StringBuffer();
			StringBuffer buffer2 = new StringBuffer();
			buffer1.append(teacher.getName()).append("老师").append(format.format(homework.getCreateAt()))
					.append("布置了作业《").append(homework.getName()).append("》");
			buffer2.append("请您监督孩子在").append(format.format(homework.getStartTime())).append("——")
					.append(format.format(homework.getDeadline())).append("完成");
			PublishHomeworkTemplate template = new PublishHomeworkTemplate();
			template.setFirst(user.get("name").toString() + "家长：");
			template.setKeyword1(buffer1.toString());
			template.setKeyword2("悠数学");
			template.setKeyword3(format.format(date));
			template.setRemark(buffer2.toString());
			wxService.sendTemplateMessage(template, url, user.get("openid").toString(), Product.YOOMATH);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Async
	public void sendIssuedHomeworkMessage(long homeworkId) {
		Homework homework = hkService.get(homeworkId);
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, homework.getCreateId());

		// 查找需要提醒的学生
		List<Map> users = homeworkStudentClazzRepo
				.find("$findWXStudentByHomework",
						Params.param("type", CredentialType.WEIXIN_MP.getValue()).put("homeworkId", homeworkId))
				.list(Map.class);

		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		Date date = new Date();
		String url = Env.getString("weixin.page.student.homeworkRemind");
		for (Map user : users) {
			StringBuffer buffer1 = new StringBuffer();
			StringBuffer buffer2 = new StringBuffer();
			buffer1.append(teacher.getName()).append("老师").append(format.format(homework.getCreateAt()))
					.append("布置的作业《").append(homework.getName()).append("》已批改");
			buffer2.append("");
			PublishHomeworkTemplate template = new PublishHomeworkTemplate();
			template.setFirst(user.get("name").toString() + "家长：");
			template.setKeyword1(buffer1.toString());
			template.setKeyword2("悠数学");
			template.setKeyword3(format.format(date));
			template.setRemark("点击查看详情");
			wxService.sendTemplateMessage(template, url, user.get("openid").toString(), Product.YOOMATH);
		}
	}
}
