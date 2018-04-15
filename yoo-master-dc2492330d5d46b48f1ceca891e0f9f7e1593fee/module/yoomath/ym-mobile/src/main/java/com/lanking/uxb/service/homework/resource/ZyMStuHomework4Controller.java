package com.lanking.uxb.service.homework.resource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.AppealType;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.correct.api.CorrectHomeworkService;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvertOption;
import com.lanking.uxb.service.homework.form.HomeworkQuestionForm;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.latex.api.LatexService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.QuestionSimilarService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.cache.StudentOperationCacheService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionErrorService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.PullQuestionForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

import httl.util.CollectionUtils;

/**
 * 悠数学移动端(学生作业相关接口)
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/s/hk/4")
public class ZyMStuHomework4Controller {

	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;
	@Autowired
	private QuestionSimilarService questionSimilarService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyCorrectingService correctingService;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private StudentOperationCacheService operationCacheService;
	@Autowired
	private ZyQuestionErrorService zyQuestionErrorService;
	@Autowired
	private QuestionAppealService questionAppealService;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private LatexService latexService;
	@Autowired
	private ZyStudentHomeworkAnswerService zyStuHkAnswerService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private CorrectHomeworkService correctHomeworkService;
	@Autowired
	private CorrectProcessor correctProcessor;

	/**
	 * 作业首页数据接口(历史作业返回已提交未下发的作业),包含寒假作业，兼容用，后续不用
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param historySize
	 *            获取历史记录的条数
	 * @return {@link Value}
	 */
	@Deprecated
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		dataMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			Set<StudentHomeworkStatus> stauts = Sets.newHashSet(StudentHomeworkStatus.SUBMITED,
					StudentHomeworkStatus.ISSUED);
			dataMap.put("historyCount", stuHkService.countAllHomeworks(Security.getUserId(), stauts));
			// 历史作业
			VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
			historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
			historyQuery.setCourse(false);
			historyQuery.setStudentId(Security.getUserId());
			historyQuery.setCursorType("startTime");
			CursorPage<Long, Map> historyPage = stuHkService.queryUnionHolidayStuHk(historyQuery,
					CP.cursor(Long.MAX_VALUE, Math.min(historySize, 20)));
			if (historyPage.isEmpty()) {
				vp.setCursor(Long.MAX_VALUE);
				vp.setItems(Collections.EMPTY_LIST);
			} else {
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
				List<Long> ids = new ArrayList<Long>(historySize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Long> stuHolidayHkIds = Lists.newArrayList();
				List<Map> maps = historyPage.getItems();
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
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				if (stuHolidayHkIds.size() > 0) {
					Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
					// 去掉不需要的initUser
					HolidayStuHomeworkConvertOption option = new HolidayStuHomeworkConvertOption();
					option.setInitUser(false);
					vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map, option)));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				vp.setCursor(historyPage.getNextCursor());
				vp.setItems(items);
			}
			dataMap.put("history", vp);
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			todoQuery.setCursorType("startTime");
			CursorPage<Long, Map> todoPage = stuHkService.queryUnionHolidayStuHk(todoQuery,
					CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				dataMap.put("todo", Collections.EMPTY_LIST);
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
					// 去掉不需要的initUser
					HolidayStuHomeworkConvertOption option = new HolidayStuHomeworkConvertOption();
					option.setInitUser(false);
					vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map, option)));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				dataMap.put("todo", items);
			}
		}
		return new Value(dataMap);
	}

	/**
	 * 作业首页历史作业数据接口(历史作业返回已提交未下发的作业),包含寒假作业，兼容用，后续不用
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@Deprecated
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistory(long cursor, @RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		CursorPage<Long, Map> historyPage = stuHkService.queryUnionHolidayStuHk(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));

		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
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
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
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
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		return new Value(vp);
	}

	/**
	 * 作业首页数据接口(历史作业返回已提交未下发的作业),不包含寒假作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param historySize
	 *            获取历史记录的条数
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "indexHk", method = { RequestMethod.POST, RequestMethod.GET })
	public Value indexHk(@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		dataMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			Set<StudentHomeworkStatus> stauts = Sets.newHashSet(StudentHomeworkStatus.SUBMITED,
					StudentHomeworkStatus.ISSUED);
			dataMap.put("historyCount", stuHkService.countAllHomeworksNoHoliday(Security.getUserId(), stauts));
			// 历史作业
			VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
			historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
			historyQuery.setCourse(false);
			historyQuery.setStudentId(Security.getUserId());
			historyQuery.setCursorType("startTime");
			CursorPage<Long, Map> historyPage = stuHkService.queryStuHk(historyQuery,
					CP.cursor(Long.MAX_VALUE, Math.min(historySize, 20)));
			if (historyPage.isEmpty()) {
				vp.setCursor(Long.MAX_VALUE);
				vp.setItems(Collections.EMPTY_LIST);
			} else {
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
				List<Long> ids = new ArrayList<Long>(historySize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Map> maps = historyPage.getItems();
				for (Map map : maps) {
					long id = ((BigInteger) map.get("id")).longValue();
					stuHkIds.add(id);
					ids.add(id);
				}
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
					stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
					stuHkOption.setInitHomework(true);
					vs.putAll(stuHkConvert.to(map, stuHkOption));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				vp.setCursor(historyPage.getNextCursor());
				vp.setItems(items);
			}
			dataMap.put("history", vp);
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			todoQuery.setCursorType("startTime");
			CursorPage<Long, Map> todoPage = stuHkService.queryStuHk(todoQuery, CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				dataMap.put("todo", Collections.EMPTY_LIST);
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
					StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
					stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
					stuHkOption.setInitHomework(true);
					vs.putAll(stuHkConvert.to(map, stuHkOption));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				dataMap.put("todo", items);
			}
		}
		return new Value(dataMap);
	}

	/**
	 * 作业首页历史作业数据接口(历史作业返回已提交未下发的作业),不包含寒假作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHkHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHkHistory(long cursor,
			@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		CursorPage<Long, Map> historyPage = stuHkService.queryStuHk(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));

		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				long id = ((BigInteger) map.get("id")).longValue();
				stuHkIds.add(id);
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
				stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
				stuHkOption.setInitHomework(true);
				vs.putAll(stuHkConvert.to(map, stuHkOption));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		return new Value(vp);
	}

	/**
	 * 学生端反馈题目错误
	 * 
	 * @since yoomath(mobile) V1.5.4
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questionError", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questionError(@RequestParam(value = "types", required = false) List<QuestionErrorType> types,
			@RequestParam(value = "questionId") Long questionId,
			@RequestParam(value = "description", required = false) String description) {
		zyQuestionErrorService.saveError(description, types, questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 学生端申述题目批改错误
	 * 
	 * @since yoomath(mobile) V1.5.4
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "answerCanComplaint", method = { RequestMethod.POST, RequestMethod.GET })
	public Value answerCanComplaint(@RequestParam(value = "sHkQuestionId") Long sHkQuestionId) {
		StudentHomeworkQuestion question = shqService.get(sHkQuestionId);
		if (question == null) {
			return new Value(new IllegalArgException());
		}
		Date now = new Date();
		// 如果批改时间到现在已经超过168小时，不能申述
		if ((now.getTime() - question.getCorrectAt().getTime()) > 168 * 60 * 60 * 1000) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_ANSWER_APPEAL_EXCEED_TIMES));
		}

		// 如果已经有申述记录了，也不能申述
		QuestionAppeal appeal = questionAppealService.getAppeal(sHkQuestionId);
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_IN_APPEAL));
		} else if (appeal != null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_PROCESSED));
		}

		return new Value();
	}

	/**
	 * 学生端申述题目批改错误
	 * 
	 * @since yoomath(mobile) V1.5.4
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "complaintAnswer", method = { RequestMethod.POST, RequestMethod.GET })
	public Value complaintAnswer(@RequestParam(value = "sHkQuestionId") Long sHkQuestionId,
			@RequestParam(value = "comment") String comment, Integer source) {
		StudentHomeworkQuestion question = shqService.get(sHkQuestionId);
		if (question == null) {
			return new Value(new IllegalArgException());
		}
		Date now = new Date();
		// 如果批改时间到现在已经超过168小时，不能申述
		if ((now.getTime() - question.getCorrectAt().getTime()) > 168 * 60 * 60 * 1000) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_ANSWER_APPEAL_EXCEED_TIMES));
		}

		// 如果已经有申述记录了，也不能申述
		QuestionAppeal appeal = questionAppealService.getAppeal(sHkQuestionId);
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_IN_APPEAL));
		} else if (appeal != null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_QUESTION_ALREADY_PROCESSED));
		}

		questionAppealService.addComment(AppealType.CORRECT_ERROR, sHkQuestionId, source, comment, UserType.STUDENT);
		return new Value();
	}

	/**
	 * 学生端反馈订正题不会做
	 * 
	 * @since yoomath(mobile) V1.5.4
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "noCorrectCommit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value noCorrectCommit(Long sHkQuestionId) {
		if (sHkQuestionId <= 0) {
			return new Value(new IllegalArgException());
		}

		StudentHomeworkQuestion sHkQuestion = shqService.get(sHkQuestionId);
		// 如果已经订正，直接返回
		if (sHkQuestion.isRevised()) {
			return new Value();
		}

		// 修改订正状态为已订正
		shqService.updateReviseStatus(sHkQuestionId);

		// 调用新的批改流程
		correctProcessor.afterStudentCorrectQuestion(sHkQuestionId, false);

		return new Value();
	}

	/**
	 * 学生端订正题提交
	 * 
	 * @since yoomath(mobile) V1.5.4
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "correctCommit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value correctCommit(HomeworkQuestionForm form) {
		if (form == null || form.getType() != 1 || form.getStuHkId() <= 0 || form.getQuestionId() <= 0
				|| form.getStuHkQuestionId() <= 0) {
			return new Value(new IllegalArgException());
		}

		StudentHomework studentHomework = studentHomeworkService.get(form.getStuHkId());
		// 请求的学生作业不存在或者是此份作业不属于此学生
		if (studentHomework == null || studentHomework.getStudentId() != Security.getUserId()) {
			throw new IllegalArgException();
		}

		long stuHkQuestionId = form.getStuHkQuestionId();
		StudentHomeworkQuestion sHkQuestion = shqService.get(stuHkQuestionId);
		// 如果已经订正，直接返回
		if (sHkQuestion.isRevised()) {
			return new Value();
		}

		// 对应的答案Map StudentHomeworkQuestion.id -> 答案列表
		Map<Long, List<String>> asciiQuestionMathMap = new HashMap<Long, List<String>>(1);
		Map<Long, List<String>> latexQuestionMathMap = new HashMap<Long, List<String>>(1);
		// 处理学生答题中的图片
		Map<Long, List<Long>> solvingImgs = new HashMap<Long, List<Long>>(1);

		List<Long> questionIds = new ArrayList<Long>(1);
		questionIds.add(form.getQuestionId());
		solvingImgs.put(form.getStuHkQuestionId(), form.getImages());

		Map<Long, Question> questionMap = questionService.mget(questionIds);
		// 组成StudentHomeworkQuestion.id -> Question.Type的Map
		Map<Long, Type> questionTypeMap = new HashMap<Long, Type>(questionMap.size());

		// 处理学生提交的答案
		Question q = questionMap.get(form.getQuestionId());
		questionTypeMap.put(stuHkQuestionId, q.getType());

		if (StringUtils.isBlank(form.getAsciimathAnswers())) {
			asciiQuestionMathMap.put(stuHkQuestionId, Collections.EMPTY_LIST);
		} else {
			List<String> answerContents = JSONArray.parseArray(form.getAsciimathAnswers(), String.class);
			if (q.getType() == Type.FILL_BLANK) {
				List<String> tmpAnswerContents = new ArrayList<String>(answerContents.size());
				for (String answerContent : answerContents) {
					answerContent = "<ux-mth>" + answerContent + "</ux-mth>";
					tmpAnswerContents.add(answerContent);
				}
				asciiQuestionMathMap.put(stuHkQuestionId, tmpAnswerContents);

				if (asciiQuestionMathMap.get(stuHkQuestionId).size() != q.getAnswerNumber()) {
					throw new IllegalArgException();
				}

				String mmlMathStr = form.getMathmlAnswers();
				if (StringUtils.isBlank(mmlMathStr)) {
					latexQuestionMathMap.put(stuHkQuestionId, Collections.EMPTY_LIST);
				} else {
					JSONArray mmlArray = JSONArray.parseArray(mmlMathStr);
					List<String> mmls = new ArrayList<String>(mmlArray.size());
					for (int i = 0; i < mmlArray.size(); i++) {
						mmls.add(mmlArray.getString(i));
					}

					List<String> latexs = latexService.multiMml2Latex(mmls);

					latexQuestionMathMap.put(stuHkQuestionId, latexs);
				}
			} else {
				asciiQuestionMathMap.put(stuHkQuestionId, answerContents);
				latexQuestionMathMap.put(stuHkQuestionId, answerContents);
			}
		}

		zyStuHkAnswerService.doQuestion(latexQuestionMathMap, asciiQuestionMathMap, solvingImgs, questionTypeMap,
				Security.getUserId(), null, null);

		// 修改订正状态为已订正
		shqService.updateReviseStatus(stuHkQuestionId);

		// 调用新的批改流程
		correctProcessor.afterStudentCorrectQuestion(stuHkQuestionId, true);

		return new Value();

	}

	/**
	 * 作业中错题针对性练习
	 * 
	 * @since yoomath(mobile) V1.4.8
	 * @param questionIds
	 *            错题id
	 * @param pulledIds
	 *            学生作业id
	 * @return {@link Value}
	 */
	@MemberAllowed(memberType = "VIP")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "pertinenceFallible", method = { RequestMethod.POST, RequestMethod.GET })
	public Value pertinenceFallible(@RequestParam(value = "questionIds") List<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> data = new HashMap<>();
		Map<Long, QuestionSimilar> similars = questionSimilarService.mGetByQuestion(questionIds);
		List<Long> cacheIds = operationCacheService.getHomeworkFalliblePulledIds(Security.getUserId());

		// 没有相似题按照知识点推荐
		if (similars == null || similars.isEmpty()) {
			// 推荐数量
			int pullSize = questionIds.size() >= 5 ? questionIds.size() : questionIds.size() * 2;
			// 客户端限制最大25题
			if (pullSize > 25) {
				pullSize = 25;
			}
			List<Long> pullIds = getPullQuestionsByKnowledgeCode(questionIds, pullSize, cacheIds);

			List<Question> questions = questionService.mgetList(pullIds);
			int predictTime = 0;
			double difficulty = 0;
			for (Question q : questions) {
				predictTime += questionService.calPredictTime(q);
				difficulty += q.getDifficulty();
			}
			data.put("predictTime", predictTime);

			if (CollectionUtils.isNotEmpty(questions)) {
				BigDecimal difficultyDecimal = new BigDecimal(difficulty / questions.size())
						.setScale(BigDecimal.ROUND_HALF_UP, 2);
				data.put("difficulty", difficultyDecimal.doubleValue());
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnswer(true);
				option.setAnalysis(true);
				option.setCollect(true);
				data.put("questions", questionConvert.to(questions, option));
			}
		} else {
			List<Question> questions = Lists.newArrayList();
			questions = getPullQuestions(questionIds, similars, cacheIds);

			// 没有相似题从知识点取
			if (CollectionUtils.isEmpty(questions)) {
				// 推荐数量
				int pullSize = questionIds.size() >= 5 ? questionIds.size() : questionIds.size() * 2;
				// 客户端限制最大25题
				if (pullSize > 25) {
					pullSize = 25;
				}
				List<Long> pullIds = getPullQuestionsByKnowledgeCode(questionIds, pullSize, cacheIds);
				questions = questionService.mgetList(pullIds);
			}

			// 客户端限制最大25题
			questions = questions.stream().limit(25).collect(Collectors.toList());

			int predictTime = 0;
			double difficulty = 0;
			for (Question q : questions) {
				predictTime += questionService.calPredictTime(q);
				difficulty += q.getDifficulty();
			}
			data.put("predictTime", predictTime);

			if (CollectionUtils.isNotEmpty(questions)) {
				BigDecimal difficultyDecimal = new BigDecimal(difficulty / questions.size())
						.setScale(BigDecimal.ROUND_HALF_UP, 2);
				data.put("difficulty", difficultyDecimal.doubleValue());
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnswer(true);
				option.setAnalysis(true);
				option.setCollect(true);
				data.put("questions", questionConvert.to(questions, option));

				// 记录缓存
				List<Long> ids = questions.stream().map(p -> p.getId()).collect(Collectors.toList());
				operationCacheService.setHomeworkFalliblePulledIds(Security.getUserId(), ids);
			}
		}

		return new Value(data);
	}

	/**
	 * 根据题目推荐相似题
	 * 
	 * @param questionIds
	 * @param similars
	 * @param cacheIds
	 *            缓存中取的推荐题目id
	 */
	private List<Question> getPullQuestions(List<Long> questionIds, Map<Long, QuestionSimilar> similars,
			List<Long> cacheIds) {
		// 1.删掉所有包含原题目的相似题
		// 2.相似题列表按照难度重新排序，升序排列
		List<Question> similarQuestions = Lists.newArrayList();

		int singleSize = 0;
		if (questionIds.size() >= 5) {
			singleSize = 1;
		} else {
			singleSize = 2;
		}
		int totalSize = singleSize * questionIds.size();

		int pulledSize = 0; // 已经推荐的数量
		List<Long> similarQuestionNotRepeatIds = Lists.newArrayList(); // 防止重复题
		List<Question> allSimilars = Lists.newArrayList(); // 所有满足规则的推荐题目
		List<Question> cacheSameQuestions = Lists.newArrayList(); // 和缓存相同的推荐题目

		for (Map.Entry<Long, QuestionSimilar> map : similars.entrySet()) {
			List<Long> similarQuestionIds = map.getValue().getLikeQuestions();

			Map<String, List<Question>> questionMap = pullSingleQuestion(singleSize, questionIds, similarQuestionIds,
					similarQuestionNotRepeatIds, cacheIds);
			if (!questionMap.isEmpty()) {
				List<Question> questions = questionMap.get("similarQuestions");
				List<Question> addSimilarQuestions = questionMap.get("allSimilarQuestions");
				List<Question> cache = questionMap.get("cacheSameQuestions");
				// 推荐的相似题
				// 处理重复题
				if (CollectionUtils.isNotEmpty(questions)) {
					for (Question q : questions) {
						if (!similarQuestionNotRepeatIds.contains(q.getId())) {
							similarQuestions.add(q);
							similarQuestionNotRepeatIds.add(q.getId());
							pulledSize++;
						}
					}
				}
				// 记录所有的相似题
				if (CollectionUtils.isNotEmpty(addSimilarQuestions)) {
					allSimilars.addAll(addSimilarQuestions);
				}
				// 记录缓存题目
				if (CollectionUtils.isNotEmpty(cache)) {
					cacheSameQuestions.addAll(cache);
				}
			}
		}

		// 补足题目
		int margin = totalSize - pulledSize;
		if (margin > 0) {
			randomSimilarQuestions(allSimilars, similarQuestions, margin);
		}

		// 从缓存中取推荐题目
		margin = totalSize - similarQuestions.size();
		if (margin > 0) {
			randomSimilarQuestions(cacheSameQuestions, similarQuestions, margin);
		}

		return similarQuestions;
	}

	/**
	 * 根据每一题推荐相似题
	 * 
	 * @param singleCount
	 *            推荐数量
	 * @param questionIds
	 *            基础题目id
	 * @param similarQuestionIds
	 *            相似题id
	 * @param pulledQuestionIds
	 *            已经推荐的题目id
	 * @param cacheIds
	 *            缓存中的推荐题目
	 */
	private Map<String, List<Question>> pullSingleQuestion(int singleCount, List<Long> questionIds,
			List<Long> similarQuestionIds, List<Long> pulledQuestionIds, List<Long> cacheIds) {
		Map<String, List<Question>> data = new HashMap<>();
		List<Question> similarQuestions = Lists.newArrayList();
		List<Long> sameCacheIds = Lists.newArrayList();
		if (CollectionUtils.isEmpty(similarQuestionIds)) {
			return data;
		}

		// 过滤相似题包含原题目的
		for (Long value : questionIds) {
			if (similarQuestionIds.contains(value)) {
				similarQuestionIds.remove(value);
			}
		}
		// 过滤已推荐过的题目
		for (Long value : pulledQuestionIds) {
			if (similarQuestionIds.contains(value)) {
				similarQuestionIds.remove(value);
			}
		}
		// 过滤和缓存相同的题目
		for (Long value : cacheIds) {
			if (similarQuestionIds.contains(value)) {
				similarQuestionIds.remove(value);
				sameCacheIds.add(value);
			}
		}
		data.put("cacheSameQuestions", questionService.mgetList(sameCacheIds));

		List<Question> questions = questionService.mgetList(similarQuestionIds);
		if (CollectionUtils.isEmpty(questions)) {
			return data;
		}
		// 只要选择题
		questions = questions.stream().filter(q -> q.getType() == Type.SINGLE_CHOICE).collect(Collectors.toList());
		// 过滤校验通过的题目
		questions = questions.stream().filter(q -> q.getStatus() == CheckStatus.PASS).collect(Collectors.toList());
		// 只取公共题库
		questions = questions.stream().filter(q -> q.getSchoolId() == 0L).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(questions)) {
			return data;
		}

		// 当前相似题数量<=推荐数量,直接全部返回
		if (questions.size() <= singleCount) {
			data.put("similarQuestions", questions);
			data.put("allSimilarQuestions", questions);
			return data;
		}

		// 按照难度排序,规则为[0-0.4):hard,[0.4-0.8):improve,[0.8-1]:basic
		List<Question> hardQuestions = Lists.newArrayList();
		List<Question> improveQuestions = Lists.newArrayList();
		List<Question> basicQuestions = Lists.newArrayList();
		for (Question value : questions) {
			if (value.getDifficulty().doubleValue() >= 0 && value.getDifficulty().doubleValue() < 0.4D) {
				hardQuestions.add(value);
			} else if (value.getDifficulty().doubleValue() >= 0.4D && value.getDifficulty().doubleValue() < 0.8D) {
				improveQuestions.add(value);
			} else if (value.getDifficulty().doubleValue() >= 0.8D && value.getDifficulty().doubleValue() <= 1D) {
				basicQuestions.add(value);
			}
		}

		// 推荐按照简单->困难的顺序,随机推荐
		// 如果相似题数量小于等于取的数量
		int pullSingleCount = 0; // 计数器
		Random random = new Random();
		List<Long> similarIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(basicQuestions)) {
			while (pullSingleCount < singleCount) {
				if (basicQuestions.size() == 0) {
					break;
				}
				int index = random.nextInt(basicQuestions.size());
				Question q = basicQuestions.get(index);
				if (!similarIds.contains(q.getId())) {
					similarQuestions.add(q);
					similarIds.add(q.getId());
					pullSingleCount++;
				}

				basicQuestions.remove(index);
			}
		}
		if (pullSingleCount < singleCount && CollectionUtils.isNotEmpty(improveQuestions)) {
			while (pullSingleCount < singleCount) {
				if (improveQuestions.size() == 0) {
					break;
				}
				int index = random.nextInt(improveQuestions.size());
				Question q = improveQuestions.get(index);
				if (!similarIds.contains(q.getId())) {
					similarQuestions.add(q);
					similarIds.add(q.getId());
					pullSingleCount++;
				}

				improveQuestions.remove(index);
			}
		}
		if (pullSingleCount < singleCount && CollectionUtils.isNotEmpty(hardQuestions)) {
			while (pullSingleCount < singleCount) {
				if (hardQuestions.size() == 0) {
					break;
				}
				int index = random.nextInt(hardQuestions.size());
				Question q = hardQuestions.get(index);
				if (!similarIds.contains(q.getId())) {
					similarQuestions.add(q);
					similarIds.add(q.getId());
					pullSingleCount++;
				}

				hardQuestions.remove(index);
			}
		}

		data.put("similarQuestions", similarQuestions);
		data.put("allSimilarQuestions", questions);

		return data;
	}

	/**
	 * 根据知识点推荐题目
	 * 
	 * @param questionIds
	 * @param knowledgePointList
	 * @param size
	 * @param cacheIds
	 *            排除缓存中的题目
	 */
	private List<Long> getPullQuestionsByKnowledgeCode(List<Long> questionIds, int size, List<Long> cacheIds) {
		// 根据题目ID查知识点
		Map<Long, List<Long>> kpCodeMap = questionKnowledgeService.mgetByQuestions(questionIds);
		Set<Long> knowledgePointCodes = Sets.newHashSet();
		for (Entry<Long, List<Long>> entry : kpCodeMap.entrySet()) {
			for (Long code : entry.getValue()) {
				knowledgePointCodes.add(code);
			}
		}

		Map<Long, KnowledgePoint> knowledgePointMap = knowledgePointService.mget(knowledgePointCodes);
		List<Long> knowledgePointList = Lists.newArrayList();
		for (Map.Entry<Long, KnowledgePoint> value : knowledgePointMap.entrySet()) {
			knowledgePointList.add(value.getKey());
		}

		// 根据知识点推荐题目
		List<Long> excludes = Lists.newArrayList(); // 需要排除的题目
		excludes.addAll(questionIds);
		if (CollectionUtils.isNotEmpty(cacheIds)) {
			excludes.addAll(cacheIds);
		}
		PullQuestionForm form = new PullQuestionForm();
		form.setKnowledgePoints(knowledgePointList);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			form.setqIds(excludes);
		}
		form.setCount(size);
		form.setType(PullQuestionType.KNOWPOINT_ENHANCE_EXERCISE);

		return pullQuestionService.pull(form);
	}

	/**
	 * 取出的题目数量不满足时补足题目, 随机取
	 * 
	 * @param allQuestions
	 * @param pulledQuestions
	 * @return
	 */
	private List<Question> randomSimilarQuestions(List<Question> allQuestions, List<Question> pulledQuestions,
			int size) {
		int count = 0;
		List<Long> randomIds = Lists.newArrayList();
		for (Question all : allQuestions) {
			if (CollectionUtils.isEmpty(pulledQuestions)) {
				randomIds.add(all.getId());
			} else {
				for (Question s : pulledQuestions) {
					if (s.getId().longValue() != all.getId().longValue()) {
						randomIds.add(all.getId());
						break;
					}
				}
			}
		}

		// 随机
		Random random = new Random();
		if (CollectionUtils.isNotEmpty(randomIds)) {
			while (count < size) {
				if (randomIds.size() == 0) {
					break;
				}

				int index = random.nextInt(randomIds.size());
				Long id = randomIds.get(index);
				Question q = new Question();
				for (Question value : allQuestions) {
					if (value.getId().longValue() == id.longValue()) {
						q = value;
						break;
					}
				}

				boolean repeatFlag = false;
				for (Question s : pulledQuestions) {
					if (s.getId().longValue() == q.getId().longValue()) {
						repeatFlag = true;
						break;
					}
				}

				if (!repeatFlag) {
					pulledQuestions.add(q);
					count++;
				}

				randomIds.remove(index);
			}
		}

		return pulledQuestions;
	}

	/**
	 * 提交错题针对性练习
	 * 
	 * @since yoomath(mobile) V1.4.8
	 * @return {@link Value}
	 */
	@MemberAllowed(memberType = "VIP")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "fallibleCommit")
	public Value fallibleCommit(ExerciseCommitForm form) {
		if (CollectionUtils.isEmpty(form.getqIds()) || CollectionUtils.isEmpty(form.getAnswerList())
				|| form.getqIds().size() != form.getCount() || form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}

		List<Map<String, Object>> results = correctingService.simpleCorrect(form.getqIds(), form.getAnswerList());

		VExerciseResult result = new VExerciseResult(2);
		result.setqIds(form.getqIds());
		List<HomeworkAnswerResult> rets = new ArrayList<HomeworkAnswerResult>(form.getCount());
		List<Boolean> dones = new ArrayList<Boolean>(form.getCount());
		int rightCount = 0, getGrowth = 0, earnCoin = 0;
		GrowthLog honorGrowthLog = null;
		for (Map<String, Object> map : results) {
			HomeworkAnswerResult ret = (HomeworkAnswerResult) map.get("result");
			boolean done = (Boolean) map.get("done");
			rets.add(ret);
			if (ret == HomeworkAnswerResult.RIGHT) {
				rightCount++;
			}
			dones.add(done);

			if (done) {
				GrowthLog growthLog = growthService.grow(GrowthAction.DOING_DAILY_EXERCISE, Security.getUserId(), true);
				coinsService.earn(CoinsAction.DOING_DAILY_EXERCISE, Security.getUserId());

				if (growthLog.getHonor() != null) {
					getGrowth++;
					earnCoin++;

					honorGrowthLog = growthLog;
				}
			}
		}
		result.setDones(dones);
		result.setRightRate(
				BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		result.setResults(rets);
		// 记录答案、统计学情、记录错题
		zyStuQaService.asynCreate(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
				null, null, null, rets, StudentQuestionAnswerSource.HOMEWORK_FALLIBLE_PRACTICE, new Date());

		if (honorGrowthLog != null) {
			VUserReward userReward = new VUserReward(honorGrowthLog.getHonor().getUpRewardCoins(),
					honorGrowthLog.getHonor().isUpgrade(), honorGrowthLog.getHonor().getLevel(), getGrowth, earnCoin);
			result.setUserReward(userReward);
		}

		// 薄弱知识点练习，题量任务
		JSONObject obj = new JSONObject();
		obj.put("taskCode", 101020016);
		obj.put("userId", Security.getUserId());
		obj.put("isClient", Security.isClient());
		Map<String, Object> pms = new HashMap<String, Object>(1);
		pms.put("questionCount", form.getCount());
		obj.put("params", pms);
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(obj).build());
		return new Value(result);
	}
}
