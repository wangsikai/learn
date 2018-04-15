package com.lanking.cloud.job.CorrectUserTrustRank.DAO;

import com.lanking.microservice.domain.yoocorrect.TrustRankStatLog;

/**
 * <p>Description:<p>
 * @author pengcheng.yu
 * @date 2018年3月26日
 * @since 小优秀快批
 */
public interface CorrectUserTrustRankStatLogDao {
	TrustRankStatLog add(TrustRankStatLog trustRankStatLog);
	/**
	 * 根据主键查询
	 * @date: 2018年3月26日
	 * @author: pengcheng.yu
	 * @param id
	 * @return
	 */
	TrustRankStatLog get(Long id);
	/**
	 * 根据批改员id查询
	 * @date: 2018年3月26日
	 * @author: pengcheng.yu
	 * @param correctUserId
	 * @return
	 */
	TrustRankStatLog getByCorrectUserId(Long correctUserId);
	
	/**
	 * 更新记录
	 * @date: 2018年3月26日
	 * @author: pengcheng.yu
	 * @param id
	 * @param curQuestionCount 这次总共统计查询的习题数
	 * @param curErrorCount 这次统计查询的习题数中老师批改错误的习题数
	 */
	TrustRankStatLog update(Long id,Integer curQuestionCount,Integer curErrorCount);
	
	/**
	 * 每统计查询完5000道习题就将记录的教师批改错题数清0
	 * <p>Description:<p>
	 * @date: 2018年3月26日
	 * @author: pengcheng.yu
	 * @param id
	 */
	void clear(Long id);
}
