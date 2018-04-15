package com.lanking.uxb.zycon.holiday.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.zycon.holiday.convert.ZycHolidayStuHomeworkItemQuestionConvert;
import com.lanking.uxb.zycon.holiday.value.VZycHolidayStuHomeworkItemQuestion;

/**
 * 假期作业批改确认接口
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@RestController
@RequestMapping(value = "zyc/holiday/cf")
public class ZycHolidayConfirmController {
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionService stuHomeworkItemQuestionService;
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionConvert stuHomeworkItemQuestionConvert;

	/**
	 * 查找待确认的假期作业
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @param questionCode
	 *            题目编号
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findConfirm", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findConfirm(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size,
			@RequestParam(value = "questionCode", required = false) String questionCode) {
		Pageable pageable = P.index(page, size);
		
		Page<HolidayStuHomeworkItemQuestion> resultPage = null;
		if (StringUtils.isBlank(questionCode)) {
			resultPage = stuHomeworkItemQuestionService.findConfirmQuestions(pageable);
		} else {
			resultPage = stuHomeworkItemQuestionService.findConfirmQuestionsByCode(pageable, questionCode);
		}
		
		List<HolidayStuHomeworkItemQuestion> questions = resultPage.getItems();
		VPage<VZycHolidayStuHomeworkItemQuestion> retPage = new VPage<VZycHolidayStuHomeworkItemQuestion>();
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setTotal(resultPage.getTotalCount());
		retPage.setTotalPage(resultPage.getPageCount());
		retPage.setItems(stuHomeworkItemQuestionConvert.to(questions));
		return new Value(retPage);
	}

	/**
	 * 确认题目
	 *
	 * @param ids
	 *            列表
	 * @return {@link Value}
	 */
	@RequestMapping(value = "confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public Value confirm(@RequestParam(value = "ids") List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return new Value(new IllegalArgException());
		}

		stuHomeworkItemQuestionService.confirm(ids);

		return new Value();
	}
}
