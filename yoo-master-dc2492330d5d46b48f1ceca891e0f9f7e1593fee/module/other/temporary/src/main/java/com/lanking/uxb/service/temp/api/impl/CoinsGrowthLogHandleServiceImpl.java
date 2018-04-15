package com.lanking.uxb.service.temp.api.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.uxb.service.temp.api.CoinsGrowthLogHandle2Service;
import com.lanking.uxb.service.temp.api.CoinsGrowthLogHandleService;

@Service
public class CoinsGrowthLogHandleServiceImpl implements CoinsGrowthLogHandleService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CoinsGrowthLogHandle2Service handle2Service;

	@Override
	public void handle() {
		// 大于等于20人的班级
		List<HomeworkClazz> clazzList = handle2Service.getClassList();
		for (HomeworkClazz clazz : clazzList) {
			Long count = handle2Service.logCount(107, clazz.getTeacherId(), null, null);
			try {
				if (count < 3) {
					Long currentCount = handle2Service.logCount(107, clazz.getTeacherId(), Biz.CLASS, clazz.getId());
					if (currentCount == 0) {
						handle2Service.save(clazz);
					}
				}
			} catch (Exception e) {
				logger.error("CoinsGrowthLogHandle", e);
			}
		}
	}

}
