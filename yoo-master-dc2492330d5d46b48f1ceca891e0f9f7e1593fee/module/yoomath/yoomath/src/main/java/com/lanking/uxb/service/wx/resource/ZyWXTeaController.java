package com.lanking.uxb.service.wx.resource;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.domain.yoomath.stat.ClassStatisticsReport;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.PageNotFoundException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.WXBindCheck;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.report.api.ClassStatisticsReportService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 悠数学微信服务号控制器.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
@RestController
@RequestMapping("zy/wx/t")
public class ZyWXTeaController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private HomeworkConvert hkConvert;
	@Autowired
	private StudentHomeworkService shService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ZyHomeworkStatisticService hkStatisticService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private ClassStatisticsReportService classStatisticsReportService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private ZyWXMessageService zyWXMessageService;

	/**
	 * 作业管理页.
	 * 
	 * @return
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "homeworkManage", method = { RequestMethod.GET })
	public Value homeworkManageIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/page/teacher/homework-manage.html?" + System.currentTimeMillis());
		} catch (IOException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 学情报告页.
	 * 
	 * @return
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "report", method = { RequestMethod.GET })
	public Value tReportIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/page/teacher/report.html?" + System.currentTimeMillis());
		} catch (IOException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 待处理作业数据.
	 * 
	 * @param todoSize
	 *            待处理作业条数
	 * @return
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "homeworkManage/queryTodos", method = { RequestMethod.POST })
	public Value homeworkManage(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "todoSize", defaultValue = "5") int todoSize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(3);
		long userId = Security.getUserId();
		// Long userId = 257152333627006976L;

		long clazzCount = zyHkClassService.currentCount(userId); // 教师班级数量
		dataMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			// 处理待处理列表
			ZyHomeworkQuery query = new ZyHomeworkQuery();
			query.setTeacherId(userId);
			query.setClassId(null);
			query.setCourse(false);
			query.setStatus(Sets.newHashSet(HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE)); // 获取待处理的
			Page<Homework> page = zyHomeworkService.query(query, P.index(pageNo, todoSize));

			List<VHomework> vHomeworks = homeworkConvert.to(page.getItems(), new HomeworkConvertOption(true));
			if (CollectionUtils.isNotEmpty(vHomeworks)) {
				List<Long> homeworkClassIds = new ArrayList<Long>(vHomeworks.size());
				for (VHomework v : vHomeworks) {
					homeworkClassIds.add(v.getHomeworkClazzId());
				}
				Map<Long, VHomeworkClazz> map = zyHkClassConvert.to(zyHkClassService.mget(homeworkClassIds));
				for (VHomework v : vHomeworks) {
					v.setHomeworkClazz(map.get(v.getHomeworkClazzId()));

					// 下发按钮控制
					// see homeworkLook.js -> $scope.hasunSubmit2
					if (v.getStatus() != HomeworkStatus.ISSUED && v.getStatus() != HomeworkStatus.INIT
							&& v.getCorrectingType() == HomeworkCorrectingType.TEACHER) {
						List<StudentHomework> shs = shService.listByHomework(v.getId());
						for (StudentHomework studentHomework : shs) {
							if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED
									&& studentHomework.getStuSubmitAt() != null && studentHomework.getSubmitAt() != null
									&& studentHomework.isStudentCorrected() && studentHomework.getRightRate() == null) {
								v.setShowIssue(false);
							}
						}
					} // 下发按钮控制end
				}
			}

			if (CollectionUtils.isEmpty(vHomeworks)) {
				dataMap.put("todos", Collections.EMPTY_LIST);
				dataMap.put("todosCount", 0);
			} else {
				dataMap.put("todos", vHomeworks);
				dataMap.put("todosCount", page.getTotalCount());
			}
		} else {
			dataMap.put("todos", Collections.EMPTY_LIST);
			dataMap.put("todosCount", 0);
		}
		return new Value(dataMap);
	}

	/**
	 * 查询历史作业.
	 * 
	 * @param pageNo
	 *            页码
	 * @param historySize
	 *            获取条数
	 * @return
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "homeworkManage/queryHistory", method = { RequestMethod.POST })
	public Value queryHistory(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		long userId = Security.getUserId();
		// long userId = 257152333627006976L;

		// Page<Homework> hkPage =
		// zyHomeworkService.queryHistoryHomework(userId, null, null, null,
		// null, null, null,
		// null, true, P.index(pageNo, historySize));

		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(userId);
		query.setClassId(null);
		query.setCourse(false);
		query.setStatus(Sets.newHashSet(HomeworkStatus.ISSUED)); // 获取已下发的
		Page<Homework> hkPage = zyHomeworkService.query(query, P.index(pageNo, historySize));

		List<VHomework> vhkList = hkConvert.to(hkPage.getItems(), new HomeworkConvertOption(true));
		if (CollectionUtils.isNotEmpty(vhkList)) {
			List<Long> classIds = new ArrayList<Long>(vhkList.size());
			for (VHomework v : vhkList) {
				classIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> vmap = zyHkClassConvert.to(zyHkClassService.mget(classIds));
			for (VHomework v : vhkList) {
				v.setHomeworkClazz(vmap.get(v.getHomeworkClazzId()));
			}
		}
		dataMap.put("historyCount", hkPage.getTotalCount());
		dataMap.put("historys", vhkList);
		return new Value(dataMap);
	}

	/**
	 * 下发作业（废弃）
	 * 
	 * @since 2.1
	 * @since 小优快批，2018-2-26，不再有“下发”这一过程
	 * 
	 * @param hkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@ApiAllowed(accessRate = 0)
	@RequestMapping(value = "homeworkManage/issue", method = { RequestMethod.POST })
	@Deprecated
	public Value issue(long hkId) {
		try {
			long userId = Security.getUserId();
			// long userId = 257152333627006976L;

			Homework homework = hkService.get(hkId);
			if (homework.getCreateId().longValue() != userId) {
				return new Value(new PageNotFoundException());
			}
			if (homework.getStatus() == HomeworkStatus.INIT) {
				return new Value(new PageNotFoundException());
			} else if (homework.getStatus() == HomeworkStatus.ISSUED) {
				return new Value();
			}
			zyHomeworkService.issue(homework, true);
			coinsService.earn(CoinsAction.TEA_HOMEOWORK_RESULT, userId, 0, Biz.HOMEWORK, hkId);
			// 处理相关统计
			hkStatisticService.staticAfterIssue(hkId);
			// 给学生发送推送消息
			mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
					MQ.builder().data(new PushHandleForm(PushHandleAction.ISSUED_HOMEWORK, hkId)).build());
			// 统计相关成长值和金币
			growthService.grow(GrowthAction.ISSUE_HOMEWORK, userId, -1, Biz.HOMEWORK, hkId, true);
			coinsService.earn(CoinsAction.ISSUE_HOMEWORK, userId, -1, Biz.HOMEWORK, hkId);
			// 异步统计
			hkStatisticService.asyncStaticHomework(hkId);

			// 微信消息
			zyWXMessageService.sendIssuedHomeworkMessage(hkId);
			return new Value();
		} catch (HomeworkException e) {
			return new Value(e);
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 报告首页数据.
	 * 
	 * @return
	 */
	@MemberAllowed
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "homeworkManage/reportIndex", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getReport() {
		Map<String, Object> dataMap = new HashMap<String, Object>(4);
		dataMap.put("memberType", SecurityContext.getMemberType());
		if (SecurityContext.getMemberType() != MemberType.NONE) {
			long userId = Security.getUserId();
			Teacher teacher = (Teacher) teacherService.getUser(userId);
			if (teacher == null) {
				return new Value();
			}
			if (teacher.getSubjectCode() == null) {
				return new Value(dataMap);
			}
			long classId = 0;

			SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
			Date minDate = null;
			Date maxDate = null;
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.add(Calendar.MONTH, -1);
			try {
				maxDate = format.parse(format.format(cal.getTime()));
			} catch (ParseException e) {
				logger.error("format date error:", e);
			}
			cal.add(Calendar.MONTH, -12);
			try {
				minDate = format.parse(format.format(cal.getTime()));
			} catch (ParseException e) {
				logger.error("format date error:", e);
			}

			List<HomeworkClazz> classList = zyHomeworkClassService.listCurrentClazzs(userId);
			List<Long> classIds = new ArrayList<Long>();
			for (HomeworkClazz hc : classList) {
				classIds.add(hc.getId());
			}
			// 班级列表,当前老师班级下有学情报表数据的集合
			List<HomeworkClazz> clazzs = classStatisticsReportService.getClazzByMinDate(classIds, minDate, maxDate);

			if (clazzs.size() > 0) {
				classId = clazzs.get(0).getId();
			}

			// 年月列表
			List<Map> dates = classStatisticsReportService.getDatesByMinDate(classId, minDate, maxDate);

			ClassStatisticsReport classStatisticsReport = null;
			if (dates.size() > 0) {
				int year = Integer.parseInt(dates.get(0).get("cal_date").toString().split("-")[0]);
				int month = Integer.parseInt(dates.get(0).get("cal_date").toString().split("-")[1]);
				classStatisticsReport = classStatisticsReportService.getClassReport(teacher.getSubjectCode(), classId,
						year, month);
			}

			dataMap.put("clazzs", clazzs);
			dataMap.put("dates", dates);
			dataMap.put("report", classStatisticsReport);
		}
		return new Value(dataMap);
	}

	/**
	 * 根据班级ID获取最近一年的年月列表.
	 * 
	 * @param classId
	 *            班级ID.
	 * @return
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "homeworkManage/getDates", method = { RequestMethod.POST })
	public Value getReport(Long classId) {
		if (classId == null) {
			return new Value(new MissingArgumentException());
		}

		long userId = Security.getUserId();
		// long userId = 257152333627006976L;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		Date minDate = null;
		Date maxDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.MONTH, -1);
		try {
			maxDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}
		cal.add(Calendar.MONTH, -12);
		try {
			minDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}

		Teacher teacher = (Teacher) teacherService.getUser(userId);

		// 年月列表
		List<Map> dates = classStatisticsReportService.getDatesByMinDate(classId, minDate, maxDate);

		return new Value(dates);
	}

	/**
	 * 获取报告数据.
	 * 
	 * @param year
	 *            统计年份
	 * @param month
	 *            统计月份
	 * @param classId
	 *            班级Id
	 * @return
	 */
	@WXBindCheck(userType = "TEACHER", product = "YOOMATH")
	@RequestMapping(value = "homeworkManage/getReport", method = { RequestMethod.POST })
	public Value getReport(Integer year, Integer month, Long classId) {
		if (null == year || null == month || null == classId) {
			return new Value(new MissingArgumentException());
		}
		long userId = Security.getUserId();
		// long userId = 257152333627006976L;

		// Map<String, Object> dataMap = new HashMap<String, Object>();
		Teacher teacher = (Teacher) teacherService.getUser(userId);
		ClassStatisticsReport classStatisticsReport = classStatisticsReportService
				.getClassReport(teacher.getSubjectCode(), classId, year, month);
		return new Value(classStatisticsReport);
	}
}
