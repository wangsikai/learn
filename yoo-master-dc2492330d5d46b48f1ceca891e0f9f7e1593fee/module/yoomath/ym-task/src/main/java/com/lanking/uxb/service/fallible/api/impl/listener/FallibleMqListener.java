package com.lanking.uxb.service.fallible.api.impl.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathFallibleRegistryConstants;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.uxb.service.fallible.api.TaskFallibleOcrSearchRecordService;
import com.lanking.uxb.service.fallible.api.TeacherFallibleQuestionService;
import com.lanking.uxb.service.search.api.IndexService;

/**
 * 错题相关的mq消息接收
 * 
 * @since 2.2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月1日
 */
@Component
@Exchange(name = MqYoomathFallibleRegistryConstants.EX_YM_FALLIBLE_TASK)
public class FallibleMqListener {

	private Logger logger = LoggerFactory.getLogger(FallibleMqListener.class);

	@Autowired
	private TaskFallibleOcrSearchRecordService fallibleOcrSearchRecordService;
	@Autowired
	private TeacherFallibleQuestionService teacherFallibleQuestionService;
	@Autowired
	private IndexService indexService;

	/**
	 * 记录ocr-search-add fallible的记录
	 * 
	 * @since 2.2.0
	 * @param mq
	 *            消息体
	 */
	@Listener(queue = MqYoomathFallibleRegistryConstants.QUEUE_YM_FALLIBLE_TASK_OCRSEARCH, routingKey = MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_OCRSEARCH)
	public void ocrSearchRecord(JSONObject mq) {
		try {
			String action = mq.getString("action");
			long fileId = mq.getLongValue("fileId");
			switch (action) {
			case "create":
				Date createAt = mq.getDate("createAt");
				List<Long> questions = null;
				if (mq.containsKey("questions")) {
					JSONArray array = mq.getJSONArray("questions");
					questions = new ArrayList<Long>(array.size());
					for (Object qId : array) {
						questions.add((Long) qId);
					}
				} else {
					questions = new ArrayList<Long>(0);
				}
				fallibleOcrSearchRecordService.create(fileId, questions, createAt);
				break;
			case "choose":
				long chooseQuestion = mq.getLongValue("chooseQuestion");
				Date chooseAt = mq.getDate("chooseAt");
				fallibleOcrSearchRecordService.choose(fileId, chooseQuestion, chooseAt);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("ocr search add fallible record error:", e);
		}
	}

	/**
	 * 教师错题记录更新
	 * 
	 * @since 2.2.0
	 * @param mq
	 *            消息体
	 */
	@Listener(queue = MqYoomathFallibleRegistryConstants.QUEUE_YM_FALLIBLE_TASK_TEACHER, routingKey = MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_TEACHER)
	public void teacherFallible(JSONObject mq) {
		try {
			long fallId = teacherFallibleQuestionService.update(mq.getLongValue("teacherId"),
					mq.getLongValue("questionId"), mq.getObject("result", HomeworkAnswerResult.class),
					mq.getInteger("subjectCode"), mq.getObject("type", Type.class), mq.getInteger("typeCode"),
					mq.getDouble("difficulty"));
			// TODO 待优化(目前是一题一题处理的)
			indexService.syncUpdate(IndexType.TEACHER_FALLIBLE_QUESTION, fallId);
		} catch (Exception e) {
			logger.error("teacher fallible error:", e);
		}
	}

	/**
	 * 学生错题记录更新
	 * 
	 * @since 2.2.0
	 * @param mq
	 *            消息体
	 */
	@Listener(queue = MqYoomathFallibleRegistryConstants.QUEUE_YM_FALLIBLE_TASK_STUDENT, routingKey = MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_STUDENT)
	public void studentFallible(JSONObject mq) {

	}
}
