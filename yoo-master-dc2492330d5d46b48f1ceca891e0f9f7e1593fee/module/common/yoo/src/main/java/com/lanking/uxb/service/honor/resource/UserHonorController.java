package com.lanking.uxb.service.honor.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 荣誉-相关rest API
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
@RestController
@RequestMapping("honor")
public class UserHonorController {
	private Logger logger = LoggerFactory.getLogger(UserHonorController.class);
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private GrowthRuleService growthRuleService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserTaskService userTaskService;

	/**
	 * 签到接口
	 * 
	 * @since yoomath V1.8
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "checkIn", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkIn() {
		VUserReward vUserReward = null;
		// 判断是否已经签到过
		if (Security.getUserType() == UserType.STUDENT) {
			Date date = new Date();
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
			}
			UserTaskLog log = userTaskLogService.findByCodeAndUser(101010001, Security.getUserId(), date);
			if (log == null) {
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101010001);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", Security.isClient());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG_101010001,
						MQ.builder().data(messageObj).build());
			}
			int total = 0;
			long sleep = 500;
			sleep(sleep);
			log = userTaskLogService.findByCodeAndUser(101010001, Security.getUserId(), date);
			while (log == null && total < 3000) {
				sleep(sleep);
				total += sleep;
				logger.debug("sleep:{}", sleep);
				log = userTaskLogService.findByCodeAndUser(101010001, Security.getUserId(), date);
			}
			UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
			if (honor.getLevel() > 1) {
				int code = 101020001;
				code += honor.getLevel() - 2;
				UserTask userTask = userTaskService.get(code);
				honor.setUpRewardCoins(userTask.getUserTaskRuleCfg().getCoinsValue());
			} else {
				honor.setUpgrade(false);
			}

			vUserReward = new VUserReward(honor.getUpRewardCoins(), honor.isUpgrade(), honor.getLevel(),
					log.getGrowth(), log.getCoins());
			String content = log.getContent();
			JSONObject contentJsonObj = JSONObject.parseObject(content);
			vUserReward.setCheckInCount(contentJsonObj.getLong("signDay"));
			return new Value(vUserReward);
		} else {
			String todayKey = growthCacheService.getTodayKey(GrowthAction.DAILY_CHECKIN, Security.getUserId());
			int todayGrowth = (int) growthCacheService.get(todayKey);
			if (todayGrowth == -1) {

				GrowthLog growthlog = growthService.grow(GrowthAction.DAILY_CHECKIN, Security.getUserId(), true);
				CoinsLog coinslog = coinsService.earn(CoinsAction.DAILY_CHECKIN, Security.getUserId());
				vUserReward = new VUserReward(growthlog.getHonor().getUpRewardCoins(), growthlog.getHonor().isUpgrade(),
						growthlog.getHonor().getLevel(), growthlog.getGrowthValue(), coinslog.getCoinsValue());
				String continuousKey = growthCacheService.getContinuousCheckInKey(GrowthAction.DAILY_CHECKIN,
						Security.getUserId());
				// 先判断缓存里是否存在，不存在去数据库查
				if (growthCacheService.get(continuousKey) != -1) {
					vUserReward.setCheckInCount(growthCacheService.get(continuousKey));
				} else {
					GrowthLog lastestGlog = growthLogService.getLastestCheckIn(Security.getUserId(),
							growthRuleService.getByAction(GrowthAction.DAILY_CHECKIN).getCode());
					vUserReward.setCheckInCount(Long.parseLong(lastestGlog.getP1()));
				}

			}
		}
		return new Value(vUserReward);
	}

	/**
	 * @param i
	 */
	private void sleep(long i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
