package com.lanking.uxb.service.intelligentCorrection.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqIntelligentCorrectionRegistryConstants;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveService;
import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveWrongLibraryService;
import com.lanking.uxb.service.intelligentCorrection.util.AnswerFilterUtil;

@Component
@Exchange(name = MqIntelligentCorrectionRegistryConstants.EX_INTELLIGENTCORRECTION)
public class MqIntelligentCorrectionListener {

	private Logger logger = LoggerFactory.getLogger(MqIntelligentCorrectionListener.class);

	@Autowired
	private AnswerArchiveService answerArchiveService;
	@Autowired
	private AnswerArchiveWrongLibraryService answerArchiveWrongLibraryService;

	@Listener(queue = MqIntelligentCorrectionRegistryConstants.QUEUE_INTELLIGENTCORRECTION_ARCHIVE, routingKey = MqIntelligentCorrectionRegistryConstants.RK_INTELLIGENTCORRECTION_ARCHIVE)
	public void archive(JSONObject json) {
		try {
			long answerId = json.getLong("answerId");
			String content = json.getString("content");
			if (StringUtils.isNotBlank(content)) {
				content = AnswerFilterUtil.tagFilter(content);
				HomeworkAnswerResult result = json.getObject("result", HomeworkAnswerResult.class);
				if (result == HomeworkAnswerResult.RIGHT) {
					answerArchiveService.createRightAnswer(answerId, content);
					answerArchiveWrongLibraryService.delete(answerId, content);
				} else if (result == HomeworkAnswerResult.WRONG) {
					answerArchiveService.deleteRightAnswer(answerId, content);
					answerArchiveWrongLibraryService.update(answerId, content);
				}
			}
		} catch (Exception e) {
			logger.error("archive error:{}", e, json.toString());
		}
	}
}