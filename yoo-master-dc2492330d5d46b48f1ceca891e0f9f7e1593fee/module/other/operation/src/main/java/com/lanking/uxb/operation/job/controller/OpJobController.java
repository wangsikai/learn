package com.lanking.uxb.operation.job.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqJobRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.job.JobMonitor;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.operation.common.ex.OperationConsoleException;
import com.lanking.uxb.operation.job.api.OpJobService;

/**
 * 任务管理相关接口
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年6月13日
 */
@RestController
@RequestMapping(value = "op/job")
public class OpJobController {

	@Autowired
	private OpJobService jobService;
	@Autowired
	private MqSender mqSender;

	@RequestMapping(value = "listAllJobDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAllJobDetails() {
		List<Map<String, Object>> items = jobService.findAllJobDetails();
		ValueMap vm = ValueMap.value("items", items);
		return new Value(vm);
	}

	@RequestMapping(value = "listAllTriggers", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAllTriggers() {
		List<Map<String, Object>> items = jobService.findAllTriggers();
		ValueMap vm = ValueMap.value("items", items);
		return new Value(vm);
	}

	@RequestMapping(value = "pause", method = { RequestMethod.GET, RequestMethod.POST })
	public Value pause(String triggerName, String triggerGroup) {
		JSONObject endJson = new JSONObject();
		endJson.put("action", "pause");
		endJson.put("triggerName", triggerName);
		endJson.put("triggerGroup", triggerGroup);
		mqSender.send(MqJobRegistryConstants.EX_JOB, MqJobRegistryConstants.RK_JOB_MONITOR,
				MQ.builder().data(endJson).build());
		return new Value();
	}

	@RequestMapping(value = "waiting", method = { RequestMethod.GET, RequestMethod.POST })
	public Value waiting(String triggerName, String triggerGroup) {
		JSONObject endJson = new JSONObject();
		endJson.put("action", "waiting");
		endJson.put("triggerName", triggerName);
		endJson.put("triggerGroup", triggerGroup);
		mqSender.send(MqJobRegistryConstants.EX_JOB, MqJobRegistryConstants.RK_JOB_MONITOR,
				MQ.builder().data(endJson).build());
		return new Value();
	}

	@RequestMapping(value = "queryMonitor", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryMonitor(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
		Pageable pageable = P.offset((page - 1) * pageSize, pageSize);
		Page<JobMonitor> monitorPage = jobService.query(pageable);
		VPage<JobMonitor> vPage = new VPage<JobMonitor>();
		vPage.setCurrentPage(page);
		vPage.setItems(monitorPage.getItems());
		vPage.setPageSize(pageSize);
		vPage.setTotal(monitorPage.getTotalCount());
		vPage.setTotalPage(monitorPage.getPageCount());
		return new Value(vPage);
	}

	@RequestMapping(value = "schedule", method = { RequestMethod.GET, RequestMethod.POST })
	public Value schedule(String jobGroup, String jobName, String jobDescription, String jobClassName) {
		JSONObject endJson = new JSONObject();
		endJson.put("action", "schedule");
		endJson.put("jobGroup", jobGroup);
		endJson.put("jobName", jobName);
		endJson.put("jobDescription", jobDescription);
		endJson.put("jobClassName", jobClassName);
		mqSender.send(MqJobRegistryConstants.EX_JOB, MqJobRegistryConstants.RK_JOB_MONITOR,
				MQ.builder().data(endJson).build());
		return new Value();
	}

	/**
	 * 
	 * @param triggerName
	 * @param cronExpression
	 * @return
	 */
	@RequestMapping(value = "updateCronExpression", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateCronExpression(String triggerName, String cronExpression) {
		if (jobService.countInQrtzFiredTriggers(triggerName) > 0) {
			return new Value(new OperationConsoleException(OperationConsoleException.OPERATION_QRTZ_TRIGGERNAME_ERROR));
		}
		jobService.updateCronExpression(triggerName, cronExpression);
		return new Value();
	}
}
