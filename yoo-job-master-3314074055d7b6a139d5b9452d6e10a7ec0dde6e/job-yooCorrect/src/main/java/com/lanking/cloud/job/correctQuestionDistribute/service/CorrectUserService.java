package com.lanking.cloud.job.correctQuestionDistribute.service;

import java.util.List;

import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;
import com.lanking.microservice.domain.yoocorrect.CorrectUserPool;
import com.lanking.microservice.domain.yoocorrect.CorrectUserType;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月8日
 * @since 小优秀快批
 */
public interface CorrectUserService {
	/**
	 * <p>
	 * Description:根据批改用户类型获取等待批改的用户
	 * <p>
	 * 
	 * @date: 2018年3月8日
	 * @author: pengcheng.yu
	 * @param type
	 * @return
	 */
	List<CorrectUser> queryWaitToCoorecUsersByType(CorrectUserType type);

	/**
	 * <p>Description:<p>
	 * @date: 2018年3月10日
	 * @author: pengcheng.yu
	 * @param correctQuestionId
	 * @param correctUsreId
	 * @param canReceive 是否可以继续接收派发的题
	 */
	void distributeCorrectQuestion(CorrectQuestion question, Long correctUsreId);
	
	/**
	 * <p>Description:查询已经被分配过题目的用户相关信息<p>
	 * @date: 2018年3月10日
	 * @author: pengcheng.yu
	 * @return
	 */
	List<CorrectUserPool> findHasDistributedUsers();
	
	/**
	 * 删除超时未处理的批改用户池相关数据
	 * <p>Description:<p>
	 * @date: 2018年3月10日
	 * @author: pengcheng.yu
	 * @param correctUserPoolIds
	 */
	void deleteTimeOutCorrectUserPools(List<Long> correctUserPoolIds);
	/**
	 * 查询所有可用批改用户
	 * @return
	 */
	List<Long> findAllCorrectUserIds();
	/**
	 * 更新批改用户信任值
	 * @param id
	 * @param trustRank
	 */
	void updateTrustRank(Long id,Integer trustRank);
}
