package com.lanking.uxb.zycon.base.resource;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.base.api.ZycClassStatisticsReportService;
import com.lanking.uxb.zycon.base.api.ZycStudentStatisticsReportService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkClazzService;
import com.lanking.uxb.zycon.qs.api.ZycTeacherService;

/**
 * 可能存在的悠数学只统计某个老师的班级，或者某个班级的学生的统计的情况 所用接口。
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月8日 下午2:46:53
 */
@RestController
@RequestMapping("zyc/stat")
public class ZycStatisticsController {

	@Autowired
	private ZycClassStatisticsReportService classStatService;
	@Autowired
	private ZycTeacherService teacherService;
	@Autowired
	private ZycStudentStatisticsReportService studentStatService;
	@Autowired
	private ZycHomeworkClazzService homeworkClazzService;

	/**
	 * 班级统计
	 * 
	 * @param year
	 *            年
	 * @param months
	 *            月 数组 不传则统计全年
	 * @param teacherId
	 *            教师ID
	 * @return
	 */
	@RequestMapping(value = "classStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value classStat(@RequestParam(value = "year", required = true) int year,
			@RequestParam(value = "months", required = false) List<Integer> months,
			@RequestParam(value = "teacherId", required = true) long teacherId) {
		if (CollectionUtils.isEmpty(months)) {
			months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		}
		for (Integer month : months) {
			classStatService.create(year, month, teacherService.get(teacherId));
		}
		return new Value();
	}

	@RequestMapping(value = "statAllClass", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statAllClass() {
		// 当前毫秒数
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		CursorPage<Long, Teacher> page = teacherService.getAll(CP.cursor(Long.MAX_VALUE, 200));
		while (page.isNotEmpty()) {
			for (Teacher teacher : page) {
				classStatService.create(year, month, teacher);
			}
			page = teacherService.getAll(CP.cursor(page.getLast().getId(), 200));
		}
		return new Value();
	}

	/**
	 * 学生统计
	 * 
	 * @param year
	 *            年
	 * @param months
	 *            月 数组形式 不传则统计全年
	 * @param clazzId
	 *            班级ID
	 * @return
	 */
	@RequestMapping(value = "studentStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentStat(@RequestParam(value = "year", required = true) int year,
			@RequestParam(value = "months", required = false) List<Integer> months,
			@RequestParam(value = "clazzId", required = true) Long clazzId) {
		if (CollectionUtils.isEmpty(months)) {
			months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		}
		for (Integer month : months) {
			studentStatService.create(year, month, clazzId);
		}

		return new Value();
	}

	@RequestMapping(value = "statAllStudent", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statAllStudent() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		CursorPage<Long, HomeworkClazz> page = homeworkClazzService.getAll(CP.cursor(Long.MAX_VALUE, 200));
		while (page.isNotEmpty()) {
			for (HomeworkClazz clazz : page) {
				studentStatService.create(year, month, clazz.getId());
			}
			page = homeworkClazzService.getAll(CP.cursor(page.getLast().getId(), 200));
		}
		return new Value();
	}

	public Value doStudentTask() {
		return new Value();
	}

}
