package com.lanking.uxb.service.wx.resource;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.constants.MqSessionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.stat.StudentStatisticsReport;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.WXBindCheck;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.report.api.StudentStatisticsReportService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.wx.api.ZyWXReportService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 悠数学微信服务号控制器.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
@RestController
@RequestMapping("zy/wx/s")
public class ZyWXStuController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AccountService accountService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private StudentStatisticsReportService studentStatisticsReportService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyWXReportService zyWXReportService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 作业提醒页.
	 * 
	 * @return
	 */
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind", method = { RequestMethod.GET })
	public Value homeworkRemindIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/page/student/homework-manage.html");
		} catch (IOException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 学业报告页.
	 * 
	 * @return
	 */
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "report", method = { RequestMethod.GET })
	public Value sReportIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect("/page/student/report.html?" + System.currentTimeMillis());
		} catch (IOException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 密码找回页.
	 * 
	 * @return
	 */
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "findPassword", method = { RequestMethod.GET })
	public Value findPasswordIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			long userId = Security.getUserId();
			// long userId = 257170432854073344L;
			User user = accountService.getUserByUserId(userId);
			if (null != user) {
				WebUtils.addCookie(request, response, "CURRENT_USER", URLEncoder.encode(user.getName(), "UTF-8"));
			}
			response.sendRedirect("/page/student/find-password.html");
		} catch (IOException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 作业提醒首页数据.
	 * 
	 * @param todoSize
	 *            待处理作业条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/index", method = { RequestMethod.POST })
	public Value homeworkManage(@RequestParam(value = "todoSize", defaultValue = "20") int todoSize,
			@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		long userId = Security.getUserId();
		// long userId = 257170432854073344L;

		Map<String, Object> dataMap = new HashMap<String, Object>(5);
		long clazzCount = zyHkStuClazzService.countStudentClazz(userId, null);
		dataMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			dataMap.put("todosCount",
					stuHkService.countHomeworks(userId, Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT)));
			dataMap.put("historyCount",
					stuHkService.countHomeworks(userId, Sets.newHashSet(StudentHomeworkStatus.ISSUED)));

			// 待处理作业
			// 待处理作业
			VCursorPage<VStudentHomework> vp1 = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(userId);
			CursorPage<Long, StudentHomework> todoPage = stuHkService.query(todoQuery,
					CP.cursor(Long.MAX_VALUE, Math.min(todoSize, 5)));
			if (todoPage.isEmpty()) {
				vp1.setCursor(Long.MAX_VALUE);
				vp1.setItems(Collections.EMPTY_LIST);
			} else {
				vp1.setCursor(todoPage.getNextCursor());
				List<VStudentHomework> vStudentHomeworks = stuHkConvert.to(todoPage.getItems(), false, true, false,
						false);
				Set<Long> clazzIds = new HashSet<Long>();
				for (VStudentHomework vsh : vStudentHomeworks) {
					clazzIds.add(vsh.getHomework().getHomeworkClazzId());
				}
				Map<Long, VHomeworkClazz> clazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
				for (VStudentHomework vStudentHomework : vStudentHomeworks) {
					vStudentHomework.getHomework()
							.setHomeworkClazz(clazzMap.get(vStudentHomework.getHomework().getHomeworkClazzId()));
				}
				vp1.setItems(vStudentHomeworks);
			}
			dataMap.put("todo", vp1);

			// 历史作业
			VCursorPage<VStudentHomework> vp2 = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
			historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
			historyQuery.setCourse(false);
			historyQuery.setStudentId(userId);
			CursorPage<Long, StudentHomework> historyPage = stuHkService.query(historyQuery,
					CP.cursor(Long.MAX_VALUE, Math.min(historySize, historySize)));
			if (historyPage.isEmpty()) {
				vp2.setCursor(Long.MAX_VALUE);
				vp2.setItems(Collections.EMPTY_LIST);
			} else {
				vp2.setCursor(historyPage.getNextCursor());
				List<VStudentHomework> vStudentHomeworks = stuHkConvert.to(historyPage.getItems(), false, true, false,
						false);
				Set<Long> clazzIds = new HashSet<Long>();
				for (VStudentHomework vsh : vStudentHomeworks) {
					clazzIds.add(vsh.getHomework().getHomeworkClazzId());
				}
				Map<Long, VHomeworkClazz> clazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
				for (VStudentHomework vStudentHomework : vStudentHomeworks) {
					vStudentHomework.getHomework()
							.setHomeworkClazz(clazzMap.get(vStudentHomework.getHomework().getHomeworkClazzId()));
				}
				vp2.setItems(vStudentHomeworks);
			}
			dataMap.put("history", vp2);
		} else {
			dataMap.put("todosCount", 0);
			dataMap.put("historyCount", 0);
		}
		return new Value(dataMap);
	}

	/**
	 * 查询待完成作业.
	 * 
	 * @param cursor
	 *            游标值
	 * @param todoSize
	 *            获取条数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/queryTodos", method = { RequestMethod.POST })
	public Value queryTodos(long cursor, @RequestParam(value = "todoSize", defaultValue = "5") int todoSize) {
		long userId = Security.getUserId();
		// long userId = 257170432854073344L;

		// 待处理作业
		ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
		todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
		todoQuery.setCourse(false);
		todoQuery.setStudentId(userId);
		CursorPage<Long, StudentHomework> todoPage = stuHkService.query(todoQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(todoSize, 5)));

		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		if (todoPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			vp.setCursor(todoPage.getNextCursor());
			List<VStudentHomework> vStudentHomeworks = stuHkConvert.to(todoPage.getItems(), false, true, false, false);
			Set<Long> clazzIds = new HashSet<Long>();
			for (VStudentHomework vsh : vStudentHomeworks) {
				clazzIds.add(vsh.getHomework().getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> clazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
			for (VStudentHomework vStudentHomework : vStudentHomeworks) {
				vStudentHomework.getHomework()
						.setHomeworkClazz(clazzMap.get(vStudentHomework.getHomework().getHomeworkClazzId()));
			}
			vp.setItems(vStudentHomeworks);
		}

		return new Value(vp);
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
	@SuppressWarnings("unchecked")
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/queryHistory", method = { RequestMethod.POST })
	public Value queryHistory(long cursor, @RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		long userId = Security.getUserId();
		// long userId = 257170432854073344L;

		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(userId);
		CursorPage<Long, StudentHomework> historyPage = stuHkService.query(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));
		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			vp.setCursor(historyPage.getNextCursor());
			List<VStudentHomework> vStudentHomeworks = stuHkConvert.to(historyPage.getItems(), false, true, false,
					false);
			Set<Long> clazzIds = new HashSet<Long>();
			for (VStudentHomework vsh : vStudentHomeworks) {
				clazzIds.add(vsh.getHomework().getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> clazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
			for (VStudentHomework vStudentHomework : vStudentHomeworks) {
				vStudentHomework.getHomework()
						.setHomeworkClazz(clazzMap.get(vStudentHomework.getHomework().getHomeworkClazzId()));
			}
			vp.setItems(vStudentHomeworks);
		}
		return new Value(vp);
	}

	/**
	 * 重置密码.
	 * 
	 * @param p1
	 *            密码
	 * @param p2
	 *            确认密码
	 * @param length
	 *            密码长度
	 * @return
	 */
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "resetPassword", method = { RequestMethod.POST })
	public Value resetPassword(String p1, String p2, Integer length) {
		if (StringUtils.isBlank(p1) || StringUtils.isBlank(p2) || length == null) {
			return new Value(new MissingArgumentException());
		}
		if (!p1.equals(p2)) {
			return new Value(new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT));
		}
		if (Security.isLogin()) {
			long accountId = Security.getAccountId();
			Credential credential = credentialService.getCredentialByAccountId(Product.YOOMATH,
					CredentialType.WEIXIN_MP, accountId);
			if (null != credential) {
				accountService.updatePassword(accountId, p1, length);
				JSONObject jo = new JSONObject();
				jo.put("userId", Security.getUserId());
				jo.put("excludeToken", Security.getToken());
				mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
						MQ.builder().data(jo).build());
			}
		}
		return new Value();
	}

	/**
	 * 报告首页数据.
	 * 
	 * @return
	 */
	@MemberAllowed
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/reportIndex", method = { RequestMethod.POST })
	public Value getReport(Boolean buy) {
		Map<String, Object> dataMap = new HashMap<String, Object>(6);
		dataMap.put("memberType", SecurityContext.getMemberType());

		long userId = Security.getUserId();

		Student student = (Student) studentService.getUser(userId);
		if (student == null) {
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
			;
		}
		cal.add(Calendar.MONTH, -12);
		try {
			minDate = format.parse(format.format(cal.getTime()));
		} catch (ParseException e) {
			logger.error("format date error:", e);
		}

		// 班级列表
		List<HomeworkClazz> clazzs = studentStatisticsReportService.getClazzByMinDate(null, userId, minDate, maxDate,
				buy);
		if (clazzs.size() > 0) {
			classId = clazzs.get(0).getId();
		}

		// 年月列表
		List<String> dates = studentStatisticsReportService.getDatesByMinDate(null, userId, classId, minDate, maxDate,
				buy);
		StudentStatisticsReport studentStatisticsReport = null;
		if (dates.size() > 0) {
			int year = Integer.parseInt(dates.get(0).split("-")[0]);
			int month = Integer.parseInt(dates.get(0).split("-")[1]);
			studentStatisticsReport = studentStatisticsReportService.getStudentReport(userId, classId, year, month);
		}

		dataMap.put("name", student.getName());
		if (null != student.getSchoolId()) {
			dataMap.put("school", schoolService.get(student.getSchoolId()));
		}
		dataMap.put("clazzs", clazzs);
		dataMap.put("dates", dates);
		dataMap.put("report", studentStatisticsReport);
		/*
		 * if (SecurityContext.getMemberType() != MemberType.NONE) {
		 * dataMap.put("report", studentStatisticsReport); }
		 */
		return new Value(dataMap);
	}

	/**
	 * 根据班级ID获取最近一年的年月列表.
	 * 
	 * @param classId
	 *            班级ID.
	 * @return
	 */
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/getDates", method = { RequestMethod.POST })
	public Value getReport(Long classId, Boolean buy) {
		if (classId == null) {
			return new Value(new MissingArgumentException());
		}

		long userId = Security.getUserId();
		// long userId = 257170432854073344L;

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

		// 年月列表
		List<String> dates = studentStatisticsReportService.getDatesByMinDate(null, userId, classId, minDate, maxDate,
				buy);

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
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/getReport", method = { RequestMethod.POST })
	public Value getReport(Integer year, Integer month, Long classId) {
		if (null == year || null == month || null == classId) {
			return new Value(new MissingArgumentException());
		}
		long userId = Security.getUserId();
		// long userId = 257170432854073344L;

		StudentStatisticsReport studentStatisticsReport = studentStatisticsReportService.getStudentReport(userId,
				classId, year, month);
		return new Value(studentStatisticsReport);
	}

	/**
	 * 购买报告数据.
	 * 
	 * @param reportId
	 *            报告ID
	 * @param code
	 *            报告兑换码
	 * @return
	 */
	@WXBindCheck(userType = "STUDENT", product = "YOOMATH")
	@RequestMapping(value = "homeworkRemind/buyReport", method = { RequestMethod.POST })
	public Value buyReport(Long reportId, String reportCode) {
		if (null == reportId || null == reportCode) {
			return new Value(new MissingArgumentException());
		}
		long userId = Security.getUserId();
		// long accountId = Security.getAccountId();
		// long userId = 257170432854073344L;
		// long accountId = 257170432812130304L;

		StudentStatisticsReport studentStatisticsReport = studentStatisticsReportService.get(reportId);
		if (null != studentStatisticsReport && studentStatisticsReport.getStudentId().longValue() == userId) {
			// Credential credential =
			// credentialService.getCredentialByAccountId(Product.YOOMATH,
			// CredentialType.WEIXIN,
			// accountId);
			// if (credential != null) {
			// if (reportCode.equals("88888888")) {
			// // 购买成功
			// studentStatisticsReportService.buReport(reportId);
			// return new Value(true);
			// }

			Boolean same = zyWXReportService.checkReportCode(reportCode);
			if (same != null && same) {
				// 购买成功
				studentStatisticsReportService.buReport(reportId);
			}
			return new Value(same);
			// } else {
			// return new Value(new UxbException(UxbException.NO_PERMISSON));
			// }
		} else {
			return new Value(new IllegalArgException());
		}
	}
}
