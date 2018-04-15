package com.lanking.uxb.zycon.homework.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.homework.value.VZycQuestion;

/**
 * 作业批改待确认
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@RestController
@RequestMapping(value = "zyc/hk/cf")
public class ZycHomeworkConfirmController {
	@Autowired
	@Qualifier(value = "hzycQuestionConvert")
	private ZycQuestionConvert questionConvert;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycStudentHomeworkQuestionService studentHomeworkQuestionService;
	
	
	/**
	 * 查询普通作业待确认的题目
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页多少条数据
	 * @param questionCode
	 *            题目编号
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findHkQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findHkQuestion(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size,
			@RequestParam(value = "questionCode", required = false) String questionCode) {
		Pageable pageable = P.index(page, size);

		Map<String, Object> queryMap = new HashMap<>();
		if (StringUtils.isBlank(questionCode)) {
			queryMap = questionService.zycFindConfirmQuestions(pageable);
		} else {
			queryMap = questionService.zycFindConfirmQuestionsByCode(pageable, questionCode);
		}

		VPage<VZycQuestion> retPage = new VPage<VZycQuestion>();

		List<VZycQuestion> vs = questionConvert.to((List<Question>) queryMap.get("items"));
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setItems(vs);
		retPage.setTotal((Long) queryMap.get("totalCount"));
		retPage.setTotalPage((Integer) queryMap.get("totalPage"));

		return new Value(retPage);
	}

	/**
	 * 确认批改结果
	 *
	 * @param stuHkQuestionIds
	 *            确认已批改结果
	 * @return {@link Value}
	 */
	@RequestMapping(value = "confirmHk", method = { RequestMethod.GET, RequestMethod.POST })
	public Value confirmHk(@RequestParam(value = "stuHkQuestionIds") List<Long> stuHkQuestionIds) {
		if (CollectionUtils.isEmpty(stuHkQuestionIds)) {
			return new Value(new IllegalArgException());
		}

		studentHomeworkQuestionService.confirm(stuHkQuestionIds);
		return new Value();
	}
}
