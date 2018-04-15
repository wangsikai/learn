package com.lanking.uxb.operation.question.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.operation.question.api.OpQuestionManage;
import com.lanking.uxb.operation.question.controller.OpQuestionController;
import com.lanking.uxb.operation.wordml.api.OpWordMLQuestionManage;
import com.lanking.uxb.service.question.api.QuestionQRCodeService;

@Service
public class OpQuestionManageImpl implements OpQuestionManage {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private QuestionQRCodeService questionQRCodeService;
	@Autowired
	private OpWordMLQuestionManage opQuestionManage;

	@Override
	public void rebuildQuestionQRCodeImage(long minId) {
		int pageSize = 200;
		int page = 1;
		int count = 0;
		try {
			Page<Question> p = opQuestionManage.wordMLQueryByPage(0, minId, P.index(page, pageSize));
			if (p.getItemSize() > 0) {
				logger.info("重建习题二维码：" + count + "/" + p.getTotalCount());
				if (OpQuestionController.stop) {
					OpQuestionController.stop = false;
					OpQuestionController.building = false;
					return;
				}
				questionQRCodeService.batchMakeQRCodeImage(p.getItems());
				count += p.getItemSize();
				logger.info("重建习题二维码：习题个数=" + count + "/" + p.getTotalCount());
			}
			while (p.isNotEmpty()) {
				if (OpQuestionController.stop) {
					OpQuestionController.stop = false;
					OpQuestionController.building = false;
					return;
				}
				page++;
				p = opQuestionManage.wordMLQueryByPage(0, minId, P.index(page, pageSize));
				if (p.getItemSize() > 0) {
					logger.info("重建习题二维码：" + count + "/" + p.getTotalCount());
					questionQRCodeService.batchMakeQRCodeImage(p.getItems());
					count += p.getItemSize();
					logger.info("重建习题二维码：习题个数=" + count + "/" + p.getTotalCount());
				}
			}
		} catch (Exception e) {
			logger.error("重建习题二维码出错！", e);
		}
		OpQuestionController.stop = false;
		OpQuestionController.building = false;
	}
}
