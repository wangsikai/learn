package com.lanking.uxb.service.stuweakkp.api.impl.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.uxb.service.stuweakkp.api.TaskYmStuExerciseWeakKnowStatService;

public class StuExerciseWeakKnowStatJob implements SimpleJob {
	@Autowired
	private TaskYmStuExerciseWeakKnowStatService ymStuExerciseWeakKnowStatService;

	@Override
	public void execute(ShardingContext shardingContext) {
		ymStuExerciseWeakKnowStatService.statWeakKnow(2);
	}

}
