package com.lanking.intercomm.yoocorrect.dto;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

/**
 * 快批奖励设置.
 *
 */
@Getter
@Setter
public class CorrectRewardConfig {

	/**
	 * 每日奖励（多个批改题量设置，按题量数量从小到大排列）.
	 */
	List<CorrectReward> dayCorrectRewards = Lists.newArrayList();
}
