package com.lanking.uxb.service.temp.api;

import java.util.List;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;

public interface CoinsGrowthLogHandle2Service {

	void save(HomeworkClazz clazz);

	Long logCount(int ruleCode, Long userId, Biz biz, Long bizId);

	List<HomeworkClazz> getClassList();

}
