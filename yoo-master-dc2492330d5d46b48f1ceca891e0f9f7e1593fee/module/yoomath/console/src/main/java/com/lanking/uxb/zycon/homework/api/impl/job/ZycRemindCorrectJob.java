package com.lanking.uxb.zycon.homework.api.impl.job;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.support.console.common.CorrectUser;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.operation.api.ZycCorrectUserService;

/**
 * 提醒运营人员批改题目 2小时触发一次，当题目数量>100时发送短信。
 *
 * @author xinyu.zhou
 * @since yoomath 1.9
 */
public class ZycRemindCorrectJob implements SimpleJob {
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycCorrectUserService correctUserService;
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionService stuHomeworkItemQuestionService;
	@Autowired
	private MessageSender messageSender;

	@Override
	public void execute(ShardingContext shardingContext) {
		Integer lastCommitMinutes = Env.getInt("homework.allcommit.then");
		int remindBound = Env.getInt("zycon.remindBound");

		long unCorrectQuestionCount = questionService.countUnCommitQuestions(lastCommitMinutes);
		long holidayUnCorrectQuestionCount = stuHomeworkItemQuestionService.countNotCorrectQuestions();
		if (unCorrectQuestionCount > remindBound || holidayUnCorrectQuestionCount > remindBound) {
			List<CorrectUser> users = correctUserService.list();
			List<String> targets = new ArrayList<String>(users.size());
			for (CorrectUser user : users) {
				if (user.getStatus() == Status.ENABLED) {
					targets.add(user.getMobile());
				}
			}
			messageSender.send(new SmsPacket(targets, 10000015, ValueMap.value("remindBound", remindBound)));
		}
	}

}
