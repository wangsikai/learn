package com.lanking.uxb.zycon.holiday.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.support.console.common.HomeworkCorrectLogType;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayArchiveService;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayHomeworkService;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.zycon.holiday.cache.ZycHolidayStuHomeworkQuestionCacheService;
import com.lanking.uxb.zycon.holiday.convert.ZycHolidayHomeworkConvert;
import com.lanking.uxb.zycon.holiday.convert.ZycHolidayStuHomeworkItemQuestionConvert;
import com.lanking.uxb.zycon.holiday.value.VZycHolidayHomework;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkCorrectLogService;
import com.lanking.uxb.zycon.homework.form.HomeworkCorrectForm;
import com.lanking.uxb.zycon.homework.form.HomeworkCorrectLogForm;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryForm;

/**
 * 假期作业批改接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@RestController
@RequestMapping(value = "/zyc/holiday")
public class ZycHolidayCorrectController {
	@Autowired
	private ZycHomeworkCorrectLogService homeworkCorrectLogService;
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionService holidayStuHomeworkItemQuestionService;
	@Autowired
	private ZycHolidayStuHomeworkQuestionCacheService stuHomeworkQuestionCacheService;
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionConvert stuHomeworkItemQuestionConvert;
	@Autowired
	private ZycHolidayHomeworkService holidayHomeworkService;
	@Autowired
	private ZycHolidayHomeworkConvert holidayHomeworkConvert;
	@Autowired
	private ZycHolidayArchiveService holidayArchiveService;

	/**
	 * 查询现在作业列表
	 *
	 * @return Value
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(HomeworkQueryForm form) {
		Page<HolidayHomework> pageValue = holidayHomeworkService.page(form);
		VPage<VZycHolidayHomework> vPage = new VPage<VZycHolidayHomework>();
		List<VZycHolidayHomework> vs = holidayHomeworkConvert.to(pageValue.getItems());
		vPage.setItems(vs);
		vPage.setCurrentPage(form.getPage());
		vPage.setTotalPage(pageValue.getPageCount());
		vPage.setPageSize(form.getSize());
		vPage.setTotal(pageValue.getTotalCount());
		return new Value(vPage);
	}

	/**
	 * 导出操作
	 *
	 * form 中导出传入的size固定由前端传入进来
	 *
	 * @param response
	 *            {@link HttpServletResponse}
	 * @param form
	 *            {@link HomeworkQueryForm}
	 */
	@RequestMapping(value = "export", method = { RequestMethod.GET })
	public void export(HttpServletResponse response, HomeworkQueryForm form) {
		Page<HolidayHomework> pageValue = holidayHomeworkService.page(form);
		VPage<VZycHolidayHomework> vPage = new VPage<VZycHolidayHomework>();
		List<VZycHolidayHomework> vs = holidayHomeworkConvert.to(pageValue.getItems());
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("省");
		row.createCell(1).setCellValue("市");
		row.createCell(2).setCellValue("学校");
		row.createCell(3).setCellValue("教师");
		row.createCell(4).setCellValue("班级");
		row.createCell(5).setCellValue("作业名称");
		row.createCell(6).setCellValue("答题时间");
		row.createCell(7).setCellValue("整体完成情况");
		row.createCell(8).setCellValue("状态");

		int i = 1;
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		for (VZycHolidayHomework v : vs) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(v.getClazz().getProvince());
			row.createCell(1).setCellValue(v.getClazz().getCity());
			row.createCell(2).setCellValue(v.getClazz().getSchoolName());
			row.createCell(3).setCellValue(v.getClazz().getTeacherName());
			row.createCell(4).setCellValue(v.getClazz().getName());
			row.createCell(5).setCellValue(v.getName());
			row.createCell(6).setCellValue(format.format(v.getStartTime()) + "~" + format.format(v.getDeadline()));
			row.createCell(7).setCellValue(
					v.getCompletionRate() != null ? v.getCompletionRate().doubleValue() + "%" : "--");

			String statusName = "";
			switch (v.getHomeworkStatus()) {
			case INIT:
				statusName = "待分发";
				break;
			case PUBLISH:
				statusName = "作业中";
				break;
			case NOT_ISSUE:
				statusName = "已截止";
				break;
			case ISSUED:
				statusName = "已截止";
				break;
			}
			row.createCell(8).setCellValue(statusName);
			i++;
		}

		try {
			response.reset();
			response.setContentType("bin");
			response.addHeader("Content-Disposition",
					"attachment; filename=\"" + new String("假期作业列表.xls".getBytes("UTF-8"), "ISO-8859-1") + "\"");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
		}

	}

	/**
	 * 查询待批改的题目列表
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "queryNotCorrectQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryNotCorrectQuestions(@RequestParam(value = "size", defaultValue = "5") int size) {
		List<HolidayStuHomeworkItemQuestion> itemQuestions = holidayStuHomeworkItemQuestionService
				.findCorrectQuestions(size);

		for (HolidayStuHomeworkItemQuestion q : itemQuestions) {
			stuHomeworkQuestionCacheService.push(q.getQuestionId());
		}

		return new Value(stuHomeworkItemQuestionConvert.to(itemQuestions));
	}

	/**
	 * 新的批改接口，假期作业针对多空填空题也可以多空进行批改。
	 *
	 * @since 3.9.0
	 * @param formStr
	 *            {@link HomeworkCorrectForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "correct", method = { RequestMethod.GET, RequestMethod.POST })
	public Value correct(String formStr) {

		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		HomeworkCorrectForm form = JSON.parseObject(formStr, HomeworkCorrectForm.class);
		List<HomeworkCorrectLogForm> logForms = new ArrayList<HomeworkCorrectLogForm>(form.getSqIds().size());

		int i = 0;
		for (Long qId : form.getSqIds()) {
			HomeworkAnswerResult questionResult = form.getResults().get(i);
			holidayStuHomeworkItemQuestionService.correct(qId, form.getAnswerResults().get(i), Security.getUserId());
			HomeworkCorrectLogForm logForm = new HomeworkCorrectLogForm(qId, questionResult);
			i++;
			holidayArchiveService.asyncAnswerArchive(qId, questionResult);

			logForms.add(logForm);
		}

		homeworkCorrectLogService.save(Security.getUserId(), logForms, HomeworkCorrectLogType.HOLIDAY_HOME);

		return new Value();
	}
}
