package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Maps;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.uxb.service.imperialExamination.api.AbstractImperialExaminationProcessHandle;
import com.lanking.uxb.service.imperialExamination.api.ImperialExaminationProcessService;
import com.lanking.uxb.service.imperialExamination.api.impl.handle.ProcessMessageHandle;

@Service
public class ImperialExaminationProcessServiceImpl implements ImperialExaminationProcessService,
		ApplicationContextAware {

	private ApplicationContext appContext;

	private Map<ImperialExaminationProcess, AbstractImperialExaminationProcessHandle> processHandles;
	@Autowired
	private ProcessMessageHandle messageHandle;
	
	@Override
	public void process(ImperialExaminationActivity activity) throws Exception {
		AbstractImperialExaminationProcessHandle handle = getHandle(activity);
		if (handle != null) {
			handle.process(activity.getCode());
		}

		// 信息发送时间不固定，单独一个handle
		if (!messageHandle.isProcessed(activity)) {
			messageHandle.process(activity);
		}
	}

	AbstractImperialExaminationProcessHandle getHandle(ImperialExaminationActivity activity) throws Exception {
		AbstractImperialExaminationProcessHandle handle = null;
		if (processHandles == null) {
			processHandles = Maps.newHashMap();
			for (AbstractImperialExaminationProcessHandle p : appContext.getBeansOfType(
					AbstractImperialExaminationProcessHandle.class).values()) {
				if (processHandles.containsKey(p.getProcess())) {
					throw new Exception();
				} else {
					processHandles.put(p.getProcess(), p);
				}
			}
		}
		ImperialExaminationProcessTime currentProcessTime = activity.getCfg().getCurretStage();
		if (currentProcessTime != null) {
			handle = processHandles.get(currentProcessTime.getProcess());
		}
		return handle;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}

}
