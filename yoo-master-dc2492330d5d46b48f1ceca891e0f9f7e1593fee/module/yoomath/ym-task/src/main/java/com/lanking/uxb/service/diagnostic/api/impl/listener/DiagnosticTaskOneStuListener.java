package com.lanking.uxb.service.diagnostic.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassLatestHomeworkService;

@Component
@Exchange(name = MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK)
public class DiagnosticTaskOneStuListener {

	private Logger logger = LoggerFactory.getLogger(DiagnosticTaskOneStuListener.class);
	@Autowired
	private StaticStuDiagnosticClassLatestHomeworkService diagStuClassLastestStatService;

	void diagnoOneStudent(JSONObject mq) {
		try {
			logger.info("diagno one student mq:{}", mq.toString());
			long studentId = mq.getLongValue("studentId");
			long classId = mq.getLongValue("classId");
			diagStuClassLastestStatService.deleteLastestByStuClass(classId, studentId);
			diagStuClassLastestStatService.statOneStuClassLastestData(studentId, classId);
		} catch (Exception e) {
			logger.error("task diagno one student error:", e);
		}
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_0, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_0)
	public void diagnoOneStudent0(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_1, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_1)
	public void diagnoOneStudent1(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_2, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_2)
	public void diagnoOneStudent2(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_3, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_3)
	public void diagnoOneStudent3(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_4, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_4)
	public void diagnoOneStudent4(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_5, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_5)
	public void diagnoOneStudent5(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_6, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_6)
	public void diagnoOneStudent6(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_7, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_7)
	public void diagnoOneStudent7(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_8, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_8)
	public void diagnoOneStudent8(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_9, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_9)
	public void diagnoOneStudent9(JSONObject mq) {
		diagnoOneStudent(mq);
	}

	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_10, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_10)
	public void diagnoOneStudent10(JSONObject mq) {
		diagnoOneStudent(mq);
	}

}
