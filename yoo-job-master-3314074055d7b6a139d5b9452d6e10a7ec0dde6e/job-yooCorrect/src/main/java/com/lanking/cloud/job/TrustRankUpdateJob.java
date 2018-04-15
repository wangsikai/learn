package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.CorrectUserTrustRank.service.CorrectUserTrustRankService;

/**
 * <p>Description:批改用户信用值更新job<p>
 * @author pengcheng.yu
 * @date 2018年3月26日
 * @since 小优秀快批
 */
public class TrustRankUpdateJob implements SimpleJob{

	@Autowired
	CorrectUserTrustRankService correctUserTrustRankService;
	@Override
	public void execute(ShardingContext arg0) {
		correctUserTrustRankService.doTrustRankUpdate();
	}

}
