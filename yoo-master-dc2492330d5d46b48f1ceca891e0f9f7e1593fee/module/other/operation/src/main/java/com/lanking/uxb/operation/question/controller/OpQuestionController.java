package com.lanking.uxb.operation.question.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.operation.question.api.OpQuestionManage;

/**
 * 习题相关操作.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月8日
 */
@RestController
@RequestMapping(value = "op/question")
public class OpQuestionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OpQuestionManage opQuestionManage;

	public static boolean stop = false; // 中止
	public static boolean building = false; // 重建中

	/**
	 * 重新创建习题二维码.
	 * 
	 * @return
	 */
	@RequestMapping(value = { "rebuild" })
	public Value rebuild(HttpServletRequest request, Long minId) {
		if (OpQuestionController.building) {
			return new Value(1);
		}
		OpQuestionController.building = true;
		minId = minId == null ? 0 : minId;
		try {
			opQuestionManage.rebuildQuestionQRCodeImage(minId);
			return new Value(0);
		} catch (AbstractException e) {
			logger.error("重新创建习题二维码出错！", e);
			OpQuestionController.building = false;
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
		if (!OpQuestionController.building) {
			return new Value(0);
		} else {
			OpQuestionController.stop = true;
			return new Value(1);
		}
	}
}
