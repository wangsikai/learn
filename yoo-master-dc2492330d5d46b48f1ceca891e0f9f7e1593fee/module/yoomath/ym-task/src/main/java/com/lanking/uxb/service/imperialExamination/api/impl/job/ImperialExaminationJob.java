 package com.lanking.uxb.service.imperialExamination.api.impl.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.imperialExamination.api.ImperialExaminationProcessService;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityService;

public class ImperialExaminationJob implements SimpleJob {
	private Logger logger = LoggerFactory.getLogger(ImperialExaminationJob.class);
	@Autowired
	private TaskImperialExaminationActivityService taskImperialExaminationActivityService;
	@Autowired
	private ImperialExaminationProcessService imperialExaminationProcessService;

	@Override
	public void execute(ShardingContext shardingContext) {

		List<ImperialExaminationActivity> allActivitys = taskImperialExaminationActivityService
				.listAllProcessingActivity();
		if (CollectionUtils.isNotEmpty(allActivitys)) {
			for (ImperialExaminationActivity activity : allActivitys) {
				try {
					imperialExaminationProcessService.process(activity);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("process activity:{} fail", JSONObject.toJSON(activity));
				}
			}
		}
	}

}
