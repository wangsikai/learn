package com.lanking.cloud.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.lite.lifecycle.api.JobOperateAPI;
import com.google.common.base.Optional;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqReportRegistryConstants;
import com.lanking.cloud.job.PaperReportJob;

@Component
@Exchange(name = MqReportRegistryConstants.EX_PAPERREPORT)
public class PaperReportDataListener {
	@Autowired
	private JobOperateAPI jobOperateAPI;

	@Listener(queue = MqReportRegistryConstants.QUEUE_PAPERREPORT_DATA, routingKey = MqReportRegistryConstants.RK_PAPERREPORT_DATA)
	public void trigger(JSONObject jo) {
		String jobName = PaperReportJob.class.getSimpleName();
		jobOperateAPI.trigger(Optional.of(jobName), Optional.<String>absent());
	}
}
