package com.lanking.uxb.service.index.resource;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLogStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStarLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStatus;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskType;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskUserScope;
import com.lanking.cloud.domain.yoo.recommend.RecommendKnowpointCard;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalCount;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.ActivityEntranceCfgService;
import com.lanking.uxb.service.activity.convert.ActivityEntranceCfgConvert;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.convert.KnowledgePointCardConvert;
import com.lanking.uxb.service.code.value.VKnowledgePointCard;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.dailyPractice.resource.ZyMStuDailyPracticeController;
import com.lanking.uxb.service.doQuestion.api.DoQuestionGoalService;
import com.lanking.uxb.service.doQuestion.convert.DoQuestionGoalConvert;
import com.lanking.uxb.service.doQuestion.convert.DoQuestionGoalLevelConvert;
import com.lanking.uxb.service.doQuestion.value.VDoQuestionGoal;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.api.UserTaskStarLogService;
import com.lanking.uxb.service.honor.convert.UserTaskConvert;
import com.lanking.uxb.service.honor.form.UserTaskQueryForm;
import com.lanking.uxb.service.honor.value.VUserTask;
import com.lanking.uxb.service.push.util.YmPushUrls;
import com.lanking.uxb.service.ranking.api.DoQuestionClassRankService;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingQuery;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingService;
import com.lanking.uxb.service.ranking.util.DoQuestionClassRankUtil;
import com.lanking.uxb.service.report.api.StudentStatisticsReportService;
import com.lanking.uxb.service.report.api.StudentWeekReportService;
import com.lanking.uxb.service.report.cache.LearnReportCacheService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.api.EmbeddedAppQuery;
import com.lanking.uxb.service.sys.api.EmbeddedAppService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.sys.convert.EmbeddedAppConvert;
import com.lanking.uxb.service.sys.value.VEmbeddedApp;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.web.api.RecommendKnowpointCardService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.value.VDailyPractise;

