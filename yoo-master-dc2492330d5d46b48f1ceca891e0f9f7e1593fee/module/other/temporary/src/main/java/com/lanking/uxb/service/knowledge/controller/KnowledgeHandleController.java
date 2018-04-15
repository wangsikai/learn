package com.lanking.uxb.service.knowledge.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.knowledge.api.KnowledgeHandleService;

/**
 * 临时处理新知识点异常的作业记录
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "temp/kpConvert")
public class KnowledgeHandleController {

	@Autowired
	private KnowledgeHandleService kpHandleService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理homework 新知识点字段为空的
	 * 
	 * @return
	 */
	@RequestMapping(value = "doKpIsNull", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doKpIsNull() {
		logger.error("---start KnowledgeHandleController doKpIsNull----");
		List<Long> hkIds = kpHandleService.findKpIsNullList();
		for (Long homeworkId : hkIds) {
			List<Long> kps = kpHandleService.findNewKps(homeworkId);
			if (CollectionUtils.isNotEmpty(kps)) {
				kpHandleService.updateKp(homeworkId, kps);
			} else {
				logger.error("KnowledgeHandleController doKpIsNull error ,作业Id={}", homeworkId);
			}
		}
		logger.error("---end KnowledgeHandleController doKpIsNull----");
		return new Value();
	}

	/**
	 * 处理homework 新知识点填的是旧知识点的数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "doKpIsWrong", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doKpIsWrong() {
		logger.error("---start KnowledgeHandleController doKpIsWrong----");
		List<Long> hkIds = kpHandleService.findKpIsWrongList();
		for (Long homeworkId : hkIds) {
			List<Long> kps = kpHandleService.findNewKps(homeworkId);
			if (CollectionUtils.isNotEmpty(kps)) {
				kpHandleService.updateKp(homeworkId, kps);
			} else {
				logger.error("KnowledgeHandleController doKpIsWrong error ,作业Id={}", homeworkId);
			}
		}
		logger.error("---end KnowledgeHandleController doKpIsWrong----");
		return new Value();
	}
}
