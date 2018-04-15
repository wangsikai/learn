package com.lanking.uxb.operation.wordml.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.operation.wordml.api.OpWordMLCacheManage;

/**
 * WordML缓存管理.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月5日
 */
@RestController
@RequestMapping("op/wordml")
public class OpWordMLCacheController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OpWordMLCacheManage wordMLCacheManage;

	public static boolean stop = false; // 中止
	public static boolean building = false; // 重建中

	/**
	 * 重新创建索引.
	 * 
	 * @param type
	 *            方式：0：全部重建，1：保留缓存表中数据，没有的从question表中重建
	 * @return
	 */
	@RequestMapping(value = { "rebuild" })
	public Value rebuild(HttpServletRequest request, Integer type, Long minId) {
		if (OpWordMLCacheController.building) {
			return new Value(1);
		}
		OpWordMLCacheController.building = true;
		type = type == null ? 0 : type;
		minId = minId == null ? 0 : minId;
		try {
			String host = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
			wordMLCacheManage.rebuildQuestionWordML(type, host, minId);
			return new Value(0);
		} catch (AbstractException e) {
			logger.error("重建习题WordML缓存出错！", e);
			OpWordMLCacheController.building = false;
			return new Value(e);
		}
	}

	/**
	 * 终止.
	 * 
	 * @return
	 */
	@RequestMapping(value = { "stop" })
	public Value stop() {
		if (!OpWordMLCacheController.building) {
			return new Value(0);
		} else {
			OpWordMLCacheController.stop = true;
			return new Value(1);
		}
	}
}