/**
 * 学生端首页相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月28日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s")
public class ZyMStuIndexController {
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;
	@Autowired
	private ZyMStuDailyPracticeController stuDailyPracticeController;
	@Autowired
	private DoQuestionGoalLevelConvert doQuestionGoalLevelConvert;
	@Autowired
	private DoQuestionGoalService doQuestionGoalService;
	@Autowired
	private DoQuestionGoalConvert doQuestionGoalConvert;
	@Autowired
	private DoQuestionRankingService doQuestionRankingService;
	@Autowired
	private LearnReportCacheService learnReportCacheService;
	@Autowired
	private StudentStatisticsReportService studentStatisticsReportService;
	@Autowired
	private ActivityEntranceCfgService activityEntranceCfgService;
	@Autowired
	private ActivityEntranceCfgConvert activityEntranceCfgConvert;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private EmbeddedAppService embeddedAppService;
	@Autowired
	private EmbeddedAppConvert embeddedAppConvert;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private RecommendKnowpointCardService recommendKnowpointCardService;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;
	@Autowired
	private KnowledgePointCardConvert knowledgePointCardConvert;
	@Autowired
	private UserTaskService userTaskService;
	@Autowired
	private UserTaskConvert userTaskConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserTaskStarLogService starLogService;
	@Autowired
	private DoQuestionClassRankService doQuestionClassRankService;
	@Autowired
	private StudentWeekReportService studentWeekReportService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		Map<String, Object> data = new HashMap<String, Object>(10);
		// 目标
		Map<String, Object> goalMap = new HashMap<String, Object>(3);
		DoQuestionGoal myGoal = doQuestionGoalService.findByUserId(Security.getUserId());
		if (myGoal != null) {
			VDoQuestionGoal vmyGoal = doQuestionGoalConvert.to(myGoal);
			goalMap.put("myGoal", vmyGoal);
			long date0 = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			DoQuestionGoalCount myGoalCount = doQuestionGoalService.findByUserId(Security.getUserId(), date0);
			vmyGoal.setCompletedGoal(myGoalCount != null ? myGoalCount.getGoal() : 0);

		}
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isNotEmpty(clazzs)) {
			long classId = clazzs.get(0).getClassId();
			// 查找排名
			DoQuestionRankingQuery query = new DoQuestionRankingQuery();
			query.setClassId(classId);
			query.setDay(7);
			query.setStudentId(Security.getUserId());
			DoQuestionClassStat stat = doQuestionRankingService.findStudentInClassStat(query);
			if (stat != null && stat.getRank() != null) {
				goalMap.put("classRanking", stat.getRank());
			}
		}
		data.put("goal", goalMap);
		// 作业
		Map<String, Object> homeworkMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		homeworkMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			todoQuery.setCursorType("startTime");
			todoQuery.setMobileIndex(true);
			CursorPage<Long, Map> todoPage = stuHkService.queryUnionHolidayStuHk(todoQuery,
					CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				homeworkMap.put("todo", Collections.EMPTY_LIST);
			} else {
				int todoSize = todoPage.getItemSize();
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
				List<Long> ids = new ArrayList<Long>(todoSize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Long> stuHolidayHkIds = Lists.newArrayList();
				List<Map> maps = todoPage.getItems();
				for (Map map : maps) {
					int type = ((BigInteger) map.get("type")).intValue();
					long id = ((BigInteger) map.get("id")).longValue();
					if (type == 1) {
						stuHkIds.add(id);
					} else if (type == 2) {
						stuHolidayHkIds.add(id);
					}
					ids.add(id);
				}
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				if (stuHolidayHkIds.size() > 0) {
					Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
					vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				homeworkMap.put("todo", items);
			}
		}
		data.put("homework", homeworkMap);
		// 每日练
		Map<String, Object> dailyPracticeMap = (Map<String, Object>) stuDailyPracticeController.findDailyPractise(1)
				.getRet();
		VCursorPage<VDailyPractise> dailyPracticePage = (VCursorPage<VDailyPractise>) dailyPracticeMap.get("practises");
		if (CollectionUtils.isNotEmpty(dailyPracticePage.getItems())) {
			if (!dailyPracticePage.getItems().get(0).isNowDay()) {
				dailyPracticePage.setItems(Collections.EMPTY_LIST);
				dailyPracticeMap.put("practises", dailyPracticePage);
			}
		}
		dailyPracticeMap.put("needSetting", false);
		data.put("dailyPractice", dailyPracticeMap);
		// 学业报告
		Map<String, Object> report = new HashMap<String, Object>(3);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		String cacheFlag = learnReportCacheService.getCurMonthTips(Security.getUserId());
		if ("true".equals(cacheFlag)) {
			report.put("show", false);
		} else {
			if ("false".equals(cacheFlag)) {
				report.put("show", true);
			} else {
				boolean exist = studentStatisticsReportService.existReport(now.get(Calendar.YEAR),
						now.get(Calendar.MONTH) + 1, Security.getUserId());
				if (exist) {
					report.put("show", true);
					learnReportCacheService.setCurMonthTips(Security.getUserId(), false);
				} else {
					report.put("show", false);
				}
			}
		}
		report.put("title", now.get(Calendar.MONTH) + 1 + "月份学业分析报告");
		report.put("url", Env.getString("report.learn.url", new Object[] { Security.getToken() }));
		data.put("report", report);
		// 活动入口
		ActivityEntranceCfg cfg = activityEntranceCfgService.findByApp(YooApp.MATH_STUDENT);
		if (cfg != null) {
			data.put("activityEntranceCfg", activityEntranceCfgConvert.to(cfg));
		}
		// banner
		List<Banner> banners = bannerService.listEnable(new BannerQuery(YooApp.MATH_STUDENT, BannerLocation.HOME));
		if (CollectionUtils.isNotEmpty(banners)) {
			data.put("banner", bannerConvert.to(banners));
		} else {
			data.put("banner", Collections.EMPTY_LIST);
		}
		// 内嵌入口
		List<EmbeddedApp> embeddedApps = embeddedAppService
				.list(new EmbeddedAppQuery(YooApp.MATH_STUDENT, EmbeddedAppLocation.HOME));
		if (CollectionUtils.isNotEmpty(embeddedApps)) {
			boolean newDp = false;
			if (CollectionUtils.isNotEmpty(dailyPracticePage.getItems())) {
				VDailyPractise firstDp = dailyPracticePage.getItems().get(0);
				newDp = firstDp.isNowDay() && firstDp.getCommitAt() == null;
			}

			List<VEmbeddedApp> apps = embeddedAppConvert.to(embeddedApps);
			for (VEmbeddedApp app : apps) {
				if (YmPushUrls.MATH_STUDENT_DAILYPRACTICE_HOME.equals(app.getUrl()) && newDp) {
					app.setMsg(1);
					break;
				}
			}
			data.put("embeddedApp", apps);
		} else {
			data.put("embeddedApp", Collections.EMPTY_LIST);
		}
		// 是否提示绑定信息
		Account account = accountService.getAccountByUserId(Security.getUserId());
		List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, Security.getAccountId());
		boolean hasBindQQ = false;
		boolean hasBindWX = false;

		for (Credential c : credentials) {
			if (c.getType() == CredentialType.QQ) {
				hasBindQQ = true;
				continue;
			}
			if (c.getType() == CredentialType.WEIXIN) {
				hasBindWX = true;
			}
		}
		data.put("showBindInfo", StringUtils.isEmpty(account.getEmail()) && StringUtils.isEmpty(account.getMobile())
				&& (!hasBindQQ) && (!hasBindWX));

		// 知识卡片推荐
		RecommendKnowpointCard recommendKnowpointCard = recommendKnowpointCardService
				.getRecommendKnowpointCard(Security.getUserId());
		if (recommendKnowpointCard != null) {
			KnowledgePointCard knowledgePointCard = knowledgePointCardService
					.get(recommendKnowpointCard.getKnowpointCardId());
			VKnowledgePointCard vcard = knowledgePointCardConvert.to(knowledgePointCard);
			data.put("card", vcard);
		}

		return new Value(data);
	}

	/**
	 *
	 * @since 4.0.0
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "2/index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index2() {
		Map<String, Object> data = new HashMap<String, Object>(11);
		// 目标
		Map<String, Object> goalMap = new HashMap<String, Object>(3);
		DoQuestionGoal myGoal = doQuestionGoalService.findByUserId(Security.getUserId());
		if (myGoal != null) {
			VDoQuestionGoal vmyGoal = doQuestionGoalConvert.to(myGoal);
			goalMap.put("myGoal", vmyGoal);
			long date0 = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			DoQuestionGoalCount myGoalCount = doQuestionGoalService.findByUserId(Security.getUserId(), date0);
			vmyGoal.setCompletedGoal(myGoalCount != null ? myGoalCount.getGoal() : 0);

		}
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isNotEmpty(clazzs)) {
			long classId = clazzs.get(0).getClassId();
			// 查找排名
			// DoQuestionRankingQuery query = new DoQuestionRankingQuery();
			// query.setClassId(classId);
			// query.setDay(7);
			// query.setStudentId(Security.getUserId());
			// DoQuestionClassStat stat =
			// doQuestionRankingService.findStudentInClassStat(query);
			// if (stat != null && stat.getRank() != null) {
			// goalMap.put("classRanking", stat.getRank());
			// }

			// @since1.4.7 app端采用新版排行榜
			// 查询时间信息
			Map<String, Integer> timeInfo = DoQuestionClassRankUtil.getNowTime(7);
			int startDate = timeInfo.get("startDate");
			int endDate = timeInfo.get("endDate");
			DoQuestionClassRank rank = doQuestionClassRankService.findStudentInClassRank(classId, startDate, endDate,
					Security.getUserId());
			if (rank != null && rank.getRank() != null) {
				goalMap.put("classRanking", rank.getRank());
			}
		}
		data.put("goal", goalMap);
		// 作业
		Map<String, Object> homeworkMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		homeworkMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			todoQuery.setCursorType("startTime");
			todoQuery.setMobileIndex(true);
			CursorPage<Long, Map> todoPage = stuHkService.queryUnionHolidayStuHk(todoQuery,
					CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				homeworkMap.put("todo", Collections.EMPTY_LIST);
			} else {
				int todoSize = todoPage.getItemSize();
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
				List<Long> ids = new ArrayList<Long>(todoSize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Long> stuHolidayHkIds = Lists.newArrayList();
				List<Map> maps = todoPage.getItems();
				for (Map map : maps) {
					int type = ((BigInteger) map.get("type")).intValue();
					long id = ((BigInteger) map.get("id")).longValue();
					if (type == 1) {
						stuHkIds.add(id);
					} else if (type == 2) {
						stuHolidayHkIds.add(id);
					}
					ids.add(id);
				}
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				if (stuHolidayHkIds.size() > 0) {
					Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
					vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				homeworkMap.put("todo", items);
			}
		}
		data.put("homework", homeworkMap);
		// 每日练
		Map<String, Object> dailyPracticeMap = (Map<String, Object>) stuDailyPracticeController.findDailyPractise(1)
				.getRet();
		VCursorPage<VDailyPractise> dailyPracticePage = (VCursorPage<VDailyPractise>) dailyPracticeMap.get("practises");
		if (CollectionUtils.isNotEmpty(dailyPracticePage.getItems())) {
			if (!dailyPracticePage.getItems().get(0).isNowDay()) {
				dailyPracticePage.setItems(Collections.EMPTY_LIST);
				dailyPracticeMap.put("practises", dailyPracticePage);
			}
		}
		dailyPracticeMap.put("needSetting", false);
		data.put("dailyPractice", dailyPracticeMap);
		// 学业报告
		// @since v1.4.7 学业报告替换成周成绩报告
		// 缓存直接复用之前的学业报告
		Map<String, Object> report = new HashMap<String, Object>(3);
		// Calendar now = Calendar.getInstance();
		// now.add(Calendar.MONTH, -1);
		String cacheFlag = learnReportCacheService.getCurWeekTips(Security.getUserId());
		// if ("true".equals(cacheFlag)) {
		// report.put("show", false);
		// } else {
		// if ("false".equals(cacheFlag)) {
		// report.put("show", true);
		// } else {
		// boolean exist =
		// studentStatisticsReportService.existReport(now.get(Calendar.YEAR),
		// now.get(Calendar.MONTH) + 1, Security.getUserId());
		// if (exist) {
		// report.put("show", true);
		// learnReportCacheService.setCurMonthTips(Security.getUserId(), false);
		// } else {
		// report.put("show", false);
		// }
		// }
		// }
		int week = LocalDate.now().getDayOfWeek().getValue();
		String startDate = LocalDate.now().minusWeeks(1).minusDays(week - 1).toString();
		String endDate = LocalDate.now().minusWeeks(1).plusDays(7 - week).toString();
		if ("true".equals(cacheFlag)) {
			report.put("show", false);
		} else {
			StudentWeekReport studentWeekReport = studentWeekReportService.findWeekReport(Security.getUserId(),
					startDate, endDate);
			if ("false".equals(cacheFlag) && studentWeekReport != null) {
				report.put("show", true);
			} else {
				if (studentWeekReport != null) {
					report.put("show", true);
					learnReportCacheService.setCurWeekTips(Security.getUserId(), false);
				} else {
					report.put("show", false);
				}
			}
		}
		report.put("title", getWeekReportTital(startDate, endDate));
		// report.put("url", Env.getString("report.learn.url", new
		// Object[]{Security.getToken()}));
		report.put("url", Env.getString("reportStu.h5.url", new Object[] { Security.getUserId() }));
		data.put("report", report);
		// 活动入口
		ActivityEntranceCfg cfg = activityEntranceCfgService.findByApp(YooApp.MATH_STUDENT);
		if (cfg != null) {
			data.put("activityEntranceCfg", activityEntranceCfgConvert.to(cfg));
		}
		// banner
		List<Banner> banners = bannerService.listEnable(new BannerQuery(YooApp.MATH_STUDENT, BannerLocation.HOME));
		if (CollectionUtils.isNotEmpty(banners)) {
			data.put("banner", bannerConvert.to(banners));
		} else {
			data.put("banner", Collections.EMPTY_LIST);
		}
		// 内嵌入口
		List<EmbeddedApp> embeddedApps = embeddedAppService
				.list(new EmbeddedAppQuery(YooApp.MATH_STUDENT, EmbeddedAppLocation.HOME));
		if (CollectionUtils.isNotEmpty(embeddedApps)) {
			boolean newDp = false;
			if (CollectionUtils.isNotEmpty(dailyPracticePage.getItems())) {
				VDailyPractise firstDp = dailyPracticePage.getItems().get(0);
				newDp = firstDp.isNowDay() && firstDp.getCommitAt() == null;
			}

			List<VEmbeddedApp> apps = embeddedAppConvert.to(embeddedApps);
			for (VEmbeddedApp app : apps) {
				if (YmPushUrls.MATH_STUDENT_DAILYPRACTICE_HOME.equals(app.getUrl()) && newDp) {
					app.setMsg(1);
					break;
				}
			}
			data.put("embeddedApp", apps);
		} else {
			data.put("embeddedApp", Collections.EMPTY_LIST);
		}
		// 是否提示绑定信息
		Account account = accountService.getAccountByUserId(Security.getUserId());
		List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, Security.getAccountId());
		boolean hasBindQQ = false;
		boolean hasBindWX = false;

		for (Credential c : credentials) {
			if (c.getType() == CredentialType.QQ) {
				hasBindQQ = true;
				continue;
			}
			if (c.getType() == CredentialType.WEIXIN) {
				hasBindWX = true;
			}
		}
		data.put("showBindInfo", StringUtils.isEmpty(account.getEmail()) && StringUtils.isEmpty(account.getMobile())
				&& (!hasBindQQ) && (!hasBindWX));

		UserTaskQueryForm form = new UserTaskQueryForm();
		UserType userType = Security.getUserType();
		form.setUserType(userType);
		form.setStatus(UserTaskStatus.OPEN);
		User user = userService.get(Security.getUserId());
		form.setScope(user.getUserChannelCode() > 10000 ? UserTaskUserScope.CHANNEL_USER
				: UserTaskUserScope.SELF_REGISTRATION);

		List<UserTask> userTasks = userTaskService.findNotFindFinishUserTask(form, Security.getUserId());
		List<VUserTask> vs = userTaskConvert.to(userTasks, true);

		List<VUserTask> taskList = new ArrayList<VUserTask>(vs.size());

		VUserTask homeworkTask = null;
		boolean hasMoreTask = false, hasUnCompleteNewUserTask = false;
		for (VUserTask v : vs) {
			if (v.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
				hasMoreTask = true;
			}
			// 如果是签到任务显示完成
			if (v.getCode() == 101010001 && v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				Date date = new Date();
				hasMoreTask = true;
				String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
				Date yesterday = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
					yesterday = DateUtils.addDays(date, -1);
				} catch (ParseException e) {
				}
				UserTask userTask = userTaskService.get(v.getCode());
				UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(v.getCode(), Security.getUserId(),
						yesterday);
				int coins = 0, growth = 0, stars = 0;
				UserTaskRuleCfg userTaskRuleCfg = userTask.getUserTaskRuleCfg();
				List<Integer> itemCoins = userTaskRuleCfg.getItemCoins();
				List<Integer> itemStars = userTaskRuleCfg.getItemStar();
				List<Integer> itemGrowth = userTaskRuleCfg.getItemGrowth();
				if (yesterdayLog == null) {
					if (itemCoins != null) {
						coins = itemCoins.get(0);
					}
					if (itemGrowth != null) {
						growth = itemGrowth.get(0);
					}
					if (itemStars != null) {
						stars = itemStars.get(0);
					}
				} else {
					for (Integer c : itemCoins) {
						coins += c;
					}
					for (Integer g : itemGrowth) {
						growth += g;
					}
					for (Integer s : itemStars) {
						stars += s;
					}
				}
				v.getLog().setStar(stars);
				v.getLog().setCoins(coins);
				v.getLog().setGrowth(growth);
				v.getLog().setStatus(UserTaskLogStatus.COMPLETE);
			}
			// 检测学生个人信息完成情况
			if (v.getCode() == 101000001 && v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				// 发送mq及时更新
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", true);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}
			// 新手log任务为空处理
			if (v.getType() == UserTaskType.NEW_USER) {
				this.handleTaskLog(v);
				if (v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
					hasUnCompleteNewUserTask = true;
				}
			}

			if (v.getCode() == 101010002) {
				homeworkTask = v;
				continue;
			}
			if (v.getLog() != null && v.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
				taskList.add(0, v);
			} else {
				taskList.add(v);
			}
		}

		if (!hasUnCompleteNewUserTask) {
			// 没有未完成的新手任务
			Date date = userTaskLogService.getLatestCompleteDate(UserTaskType.NEW_USER, Security.getUserId());
			if (date == null) {
				data.put("showNewUserReward", false);
			} else {
				if (System.currentTimeMillis() - date.getTime() > TimeUnit.HOURS.toMillis(24)) {
					data.put("showNewUserReward", false);
				} else {
					data.put("showNewUserReward", true);
				}
			}
		} else {
			data.put("showNewUserReward", true);
		}
		data.put("userTasks", taskList);

		// 当前的日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format.format(date));
		} catch (Exception e) {
		}

		if (hasMoreTask) {
			// 新手任务中有未领取的数据
			data.put("hasMoreTasks", true);
		} else {
			Long notReceiveTaskCount = userTaskLogService.countNotReceiveTask(UserTaskType.ACHIEVEMENT,
					Security.getUserId());
			if (notReceiveTaskCount != null && notReceiveTaskCount > 0) {
				// 成就任务中有未领取的任务
				data.put("hasMoreTasks", true);
			} else {

				// 判断是否有宝箱未领取
				UserTaskStarLog starLog = starLogService.query(date, Security.getUserId());
				if (starLog == null) {
					data.put("hasMoreTasks", false);
				} else {
					if (starLog.getStar() <= 0) {
						data.put("hasMoreTasks", false);
					} else {
						int star = starLog.getStar();
						String treasureRuleValue = parameterService.get(Product.YOOMATH, "userTask.treasure.rule")
								.getValue();
						List<Integer> canReceiveStarReward = new ArrayList<Integer>(3);
						if (StringUtils.isNotBlank(treasureRuleValue)) {
							JSONObject treasureRuleJson = JSONObject.parseObject(treasureRuleValue);
							JSONArray levelArr = treasureRuleJson.getJSONArray("starLevels");

							for (Object o : levelArr) {
								JSONObject oj = JSONObject.parseObject(o.toString());

								if (star >= oj.getInteger("star")) {
									canReceiveStarReward.add(oj.getInteger("star"));
								}
							}
						}
						List<Integer> receiveStarLogs = null;
						if (StringUtils.isNotBlank(starLog.getContent())) {
							JSONObject jsonObject = JSONObject.parseObject(starLog.getContent());
							JSONArray takeLogArr = jsonObject.getJSONArray("takeLogs");
							receiveStarLogs = new ArrayList<Integer>(takeLogArr.size());

							for (Object o : takeLogArr) {
								JSONObject logObj = JSONObject.parseObject(o.toString());
								receiveStarLogs.add(logObj.getInteger("star"));
							}
						}

						boolean hasUnGet = false;
						if (CollectionUtils.isNotEmpty(canReceiveStarReward)) {
							if (CollectionUtils.isNotEmpty(receiveStarLogs)) {
								for (Integer canReceiveStar : canReceiveStarReward) {
									if (!receiveStarLogs.contains(canReceiveStar)) {
										hasUnGet = true;
										break;
									}
								}
							} else {
								hasUnGet = true;
							}
						}

						data.put("hasMoreTasks", hasUnGet);
					}

				}
			}
		}
		Parameter parameter = parameterService.get(Product.YOOMATH, "stu.yoo.know.h5.url");
		List<CoinsLog> userGuideLogs = coinsLogService.findLogs(101000005, Security.getUserId(), null);
		if (CollectionUtils.isEmpty(userGuideLogs)) {
			data.put("learnMoreUrl", parameter == null ? "" : parameter.getValue());
		}

		// 用户任务更多h5 url
		Parameter moreTaskH5Url = parameterService.get(Product.YOOMATH, "stu.userTask.index.url");
		data.put("taskIndexUrl", moreTaskH5Url == null ? "" : moreTaskH5Url.getValue());

		UserTaskLog userTaskLog = userTaskLogService.findByCodeAndUser(101010002, Security.getUserId(), date);
		// 若没有需要完成的作业则不需要显示"完成作业"当日任务
		if (CollectionUtils.isNotEmpty((Collection) homeworkMap.get("todo"))
				|| (userTaskLog != null && userTaskLog.getStatus() != UserTaskLogStatus.RECEIVE)) {
			data.put("homeworkTask", homeworkTask);
		}

		return new Value(data);
	}
	
	/**
	 * 获取学生端首页的banner和目标，还有活动入口
	 * @since 4.0.0
	 * @return  
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/bannerAndGoal", method = { RequestMethod.GET, RequestMethod.POST })
	public Value bannerAndGoal() {
		Map<String, Object> data = new HashMap<String, Object>(11);
		// 目标
		Map<String, Object> goalMap = new HashMap<String, Object>(3);
		DoQuestionGoal myGoal = doQuestionGoalService.findByUserId(Security.getUserId());
		if (myGoal != null) {
			VDoQuestionGoal vmyGoal = doQuestionGoalConvert.to(myGoal);
			goalMap.put("myGoal", vmyGoal);
			long date0 = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			DoQuestionGoalCount myGoalCount = doQuestionGoalService.findByUserId(Security.getUserId(), date0);
			vmyGoal.setCompletedGoal(myGoalCount != null ? myGoalCount.getGoal() : 0);

		}
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isNotEmpty(clazzs)) {
			long classId = clazzs.get(0).getClassId();

			// @since1.4.7 app端采用新版排行榜
			// 查询时间信息
			Map<String, Integer> timeInfo = DoQuestionClassRankUtil.getNowTime(7);
			int startDate = timeInfo.get("startDate");
			int endDate = timeInfo.get("endDate");
			DoQuestionClassRank rank = doQuestionClassRankService.findStudentInClassRank(classId, startDate, endDate,
					Security.getUserId());
			if (rank != null && rank.getRank() != null) {
				goalMap.put("classRanking", rank.getRank());
			}
		}
		data.put("goal", goalMap);
		
		// 活动入口
		ActivityEntranceCfg cfg = activityEntranceCfgService.findByApp(YooApp.MATH_STUDENT);
		if (cfg != null) {
			data.put("activityEntranceCfg", activityEntranceCfgConvert.to(cfg));
		}
		
		// 每日练
		Map<String, Object> dailyPracticeMap = (Map<String, Object>) stuDailyPracticeController.findDailyPractise(1)
				.getRet();
		VCursorPage<VDailyPractise> dailyPracticePage = (VCursorPage<VDailyPractise>) dailyPracticeMap.get("practises");
		if (CollectionUtils.isNotEmpty(dailyPracticePage.getItems())) {
			if (!dailyPracticePage.getItems().get(0).isNowDay()) {
				dailyPracticePage.setItems(Collections.EMPTY_LIST);
				dailyPracticeMap.put("practises", dailyPracticePage);
			}
		}
		dailyPracticeMap.put("needSetting", false);
		data.put("dailyPractice", dailyPracticeMap);
		
		// 内嵌入口
		List<EmbeddedApp> embeddedApps = embeddedAppService
				.list(new EmbeddedAppQuery(YooApp.MATH_STUDENT, EmbeddedAppLocation.HOME));
		if (CollectionUtils.isNotEmpty(embeddedApps)) {
			boolean newDp = false;
			if (CollectionUtils.isNotEmpty(dailyPracticePage.getItems())) {
				VDailyPractise firstDp = dailyPracticePage.getItems().get(0);
				newDp = firstDp.isNowDay() && firstDp.getCommitAt() == null;
			}

			List<VEmbeddedApp> apps = embeddedAppConvert.to(embeddedApps);
			for (VEmbeddedApp app : apps) {
				if (YmPushUrls.MATH_STUDENT_DAILYPRACTICE_HOME.equals(app.getUrl()) && newDp) {
					app.setMsg(1);
					break;
				}
			}
			data.put("embeddedApp", apps);
		} else {
			data.put("embeddedApp", Collections.EMPTY_LIST);
		}
		
		// banner
		List<Banner> banners = bannerService.listEnable(new BannerQuery(YooApp.MATH_STUDENT, BannerLocation.HOME));
		if (CollectionUtils.isNotEmpty(banners)) {
			data.put("banner", bannerConvert.to(banners));
		} else {
			data.put("banner", Collections.EMPTY_LIST);
		}
		
		return new Value(data);
	}
	
	
	/**
	 * 获取学生端首页的日常任务和作业列表
	 * @since 4.0.0
	 * @return  
	 */
	@Deprecated
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/taskAndHomework", method = { RequestMethod.GET, RequestMethod.POST })
	public Value taskAndHomework() {
		Map<String, Object> data = new HashMap<String, Object>(11);
		
		// 作业
		Map<String, Object> homeworkMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		homeworkMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			todoQuery.setCursorType("startTime");
			todoQuery.setMobileIndex(true);
			CursorPage<Long, Map> todoPage = stuHkService.queryUnionHolidayStuHk(todoQuery,
					CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				homeworkMap.put("todo", Collections.EMPTY_LIST);
			} else {
				int todoSize = todoPage.getItemSize();
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
				List<Long> ids = new ArrayList<Long>(todoSize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Long> stuHolidayHkIds = Lists.newArrayList();
				List<Map> maps = todoPage.getItems();
                for (Map map : maps) {
                   int type = ((BigInteger) map.get("type")).intValue();
                   long id = ((BigInteger) map.get("id")).longValue();
                   if (type == 1) {
                       stuHkIds.add(id);
                   } else if (type == 2) {
                       stuHolidayHkIds.add(id);
                   }
                   ids.add(id);
                 }

				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				homeworkMap.put("todo", items);
			}
		}
		data.put("homework", homeworkMap);
		
		// 是否提示绑定信息
		Account account = accountService.getAccountByUserId(Security.getUserId());
		List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, Security.getAccountId());
		boolean hasBindQQ = false;
		boolean hasBindWX = false;

		for (Credential c : credentials) {
			if (c.getType() == CredentialType.QQ) {
				hasBindQQ = true;
				continue;
			}
			if (c.getType() == CredentialType.WEIXIN) {
				hasBindWX = true;
			}
		}
		data.put("showBindInfo", StringUtils.isEmpty(account.getEmail()) && StringUtils.isEmpty(account.getMobile())
				&& (!hasBindQQ) && (!hasBindWX));
		
		UserTaskQueryForm form = new UserTaskQueryForm();
		UserType userType = Security.getUserType();
		form.setUserType(userType);
		form.setStatus(UserTaskStatus.OPEN);
		User user = userService.get(Security.getUserId());
		form.setScope(user.getUserChannelCode() > 10000 ? UserTaskUserScope.CHANNEL_USER
				: UserTaskUserScope.SELF_REGISTRATION);

		List<UserTask> userTasks = userTaskService.findNotFindFinishUserTask(form, Security.getUserId());
		List<VUserTask> vs = userTaskConvert.to(userTasks, true);

		List<VUserTask> taskList = new ArrayList<VUserTask>(vs.size());

		VUserTask homeworkTask = null;
		boolean hasMoreTask = false, hasUnCompleteNewUserTask = false;
		for (VUserTask v : vs) {
			if (v.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
				hasMoreTask = true;
			}
			// 如果是签到任务显示完成
			if (v.getCode() == 101010001 && v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				Date date = new Date();
				hasMoreTask = true;
				String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
				Date yesterday = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
					yesterday = DateUtils.addDays(date, -1);
				} catch (ParseException e) {
				}
				UserTask userTask = userTaskService.get(v.getCode());
				UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(v.getCode(), Security.getUserId(),
						yesterday);
				int coins = 0, growth = 0, stars = 0;
				UserTaskRuleCfg userTaskRuleCfg = userTask.getUserTaskRuleCfg();
				List<Integer> itemCoins = userTaskRuleCfg.getItemCoins();
				List<Integer> itemStars = userTaskRuleCfg.getItemStar();
				List<Integer> itemGrowth = userTaskRuleCfg.getItemGrowth();
				if (yesterdayLog == null) {
					if (itemCoins != null) {
						coins = itemCoins.get(0);
					}
					if (itemGrowth != null) {
						growth = itemGrowth.get(0);
					}
					if (itemStars != null) {
						stars = itemStars.get(0);
					}
				} else {
					for (Integer c : itemCoins) {
						coins += c;
					}
					for (Integer g : itemGrowth) {
						growth += g;
					}
					for (Integer s : itemStars) {
						stars += s;
					}
				}
				v.getLog().setStar(stars);
				v.getLog().setCoins(coins);
				v.getLog().setGrowth(growth);
				v.getLog().setStatus(UserTaskLogStatus.COMPLETE);
			}
			// 检测学生个人信息完成情况
			if (v.getCode() == 101000001 && v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				// 发送mq及时更新
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", true);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}
			// 新手log任务为空处理
			if (v.getType() == UserTaskType.NEW_USER) {
				this.handleTaskLog(v);
				if (v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
					hasUnCompleteNewUserTask = true;
				}
			}

			if (v.getCode() == 101010002) {
				homeworkTask = v;
				continue;
			}
			if (v.getLog() != null && v.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
				taskList.add(0, v);
			} else {
				taskList.add(v);
			}
		}

		if (!hasUnCompleteNewUserTask) {
			// 没有未完成的新手任务
			Date date = userTaskLogService.getLatestCompleteDate(UserTaskType.NEW_USER, Security.getUserId());
			if (date == null) {
				data.put("showNewUserReward", false);
			} else {
				if (System.currentTimeMillis() - date.getTime() > TimeUnit.HOURS.toMillis(24)) {
					data.put("showNewUserReward", false);
				} else {
					data.put("showNewUserReward", true);
				}
			}
		} else {
			data.put("showNewUserReward", true);
		}
		data.put("userTasks", taskList);

		// 当前的日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format.format(date));
		} catch (Exception e) {
		}

		if (hasMoreTask) {
			// 新手任务中有未领取的数据
			data.put("hasMoreTasks", true);
		} else {
			Long notReceiveTaskCount = userTaskLogService.countNotReceiveTask(UserTaskType.ACHIEVEMENT,
					Security.getUserId());
			if (notReceiveTaskCount != null && notReceiveTaskCount > 0) {
				// 成就任务中有未领取的任务
				data.put("hasMoreTasks", true);
			} else {

				// 判断是否有宝箱未领取
				UserTaskStarLog starLog = starLogService.query(date, Security.getUserId());
				if (starLog == null) {
					data.put("hasMoreTasks", false);
				} else {
					if (starLog.getStar() <= 0) {
						data.put("hasMoreTasks", false);
					} else {
						int star = starLog.getStar();
						String treasureRuleValue = parameterService.get(Product.YOOMATH, "userTask.treasure.rule")
								.getValue();
						List<Integer> canReceiveStarReward = new ArrayList<Integer>(3);
						if (StringUtils.isNotBlank(treasureRuleValue)) {
							JSONObject treasureRuleJson = JSONObject.parseObject(treasureRuleValue);
							JSONArray levelArr = treasureRuleJson.getJSONArray("starLevels");

							for (Object o : levelArr) {
								JSONObject oj = JSONObject.parseObject(o.toString());

								if (star >= oj.getInteger("star")) {
									canReceiveStarReward.add(oj.getInteger("star"));
								}
							}
						}
						List<Integer> receiveStarLogs = null;
						if (StringUtils.isNotBlank(starLog.getContent())) {
							JSONObject jsonObject = JSONObject.parseObject(starLog.getContent());
							JSONArray takeLogArr = jsonObject.getJSONArray("takeLogs");
							receiveStarLogs = new ArrayList<Integer>(takeLogArr.size());

							for (Object o : takeLogArr) {
								JSONObject logObj = JSONObject.parseObject(o.toString());
								receiveStarLogs.add(logObj.getInteger("star"));
							}
						}

						boolean hasUnGet = false;
						if (CollectionUtils.isNotEmpty(canReceiveStarReward)) {
							if (CollectionUtils.isNotEmpty(receiveStarLogs)) {
								for (Integer canReceiveStar : canReceiveStarReward) {
									if (!receiveStarLogs.contains(canReceiveStar)) {
										hasUnGet = true;
										break;
									}
								}
							} else {
								hasUnGet = true;
							}
						}

						data.put("hasMoreTasks", hasUnGet);
					}

				}
			}
		}
		
		UserTaskLog userTaskLog = userTaskLogService.findByCodeAndUser(101010002, Security.getUserId(), date);
		// 若没有需要完成的作业则不需要显示"完成作业"当日任务
		if (CollectionUtils.isNotEmpty((Collection) homeworkMap.get("todo"))
				|| (userTaskLog != null && userTaskLog.getStatus() != UserTaskLogStatus.RECEIVE)) {
			data.put("homeworkTask", homeworkTask);
		}
		
		return new Value(data);
	}
	
	/**
	 * 获取学生端首页的日常任务和作业列表，不包括假期作业
	 * @since 4.0.0
	 * @return  
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/taskAndHomeworkNoHoliday", method = { RequestMethod.GET, RequestMethod.POST })
	public Value taskAndHomeworkNoHoliday() {
		Map<String, Object> data = new HashMap<String, Object>(11);
		
		// 作业
		Map<String, Object> homeworkMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		homeworkMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			todoQuery.setCursorType("startTime");
			todoQuery.setMobileIndex(true);
			CursorPage<Long, Map> todoPage = stuHkService.queryStuHk(todoQuery,
					CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				homeworkMap.put("todo", Collections.EMPTY_LIST);
			} else {
				int todoSize = todoPage.getItemSize();
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
				List<Long> ids = new ArrayList<Long>(todoSize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Map> maps = todoPage.getItems();
				for (Map map : maps) {
					long id = ((BigInteger) map.get("id")).longValue();
					stuHkIds.add(id);
					ids.add(id);
				}
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				homeworkMap.put("todo", items);
			}
		}
		data.put("homework", homeworkMap);
		
		// 是否提示绑定信息
		Account account = accountService.getAccountByUserId(Security.getUserId());
		List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, Security.getAccountId());
		boolean hasBindQQ = false;
		boolean hasBindWX = false;

		for (Credential c : credentials) {
			if (c.getType() == CredentialType.QQ) {
				hasBindQQ = true;
				continue;
			}
			if (c.getType() == CredentialType.WEIXIN) {
				hasBindWX = true;
			}
		}
		data.put("showBindInfo", StringUtils.isEmpty(account.getEmail()) && StringUtils.isEmpty(account.getMobile())
				&& (!hasBindQQ) && (!hasBindWX));
		
		UserTaskQueryForm form = new UserTaskQueryForm();
		UserType userType = Security.getUserType();
		form.setUserType(userType);
		form.setStatus(UserTaskStatus.OPEN);
		User user = userService.get(Security.getUserId());
		form.setScope(user.getUserChannelCode() > 10000 ? UserTaskUserScope.CHANNEL_USER
				: UserTaskUserScope.SELF_REGISTRATION);

		List<UserTask> userTasks = userTaskService.findNotFindFinishUserTask(form, Security.getUserId());
		List<VUserTask> vs = userTaskConvert.to(userTasks, true);

		List<VUserTask> taskList = new ArrayList<VUserTask>(vs.size());

		VUserTask homeworkTask = null;
		boolean hasMoreTask = false, hasUnCompleteNewUserTask = false;
		for (VUserTask v : vs) {
			if (v.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
				hasMoreTask = true;
			}
			// 如果是签到任务显示完成
			if (v.getCode() == 101010001 && v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				Date date = new Date();
				hasMoreTask = true;
				String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
				Date yesterday = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
					yesterday = DateUtils.addDays(date, -1);
				} catch (ParseException e) {
				}
				UserTask userTask = userTaskService.get(v.getCode());
				UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(v.getCode(), Security.getUserId(),
						yesterday);
				int coins = 0, growth = 0, stars = 0;
				UserTaskRuleCfg userTaskRuleCfg = userTask.getUserTaskRuleCfg();
				List<Integer> itemCoins = userTaskRuleCfg.getItemCoins();
				List<Integer> itemStars = userTaskRuleCfg.getItemStar();
				List<Integer> itemGrowth = userTaskRuleCfg.getItemGrowth();
				if (yesterdayLog == null) {
					if (itemCoins != null) {
						coins = itemCoins.get(0);
					}
					if (itemGrowth != null) {
						growth = itemGrowth.get(0);
					}
					if (itemStars != null) {
						stars = itemStars.get(0);
					}
				} else {
					for (Integer c : itemCoins) {
						coins += c;
					}
					for (Integer g : itemGrowth) {
						growth += g;
					}
					for (Integer s : itemStars) {
						stars += s;
					}
				}
				v.getLog().setStar(stars);
				v.getLog().setCoins(coins);
				v.getLog().setGrowth(growth);
				v.getLog().setStatus(UserTaskLogStatus.COMPLETE);
			}
			// 检测学生个人信息完成情况
			if (v.getCode() == 101000001 && v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
				// 发送mq及时更新
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000001);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", true);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}
			// 新手log任务为空处理
			if (v.getType() == UserTaskType.NEW_USER) {
				this.handleTaskLog(v);
				if (v.getLog().getStatus() == UserTaskLogStatus.TASKING) {
					hasUnCompleteNewUserTask = true;
				}
			}

			if (v.getCode() == 101010002) {
				homeworkTask = v;
				continue;
			}
			if (v.getLog() != null && v.getLog().getStatus() == UserTaskLogStatus.COMPLETE) {
				taskList.add(0, v);
			} else {
				taskList.add(v);
			}
		}

		if (!hasUnCompleteNewUserTask) {
			// 没有未完成的新手任务
			Date date = userTaskLogService.getLatestCompleteDate(UserTaskType.NEW_USER, Security.getUserId());
			if (date == null) {
				data.put("showNewUserReward", false);
			} else {
				if (System.currentTimeMillis() - date.getTime() > TimeUnit.HOURS.toMillis(24)) {
					data.put("showNewUserReward", false);
				} else {
					data.put("showNewUserReward", true);
				}
			}
		} else {
			data.put("showNewUserReward", true);
		}
		data.put("userTasks", taskList);

		// 当前的日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format.format(date));
		} catch (Exception e) {
		}

		if (hasMoreTask) {
			// 新手任务中有未领取的数据
			data.put("hasMoreTasks", true);
		} else {
			Long notReceiveTaskCount = userTaskLogService.countNotReceiveTask(UserTaskType.ACHIEVEMENT,
					Security.getUserId());
			if (notReceiveTaskCount != null && notReceiveTaskCount > 0) {
				// 成就任务中有未领取的任务
				data.put("hasMoreTasks", true);
			} else {

				// 判断是否有宝箱未领取
				UserTaskStarLog starLog = starLogService.query(date, Security.getUserId());
				if (starLog == null) {
					data.put("hasMoreTasks", false);
				} else {
					if (starLog.getStar() <= 0) {
						data.put("hasMoreTasks", false);
					} else {
						int star = starLog.getStar();
						String treasureRuleValue = parameterService.get(Product.YOOMATH, "userTask.treasure.rule")
								.getValue();
						List<Integer> canReceiveStarReward = new ArrayList<Integer>(3);
						if (StringUtils.isNotBlank(treasureRuleValue)) {
							JSONObject treasureRuleJson = JSONObject.parseObject(treasureRuleValue);
							JSONArray levelArr = treasureRuleJson.getJSONArray("starLevels");

							for (Object o : levelArr) {
								JSONObject oj = JSONObject.parseObject(o.toString());

								if (star >= oj.getInteger("star")) {
									canReceiveStarReward.add(oj.getInteger("star"));
								}
							}
						}
						List<Integer> receiveStarLogs = null;
						if (StringUtils.isNotBlank(starLog.getContent())) {
							JSONObject jsonObject = JSONObject.parseObject(starLog.getContent());
							JSONArray takeLogArr = jsonObject.getJSONArray("takeLogs");
							receiveStarLogs = new ArrayList<Integer>(takeLogArr.size());

							for (Object o : takeLogArr) {
								JSONObject logObj = JSONObject.parseObject(o.toString());
								receiveStarLogs.add(logObj.getInteger("star"));
							}
						}

						boolean hasUnGet = false;
						if (CollectionUtils.isNotEmpty(canReceiveStarReward)) {
							if (CollectionUtils.isNotEmpty(receiveStarLogs)) {
								for (Integer canReceiveStar : canReceiveStarReward) {
									if (!receiveStarLogs.contains(canReceiveStar)) {
										hasUnGet = true;
										break;
									}
								}
							} else {
								hasUnGet = true;
							}
						}

						data.put("hasMoreTasks", hasUnGet);
					}

				}
			}
		}
		
		UserTaskLog userTaskLog = userTaskLogService.findByCodeAndUser(101010002, Security.getUserId(), date);
		// 若没有需要完成的作业则不需要显示"完成作业"当日任务
		if (CollectionUtils.isNotEmpty((Collection) homeworkMap.get("todo"))
				|| (userTaskLog != null && userTaskLog.getStatus() != UserTaskLogStatus.RECEIVE)) {
			data.put("homeworkTask", homeworkTask);
		}
		
		return new Value(data);
	}
	
	/**
	 * 获取学生端首页的新手入口,成绩报告
	 * @since 4.0.0
	 * @return  
	 */
	@SuppressWarnings({ "unchecked" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/newAndReport", method = { RequestMethod.GET, RequestMethod.POST })
	public Value newAndDaily() {
		Map<String, Object> data = new HashMap<String, Object>(11);
		
		// 学业报告
		// @since v1.4.7 学业报告替换成周成绩报告
		// 缓存直接复用之前的学业报告
		Map<String, Object> report = new HashMap<String, Object>(3);
	
		String cacheFlag = learnReportCacheService.getCurWeekTips(Security.getUserId());
		
		int week = LocalDate.now().getDayOfWeek().getValue();
		String startDate = LocalDate.now().minusWeeks(1).minusDays(week - 1).toString();
		String endDate = LocalDate.now().minusWeeks(1).plusDays(7 - week).toString();
		if ("true".equals(cacheFlag)) {
			report.put("show", false);
		} else {
			StudentWeekReport studentWeekReport = studentWeekReportService.findWeekReport(Security.getUserId(),
					startDate, endDate);
			if ("false".equals(cacheFlag) && studentWeekReport != null) {
				report.put("show", true);
			} else {
				if (studentWeekReport != null) {
					report.put("show", true);
					learnReportCacheService.setCurWeekTips(Security.getUserId(), false);
				} else {
					report.put("show", false);
				}
			}
		}
		report.put("title", getWeekReportTital(startDate, endDate));
		report.put("url", Env.getString("reportStu.h5.url", new Object[] { Security.getUserId() }));
		data.put("report", report);
		
		//新手引导
		Parameter parameter = parameterService.get(Product.YOOMATH, "stu.yoo.know.h5.url");
		List<CoinsLog> userGuideLogs = coinsLogService.findLogs(101000005, Security.getUserId(), null);
		if (CollectionUtils.isEmpty(userGuideLogs)) {
			data.put("learnMoreUrl", parameter == null ? "" : parameter.getValue());
		}

		// 用户任务更多h5 url
		Parameter moreTaskH5Url = parameterService.get(Product.YOOMATH, "stu.userTask.index.url");
		data.put("taskIndexUrl", moreTaskH5Url == null ? "" : moreTaskH5Url.getValue());
		
		return new Value(data);
	}

	/**
	 * 处理新手任务log为空的情况
	 * 
	 * @param userTask
	 *            {@link VUserTask}
	 */
	private void handleTaskLog(VUserTask userTask) {
		if (userTask.getType() == UserTaskType.NEW_USER) {
			int allCount = userTask.getItems() == null ? 0 : userTask.getItems().size();
			if (userTask.getLog() != null && userTask.getLog().getId() != -1) {
				Map<String, Object> map = userTask.getLog().getDetail();
				int doCount = 0;
				if (map != null) {
					List<Object> items = (List<Object>) map.get("items");
					doCount = items.size();
				}
				if (doCount == 0 && map.get("completeAt") != null) {
					doCount = allCount;
				}
				userTask.getLog().setCompleteTitle(doCount + "/" + allCount);
				return;
			}
			// 对个人信息设置（如果个人信息在log里面没拿到值）
			if (userTask.getCode() == 101000001) {
				Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
				// "填写用户名","填写真实姓名","填写性别","填写生日","填写学校","填写入学年份"
				// 用户名必填
				int doCount = 1;
				if (student.getYear() != null && student.getYear() > 0) {
					doCount += 1;
				}
				if (student.getBirthday() != null) {
					doCount += 1;
				}
				if (student.getSex() != null && student.getSex() != Sex.UNKNOWN) {
					doCount += 1;
				}
				if (student.getSchoolId() != null && student.getSchoolId() > 0) {
					doCount += 1;
				}
				if (StringUtils.isNotBlank(student.getName())) {
					doCount += 1;
				}
				userTask.getLog().setCompleteTitle(doCount + "/" + allCount);
				return;
			}
			if (userTask.getLog() != null && userTask.getLog().getCompleteTitle() == null) {
				userTask.getLog().setCompleteTitle("0/" + allCount);
				return;
			}
		}
	}

	private String getWeekReportTital(String startDate, String endDate) {
		if (startDate == null || endDate == null || startDate.length() < 5 || endDate.length() < 5) {
			return null;
		}

		// startDate + "~" + endDate + "周成绩报告"
		StringBuilder builder = new StringBuilder();
		builder.append(startDate.substring(5, startDate.length()).replaceAll("-", "."));
		builder.append("~");
		builder.append(endDate.substring(5, startDate.length()).replaceAll("-", "."));
		builder.append("周成绩报告");

		return builder.toString();
	}
}
