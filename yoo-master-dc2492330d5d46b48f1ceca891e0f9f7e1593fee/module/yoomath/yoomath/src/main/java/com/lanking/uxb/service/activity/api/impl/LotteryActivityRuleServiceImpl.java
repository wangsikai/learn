package com.lanking.uxb.service.activity.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.lottery.LotteryActivityRule;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.LotteryActivityRuleService;

/**
 * 活动规则接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年12月24日
 */
@Service
@Transactional(readOnly = true)
public class LotteryActivityRuleServiceImpl implements LotteryActivityRuleService {
	@Autowired
	@Qualifier("LotteryActivityRuleRepo")
	private Repo<LotteryActivityRule, Long> repo;

	@Override
	public List<LotteryActivityRule> listRulesByActivity(long activityCode) {
		return repo.find("$listRulesByActivity", Params.param("activityCode", activityCode)).list();
	}
}
