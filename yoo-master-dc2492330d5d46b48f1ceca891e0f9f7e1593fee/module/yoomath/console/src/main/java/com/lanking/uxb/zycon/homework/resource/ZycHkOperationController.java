package com.lanking.uxb.zycon.homework.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkService;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.uxb.zycon.homework.api.ZycHkOperationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台批改作业接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@RestController
@RequestMapping(value = "zyc/hkOperation")
public class ZycHkOperationController {
	@Autowired
	private ZycHkOperationService zycHkOperationService;
	@Autowired
	private ZycQuestionService zycQuestionService;
	@Autowired
	private ZycAccountService zycAccountService;
	@Autowired
	private ZycStudentHomeworkService zycStudentHomeworkService;
	@Autowired
	private ZycHolidayStuHomeworkItemAnswerService stuHomeworkItemAnswerService;

	@RequestMapping(value = "listAnswers")
	public Value listAnswers(long stuHkId, int idx) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		Long questionId = zycHkOperationService.queryQuestionId(stuHkId, idx);
		if (questionId != null && questionId > 0) {
			data.put("standardAnswer", zycHkOperationService.findStandardAnswer(stuHkId, idx, questionId));
			data.put("studentAnswer", zycHkOperationService.findStudentAnswer(stuHkId, idx, questionId, false));
			data.put("studentCorrectAnswer", zycHkOperationService.findStudentAnswer(stuHkId, idx, questionId, true));
		} else {
			data.put("standardAnswer", StringUtils.EMPTY);
			data.put("studentAnswer", StringUtils.EMPTY);
			data.put("studentCorrectAnswer", StringUtils.EMPTY);
		}
		return new Value(data);
	}

	/**
	 * 下载学生答案及标准答案,txt格式 <br/>
	 *
	 * 文件名为: 学生作业id_题目id.txt
	 *
	 * 文档格式如下
	 *
	 * 正确答案: ... <br />
	 * 学生答案: ... <br />
	 * 题目编号: ... <br />
	 * 学生帐户名: ... <br />
	 *
	 * 若有多个答案各答案之间使用","进行分隔
	 *
	 * @param stuHkId
	 *            学生作业id
	 * @param qId
	 *            题目id
	 * @param studentId
	 *            学生id
	 * @param code
	 *            题目code
	 */
	@RequestMapping(value = "downloadAnswers", method = { RequestMethod.GET, RequestMethod.POST })
	public void downloadAnswers(long stuHkId, long qId, int idx,
			@RequestParam(value = "studentId", required = false) Long studentId,
			@RequestParam(value = "code", required = false) String code, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String fileName = stuHkId + "_" + qId + ".txt";
		response.reset();
		response.setContentType("bin");
		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
		StringBuilder content = new StringBuilder();
		content.append("标准答案:");
		for (String standarAnswer : zycHkOperationService.findStandardAnswer(stuHkId, idx, qId)) {
			content.append(standarAnswer).append(",");
		}
		if (content.lastIndexOf(",") != -1) {
			content.deleteCharAt(content.lastIndexOf(","));
		}
		content.append("\n");
		content.append("学生答案:");
		for (String studentAnswer : zycHkOperationService.findStudentAnswer(stuHkId, idx, qId, false)) {
			content.append(studentAnswer).append(",");
		}
		if (content.lastIndexOf(",") != -1) {
			content.deleteCharAt(content.lastIndexOf(","));
		}
		
		content.append("\n");
		content.append("学生订正答案:");
		for (String studentCorrectAnswer : zycHkOperationService.findStudentAnswer(stuHkId, idx, qId, true)) {
			content.append(studentCorrectAnswer).append(",");
		}
		if (content.lastIndexOf(",") != -1) {
			content.deleteCharAt(content.lastIndexOf(","));
		}

		if (code == null) {
			code = zycQuestionService.getQuestionCode(qId);
		}
		content.append("\n").append("题目编号:").append(code).append("\n");
		if (studentId == null) {
			studentId = zycStudentHomeworkService.get(stuHkId).getStudentId();
		}
		content.append("学生账户名:").append(zycAccountService.getAccountByUserId(studentId).getName());

		response.getOutputStream().write(content.toString().getBytes());
	}

	/**
	 * 下载学生假期作业答案及标准答案
	 * 
	 * @param stuItemQuestionId
	 *            学生假期作业专项题目id
	 * @param stuHomeworkId
	 *            学生假期作业id
	 * @param qId
	 *            题目id
	 * @param code
	 *            题目的编号
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param response
	 *            {@link HttpServletResponse}
	 * @param studentId
	 *            学生id
	 */
	@RequestMapping(value = "downloadHolidayAnswers", method = { RequestMethod.GET, RequestMethod.POST })
	public void downloadHolidayAnswers(long stuItemQuestionId, long stuHomeworkId, long qId,
			@RequestParam(value = "code", required = false) String code, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value = "studentId") long studentId) throws IOException {
		String fileName = stuHomeworkId + "_" + stuItemQuestionId + ".txt";
		response.reset();
		response.setContentType("bin");

		response.addHeader("Content-Disposition", "attachment; filename=\""
				+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
		StringBuilder content = new StringBuilder();
		content.append("标准答案:");

		for (String standarAnswer : zycHkOperationService.findStandardAnswer(stuHomeworkId, 0, qId)) {
			content.append(standarAnswer).append(",");
		}
		if (content.lastIndexOf(",") != -1) {
			content.deleteCharAt(content.lastIndexOf(","));
		}
		content.append("\n");
		content.append("学生答案:");
		List<HolidayStuHomeworkItemAnswer> stuHomeworkItemAnswers = stuHomeworkItemAnswerService
				.query(stuItemQuestionId);
		for (HolidayStuHomeworkItemAnswer answer : stuHomeworkItemAnswers) {
			content.append(answer.getContent()).append(",");
		}

		if (content.lastIndexOf(",") != -1) {
			content.deleteCharAt(content.lastIndexOf(","));
		}

		if (code == null) {
			code = zycQuestionService.getQuestionCode(qId);
		}
		content.append("\n").append("题目编号:").append(code).append("\n");
		content.append("学生账户名:").append(zycAccountService.getAccountByUserId(studentId).getName());

		response.getOutputStream().write(content.toString().getBytes());
	}

}
