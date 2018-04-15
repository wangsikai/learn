package com.lanking.uxb.service.diagnostic.api.impl.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkService;
import com.lanking.uxb.service.diagnostic.api.student.StaticDiagnosticClassTopnKpService;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassKpService;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.api.teacher.StaticDiagnosticService;

/**
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年6月3日
 */
@Component
@Exchange(name = MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK)
public class DiagnosticTaskListener {

	private Logger logger = LoggerFactory.getLogger(DiagnosticTaskListener.class);
	@Autowired
	private StaticDiagnosticService staticDiagnosticService;
	@Autowired
	private StaticStuDiagnosticClassLatestHomeworkService diagStuClassLastestStatService;
	@Autowired
	private StaticHomeworkClassService homeworkClassService;
	@Autowired
	private StaticHomeworkService staticHomeworkService;
	@Autowired
	private StaticStuDiagnosticClassKpService staticStuDiagnosticClassKpService;
	@Autowired
	private StaticStuDiagnosticClassLatestHomeworkService staticStuDiagnosticClassLatestHomeworkService;
	@Autowired
	private StaticDiagnosticClassTopnKpService topKpService;

	/**
	 * 下发作业后在完成作业基础统计后发送消息触发此统计
	 * 
	 * @since 2.1.0
	 * @since 教师端 v1.3.0 2017-7-5 教学诊断，学生退出加入班级对班级知识点统计有影响.
	 * @since 小优快批，没有下发流程，该调用将在作业全部批改完成后进行
	 * 
	 * @param mq
	 *            mq消息体<br>
	 * 
	 *            <pre>
	 * {
	 * 	classId:班级ID,
	 *  homeworkId:作业ID
	 * }
	 *            </pre>
	 */
	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_CLASS_HOMEWORK, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_CLASS_HOMEWORK)
	public void diagnoClass(JSONObject mq) {
		try {
			logger.info("diagno class mq *new:{}", mq.toString());
			long classId = mq.getLongValue("classId"); // 涉及的班级
			Long homeworkId = mq.getLong("homeworkId"); // 当为下发作业时，此处有值

			if (homeworkId != null) {
				// 下发作业
				HomeworkClazz clazz = homeworkClassService.get(classId);
				Homework homework = staticHomeworkService.get(homeworkId);
				staticDiagnosticService.staticDiagnosticByHomework(clazz, homework);
			}
		} catch (Exception e) {
			logger.error("task diagno class error *new:", e);
		}
	}

	/**
	 * 下发作业后在完成作业基础统计后发送消息触发此统计<br>
	 * 最近30次
	 * 
	 * @since 2.1.0
	 * @param mq
	 *            mq消息体<br>
	 * 
	 *            <pre>
	 * {
	 * 	classId:班级ID,
	 *  homeworkId:作业ID
	 * }
	 *            </pre>
	 */
	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK)
	public void diagnoStudent(JSONObject mq) {
		try {
			logger.info("diagno student mq:{}", mq.toString());
			long homeworkId = mq.getLongValue("homeworkId");
			long classId = mq.getLongValue("classId");
			diagStuClassLastestStatService.statStuClassLastestData(classId, homeworkId);
		} catch (Exception e) {
			logger.error("task diagno student error:", e);
		}
	}

	/**
	 * 下发作业后在完成作业基础统计后发送消息触发此统计<br>
	 * 所有的，包含历史
	 * 
	 * @param mq
	 *            mq消息体<br>
	 * 
	 *            <pre>
	 * {
	 * 	classId:班级ID,
	 *  homeworkId:作业ID
	 * }
	 *            </pre>
	 */
	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_HOMEWORK_ALL, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_ALL)
	public void diagnoStudent2(JSONObject mq) {
		try {
			logger.info("diagno student mq:{}", mq.toString());
			long homeworkId = mq.getLongValue("homeworkId");
			long classId = mq.getLongValue("classId");
			diagStuClassLastestStatService.stuHkStatistic(homeworkId);
			staticStuDiagnosticClassKpService.statStuClassKp(classId, homeworkId);
		} catch (Exception e) {
			logger.error("task diagno student error:", e);
		}
	}

	/**
	 * 学生离开班级
	 * 
	 * @param mq
	 */
	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_LEAVE, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_LEAVE)
	public void diagnoStudentLeave(JSONObject mq) {
		try {
			logger.info("diagno student mq:{}", mq.toString());
			long studentId = mq.getLongValue("studentId");
			long classId = mq.getLongValue("classId");
			staticStuDiagnosticClassKpService.statWhenLeave(studentId, classId);
			staticStuDiagnosticClassLatestHomeworkService.statWhenLeave(studentId, classId);

			// 班级数据统计
			HomeworkClazz clazz = homeworkClassService.get(classId);
			staticDiagnosticService.staticDiagnosticByStudent(clazz, studentId, false);
			// 重新计算这个班级的topn数据
			topKpService.staticTopnStu(classId);
		} catch (Exception e) {
			logger.error("task diagnoStudentLeave error:", e);
		}
	}

	/**
	 * 学生加入班级
	 * 
	 * @param mq
	 */
	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_STUDENT_JOIN, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN)
	public void diagnoStudentJoin(JSONObject mq) {
		try {
			logger.info("diagno student mq:{}", mq.toString());
			long studentId = mq.getLongValue("studentId");
			long classId = mq.getLongValue("classId");
			staticStuDiagnosticClassKpService.statWhenJoin(studentId, classId);
			staticStuDiagnosticClassLatestHomeworkService.statWhenJoin(studentId, classId);

			// 班级数据统计，判断该学生在该班级是否有原始的作业数据
			boolean hasStudentHomework = staticHomeworkService.hasStudentHomework(classId, studentId, null);
			HomeworkClazz clazz = homeworkClassService.get(classId);
			if (hasStudentHomework) {
				// 若存在原始作业，需要对该班级及学生所在其他班级重新进行统计
				staticDiagnosticService.staticDiagnosticByStudent(clazz, studentId, true);
			} else {
				// 若不存在学生作业，需要将学生在其他班级的数据带过来
				staticDiagnosticService.staticDiagnosticByStudentOfEmpty(clazz, studentId);
			}

			// 重新计算这个班级的topn数据
			topKpService.staticTopnStu(classId);
		} catch (Exception e) {
			logger.error("task diagnoStudentLeave error:", e);
		}
	}

	/**
	 * 处理班级相关增减量统计数据.
	 * 
	 * @param mq
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listener(queue = MqYoomathDiagnoRegistryConstants.QUEUE_YM_DIAGNO_TASK_CLASS_INCR, routingKey = MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_CLASS_INCR)
	public void diagnoClassINCData(JSONObject mq) {
		try {
			logger.info("diagno classinc data mq:{}", mq.toString());
			long classId = mq.getLongValue("classId");
			boolean incr = mq.getBooleanValue("incr");
			boolean allStatic = mq.getBooleanValue("allStatic");
			JSONArray homeworkIdArray = mq.getJSONArray("homeworkIds");
			JSONArray array = mq.getJSONArray("hkQuestion");
			if (array == null || array.size() == 0) {
				return;
			}
			List<Map> hkQuestion = new ArrayList<Map>(array.size());
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Map map = new HashMap();
				map.put("question_id", obj.getLongValue("question_id"));
				map.put("right_count", obj.getIntValue("right_count"));
				map.put("wrong_count", obj.getIntValue("wrong_count"));
				map.put("difficulty", obj.getDoubleValue("difficulty"));
				map.put("homework_id", obj.getLongValue("homework_id"));
				hkQuestion.add(map);
			}
			List<Long> homeworkIds = new ArrayList<Long>(homeworkIdArray.size());
			for (int i = 0; i < homeworkIdArray.size(); i++) {
				homeworkIds.add(homeworkIdArray.getLong(i));
			}

			HomeworkClazz clazz = homeworkClassService.get(classId);
			staticDiagnosticService.staticDiagnosticClassIncrDatas(clazz, homeworkIds, hkQuestion, incr, allStatic);
		} catch (Exception e) {
			logger.error("task classinc data error:", e);
		}
	}
}
