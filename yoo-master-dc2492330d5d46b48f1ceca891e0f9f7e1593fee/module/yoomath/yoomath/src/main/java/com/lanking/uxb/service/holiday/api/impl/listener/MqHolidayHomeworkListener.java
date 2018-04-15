package com.lanking.uxb.service.holiday.api.impl.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathCounterDetailRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHolidayHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;

@Component
@Exchange(name = MqYoomathHolidayHomeworkRegistryConstants.EX_YM_HOLIDAYHOMEWORK)
public class MqHolidayHomeworkListener {

	private Logger logger = LoggerFactory.getLogger(MqHolidayHomeworkListener.class);

	@Autowired
	private MqSender mqSender;
	@Autowired
	private HolidayHomeworkItemQuestionService hhiqService;
	@Autowired
	private HolidayStuHomeworkItemQuestionService hshiqService;

	/**
	 * 分发作业的时候处理
	 * 
	 * @param json
	 *            key:{teacherId,holidayHomeworkItemIds}
	 */
	@SuppressWarnings("unchecked")
	@Listener(queue = MqYoomathHolidayHomeworkRegistryConstants.QUEUE_YM_HOLIDAYHOMEWORK_PUBLISH, routingKey = MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_PUBLISH)
	public void publish(JSONObject json) {
		try {
			long teacherId = json.getLongValue("teacherId");
			List<Long> holidayHomeworkItemIds = json.getObject("holidayHomeworkItemIds", List.class);
			for (Long holidayHomeworkItemId : holidayHomeworkItemIds) {
				List<HolidayHomeworkItemQuestion> hhiqs = hhiqService.getHomeworkQuestion(holidayHomeworkItemId);
				for (HolidayHomeworkItemQuestion hhiq : hhiqs) {
					JSONObject jo = new JSONObject();
					jo.put("bizId", hhiq.getQuestionId());
					jo.put("otherBizId", teacherId);
					jo.put("count", Count.COUNTER_1);
					jo.put("delta", 1);
					mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
							MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
							MQ.builder().data(jo).build());
				}
			}
		} catch (Exception e) {
			logger.error("publish error:", e);
		}
	}

	/**
	 * 提交假期作业的时候处理
	 * 
	 * @param json
	 *            key:{studentId,holidayStuHomeworkItemId}
	 */
	@Listener(queue = MqYoomathHolidayHomeworkRegistryConstants.QUEUE_YM_HOLIDAYHOMEWORK_COMMIT, routingKey = MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_COMMIT)
	public void commit(JSONObject json) {
		try {
			long studentId = json.getLongValue("studentId");
			long holidayStuHomeworkItemId = json.getLongValue("holidayStuHomeworkItemId");
			List<HolidayStuHomeworkItemQuestion> hshiqs = hshiqService.queryQuestionList(holidayStuHomeworkItemId);
			for (HolidayStuHomeworkItemQuestion hshiq : hshiqs) {
				JSONObject jo = new JSONObject();
				jo.put("bizId", hshiq.getQuestionId());
				jo.put("otherBizId", studentId);
				jo.put("count", Count.COUNTER_1);
				jo.put("delta", 1);
				mqSender.send(MqYoomathCounterDetailRegistryConstants.EX_YM_COUNTERDETAIL,
						MqYoomathCounterDetailRegistryConstants.RK_YM_COUNTERDETAIL_QUESTIONUSER,
						MQ.builder().data(jo).build());
			}
		} catch (Exception e) {
			logger.error("commit error:", e);
		}
	}
}