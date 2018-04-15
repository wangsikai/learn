package com.lanking.uxb.service.report.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.HomeworkRightRateStat;

public interface HomeworkRightRateStatService {

	/**
	 * 获取近期班级平均正确率,按周统计<br>
	 * 目前显示的最近三个月的
	 * 
	 * @param classId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<HomeworkRightRateStat> getStat(Long classId, Date startTime, Date endTime);

}
