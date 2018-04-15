package com.lanking.cloud.job.nationalDayActivity.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqActivityRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.job.nationalDayActivity.DAO.HomeworkDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01DAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01HomeworkDAO;
import com.lanking.cloud.job.nationalDayActivity.service.ActivityHomeworkService;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01Service;

@Service("nda01ActivityHomeworkService")
@Transactional(readOnly = true)
public class ActivityHomeworkServiceImpl implements ActivityHomeworkService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01HomeworkDAO")
	private NationalDayActivity01HomeworkDAO nationalDayActivity01HomeworkDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01DAO")
	private NationalDayActivity01DAO nda01DAO;
	@Autowired
	@Qualifier("nda01HomeworkDAO")
	private HomeworkDAO homeworkDAO;
	@Autowired
	private MqSender mqSender;

	@Transactional
	@Override
	public void publishHomework(long teacherId, long homeworkId) {
		NationalDayActivity01 nda01 = nda01DAO.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);
		// 保存新的作业记录
		long now = System.currentTimeMillis();
		if (nda01.getStartTime().getTime() <= now && nda01.getEndTime().getTime() >= now) {// 当前时间在活动时间内
			// 判断当前有没有参与活动的作业，如果没有则从homework表中拉取除了当前处理的homeworkId所有符合活动范围的有效作业
			if (nationalDayActivity01HomeworkDAO.hasNoHomework()) {
				List<Homework> homeworks = homeworkDAO.pullHomework(homeworkId, nda01.getStartTime(),
						nda01.getEndTime(), NationalDayActivity01Service.MIN_DISTRIBUTE_COUNT);
				for (Homework hk : homeworks) {
					nationalDayActivity01HomeworkDAO.create(hk.getCreateId(), hk.getId());
				}
			} else {
				Homework hk = homeworkDAO.get(homeworkId);
				if (hk.getDistributeCount() >= NationalDayActivity01Service.MIN_DISTRIBUTE_COUNT
						&& hk.getStartTime().getTime() >= nda01.getStartTime().getTime()
						&& hk.getStartTime().getTime() <= nda01.getEndTime().getTime()) {// 作业分发数量>20&&作业开始时间在活动时间内
					nationalDayActivity01HomeworkDAO.create(teacherId, homeworkId);
				}
			}
		}
	}

	@Transactional
	@Override
	public void deleteHomework(long homeworkId) {
		NationalDayActivity01 nda01 = nda01DAO.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);
		long now = System.currentTimeMillis();
		if (nda01.getStartTime().getTime() <= now && nda01.getEndTime().getTime() >= now) {// 当前时间在活动时间内
			nationalDayActivity01HomeworkDAO.delete(homeworkId);
		}
	}

	@Override
	public void issueHomework(long homeworkId) {
		NationalDayActivity01 nda01 = nda01DAO.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);
		Homework hk = homeworkDAO.get(homeworkId);
		if (hk.getStartTime().getTime() >= nda01.getStartTime().getTime()
				&& hk.getStartTime().getTime() <= nda01.getEndTime().getTime()
				&& hk.getIssueAt().getTime() >= nda01.getStartTime().getTime()
				&& hk.getIssueAt().getTime() <= nda01.getEndTime().getTime()) {// 开始时间和下发时间都在活动时间内的
			Map<Long, List<Long>> maps = homeworkDAO.queryRightQuestion(homeworkId);
			for (Long studentId : maps.keySet()) {
				List<Long> questionIds = maps.get(studentId);
				JSONObject jo = new JSONObject();
				jo.put("studentId", studentId);
				jo.put("questionIds", questionIds);
				jo.put("doAt", hk.getIssueAt().getTime());
				mqSender.asynSend(MqActivityRegistryConstants.EX_NDA01, MqActivityRegistryConstants.RK_NDA01_DQ,
						MQ.builder().data(jo).build());
			}
		}
	}

}
