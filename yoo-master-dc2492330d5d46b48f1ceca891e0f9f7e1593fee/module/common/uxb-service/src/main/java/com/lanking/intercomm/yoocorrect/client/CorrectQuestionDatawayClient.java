package com.lanking.intercomm.yoocorrect.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.dto.CorrectErrorQuestionData;
import com.lanking.intercomm.yoocorrect.dto.CorrectQuestionData;

/**
 * 小悠快批-题目批改服务客户端.
 * 
 * @author wanlong.che
 *
 */
@FeignClient("${correct-server.name}")
public interface CorrectQuestionDatawayClient {

	@RequestMapping(value = "/correctQuestion/receiveCorrectQuestions", method = { RequestMethod.POST })
	Value receiveCorrectQuestions(List<CorrectQuestionData> datas);

	@RequestMapping(value = "/correctQuestion/correctQuestionsCount", method = { RequestMethod.POST })
	Value correctQuestionsCount();

	/**
	 * 开始批改.
	 * 
	 * @param userId
	 *            快批用户ID
	 */
	@RequestMapping(value = "/correctQuestion/startCorrect", method = { RequestMethod.POST })
	Value startCorrect(@RequestParam("userId") Long userId);

	/**
	 * 获取待批改的习题.
	 * 
	 * @param userId
	 *            快批用户ID
	 * @return
	 */
	@RequestMapping(value = "/correctQuestion/getCorrectQuestion", method = { RequestMethod.POST })
	Value getCorrectQuestion(@RequestParam("userId") Long userId);

	/**
	 * 批改完成当前习题.
	 * 
	 * @param userId
	 *            快批用户ID
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @param costTime
	 *            花费时间
	 * @param rightRate
	 *            正确率
	 * @return
	 */
	@RequestMapping(value = "/correctQuestion/correctComplete", method = { RequestMethod.POST })
	Value correctComplete(@RequestParam("userId") Long userId,
			@RequestParam("studentHomeworkQuestionId") Long studentHomeworkQuestionId,
			@RequestParam("costTime") Integer costTime, @RequestParam("rightRate") Integer rightRate,
			@RequestParam("statBills") Boolean statBills);

	/**
	 * 停止接收题目.
	 * 
	 * @param userId
	 *            快批用户ID
	 * @return
	 */
	@RequestMapping(value = "/correctQuestion/stopCorrect", method = { RequestMethod.POST })
	Value stopCorrect(@RequestParam("userId") Long userId);

	/**
	 * 模拟完成.
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/correctQuestion/mockComplete", method = { RequestMethod.POST })
	Value mockComplete(@RequestParam("userId") Long userId);

	/**
	 * 反馈题目有误.
	 * 
	 * @param userId
	 *            批改员
	 * @param studentHomeworkQuestionId
	 *            学生作业ID
	 * @return
	 */
	@RequestMapping(value = "/correctQuestion/feedbackQuestion", method = { RequestMethod.POST })
	Value feedbackQuestion(@RequestParam("userId") Long userId,
			@RequestParam("studentHomeworkQuestionId") Long studentHomeworkQuestionId);

	/**
	 * 错改题.
	 * 
	 * @param adminUserId
	 *            管理员用户Id
	 * @param errorCorrectStudentHomeworkIds
	 *            错误习题
	 * @return
	 */
	@RequestMapping(value = "/correctQuestion/errorCorrect", method = { RequestMethod.POST })
	Value errorCorrect(List<CorrectErrorQuestionData> correctErrorQuestionDatas);
}
